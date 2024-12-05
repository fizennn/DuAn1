package com.duan1.polyfood.Fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.duan1.polyfood.Adapter.ThongKeAdapter;
import com.duan1.polyfood.Adapter.Top3Adapter;
import com.duan1.polyfood.Database.HoaDonDAO;
import com.duan1.polyfood.Database.ThucDonDAO;
import com.duan1.polyfood.Models.HoaDon;
import com.duan1.polyfood.Models.ThucDon;
import com.duan1.polyfood.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class StatisticFragment extends Fragment {

    private TextView tvDoanhThu, tvStartDate, tvEndDate;
    private String startDate, endDate;
    private Top3Adapter top3Adapter;
    private ArrayList<ThucDon> top3DishesList;

    private LottieAnimationView loading;
    private View viewLoad;
    private RecyclerView recyclerView;
    private ThongKeAdapter thongKeAdapter;
    private ArrayList<HoaDon> list;
    private LinearLayout linear1,linear2;






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_statistic, container, false);
        View view = inflater.inflate(R.layout.fragment_statistic, container, false);

        tvDoanhThu = view.findViewById(R.id.txtTongTien);
        tvStartDate = view.findViewById(R.id.tvStartDate);
        tvEndDate = view.findViewById(R.id.tvEndDate);
        Button btnCalculate = view.findViewById(R.id.btnCalculate);
        linear1 = view.findViewById(R.id.linerHead);
        linear2 = view.findViewById(R.id.linerFoot);

        list = new ArrayList<>();




        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        thongKeAdapter = new ThongKeAdapter(list);
        recyclerView.setAdapter(thongKeAdapter);












        tvStartDate.setOnClickListener(v -> showDatePicker(true));
        tvEndDate.setOnClickListener(v -> showDatePicker(false));


        btnCalculate.setOnClickListener(v -> {
            if (startDate != null && endDate != null) {
                HoaDonDAO hoaDonDAO = new HoaDonDAO();
                hoaDonDAO.getDoanhThuByDateRange("Hoàn thành", startDate, endDate, new HoaDonDAO.FirebaseCallback() {
                    @Override
                    public void onCallback(ArrayList<HoaDon> hoaDonList) {
                        list.clear();

                        int tongTienDoanhThu = 0;
                        for (HoaDon hoaDon : hoaDonList) {
                            list.add(hoaDon);
                            tongTienDoanhThu += hoaDon.getTongTien();
                        }
                        thongKeAdapter.notifyDataSetChanged();

                        if (tongTienDoanhThu == 0) {
                            Toast.makeText(getContext(), "Không có doanh thu trong thời gian này", Toast.LENGTH_SHORT).show();
                            linear1.setVisibility(View.GONE);
                            linear2.setVisibility(View.GONE);
                        } else {
                            tvDoanhThu.setText(String.valueOf(tongTienDoanhThu));

                            linear1.setVisibility(View.VISIBLE);
                            linear2.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCallback(HoaDon hoaDon) {}
                });
            } else {
                Toast.makeText(getActivity(), "Vui lòng chọn ngày bắt đầu và kết thúc", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    private void showDatePicker(final boolean isStartDate) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                (view, year, month, dayOfMonth) -> {
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(year, month, dayOfMonth);
                    String formattedDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDate.getTime());
                    if (isStartDate) {
                        startDate = formattedDate;
                        tvStartDate.setText("Ngày bắt đầu: " + formattedDate);
                    } else {
                        endDate = formattedDate;
                        tvEndDate.setText("Ngày kết thúc: " + formattedDate);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}