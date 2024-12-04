package com.duan1.polyfood;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.duan1.polyfood.Database.AuthenticationFireBaseHelper;
import com.duan1.polyfood.Database.NguoiDungDAO;
import com.duan1.polyfood.Models.NguoiDung;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EditProfileActivity extends AppCompatActivity {

    private EditText name, address, gender, age, email, phone;
    private NguoiDungDAO nguoiDungDAO;
    private ImageView imageViewProfile;
    private Uri imageUri;
    private NguoiDung nguoiDungGet;
    private Button btnOut;

    private LottieAnimationView loading;
    private View viewLoad;


    private ActivityResultLauncher<Intent> pickImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    imageUri = result.getData().getData();
                    imageViewProfile.setImageURI(imageUri);
                    uploadImageToFirebase(imageUri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_profile);

        //loading
        loading = findViewById(R.id.lottieLoading);
        viewLoad = findViewById(R.id.viewLoad);

        ImageButton button1 = findViewById(R.id.imgBack);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loading();


        AuthenticationFireBaseHelper auth = new AuthenticationFireBaseHelper();

        name = findViewById(R.id.textViewFullName);
        address = findViewById(R.id.textViewAddress);
        gender = findViewById(R.id.textViewGender);
        age = findViewById(R.id.textViewAge);
        email = findViewById(R.id.textViewEmail);
        phone = findViewById(R.id.textViewPhoneNumber);
        imageViewProfile = findViewById(R.id.imageViewProfile);
        ImageView imgBack = findViewById(R.id.imgBack);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        nguoiDungDAO = new NguoiDungDAO();

        Button btnUpdate = findViewById(R.id.btnUpdate);


        loadProfileImage();

        CardView button = findViewById(R.id.cAddAnh);
        button.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            pickImageLauncher.launch(intent);
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void update(){

        String Ten = name.getText().toString();
        String diaChi = address.getText().toString();
        String gioiTinh = gender.getText().toString();
        String tuoi = age.getText().toString();
        String SDT = phone.getText().toString();

        NguoiDung dung1 = new NguoiDung();

        dung1 = nguoiDungGet;

        dung1.setHoTen(Ten);
        dung1.setDiaChi(diaChi);
        dung1.setSex(gioiTinh);
        dung1.setAge(tuoi);
        dung1.setSdt(SDT);


        loading();

        nguoiDungDAO.update(dung1, imageUri, new NguoiDungDAO.onSuccess() {
            @Override
            public void success(boolean success) {
                if (success==true){
                    Toast.makeText(EditProfileActivity.this, "Cập Nhật Thành Công !", Toast.LENGTH_SHORT).show();
                    imageUri=null;
                    loaded();
                }
            }
        });
    }


    private void loadProfileImage() {
        nguoiDungDAO.getAllNguoiDung(new NguoiDungDAO.FirebaseCallback() {
            @Override
            public void onCallback(NguoiDung nguoiDung) {

                nguoiDungGet = nguoiDung;

                name.setText(nguoiDung.getHoTen());
                address.setText(nguoiDung.getDiaChi());
                gender.setText(nguoiDung.getSex());
                age.setText(nguoiDung.getAge());
                email.setText(nguoiDung.getEmail());
                phone.setText(nguoiDung.getSdt());
                loadImageFromUrl(nguoiDung.getimgUrl());
            }
        });
    }

    private void loadImageFromUrl(String imageUrl) {
        if (this != null) {
            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.load) // Hình placeholder trong lúc tải
                    .error(R.drawable.load) // Hình lỗi nếu tải thất bại
                    .listener(new RequestListener<Drawable>() {

                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            loaded();
                            return false;
                        }
                    })
                    .into(imageViewProfile);
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        nguoiDungDAO.addNguoiDungImg(nguoiDungGet,imageUri);
    }

    public void loading(){
        loading.setVisibility(View.VISIBLE);
        viewLoad.setVisibility(View.VISIBLE);
    }

    public void loaded(){
        loading.setVisibility(View.GONE);
        viewLoad.setVisibility(View.GONE);
    }
}