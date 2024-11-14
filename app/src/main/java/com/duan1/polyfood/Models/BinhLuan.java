package com.duan1.polyfood.Models;

public class BinhLuan {
    private String id,ten,anh,bl,idbl,ngay;
    private int sao;

    public BinhLuan() {
    }

    public BinhLuan(String id, String ten, String anh, String bl, int sao) {
        this.id = id;
        this.ten = ten;
        this.anh = anh;
        this.bl = bl;
        this.sao = sao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

    public String getBl() {
        return bl;
    }

    public void setBl(String bl) {
        this.bl = bl;
    }

    public int getSao() {
        return sao;
    }

    public void setSao(int sao) {
        this.sao = sao;
    }

    public String getIdbl() {
        return idbl;
    }

    public void setIdbl(String idbl) {
        this.idbl = idbl;
    }


    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }
}


