package com.duan1.polyfood;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duan1.polyfood.Adapter.CartIteamAdapter;
import com.duan1.polyfood.Models.ThucDon;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private static final org.apache.commons.logging.Log log = LogFactory.getLog(CartActivity.class);
    private List<ThucDon> listCart = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private Gson gson = new Gson();
    private static final String TAG = "CartActivity";
    private CardView view;
    private LinearLayout layout;
    private Button btnDelete,btnCancle;

    private CartIteamAdapter thucDonNgangAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        sharedPreferences = getSharedPreferences("cart", MODE_PRIVATE);
        getCart();
        view = findViewById(R.id.imgBack111);
        RecyclerView recyclerView = findViewById(R.id.rcyCartItem);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        thucDonNgangAdapter = new CartIteamAdapter(listCart, this);
        recyclerView.setAdapter(thucDonNgangAdapter);
        layout = findViewById(R.id.viewSelected);
        btnDelete = findViewById(R.id.btnDelete);
        btnCancle = findViewById(R.id.btnCancle);


        view.setOnClickListener(v -> {
            finish();
        });

        ImageButton imageButton = findViewById(R.id.btnSelect);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
                for (ThucDon item : listCart){
                    item.setSelected("VISIBLE");

                }
                layout.setVisibility(View.VISIBLE);
                thucDonNgangAdapter.notifyDataSetChanged();

            }
        });

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
                layout.setVisibility(View.GONE);
                thucDonNgangAdapter.notifyDataSetChanged();
            }
        });


        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hiển thị dialog xác nhận
                new AlertDialog.Builder(CartActivity.this)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc chắn muốn xóa các mục đã chọn không?")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Xử lý xóa các mục đã chọn
                                ArrayList<ThucDon> updatedList = new ArrayList<>();
                                for (ThucDon item : listCart) {
                                    if ("VISIBLE".equalsIgnoreCase(item.getSelected())) {
                                        updatedList.add(item);
                                    }
                                }
                                listCart.clear();
                                listCart.addAll(updatedList);

                                clear();

                                // Cập nhật lại SharedPreferences
                                updateSharedPreferences();

                                // Ẩn giao diện lựa chọn và cập nhật giao diện

                                layout.setVisibility(View.GONE);
                                thucDonNgangAdapter.notifyDataSetChanged();

                                // Thông báo cho người dùng
                                Toast.makeText(CartActivity.this, "Đã xóa các mục đã chọn", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss(); // Đóng dialog khi nhấn Hủy
                            }
                        })
                        .show();
            }
        });
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

    public void clear(){
        for (ThucDon item : listCart){
            item.setSelected("");
        }
    }

    private void updateSharedPreferences() {
        try {
            String json = gson.toJson(listCart); // Chuyển đổi danh sách sang JSON
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("listCart", json); // Lưu JSON vào SharedPreferences
            editor.apply(); // Áp dụng thay đổi
            Log.d(TAG, "Cart updated in SharedPreferences.");
        } catch (Exception e) {
            Log.e(TAG, "Error saving cart to SharedPreferences: " + e.getMessage());
        }
    }

}
