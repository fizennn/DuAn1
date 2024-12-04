package com.duan1.polyfood;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText edtEmailReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trungnt_reset_password);
        TextView txtRegisNowReset = findViewById(R.id.txtRegisNowReset);
        edtEmailReset = findViewById(R.id.edtEmailReset);
        Button btnSendEmailReset = findViewById(R.id.btnSendEmailReset);
        txtRegisNowReset.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
        // Lấy email người dùng nhập vào


        btnSendEmailReset.setOnClickListener(view -> {
            String email = edtEmailReset.getText().toString().trim();

                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        auth.sendPasswordResetEmail(email)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(this, "Email khôi phục đã được gửi", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.e("FirebaseAuthError", "Gửi email khôi phục thất bại", task.getException());
                                        Toast.makeText(this, "Gửi email khôi phục thất bại", Toast.LENGTH_SHORT).show();
                                    }
                    });
        });



    }
}