package com.duan1.polyfood;

import android.content.Intent;
import android.os.Bundle;
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

import com.airbnb.lottie.LottieAnimationView;
import com.duan1.polyfood.Database.NguoiDungDAO;
import com.duan1.polyfood.Models.NguoiDung;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText edtPassword, edtEmail;
    private ImageView ivEye;
    private boolean isPasswordVisible = false;
    private FirebaseAuth auth;
    private NguoiDungDAO nguoiDungDAO;
    private int login;

    private LottieAnimationView loading;
    private View viewLoad;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        nguoiDungDAO = new NguoiDungDAO();


        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        edtPassword = findViewById(R.id.edtPassword);
        TextView txtRegisNow = findViewById(R.id.txtRegisNow);
        Button btnLogin = findViewById(R.id.btnLogin);
        edtEmail = findViewById(R.id.edtEmail);
        TextView txvForgotPass = findViewById(R.id.txvForgotPass);
        loading = findViewById(R.id.lottieLoading);
        viewLoad = findViewById(R.id.viewLoad);

        txvForgotPass.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
            startActivity(intent);
        });


        txtRegisNow.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        btnLogin.setOnClickListener(view -> {
            loading();
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            boolean isValid = validate(email, password);
            if (isValid) {
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    String UID = auth.getUid();

                                    nguoiDungDAO.check(new NguoiDungDAO.FirebaseCallback() {
                                        @Override
                                        public void onCallback(NguoiDung nguoiDung) {
                                            loaded();
                                            if (nguoiDung == null) {
                                                Intent intent = new Intent(LoginActivity.this, InputInfoActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            } else {
                                                int role = nguoiDung.getRole(); // Lấy role từ object NguoiDung
                                                Log.e("Role", "Role hien tai la" + role);
                                                Intent intent;
                                                switch (role) {
                                                    case 1: // Driver
                                                        intent = new Intent(LoginActivity.this, DeliveryActivity.class);
                                                        break;
                                                    case 2: // Restaurant
                                                        intent = new Intent(LoginActivity.this, RestaurantActivity.class);
                                                        break;
                                                    default: // Default là User
                                                        intent = new Intent(LoginActivity.this, MainActivity.class);
                                                        break;
                                                }
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            }
                                        }
                                    }, UID);
                                } else {
                                    Toast.makeText(LoginActivity.this, "Sai Tài Khoản Mật Khẩu", Toast.LENGTH_SHORT).show();
                                    loaded();
                                }
                            }
                        });
            } else {
                loaded();
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

    public void loading(){
        loading.setVisibility(View.VISIBLE);
        viewLoad.setVisibility(View.VISIBLE);
    }

    public void loaded(){
        loading.setVisibility(View.GONE);
        viewLoad.setVisibility(View.GONE);
    }
}