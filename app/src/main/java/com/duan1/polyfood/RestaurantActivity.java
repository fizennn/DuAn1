package com.duan1.polyfood;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.duan1.polyfood.Fragment.DishFragment;
import com.duan1.polyfood.Fragment.DishSuggestFragment;
import com.duan1.polyfood.Fragment.HomeFragment;
import com.duan1.polyfood.Fragment.OrderFragment;
import com.duan1.polyfood.Fragment.ProfileFragment;
import com.duan1.polyfood.Fragment.ProfileResFragment;
import com.duan1.polyfood.Fragment.StatisticFragment;
import com.duan1.polyfood.Fragment.TagFragment;
import com.duan1.polyfood.Fragment.Top3Fragment;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;

/** @noinspection deprecation*/
public class RestaurantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_restaurant);

        setupToolbar();
        setupViewPager();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setupViewPager() {
        ViewPager viewPager = findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Tái sử dụng một instance của HomeFragment cho tất cả các tab, nếu không cần các fragment khác nhau
        DishFragment fragment1 = new DishFragment();
        DishSuggestFragment fragment5 = new DishSuggestFragment();
        OrderFragment fragment2 = new OrderFragment();
        StatisticFragment fragment3 = new StatisticFragment();
//        ProfileResFragment fragment4 = new ProfileResFragment();
        TagFragment fragment6 = new TagFragment();
        Top3Fragment fragmenttop3 = new Top3Fragment();


        adapter.addFragment(fragment1, "Món ăn");
        adapter.addFragment(fragment6, "Nhãn Dán");
        adapter.addFragment(fragment5, "Món ăn gợi ý");
        adapter.addFragment(fragment2, "Đơn hàng");
        adapter.addFragment(fragment3, "Thống kê");
        adapter.addFragment(fragmenttop3, "Bảng Xếp Hạng");

//        adapter.addFragment(fragment4, "Thông tin");

        viewPager.setAdapter(adapter);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    /** @noinspection deprecation*/
    private static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager manager) {
            super(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    /** @noinspection deprecation*/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
