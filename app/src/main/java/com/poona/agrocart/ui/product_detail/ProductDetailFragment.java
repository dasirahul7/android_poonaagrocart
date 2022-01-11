package com.poona.agrocart.ui.product_detail;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.app.AppUtils;
import com.poona.agrocart.data.shared_preferences.AppSharedPreferences;
import com.poona.agrocart.databinding.FragmentProductDetailBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.basket_detail.model.Subscription;
import com.poona.agrocart.ui.home.HomeActivity;
import com.poona.agrocart.ui.home.adapter.OfferProductListAdapter;
import com.poona.agrocart.ui.home.model.Product;
import com.poona.agrocart.ui.nav_favourites.FavouriteItem;
import com.poona.agrocart.ui.product_detail.adapter.BasketContentsAdapter;
import com.poona.agrocart.ui.product_detail.adapter.ProductCommentsAdapter;
import com.poona.agrocart.ui.product_detail.adapter.WeightAdapter;
import com.poona.agrocart.ui.product_detail.model.BasketContent;
import com.poona.agrocart.ui.product_detail.model.ProductComment;
import com.poona.agrocart.ui.product_detail.model.ProductDetail;
import com.poona.agrocart.widgets.CustomTextView;
import com.poona.agrocart.widgets.ExpandIconView;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

