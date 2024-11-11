package com.duan1.polyfood.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duan1.polyfood.Adapter.HoaDonAdapter;
import com.duan1.polyfood.Database.DonHangDAO;
import com.duan1.polyfood.Database.HoaDonDAO;
import com.duan1.polyfood.Models.DonHang;
import com.duan1.polyfood.Models.HoaDon;
import com.duan1.polyfood.R;

import java.util.ArrayList;


public class BillFragment extends Fragment {

    private RecyclerView recyclerView;
    private HoaDonAdapter hoaDonAdapter;
    private HoaDonDAO hoaDonDAO;
    private ArrayList<HoaDon> listHoaDon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bill, container, false);

        hoaDonDAO = new HoaDonDAO();

        recyclerView = view.findViewById(R.id.recyclerViewBills);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        listHoaDon = new ArrayList<>();

        hoaDonDAO.getAllHoaDon(new HoaDonDAO.FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<HoaDon> hoaDonList) {
                listHoaDon.clear();
                for (HoaDon don : hoaDonList) {
                    listHoaDon.add(don);
                }
                hoaDonAdapter = new HoaDonAdapter(getContext(), listHoaDon);
                recyclerView.setAdapter(hoaDonAdapter);
            }
        });

        return view;
    }
}