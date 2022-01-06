package com.poona.agrocart.ui.home;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.R;
import com.poona.agrocart.app.AppConstants;
import com.poona.agrocart.app.AppUtils;
import com.poona.agrocart.data.shared_preferences.AppSharedPreferences;
import com.poona.agrocart.databinding.FragmentHomeBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.home.adapter.BannerAdapter;
import com.poona.agrocart.ui.home.adapter.BasketAdapter;
import com.poona.agrocart.ui.home.adapter.OfferProductListAdapter;
import com.poona.agrocart.ui.home.adapter.CategoryAdapter;
import com.poona.agrocart.ui.home.adapter.ProductListAdapter;
import com.poona.agrocart.ui.home.model.Banner;
import com.poona.agrocart.ui.home.model.Basket;
import com.poona.agrocart.ui.home.model.Category;
import com.poona.agrocart.ui.home.model.Product;

import java.util.ArrayList;

public class HomeFragment extends BaseFragment {

    private static final int AUTO_SCROLL_THRESHOLD_IN_MILLI = 3000;
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding fragmentHomeBinding;
    private BannerAdapter bannerAdapter;
    private CategoryAdapter categoryAdapter;
    private ProductListAdapter productListAdapter;
    private OfferProductListAdapter bestsellingAdapter;
    private OfferProductListAdapter offerListAdapter;
    private BasketAdapter basketAdapter;
    private ArrayList<Product> bestSellings = new ArrayList<>();
    private ArrayList<Product> offerProducts = new ArrayList<>();
    private ArrayList<Product> cartProductList = new ArrayList<>();
    private ArrayList<Banner> banners = new ArrayList<>();
    private ArrayList<Category> categories = new ArrayList<>();
    private ArrayList<Basket> baskets = new ArrayList<>();
    private AppSharedPreferences session;
    private final ArrayList<Product> mCartList = new ArrayList<Product>();
    private ArrayList<String> BasketIds = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        session = new AppSharedPreferences(requireActivity());
        View root = fragmentHomeBinding.getRoot();
        getBasketItems();
        setBannersView();
        setShopByCategory(root);
        setBestSellings(root);
        setOfferProduct(root);
        setBasketItems(root);
        setCartItems(root);
        setStoreBanner(root);
        return root;
    }

    private void getBasketItems() {
        homeViewModel.getSavesProductInBasket().observe(getViewLifecycleOwner(), products -> {
            if (products!=null){
                for (Product saved : products)
                    BasketIds.add(saved.getId());
            }
        });
    }

    private void setStoreBanner(View root) {
        AppUtils.setImage(fragmentHomeBinding.imgStore, "https://www.linkpicture.com/q/store-2.png");
        fragmentHomeBinding.cardviewOurShops.setOnClickListener(v -> {
            redirectToOurShops(root);
        });
    }

    private void redirectToOurShops(View v) {
        Navigation.findNavController(v).navigate(R.id.action_nav_home_to_nav_our_stores);
    }

    private void setCartItems(View root) {

        homeViewModel.getLiveDataCartProduct().observe(getViewLifecycleOwner(), cartProducts -> {
            this.cartProductList = cartProducts;
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
            fragmentHomeBinding.recProduct.setNestedScrollingEnabled(false);
            fragmentHomeBinding.recProduct.setHasFixedSize(true);
            fragmentHomeBinding.recProduct.setLayoutManager(linearLayoutManager);
            productListAdapter = new ProductListAdapter(cartProductList, getActivity(), root);
            fragmentHomeBinding.recProduct.setAdapter(productListAdapter);
            productListAdapter.setOnProductClick(product -> {
                toProductDetail(product, root);
            });
            productListAdapter.setOnPlusClick((product, position) -> {
                addToBasket(product);
                this.cartProductList.get(position).setInBasket(true);
                this.cartProductList.get(position).setQuantity("1");
                productListAdapter.notifyItemChanged(position);
            });
        });

    }

    private void setBasketItems(View view) {
        homeViewModel.getLiveDataBaskets().observe(getViewLifecycleOwner(), baskets -> {
            this.baskets = baskets;
            basketAdapter = new BasketAdapter(this.baskets, getActivity(), view);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
            fragmentHomeBinding.recBasket.setNestedScrollingEnabled(false);
            fragmentHomeBinding.recBasket.setLayoutManager(layoutManager);
            fragmentHomeBinding.recBasket.setAdapter(basketAdapter);
        });

    }

    private void setOfferProduct(View root) {
        homeViewModel.getLiveDataOffer().observe(getViewLifecycleOwner(), liveProducts -> {
            this.offerProducts = liveProducts;
            offerListAdapter = new OfferProductListAdapter(this.offerProducts, getActivity(), root);
            LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
            fragmentHomeBinding.recOffers.setNestedScrollingEnabled(false);
            fragmentHomeBinding.recOffers.setLayoutManager(layoutManager);
            fragmentHomeBinding.recOffers.setAdapter(offerListAdapter);
            // Redirect to Product details
            offerListAdapter.setOnProductClick(product -> {
                toProductDetail(product, root);
            });
            offerListAdapter.setOnPlusClick((product, position) -> {
                addToBasket(product);
                offerProducts.get(position).setInBasket(true);
                offerProducts.get(position).setQuantity("1");
                offerListAdapter.notifyItemChanged(position);
            });
        });
    }

    private void setBestSellings(View root) {
        homeViewModel.getLiveDataBestSelling().observe(getViewLifecycleOwner(), liveList -> {
            this.bestSellings = liveList;
            LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
            bestsellingAdapter = new OfferProductListAdapter(this.bestSellings, requireActivity(), root);
            fragmentHomeBinding.recOfferProduct.setNestedScrollingEnabled(false);
            fragmentHomeBinding.recOfferProduct.setLayoutManager(layoutManager);
            fragmentHomeBinding.recOfferProduct.setAdapter(bestsellingAdapter);
            // Redirect to Product details
            bestsellingAdapter.setOnProductClick(product -> {
                toProductDetail(product, root);
            });
            bestsellingAdapter.setOnPlusClick((product, position) -> {
                addToBasket(product);
                this.bestSellings.get(position).setInBasket(true);
                this.bestSellings.get(position).setQuantity("1");
                bestsellingAdapter.notifyItemChanged(position);
            });

        });

    }

    private void setShopByCategory(View view) {
        homeViewModel.getLiveDataCategory().observe(getViewLifecycleOwner(), categories1 -> {
            categories = categories1;
            LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
            categoryAdapter = new CategoryAdapter(categories, requireActivity(), view);
            fragmentHomeBinding.recCategory.setNestedScrollingEnabled(false);
            fragmentHomeBinding.recCategory.setAdapter(categoryAdapter);
            fragmentHomeBinding.recCategory.setLayoutManager(layoutManager);
        });

    }

    private void setBannersView() {
        homeViewModel.getLiveDataBanner().observe(getViewLifecycleOwner(), banners1 -> {
            banners = banners1;
            bannerAdapter = new BannerAdapter(banners, requireActivity());

            fragmentHomeBinding.viewPagerBanner.setAdapter(bannerAdapter);
            // Set up tab indicators
            fragmentHomeBinding.dotsIndicator.setViewPager(fragmentHomeBinding.viewPagerBanner);
        });


    }

    @Override
    public void onResume() {
        try {
            activeHomeTitleBar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    private void activeHomeTitleBar() {
        ((HomeActivity) requireActivity()).binding.appBarHome.toolbar.post(() -> {
            Drawable d = ResourcesCompat.getDrawable(getResources(),
                    R.drawable.menu_icon_toggle, null);
            ((HomeActivity) requireActivity()).binding.appBarHome.toolbar.setNavigationIcon(d);
            ((HomeActivity) requireActivity()).binding.appBarHome.toolbar.setPadding(10, 0, 0, 0);
        });
        if (((HomeActivity) requireActivity()).binding.appBarHome.textTitle.getVisibility() == View.VISIBLE)
            ((HomeActivity) requireActivity()).binding.appBarHome.textTitle.setVisibility(View.GONE);
        if (((HomeActivity) requireActivity()).binding.appBarHome.imgDelete.getVisibility() == View.VISIBLE)
            ((HomeActivity) requireActivity()).binding.appBarHome.imgDelete.setVisibility(View.GONE);
        if (((HomeActivity) requireActivity()).binding.appBarHome.basketMenu.getVisibility() == View.VISIBLE)
            ((HomeActivity) requireActivity()).binding.appBarHome.basketMenu.setVisibility(View.GONE);
        if (((HomeActivity) requireActivity()).binding.appBarHome.rlProductTag.getVisibility() == View.VISIBLE)
            ((HomeActivity) requireActivity()).binding.appBarHome.rlProductTag.setVisibility(View.GONE);
        ((HomeActivity) requireActivity()).binding.appBarHome.tvAddress.setVisibility(View.VISIBLE);
        ((HomeActivity) requireActivity()).binding.appBarHome.logImg.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentHomeBinding = null;
    }

    private void addToBasket(Product item) {
        if (item.getQuantity() == null)
            item.setQuantity("1");
        mCartList.add(item);
        session.saveArrayList(mCartList, AppConstants.CART_LIST);
        for (int i = 0; i < mCartList.size(); i++) {
            System.out.println("Added: " + mCartList.get(i).getName());
        }
    }

    private void toProductDetail(Product product, View root) {
        Bundle bundle = new Bundle();
        bundle.putString("name", product.getName());
        bundle.putString("image", product.getImg());
        bundle.putString("price", product.getPrice());
        bundle.putString("brand", product.getBrand());
        bundle.putString("weight", product.getWeight());
        bundle.putString("quantity", product.getQuantity());
        bundle.putBoolean("organic", product.isOrganic());
        bundle.putBoolean("isInBasket", product.isInBasket());
        bundle.putString("Product", "Product");
        Navigation.findNavController(root).navigate(R.id.action_nav_home_to_nav_product_details, bundle);
    }

}