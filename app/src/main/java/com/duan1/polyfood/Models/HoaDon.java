package com.duan1.polyfood.Models;

import java.util.HashMap;
import java.util.Map;

public class HoaDon {
    private String id_hd;
    private String tenMonAn;
    private int gia;
    private int soLuong;
    private int tongTien;
    private String hinhAnh;
    private String phuongThucThanhToan;
    private String trangThai;
    private String ngayDatHang;
    private String id_nd;

    // Constructor
    public HoaDon() {}

    public HoaDon(String id_hd, String tenMonAn, int gia, int soLuong, int tongTien, String hinhAnh, String phuongThucThanhToan, String trangThai, String ngayDatHang) {
        this.id_hd = id_hd;
        this.tenMonAn = tenMonAn;
        this.gia = gia;
        this.soLuong = soLuong;
        this.tongTien = tongTien;
        this.hinhAnh = hinhAnh;
        this.phuongThucThanhToan = phuongThucThanhToan;
        this.trangThai = trangThai;
        this.ngayDatHang = ngayDatHang;
    }


    // Getter và Setter
    public String getId_hd() {
        return id_hd;
    }

    public void setId_hd(String id_hd) {
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

    // Getter và Setter cho trạng thái
    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    // Getter và Setter cho ngày đặt hàng
    public String getNgayDatHang() {
        return ngayDatHang;
    }

    public void setNgayDatHang(String ngayDatHang) {
        this.ngayDatHang = ngayDatHang;
    }

    public void calculateTongTien() {
        this.tongTien = this.gia * this.soLuong;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id_hd", id_hd);
        result.put("tenMonAn", tenMonAn);
        result.put("gia", gia);
        result.put("soLuong", soLuong);
        result.put("tongTien", tongTien);
        result.put("hinhAnh", hinhAnh);
        result.put("phuongThucThanhToan", phuongThucThanhToan);
        result.put("trangThai", trangThai);
        result.put("ngayDatHang", ngayDatHang);
        return result;
    }

    public String getId_nd() {
        return id_nd;
    }

    public void setId_nd(String hoaDon) {
        this.id_nd = hoaDon;
    }
}