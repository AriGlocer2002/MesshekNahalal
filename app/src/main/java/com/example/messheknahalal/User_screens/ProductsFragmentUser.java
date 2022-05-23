package com.example.messheknahalal.User_screens;

import static android.content.Context.MODE_PRIVATE;

import android.database.sqlite.SQLiteDatabase;
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
import com.example.messheknahalal.models.Product;
import com.example.messheknahalal.R;
import com.example.messheknahalal.Utils.Utils;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class ProductsFragmentUser extends Fragment implements ProductsAdapterFirebase.OnAddToCartListener {

    public static final String TAG = "ProductsFragmentUser";

    RecyclerView rv_vegetables_buy;
    RecyclerView rv_fruits_buy;
    RecyclerView rv_shelf_buy;
    RecyclerView rv_others_buy;
    ChipGroup sort;


    Query vegetablesRef = FirebaseDatabase.getInstance().getReference("Product").child("Vegetable").orderByValue();
    Query fruitsRef = FirebaseDatabase.getInstance().getReference("Product").child("Fruit").orderByValue();
    Query shelfRef = FirebaseDatabase.getInstance().getReference("Product").child("Shelf").orderByValue();
    Query othersRef = FirebaseDatabase.getInstance().getReference("Product").child("Other").orderByValue();

    FirebaseRecyclerOptions.Builder<Product> vegetablesOptions;
    FirebaseRecyclerOptions.Builder<Product> fruitsOptions;
    FirebaseRecyclerOptions.Builder<Product> shelfOptions;
    FirebaseRecyclerOptions.Builder<Product> othersOptions;

    ProductsAdapterFirebase vegetablesAdapter, fruitsAdapter, shelfAdapter, othersAdapter;

    SQLiteDatabase db;

    public ProductsFragmentUser() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = requireContext().openOrCreateDatabase(Utils.DATABASE_NAME, MODE_PRIVATE, null);

        rv_vegetables_buy = view.findViewById(R.id.rv_vegetables_buy);
        rv_fruits_buy = view.findViewById(R.id.rv_fruits_buy);
        rv_shelf_buy = view.findViewById(R.id.rv_shelf_buy);
        rv_others_buy = view.findViewById(R.id.rv_others_buy);
        sort = view.findViewById(R.id.sort_spinner);

        List<String> sorts = new ArrayList<>();
        sorts.add("Name");
        sorts.add("Price");


        sort.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                if(!checkedIds.isEmpty()){

                    Chip chip = group.findViewById(checkedIds.get(0));
                    changeSort(chip.getText().toString());
                }
            }
        });

        vegetablesOptions = new FirebaseRecyclerOptions.Builder<Product>()
                .setLifecycleOwner(getViewLifecycleOwner())
                .setQuery(vegetablesRef, Product.class);

        vegetablesAdapter = new ProductsAdapterFirebase(vegetablesOptions.build(), requireContext(), false, this);
        rv_vegetables_buy.setAdapter(vegetablesAdapter);
        rv_vegetables_buy.setLayoutManager(new WrapContentLinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        fruitsOptions = new FirebaseRecyclerOptions.Builder<Product>()
                .setLifecycleOwner(getViewLifecycleOwner())
                .setQuery(fruitsRef, Product.class);

        fruitsAdapter = new ProductsAdapterFirebase(fruitsOptions.build(), requireContext(), false, this);
        rv_fruits_buy.setAdapter(fruitsAdapter);
        rv_fruits_buy.setLayoutManager(new WrapContentLinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        shelfOptions = new FirebaseRecyclerOptions.Builder<Product>()
                .setLifecycleOwner(getViewLifecycleOwner())
                .setQuery(shelfRef, Product.class);

        shelfAdapter = new ProductsAdapterFirebase(shelfOptions.build(), requireContext(), false, this);
        rv_shelf_buy.setAdapter(shelfAdapter);
        rv_shelf_buy.setLayoutManager(new WrapContentLinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        othersOptions = new FirebaseRecyclerOptions.Builder<Product>()
                .setLifecycleOwner(getViewLifecycleOwner())
                .setQuery(othersRef, Product.class);

        othersAdapter = new ProductsAdapterFirebase(othersOptions.build(), requireContext(), false, this);
        rv_others_buy.setAdapter(othersAdapter);
        rv_others_buy.setLayoutManager(new WrapContentLinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

    }

    private void changeSort(@NonNull String s) {
        if(s.equals("Name")){
            vegetablesRef = FirebaseDatabase.getInstance().getReference("Product").child("Vegetable").orderByChild("name");
            fruitsRef = FirebaseDatabase.getInstance().getReference("Product").child("Fruit").orderByChild("name");
            shelfRef = FirebaseDatabase.getInstance().getReference("Product").child("Shelf").orderByChild("name");
            othersRef = FirebaseDatabase.getInstance().getReference("Product").child("Other").orderByChild("name");
        }
        else{
            vegetablesRef = FirebaseDatabase.getInstance().getReference("Product").child("Vegetable").orderByChild("price");
            fruitsRef = FirebaseDatabase.getInstance().getReference("Product").child("Fruit").orderByChild("price");
            shelfRef = FirebaseDatabase.getInstance().getReference("Product").child("Shelf").orderByChild("price");
            othersRef = FirebaseDatabase.getInstance().getReference("Product").child("Other").orderByChild("price");
        }

        vegetablesOptions = new FirebaseRecyclerOptions.Builder<Product>()
                .setLifecycleOwner(getViewLifecycleOwner()).setQuery(vegetablesRef, Product.class);

        fruitsOptions = new FirebaseRecyclerOptions.Builder<Product>()
                .setLifecycleOwner(getViewLifecycleOwner()).setQuery(fruitsRef, Product.class);

        shelfOptions = new FirebaseRecyclerOptions.Builder<Product>()
                .setLifecycleOwner(getViewLifecycleOwner()).setQuery(shelfRef, Product.class);

        othersOptions = new FirebaseRecyclerOptions.Builder<Product>()
                .setLifecycleOwner(getViewLifecycleOwner()).setQuery(othersRef, Product.class);


        vegetablesAdapter.updateOptions(vegetablesOptions.build());
        fruitsAdapter.updateOptions(fruitsOptions.build());
        shelfAdapter.updateOptions(shelfOptions.build());
        othersAdapter.updateOptions(othersOptions.build());

        rv_vegetables_buy.setAdapter(vegetablesAdapter);
        rv_fruits_buy.setAdapter(fruitsAdapter);
        rv_shelf_buy.setAdapter(shelfAdapter);
        rv_others_buy.setAdapter(othersAdapter);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_products_user, container, false);
    }

    @Override
    public void addToCart(int position, Product product) {
        Utils.addProduct(db, product);
    }
}