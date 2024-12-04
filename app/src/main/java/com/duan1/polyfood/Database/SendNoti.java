package com.duan1.polyfood.Database;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;


/** @noinspection deprecation*/
public class SendNoti {

    Context context;

    public SendNoti(Context context) {
        this.context = context;
    }

    /** @noinspection deprecation*/
    public void sendNoti(){
        String topic = "highScores";



        // Xây dựng thông báo với RemoteMessage.Builder
        RemoteMessage message = new RemoteMessage.Builder(topic + "@topics")
                .addData("score", "850")
                .addData("time", "2:45")
                .build();

       FirebaseMessaging.getInstance().send(message);

    }

    public void  subTopic(){
        FirebaseMessaging.getInstance().subscribeToTopic("weather").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed";
                        if (!task.isSuccessful()) {
                            msg = "Subscribe failed";
                        }
                        Log.d("zzzzzzz", msg);
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
