package com.duan1.polyfood;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duan1.polyfood.Adapter.ThucDonNgangAdapter;
import com.duan1.polyfood.Database.ThucDonDAO;
import com.duan1.polyfood.Models.ThucDon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSearch;
    private List<ThucDon> listThucDon;
    private ThucDonNgangAdapter thucDonNgangAdapter;
    private ThucDonDAO thucDonDAO;
    private EditText edtSearch;
    private ImageView imgSortPrice;
    private boolean isAscending = true; // true: Sắp xếp tăng dần, false: Giảm dần

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);

        edtSearch = findViewById(R.id.edtSearch);
        imgSortPrice = findViewById(R.id.imgSortPrice);


        thucDonDAO = new ThucDonDAO();

        recyclerViewSearch = findViewById(R.id.recyclerviewSearch);
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.VERTICAL, false));

        listThucDon = new ArrayList<>();

        thucDonDAO.getAllThucDon(new ThucDonDAO.FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<ThucDon> thucDonList) {
                listThucDon.clear();
                listThucDon.addAll(thucDonList);
                thucDonNgangAdapter = new ThucDonNgangAdapter(listThucDon, SearchActivity.this);
                recyclerViewSearch.setAdapter(thucDonNgangAdapter);
            }

            @Override
            public void onCallback(ThucDon thucDon) {}
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    searchThucDon(s.toString());
                } else {
                    // Nếu không có từ khóa thì lấy lại tất cả ThucDon
                    thucDonDAO.getAllThucDon(new ThucDonDAO.FirebaseCallback() {
                        @Override
                        public void onCallback(ArrayList<ThucDon> thucDonList) {
                            listThucDon.clear();
                            listThucDon.addAll(thucDonList);
                            thucDonNgangAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCallback(ThucDon thucDon) {}
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Sự kiện sắp xếp khi nhấn vào imgSortPrice
        imgSortPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortListByPrice();
            }
        });

    }

    private void sortListByPrice() {
        if (isAscending) {
            Collections.sort(listThucDon, new Comparator<ThucDon>() {
                @Override
                public int compare(ThucDon o1, ThucDon o2) {
                    return Double.compare(o1.getGia(), o2.getGia()); // Sắp xếp tăng dần
                }
            });
            imgSortPrice.setImageResource(R.drawable.tang_dan);
        } else {
            Collections.sort(listThucDon, new Comparator<ThucDon>() {
                @Override
                public int compare(ThucDon o1, ThucDon o2) {
                    return Double.compare(o2.getGia(), o1.getGia()); // Sắp xếp giảm dần
                }
            });
            imgSortPrice.setImageResource(R.drawable.giam_dan);
        }

        isAscending = !isAscending;
        thucDonNgangAdapter.notifyDataSetChanged();
    }


    private void searchThucDon(String name) {
        thucDonDAO.searchThucDonByName(name, new ThucDonDAO.FirebaseCallback() {
            @Override
            public void onCallback(ArrayList<ThucDon> thucDonList) {
                listThucDon.clear();
                listThucDon.addAll(thucDonList);
                thucDonNgangAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCallback(ThucDon thucDon) {

            }
        });
    }

}