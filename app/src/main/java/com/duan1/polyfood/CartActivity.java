package com.duan1.polyfood;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duan1.polyfood.Adapter.CartIteamAdapter;
import com.duan1.polyfood.Models.ThucDon;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private List<ThucDon> listCart = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private Gson gson = new Gson();
    private static final String TAG = "CartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        sharedPreferences = getSharedPreferences("cart", MODE_PRIVATE);
        getCart();

        RecyclerView recyclerView = findViewById(R.id.rcyCartItem);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CartIteamAdapter thucDonNgangAdapter = new CartIteamAdapter(listCart, this);
        recyclerView.setAdapter(thucDonNgangAdapter);
    }

    private void getCart() {
        String json = sharedPreferences.getString("listCart", "");
        if (!json.isEmpty()) {
            try {
                Type type = new TypeToken<ArrayList<ThucDon>>() {}.getType();
                listCart = gson.fromJson(json, type);
                if (listCart == null) listCart = new ArrayList<>();
                Log.d(TAG, "Cart loaded. Items count: " + listCart.size());
            } catch (Exception e) {
                Log.e(TAG, "Error parsing JSON: " + e.getMessage());
                listCart = new ArrayList<>();
            }
        } else {
            Log.d(TAG, "No cart data found in SharedPreferences.");
            listCart = new ArrayList<>();
        }
    }
}
