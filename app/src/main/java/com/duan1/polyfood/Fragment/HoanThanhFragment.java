package com.duan1.polyfood.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duan1.polyfood.Adapter.HoaDonAdapter;
import com.duan1.polyfood.Database.HoaDonDAO;
import com.duan1.polyfood.Models.HoaDon;
import com.duan1.polyfood.R;

import java.util.ArrayList;

public class HoanThanhFragment extends Fragment {
    private RecyclerView recyclerView;
    private HoaDonAdapter hoaDonAdapter;
    private HoaDonDAO hoaDonDAO;
    private ArrayList<HoaDon> listHoaDon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hoan_thanh, container, false);

        hoaDonDAO = new HoaDonDAO();

        recyclerView = view.findViewById(R.id.recyclerViewHoaDon);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        listHoaDon = new ArrayList<>();

        // Lấy dữ liệu với trạng thái "Chờ xử lý"
        hoaDonDAO.getHoaDonByStatus("Hoàn thành", new HoaDonDAO.FirebaseCallback() {
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

        refreshHoaDonList();

        return view;
    }

    public void refreshHoaDonList() {
        hoaDonDAO.getHoaDonByStatus("Hoàn thành", new HoaDonDAO.FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<HoaDon> hoaDonList) {
                listHoaDon.clear();
                listHoaDon.addAll(hoaDonList);
                hoaDonAdapter.notifyDataSetChanged();
            }
        });
    }
}
