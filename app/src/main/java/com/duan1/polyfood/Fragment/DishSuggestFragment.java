package com.duan1.polyfood.Fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.duan1.polyfood.Adapter.DishSuggestAdapter;
import com.duan1.polyfood.Adapter.FoodAdapter;
import com.duan1.polyfood.Adapter.ThucDonAdapter;
import com.duan1.polyfood.Adapter.ThucDonNgangAdapter;
import com.duan1.polyfood.Adapter.ThucDonSuggestAdapter;
import com.duan1.polyfood.Database.ThucDonDAO;
import com.duan1.polyfood.Models.ThucDon;
import com.duan1.polyfood.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class DishSuggestFragment extends Fragment {

    private ImageView imgSelectedImage;
    private Uri imageUri;
    private ThucDonDAO thucDonDAO;
    private StorageReference storageReference;
    private RecyclerView recyclerView;
    private ThucDonSuggestAdapter thucDonSuggestAdapter;
    private FloatingActionButton btnAddDishSuggest;
    private List<ThucDon> foodList;
    private RecyclerView recyclerViewNgang;
    private List<ThucDon> foodListNgang;
    private ThucDonNgangAdapter thucDonNgangAdapter;



    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    imgSelectedImage.setImageURI(imageUri);
                }
            });


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dishsuggest, container, false);

        thucDonDAO = new ThucDonDAO();
        btnAddDishSuggest = view.findViewById(R.id.floatAddDishSuggest);
        recyclerView = view.findViewById(R.id.recyclerViewDishesSuggest);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        // Tải dữ liệu vào foodList và cập nhật adapter cho recyclerview1
        foodList = new ArrayList<>();
        thucDonSuggestAdapter = new ThucDonSuggestAdapter(foodList,getContext());
        recyclerView.setAdapter(thucDonSuggestAdapter);

        // Gọi hàm loadSuggestedDishes để lấy dữ liệu từ Firebase
        loadSuggestedDishes();


        btnAddDishSuggest.setOnClickListener(v -> showAddThucDonDialog());

        return view;
    }



    private void showAddThucDonDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_dishsuggest, null);
        builder.setView(dialogView);

        recyclerViewNgang = dialogView.findViewById(R.id.recyclerViewDishes);
        recyclerViewNgang.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        foodListNgang = new ArrayList<>();

        thucDonDAO.getAllThucDon(new ThucDonDAO.FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<ThucDon> thucDonList) {
                foodListNgang.clear();
                foodListNgang.addAll(thucDonList); // Thêm món ăn vào danh sách
                DishSuggestAdapter dishSuggestAdapter = new DishSuggestAdapter(foodListNgang, getContext());
                recyclerViewNgang.setAdapter(dishSuggestAdapter);
            }

            @Override
            public void onCallback(ThucDon thucDon) {
                // Không cần xử lý callback này ở đây
            }

            @Override
            public void onCallback(Float star) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void loadSuggestedDishes() {
        thucDonDAO.getSuggestedDishes(new ThucDonDAO.FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<ThucDon> suggestedDishes) {
                if (suggestedDishes == null || suggestedDishes.isEmpty()) {
                    Log.d(TAG, "Không có món ăn gợi ý.");
                } else {
                    Log.d(TAG, "Dữ liệu món ăn gợi ý đã được tải: " + suggestedDishes.size());
                    foodList.clear();
                    foodList.addAll(suggestedDishes);
                    thucDonSuggestAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView
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
    }

}