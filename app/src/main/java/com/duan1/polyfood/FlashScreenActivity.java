package com.duan1.polyfood;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.duan1.polyfood.Database.AuthenticationFireBaseHelper;
import com.duan1.polyfood.Database.NguoiDungDAO;
import com.duan1.polyfood.Models.NguoiDung;

public class FlashScreenActivity extends AppCompatActivity {

    private AuthenticationFireBaseHelper fireBaseHelper;
    private NguoiDungDAO nguoiDungDAO;
    private static final String TAG = "FlashScreenActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_flash_screen);

        fireBaseHelper = new AuthenticationFireBaseHelper();
        nguoiDungDAO = new NguoiDungDAO();

        Intent wellcomeIntent = new Intent(FlashScreenActivity.this, WellcomeActivity.class);
        Intent inputInfoIntent = new Intent(FlashScreenActivity.this, InputInfoActivity.class);
        Intent userMainIntent = new Intent(FlashScreenActivity.this, MainActivity.class);
        Intent driverMainIntent = new Intent(FlashScreenActivity.this, DeliveryActivity.class);
        Intent restaurantMainIntent = new Intent(FlashScreenActivity.this, RestaurantActivity.class);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Kiểm tra xem người dùng đã đăng nhập chưa
                if (fireBaseHelper.getUID() == null) {
                    startActivity(wellcomeIntent);
                    finish();
                } else {
                    // Lấy thông tin người dùng từ database
                    nguoiDungDAO.getAllNguoiDungByID(fireBaseHelper.getUID(), new NguoiDungDAO.FirebaseCallback() {
                        @Override
                        public void onCallback(NguoiDung nguoiDung) {
                            if (nguoiDung == null) {
                                // Nếu không tìm thấy thông tin người dùng, chuyển đến InputInfoActivity
                                startActivity(inputInfoIntent);
                                finish();
                            } else {
                                // Kiểm tra role và điều hướng
                                int role = nguoiDung.getRole();
                                switch (role) {
                                    case 0: // User
                                        startActivity(userMainIntent);
                                        break;
                                    case 1: // Driver
                                        startActivity(driverMainIntent);
                                        break;
                                    case 2: // Restaurant
                                        startActivity(restaurantMainIntent);
                                        break;
                                    default:
                                        Log.e(TAG, "Role không hợp lệ: " + role);
                                        startActivity(wellcomeIntent);
                                        break;
                                }
                                finish();
                            }
                        }
                    });
                }
            }
        }, 3000); // Delay 3 giây
    }
}
