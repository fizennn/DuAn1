package com.duan1.polyfood;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.duan1.polyfood.Database.NguoiDungDAO;
import com.duan1.polyfood.Models.NguoiDung;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText edtPassword, edtEmail;
    private ImageView ivEye;
    private boolean isPasswordVisible = false;
    private TextView txtRegisNow;
    private Button btnLogin;
    private FirebaseAuth auth;
    private TextView txvForgotPass;
    private NguoiDungDAO nguoiDungDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nguoiDungDAO = new NguoiDungDAO();

        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        edtPassword = findViewById(R.id.edtPassword);
        txtRegisNow = findViewById(R.id.txtRegisNow);
        btnLogin = findViewById(R.id.btnLogin);
        edtEmail = findViewById(R.id.edtEmail);
        txvForgotPass = findViewById(R.id.txvForgotPass);

        txvForgotPass.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
            startActivity(intent);
        });


        txtRegisNow.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        btnLogin.setOnClickListener(view -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            boolean isValid = validate(email, password);
            if (isValid){
                auth.signInWithEmailAndPassword(email, password).
                        addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                    nguoiDungDAO.getAllNguoiDung(new NguoiDungDAO.FirebaseCallback() {
                                        @Override
                                        public void onCallback(NguoiDung nguoiDung) {
                                            if (nguoiDung==null){
                                                Intent intent = new Intent(LoginActivity.this,InputInfoActivity.class);
                                                startActivity(intent);
                                            }else{
                                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });

                                }else{
                                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
    private boolean validate(String email, String password){
        if (email.isEmpty()) {
            edtEmail.setError("Email không được để trống");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Email không hợp lệ");
            return false;
        }
        if (password.length() < 6) {
            edtPassword.setError("Mật khẩu phải dài hơn 6 ký tự");
            return false;
        }
        if (password.isEmpty()) {
            edtPassword.setError("Mật khẩu không được để trống");
            return false;
        }

        return true;
    }
}