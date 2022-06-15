package com.example.messheknahalal.User_screens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messheknahalal.Admin_screens.ProductsAdapterFirebase;
import com.example.messheknahalal.Admin_screens.WrapContentLinearLayoutManager;
import com.example.messheknahalal.R;
import com.example.messheknahalal.Utils.Utils;
import com.example.messheknahalal.models.Product;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class ProductsFragmentUser extends Fragment implements ProductsAdapterFirebase.OnAddToCartListener {

    RecyclerView rv_vegetables_buy;
    RecyclerView rv_fruits_buy;
    RecyclerView rv_shelf_buy;
    RecyclerView rv_others_buy;
    ChipGroup sort;

    DatabaseReference productsInCartRef = FirebaseDatabase.getInstance().getReference("Carts");
    final DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("Product");

    Query vegetablesRef = productsRef.child("Vegetable").orderByValue();
    Query fruitsRef = productsRef.child("Fruit").orderByValue();
    Query shelfRef = productsRef.child("Shelf").orderByValue();
    Query othersRef = productsRef.child("Other").orderByValue();

    FirebaseRecyclerOptions<Product> vegetablesOptions;
    FirebaseRecyclerOptions<Product> fruitsOptions;
    FirebaseRecyclerOptions<Product> shelfOptions;
    FirebaseRecyclerOptions<Product> othersOptions;

    ProductsAdapterFirebase vegetablesAdapter, fruitsAdapter, shelfAdapter, othersAdapter;

    public ProductsFragmentUser() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String emailPath = Utils.emailToUserPath(email);

        productsInCartRef = productsInCartRef.child(emailPath);

        rv_vegetables_buy = view.findViewById(R.id.rv_vegetables_buy);
        rv_fruits_buy = view.findViewById(R.id.rv_fruits_buy);
        rv_shelf_buy = view.findViewById(R.id.rv_shelf_buy);
        rv_others_buy = view.findViewById(R.id.rv_others_buy);

        sort = view.findViewById(R.id.sort_spinner);

        List<String> sorts = new ArrayList<>();
        sorts.add("name");
        sorts.add("price");
        sorts.add("stock");

        List<Integer> chips = new ArrayList<>();
        chips.add(R.id.chip_name);
        chips.add(R.id.chip_price);
        chips.add(R.id.chip_stock);

        sort.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                if(!checkedIds.isEmpty()){
                    int position = chips.indexOf(checkedIds.get(0));
                    String parameter = sorts.get(position);
                    changeSort(parameter);
                }
            }
        });

        vegetablesOptions = new FirebaseRecyclerOptions.Builder<Product>()
                .setLifecycleOwner(getViewLifecycleOwner())
                .setQuery(vegetablesRef, Product.class)
                .build();

        vegetablesAdapter = new ProductsAdapterFirebase(vegetablesOptions, requireContext(), false, this);
        rv_vegetables_buy.setAdapter(vegetablesAdapter);
        rv_vegetables_buy.setLayoutManager(new WrapContentLinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        fruitsOptions = new FirebaseRecyclerOptions.Builder<Product>()
                .setLifecycleOwner(getViewLifecycleOwner())
                .setQuery(fruitsRef, Product.class)
                .build();

        fruitsAdapter = new ProductsAdapterFirebase(fruitsOptions, requireContext(), false, this);
        rv_fruits_buy.setAdapter(fruitsAdapter);
        rv_fruits_buy.setLayoutManager(new WrapContentLinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        shelfOptions = new FirebaseRecyclerOptions.Builder<Product>()
                .setLifecycleOwner(getViewLifecycleOwner())
                .setQuery(shelfRef, Product.class)
                .build();

        shelfAdapter = new ProductsAdapterFirebase(shelfOptions, requireContext(), false, this);
        rv_shelf_buy.setAdapter(shelfAdapter);
        rv_shelf_buy.setLayoutManager(new WrapContentLinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        othersOptions = new FirebaseRecyclerOptions.Builder<Product>()
                .setLifecycleOwner(getViewLifecycleOwner())
                .setQuery(othersRef, Product.class)
                .build();

        othersAdapter = new ProductsAdapterFirebase(othersOptions, requireContext(), false, this);
        rv_others_buy.setAdapter(othersAdapter);
        rv_others_buy.setLayoutManager(new WrapContentLinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

    }

    private void changeSort(@NonNull String parameter) {
        vegetablesRef = productsRef.child("Vegetable").orderByChild(parameter);
        fruitsRef = productsRef.child("Fruit").orderByChild(parameter);
        shelfRef = productsRef.child("Shelf").orderByChild(parameter);
        othersRef = productsRef.child("Other").orderByChild(parameter);

        vegetablesOptions = new FirebaseRecyclerOptions.Builder<Product>()
                .setLifecycleOwner(getViewLifecycleOwner())
                .setQuery(vegetablesRef, Product.class)
                .build();

        fruitsOptions = new FirebaseRecyclerOptions.Builder<Product>()
                .setLifecycleOwner(getViewLifecycleOwner())
                .setQuery(fruitsRef, Product.class)
                .build();

        shelfOptions = new FirebaseRecyclerOptions.Builder<Product>()
                .setLifecycleOwner(getViewLifecycleOwner())
                .setQuery(shelfRef, Product.class)
                .build();

        othersOptions = new FirebaseRecyclerOptions.Builder<Product>()
                .setLifecycleOwner(getViewLifecycleOwner())
                .setQuery(othersRef, Product.class)
                .build();

        vegetablesAdapter.updateOptions(vegetablesOptions);
        fruitsAdapter.updateOptions(fruitsOptions);
        shelfAdapter.updateOptions(shelfOptions);
        othersAdapter.updateOptions(othersOptions);

        rv_vegetables_buy.setAdapter(vegetablesAdapter);
        rv_fruits_buy.setAdapter(fruitsAdapter);
        rv_shelf_buy.setAdapter(shelfAdapter);
        rv_others_buy.setAdapter(othersAdapter);

        rv_vegetables_buy.smoothScrollToPosition(0);
        rv_fruits_buy.smoothScrollToPosition(0);
        rv_shelf_buy.smoothScrollToPosition(0);
        rv_others_buy.smoothScrollToPosition(0);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_products_user, container, false);
    }

    @Override
    public void addToCart(int position, @NonNull Product product) {
        String productName = product.getName();
        String productPath = Utils.productNameToPath(productName);

        product.setAmount(1);

        productsInCartRef.child("products").child(productPath).setValue(product);
    }
}