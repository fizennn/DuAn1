package com.duan1.polyfood;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.duan1.polyfood.Adapter.ThongBaoAdapter;
import com.duan1.polyfood.Database.ThongBaoDao;
import com.duan1.polyfood.Models.ThongBao;

import java.util.ArrayList;

public class ThongBaoActivity extends AppCompatActivity {

    private RecyclerView rcvThongBao;

    private ThongBaoDao thongBaoDao;
    private ThongBaoAdapter thongBaoAdapter;

    private ImageButton imageButton;

    private LottieAnimationView loading,empty;
    private View viewLoad;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_bao);

        init();

        loading();

        loadThongBao();

        //Back
        imageButton.setOnClickListener(v -> {finish();});




    }

    public void init(){
        rcvThongBao = findViewById(R.id.rcvThongBao);
        imageButton = findViewById(R.id.imgBack);
        loading = findViewById(R.id.lottieLoading);
        viewLoad = findViewById(R.id.viewLoad);
        empty = findViewById(R.id.empty);

        thongBaoDao = new ThongBaoDao();



    }

    public void loadThongBao(){
        thongBaoDao.getAllThongBao(new ThongBaoDao.FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<ThongBao> thongBaoList) {
                if (thongBaoList==null){
                    loaded();
                    empty.setVisibility(View.VISIBLE);
                    return;
                }
                thongBaoAdapter = new ThongBaoAdapter(thongBaoList, ThongBaoActivity.this, new ThongBaoAdapter.onLoad() {
                    @Override
                    public void onLoad(int i) {
                        loaded();
                    }
                });
                rcvThongBao.setLayoutManager(new LinearLayoutManager(ThongBaoActivity.this, LinearLayoutManager.VERTICAL, false));
                rcvThongBao.setAdapter(thongBaoAdapter);
            }
        });
    }
    public void loading(){
        loading.setVisibility(View.VISIBLE);
        viewLoad.setVisibility(View.VISIBLE);
    }

    public void loaded(){
        loading.setVisibility(View.GONE);
        viewLoad.setVisibility(View.GONE);
    }
}