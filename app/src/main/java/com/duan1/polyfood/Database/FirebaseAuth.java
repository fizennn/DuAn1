package com.duan1.polyfood.Database;

import android.content.Context;
import android.util.Log;

import com.google.auth.oauth2.GoogleCredentials;

import java.io.IOException;
import java.io.InputStream;

public class FirebaseAuth {
    private static final String TAG = "FirebaseAuth";
    private final Context context;

    public FirebaseAuth(Context context) {
        this.context = context; // Lưu context của ứng dụng
    }

    public interface Callback {
        void onSuccess(String token);
        void onFailure(Exception e);
    }

    public void getAccessToken(Callback callback) {
        new Thread(() -> {
            try {
                // Đọc file JSON từ thư mục assets
                InputStream serviceAccount = context.getAssets().open("key.json");

                // Khởi tạo GoogleCredentials từ tài khoản dịch vụ
                GoogleCredentials credentials = GoogleCredentials
                        .fromStream(serviceAccount)
                        .createScoped("https://www.googleapis.com/auth/firebase.messaging");

                // Lấy OAuth2 Access Token
                credentials.refreshIfExpired();
                String token = credentials.getAccessToken().getTokenValue();
                Log.d(TAG, "Access token retrieved successfully: " + token);
                callback.onSuccess(token);
            } catch (IOException e) {
                Log.e(TAG, "Failed to read service account file: " + e.getMessage());
                callback.onFailure(e);
            } catch (Exception e) {
                Log.e(TAG, "Failed to retrieve access token: " + e.getMessage());
                callback.onFailure(e);
            }
        }).start();
    }
}
