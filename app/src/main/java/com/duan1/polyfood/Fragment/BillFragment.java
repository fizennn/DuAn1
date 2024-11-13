package com.duan1.polyfood.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duan1.polyfood.Adapter.HoaDonAdapter;
import com.duan1.polyfood.Adapter.HoaDonPageAdapter;
import com.duan1.polyfood.Database.DonHangDAO;
import com.duan1.polyfood.Database.HoaDonDAO;
import com.duan1.polyfood.Models.DonHang;
import com.duan1.polyfood.Models.HoaDon;
import com.duan1.polyfood.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;


public class BillFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private HoaDonPageAdapter hoaDonPageAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bill, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

        // Tạo và thiết lập adapter cho ViewPager2
        hoaDonPageAdapter = new HoaDonPageAdapter(getActivity());
        viewPager.setAdapter(hoaDonPageAdapter);

        // Liên kết TabLayout với ViewPager2
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Chờ xử lý");
                            break;
                        case 1:
                            tab.setText("Chờ giao");
                            break;
                        case 2:
                            tab.setText("Đang giao");
                            break;
                        case 3:
                            tab.setText("Hoàn thành");
                            break;
                    }
                }).attach();

        return view;
    }
}