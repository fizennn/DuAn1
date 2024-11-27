package com.duan1.polyfood.Fragment;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.duan1.polyfood.Adapter.ThucDonAdapter;
import com.duan1.polyfood.Adapter.ThucDonNgangAdapter;
import com.duan1.polyfood.Adapter.ThucDonNgangDishAdapter;
import com.duan1.polyfood.Database.StickerDao;
import com.duan1.polyfood.Database.ThucDonDAO;
import com.duan1.polyfood.Models.Sticker;
import com.duan1.polyfood.Models.ThucDon;
import com.duan1.polyfood.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class DishFragment extends Fragment {

    private ImageView imgSelectedImage;
    private Uri imageUri;
    private ThucDonDAO thucDonDAO;
    private StorageReference storageReference;
    private RecyclerView recyclerViewNgang;
    private ThucDonNgangDishAdapter thucDonNgangAdapter;
    private List<ThucDon> foodListNgang;
    private StickerDao stickerDao;
    private Spinner spinner1,spinner2,spinner3;
    List<Sticker> ds = new ArrayList<>();
    private Context context;


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
        View view = inflater.inflate(R.layout.fragment_dish, container, false);

        context = getContext();

        thucDonDAO = new ThucDonDAO();
        CardView btnAdd = view.findViewById(R.id.floatAdd);

        stickerDao = new StickerDao();

        recyclerViewNgang = view.findViewById(R.id.recyclerViewDishes);
        recyclerViewNgang.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        foodListNgang = new ArrayList<>();

        thucDonDAO.getAllThucDon(new ThucDonDAO.FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<ThucDon> thucDonList) {
                foodListNgang.clear();
                for (ThucDon don : thucDonList){
                    foodListNgang.add(don);
                }
                thucDonNgangAdapter = new ThucDonNgangDishAdapter(foodListNgang,context);
                thucDonNgangAdapter.setOnItemClickListener(new ThucDonNgangDishAdapter.OnItemClickListener() {
                    @Override
                    public void onEdit(ThucDon thucDon) {
                        showAddThucDonDialog(thucDon);
                    }
                });
                recyclerViewNgang.setAdapter(thucDonNgangAdapter);
            }

            @Override
            public void onCallback(ThucDon thucDon) {

            }

            @Override
            public void onCallback(Float star) {

            }
        });


        btnAdd.setOnClickListener(v -> showAddThucDonDialog(null));



        return view;
    }



    private void showAddThucDonDialog(ThucDon don) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_add_thuc_don);

        imgSelectedImage = dialog.findViewById(R.id.imgSelectedImage);
        EditText edtIdNh = dialog.findViewById(R.id.edtIhnh);
        EditText edtTen = dialog.findViewById(R.id.edtTen);
        EditText edtGia = dialog.findViewById(R.id.edtGia);
        EditText edtMoTa = dialog.findViewById(R.id.edtMoTa);
//        EditText edtDanhGia = dialog.findViewById(R.id.edtDanhgia);
//        EditText edtPhanHoi = dialog.findViewById(R.id.edtPhanhoi);
        ImageButton btnChooseImage = dialog.findViewById(R.id.btnChooseImage);
        Button btnSaveThucDon = dialog.findViewById(R.id.btnAddThucDon);

        spinner1 = dialog.findViewById(R.id.spiner1);
        spinner2 = dialog.findViewById(R.id.spiner2);
        spinner3 = dialog.findViewById(R.id.spiner3);

        stickerDao.getAll(new StickerDao.StickerCallback() {
            @Override
            public void onSuccess(Sticker sticker) {

            }

            @Override
            public void onSuccess(List<Sticker> stickerList) {


                ds.add(new Sticker("","None",""));
                ds.addAll(stickerList);

                ArrayAdapter<Sticker> adapter = new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_item, // Giao diện cho mỗi mục
                        ds
                );

                // Cài đặt layout khi hiển thị danh sách
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // Gán adapter cho Spinner
                spinner1.setAdapter(adapter);
                spinner2.setAdapter(adapter);
                spinner3.setAdapter(adapter);

                if (don!=null){
                    for (int i = 0 ; i < ds.size() ; i++){
                        if (ds.get(i).getId().equalsIgnoreCase(don.getSticker1())){
                            spinner1.setSelection(i);
                        }
                        if (ds.get(i).getId().equalsIgnoreCase(don.getSticker2())){
                            spinner2.setSelection(i);
                        }
                        if (ds.get(i).getId().equalsIgnoreCase(don.getSticker3())){
                            spinner3.setSelection(i);
                        }
                    }
                }

            }

            @Override
            public void onFailure(String error) {

            }
        });






        btnChooseImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        if (don!=null){
            edtIdNh.setText(don.getId_nh());
            edtTen.setText(don.getTen());
            edtGia.setText(String.valueOf(don.getGia()));
            edtMoTa.setText(don.getMoTa());

            if (getContext() != null) {
                Glide.with(getContext())
                        .load(don.getHinhAnh())
                        .placeholder(R.drawable.load)
                        .error(R.drawable.load)
                        .into(imgSelectedImage);
            }



        }

        btnSaveThucDon.setOnClickListener(v -> {



            ThucDon thucDon = new ThucDon();
            thucDon.setId_nh(edtIdNh.getText().toString());
            thucDon.setTen(edtTen.getText().toString());


            if (spinner1.getSelectedItemPosition()!=0){
                Sticker sticker = (Sticker) spinner1.getSelectedItem();
                thucDon.setSticker1(sticker.getId());
            }

            if (spinner2.getSelectedItemPosition()!=0){
                Sticker sticker = (Sticker) spinner2.getSelectedItem();
                thucDon.setSticker2(sticker.getId());
            }

            if (spinner3.getSelectedItemPosition()!=0){
                Sticker sticker = (Sticker) spinner3.getSelectedItem();
                thucDon.setSticker3(sticker.getId());
            }




            try {
                thucDon.setGia(Integer.parseInt(edtGia.getText().toString()));
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Invalid price", Toast.LENGTH_SHORT).show();
                return;
            }
            thucDon.setMoTa(edtMoTa.getText().toString());
//            thucDon.setDanhGia(edtDanhGia.getText().toString());
//            thucDon.setPhanHoi(edtPhanHoi.getText().toString());

            if (don==null){

                if (imageUri == null) {
                    Toast.makeText(getContext(), "Please select an image", Toast.LENGTH_SHORT).show();
                    return;
                }

                thucDonDAO.addThucDon(thucDon, imageUri);
                imageUri=null;
                dialog.dismiss();
                Toast.makeText(getContext(), "Adding menu item...", Toast.LENGTH_SHORT).show();
            }else {
                thucDon.setGoiY(don.getGoiY());
                thucDon.setDanhGia(don.getDanhGia());
                thucDon.setPhanHoi(don.getPhanHoi());
                thucDon.setId_td(don.getId_td());
                thucDon.setHinhAnh(don.getHinhAnh());
                thucDonDAO.update(thucDon, imageUri);
                dialog.dismiss();
                Toast.makeText(getContext(), "Updating menu item...", Toast.LENGTH_SHORT).show();

            }


        });

        dialog.show();
    }

}