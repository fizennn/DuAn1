package com.duan1.polyfood.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.duan1.polyfood.Adapter.NguoiGiaoAdapter;
import com.duan1.polyfood.Database.HoaDonDAO;
import com.duan1.polyfood.Models.HoaDon;
import com.duan1.polyfood.R;

import java.util.ArrayList;

public class XacNhanDonHangFragment extends Fragment {

    private RecyclerView recyclerView;
    private NguoiGiaoAdapter nguoiGiaoAdapter;
    private ArrayList<HoaDon> listHoaDon;
    private LottieAnimationView linearLayout;

    private LottieAnimationView loading;
    private View viewLoad;



    public void loading(){
        loading.setVisibility(View.VISIBLE);
        viewLoad.setVisibility(View.VISIBLE);
    }

    public void loaded(){
        loading.setVisibility(View.GONE);
        viewLoad.setVisibility(View.GONE);
    }

    public XacNhanDonHangFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_xac_nhan_don_hang1, container, false);
        HoaDonDAO hoaDonDAO = new HoaDonDAO();

        loading = view.findViewById(R.id.lottieLoading);
        viewLoad = view.findViewById(R.id.viewLoad);



        loading();

        recyclerView = view.findViewById(R.id.recyclerViewHoaDon);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        listHoaDon = new ArrayList<>();

        hoaDonDAO.getHoaDonByStatus("Chờ giao", new HoaDonDAO.FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<HoaDon> hoaDonList) {
                listHoaDon.clear();
                for (HoaDon don : hoaDonList) {
                    listHoaDon.add(don);
                }
                nguoiGiaoAdapter = new NguoiGiaoAdapter(getContext(), listHoaDon, new NguoiGiaoAdapter.CallBack() {
                    @Override
                    public void delete(int i) {
                        listHoaDon.remove(i);
                        nguoiGiaoAdapter.notifyItemRemoved(i);
                        Toast.makeText( getContext(), "Hủy Đơn Hàng Thành Công!", Toast.LENGTH_SHORT).show();

                        nguoiGiaoAdapter.notifyDataSetChanged();
                    }
                });
                recyclerView.setAdapter(nguoiGiaoAdapter);
                loaded();
            }

            @Override
            public void onCallback(HoaDon hoaDon) {

            }
        });

        return view;
    }
}