public class ProductDetailFragment extends BaseFragment implements View.OnClickListener {
    public int count = 0;
    private FragmentProductDetailBinding fragmentProductDetailBinding;
    private ProductDetailViewModel productDetailViewModel;
    private ProductImagesAdapter productImagesAdapter;
    private boolean isProductDetailsVisible = true, isNutritionDetailsVisible = true, isAboutThisProductVisible = true,
            isBasketContentsVisible = true, isBenefitsVisible = true, isStorageVisible = true, isOtherProductInfo = true,
            isVariableWtPolicyVisible = true, isFavourite = false;
    private int quantity = 1;
    public ViewPager vpImages;
    private DotsIndicator dotsIndicator;
    private RecyclerView rvProductComment;
    private LinearLayoutManager linearLayoutManager;
    private ProductCommentsAdapter productCommentsAdapter;
    private ArrayList<ProductComment> commentArrayList;
    private ArrayList<BasketContent> basketContentArrayList;
    private ArrayList<Product> similarProducts;
    private BasketContentsAdapter basketContentsAdapter;
    private ProductDetail details;
    private OfferProductListAdapter productListAdapter;
    private View root;
    private boolean BasketType = false;
    private Calendar calendar;
    private int mYear, mMonth, mDay;
    private ImageView txtOrganic;
    private CustomTextView txtBrand;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentProductDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_detail, container, false);
        fragmentProductDetailBinding.setLifecycleOwner(this);
        root = fragmentProductDetailBinding.getRoot();
        initTitleWithBackBtn("");
        initView();
        setSimilarItems();

        return root;
    }

    private void setSimilarItems() {
        productDetailViewModel.getSimilarProductLiveData().observe(getViewLifecycleOwner(), similarItems -> {
            this.similarProducts = similarItems;
            LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
            productListAdapter = new OfferProductListAdapter(this.similarProducts, requireActivity(), root);
            fragmentProductDetailBinding.rvSimilar.setNestedScrollingEnabled(false);
            fragmentProductDetailBinding.rvSimilar.setLayoutManager(layoutManager);
            fragmentProductDetailBinding.rvSimilar.setAdapter(productListAdapter);
            productListAdapter.setOnProductClick(product -> {
                toProductDetail(product, root);
            });
        });
    }

    private void initView() {
        ((HomeActivity) requireActivity()).binding.appBarHome.rlProductTag.setVisibility(View.VISIBLE);
        txtOrganic = ((HomeActivity) requireActivity()).binding.appBarHome.txtOrganic;
        txtBrand = ((HomeActivity) requireActivity()).binding.appBarHome.tvBrand;
        try {
            if (getArguments() != null) {
                if (getArguments().get("Product").equals("BasketDetail")) {
                    BasketType = true;
                    setProductList();
                    makeItBasketDetails();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragmentProductDetailBinding.llProductDetails.setOnClickListener(this);
        fragmentProductDetailBinding.llNutritions.setOnClickListener(this);
        fragmentProductDetailBinding.llAboutThisProduct.setOnClickListener(this);
        fragmentProductDetailBinding.llBenefits.setOnClickListener(this);
        fragmentProductDetailBinding.llStorageAndUse.setOnClickListener(this);
        fragmentProductDetailBinding.llOtherProductInfo.setOnClickListener(this);
        fragmentProductDetailBinding.llVariableWeightPolicy.setOnClickListener(this);
        fragmentProductDetailBinding.ivPlus.setOnClickListener(this);
        fragmentProductDetailBinding.ivMinus.setOnClickListener(this);
        fragmentProductDetailBinding.ivFavourite.setOnClickListener(this);
        // BasketDetail Product views
        fragmentProductDetailBinding.layoutAdded.llProductList.setOnClickListener(this);
        fragmentProductDetailBinding.layoutAdded.imgPlus.setOnClickListener(this);
        fragmentProductDetailBinding.layoutAdded.imgMinus.setOnClickListener(this);
        fragmentProductDetailBinding.layoutAdded.tvStartDate.setOnClickListener(this);


        vpImages = fragmentProductDetailBinding.vpProductImages;
        dotsIndicator = fragmentProductDetailBinding.dotsIndicator;
        rvProductComment = fragmentProductDetailBinding.rvProductComment;

        setValues();


    }

    private void setProductList() {
        basketContentArrayList = new ArrayList<>();
        prepareListingData();

        linearLayoutManager = new LinearLayoutManager(requireContext());
        fragmentProductDetailBinding.layoutAdded.rvBasketContents.setHasFixedSize(true);
        fragmentProductDetailBinding.layoutAdded.rvBasketContents.setLayoutManager(linearLayoutManager);

        basketContentsAdapter = new BasketContentsAdapter(basketContentArrayList);
        fragmentProductDetailBinding.layoutAdded.rvBasketContents.setAdapter(basketContentsAdapter);
    }

    private void prepareListingData() {
        for (int i = 0; i < 4; i++) {
            BasketContent basketContent = new BasketContent();
            basketContent.setContentName(getString(R.string.moong));
            basketContent.setContentWeight(getString(R.string._500gm));
            basketContentArrayList.add(basketContent);
        }
    }

    private void makeItBasketDetails() {
        hideOrShowProductDetails();
        ((HomeActivity) requireActivity()).binding.appBarHome.rlProductTag.setVisibility(View.GONE);
        fragmentProductDetailBinding.txtItemOffer.setVisibility(View.INVISIBLE);
        fragmentProductDetailBinding.cardOfferMsg.setVisibility(View.GONE);
        fragmentProductDetailBinding.tvLocation.setVisibility(View.GONE);
        fragmentProductDetailBinding.rvWeights.setVisibility(View.GONE);
        fragmentProductDetailBinding.inSubscription.setVisibility(View.VISIBLE);
        fragmentProductDetailBinding.tvOneTimeRate.setVisibility(View.VISIBLE);
    }

    private void setValues() {
        details = new ProductDetail();
        ProductDetail productDetail = new ProductDetail();
        productDetail.setProductName(getArguments() != null ? getArguments().getString("name") : null);
        ArrayList<String> images = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            images.add(getArguments() != null ? getArguments().getString("image") : null);
        // Set Product Details here
        productDetail.setProductLocation("Pune");
        productDetail.setPrice(getArguments() != null ? getArguments().getString("price") : null);
        productDetail.setBrand(getArguments() != null ? getArguments().getString("brand") : null);
        productDetail.setOrganic(getArguments() != null ? getArguments().getBoolean("organic") : null);
        productDetail.setQuantity(getArguments() != null ? getArguments().getString("quantity") : null);
        productDetail.setBasket(getArguments() != null ? getArguments().getBoolean("isInBasket") : null);
        productDetail.setOfferPrice("Rs. 200");
        productDetail.setOfferValue("24% Off");
        productDetail.setProductOfferMsg(getString(R.string.product_offer_msg));
        productDetail.setProductImages(images);
        ArrayList<String> weight = new ArrayList<>();
        weight.add("1kg");
        weight.add("250gms");
        weight.add("500gms");
        weight.add("1pc");
        productDetail.setWeightOfProduct(weight);
        productDetail.setProductDetailBrief(getString(R.string.apples_are_nutritious_apples_may_be_good_for_weight_loss_apples_may_be_good_for_your_heart_as_part_of_a_healtful_and_varied_diet));
        productDetail.setNutritionDetailBrief(getString(R.string.apples_are_nutritious_apples_may_be_good_for_weight_loss_apples_may_be_good_for_your_heart_as_part_of_a_healtful_and_varied_diet));
        //setSubscription
        // Add Subscription Details
        ArrayList<String> subType = new ArrayList<>();
        subType.add("Daily");
        subType.add("Weekly");
        subType.add("Monthly");
        Subscription subscription = new Subscription("Rs. 1500", "per basket", "Special rate for subscription", subType, "1", "15 Dec 2021", "Rs. 1400 X 5 ", "Rs. 7000");
        productDetail.setSubscription(subscription);
        // Set All Comments Here
        ArrayList<ProductComment> commentArrayList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ProductComment comment = new ProductComment();
            comment.setUserImg("data:image/png;base64,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");
//            comment.setUserImg("https://www.linkpicture.com/q/pngfuel-1-1.png");
            comment.setUserName(getString(R.string.johnson_doe));
            comment.setDate(getString(R.string._12_jan_2021));
            comment.setRating(2.5f);
            comment.setComment(getString(R.string.lorem_ipsum_dolor_sit_amet_consectetur_adipiscing));
            commentArrayList.add(comment);
        }
        productDetail.setCommentList(commentArrayList);
        productDetailViewModel = new ViewModelProvider(this).get(ProductDetailViewModel.class);
        fragmentProductDetailBinding.setProductDetailViewModel(productDetailViewModel);
        productDetailViewModel.productDetailMutableLiveData.setValue(productDetail);
        details = productDetailViewModel.productDetailMutableLiveData.getValue();

        setViewPagerAdapterItems();
        setCommentsAdapter();
        assert details != null;
        WeightAdapter weightAdapter = new WeightAdapter(details.getWeightOfProduct(), getActivity());
        fragmentProductDetailBinding.rvWeights.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        fragmentProductDetailBinding.rvWeights.setHasFixedSize(true);
        fragmentProductDetailBinding.rvWeights.setAdapter(weightAdapter);
        //set the top Tags
        if (details.isOrganic())
            txtOrganic.setVisibility(View.VISIBLE);
        else txtOrganic.setVisibility(View.GONE);
        if (details.getBrand() != null)
            txtBrand.setText(MessageFormat.format("Brand-{0}", details.getBrand()));
        else txtBrand.setVisibility(View.INVISIBLE);
        if (details.getQuantity() != null && details.getQuantity().equals("0"))
            outOfStockView();
        if (details.getQuantity() != null && details.getBasket()) {
            fragmentProductDetailBinding.ivMinus.setVisibility(View.VISIBLE);
            fragmentProductDetailBinding.etQuantity.setVisibility(View.VISIBLE);
        }

        //hide all expanded views initially
//        show Subscription
        setSubscription();
        hideOrShowAboutThisProduct();
        hideOrShowBenefits();
        hideOrShowStorageAndUses();
        hideOrShowOtherProductInfo();
        hideOrShowVariableWeightPolicy();
        hideOrShowNutritionDetails();

    }

    private void outOfStockView() {
        fragmentProductDetailBinding.llOutOfStock.setVisibility(View.VISIBLE);
        fragmentProductDetailBinding.tvLocation.setVisibility(View.GONE);
        fragmentProductDetailBinding.rvWeights.setVisibility(View.GONE);
        fragmentProductDetailBinding.llPriceAndQty.setVisibility(View.GONE);
        fragmentProductDetailBinding.cardOfferMsg.setVisibility(View.GONE);

    }

    private void setSubscription() {
        Subscription subscription = details.getSubscription();
        fragmentProductDetailBinding.layoutAdded.tvSubPrice.setText(subscription.getSubPrice());
        fragmentProductDetailBinding.layoutAdded.tvSubUnit.setText(subscription.getSubUnit());
        fragmentProductDetailBinding.layoutAdded.tvSubQty.setText(subscription.getSubQty());
        fragmentProductDetailBinding.layoutAdded.tvSubUnitPrice.setText(subscription.getSubUnitPrice());
        fragmentProductDetailBinding.layoutAdded.tvSubAmount.setText(subscription.getSubTotalAmount());
        fragmentProductDetailBinding.layoutAdded.tvSubMsg.setText(subscription.getSubMsg());
        RecyclerView rvType = fragmentProductDetailBinding.layoutAdded.rvSubType;
        WeightAdapter subTypeAdapter = new WeightAdapter(details.getSubscription().getSubTypes(), getActivity());
        rvType.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvType.setHasFixedSize(true);
        rvType.setAdapter(subTypeAdapter);
    }

    private void setCommentsAdapter() {
        commentArrayList = new ArrayList<>();
        commentArrayList = details.getCommentList();
        linearLayoutManager = new LinearLayoutManager(requireContext());
        rvProductComment.setHasFixedSize(true);
        rvProductComment.setLayoutManager(linearLayoutManager);
        rvProductComment.setNestedScrollingEnabled(false);
        productCommentsAdapter = new ProductCommentsAdapter(commentArrayList, false);
        rvProductComment.setAdapter(productCommentsAdapter);
    }

    private void setViewPagerAdapterItems() {

        count = details.getProductImages().size();
        productImagesAdapter = new ProductImagesAdapter(ProductDetailFragment.this,
                getChildFragmentManager(), details.getProductImages());
        vpImages.setAdapter(productImagesAdapter);
        productImagesAdapter.notifyDataSetChanged();
        vpImages.addOnPageChangeListener(productImagesAdapter);
        dotsIndicator.setViewPager(vpImages);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_product_list:
                hideOrShowBasketContentsList();
                break;
            case R.id.ll_product_details:
                hideOrShowProductDetails();
                break;
            case R.id.ll_about_this_product:
                hideOrShowAboutThisProduct();
                break;
            case R.id.ll_benefits:
                hideOrShowBenefits();
                break;
            case R.id.ll_storage_and_use:
                hideOrShowStorageAndUses();
                break;
            case R.id.ll_other_product_info:
                hideOrShowOtherProductInfo();
                break;
            case R.id.ll_variable_weight_policy:
                hideOrShowVariableWeightPolicy();
                break;
            case R.id.ll_nutritions:
                hideOrShowNutritionDetails();
                break;
            case R.id.iv_minus:
                decreaseQuantity(fragmentProductDetailBinding.etQuantity.getText().toString(),
                        fragmentProductDetailBinding.etQuantity, fragmentProductDetailBinding.ivMinus);
                break;
            case R.id.iv_plus:
                increaseQuantity(fragmentProductDetailBinding.etQuantity.getText().toString(), fragmentProductDetailBinding.etQuantity, fragmentProductDetailBinding.ivMinus);
                break;
            case R.id.img_plus:
                increaseQuantity(fragmentProductDetailBinding.layoutAdded.tvSubQty.getText().toString(),
                        fragmentProductDetailBinding.layoutAdded.tvSubQty, fragmentProductDetailBinding.layoutAdded.imgMinus);
                break;
            case R.id.img_minus:
                decreaseQuantity(fragmentProductDetailBinding.layoutAdded.tvSubQty.getText().toString(),
                        fragmentProductDetailBinding.layoutAdded.tvSubQty, fragmentProductDetailBinding.layoutAdded.imgMinus);
                break;
            case R.id.iv_favourite:
                addOrRemoveFromFavourite();
                break;
            case R.id.tv_start_date:
                showCalendar(fragmentProductDetailBinding.layoutAdded.tvStartDate);
                break;
        }

    }

    private void showCalendar(CustomTextView tvDate) {
        //showing date picker dialog
        DatePickerDialog dpd;
        calendar = Calendar.getInstance();
        Calendar mcurrentDate = Calendar.getInstance();
        mYear = mcurrentDate.get(Calendar.YEAR);
        mMonth = mcurrentDate.get(Calendar.MONTH);
        mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        dpd = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String txtDisplayDate = null;
                String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                try {
                    txtDisplayDate = formatDate(selectedDate, "yyyy-MM-dd", "dd MMM yyyy");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {

                    if (tvDate != null) {
                        tvDate.setText(txtDisplayDate);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                calendar.set(year, month, dayOfMonth);
            }
        },
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        );
        dpd.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        dpd.show();

    }


    private void hideOrShowVariableWeightPolicy() {
        if (isVariableWtPolicyVisible) {
            fragmentProductDetailBinding.ivVariableWeightPolicyShowHide.setState(ExpandIconView.MORE, true);
            collapse(fragmentProductDetailBinding.tvVariableWeightPolicyBreif);
        } else {
            fragmentProductDetailBinding.ivVariableWeightPolicyShowHide.setState(ExpandIconView.LESS, true);
            expand(fragmentProductDetailBinding.tvVariableWeightPolicyBreif);
        }
        isVariableWtPolicyVisible = !isVariableWtPolicyVisible;
    }

    private void hideOrShowOtherProductInfo() {
        if (isOtherProductInfo) {
            fragmentProductDetailBinding.ivOtherProductInfoHideShow.setState(ExpandIconView.MORE, true);
            collapse(fragmentProductDetailBinding.tvOtherProductInfoBrief);
        } else {
            fragmentProductDetailBinding.ivOtherProductInfoHideShow.setState(ExpandIconView.LESS, true);
            expand(fragmentProductDetailBinding.tvOtherProductInfoBrief);
        }
        isOtherProductInfo = !isOtherProductInfo;
    }

    private void hideOrShowStorageAndUses() {
        if (isStorageVisible) {
            fragmentProductDetailBinding.ivStorageUseHideShow.setState(ExpandIconView.MORE, true);
            collapse(fragmentProductDetailBinding.tvStorageAndUseBrief);
        } else {
            fragmentProductDetailBinding.ivStorageUseHideShow.setState(ExpandIconView.LESS, true);
            expand(fragmentProductDetailBinding.tvStorageAndUseBrief);
        }
        isStorageVisible = !isStorageVisible;
    }

    private void hideOrShowBenefits() {
        if (isBenefitsVisible) {
            fragmentProductDetailBinding.ivBenefitsHideShow.setState(ExpandIconView.MORE, true);
            collapse(fragmentProductDetailBinding.tvBenefitsBrief);
        } else {
            fragmentProductDetailBinding.ivBenefitsHideShow.setState(ExpandIconView.LESS, true);
            expand(fragmentProductDetailBinding.tvBenefitsBrief);
        }
        isBenefitsVisible = !isBenefitsVisible;
    }

    private void hideOrShowAboutThisProduct() {
        if (isAboutThisProductVisible) {
            fragmentProductDetailBinding.ivAboutThisProductHideShow.setState(ExpandIconView.MORE, true);
            collapse(fragmentProductDetailBinding.tvAboutThisProductBrief);
        } else {
            fragmentProductDetailBinding.ivAboutThisProductHideShow.setState(ExpandIconView.LESS, true);
            expand(fragmentProductDetailBinding.tvAboutThisProductBrief);
        }
        isAboutThisProductVisible = !isAboutThisProductVisible;
    }

    private void addOrRemoveFromFavourite() {
        if (isFavourite) {
            fragmentProductDetailBinding.ivFavourite.setImageResource(R.drawable.ic_heart_without_colour);
        } else {
            fragmentProductDetailBinding.ivFavourite.setImageResource(R.drawable.ic_filled_heart);
        }
        isFavourite = !isFavourite;
    }

    private void increaseQuantity(String qty, CustomTextView etQuantity, ImageView view) {
        int quantity = Integer.parseInt(qty);
        quantity++;
        etQuantity.setText(String.valueOf(quantity));
        AppUtils.setMinusButton(quantity, view);
    }

    private void decreaseQuantity(String qty, CustomTextView etQuantity, ImageView view) {
        int quantity = Integer.parseInt(qty);
        if (quantity == 1) {
            warningToast(requireActivity(), getString(R.string.quantity_less_than_one));
        } else {
            quantity--;
            etQuantity.setText(String.valueOf(quantity));
        }
        AppUtils.setMinusButton(quantity, view);
    }

    private void hideOrShowNutritionDetails() {
        if (isNutritionDetailsVisible) {
            fragmentProductDetailBinding.ivNutritionsShowHide.setState(ExpandIconView.MORE, true);
            collapse(fragmentProductDetailBinding.tvNutritionsBrief);
        } else {
            fragmentProductDetailBinding.ivNutritionsShowHide.setState(ExpandIconView.LESS, true);
            expand(fragmentProductDetailBinding.tvNutritionsBrief);
        }
        isNutritionDetailsVisible = !isNutritionDetailsVisible;
    }

    private void hideOrShowProductDetails() {
        if (isProductDetailsVisible) {
            fragmentProductDetailBinding.ivProductDetailHideShow.setState(ExpandIconView.MORE, true);
            collapse(fragmentProductDetailBinding.tvProductDetailBrief);
        } else {
            fragmentProductDetailBinding.ivProductDetailHideShow.setState(ExpandIconView.LESS, true);
            expand(fragmentProductDetailBinding.tvProductDetailBrief);
        }
        isProductDetailsVisible = !isProductDetailsVisible;
    }

    private void hideOrShowBasketContentsList() {
        if (isBasketContentsVisible) {
            fragmentProductDetailBinding.layoutAdded.ivProductLists.setState(ExpandIconView.MORE, true);
            collapse(fragmentProductDetailBinding.layoutAdded.rvBasketContents);
        } else {
            fragmentProductDetailBinding.layoutAdded.ivProductLists.setState(ExpandIconView.LESS, true);
            expand(fragmentProductDetailBinding.layoutAdded.rvBasketContents);
        }
        isBasketContentsVisible = !isBasketContentsVisible;
    }

    private void toProductDetail(Product product, View root) {
        Bundle bundle = new Bundle();
        bundle.putString("name", product.getName());
        bundle.putString("image", product.getImg());
        bundle.putString("price", product.getPrice());
        bundle.putString("brand", product.getBrand());
        bundle.putString("quantity", product.getQuantity());
        bundle.putBoolean("organic", product.isOrganic());
        Navigation.findNavController(root).navigate(R.id.action_nav_home_to_nav_product_details, bundle);
    }
}