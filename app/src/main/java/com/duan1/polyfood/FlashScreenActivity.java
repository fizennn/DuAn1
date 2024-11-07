package com.duan1.polyfood;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.duan1.polyfood.Database.AuthenticationFireBaseHelper;

public class FlashScreenActivity extends AppCompatActivity {

    private AuthenticationFireBaseHelper fireBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_flash_screen);

        fireBaseHelper = new AuthenticationFireBaseHelper();

        Intent i = new Intent(FlashScreenActivity.this, WellcomeActivity.class);
        Intent i1 = new Intent(FlashScreenActivity.this, MainActivity.class);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (fireBaseHelper.getUID()==null){
                    startActivity(i);
                }else {
                    startActivity(i1);
                }

            }
        },3000);
    }
}