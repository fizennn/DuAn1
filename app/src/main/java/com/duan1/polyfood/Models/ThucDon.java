package com.duan1.polyfood.Models;

public class ThucDon {
    private String id_td;
    private String id_nh;
    private String ten;
    private int gia;
    private String moTa;
    private int danhGia;
    private String phanHoi;

    public ThucDon() {}

    public ThucDon(String id_td, String id_nh, String ten, int gia, String moTa, int danhGia, String phanHoi) {
        this.id_td = id_td;
        this.id_nh = id_nh;
        this.ten = ten;
        this.gia = gia;
        this.moTa = moTa;
        this.danhGia = danhGia;
        this.phanHoi = phanHoi;
    }

    public String getId_td() { return id_td; }
    public void setId_td(String id_td) { this.id_td = id_td; }

    public String getId_nh() { return id_nh; }
    public void setId_nh(String id_nh) { this.id_nh = id_nh; }

    public String getTen() { return ten; }
    public void setTen(String ten) { this.ten = ten; }

    public int getGia() { return gia; }
    public void setGia(int gia) { this.gia = gia; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public int getDanhGia() { return danhGia; }
    public void setDanhGia(int danhGia) { this.danhGia = danhGia; }

    public String getPhanHoi() { return phanHoi; }
    public void setPhanHoi(String phanHoi) { this.phanHoi = phanHoi; }
}