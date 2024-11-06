package com.duan1.polyfood.Models;

public class NhaHang {
    private String id_nh;
    private String ten;
    private String diaChi;
    private String email;
    private String sdt;
    private String gioiThieu;

    public NhaHang() {}

    public NhaHang(String id_nh, String ten, String diaChi, String email, String sdt, String gioiThieu) {
        this.id_nh = id_nh;
        this.ten = ten;
        this.diaChi = diaChi;
        this.email = email;
        this.sdt = sdt;
        this.gioiThieu = gioiThieu;
    }

    public String getId_nh() { return id_nh; }
    public void setId_nh(String id_nh) { this.id_nh = id_nh; }

    public String getTen() { return ten; }
    public void setTen(String ten) { this.ten = ten; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public String getGioiThieu() { return gioiThieu; }
    public void setGioiThieu(String gioiThieu) { this.gioiThieu = gioiThieu; }
}