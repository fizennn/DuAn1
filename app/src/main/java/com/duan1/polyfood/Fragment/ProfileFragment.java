package com.duan1.polyfood.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.duan1.polyfood.Database.NguoiDungDAO;
import com.duan1.polyfood.Models.NguoiDung;
import com.duan1.polyfood.R;


public class ProfileFragment extends Fragment {
    private TextView name,address,gender,age,email,phone;
    private NguoiDungDAO nguoiDungDAO;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        nguoiDungDAO = new NguoiDungDAO();

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        name = view.findViewById(R.id.textViewFullName);
        address = view.findViewById(R.id.textViewAddress);
        gender = view.findViewById(R.id.textViewGender);
        age = view.findViewById(R.id.textViewAge);
        email = view.findViewById(R.id.textViewEmail);
        phone = view.findViewById(R.id.textViewPhoneNumber);

        try {
            nguoiDungDAO.getAllNguoiDung(new NguoiDungDAO.FirebaseCallback() {
                @Override
                public void onCallback(NguoiDung nguoiDung) {
                    name.setText(nguoiDung.getHoTen()+"");
                    address.setText(nguoiDung.getDiaChi()+"");
                    gender.setText(nguoiDung.getSex()+"");
                    age.setText(nguoiDung.getAge()+"");
                    email.setText(nguoiDung.getEmail()+"");
                    phone.setText(nguoiDung.getSdt()+"");
                }
            });
        }catch (Exception exception){

        }



        return view;
    }
}