package com.duan1.polyfood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.duan1.polyfood.Database.HoaDonDAO;
import com.duan1.polyfood.Models.HoaDon;

import java.util.ArrayList;

public class ChiTietHoaDonActivity extends AppCompatActivity {

    private TextView txtTenMonAn, txtGia, txtSoLuong, txtTongTien, txtPhuongThucThanhToan, txtTrangThai;
    private ImageView imgMonAn;
    private HoaDonDAO hoaDonDAO;
    private HoaDon hoaDon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_hoa_don);


        // Ánh xạ các View
        txtTenMonAn = findViewById(R.id.txtTenMonAn);
        txtGia = findViewById(R.id.txtGia);
        txtSoLuong = findViewById(R.id.txtSoluong);
        txtTongTien = findViewById(R.id.txtTongTien);
        txtPhuongThucThanhToan = findViewById(R.id.txtPhuongThucThanhToan);
        txtTrangThai = findViewById(R.id.txtTrangThai);
        imgMonAn = findViewById(R.id.img);


        hoaDonDAO = new HoaDonDAO();

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

                    Glide.with(ChiTietHoaDonActivity.this)
                            .load(hoaDon.getHinhAnh())
                            .placeholder(R.drawable.load)
                            .error(R.drawable.load)
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
}