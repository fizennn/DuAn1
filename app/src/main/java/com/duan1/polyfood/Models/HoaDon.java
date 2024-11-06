package com.duan1.polyfood.Models;

public class HoaDon {
    private String id_tt;
    private String id_dh;
    private String phuongThuc;
    private String ngay;
    private int soTien;
    private String trangThai;

    public HoaDon() {}

    public HoaDon(String id_tt, String id_dh, String phuongThuc, String ngay, int soTien, String trangThai) {
        this.id_tt = id_tt;
        this.id_dh = id_dh;
        this.phuongThuc = phuongThuc;
        this.ngay = ngay;
        this.soTien = soTien;
        this.trangThai = trangThai;
    }

    public String getId_tt() { return id_tt; }
    public void setId_tt(String id_tt) { this.id_tt = id_tt; }

    public String getId_dh() { return id_dh; }
    public void setId_dh(String id_dh) { this.id_dh = id_dh; }

    public String getPhuongThuc() { return phuongThuc; }
    public void setPhuongThuc(String phuongThuc) { this.phuongThuc = phuongThuc; }

    public String getNgay() { return ngay; }
    public void setNgay(String ngay) { this.ngay = ngay; }

    public int getSoTien() { return soTien; }
    public void setSoTien(int soTien) { this.soTien = soTien; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
