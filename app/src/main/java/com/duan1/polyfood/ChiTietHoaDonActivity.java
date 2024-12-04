package com.duan1.polyfood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.duan1.polyfood.Database.HoaDonDAO;
import com.duan1.polyfood.Models.HoaDon;

import java.util.ArrayList;

public class ChiTietHoaDonActivity extends AppCompatActivity {

    private TextView txtTenMonAn, txtGia, txtSoLuong, txtTongTien, txtPhuongThucThanhToan, txtTrangThai, txtSdt, txtDiaChi;
    private ImageView imgMonAn;
    private HoaDon hoaDon;
    private LottieAnimationView loading;
    private View viewLoad;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chi_tiet_hoa_don);





        // Ánh xạ các View
        loading = findViewById(R.id.lottieLoading);
        viewLoad = findViewById(R.id.viewLoad);
        txtTenMonAn = findViewById(R.id.txtTenMonAn);
        txtGia = findViewById(R.id.txtGia);
        txtSoLuong = findViewById(R.id.txtSoluong);
        txtTongTien = findViewById(R.id.txtTongTien);
        txtPhuongThucThanhToan = findViewById(R.id.txtPhuongThucThanhToan);
        txtTrangThai = findViewById(R.id.txtTrangThai);
        imgMonAn = findViewById(R.id.img);
        txtSdt = findViewById(R.id.txtSdt);
        txtDiaChi = findViewById(R.id.txtDiaChi);

        CardView view = findViewById(R.id.imgBack111);

        loading();

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        HoaDonDAO hoaDonDAO = new HoaDonDAO();

        // Lấy id_hd từ Intent
        Intent intent = getIntent();
        String id_hd = intent.getStringExtra("id_hd");

        // Lấy thông tin hóa đơn từ Firebase
        hoaDonDAO.getHoaDonById(id_hd, new HoaDonDAO.FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<HoaDon> hoaDonList) {

            }

            @Override
            public void onCallback(HoaDon hoaDon) {
                if (hoaDon != null) {
                    // Hiển thị thông tin hóa đơn
                    txtTenMonAn.setText(hoaDon.getTenMonAn());
                    txtGia.setText(formatToVND(hoaDon.getGia()));
                    txtSoLuong.setText("x" + hoaDon.getSoLuong());
                    txtTongTien.setText(formatToVND(hoaDon.getTongTien()));
                    txtPhuongThucThanhToan.setText(hoaDon.getPhuongThucThanhToan());
                    txtTrangThai.setText(hoaDon.getTrangThai());
                    txtSdt.setText(hoaDon.getSdt());
                    txtDiaChi.setText(hoaDon.getDiaChi());

                    Glide.with(ChiTietHoaDonActivity.this)
                            .load(hoaDon.getHinhAnh())
                            .placeholder(R.drawable.load)
                            .error(R.drawable.load)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    loaded();
                                    return false;
                                }
                            })
                            .into(imgMonAn);

                } else {
                    Toast.makeText(ChiTietHoaDonActivity.this, "Không tìm thấy hóa đơn!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });


    }

    private String formatToVND(int amount) {
        return String.format("%,d VND", amount);
    }
    public void loading(){
        loading.setVisibility(View.VISIBLE);
        viewLoad.setVisibility(View.VISIBLE);
    }

    public void loaded(){
        loading.setVisibility(View.GONE);
        viewLoad.setVisibility(View.GONE);
    }
}