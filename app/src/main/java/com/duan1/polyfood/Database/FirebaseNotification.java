package com.duan1.polyfood.Database;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class FirebaseNotification {

    private static final String PROJECT_ID = "shopcake-528de"; // Project ID của bạn

    public void sendNotification(Context context, String title, String body, String topic) {
        Log.d("FirebaseNotification", "Initiating notification process...");
        FirebaseAuth firebaseAuth = new FirebaseAuth(context); // Firebase Auth class

        try {
            // Lấy Access Token từ FirebaseAuth
            firebaseAuth.getAccessToken(new FirebaseAuth.Callback() {
                @Override
                public void onSuccess(String token) {
                    Log.d("FirebaseNotification", "Access token retrieved successfully");
                    sendPushNotification(token, title, body, topic);
                }

                @Override
                public void onFailure(Exception e) {
                    Log.e("FirebaseNotification", "Failed to retrieve access token: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("FirebaseNotification", "Error while initiating notification: " + e.getMessage());
        }
    }

    public void sendPushNotification(String accessToken, String title, String body, String topic) {
        Log.d("FirebaseNotification", "Sending push notification...");

        new Thread(() -> {
            HttpURLConnection connection = null;
            try {
                // URL của FCM API
                String apiUrl = "https://fcm.googleapis.com/v1/projects/" + PROJECT_ID + "/messages:send";
                URL url = new URL(apiUrl);
                connection = (HttpURLConnection) url.openConnection();

                // Cấu hình HTTP Request
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", "Bearer " + accessToken); // Dùng Access Token
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                // JSON Payload
                String payload = String.format(
                        "{"
                                + "\"message\": {"
                                + "\"topic\": \"%s\","
                                + "\"notification\": {"
                                + "\"title\": \"%s\","
                                + "\"body\": \"%s\""
                                + "}"
                                + "}"
                                + "}",
                        topic, title, body
                );

                // Gửi dữ liệu
                try (OutputStream outputStream = connection.getOutputStream()) {
                    outputStream.write(payload.getBytes(StandardCharsets.UTF_8));
                    outputStream.flush();
                }

                // Đọc phản hồi
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        String inputLine;
                        StringBuilder response = new StringBuilder();
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        Log.d("FirebaseNotification", "Notification sent successfully: " + response);
                    }
                } else {
                    try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
                        String inputLine;
                        StringBuilder errorResponse = new StringBuilder();
                        while ((inputLine = errorReader.readLine()) != null) {
                            errorResponse.append(inputLine);
                        }
                        Log.e("FirebaseNotification", "Failed to send notification. Response Code: " + responseCode +
                                ", Error: " + errorResponse);
                    }
                }

            } catch (Exception e) {
                Log.e("FirebaseNotification", "Exception while sending notification: " + e.getMessage());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }).start();
    }
}
