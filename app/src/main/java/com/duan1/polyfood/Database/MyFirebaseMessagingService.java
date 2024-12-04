package com.duan1.polyfood.Database;

import com.duan1.polyfood.MainActivity;
import com.duan1.polyfood.R;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCMService";


    public MyFirebaseMessagingService() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        // Xử lý lỗi nếu không lấy được token
                        Log.w("FCM", "Fetching FCM token failed", task.getException());
                        return;
                    }

                    // Lấy FCM Token
                    String token = task.getResult();
                    Log.d("FCM", "FCM Token: " + token);

                    // Gửi token lên server hoặc lưu trữ token
                });
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Kiểm tra xem có thông báo hay không
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();

            // Gọi hàm hiển thị thông báo
            showNotification(title, body);
        }
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(TAG, "New token: " + token);
        // Gửi token lên server nếu cần
    }



    private void showNotification(String title, String body) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Tạo PendingIntent
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
        );

        String channelId = "default_channel";

        // Tạo Notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.img)
                .setBadgeIconType(R.drawable.img)
                .setContentTitle(title)        // Tiêu đề thông báo
                .setContentText(body)          // Nội dung thông báo
                .setAutoCancel(true)           // Tự động hủy khi người dùng nhấn
                .setPriority(NotificationCompat.PRIORITY_HIGH) // Đặt ưu tiên cao
                .setCategory(NotificationCompat.CATEGORY_ALARM) // Đảm bảo thông báo "hiện lên"
                .setContentIntent(pendingIntent);  // Gán PendingIntent     // Mở MainActivity khi nhấn vào thông báo

//        notificationBuilder.addAction(
//                R.drawable.img, // Icon của nút
//                "Reply",               // Văn bản hiển thị
//                pendingIntent     // Intent khi người dùng nhấn nút
//        );

        // Kiểm tra và tạo NotificationChannel cho Android 8.0 trở lên
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Default Channel",
                    NotificationManager.IMPORTANCE_HIGH // Đảm bảo kênh có mức độ ưu tiên cao
            );
            notificationManager.createNotificationChannel(channel);
        }

        // Gửi thông báo
        notificationManager.notify(0, notificationBuilder.build());
    }

}
