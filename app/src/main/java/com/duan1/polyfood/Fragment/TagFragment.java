package com.duan1.polyfood.Fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_tag, container, false);

        stickerDao = new StickerDao();







        stickerDao.getAll(new StickerDao.StickerCallback() {
            @Override
            public void onSuccess(List<Sticker> List) {
                recyclerView = view.findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



                adapter = new StickerAdapter(List, new StickerAdapter.OnItemClickListener() {
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
            String color1 = mauString[color-1];

            if (!content.isEmpty()) {


                if (sticker == null) { // Thêm mới
                    stickerDao.addSticker(new Sticker(null, content, color1));
                } else { // Cập nhật
                    sticker.setContent(content);
                    sticker.setColor(color1);
                    stickerDao.updateSticker(sticker);
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

}