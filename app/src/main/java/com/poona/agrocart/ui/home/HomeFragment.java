package com.poona.agrocart.ui.home;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.R;
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
    private ArrayList<Banner> banners = new ArrayList<>();
    private ArrayList<Category> categories = new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    private ProductListAdapter productListAdapter;
    private ProductListAdapter offerListAdapter;
    private BasketAdapter basketAdapter;
    private ArrayList<Product> products = new ArrayList<>();
    private ArrayList<Product> offerProducts = new ArrayList<>();
    private ArrayList<Basket> baskets = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = fragmentHomeBinding.getRoot();
        try {
            initHomeTitleBar();
        } catch (Exception e) {
            e.printStackTrace();
        }

        addBannersView();
        addCategories();
        addProducts();
        addOfferProduct();
        addBasketItems();
        return root;
    }

    private void addBasketItems() {
        Basket basket = new Basket("Fruit\n" +
                "Baskets","Rs 15000",R.drawable.basket_item);
        for (int i=0;i<4;i++)
            baskets.add(basket);
        basketAdapter = new BasketAdapter(baskets,getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);
        fragmentHomeBinding.recBasket.setLayoutManager(layoutManager);
        fragmentHomeBinding.recBasket.setAdapter(basketAdapter);
    }

    private void addOfferProduct() {
        Product offerProduct = new Product("1","Red Apple","1Kg"
                ,"10% Off","Rs150","Rs. 140", R.drawable.apple);
        Product offerProduct1 = new Product("2","Organic Bananas","12 pcs"
                ,"10% Off","Rs 45","Rs. 35", R.drawable.banana);
        Product offerProduct2 = new Product("3","BRed Apple","1Kg"
                ,"10% Off","Rs150","Rs. 140", R.drawable.apple);
        Product offerProduct3 = new Product("4","Organic Bananas","12 pcs"
                ,"10% Off","Rs 45","Rs. 35", R.drawable.banana);

        offerProducts.add(offerProduct);
        offerProducts.add(offerProduct1);
        offerProducts.add(offerProduct2);
        offerProducts.add(offerProduct3);
        offerListAdapter = new ProductListAdapter(offerProducts,getActivity());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireActivity(),RecyclerView.HORIZONTAL,false);
        fragmentHomeBinding.recOffers.setLayoutManager(layoutManager);
        fragmentHomeBinding.recOffers.setAdapter(offerListAdapter);
    }

    private void addProducts() {
            Product product = new Product("1","Bell Pepper Red","1Kg"
                    ,"10% Off","Rs25","Rs. 15", R.drawable.capsicon);
            Product product1 = new Product("2","Ginger","250 gms"
                    ,"10% Off","","Rs. 140", R.drawable.ginger);
            Product product2 = new Product("3","Bell Pepper Red","1Kg"
                    ,"10% Off","Rs25","Rs. 15", R.drawable.capsicon);
            Product product3 = new Product("4","Ginger","500 gms"
                    ,"10% Off","Rs280","Rs. 250", R.drawable.ginger);

            products.add(product);
            products.add(product1);
            products.add(product2);
            products.add(product3);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireActivity(),RecyclerView.HORIZONTAL,false);
        productListAdapter = new ProductListAdapter(products,requireActivity());
        fragmentHomeBinding.recProduct.setLayoutManager(layoutManager);
        fragmentHomeBinding.recProduct.setAdapter(productListAdapter);
    }

    private void addCategories() {
        Category category = new Category("1","Green Vegetables",R.drawable.green_leafy_vegetable);
        Category category1 = new Category("2","Fruit Vegetables",R.drawable.tomato);
        Category category2 = new Category("3","Green Vegetables",R.drawable.green_leafy_vegetable);
        Category category3 = new Category("4","Fruit Vegetables",R.drawable.tomato);

        categories.add(category);
        categories.add(category1);
        categories.add(category2);
        categories.add(category3);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireActivity(),RecyclerView.HORIZONTAL,false);
        categoryAdapter = new CategoryAdapter(categories,requireActivity());
        fragmentHomeBinding.recCategory.setAdapter(categoryAdapter);
        fragmentHomeBinding.recCategory.setLayoutManager(layoutManager);
    }

    private void addBannersView() {
        Banner banner = new Banner("1","Banner1",R.drawable.banner_img);
        for (int i=0;i<3;i++){
            banners.add(banner);
        }
        System.out.println(banners.size());
        BannerAdapter bannerAdapter = new BannerAdapter(banners,requireActivity());

        fragmentHomeBinding.viewPagerBanner.setAdapter(bannerAdapter);
        fragmentHomeBinding.tlIndicators.setupViewPager(fragmentHomeBinding.viewPagerBanner);
       // tabs.setupWithViewPager(autoScrollViewPager);

        // start auto scroll
        fragmentHomeBinding.viewPagerBanner.startAutoScroll();

        // set auto scroll time in mili
        fragmentHomeBinding.viewPagerBanner.setSlideInterval(AUTO_SCROLL_THRESHOLD_IN_MILLI);

        // enable recycling using true
        fragmentHomeBinding.viewPagerBanner.setCycle(true);


//        bannerAdapter = new BannerAdapter(banners,requireActivity());
//        binding.viewPagerBanner.setAdapter(bannerAdapter);
//        bannerAdapter.notifyDataSetChanged();

//        binding.viewPagerBanner.addOnPageChangeListener(bannerAdapter);
    }

    @Override
    public void onResume() {
        try {
            initHomeTitleBar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    private void initHomeTitleBar() {
        ((HomeActivity) requireActivity()).binding.appBarHome.toolbar.post(() -> {
            Drawable d = ResourcesCompat.getDrawable(getResources(),
                    R.drawable.ic_poona_agrocart_logo_final, null);
            ((HomeActivity) requireActivity()).binding.appBarHome.toolbar.setNavigationIcon(d);
            ((HomeActivity) requireActivity()).binding.appBarHome.toolbar.setPadding(30,0,0,0);
            ((HomeActivity) requireActivity()).binding.appBarHome.toolbar.setNavigationOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requireActivity().onBackPressed();
                        }
                    }
            );
        });
        if (((HomeActivity)requireActivity()).binding.appBarHome.textTitle.getVisibility()==View.VISIBLE)
        ((HomeActivity)requireActivity()).binding.appBarHome.textTitle.setVisibility(View.GONE);
        ((HomeActivity)requireActivity()).binding.appBarHome.textView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentHomeBinding = null;
    }
}