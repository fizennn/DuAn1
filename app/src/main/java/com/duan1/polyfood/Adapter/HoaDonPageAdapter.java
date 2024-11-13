package com.duan1.polyfood.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.duan1.polyfood.Fragment.ChoGiaoFragment;
import com.duan1.polyfood.Fragment.ChoXuLyFragment;
import com.duan1.polyfood.Fragment.DangGiaoFragment;
import com.duan1.polyfood.Fragment.HoanThanhFragment;

public class HoaDonPageAdapter extends FragmentStateAdapter {
    public HoaDonPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ChoXuLyFragment(); // Chờ xử lý
            case 1:
                return new ChoGiaoFragment(); // Chờ giao
            case 2:
                return new DangGiaoFragment(); // Đang giao
            case 3:
                return new HoanThanhFragment(); // Hoàn thành
            default:
                return new ChoXuLyFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
