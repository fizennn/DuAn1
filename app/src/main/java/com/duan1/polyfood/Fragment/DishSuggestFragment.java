package com.duan1.polyfood.Fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.duan1.polyfood.Adapter.ThucDonNgangAdapter;
import com.duan1.polyfood.Adapter.ThucDonSuggestAdapter;
import com.duan1.polyfood.Database.ThucDonDAO;
import com.duan1.polyfood.Models.ThucDon;
import com.duan1.polyfood.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class DishSuggestFragment extends Fragment {

    private ImageView imgSelectedImage;
    private ThucDonDAO thucDonDAO;
    private StorageReference storageReference;
    private RecyclerView recyclerView;
    private ThucDonSuggestAdapter thucDonSuggestAdapter;
    private FloatingActionButton btnAddDishSuggest;
    private List<ThucDon> foodList;
    private RecyclerView recyclerViewNgang;
    private List<ThucDon> foodListNgang;
    private ThucDonNgangAdapter thucDonNgangAdapter;
    private Context context;

    private ArrayList<ThucDon> list = new ArrayList<>();

    private LottieAnimationView loading;
    private View viewLoad;



    public void loading(){
        loading.setVisibility(View.VISIBLE);
        viewLoad.setVisibility(View.VISIBLE);
    }

    public void loaded(){
        loading.setVisibility(View.GONE);
        viewLoad.setVisibility(View.GONE);
    }



    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    imgSelectedImage.setImageURI(imageUri);
                }
            });


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_dishsuggest, container, false);


        loading = view.findViewById(R.id.lottieLoading);
        viewLoad = view.findViewById(R.id.viewLoad);

        loading();

        context = getContext();

        thucDonDAO = new ThucDonDAO();
        recyclerView = view.findViewById(R.id.recyclerViewDishesSuggest);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));







        thucDonDAO.getAllThucDon(new ThucDonDAO.FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<ThucDon> thucDonList) {

                list = thucDonList;

                thucDonSuggestAdapter = new ThucDonSuggestAdapter(list,context);
                recyclerView.setAdapter(thucDonSuggestAdapter);
                loaded();
            }

            @Override
            public void onCallback(ThucDon thucDon) {
                // Không cần xử lý callback này ở đây
            }
            @Override
            public  void onCallback(Float star){

            }
        });

        return view;
    }



    private void showAddThucDonDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_dishsuggest, null);
        builder.setView(dialogView);



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
            public  void onCallback(Float star){

            }
        });

    }

    public void sapXep(){
        Collections.sort(list, new Comparator<ThucDon>() {
            @Override
            public int compare(ThucDon dish1, ThucDon dish2) {
                // Trường hợp cả hai đều null
                if (dish1 == null && dish2 == null) return 0;
                // Trường hợp chỉ một trong hai null
                if (dish1 == null) return 1;
                if (dish2 == null) return -1;

                // So sánh thuộc tính name (xử lý trường hợp name null)
                String name1 = dish1.getGoiY();
                String name2 = dish2.getGoiY();
                if (name1 == null && name2 == null) return 0;
                if (name1 == null) return 1;
                if (name2 == null) return -1;

                return name1.compareTo(name2);
            }
        });
    }

}