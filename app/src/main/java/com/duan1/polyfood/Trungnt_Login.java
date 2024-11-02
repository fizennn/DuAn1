package com.duan1.polyfood;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Trungnt_Login extends AppCompatActivity {

    private EditText edtPassword, edtUserName;
    private ImageView ivEye;
    private boolean isPasswordVisible = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trungnt_login);

        edtPassword = findViewById(R.id.edtPassword);
        ivEye = findViewById(R.id.ivEye);

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
    }
}