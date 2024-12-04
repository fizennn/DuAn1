package com.duan1.polyfood.Models;

public class ThucDon {
    private String id_td;
    private String id_nh;
    private String ten;
    private int gia;
    private String moTa;
    private String danhGia;
    private String phanHoi;
    private String hinhAnh;
    private int soLuong;
    private String sticker1,sticker2,sticker3;
    private String goiY;
    private String selected;

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public ThucDon() {}

    public ThucDon(String id_td, String id_nh, String ten, int gia, String moTa, String danhGia, String phanHoi, String hinhAnh) {
        this.id_td = id_td;
        this.id_nh = id_nh;
        this.ten = ten;
        this.gia = gia;
        this.moTa = moTa;
        this.danhGia = danhGia;
        this.phanHoi = phanHoi;
        this.hinhAnh = hinhAnh;
    }

    public ThucDon(String ten, String danhGia, String hinhAnh, int gia) {
        this.ten = ten;
        this.danhGia = danhGia;
        this.hinhAnh = hinhAnh;
        this.gia = gia;
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

    public String getDanhGia() { return danhGia; }
    public void setDanhGia(String danhGia) { this.danhGia = danhGia; }

    public String getPhanHoi() { return phanHoi; }
    public void setPhanHoi(String phanHoi) { this.phanHoi = phanHoi; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getSticker1() {
        return sticker1;
    }

    public void setSticker1(String sticker1) {
        this.sticker1 = sticker1;
    }

    public String getSticker2() {
        return sticker2;
    }

    public void setSticker2(String sticker2) {
        this.sticker2 = sticker2;
    }

    public String getSticker3() {
        return sticker3;
    }

    public void setSticker3(String sticker3) {
        this.sticker3 = sticker3;
    }

    public String getGoiY() {
        return goiY;
    }

    public void setGoiY(String goiY) {
        this.goiY = goiY;
    }
}