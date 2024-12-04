package com.duan1.polyfood.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airbnb.lottie.LottieAnimationView;
import com.duan1.polyfood.Adapter.HoaDonPageAdapter;
import com.duan1.polyfood.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class BillFragment extends Fragment {

    private LottieAnimationView loading;
    private View viewLoad;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bill, container, false);

        loading = view.findViewById(R.id.lottieLoading);
        viewLoad = view.findViewById(R.id.viewLoad);


        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager2 viewPager = view.findViewById(R.id.viewPager);

        // Tạo và thiết lập adapter cho ViewPager2
        HoaDonPageAdapter hoaDonPageAdapter = new HoaDonPageAdapter(getActivity());
        viewPager.setAdapter(hoaDonPageAdapter);

        // Liên kết TabLayout với ViewPager2
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Tất cả");
                            break;
                        case 1:
                            tab.setText("Chờ xử lý");
                            break;
                        case 2:
                            tab.setText("Chờ giao");
                            break;
                        case 3:
                            tab.setText("Đang giao");
                            break;
                        case 4:
                            tab.setText("Hoàn thành");
                            break;
                    }
                }).attach();

        return view;
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