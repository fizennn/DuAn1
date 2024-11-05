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

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Trungnt_Register extends AppCompatActivity {

    private TextView txtLoginNow;
    private ImageView ivEye1, ivEye2;
    private boolean isPasswordVisible = false;
    private EditText edtUserNameRegis, edtEmailRegis, edtPasswordRegis, edtConfirmRegis;
    private Button btnRegister;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_trungnt_register);
        auth = FirebaseAuth.getInstance();
        edtUserNameRegis = findViewById(R.id.edtUserNameRegis);
        edtEmailRegis = findViewById(R.id.edtEmailRegis);
        edtPasswordRegis = findViewById(R.id.edtPasswordRegis);
        edtConfirmRegis = findViewById(R.id.edtConfirmRegis);
        ivEye1 = findViewById(R.id.ivEye1);
        ivEye2 = findViewById(R.id.ivEye2);
        txtLoginNow = findViewById(R.id.txtLoginNow);
        btnRegister = findViewById(R.id.btnRegister);
        txtLoginNow.setOnClickListener(view -> {
            Intent intent = new Intent(Trungnt_Register.this, Trungnt_Login.class);
            startActivity(intent);
        });
        ivEye1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    // Ẩn mật khẩu
                    edtPasswordRegis.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivEye1.setImageResource(R.drawable.hide_icon); // Thay đổi biểu tượng thành mắt đóng
                } else {
                    // Hiện mật khẩu
                    edtPasswordRegis.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivEye1.setImageResource(R.drawable.hide_icon); // Thay đổi biểu tượng thành mắt mở
                }
                isPasswordVisible = !isPasswordVisible;
                edtPasswordRegis.setSelection(edtPasswordRegis.length()); // Đặt con trỏ về cuối EditText
            }
        });
        ivEye2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    // Ẩn mật khẩu
                    edtConfirmRegis.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivEye2.setImageResource(R.drawable.hide_icon); // Thay đổi biểu tượng thành mắt đóng
                } else {
                    // Hiện mật khẩu
                    edtConfirmRegis.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivEye2.setImageResource(R.drawable.hide_icon); // Thay đổi biểu tượng thành mắt mở
                }
                isPasswordVisible = !isPasswordVisible;
                edtConfirmRegis.setSelection(edtConfirmRegis.length()); // Đặt con trỏ về cuối EditText
            }
        });
        btnRegister.setOnClickListener(view -> {
            String password = edtPasswordRegis.getText().toString().trim();
            String confirmPassword = edtConfirmRegis.getText().toString().trim();
            String email = edtEmailRegis.getText().toString().trim();
            String user = edtUserNameRegis.getText().toString().trim();
            boolean isPasswordValid = check(password, confirmPassword, email, user);

            if (isPasswordValid){

                Log.e("aa", "onCreate: "+email );
                Log.e("aa", "onCreate: "+password );
                auth.createUserWithEmailAndPassword(email,
                        password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser currentUser = auth.getCurrentUser();
                            Toast.makeText(Trungnt_Register.this, "Dang ky Thanh cong"+currentUser, Toast.LENGTH_SHORT).show();
                        }else{
                            Log.e("FirebaseAuthError", "Đăng ký thất bại", task.getException());
                            Toast.makeText(Trungnt_Register.this, "Dang ky that bai", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else {
//                Toast.makeText(this, "Mật khẩu không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Boolean check(String password, String confirmPassword, String email, String user) {

        if (user.isEmpty()) {
            edtUserNameRegis.setError("Tên người dùng không được để trống");
            return false;
        }
        if (email.isEmpty()) {
            edtEmailRegis.setError("Email không được để trống");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmailRegis.setError("Email không hợp lệ");
            return false;
        }
        if (password.isEmpty()) {
            edtPasswordRegis.setError("Mật khẩu không được để trống");
            return false;
        }
        // Kiểm tra độ dài mật khẩu
        if (password.length() < 6) {
            edtPasswordRegis.setError("Mật khẩu phải dài hơn 6 ký tự");
            return false;
        }
        // Kiểm tra xác nhận mật khẩu
        if (!password.equals(confirmPassword)) {
            edtConfirmRegis.setError("Mật khẩu xác nhận không khớp");
            return false;
        }



        return true;
    }

}