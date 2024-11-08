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

import java.util.ArrayList;

public class FlashScreenActivity extends AppCompatActivity {

    private AuthenticationFireBaseHelper fireBaseHelper;
    private NguoiDungDAO nguoiDungDAO;
    String TAG = "zzzzzzzz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_flash_screen);

        fireBaseHelper = new AuthenticationFireBaseHelper();
        nguoiDungDAO = new NguoiDungDAO();

        Intent i = new Intent(FlashScreenActivity.this, WellcomeActivity.class);
        Intent i1 = new Intent(FlashScreenActivity.this, MainActivity.class);
        Intent input = new Intent(FlashScreenActivity.this, InputInfoActivity.class);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (fireBaseHelper.getUID()==null){
                    startActivity(i);
                }else {
                    nguoiDungDAO.getAllNguoiDung(new NguoiDungDAO.FirebaseCallback() {
                        @Override
                        public void onCallback(NguoiDung nguoiDung) {
                            if (nguoiDung==null){
                                startActivity(input);
                            }else{
                                startActivity(i1);
                            }
                        }
                    });
                }

            }
        },3000);
    }
}