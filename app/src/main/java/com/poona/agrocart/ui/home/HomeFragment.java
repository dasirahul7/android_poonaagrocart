package com.poona.agrocart.ui.home;

import static com.poona.agrocart.app.AppConstants.LANDSCAPE;
import static com.poona.agrocart.app.AppConstants.PORTRAIT;

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
import com.poona.agrocart.ui.home.adapter.CategoryAdapter;
import com.poona.agrocart.ui.home.adapter.ProductListAdapter;
import com.poona.agrocart.ui.home.model.Banner;
import com.poona.agrocart.ui.home.model.Basket;
import com.poona.agrocart.ui.home.model.Category;
import com.poona.agrocart.ui.home.model.Product;
import com.poona.agrocart.ui.nav_my_cart.CartItem;

import java.util.ArrayList;

public class HomeFragment extends BaseFragment {

    private static final int AUTO_SCROLL_THRESHOLD_IN_MILLI = 3000;
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding fragmentHomeBinding;
    private BannerAdapter bannerAdapter;
    private CategoryAdapter categoryAdapter;
    private ProductListAdapter productListAdapter;
    private ProductListAdapter offerListAdapter;
    private BasketAdapter basketAdapter;
    private ArrayList<Product> bestSellings = new ArrayList<>();
    private ArrayList<Product> offerProducts = new ArrayList<>();
    private ArrayList<Product> cartProductList = new ArrayList<>();
    private ArrayList<Banner> banners = new ArrayList<>();
    private ArrayList<Category> categories = new ArrayList<>();
    private ArrayList<Basket> baskets = new ArrayList<>();
    private AppSharedPreferences session;
    private final ArrayList<Product> mCartList = new ArrayList<Product>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        session = new AppSharedPreferences(requireActivity());
        View root = fragmentHomeBinding.getRoot();
        setBannersView();
        setShopByCategory(root);
        setBestSellings(root);
        setOfferProduct(root);
        setBasketItems(root);
        setCartItems(root);
        setStoreBanner(root);
        return root;
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
            productListAdapter = new ProductListAdapter(cartProductList, getActivity(), LANDSCAPE, root);
            fragmentHomeBinding.recProduct.setAdapter(productListAdapter);
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
        homeViewModel.getLiveDataOffer().observe(getViewLifecycleOwner(), offerProducts -> {
            this.offerProducts = offerProducts;
            offerListAdapter = new ProductListAdapter(this.offerProducts, getActivity(), PORTRAIT, root);
            LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
            fragmentHomeBinding.recOffers.setNestedScrollingEnabled(false);
            fragmentHomeBinding.recOffers.setLayoutManager(layoutManager);
            fragmentHomeBinding.recOffers.setAdapter(offerListAdapter);
        });
    }

    private void setBestSellings(View root) {
        homeViewModel.getLiveDataBestSelling().observe(getViewLifecycleOwner(), bestSellings -> {
            this.bestSellings = bestSellings;
            LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
            productListAdapter = new ProductListAdapter(this.bestSellings, requireActivity(), PORTRAIT, root);
            fragmentHomeBinding.recOfferProduct.setNestedScrollingEnabled(false);
            fragmentHomeBinding.recOfferProduct.setLayoutManager(layoutManager);
            fragmentHomeBinding.recOfferProduct.setAdapter(productListAdapter);
            productListAdapter.setOnAddClickListener(product -> {
                addToBasket(product);
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
//        banners.clear();
//        Banner banner = new Banner("1", "Banner1", "https://www.linkpicture.com/q/banner_img.jpg");
//        for (int i = 0; i < 3; i++) {
//            banners.add(banner);
//        }
//        System.out.println(banners.size());
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
//        CartItem cartItem = new CartItem();
//        cartItem.setImgUrl(item.getImg());
//        cartItem.setName(item.getName());
//        cartItem.setPrice(item.getOfferPrice());
//        cartItem.setFinalPrice(item.getPrice());
//        cartItem.setWeight(item.getWeight());
//        cartItem.setQuantity(item.getQuantity());
        mCartList.add(item);
        session.saveArrayList(mCartList, AppConstants.CART_LIST);
        for (int i=0;i<mCartList.size();i++){
            System.out.println("Added: "+mCartList.get(i).getName());
        }
    }
}