package com.duan1.polyfood;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.duan1.polyfood.Database.AuthenticationFireBaseHelper;
import com.duan1.polyfood.Database.NguoiDungDAO;
import com.duan1.polyfood.Models.NguoiDung;

public class InputInfoActivity extends AppCompatActivity {

    private EditText edtName, edtAge, edtAddress, edtPhone;
    private String TAG = "zzzzzzzzzzzz";
    private NguoiDungDAO nguoiDungDAO;
    private AuthenticationFireBaseHelper fireBaseHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_input_info);

        nguoiDungDAO = new NguoiDungDAO();
        fireBaseHelper = new AuthenticationFireBaseHelper();

        Log.d(TAG, "onCreate: INPUT"+fireBaseHelper.getUID());


        edtName = findViewById(R.id.edtName);
        edtAge = findViewById(R.id.edtAge);
        edtAddress = findViewById(R.id.edtAddress);
        edtPhone = findViewById(R.id.edtPhone);
        Button btnGetInfo = findViewById(R.id.btnGetInfo);

        Spinner spinnerGender = findViewById(R.id.spinnerGender);
        String[] genders = {"Nam", "Nữ", "Khác"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genders);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);


        btnGetInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = edtName.getText().toString().trim();
                String age = edtAge.getText().toString().trim();
                String address = edtAddress.getText().toString().trim();
                String phone = edtPhone.getText().toString().trim();
                String gender = spinnerGender.getSelectedItem().toString();


                if (name.isEmpty() || age.isEmpty() || address.isEmpty() || phone.isEmpty()) {
                    Toast.makeText(InputInfoActivity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                } else {
                    NguoiDung nguoiDung = new NguoiDung();
                    nguoiDung.setHoTen(name);
                    nguoiDung.setAge(age);
                    nguoiDung.setDiaChi(address);
                    nguoiDung.setSdt(phone);
                    nguoiDung.setSex(gender);
                    nguoiDung.setEmail(fireBaseHelper.getEmail());


                    String info = "Họ tên: " + name + "\nTuổi: " + age + "\nGiới tính: " + gender + "\nĐịa chỉ: " + address + "\nSĐT: " + phone;
                    Log.d(TAG, "onClick: "+info);

                    nguoiDungDAO.addNguoiDung(nguoiDung);

                    Intent intent = new Intent(InputInfoActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}