package com.duan1.polyfood;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.duan1.polyfood.Database.NguoiDungDAO;
import com.duan1.polyfood.Models.NguoiDung;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class WellcomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    /** @noinspection deprecation*/
    private GoogleSignInClient mGoogleSignInClient;
    private NguoiDungDAO nguoiDungDAO = new NguoiDungDAO();

    /** @noinspection deprecation*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_wellcome);

        mAuth = FirebaseAuth.getInstance();

        // Cấu hình Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Lấy từ Firebase Console
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Button btnLogin = findViewById(R.id.btnLogin);
        TextView txvRegister = findViewById(R.id.txvBtnRegister);
        Button btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn);

        btnGoogleSignIn.setOnClickListener(v -> {
            signInWithGoogle();
        });

        btnLogin.setOnClickListener(v -> {
            Intent i = new Intent(WellcomeActivity.this, LoginActivity.class);
            startActivity(i);
        });

        txvRegister.setOnClickListener(v -> {
            Intent i = new Intent(WellcomeActivity.this, RegisterActivity.class);
            startActivity(i);
        });
    }

    /** @noinspection deprecation*/
    private void signInWithGoogle() {
        // Bắt đầu quá trình đăng nhập Google
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 1001); // Mã yêu cầu cho việc đăng nhập
    }

    /** @noinspection deprecation*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account);
                }
            } catch (ApiException e) {
                Log.e("GoogleSignIn", "Google Sign-In failed: " + e.getMessage(), e);
                Toast.makeText(this, "Đăng nhập Google thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        try {
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Đăng ký và đăng nhập thành công
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d("FirebaseAuth", "Đăng ký Google thành công: " + user.getUid());
                            Toast.makeText(this, "Đăng ký Google thành công", Toast.LENGTH_SHORT).show();

                            // Chuyển đến InputInfoActivity
                            Intent intent = new Intent(WellcomeActivity.this, InputInfoActivity.class);
                            startActivity(intent);
                            finish(); // Kết thúc màn hình hiện tại
                        } else {
                            // Xử lý nếu đăng nhập thất bại
                            Log.e("FirebaseAuth", "Đăng ký Google thất bại", task.getException());
                            Toast.makeText(this, "Đăng ký Google thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {
            Log.e("FirebaseAuth", "Unexpected error in firebaseAuthWithGoogle", e);
            Toast.makeText(this, "Lỗi không xác định khi xác thực với Firebase", Toast.LENGTH_SHORT).show();
        }
    }
}
