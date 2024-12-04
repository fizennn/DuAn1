package com.duan1.polyfood.Database;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NotificationSender {

    public void subTopic(){
        FirebaseMessaging.getInstance().subscribeToTopic("highScores")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        System.out.println("Subscribed to topic successfully.");
                        SendNoti();
                    }
                });
    }

    public void SendNoti(){

        new Thread(() -> {
            String apiUrl = "https://fcm.googleapis.com/fcm/send";
            String serverKey ="AIzaSyB3IEoe9O_GDfmN2hp5jIKZ6lJ_WlUS0Hk"; // Thay bằng Server Key của bạn
            String topic = "highScores";

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "key=" + serverKey);
                connection.setDoOutput(true);

                // JSON payload
                String payload = "{"
                        + "\"to\": \"/topics/" + topic + "\","
                        + "\"data\": {"
                        + "\"score\": \"850\","
                        + "\"time\": \"2:45\""
                        + "},"
                        + "\"notification\": {"
                        + "\"title\": \"New High Score!\","
                        + "\"body\": \"A new high score of 850 has been achieved!\","
                        + "\"click_action\": \"FLUTTER_NOTIFICATION_CLICK\""
                        + "}"
                        + "}";

                // Gửi request
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(payload.getBytes());
                outputStream.flush();
                outputStream.close();

                // Kiểm tra phản hồi
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.d("zzzzzzzzzzzz", "Notification sent successfully.");
                } else {
                    Log.d("zzzzzzzzzzz", "Failed to send notification. Response Code: " + responseCode);
                }

                InputStream inputStream;
                if (responseCode >= 200 && responseCode < 300) {
                    inputStream = connection.getInputStream();
                } else {
                    inputStream = connection.getErrorStream();
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                Log.d("FCM_RESPONSE", response.toString());

            } catch (Exception e) {
            }
        }).start();




    }

}


