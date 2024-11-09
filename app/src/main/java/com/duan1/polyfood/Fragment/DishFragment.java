package com.duan1.polyfood.Fragment;

import static android.app.Activity.RESULT_OK;

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
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.duan1.polyfood.Adapter.ThucDonAdapter;
import com.duan1.polyfood.Database.ThucDonDAO;
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
    private RecyclerView recyclerView;
    private ThucDonAdapter thucDonAdapter;
    private List<ThucDon> thucDonList = new ArrayList<>();

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

        recyclerView = view.findViewById(R.id.recyclerViewDishes);
        Button btnAdd = view.findViewById(R.id.floatAdd);

        thucDonList = new ArrayList<>();
        thucDonDAO = new ThucDonDAO();
        thucDonAdapter = new ThucDonAdapter(requireContext(), thucDonList);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(thucDonAdapter);

        loadThucDon();

        btnAdd.setOnClickListener(v -> showAddThucDonDialog());
        return view;
    }

    private void loadThucDon() {
        thucDonDAO.getAllThucDon(new ThucDonDAO.FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<ThucDon> thucDonList) {
                thucDonList.clear();
                thucDonList.addAll(thucDonList);
                thucDonAdapter.notifyDataSetChanged();
            }
        });
    }

    private void showAddThucDonDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_add_thuc_don);

        imgSelectedImage = dialog.findViewById(R.id.imgSelectedImage);
        EditText edtIdNh = dialog.findViewById(R.id.edtIhnh);
        EditText edtTen = dialog.findViewById(R.id.edtTen);
        EditText edtGia = dialog.findViewById(R.id.edtGia);
        EditText edtMoTa = dialog.findViewById(R.id.edtMoTa);
        EditText edtDanhGia = dialog.findViewById(R.id.edtDanhgia);
        EditText edtPhanHoi = dialog.findViewById(R.id.edtPhanhoi);
        Button btnChooseImage = dialog.findViewById(R.id.btnChooseImage);
        Button btnSaveThucDon = dialog.findViewById(R.id.btnAddThucDon);

        btnChooseImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        btnSaveThucDon.setOnClickListener(v -> {
            if (imageUri == null) {
                Toast.makeText(getContext(), "Please select an image", Toast.LENGTH_SHORT).show();
                return;
            }

            ThucDon thucDon = new ThucDon();
            thucDon.setId_nh(edtIdNh.getText().toString());
            thucDon.setTen(edtTen.getText().toString());
            try {
                thucDon.setGia(Integer.parseInt(edtGia.getText().toString()));
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Invalid price", Toast.LENGTH_SHORT).show();
                return;
            }
            thucDon.setMoTa(edtMoTa.getText().toString());
            try {
                thucDon.setDanhGia(Integer.parseInt(edtDanhGia.getText().toString()));
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Invalid rating", Toast.LENGTH_SHORT).show();
                return;
            }
            thucDon.setPhanHoi(edtPhanHoi.getText().toString());

            thucDonDAO.addThucDon(thucDon, imageUri);
            dialog.dismiss();
            Toast.makeText(getContext(), "Adding menu item...", Toast.LENGTH_SHORT).show();
        });

        dialog.show();
    }

}