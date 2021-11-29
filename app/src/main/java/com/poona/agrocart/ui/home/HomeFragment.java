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
    private ArrayList<Product> productLists = new ArrayList<>();
    private ArrayList<Basket> baskets = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = fragmentHomeBinding.getRoot();
        try {
//            initHomeTitleBar();
        } catch (Exception e) {
            e.printStackTrace();
        }

        fragmentHomeBinding.cardviewOurShops.setOnClickListener(v->{
            redirectToOurShops(root);
        });

        setBannersView();
        setShopByCategory(root);
        setBestSellings();
        setOfferProduct();
        setBasketItems(root);
        setCartItems();
        return root;
    }

    private void redirectToOurShops(View v)
    {
        Navigation.findNavController(v).navigate(R.id.action_nav_home_to_nav_our_stores);
    }

    private void setCartItems() {
        productLists.clear();
        for(int i = 0; i < 4; i++)
        {
            Product product = new Product("0","Potatos","1kg","10% Off","Rs 65","Rs 35","https://www.linkpicture.com/q/Potato-Free-Download-PNG-1.png");
            productLists.add(product);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        fragmentHomeBinding.recProduct.setNestedScrollingEnabled(false);
        fragmentHomeBinding.recProduct.setHasFixedSize(true);
        fragmentHomeBinding.recProduct.setLayoutManager(linearLayoutManager);
        productListAdapter = new ProductListAdapter(productLists,getActivity(),LANDSCAPE);
        fragmentHomeBinding.recProduct.setAdapter(productListAdapter);
    }

    private void setBasketItems(View view) {
        baskets.clear();
        Basket basket = new Basket("Fruit\n" +
                "Baskets","Rs 15000","https://www.linkpicture.com/q/1-200284.png");
        for (int i=0;i<4;i++)
            baskets.add(basket);
        basketAdapter = new BasketAdapter(baskets,getActivity(),view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);
        fragmentHomeBinding.recBasket.setNestedScrollingEnabled(false);
        fragmentHomeBinding.recBasket.setLayoutManager(layoutManager);
        fragmentHomeBinding.recBasket.setAdapter(basketAdapter);
    }

    private void setOfferProduct() {
        offerProducts.clear();
        Product offerProduct = new Product("1","Red Apple","1Kg"
                ,"10% Off","Rs150","Rs. 140", "https://www.linkpicture.com/q/pngfuel-1-1.png");
        Product offerProduct1 = new Product("2","Organic Bananas","12 pcs"
                ,"10% Off","Rs 45","Rs. 35", "https://www.linkpicture.com/q/banana_2.png");
        Product offerProduct2 = new Product("3","BRed Apple","1Kg"
                ,"10% Off","Rs150","Rs. 140", "https://www.linkpicture.com/q/pngfuel-1-1.png");
        Product offerProduct3 = new Product("4","Organic Bananas","12 pcs"
                ,"10% Off","Rs 45","Rs. 35", "https://www.linkpicture.com/q/banana_2.png");

        offerProducts.add(offerProduct);
        offerProducts.add(offerProduct1);
        offerProducts.add(offerProduct2);
        offerProducts.add(offerProduct3);
        offerListAdapter = new ProductListAdapter(offerProducts,getActivity(),PORTRAIT);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(),RecyclerView.HORIZONTAL,false);
        fragmentHomeBinding.recOffers.setNestedScrollingEnabled(false);
        fragmentHomeBinding.recOffers.setLayoutManager(layoutManager);
        fragmentHomeBinding.recOffers.setAdapter(offerListAdapter);
    }

    private void setBestSellings() {
        products.clear();
            Product product = new Product("1","Bell Pepper Red","1Kg"
                    ,"10% Off","Rs25","Rs. 15", "https://www.linkpicture.com/q/capsicon.png");
            Product product1 = new Product("2","Ginger","250 gms"
                    ,"","","Rs. 140", "https://www.linkpicture.com/q/ginger.png");
            Product product2 = new Product("3","Bell Pepper Red","1Kg"
                    ,"10% Off","Rs25","Rs. 15", "https://www.linkpicture.com/q/capsicon.png");
            Product product3 = new Product("4","Ginger","500 gms"
                    ,"10% Off","Rs280","Rs. 250", "https://www.linkpicture.com/q/ginger.png");

            products.add(product);
            products.add(product1);
            products.add(product2);
            products.add(product3);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(),RecyclerView.HORIZONTAL,false);
        productListAdapter = new ProductListAdapter(products,requireActivity(),PORTRAIT);
        fragmentHomeBinding.recOfferProduct.setNestedScrollingEnabled(false);
        fragmentHomeBinding.recOfferProduct.setLayoutManager(layoutManager);
        fragmentHomeBinding.recOfferProduct.setAdapter(productListAdapter);
    }

    private void setShopByCategory(View view) {
        categories.clear();
        Category category = new Category("1","Green Vegetables","https://www.linkpicture.com/q/green_leafy_vegetable.png");
        Category category1 = new Category("2","Fruit Vegetables","https://www.linkpicture.com/q/tomato_1.png");
        Category category2 = new Category("3","Green Vegetables","https://www.linkpicture.com/q/green_leafy_vegetable.png");
        Category category3 = new Category("4","Fruit Vegetables","https://www.linkpicture.com/q/tomato_1.png");

        categories.add(category);
        categories.add(category1);
        categories.add(category2);
        categories.add(category3);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity(),RecyclerView.HORIZONTAL,false);
        categoryAdapter = new CategoryAdapter(categories,requireActivity(),view);
        fragmentHomeBinding.recCategory.setNestedScrollingEnabled(false);
        fragmentHomeBinding.recCategory.setAdapter(categoryAdapter);
        fragmentHomeBinding.recCategory.setLayoutManager(layoutManager);
    }

    private void setBannersView() {
        banners.clear();
        Banner banner = new Banner("1","Banner1","https://www.linkpicture.com/q/banner_img.jpg");
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
            ((HomeActivity) requireActivity()).binding.appBarHome.toolbar.setPadding(10,0,0,0);
        });
        if (((HomeActivity)requireActivity()).binding.appBarHome.textTitle.getVisibility()==View.VISIBLE)
        ((HomeActivity)requireActivity()).binding.appBarHome.textTitle.setVisibility(View.GONE);
        if (((HomeActivity)requireActivity()).binding.appBarHome.imgDelete.getVisibility()==View.VISIBLE)
        ((HomeActivity)requireActivity()).binding.appBarHome.imgDelete.setVisibility(View.GONE);
        ((HomeActivity)requireActivity()).binding.appBarHome.textView.setVisibility(View.VISIBLE);
        ((HomeActivity)requireActivity()).binding.appBarHome.logImg.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentHomeBinding = null;
    }
}