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
import com.poona.agrocart.app.AppUtils;
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

import java.util.ArrayList;

public class HomeFragment extends BaseFragment {

    private static final int AUTO_SCROLL_THRESHOLD_IN_MILLI = 3000;
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding fragmentHomeBinding;
    private BannerAdapter bannerAdapter;
    private final ArrayList<Banner> banners = new ArrayList<>();
    private final ArrayList<Category> categories = new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    private ProductListAdapter productListAdapter;
    private ProductListAdapter offerListAdapter;
    private BasketAdapter basketAdapter;
    private final ArrayList<Product> products = new ArrayList<>();
    private final ArrayList<Product> offerProducts = new ArrayList<>();
    private final ArrayList<Product> productLists = new ArrayList<>();
    private final ArrayList<Basket> baskets = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);
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
        productLists.clear();
        for (int i = 0; i < 4; i++) {
            Product product = new Product("0", "Potatos", "1kg", "10% Off", "Rs 65", "Rs 35", "https://www.linkpicture.com/q/Potato-Free-Download-PNG-1.png","Pune");
            productLists.add(product);
            if (i == 3){
                product.setOrganic(true);
            }
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        fragmentHomeBinding.recProduct.setNestedScrollingEnabled(false);
        fragmentHomeBinding.recProduct.setHasFixedSize(true);
        fragmentHomeBinding.recProduct.setLayoutManager(linearLayoutManager);
        productListAdapter = new ProductListAdapter(productLists, getActivity(), LANDSCAPE, root);
        fragmentHomeBinding.recProduct.setAdapter(productListAdapter);
    }

    private void setBasketItems(View view) {
        baskets.clear();
        Basket basket = new Basket("Fruit\n" +
                "Baskets", "Rs 15000", "https://www.linkpicture.com/q/1-200284.png");
        for (int i = 0; i < 4; i++)
            baskets.add(basket);
        basketAdapter = new BasketAdapter(baskets, getActivity(), view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        fragmentHomeBinding.recBasket.setNestedScrollingEnabled(false);
        fragmentHomeBinding.recBasket.setLayoutManager(layoutManager);
        fragmentHomeBinding.recBasket.setAdapter(basketAdapter);
    }

    private void setOfferProduct(View root) {
        offerProducts.clear();
        Product offerProduct = new Product("1", "Red Apple", "1Kg"
                , "10% Off", "Rs150", "Rs. 140", "https://www.linkpicture.com/q/pngfuel-1-1.png","Pune");
        Product offerProduct1 = new Product("2", "Organic Bananas", "12 pcs"
                , "10% Off", "Rs 45", "Rs. 35", "https://www.linkpicture.com/q/banana_2.png","Pune");
        offerProduct1.setOrganic(true);
        Product offerProduct2 = new Product("3", "Red Apple", "1Kg"
                , "10% Off", "Rs150", "Rs. 140", "https://www.linkpicture.com/q/pngfuel-1-1.png","Pune");
        Product offerProduct3 = new Product("4", "Organic Bananas", "12 pcs"
                , "10% Off", "Rs 45", "Rs. 35", "https://www.linkpicture.com/q/banana_2.png","Pune");
        offerProduct3.setOrganic(true);
        offerProducts.add(offerProduct);
        offerProducts.add(offerProduct1);
        offerProducts.add(offerProduct2);
        offerProducts.add(offerProduct3);
        offerListAdapter = new ProductListAdapter(offerProducts, getActivity(), PORTRAIT, root);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
        fragmentHomeBinding.recOffers.setNestedScrollingEnabled(false);
        fragmentHomeBinding.recOffers.setLayoutManager(layoutManager);
        fragmentHomeBinding.recOffers.setAdapter(offerListAdapter);
    }

    private void setBestSellings(View root) {
        products.clear();
        Product product = new Product("1", "Bell Pepper Red", "1Kg"
                , "10% Off", "Rs25", "Rs. 15", "https://www.linkpicture.com/q/capsicon.png","Pune");
        Product product1 = new Product("2", "Ginger", "250 gms"
                , "10% Off", "Rs 110", "Rs. 140", "https://www.linkpicture.com/q/ginger.png","Pune");
        product1.setOrganic(true);
        Product product2 = new Product("3", "Bell Pepper Red", "1Kg"
                , "10% Off", "Rs25", "Rs. 15", "https://www.linkpicture.com/q/capsicon.png","Pune");
        Product product3 = new Product("4", "Ginger", "500 gms"
                , "10% Off", "Rs280", "Rs. 250", "https://www.linkpicture.com/q/ginger.png","Pune");
        Product product4 = new Product("4", "Ginger", ""
                , "", "", "", "https://www.linkpicture.com/q/ginger.png","Pune");
        product4.setQty("0");
        Product product5 = new Product("4", "Ginger", ""
                , "", "", "", "https://www.linkpicture.com/q/ginger.png","Pune");
        product5.setQty("0");
        products.add(product);
        products.add(product1);
        products.add(product2);
        products.add(product3);
        products.add(product4);
        products.add(product5);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
        productListAdapter = new ProductListAdapter(products, requireActivity(), PORTRAIT, root);
        fragmentHomeBinding.recOfferProduct.setNestedScrollingEnabled(false);
        fragmentHomeBinding.recOfferProduct.setLayoutManager(layoutManager);
        fragmentHomeBinding.recOfferProduct.setAdapter(productListAdapter);
    }

    private void setShopByCategory(View view) {
        categories.clear();
        Category category = new Category("1", "Green Vegetables", "https://www.linkpicture.com/q/green_leafy_vegetable.png");
        Category category1 = new Category("2", "Fruit Vegetables", "https://www.linkpicture.com/q/tomato_1.png");
        Category category2 = new Category("3", "Green Vegetables", "https://www.linkpicture.com/q/green_leafy_vegetable.png");
        Category category3 = new Category("4", "Fruit Vegetables", "https://www.linkpicture.com/q/tomato_1.png");

        categories.add(category);
        categories.add(category1);
        categories.add(category2);
        categories.add(category3);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false);
        categoryAdapter = new CategoryAdapter(categories, requireActivity(), view);
        fragmentHomeBinding.recCategory.setNestedScrollingEnabled(false);
        fragmentHomeBinding.recCategory.setAdapter(categoryAdapter);
        fragmentHomeBinding.recCategory.setLayoutManager(layoutManager);
    }

    private void setBannersView() {
        banners.clear();
        Banner banner = new Banner("1", "Banner1", "https://www.linkpicture.com/q/banner_img.jpg");
        for (int i = 0; i < 3; i++) {
            banners.add(banner);
        }
        System.out.println(banners.size());
        bannerAdapter = new BannerAdapter(banners, requireActivity());

        fragmentHomeBinding.viewPagerBanner.setAdapter(bannerAdapter);
        // Set up tab indicators
        fragmentHomeBinding.dotsIndicator.setViewPager(fragmentHomeBinding.viewPagerBanner);


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
}