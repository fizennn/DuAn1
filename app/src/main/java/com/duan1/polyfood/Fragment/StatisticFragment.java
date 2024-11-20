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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticFragment extends Fragment {

    private TextView tvDoanhThu, tvStartDate, tvEndDate;
    private Button btnCalculate;
    private String startDate, endDate;
    private RecyclerView recyclerViewTopDishes;
    private Top3Adapter top3Adapter;
    private ArrayList<ThucDon> top3DishesList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StatisticFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatisticFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatisticFragment newInstance(String param1, String param2) {
        StatisticFragment fragment = new StatisticFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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