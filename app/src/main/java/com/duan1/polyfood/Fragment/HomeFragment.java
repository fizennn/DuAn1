package com.duan1.polyfood.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duan1.polyfood.Adapter.FoodAdapter;
import com.duan1.polyfood.Adapter.ThucDonNgangAdapter;
import com.duan1.polyfood.Database.ThucDonDAO;
import com.duan1.polyfood.Models.ThucDon;
import com.duan1.polyfood.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    String TAG = "zzzzzzzzzzzz";
    private RecyclerView recyclerView,recyclerViewNgang;
    private FoodAdapter foodAdapter;
    private List<ThucDon> foodList,foodListNgang;
    private ThucDonNgangAdapter thucDonNgangAdapter;
    private ThucDonDAO thucDonDAO;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        thucDonDAO = new ThucDonDAO();

        recyclerView = view.findViewById(R.id.recyclerview1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);


        // Dữ liệu mẫu
        foodList = new ArrayList<>();

        ThucDon thucDon = new ThucDon();

        thucDon.setTen("Pho Sieu Ngon");
        thucDon.setDanhGia("5");

        foodList.add(thucDon);
        foodList.add(thucDon);
        foodList.add(thucDon);

        foodAdapter = new FoodAdapter(getContext(), foodList);
        recyclerView.setAdapter(foodAdapter);


        recyclerViewNgang = view.findViewById(R.id.recyclerview2);
        recyclerViewNgang.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        foodListNgang = new ArrayList<>();

        thucDonDAO.getAllThucDon(new ThucDonDAO.FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<ThucDon> thucDonList) {
                foodListNgang.clear();
                for (ThucDon don : thucDonList){
                    foodListNgang.add(don);
                }
                thucDonNgangAdapter = new ThucDonNgangAdapter(foodListNgang,getContext());
                recyclerViewNgang.setAdapter(thucDonNgangAdapter);
            }

            @Override
            public void onCallback(ThucDon thucDon) {

            }
        });




        return view;
    }
}