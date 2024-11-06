package com.duan1.polyfood.Models;

public class DonHang {
    private String id_dh;
    private String id_nd;
    private String id_nh;
    private String id_td;
    private int soLuong;
    private int gia;
    private String ngayGiao;
    private String trangThai;

    public DonHang() {
    }

    public DonHang(String id_dh, String id_nd, String id_nh, String id_td, int soLuong, int gia, String ngayGiao, String trangThai) {
        this.id_dh = id_dh;
        this.id_nd = id_nd;
        this.id_nh = id_nh;
        this.id_td = id_td;
        this.soLuong = soLuong;
        this.gia = gia;
        this.ngayGiao = ngayGiao;
        this.trangThai = trangThai;
    }


    public String getId_dh() {
        return id_dh;
    }

    public void setId_dh(String id_dh) {
        this.id_dh = id_dh;
    }

    public String getId_nd() {
        return id_nd;
    }

    public void setId_nd(String id_nd) {
        this.id_nd = id_nd;
    }

    public String getId_nh() {
        return id_nh;
    }

    public void setId_nh(String id_nh) {
        this.id_nh = id_nh;
    }

    public String getId_td() {
        return id_td;
    }

    public void setId_td(String id_td) {
        this.id_td = id_td;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public String getNgayGiao() {
        return ngayGiao;
    }

    public void setNgayGiao(String ngayGiao) {
        this.ngayGiao = ngayGiao;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}