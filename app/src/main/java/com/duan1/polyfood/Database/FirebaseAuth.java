package com.duan1.polyfood.Database;

import android.content.Context;
import com.google.auth.oauth2.GoogleCredentials;
import java.io.IOException;
import java.io.InputStream;

public class FirebaseAuth {
    private final Context context;

    public FirebaseAuth(Context context) {
        this.context = context; // Lưu context của ứng dụng
    }
    public interface Callback {
        void onSuccess(String token);
        void onFailure(Exception e);
    }

    public void getAccessToken(Callback callback) throws IOException {

        new Thread(() -> {
            try {
                // Đọc file JSON từ thư mục assets
                InputStream serviceAccount = context.getAssets().open("shopcake-528de-b69a332768f7.json");

                // Khởi tạo GoogleCredentials từ tài khoản dịch vụ
                GoogleCredentials credentials = GoogleCredentials
                        .fromStream(serviceAccount)
                        .createScoped("https://www.googleapis.com/auth/firebase.messaging");

                // Lấy OAuth2 Access Token
                credentials.refreshIfExpired();
                callback.onSuccess(credentials.getAccessToken().getTokenValue());
            }catch (Exception e){
            }


        }).start();

    }
}
