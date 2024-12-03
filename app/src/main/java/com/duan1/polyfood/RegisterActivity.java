package com.duan1.polyfood;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.duan1.polyfood.Models.NguoiDung;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {

     TextView txtLoginNow;
    private EditText edtUserNameRegis, edtEmailRegis, edtPasswordRegis, edtConfirmRegis;
    private Button btnRegister;
    private FirebaseAuth auth;
    private LottieAnimationView loading;
    private View viewLoad;



    public void loading(){
        loading.setVisibility(View.VISIBLE);
        viewLoad.setVisibility(View.VISIBLE);
    }

    public void loaded(){
        loading.setVisibility(View.GONE);
        viewLoad.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        loading = findViewById(R.id.lottieLoading);
        viewLoad = findViewById(R.id.viewLoad);
        auth = FirebaseAuth.getInstance();
        edtUserNameRegis = findViewById(R.id.edtUserNameRegis);
        edtEmailRegis = findViewById(R.id.edtEmailRegis);
        edtPasswordRegis = findViewById(R.id.edtPasswordRegis);
        edtConfirmRegis = findViewById(R.id.edtConfirmRegis);
        txtLoginNow = findViewById(R.id.txtLoginNow);
        btnRegister = findViewById(R.id.btnRegister);
        txtLoginNow.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        btnRegister.setOnClickListener(view -> {
            loading();
            String password = edtPasswordRegis.getText().toString().trim();
            String confirmPassword = edtConfirmRegis.getText().toString().trim();
            String email = edtEmailRegis.getText().toString().trim();
            String user = edtUserNameRegis.getText().toString().trim();
            boolean isPasswordValid = check(password, confirmPassword, email, user);

            if (isPasswordValid) {
                Log.e("aa", "onCreate: "+email );
                Log.e("aa", "onCreate: "+password );

                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sau khi đăng ký thành công, lưu thông tin người dùng vào Realtime Database
                                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                                    if (currentUser != null) {
                                        String uid = currentUser.getUid();
                                        // Tạo đối tượng người dùng
                                        NguoiDung nguoiDung = new NguoiDung();
                                        nguoiDung.setHoTen(user);
                                        nguoiDung.setEmail(email);
                                        nguoiDung.setRole(0); // Vai trò mặc định là User (0)
                                        nguoiDung.setSdt(""); // Cập nhật thông tin còn lại
                                        nguoiDung.setDiaChi("");
                                        nguoiDung.setSex("");
                                        nguoiDung.setAge("");

                                        // Lưu thông tin vào Firebase Realtime Database
                                        DatabaseReference database = FirebaseDatabase.getInstance().getReference("NguoiDung");
                                        database.child(uid).setValue(nguoiDung)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Intent intent = new Intent(RegisterActivity.this, InputInfoActivity.class);
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            startActivity(intent);
                                                        } else {
                                                            loaded();
                                                            Log.e("FirebaseError", "Không thể lưu thông tin người dùng", task.getException());
                                                        }
                                                    }
                                                });
                                    }
                                } else {
                                    loaded();
                                    Log.e("FirebaseAuthError", "Đăng Ký Thất Bại", task.getException());
                                }
                            }
                        });
            } else {
                loaded();
                // Xử lý nếu thông tin không hợp lệ
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