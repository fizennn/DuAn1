package com.duan1.polyfood.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.duan1.polyfood.Adapter.StickerAdapter;
import com.duan1.polyfood.Database.StickerDao;
import com.duan1.polyfood.Models.Sticker;
import com.duan1.polyfood.R;

import java.util.ArrayList;
import java.util.List;

public class TagFragment extends Fragment {


    private RecyclerView recyclerView;
    private StickerAdapter adapter;
    private List<Sticker> stickerList;

    private Button btnAdd;
    private Sticker editingSticker;
    private ImageView[] mau = new ImageView[6];
    private String[] mauString = {"#FFC0CB","#ADD8E6","#98FB98","#FFFF0A","#FD9A42","#673AB7"};

    private int color = 0;

    private StickerDao stickerDao;

    ImageView ivStickerImage;
    Button btnChooseImage;
    Uri selectedImageUri = null;


    private LottieAnimationView loading;
    private View viewLoad;







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_tag, container, false);

        stickerDao = new StickerDao();

        loading = view.findViewById(R.id.lottieLoading);
        viewLoad = view.findViewById(R.id.viewLoad);

        loading();









        stickerDao.getAll(new StickerDao.StickerCallback() {
            @Override
            public void onSuccess(Sticker sticker) {

            }

            @Override
            public void onSuccess(List<Sticker> List) {
                recyclerView = view.findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



                adapter = new StickerAdapter(List,getContext(), new StickerAdapter.OnItemClickListener() {
                    @Override
                    public void onEdit(Sticker sticker) {

                        showStickerDialog(sticker);
                    }

                    @Override
                    public void onDelete(Sticker sticker) {
                        List.remove(sticker);
                        adapter.notifyDataSetChanged();
                    }
                });

                recyclerView.setAdapter(adapter);

                loaded();
            }

            @Override
            public void onFailure(String error) {

            }
        });

        btnAdd = view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(v -> showStickerDialog(null));

    return view;
    }

    private void showStickerDialog(Sticker sticker) {
        // Tạo dialog
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.sticker_dialog);








        color = 0;



        mau[0] = dialog.findViewById(R.id.mau1);
        mau[1] = dialog.findViewById(R.id.mau2);
        mau[2] = dialog.findViewById(R.id.mau3);
        mau[3] = dialog.findViewById(R.id.mau4);
        mau[4] = dialog.findViewById(R.id.mau5);
        mau[5] = dialog.findViewById(R.id.mau6);





        mau[0].setOnClickListener(v -> {
            colorReset();
            color = 1;
            mau[0].setPadding(10,10,10,10);

        });

        mau[1].setOnClickListener(v -> {
            colorReset();
            color = 2;
            mau[1].setPadding(10,10,10,10);
        });

        mau[2].setOnClickListener(v -> {
            colorReset();
            color = 3;
            mau[2].setPadding(10,10,10,10);
        });

        mau[3].setOnClickListener(v -> {
            colorReset();
            color = 4;
            mau[3].setPadding(10,10,10,10);
        });

        mau[4].setOnClickListener(v -> {
            colorReset();
            color = 5;
            mau[4].setPadding(10,10,10,10);
        });

        mau[5].setOnClickListener(v -> {
            colorReset();
            color = 6;
            mau[5].setPadding(10,10,10,10);
        });

        ivStickerImage = dialog.findViewById(R.id.ivDialogStickerImage);
        btnChooseImage = dialog.findViewById(R.id.btnDialogChooseImage);


        if (requireContext() != null && sticker!=null) {



            if (sticker.getImageUri()!=null){


                Glide.with(getContext())
                        .load(sticker.getImageUri())
                        .placeholder(R.drawable.load)
                        .error(R.drawable.load)
                        .into(ivStickerImage);

                ivStickerImage.setPadding(10,10,10,10);
            }

        }

// Chọn ảnh từ thư viện

        ivStickerImage.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            pickImageLauncher.launch(intent);
        });




        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,  // Chiều rộng: Chiếm toàn bộ màn hình
                    ViewGroup.LayoutParams.WRAP_CONTENT   // Chiều cao: Phụ thuộc vào nội dung
            );
        }

        EditText etDialogContent = dialog.findViewById(R.id.etDialogContent);
        Button btnDialogSave = dialog.findViewById(R.id.btnDialogSave);


        if (sticker!=null){

            for (int i = 0 ; i < 6 ; i ++){
                if (mauString[i].equalsIgnoreCase(sticker.getColor())){
                    mau[i].setPadding(10,10,10,10);
                    color = i+1;
                    etDialogContent.setText(sticker.getContent());
                    break;
                }
            }
        }

        // Nếu sửa, điền thông tin cũ vào

        btnDialogSave.setOnClickListener(v -> {
            String content = etDialogContent.getText().toString();
            String color1 = mauString[color - 1];

            if (!content.isEmpty()) {



            }
        });


        btnDialogSave.setOnClickListener(v -> {
            String content = etDialogContent.getText().toString();
            String color1 = mauString[color-1];
            String imageUriString = selectedImageUri != null ? selectedImageUri.toString() : null;

            if (!content.isEmpty()) {


                if (sticker == null) { // Thêm mới
                    stickerDao.addStickerImg(new Sticker(null, content, color1),selectedImageUri);
                } else { // Cập nhật

                    sticker.setContent(content);
                    sticker.setColor(color1);
                    stickerDao.updateSticker(sticker,selectedImageUri);
                }

                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void colorReset(){
        for (ImageView imageView : mau) {
            imageView.setPadding(100, 100, 100, 100); // Đặt padding là 16px cho tất cả các ImageView
        }
    }


    private ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    ivStickerImage.clearColorFilter();
                    ivStickerImage.setImageURI(selectedImageUri);
                    ivStickerImage.setPadding(10,10,10,10);


                }
            });


    public void loading(){
        loading.setVisibility(View.VISIBLE);
        viewLoad.setVisibility(View.VISIBLE);
    }

    public void loaded(){
        loading.setVisibility(View.GONE);
        viewLoad.setVisibility(View.GONE);
    }

}