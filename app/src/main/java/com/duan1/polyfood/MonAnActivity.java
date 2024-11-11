package com.duan1.polyfood;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.duan1.polyfood.Database.ThucDonDAO;
import com.duan1.polyfood.Models.ThucDon;
import com.duan1.polyfood.Other.IntToVND;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MonAnActivity extends AppCompatActivity {

    private String UID;
    private String TAG = "zzzzzzzzzzzz";
    private ThucDonDAO thucDonDAO;
    private ThucDon thucDon1;
    private TextView ten, gia, mota, sao, sl;
    private ImageView img, sao1, sao2, sao3, sao4, sao5, btnremove, btnadd;
    private IntToVND vnd;
    private int soLuong;
    private List<ThucDon> listCart;
    private Gson gson;
    private LinearLayout linearLayout;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_an);

        thucDonDAO = new ThucDonDAO();
        thucDon1 = new ThucDon();
        vnd = new IntToVND();
        gson = new Gson();
        listCart = new ArrayList<>();
        soLuong = 1;

        UID = getIntent().getStringExtra("UID");
        Log.d(TAG, "onCreate: " + UID);

        ten = findViewById(R.id.txvTenChiTiet);
        gia = findViewById(R.id.txvgiachitiet);
        mota = findViewById(R.id.txvmotachitiet);
        sao = findViewById(R.id.txvSao);
        img = findViewById(R.id.imgChiTiet);
        sao1 = findViewById(R.id.imgStar1);
        sao2 = findViewById(R.id.imgStar2);
        sao3 = findViewById(R.id.imgStar3);
        sao4 = findViewById(R.id.imgStar4);
        sao5 = findViewById(R.id.imgStar5);
        btnremove = findViewById(R.id.btnDeleChiTiet);
        btnadd = findViewById(R.id.btnAddChiTiet);
        sl = findViewById(R.id.txvSoLuongChiTiet);
        linearLayout = findViewById(R.id.linerAddToCart);

        // Lấy dữ liệu từ Firebase và cập nhật UI
        thucDonDAO.getAThucDon(new ThucDonDAO.FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<ThucDon> thucDonList) { }

            @Override
            public void onCallback(ThucDon thucDon) {
                thucDon1 = thucDon;
                ten.setText(thucDon.getTen());
                gia.setText(vnd.convertToVND(thucDon.getGia()));
                mota.setText(thucDon.getMoTa());
                sao.setText(thucDon.getDanhGia());

                if (MonAnActivity.this != null) {
                    Glide.with(MonAnActivity.this)
                            .load(thucDon.getHinhAnh())
                            .placeholder(R.drawable.load)
                            .error(R.drawable.load)
                            .into(img);
                }

                // Xử lý đánh giá sao (giữ nguyên đoạn mã của bạn để cập nhật sao)
            }
        }, UID);

        Handler handler = new Handler();
        Runnable incrementRunnable = new Runnable() {
            @Override
            public void run() {
                if (soLuong < 99) {
                    soLuong++;
                    changeCost();
                }
                handler.postDelayed(this, 100); // Lặp lại sau mỗi 100ms khi giữ nút tăng
            }
        };
        Runnable decrementRunnable = new Runnable() {
            @Override
            public void run() {
                if (soLuong > 1) { // Kiểm tra để không giảm dưới 0
                    soLuong--;
                    changeCost();
                }
                handler.postDelayed(this, 100); // Lặp lại sau mỗi 100ms khi giữ nút giảm
            }
        };

        // Xử lý cho nút tăng
        btnadd.setOnTouchListener(new View.OnTouchListener() {
            private boolean isLongPress = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isLongPress = false;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isLongPress = true;
                                handler.post(incrementRunnable);
                            }
                        }, 1000); // Thời gian nhấn giữ: 1 giây
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        handler.removeCallbacks(incrementRunnable);
                        if (!isLongPress && soLuong < 99) {
                            soLuong++;
                            changeCost();
                        }
                        handler.removeCallbacksAndMessages(null);
                        break;
                }
                return true;
            }
        });

        // Xử lý cho nút giảm
        btnremove.setOnTouchListener(new View.OnTouchListener() {
            private boolean isLongPress = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isLongPress = false;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isLongPress = true;
                                handler.post(decrementRunnable);
                            }
                        }, 1000); // Thời gian nhấn giữ: 1 giây
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        handler.removeCallbacks(decrementRunnable);
                        if (!isLongPress && soLuong > 1) {
                            soLuong--;
                            changeCost();
                        }
                        handler.removeCallbacksAndMessages(null);
                        break;
                }
                return true;
            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thucDon1.setSoLuong(soLuong);
                addToCart(thucDon1);
                getCart();
                for (ThucDon don : listCart) {
                    Log.d(TAG, "onClick: " + don.getTen());
                }
            }
        });
    }

    public void changeCost() {
        gia.setText(vnd.convertToVND(thucDon1.getGia() * soLuong));
        sl.setText(String.valueOf(soLuong));
    }

    public void addToCart(ThucDon thucDon) {
        SharedPreferences sharedPreferences = getSharedPreferences("cart", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        getCart();
        listCart.add(thucDon);
        String json = gson.toJson(listCart);
        editor.putString("listCart", json);
        editor.apply();
        Toast.makeText(this, "Thêm Thành Công", Toast.LENGTH_SHORT).show();
    }

    public void getCart() {
        SharedPreferences sharedPreferences = getSharedPreferences("cart", MODE_PRIVATE);
        String json = sharedPreferences.getString("listCart", null);

        Type type = new TypeToken<ArrayList<ThucDon>>() {}.getType();
        if (json != null) {
            try {
                listCart = gson.fromJson(json, type);
            } catch (Exception e) {
                Log.e(TAG, "Lỗi khi chuyển đổi JSON: " + e.getMessage());
                listCart = new ArrayList<>();
            }
        } else {
            listCart = new ArrayList<>();
        }
    }
}
