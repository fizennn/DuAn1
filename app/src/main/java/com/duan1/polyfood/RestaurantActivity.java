package com.duan1.polyfood;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.duan1.polyfood.Database.NhaHangDAO;
import com.duan1.polyfood.Models.NhaHang;

public class RestaurantActivity extends AppCompatActivity {

    EditText edtid, edtten, edtdiachi;
    Button btnadd, btnupdate, btndelete;
    private NhaHangDAO nhaHangDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        edtid = findViewById(R.id.edtId);
        edtten = findViewById(R.id.edtTen);
        edtdiachi = findViewById(R.id.edtDiaChi);
        btnadd = findViewById(R.id.btnAddd);
        btnupdate = findViewById(R.id.btnUpdatee);
        btndelete = findViewById(R.id.btnDeletee);
        nhaHangDAO = new NhaHangDAO();

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = edtid.getText().toString();
                String ten = edtten.getText().toString();
                String diachi = edtdiachi.getText().toString();
                NhaHang nhaHang = new NhaHang(id, ten, diachi, "null", "null","null");
                nhaHangDAO.addNhaHang(nhaHang);
            }
        });

    }
}