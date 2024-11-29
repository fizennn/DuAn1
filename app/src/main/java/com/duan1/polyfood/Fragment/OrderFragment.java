package com.duan1.polyfood.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.duan1.polyfood.Adapter.HoaDonAdapter;
import com.duan1.polyfood.Adapter.NhaHangHDAdapter;
import com.duan1.polyfood.Database.HoaDonDAO;
import com.duan1.polyfood.Models.HoaDon;
import com.duan1.polyfood.R;

import java.util.ArrayList;


public class OrderFragment extends Fragment {

    private RecyclerView recyclerView;
    private NhaHangHDAdapter nhaHangHDAdapter;
    private ArrayList<HoaDon> listHoaDon;
    private HoaDonDAO hoaDonDAO;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        hoaDonDAO = new HoaDonDAO();

        recyclerView = view.findViewById(R.id.recyclerViewHoaDon);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        listHoaDon = new ArrayList<>();

//         Lấy dữ liệu với trạng thái "Chờ xử lý"
        hoaDonDAO.getHoaDonByStatus("Chờ xử lý", new HoaDonDAO.FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<HoaDon> hoaDonList) {
                listHoaDon.clear();
                for (HoaDon don : hoaDonList) {
                    listHoaDon.add(don);
                }
                nhaHangHDAdapter = new NhaHangHDAdapter(getContext(), listHoaDon);
                recyclerView.setAdapter(nhaHangHDAdapter);
            }

            @Override
            public void onCallback(HoaDon hoaDon) {

            }
        });

        return view;
    }
}