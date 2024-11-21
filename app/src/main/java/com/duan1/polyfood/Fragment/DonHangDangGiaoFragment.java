package com.duan1.polyfood.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duan1.polyfood.Adapter.NguoiGiaoAdapter;
import com.duan1.polyfood.Database.AuthenticationFireBaseHelper;
import com.duan1.polyfood.Database.HoaDonDAO;
import com.duan1.polyfood.Models.HoaDon;
import com.duan1.polyfood.R;

import java.util.ArrayList;

public class DonHangDangGiaoFragment extends Fragment {

    private RecyclerView recyclerView;
    private NguoiGiaoAdapter nguoiGiaoAdapter;
    private ArrayList<HoaDon> listHoaDon;
    private HoaDonDAO hoaDonDAO;
    private AuthenticationFireBaseHelper baseHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_xac_nhan_don_hang, container, false);
        hoaDonDAO = new HoaDonDAO();

        baseHelper=new AuthenticationFireBaseHelper();

        recyclerView = view.findViewById(R.id.recyclerViewHoaDon);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        listHoaDon = new ArrayList<>();

        hoaDonDAO.getHoaDonByStatus("Đang giao", new HoaDonDAO.FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<HoaDon> hoaDonList) {
                listHoaDon.clear();
                for (HoaDon don : hoaDonList) {
                    listHoaDon.add(don);
                }
                nguoiGiaoAdapter = new NguoiGiaoAdapter(getContext(), listHoaDon);
                recyclerView.setAdapter(nguoiGiaoAdapter);
            }

            @Override
            public void onCallback(HoaDon hoaDon) {

            }
        });

        return view;
    }

    public void addHoaDon(HoaDon hoaDon) {
        if (listHoaDon != null) {
            listHoaDon.add(hoaDon);
            nguoiGiaoAdapter.notifyDataSetChanged();
        }
    }
}
