package com.duan1.polyfood;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Trungnt_Login extends AppCompatActivity {

    private EditText edtPassword, edtEmail;
    private ImageView ivEye;
    private boolean isPasswordVisible = false;
    private TextView txtRegisNow;
    private Button btnLogin;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trungnt_login);
        auth = FirebaseAuth.getInstance();
        edtPassword = findViewById(R.id.edtPassword);
        ivEye = findViewById(R.id.ivEye);
        txtRegisNow = findViewById(R.id.txtRegisNow);
        btnLogin = findViewById(R.id.btnLogin);
        edtEmail = findViewById(R.id.edtEmail);

        ivEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    // Ẩn mật khẩu
                    edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivEye.setImageResource(R.drawable.hide_icon); // Thay đổi biểu tượng thành mắt đóng
                } else {
                    // Hiện mật khẩu
                    edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivEye.setImageResource(R.drawable.hide_icon); // Thay đổi biểu tượng thành mắt mở
                }
                isPasswordVisible = !isPasswordVisible;
                edtPassword.setSelection(edtPassword.length()); // Đặt con trỏ về cuối EditText
            }
        });
        txtRegisNow.setOnClickListener(view -> {
            Intent intent = new Intent(Trungnt_Login.this, Trungnt_Register.class);
            startActivity(intent);
        });
        btnLogin.setOnClickListener(view -> {
            String email = edtEmail.getText().toString().trim();
            Log.e("aa", "onCreate: "+email );
            String password = edtPassword.getText().toString().trim();
            Log.e("aa", "onCreate: "+password );
            boolean isValid = validate(email, password);
            if (isValid){
                auth.signInWithEmailAndPassword(email, password).
                        addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(Trungnt_Login.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(Trungnt_Login.this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
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