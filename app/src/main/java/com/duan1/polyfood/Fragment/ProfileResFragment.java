package com.duan1.polyfood.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.duan1.polyfood.Database.NhaHangDAO;
import com.duan1.polyfood.Models.NhaHang;
import com.duan1.polyfood.R;

import java.util.ArrayList;

public class ProfileResFragment extends Fragment {


    private NhaHangDAO nhaHangDAO;
    private TextView txtRestaurantName,txtIntroduction,txtAddress,txtEmail,txtPhone;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_profile_res, container, false);

        nhaHangDAO = new NhaHangDAO();

        txtRestaurantName = view.findViewById(R.id.txtRestaurantName);
        txtIntroduction = view.findViewById(R.id.txtIntroduction);
        txtAddress = view.findViewById(R.id.txtAddress);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtPhone = view.findViewById(R.id.txtPhone);



        nhaHangDAO.getThongTin(new NhaHangDAO.FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<NhaHang> nhaHangList) {

            }

            @Override
            public void onCallback(NhaHang nhaHang) {

                txtRestaurantName.setText(nhaHang.getTen()+"");
                txtIntroduction.setText(nhaHang.getGioiThieu()+"");
                txtAddress.setText(nhaHang.getDiaChi()+"");
                txtEmail.setText(nhaHang.getEmail()+"");
                txtPhone.setText(nhaHang.getSdt()+"");
            }
        });



        return view;
    }
}
