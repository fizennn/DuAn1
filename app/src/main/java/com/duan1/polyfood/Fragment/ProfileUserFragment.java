package com.duan1.polyfood.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.duan1.polyfood.Database.AuthenticationFireBaseHelper;
import com.duan1.polyfood.Database.NguoiDungDAO;
import com.duan1.polyfood.EditProfileActivity;
import com.duan1.polyfood.FlashScreenActivity;
import com.duan1.polyfood.Models.NguoiDung;
import com.duan1.polyfood.R;
import com.duan1.polyfood.WellcomeActivity;

public class ProfileUserFragment extends Fragment {

    private ImageView imgProfile,ivEditProfile,ivDangXuat;
    private TextView tvName,tvSDT;
    private Context context;

    private NguoiDungDAO nguoiDungDAO;
    private AuthenticationFireBaseHelper auth;

    private LottieAnimationView loading;
    private View viewLoad;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_profile_user, container, false);

        //Context
        context = requireContext();

        //Khaibao
        khaiBao();

        //Anh Xa
        anhXa(view);

        //Man cho
        loading();

        //Hien Thi Thong Tin User
        getUser();


        //SetOnClick

        ivEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        ivDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(context, FlashScreenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        return view;
    }

    public void khaiBao(){
        nguoiDungDAO = new NguoiDungDAO();
        auth = new AuthenticationFireBaseHelper();
    }

    public void anhXa(View view){

        //img
        imgProfile = view.findViewById(R.id.imgProFile);
        ivEditProfile = view.findViewById(R.id.ivEditProfile);
        ivDangXuat = view.findViewById(R.id.ivDangXuat);

        //textview
        tvName = view.findViewById(R.id.tvName);
        tvSDT = view.findViewById(R.id.tvSDT);

        //loading
        loading = view.findViewById(R.id.lottieLoading);
        viewLoad = view.findViewById(R.id.viewLoad);

    }

    public void loading(){
        loading.setVisibility(View.VISIBLE);
        viewLoad.setVisibility(View.VISIBLE);
    }

    public void loaded(){
        loading.setVisibility(View.GONE);
        viewLoad.setVisibility(View.GONE);
    }




    public void getUser(){
        nguoiDungDAO.getAllNguoiDungByID(auth.getUID(), new NguoiDungDAO.FirebaseCallback() {
            @Override
            public void onCallback(NguoiDung nguoiDung) {
                if (nguoiDung!=null){
                    tvName.setText(nguoiDung.getHoTen());
                    tvSDT.setText("+"+nguoiDung.getSdt());



                        Glide.with(context)
                                .load(nguoiDung.getimgUrl())
                                .placeholder(R.drawable.load)
                                .error(R.drawable.load)
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
                                .into(imgProfile);
                    }


                }

        });
    }


}