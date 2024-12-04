package com.duan1.polyfood.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.duan1.polyfood.Adapter.HoaDonAdapter;
import com.duan1.polyfood.Database.AuthenticationFireBaseHelper;
import com.duan1.polyfood.Database.HoaDonDAO;
import com.duan1.polyfood.Models.HoaDon;
import com.duan1.polyfood.R;

import java.util.ArrayList;

public class DangGiaoFragment extends Fragment {
    private RecyclerView recyclerView;
    private HoaDonAdapter hoaDonAdapter;
    private ArrayList<HoaDon> listHoaDon;
    private AuthenticationFireBaseHelper baseHelper;
    private LottieAnimationView loading;
    private View viewLoad,empty;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dang_giao, container, false);

        loading = view.findViewById(R.id.lottieLoading);
        viewLoad = view.findViewById(R.id.viewLoad);
        empty = view.findViewById(R.id.empty);

        HoaDonDAO hoaDonDAO = new HoaDonDAO();

        baseHelper=new AuthenticationFireBaseHelper();

        recyclerView = view.findViewById(R.id.recyclerViewHoaDon);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        listHoaDon = new ArrayList<>();

        loading();

        hoaDonDAO.getHoaDonByStatus("Đang giao", new HoaDonDAO.FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<HoaDon> hoaDonList) {
                listHoaDon.clear();
                for (HoaDon don : hoaDonList) {
                    String uid1 = don.getId_nd();
                    String uid2 = baseHelper.getUID();
                    Log.d("111111", "onCallback: " + "uid1: " + uid1 + "uid2: " + uid2);
                    if (uid1.equals(uid2)){
                        listHoaDon.add(don);
                    }
                }
                hoaDonAdapter = new HoaDonAdapter(getContext(), listHoaDon);
                recyclerView.setAdapter(hoaDonAdapter);
                hoaDonAdapter.notifyDataSetChanged();
                loaded();

                if (hoaDonList.size()==0){
                    empty.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCallback(HoaDon hoaDon) {

            }
        });



        return view;
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
