package com.duan1.polyfood.Models;

public class NguoiDung {
    private String id_nd;
    private String hoTen;
    private String matKhau;
    private int role;
    private String email;
    private String diaChi;
    private String sdt;

    public NguoiDung() {}

    public NguoiDung(String id_nd, String hoTen, String matKhau, int role, String email, String diaChi, String sdt) {
        this.id_nd = id_nd;
        this.hoTen = hoTen;
        this.matKhau = matKhau;
        this.role = role;
        this.email = email;
        this.diaChi = diaChi;
        this.sdt = sdt;
    }

    public String getId_nd() { return id_nd; }
    public void setId_nd(String id_nd) { this.id_nd = id_nd; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }

    public int getRole() { return role; }
    public void setRole(int role) { this.role = role; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }
}