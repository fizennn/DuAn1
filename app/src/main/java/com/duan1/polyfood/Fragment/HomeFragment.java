package com.duan1.polyfood.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.duan1.polyfood.Adapter.FoodAdapter;
import com.duan1.polyfood.Adapter.StickerNgangAdapter;
import com.duan1.polyfood.Adapter.ThucDonNgangAdapter;
import com.duan1.polyfood.CartActivity;
import com.duan1.polyfood.Database.StickerDao;
import com.duan1.polyfood.Database.ThucDonDAO;
import com.duan1.polyfood.Models.Sticker;
import com.duan1.polyfood.Models.ThucDon;
import com.duan1.polyfood.R;
import com.duan1.polyfood.SearchActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {

    private String TAG = "FixLoi1";
    private RecyclerView recyclerView, recyclerViewNgang,recyclerViewSticker;
    private FoodAdapter foodAdapter;
    private List<ThucDon> foodList, foodListNgang;
    private ThucDonNgangAdapter thucDonNgangAdapter;
    private ThucDonDAO thucDonDAO;
    private List<ThucDon> listCart;
    private Gson gson;
    private CardView btnOpenCart;
    private TextView txvSl;
    private CardView txvNoti;
    private CardView imgSearch;
    private TextView txvChao;
    private LinearLayout linearLayout;
    private List<Sticker> stickerList;
    private StickerNgangAdapter stickerNgangAdapter;
    private StickerDao stickerDao;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Log.d(TAG, "Home Fragment : On Create");
        // Initialize data and UI components
        thucDonDAO = new ThucDonDAO();
        gson = new Gson();
        listCart = new ArrayList<>();
        btnOpenCart = view.findViewById(R.id.btnOpenCart);
        txvSl = view.findViewById(R.id.txvSoLuongIteam);
        txvNoti = view.findViewById(R.id.txvNoti);
        txvChao = view.findViewById(R.id.txvChao);
        linearLayout = view.findViewById(R.id.linearLayout);
        stickerList = new ArrayList<>();
        stickerDao = new StickerDao();

        recyclerViewSticker = view.findViewById(R.id.recyclerViewSticker);


        txvChao.setText(getGreeting());


        getSticker();

        // Set up RecyclerViews
        setupRecyclerViews(view);

        // Load cart data and update UI
        getCart();
        updateCartUI();

        //Mo cart
        btnOpenCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCart();
            }
        });


        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });






        stickerDao.getAll(new StickerDao.StickerCallback() {
            @Override
            public void onSuccess(Sticker sticker) {

            }

            @Override
            public void onSuccess(List<Sticker> stickerList1) {

                recyclerViewSticker.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


                stickerNgangAdapter = new StickerNgangAdapter(stickerList1, requireContext(), new StickerNgangAdapter.OnItemClickListener() {
                    @Override
                    public void onEdit(Sticker sticker) {

                    }

                    @Override
                    public void onDelete(Sticker sticker) {

                    }

                    @Override
                    public void onClick(Sticker sticker) {

                    }
                });
                recyclerViewSticker.setAdapter(stickerNgangAdapter);



            }

            @Override
            public void onFailure(String error) {

            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getCart();
        updateCartUI();
    }

    private void setupRecyclerViews(View view) {
        // Setup cho recyclerview1
        recyclerView = view.findViewById(R.id.recyclerview1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Tải dữ liệu vào foodList và cập nhật adapter cho recyclerview1
        foodList = new ArrayList<>();
        foodAdapter = new FoodAdapter(getContext(), foodList);
        recyclerView.setAdapter(foodAdapter);






        // Gọi hàm loadSuggestedDishes để lấy dữ liệu từ Firebase
        loadSuggestedDishes();

        // Setup and fetch data for second RecyclerView
        recyclerViewNgang = view.findViewById(R.id.recyclerview2);
        recyclerViewNgang.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        foodListNgang = new ArrayList<>();
        thucDonDAO.getAllThucDon(new ThucDonDAO.FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<ThucDon> thucDonList) {
                foodListNgang.clear();
                foodListNgang.addAll(thucDonList);
                thucDonNgangAdapter = new ThucDonNgangAdapter(foodListNgang, getContext());
                recyclerViewNgang.setAdapter(thucDonNgangAdapter);
            }

            @Override
            public void onCallback(ThucDon thucDon) {
                // Not used here
            }

            @Override
            public void onCallback(Float star) {

            }
        });
    }

    private void getCart() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("cart", MODE_PRIVATE);
        String json = sharedPreferences.getString("listCart", null);
        if (json != null) {
            Type type = new TypeToken<ArrayList<ThucDon>>() {}.getType();
            try {
                listCart = gson.fromJson(json, type);
                if (listCart == null) listCart = new ArrayList<>();

            } catch (Exception e) {

                listCart = new ArrayList<>();  // Ensure list is initialized
            }
        } else {

            listCart = new ArrayList<>();  // Ensure list is initialized
        }
    }

    private void updateCartUI() {
        if (listCart.isEmpty()) {
            btnOpenCart.setVisibility(View.GONE);
            txvNoti.setVisibility(View.GONE);
        } else {
            btnOpenCart.setVisibility(View.VISIBLE);
            txvNoti.setVisibility(View.VISIBLE);
            txvSl.setText(String.valueOf(listCart.size()));
        }
    }

    public void openCart(){
        Intent intent = new Intent(getContext(), CartActivity.class);
        getContext().startActivity(intent);
    }
    private void loadSuggestedDishes() {
        thucDonDAO.getSuggestedDishes(new ThucDonDAO.FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<ThucDon> suggestedDishes) {
                if (suggestedDishes == null || suggestedDishes.isEmpty()) {

                } else {

                    foodList.clear();
                    foodList.addAll(suggestedDishes);
                    foodAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView
                }
            }

            @Override
            public void onCallback(ThucDon thucDon) {
                // Không sử dụng callback này
            }

            @Override
            public void onCallback(Float star) {

            }
        });

        thucDonDAO.getAllThucDon(new ThucDonDAO.FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<ThucDon> thucDonList) {
                if (thucDonList == null || thucDonList.isEmpty()) {

                } else {
                    foodList.clear();

                    for (ThucDon don : thucDonList){
                        if (don.getGoiY()!=null){
                            foodList.add(don);
                        }
                    }
                    foodAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView
                }
            }

            @Override
            public void onCallback(ThucDon thucDon) {

            }

            @Override
            public void onCallback(Float star) {

            }
        });
    }

    public String getGreeting() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        if (hour >= 6 && hour < 12) {
            return "Buổi Sáng Tốt Lành";
        } else if (hour >= 12 && hour < 18) {
            return "Buổi Chiều Ấm Áp";
        } else if (hour >= 18 && hour < 22) {
            return "Buổi Tối Hạnh Phúc";
        } else {
            return "Chúc Bạn Ngủ Ngon";
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "HomeFragment onDestroy: ");
    }


    private void getSticker(){

    }
}
