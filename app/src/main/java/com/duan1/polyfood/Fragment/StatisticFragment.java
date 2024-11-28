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
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
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
    private Button btnCalculate;
    private String startDate, endDate;
    private RecyclerView recyclerViewTopDishes;
    private Top3Adapter top3Adapter;
    private ArrayList<ThucDon> top3DishesList;

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_statistic, container, false);
        View view = inflater.inflate(R.layout.fragment_statistic, container, false);

        tvDoanhThu = view.findViewById(R.id.tvDoanhThu);
        tvStartDate = view.findViewById(R.id.tvStartDate);
        tvEndDate = view.findViewById(R.id.tvEndDate);
        btnCalculate = view.findViewById(R.id.btnCalculate);

        loading = view.findViewById(R.id.lottieLoading);
        viewLoad = view.findViewById(R.id.viewLoad);

        loading();

        recyclerViewTopDishes = view.findViewById(R.id.recyclerViewTopDishes);
        recyclerViewTopDishes.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Khởi tạo danh sách món ăn top 3
        top3DishesList = new ArrayList<>();
        top3Adapter = new Top3Adapter(getContext(), top3DishesList);
        recyclerViewTopDishes.setAdapter(top3Adapter);

        // Gọi phương thức lấy top 3 món ăn từ Firebase
        ThucDonDAO thucDonDAO = new ThucDonDAO();
        thucDonDAO.getTop3ThucDon(new ThucDonDAO.FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<ThucDon> top3ThucDon) {
                // Cập nhật danh sách top 3 món ăn
                top3DishesList.clear();
                top3DishesList.addAll(top3ThucDon);

                // Cập nhật adapter để hiển thị dữ liệu
                top3Adapter.notifyDataSetChanged();
                loaded();
            }

            @Override
            public void onCallback(ThucDon thucDon) {
            }

            @Override
            public void onCallback(Float star) {
            }
        });


        // Set Date Picker Dialog for start date
        tvStartDate.setOnClickListener(v -> showDatePicker(true));
        tvEndDate.setOnClickListener(v -> showDatePicker(false));


        btnCalculate.setOnClickListener(v -> {
            if (startDate != null && endDate != null) {
                HoaDonDAO hoaDonDAO = new HoaDonDAO();
                hoaDonDAO.getDoanhThuByDateRange("Hoàn thành", startDate, endDate, new HoaDonDAO.FirebaseCallback() {
                    @Override
                    public void onCallback(ArrayList<HoaDon> hoaDonList) {
                        int tongTienDoanhThu = 0;
                        for (HoaDon hoaDon : hoaDonList) {
                            tongTienDoanhThu += hoaDon.getTongTien();
                        }
                        if (tongTienDoanhThu == 0) {
                            tvDoanhThu.setText("Không có doanh thu trong khoảng thời gian này.");
                        } else {
                            tvDoanhThu.setText(String.format("Doanh Thu: %d đ", tongTienDoanhThu));
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