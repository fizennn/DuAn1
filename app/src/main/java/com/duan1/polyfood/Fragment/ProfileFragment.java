package com.duan1.polyfood.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.duan1.polyfood.Database.AuthenticationFireBaseHelper;
import com.duan1.polyfood.Database.NguoiDungDAO;
import com.duan1.polyfood.Models.NguoiDung;
import com.duan1.polyfood.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.bumptech.glide.Glide;


public class ProfileFragment extends Fragment {
    private TextView name, address, gender, age, email, phone;
    private NguoiDungDAO nguoiDungDAO;
    private ImageView imageViewProfile;
    private Uri imageUri;
    private StorageReference storageReference;
    private NguoiDung nguoiDungGet;
    private AuthenticationFireBaseHelper auth;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        auth = new AuthenticationFireBaseHelper();

        name = view.findViewById(R.id.textViewFullName);
        address = view.findViewById(R.id.textViewAddress);
        gender = view.findViewById(R.id.textViewGender);
        age = view.findViewById(R.id.textViewAge);
        email = view.findViewById(R.id.textViewEmail);
        phone = view.findViewById(R.id.textViewPhoneNumber);
        imageViewProfile = view.findViewById(R.id.imageViewProfile);

        storageReference = FirebaseStorage.getInstance().getReference();
        nguoiDungDAO = new NguoiDungDAO();

        storageReference.child("profile_images/" + auth.getUID() + ".jpg").getDownloadUrl().addOnSuccessListener(uri ->
                Glide.with(ProfileFragment.this)
                        .load(uri)
                        .into(imageViewProfile)
        ).addOnFailureListener(e -> {

            e.printStackTrace();
        });

        loadProfileImage();

        Button button = view.findViewById(R.id.btnImgProfile);
        button.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            pickImageLauncher.launch(intent);
        });

        return view;
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

                if (nguoiDung.getProfileImageUrl() != null && !nguoiDung.getProfileImageUrl().isEmpty()) {
                    loadImageFromUrl(nguoiDung.getProfileImageUrl());
                } else {
                    imageViewProfile.setImageResource(R.drawable.header_profile);
                }
            }
        });
    }

    private void loadImageFromUrl(String imageUrl) {
        if (getContext() != null) {
            Glide.with(getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.header_profile)
                    .error(R.drawable.header_profile)
                    .into(imageViewProfile);
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child("profile_images/" + auth.getUID() + ".jpg");

            fileReference.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Tải ảnh thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }


}