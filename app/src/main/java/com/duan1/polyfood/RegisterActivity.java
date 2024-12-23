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

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private boolean isPasswordVisible = false;
    private EditText edtUserNameRegis, edtEmailRegis, edtPasswordRegis, edtConfirmRegis;
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
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_register);
        loading = findViewById(R.id.lottieLoading);
        viewLoad = findViewById(R.id.viewLoad);
        auth = FirebaseAuth.getInstance();
        edtUserNameRegis = findViewById(R.id.edtUserNameRegis);
        edtEmailRegis = findViewById(R.id.edtEmailRegis);
        edtPasswordRegis = findViewById(R.id.edtPasswordRegis);
        edtConfirmRegis = findViewById(R.id.edtConfirmRegis);
        TextView txtLoginNow = findViewById(R.id.txtLoginNow);
        Button btnRegister = findViewById(R.id.btnRegister);
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

            if (isPasswordValid){

                Log.e("aa", "onCreate: "+email );
                Log.e("aa", "onCreate: "+password );
                auth.createUserWithEmailAndPassword(email,
                        password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(RegisterActivity.this,InputInfoActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }else{
                            loaded();
                            Log.e("FirebaseAuthError", "Đăng Ký Thất Bại", task.getException());
                        }
                    }
                });
            }else {
                loaded();
//              Toast.makeText(this, "Mật khẩu không hợp lệ", Toast.LENGTH_SHORT).show();
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