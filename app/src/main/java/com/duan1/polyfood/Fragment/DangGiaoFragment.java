package com.duan1.polyfood.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duan1.polyfood.Adapter.HoaDonAdapter;
import com.duan1.polyfood.Database.AuthenticationFireBaseHelper;
import com.duan1.polyfood.Database.HoaDonDAO;
import com.duan1.polyfood.Models.HoaDon;
import com.duan1.polyfood.R;

import java.util.ArrayList;

public class DangGiaoFragment extends Fragment {
    private RecyclerView recyclerView;
    private HoaDonAdapter hoaDonAdapter;
    private HoaDonDAO hoaDonDAO;
    private ArrayList<HoaDon> listHoaDon;
    private AuthenticationFireBaseHelper baseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dang_giao, container, false);

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
                    String uid1 = don.getId_nd();
                    String uid2 = baseHelper.getUID();
                    if (uid1.equalsIgnoreCase(uid2)){
                        listHoaDon.add(don);
                    }
                }
                hoaDonAdapter = new HoaDonAdapter(getContext(), listHoaDon);
                recyclerView.setAdapter(hoaDonAdapter);
                hoaDonAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCallback(HoaDon hoaDon) {

            }
        });



        return view;
    }

}
