package com.duan1.polyfood;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duan1.polyfood.Adapter.ThongBaoAdapter;
import com.duan1.polyfood.Database.ThongBaoDao;
import com.duan1.polyfood.Models.ThongBao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ThongBaoActivity extends AppCompatActivity {

    private RecyclerView rcvThongBao;

    private ThongBaoDao thongBaoDao;
    private ThongBaoAdapter thongBaoAdapter;

    private ImageButton imageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_bao);

        init();

        loadThongBao();

        //Back
        imageButton.setOnClickListener(v -> {finish();});




    }

    public void init(){
        rcvThongBao = findViewById(R.id.rcvThongBao);
        imageButton = findViewById(R.id.imgBack);

        thongBaoDao = new ThongBaoDao();


    }

    public void loadThongBao(){
        thongBaoDao.getAllThongBao(new ThongBaoDao.FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<ThongBao> thongBaoList) {
                Collections.sort(thongBaoList, new Comparator<ThongBao>() {
                    @Override
                    public int compare(ThongBao p1, ThongBao p2) {
                        return p2.getNgayThang().compareTo(p1.getNgayThang());
                    }
                });


                thongBaoAdapter = new ThongBaoAdapter(thongBaoList,ThongBaoActivity.this);
                rcvThongBao.setLayoutManager(new LinearLayoutManager(ThongBaoActivity.this));
                rcvThongBao.setAdapter(thongBaoAdapter);
            }
        });
    }
}