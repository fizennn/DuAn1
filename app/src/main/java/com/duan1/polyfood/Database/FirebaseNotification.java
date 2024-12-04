package com.duan1.polyfood.Database;

import android.content.Context;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStreamReader;
import java.io.BufferedReader;


/** @noinspection ExtractMethodRecommender*/
public class FirebaseNotification {

    private static final String PROJECT_ID = "shopcake-528de"; // Project ID của bạn

    public void senNoti(Context context,String title,String body,String topic){
        NotificationSender sender = new NotificationSender();

        FirebaseAuth firebaseAuth = new FirebaseAuth(context); // Truyền context của activity
        try {
            firebaseAuth.getAccessToken(new FirebaseAuth.Callback() {
                @Override
                public void onSuccess(String token) {
                    FirebaseNotification firebaseNotification = new FirebaseNotification();
                    firebaseNotification.sendPushNotification(token, title, body,topic);
                }

                @Override
                public void onFailure(Exception e) {

                }
            }); // Lấy Access Token

            // Tạo thông báo và gửi

        } catch (IOException e) {
        }
    }

    /** @noinspection ExtractMethodRecommender*/
    public void sendPushNotification(String accessToken, String title, String body, String topic) {

        new Thread(() -> {
            try {
                // URL FCM v1 API
                String apiUrl = "https://fcm.googleapis.com/v1/projects/" + PROJECT_ID + "/messages:send";
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Cấu hình HTTP Request
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", "Bearer " + accessToken); // Dùng Access Token
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                // JSON Payload cho Notification
                String payload = "{"
                        + "\"message\": {"
                        + "\"topic\": \""+topic+"\"," // Gửi đến topic "highScores"
                        + "\"notification\": {"
                        + "\"title\": \"" + title + "\","
                        + "\"body\": \"" + body + "\""
                        + "}"
                        + "}"
                        + "}";

                // Gửi request
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(payload.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                // Kiểm tra phản hồi
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    System.out.println("Response: " + response.toString()); // Thành công
                } else {
                    System.out.println("Failed to send notification. Response Code: " + responseCode); // Lỗi
                }

            } catch (Exception e) {
            }
        }).start();
    }
}