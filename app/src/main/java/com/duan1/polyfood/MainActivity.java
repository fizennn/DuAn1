package com.duan1.polyfood;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.Manifest;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.duan1.polyfood.Database.AuthenticationFireBaseHelper;
import com.duan1.polyfood.Database.MyFirebaseMessagingService;
import com.duan1.polyfood.Database.NguoiDungDAO;
import com.duan1.polyfood.Database.ThongBaoDao;
import com.duan1.polyfood.Fragment.BillFragment;
import com.duan1.polyfood.Fragment.FavoriteFragment;
import com.duan1.polyfood.Fragment.HomeFragment;
import com.duan1.polyfood.Fragment.ProfileFragment;
import com.duan1.polyfood.Fragment.ProfileUserFragment;
import com.duan1.polyfood.Models.ThongBao;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



    private String TAG = "FixLoi1";
    public ActivityResultLauncher<Intent> launcherTool;
    public NavigationView navigationView;
    public AuthenticationFireBaseHelper authHelper;
    private MyFirebaseMessagingService myFirebaseMessagingService;
    private DrawerLayout drawer;
    private NguoiDungDAO nguoiDungDAO = new NguoiDungDAO();


    private ImageButton imageButton;
    private ImageView ivNoti;
    private TextView tvNotiCount;

    private ThongBaoDao thongBaoDao;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);



        myFirebaseMessagingService = new MyFirebaseMessagingService();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // Nếu chưa có quyền, yêu cầu quyền
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1001);
            }
        }

        thongBaoDao = new ThongBaoDao();






        //Khai Bao

        Log.d(TAG, "MainActivity OnCreate");

        authHelper = new AuthenticationFireBaseHelper();

        //Hien Thi Thong Tin Nav







        launcherTool = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {

            }
        });

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageButton = toolbar.findViewById(R.id.toolbar_right_button);
        ivNoti = toolbar.findViewById(R.id.ivNoti);
        tvNotiCount = toolbar.findViewById(R.id.tvNotiCount);

        ivNoti.setVisibility(View.GONE);
        tvNotiCount.setVisibility(View.GONE);


        thongBaoDao.getNoti(new ThongBaoDao.FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<ThongBao> thongBaoList) {
                if (thongBaoList.size()>=1){
                    ivNoti.setVisibility(View.VISIBLE);
                    tvNotiCount.setVisibility(View.VISIBLE);

                    tvNotiCount.setText(String.valueOf(thongBaoList.size()));
                }else{
                    ivNoti.setVisibility(View.GONE);
                    tvNotiCount.setVisibility(View.GONE);
                }
            }
        });


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ThongBaoActivity.class);
                startActivity(intent);
            }
        });

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View headerView = navigationView.getHeaderView(0);
        TextView txvHeaderName = headerView.findViewById(R.id.txv_heaher_name);
        txvHeaderName.setText(authHelper.getEmail());



        navigationView.setCheckedItem(R.id.nav_client);
        navigationView.getMenu().findItem(R.id.nav_client).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_delivery).setVisible(false);
        navigationView.getMenu().findItem(R.id.nav_restaurant).setVisible(false);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        // Set the home fragment as the default
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    int id = item.getItemId();
                    if(id==R.id.nav_home) {
                        selectedFragment = new HomeFragment();
                    }
                    if(id==R.id.nav_bill) {
                        selectedFragment = new BillFragment();
                    }
                    if(id==R.id.nav_favorite) {
                        selectedFragment = new FavoriteFragment();
                    }
                    if(id==R.id.nav_profile) {
                        selectedFragment = new ProfileUserFragment();
                    }


                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {



//
//        int id = item.getItemId();
//
//


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        navigationView.setCheckedItem(R.id.nav_client);

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        // Thêm logic bạn muốn thực hiện khi nhấn nút Back
        // Ví dụ: hiển thị một dialog xác nhận thoát
        new AlertDialog.Builder(this)
                .setMessage("Bạn có chắc chắn muốn thoát?")
                .setCancelable(false)
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Kết thúc Activity
                        finish();
                    }
                })
                .setNegativeButton("Không", null)
                .show();

        // Nếu không gọi super.onBackPressed(), Activity sẽ không tự động đóng khi nhấn Back
        // super.onBackPressed(); // Gọi nếu muốn giữ hành động mặc định

        
    }
 


}
