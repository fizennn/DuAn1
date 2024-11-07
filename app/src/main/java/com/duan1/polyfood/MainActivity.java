package com.duan1.polyfood;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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
import com.duan1.polyfood.Fragment.BillFragment;
import com.duan1.polyfood.Fragment.FavoriteFragment;
import com.duan1.polyfood.Fragment.HomeFragment;
import com.duan1.polyfood.Fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    String TAG = "zzzzzzzzzzz";

    public ActivityResultLauncher<Intent> launcherTool;
    public NavigationView navigationView;
    public AuthenticationFireBaseHelper authHelper;

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //Khai Bao

        authHelper = new AuthenticationFireBaseHelper();

        //Hien Thi Thong Tin Nav


        Log.d(TAG, "onCreate: "+authHelper.getEmail());

        launcherTool = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {

            }
        });

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View headerView = navigationView.getHeaderView(0);
        TextView txvHeaderName = headerView.findViewById(R.id.txv_heaher_name);
        txvHeaderName.setText(authHelper.getEmail());



        navigationView.setCheckedItem(R.id.nav_client);

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
                        selectedFragment = new ProfileFragment();
                    }


                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Log.d(TAG, "onNavigationItemSelected: SlideBar Nav");


        int id = item.getItemId();


        if (id==R.id.nav_client){

        }
        if (id==R.id.nav_delivery){
            Intent i  = new Intent(MainActivity.this,DeliveryActivity.class);
            launcherTool.launch(i);
        }
        if (id==R.id.nav_restaurant){
            Intent i  = new Intent(MainActivity.this,RestaurantActivity.class);
            launcherTool.launch(i);

        }









        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(TAG, "onStop: STOP");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        navigationView.setCheckedItem(R.id.nav_client);
        Log.d(TAG, "onStop: Restart");
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
