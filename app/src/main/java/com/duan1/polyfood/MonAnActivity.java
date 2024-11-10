package com.duan1.polyfood;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.duan1.polyfood.Database.ThucDonDAO;
import com.duan1.polyfood.Models.ThucDon;
import com.duan1.polyfood.Other.IntToVND;

import java.util.ArrayList;

public class MonAnActivity extends AppCompatActivity {

    private String UID;
    private String TAG = "zzzzzzzzzzzz";
    private ThucDonDAO thucDonDAO;
    private ThucDon thucDon1;
    private TextView ten,gia,mota,sao,sl;
    private ImageView img,sao1,sao2,sao3,sao4,sao5,btnremove,btnadd;
    private IntToVND vnd;
    private int soLuong;
    private boolean holdAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_an);


        thucDonDAO = new ThucDonDAO();
        thucDon1 = new ThucDon();
        vnd = new IntToVND();

        soLuong = 1;
        holdAdd = false;

        UID = getIntent().getStringExtra("UID");

        Log.d(TAG, "onCreate: "+UID);

        ten = findViewById(R.id.txvTenChiTiet);
        gia = findViewById(R.id.txvgiachitiet);
        mota = findViewById(R.id.txvmotachitiet);
        sao = findViewById(R.id.txvSao);
        img = findViewById(R.id.imgChiTiet);
        sao1 = findViewById(R.id.imgStar1);
        sao2 = findViewById(R.id.imgStar2);
        sao3 = findViewById(R.id.imgStar3);
        sao4 = findViewById(R.id.imgStar4);
        sao5 = findViewById(R.id.imgStar5);
        btnremove = findViewById(R.id.btnDeleChiTiet);
        btnadd = findViewById(R.id.btnAddChiTiet);
        sl = findViewById(R.id.txvSoLuongChiTiet);



        thucDonDAO.getAThucDon(new ThucDonDAO.FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<ThucDon> thucDonList) {

            }

            @Override
            public void onCallback(ThucDon thucDon) {
                thucDon1 = thucDon;
                ten.setText(thucDon.getTen());
                gia.setText(vnd.convertToVND(thucDon.getGia()));
                mota.setText(thucDon.getMoTa());
                sao.setText(thucDon.getDanhGia());

                if (MonAnActivity.this != null) {
                    Glide.with(MonAnActivity.this)
                            .load(thucDon.getHinhAnh())
                            .placeholder(R.drawable.load)
                            .error(R.drawable.load)
                            .into(img);
                }


                float sao = Float.parseFloat(thucDon.getDanhGia());

                if (sao>4&&sao<=5){
                    if (sao==5){

                    }else{
                        sao5.setImageResource(R.drawable.starhalf50);
                    }
                }else {

                    if (sao<=4){
                        sao5.setImageResource(R.drawable.star_empty);
                    }
                }

                if (sao>3&&sao<=4){
                    if (sao==4){

                    }else{
                        sao4.setImageResource(R.drawable.starhalf50);
                    }
                }else {
                    if (sao<=3){
                        sao4.setImageResource(R.drawable.star_empty);
                    }

                }

                if (sao>2&&sao<=3){
                    if (sao==3){

                    }else{
                        sao3.setImageResource(R.drawable.starhalf50);
                    }
                }else {
                    if (sao<=2){
                        sao3.setImageResource(R.drawable.star_empty);
                    }

                }

                if (sao>1&&sao<2){
                    if (sao==2){

                    }else{
                        sao2.setImageResource(R.drawable.starhalf50);
                    }
                }else {
                    if (sao<=1){
                        sao2.setImageResource(R.drawable.star_empty);
                    }

                }

                if (sao>0&&sao<1){
                    if (sao==1){

                    }else{
                        sao1.setImageResource(R.drawable.starhalf50);
                    }
                }else {
                    if (sao<=0){
                        sao1.setImageResource(R.drawable.star_empty);
                    }
                }
            }
        },UID);






        Handler handler = new Handler();


        Runnable incrementRunnable = new Runnable() {
            @Override
            public void run() {
                if (soLuong < 99) {
                    soLuong++;
                    changeCost();
                }
                handler.postDelayed(this, 100); // Lặp lại sau mỗi 100ms khi giữ nút tăng
            }
        };

        Runnable decrementRunnable = new Runnable() {
            @Override
            public void run() {
                if (soLuong > 1) { // Kiểm tra để không giảm dưới 0
                    soLuong--;
                    changeCost();
                }
                handler.postDelayed(this, 100); // Lặp lại sau mỗi 100ms khi giữ nút giảm
            }
        };

// Xử lý cho nút tăng
        btnadd.setOnTouchListener(new View.OnTouchListener() {
            private boolean isLongPress = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isLongPress = false;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isLongPress = true;
                                handler.post(incrementRunnable);
                            }
                        }, 1000); // Thời gian nhấn giữ: 1 giây
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        handler.removeCallbacks(incrementRunnable);
                        if (!isLongPress && soLuong < 99) {
                            soLuong++;
                            changeCost();
                        }
                        handler.removeCallbacksAndMessages(null);
                        break;
                }
                return true;
            }
        });

// Xử lý cho nút giảm
        btnremove.setOnTouchListener(new View.OnTouchListener() {
            private boolean isLongPress = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isLongPress = false;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isLongPress = true;
                                handler.post(decrementRunnable);
                            }
                        }, 1000); // Thời gian nhấn giữ: 1 giây
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        handler.removeCallbacks(decrementRunnable);
                        if (!isLongPress && soLuong > 1) {
                            soLuong--;
                            changeCost();
                        }
                        handler.removeCallbacksAndMessages(null);
                        break;
                }
                return true;
            }
        });











    }

    public void changeCost(){
        gia.setText(vnd.convertToVND(thucDon1.getGia()*soLuong));
        sl.setText(String.valueOf(soLuong));
    }




}