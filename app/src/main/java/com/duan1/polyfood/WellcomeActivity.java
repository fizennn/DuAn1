package com.duan1.polyfood;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class WellcomeActivity extends AppCompatActivity {

    public Button btnLogin;
    public TextView txvRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_wellcome);

        btnLogin = findViewById(R.id.btnLogin);
        txvRegister = findViewById(R.id.txvBtnRegister);

        btnLogin.setOnClickListener(v -> {
            Intent i = new Intent(WellcomeActivity.this,LoginActivity.class);
            startActivity(i);
        });

        txvRegister.setOnClickListener(v -> {
            Intent i = new Intent(WellcomeActivity.this,RegisterActivity.class);
            startActivity(i);

        });

    }
}