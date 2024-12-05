package com.duan1.polyfood;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.duan1.polyfood.Database.DonHangDAO;
import com.duan1.polyfood.Database.GetRole;
import com.duan1.polyfood.Database.HoaDonDAO;
import com.duan1.polyfood.Database.NguoiDungDAO;
import com.duan1.polyfood.Database.ThongBaoDao;
import com.duan1.polyfood.Database.ThucDonDAO;
import com.duan1.polyfood.Fragment.BillFragment;
import com.duan1.polyfood.Models.DonHang;
import com.duan1.polyfood.Models.HoaDon;
import com.duan1.polyfood.Models.NguoiDung;
import com.duan1.polyfood.Models.ThongBao;
import com.duan1.polyfood.Models.ThucDon;
import com.duan1.polyfood.Other.IntToVND;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PayActivity extends AppCompatActivity {

    private int soLuong;
    private TextView txtTenMonAn, txtGia, txtSoluong, txtTongTien;
    private Spinner spinnerPaymentMethod;
    private EditText txtSDT, txtDiaChi;
    private ThucDon thucDon1;
    private ImageView img;
    private HoaDonDAO hoaDonDAO;
    private SharedPreferences sharedPreferences;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);





        // Khởi tạo các view
        txtTenMonAn = findViewById(R.id.txtTenMonAn);
        txtGia = findViewById(R.id.txtGia);
        txtSoluong = findViewById(R.id.txtSoluong);
        txtTongTien = findViewById(R.id.txtTongTien);
        Button btnPay = findViewById(R.id.btnPay);
        img = findViewById(R.id.img);
        spinnerPaymentMethod = findViewById(R.id.spinnerPaymentMethod);
        txtSDT = findViewById(R.id.txtSDT);
        txtDiaChi = findViewById(R.id.txtDiaChi);
        buttonSave = findViewById(R.id.btnSave);

        // Khởi tạo đối tượng DAO và ThucDon
        ThucDonDAO thucDonDAO = new ThucDonDAO();
        thucDon1 = new ThucDon();
        hoaDonDAO = new HoaDonDAO();

        NguoiDungDAO nguoiDungDAO = new NguoiDungDAO();

        nguoiDungDAO.getAllNguoiDung(new NguoiDungDAO.FirebaseCallback() {
            @Override
            public void onCallback(NguoiDung nguoiDung) {
                txtSDT.setText(nguoiDung.getSdt()+"");
                txtDiaChi.setText(nguoiDung.getDiaChi()+"");
            }
        });

        // Khởi tạo đối tượng DatabaseReference
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        soLuong = intent.getIntExtra("SO_LUONG", 0);
        String UID = getIntent().getStringExtra("UID");

        sharedPreferences = getSharedPreferences("cart", MODE_PRIVATE);

        thucDonDAO.getAThucDon(new ThucDonDAO.FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<ThucDon> thucDonList) { }

            @Override
            public void onCallback(ThucDon thucDon) {
                thucDon1 = thucDon;
                txtTenMonAn.setText(thucDon.getTen());
                txtGia.setText(formatToVND(thucDon.getGia()));
                txtSoluong.setText("x" + soLuong);

                // Tính tổng tiền (gia món ăn * soLuong)
                int tongTien = thucDon.getGia() * soLuong;
                // Hiển thị tổng tiền
                txtTongTien.setText("" + formatToVND(tongTien));

                if (PayActivity.this != null) {
                    Glide.with(PayActivity.this)
                            .load(thucDon.getHinhAnh())
                            .placeholder(R.drawable.load)
                            .error(R.drawable.load)
                            .into(img);
                }

                // Xử lý đánh giá sao (giữ nguyên đoạn mã của bạn để cập nhật sao)
            }

            @Override
            public void onCallback(Float star) {

            }
        }, UID);

        // Xử lý sự kiện thanh toán
        btnPay.setOnClickListener(v -> {

            GetRole role = new GetRole();


            role.getRole(new GetRole.CALLBACK() {
                @Override
                public void getRole(int role) {
                    if (role!=0){
                        Toast.makeText(PayActivity.this, "Bạn Không Thể Thực Hiện Thành Động !", Toast.LENGTH_SHORT).show();
                    }else {
                        String sdt = txtSDT.getText().toString().trim();
                        String diaChi = txtDiaChi.getText().toString().trim();

                        if (sdt.isEmpty()) {
                            txtSDT.setError("Số điện thoại không được để trống");
                            txtSDT.requestFocus();
                            return;
                        } else if (!sdt.matches("\\d{10,11}")) {
                            txtSDT.setError("Số điện thoại không hợp lệ (10-11 số)");
                            txtSDT.requestFocus();
                            return;
                        }

                        // Kiểm tra địa chỉ
                        if (diaChi.isEmpty()) {
                            txtDiaChi.setError("Địa chỉ không được để trống");
                            txtDiaChi.requestFocus();
                            return;
                        }

                        HoaDon hoaDon = new HoaDon();
                        hoaDon.setId_hd(FirebaseDatabase.getInstance().getReference().push().getKey());
                        hoaDon.setTenMonAn(thucDon1.getTen());
                        hoaDon.setSoLuong(soLuong);
                        hoaDon.setGia(thucDon1.getGia());
                        hoaDon.setTongTien(thucDon1.getGia() * soLuong);
                        hoaDon.setHinhAnh(thucDon1.getHinhAnh());
                        hoaDon.setSdt(txtSDT.getText().toString());
                        hoaDon.setDiaChi(txtDiaChi.getText().toString());

                        // Lấy phương thức thanh toán từ Spinner
                        String selectedPaymentMethod = spinnerPaymentMethod.getSelectedItem().toString();
                        hoaDon.setPhuongThucThanhToan(selectedPaymentMethod);

                        // Thiết lập trạng thái mặc định là "Chờ xử lý"
                        hoaDon.setTrangThai("Chờ xử lý");

                        // Thiết lập ngày đặt hàng (ngày hiện tại)
                        String currentDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
                        hoaDon.setNgayDatHang(currentDate);

                        // Gọi phương thức addDonHang để thêm đơn hàng vào Firebase
                        hoaDonDAO.addHoaDon(hoaDon);

                        ThongBaoDao thongBaoDao = new ThongBaoDao();

                        ThongBao thongBao = new ThongBao();

                        thongBao.setId_hd(hoaDon.getId_hd());
                        thongBao.setId_nn(hoaDon.getId_nd());
                        thongBao.setNoidung("Đơn hàng "+hoaDon.getTenMonAn()+" (sl:"+hoaDon.getSoLuong()+") của bạn đã được đặt!");
                        thongBao.setRole("Bạn");
                        thongBao.setTrangThai(hoaDon.getTrangThai());


                        thongBaoDao.guiThongBao(thongBao,PayActivity.this);

                        // Xóa món ăn khỏi giỏ hàng (SharedPreferences)
                        removeFromCart();

                        // Thông báo và chuyển sang màn hình BillFragment
                        Toast.makeText(PayActivity.this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();

                        Intent intent1 = new Intent(PayActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent1);
                    }
                }
            });

        });

        buttonSave.setOnClickListener(v -> {
            Toast.makeText(PayActivity.this, "Lưu đơn hàng thành công!", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(PayActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent1);
        });

    }

    private String formatToVND(int amount) {
        return String.format("%,d VND", amount);
    }

    private void removeFromCart() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = sharedPreferences.getString("listCart", "");
        if (!json.isEmpty()) {
            try {
                Type type = new TypeToken<ArrayList<ThucDon>>() {}.getType();
                List<ThucDon> listCart = new Gson().fromJson(json, type);
                if (listCart != null && !listCart.isEmpty()) {
                    // Xóa món ăn đã thanh toán khỏi giỏ hàng
                    for (int i = 0; i < listCart.size(); i++) {
                        if (listCart.get(i).getId_td().equals(thucDon1.getId_td())) {
                            listCart.remove(i);
                            break;
                        }
                    }
                    // Lưu lại giỏ hàng mới
                    editor.putString("listCart", new Gson().toJson(listCart));
                    editor.apply();
                }
            } catch (Exception e) {
                Log.e("PayActivity", "Error parsing or saving cart: " + e.getMessage());
            }
        }
    }


}