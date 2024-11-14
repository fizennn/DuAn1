package com.duan1.polyfood;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.duan1.polyfood.Adapter.BinhLuanAdapter;
import com.duan1.polyfood.Database.AuthenticationFireBaseHelper;
import com.duan1.polyfood.Database.BinhLuanDao;
import com.duan1.polyfood.Database.NguoiDungDAO;
import com.duan1.polyfood.Database.ThucDonDAO;
import com.duan1.polyfood.Models.BinhLuan;
import com.duan1.polyfood.Models.NguoiDung;
import com.duan1.polyfood.Models.ThucDon;
import com.duan1.polyfood.Other.IntToVND;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MonAnActivity extends AppCompatActivity {

    private String UID;
    private String TAG = "FixLoi1";
    private ThucDonDAO thucDonDAO;
    private ThucDon thucDon1;
    private TextView ten, gia, mota, sao, sl;
    private ImageView img, sao1, sao2, sao3, sao4, sao5, btnremove, btnadd;
    private IntToVND vnd;
    private int soLuong;
    private List<ThucDon> listCart;
    private Gson gson;
    private LinearLayout linearLayout;



    private ImageView saorate1,saorate2,saorate3,saorate4,saorate5,imgProfileComment,imgadd,imgdelete;
    private ImageButton btnImgAdd,btnSendComment;
    private EditText editTextComment;
    private int rateStar;
    private String comment;
    private Uri imageUri;
    private LinearLayout linearLayout1;
    private BinhLuanDao binhLuanDao;
    private AuthenticationFireBaseHelper baseHelper;
    private NguoiDungDAO nguoiDungDAO;
    private NguoiDung nguoiDung1;
    private RecyclerView recyclerView;
    private List<BinhLuan> binhLuanList;
    private BinhLuanAdapter adapter;



    private ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    imgadd.setImageURI(imageUri);
                    linearLayout1.setVisibility(View.VISIBLE); // Hiển thị lại
                }
            });


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_an);

        Log.d(TAG, "MonAnActivity OnCreate");

        thucDonDAO = new ThucDonDAO();
        thucDon1 = new ThucDon();
        vnd = new IntToVND();
        gson = new Gson();
        listCart = new ArrayList<>();
        soLuong = 1;

        UID = getIntent().getStringExtra("UID");

        ten = findViewById(R.id.txvTenChiTiet);
        gia = findViewById(R.id.txvgiachitiet);
        mota = findViewById(R.id.txvmotachitiet);
        sao = findViewById(R.id.txvSao);
        img = findViewById(R.id.imgChiTiet);
        sao1 = findViewById(R.id.imgStar1);
        sao2 = findViewById(R.id.imgStar2);
        sao3 = findViewById(R.id.imgStar3);
        sao4 = findViewById(R.id.imgStar4);
        sao5 = findViewById(R.id.imgStar5);
        btnremove = findViewById(R.id.btnDeleChiTiet);
        btnadd = findViewById(R.id.btnAddChiTiet);
        sl = findViewById(R.id.txvSoLuongChiTiet);
        linearLayout = findViewById(R.id.linerAddToCart);




        saorate1 = findViewById(R.id.saorate1);
        saorate2 = findViewById(R.id.saorate2);
        saorate3 = findViewById(R.id.saorate3);
        saorate4 = findViewById(R.id.saorate4);
        saorate5 = findViewById(R.id.saorate5);
        imgProfileComment = findViewById(R.id.imgProfileComment);
        btnImgAdd = findViewById(R.id.btnImgAdd);
        btnSendComment = findViewById(R.id.btnSendComment);
        editTextComment = findViewById(R.id.edtComment);
        imgadd = findViewById(R.id.imgAddHienThi);
        imgdelete = findViewById(R.id.imgXoa);
        linearLayout1 = findViewById(R.id.layoutAnh);


        rateStar = 0;


        linearLayout1.setVisibility(View.GONE);

        binhLuanDao = new BinhLuanDao();
        baseHelper = new AuthenticationFireBaseHelper();
        nguoiDungDAO = new NguoiDungDAO();
        nguoiDung1 = new NguoiDung();

        nguoiDungDAO.getAllNguoiDung(new NguoiDungDAO.FirebaseCallback() {
            @Override
            public void onCallback(NguoiDung nguoiDung) {
                nguoiDung1=nguoiDung;
                loadImageFromUrl(nguoiDung.getimgUrl(),imgProfileComment);
            }
        });


        imgdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUri = null;
                linearLayout1.setVisibility(View.GONE);
            }
        });

        btnSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MonAnActivity.this, "Da Gui Binh Luan", Toast.LENGTH_SHORT).show();
                BinhLuan binhLuan = new BinhLuan();
                binhLuan.setId(baseHelper.getUID());
                binhLuan.setBl(editTextComment.getText().toString().trim());
                binhLuan.setSao(rateStar);
                binhLuan.setTen(nguoiDung1.getHoTen());
                binhLuanDao.addBinhLuan(binhLuan,thucDon1.getId_td(),imageUri);

                rateStar = 0;
                cancleAll();
                linearLayout1.setVisibility(View.GONE);
                editTextComment.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                // Nếu bàn phím đang hiển thị, ẩn nó
                if (imm != null && getCurrentFocus() != null) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        binhLuanList = new ArrayList<>();


















        // Lấy dữ liệu từ Firebase và cập nhật UI
        thucDonDAO.getAThucDon(new ThucDonDAO.FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<ThucDon> thucDonList) { }

            @Override
            public void onCallback(ThucDon thucDon) {
                thucDon1 = thucDon;
                ten.setText(thucDon.getTen());
                gia.setText(vnd.convertToVND(thucDon.getGia()));
                mota.setText(thucDon.getMoTa());
                sao.setText(thucDon.getDanhGia());

                if (!isFinishing()) {
                    Glide.with(MonAnActivity.this)
                            .load(thucDon.getHinhAnh())
                            .placeholder(R.drawable.load)
                            .error(R.drawable.load)
                            .into(img);
                }

                binhLuanDao.getBinhLuan(thucDon.getId_td(), new BinhLuanDao.FirebaseCallback() {
                    @Override
                    public void onCallback(ArrayList<BinhLuan> binhLuanList) {
                        adapter = new BinhLuanAdapter(MonAnActivity.this, binhLuanList);
                        recyclerView.setAdapter(adapter);
                    }
                });

                // Xử lý đánh giá sao (giữ nguyên đoạn mã của bạn để cập nhật sao)
            }
        }, UID);




        Handler handler = new Handler();
        Runnable incrementRunnable = new Runnable() {
            @Override
            public void run() {
                if (soLuong < 99) {
                    soLuong++;
                    changeCost();
                }
                handler.postDelayed(this, 100); // Lặp lại sau mỗi 100ms khi giữ nút tăng
            }
        };
        Runnable decrementRunnable = new Runnable() {
            @Override
            public void run() {
                if (soLuong > 1) { // Kiểm tra để không giảm dưới 0
                    soLuong--;
                    changeCost();
                }
                handler.postDelayed(this, 100); // Lặp lại sau mỗi 100ms khi giữ nút giảm
            }
        };

        // Xử lý cho nút tăng
        btnadd.setOnTouchListener(new View.OnTouchListener() {
            private boolean isLongPress = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isLongPress = false;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isLongPress = true;
                                handler.post(incrementRunnable);
                            }
                        }, 1000); // Thời gian nhấn giữ: 1 giây
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        handler.removeCallbacks(incrementRunnable);
                        if (!isLongPress && soLuong < 99) {
                            soLuong++;
                            changeCost();
                        }
                        handler.removeCallbacksAndMessages(null);
                        break;
                }
                return true;
            }
        });

        // Xử lý cho nút giảm
        btnremove.setOnTouchListener(new View.OnTouchListener() {
            private boolean isLongPress = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isLongPress = false;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isLongPress = true;
                                handler.post(decrementRunnable);
                            }
                        }, 1000); // Thời gian nhấn giữ: 1 giây
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        handler.removeCallbacks(decrementRunnable);
                        if (!isLongPress && soLuong > 1) {
                            soLuong--;
                            changeCost();
                        }
                        handler.removeCallbacksAndMessages(null);
                        break;
                }
                return true;
            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thucDon1.setSoLuong(soLuong);
                addToCart(thucDon1);
                getCart();
                for (ThucDon don : listCart) {
                }


                // di chuyen sang trang Pay Acitivity
                Intent intent = new Intent(MonAnActivity.this, PayActivity.class);
                intent.putExtra("UID", UID);
                intent.putExtra("SO_LUONG", soLuong);
                startActivity(intent);
            }
        });



        saorate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancleAll();
                rateStar = 1;
                choiceStar();
            }
        });

        saorate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancleAll();
                rateStar = 2;
                choiceStar();
            }
        });

        saorate3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancleAll();
                rateStar = 3;
                choiceStar();
            }
        });

        saorate4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancleAll();
                rateStar = 4;
                choiceStar();
            }
        });

        saorate5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancleAll();
                rateStar = 5;
                choiceStar();
            }
        });







        btnImgAdd.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            pickImageLauncher.launch(intent);
        });







    }

    public void changeCost() {
        gia.setText(vnd.convertToVND(thucDon1.getGia() * soLuong));
        sl.setText(String.valueOf(soLuong));
    }

    public void addToCart(ThucDon thucDon) {
        SharedPreferences sharedPreferences = getSharedPreferences("cart", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        getCart();
        listCart.add(thucDon);
        String json = gson.toJson(listCart);
        editor.putString("listCart", json);
        editor.apply();
        Toast.makeText(this, "Thêm Thành Công", Toast.LENGTH_SHORT).show();
    }

    public void getCart() {
        SharedPreferences sharedPreferences = getSharedPreferences("cart", MODE_PRIVATE);
        String json = sharedPreferences.getString("listCart", null);

        Type type = new TypeToken<ArrayList<ThucDon>>() {}.getType();
        if (json != null) {
            try {
                listCart = gson.fromJson(json, type);
            } catch (Exception e) {
                listCart = new ArrayList<>();
            }
        } else {
            listCart = new ArrayList<>();
        }
    }

    public void cancleAll(){
        saorate1.setImageResource(R.drawable.star_empty);
        saorate2.setImageResource(R.drawable.star_empty);
        saorate3.setImageResource(R.drawable.star_empty);
        saorate4.setImageResource(R.drawable.star_empty);
        saorate5.setImageResource(R.drawable.star_empty);
    }

    public void choiceStar(){

        if (rateStar>=1){
            saorate1.setImageResource(R.drawable.star50);
        }
        if (rateStar>=2){
            saorate2.setImageResource(R.drawable.star50);
        }
        if (rateStar>=3){
            saorate3.setImageResource(R.drawable.star50);
        }
        if (rateStar>=4){
            saorate4.setImageResource(R.drawable.star50);
        }
        if (rateStar>=5){
            saorate5.setImageResource(R.drawable.star50);
        }
    }

    private void loadImageFromUrl(String imageUrl,ImageView img) {
        if (MonAnActivity.this != null) {
            Glide.with(MonAnActivity.this)
                    .load(imageUrl)
                    .placeholder(R.drawable.load)
                    .error(R.drawable.load)
                    .into(img);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "MonAnActivity onDestroy ");
    }
}
