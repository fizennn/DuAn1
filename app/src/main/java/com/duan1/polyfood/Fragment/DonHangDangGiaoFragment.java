package com.duan1.polyfood.Fragment;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
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
import com.google.android.gms.maps.GoogleMap;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class DonHangDangGiaoFragment extends Fragment{

    private RecyclerView recyclerView;
    private NguoiGiaoAdapter nguoiGiaoAdapter;
    private ArrayList<HoaDon> listHoaDon;
    private GoogleMap mMap;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_xac_nhan_don_hang, container, false);
        HoaDonDAO hoaDonDAO = new HoaDonDAO();



        AuthenticationFireBaseHelper baseHelper = new AuthenticationFireBaseHelper();

        recyclerView = view.findViewById(R.id.recyclerViewHoaDon);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        listHoaDon = new ArrayList<>();

        hoaDonDAO.getHoaDonByStatus("Đang giao", new HoaDonDAO.FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<HoaDon> hoaDonList) {
                listHoaDon.clear();
                for (HoaDon don : hoaDonList) {
                    listHoaDon.add(don);
                    break;
                }
                nguoiGiaoAdapter = new NguoiGiaoAdapter(getContext(), listHoaDon);
                recyclerView.setAdapter(nguoiGiaoAdapter);
            }

            @Override
            public void onCallback(HoaDon hoaDon) {

            }
        });



        getDirections("Hà Nội","Phú Thọ");


        return view;
    }

    public void addHoaDon(HoaDon hoaDon) {
        if (listHoaDon != null) {
            listHoaDon.add(hoaDon);
            nguoiGiaoAdapter.notifyDataSetChanged();
        }
    }

    private void getDirections(String from, String to) {
        try {
            // Encode parameters to handle special characters
            String encodedFrom = URLEncoder.encode(from, "UTF-8");
            String encodedTo = URLEncoder.encode(to, "UTF-8");

            Uri uri = Uri.parse("https://www.google.com/maps/dir/" + encodedFrom + "/" + encodedTo);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace(); // Handle encoding exception
        } catch (ActivityNotFoundException exception) {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }




















}
