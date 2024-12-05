package com.duan1.polyfood;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duan1.polyfood.Adapter.StickerTimKiemAdapter;
import com.duan1.polyfood.Adapter.ThucDonNgangAdapter;
import com.duan1.polyfood.Database.StickerDao;
import com.duan1.polyfood.Database.ThucDonDAO;
import com.duan1.polyfood.Models.Sticker;
import com.duan1.polyfood.Models.ThucDon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSearch;
    private List<ThucDon> listThucDon;
    private ThucDonNgangAdapter thucDonNgangAdapter;
    private ThucDonDAO thucDonDAO;
    private ImageView imgSortPrice;
    private boolean isAscending = true; // true: Sắp xếp tăng dần, false: Giảm dần
    private RecyclerView recyclerViewSticker;
    private StickerTimKiemAdapter StickerTimKiemAdapter;
    private List<ThucDon> listThucDon2;
    private List<Sticker> stickerList;
    private ImageView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        EditText edtSearch = findViewById(R.id.edtSearch);
        imgSortPrice = findViewById(R.id.imgSortPrice);
        ImageView imgBack = findViewById(R.id.imgBack);
        recyclerViewSticker = findViewById(R.id.recyclerViewSticker);
        StickerDao stickerDao = new StickerDao();
        stickerList = new ArrayList<>();
        view = findViewById(R.id.imgBack);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        thucDonDAO = new ThucDonDAO();

        recyclerViewSearch = findViewById(R.id.recyclerviewSearch);
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.VERTICAL, false));

        listThucDon2 = new ArrayList<>();
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

            @Override
            public void onCallback(Float star) {

            }
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

                        @Override
                        public void onCallback(Float star) {

                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        stickerDao.getAll(new StickerDao.StickerCallback() {
            @Override
            public void onSuccess(Sticker sticker) {

            }

            @Override
            public void onSuccess(List<Sticker> stickerList1) {

                stickerList.clear();

                stickerList.addAll(stickerList1);

                recyclerViewSticker.setLayoutManager(new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.HORIZONTAL, false));

                StickerTimKiemAdapter = new StickerTimKiemAdapter(stickerList1, SearchActivity.this, new StickerTimKiemAdapter.OnItemClickListener() {
                    @Override
                    public void onEdit(Sticker sticker) {

                    }
                    @Override
                    public void onDelete(Sticker sticker) {

                    }

                    @Override
                    public void onClick(Sticker sticker,String check) {
                        thucDonDAO.getAllThucDon(new ThucDonDAO.FirebaseCallback() {
                            @Override
                            public void onCallback(ArrayList<ThucDon> thucDonList) {
                                listThucDon2.clear();
                                listThucDon.clear();
                                listThucDon.addAll(thucDonList);

                                List<Sticker> stickerTimKiem = new ArrayList<>();
                                if (check==null){
                                    stickerTimKiem.remove(sticker);
                                }else {
                                    stickerTimKiem.add(sticker);
                                }


                                if (!stickerTimKiem.isEmpty()) {
                                    // Tạo tập hợp các ID sticker để kiểm tra nhanh hơn
                                    Set<String> stickerIds = stickerTimKiem.stream()
                                            .map(Sticker::getId)
                                            .filter(Objects::nonNull) // Loại bỏ các giá trị null
                                            .map(String::toLowerCase) // Đồng bộ chữ hoa/thường
                                            .collect(Collectors.toSet());

                                    for (ThucDon thucDon : listThucDon) {
                                        // Lấy sticker1, sticker2, sticker3 và kiểm tra null trước khi so sánh
                                        String sticker1 = thucDon.getSticker1() != null ? thucDon.getSticker1().toLowerCase() : "";
                                        String sticker2 = thucDon.getSticker2() != null ? thucDon.getSticker2().toLowerCase() : "";
                                        String sticker3 = thucDon.getSticker3() != null ? thucDon.getSticker3().toLowerCase() : "";

                                        // Kiểm tra nếu bất kỳ sticker nào khớp
                                        if (stickerIds.contains(sticker1) || stickerIds.contains(sticker2) || stickerIds.contains(sticker3)) {
                                            listThucDon2.add(thucDon);
                                        }
                                    }
                                } else {
                                    // Thêm toàn bộ `listThucDon` khi `stickerTimKiem` rỗng
                                    listThucDon2.addAll(listThucDon);
                                }



                                if(listThucDon2 != null){
                                    listThucDon.clear();
                                    listThucDon.addAll(listThucDon2);
                                    thucDonNgangAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCallback(ThucDon thucDon) {

                            }

                            @Override
                            public void onCallback(Float star) {

                            }
                        });
                    }


                });
                recyclerViewSticker.setAdapter(StickerTimKiemAdapter);


            }

            @Override
            public void onFailure(String error) {

            }
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

            @Override
            public void onCallback(Float star) {

            }
        });
    }

}