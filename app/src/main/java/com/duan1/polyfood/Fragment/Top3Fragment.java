package com.duan1.polyfood.Fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.duan1.polyfood.Adapter.Top3Adapter;
import com.duan1.polyfood.Database.HoaDonDAO;
import com.duan1.polyfood.Database.ThucDonDAO;
import com.duan1.polyfood.Models.HoaDon;
import com.duan1.polyfood.Models.ThucDon;
import com.duan1.polyfood.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class Top3Fragment extends Fragment {

    private TextView tvDoanhThu, tvStartDate, tvEndDate;
    private String startDate, endDate;
    private Top3Adapter top3Adapter;
    private ArrayList<ThucDon> top3DishesList;

    private LottieAnimationView loading;
    private View viewLoad;



    public void loading(){
        loading.setVisibility(View.VISIBLE);
        viewLoad.setVisibility(View.VISIBLE);
    }

    public void loaded(){
        loading.setVisibility(View.GONE);
        viewLoad.setVisibility(View.GONE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_statistic, container, false);
        View view = inflater.inflate(R.layout.fragment_top3, container, false);



        loading = view.findViewById(R.id.lottieLoading);
        viewLoad = view.findViewById(R.id.viewLoad);

        loading();

        RecyclerView recyclerViewTopDishes = view.findViewById(R.id.recyclerViewTopDishes);
        recyclerViewTopDishes.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Khởi tạo danh sách món ăn top 3
        top3DishesList = new ArrayList<>();
        top3Adapter = new Top3Adapter(getContext(), top3DishesList);
        recyclerViewTopDishes.setAdapter(top3Adapter);

        // Gọi phương thức lấy top 3 món ăn từ Firebase
        ThucDonDAO thucDonDAO = new ThucDonDAO();
        thucDonDAO.getTop3ThucDon(new ThucDonDAO.FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<ThucDon> top3ThucDon) {
                // Cập nhật danh sách top 3 món ăn
                top3DishesList.clear();
                top3DishesList.addAll(top3ThucDon);

                // Cập nhật adapter để hiển thị dữ liệu
                top3Adapter.notifyDataSetChanged();
                loaded();
            }

            @Override
            public void onCallback(ThucDon thucDon) {
            }

            @Override
            public void onCallback(Float star) {
            }
        });








        return view;
    }


}