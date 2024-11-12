package com.duan1.polyfood.Models;

public class HoaDon {
    private String id_hd;
    private String tenMonAn;
    private int gia;
    private int soLuong;
    private int tongTien;
    private String hinhAnh;
    private String phuongThucThanhToan;

    // Constructor
    public HoaDon() {}

    public HoaDon(String id_hd, String tenMonAn, int gia, int soLuong, int tongTien, String hinhAnh, String phuongThucThanhToan) {
        this.id_hd = id_hd;
        this.tenMonAn = tenMonAn;
        this.gia = gia;
        this.soLuong = soLuong;
        this.tongTien = tongTien;
        this.hinhAnh = hinhAnh;
        this.phuongThucThanhToan = phuongThucThanhToan;
    }

    // Getter và Setter
    public String getId_hd() {
        return id_hd;
    }

    public void setId_hd(String id_dh) {
        this.id_hd = id_hd;
    }

    public String getTenMonAn() {
        return tenMonAn;
    }

    public void setTenMonAn(String tenMonAn) {
        this.tenMonAn = tenMonAn;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getTongTien() {
        return tongTien;
    }

    public void setTongTien(int tongTien) {
        this.tongTien = tongTien;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public String getPhuongThucThanhToan() {
        return phuongThucThanhToan;
    }

    public void setPhuongThucThanhToan(String phuongThucThanhToan) {
        this.phuongThucThanhToan = phuongThucThanhToan;
    }
}
