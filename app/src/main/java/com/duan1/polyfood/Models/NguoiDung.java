package com.duan1.polyfood.Models;

public class NguoiDung {
    private String id_nd;
    private String hoTen;
    private String imgUrl;
    private int role;
    private String email;
    private String diaChi;
    private String sdt;
    private String sex;
    private String age;
    private String profileImageUrl;
    private int noti;



// ... other methods ...

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
    public NguoiDung() {}

    public NguoiDung(String id_nd, String hoTen, String imgUrl, int role, String email, String diaChi, String sdt) {
        this.id_nd = id_nd;
        this.hoTen = hoTen;
        this.imgUrl = imgUrl;
        this.role = role;
        this.email = email;
        this.diaChi = diaChi;
        this.sdt = sdt;
    }

    public String getId_nd() { return id_nd; }
    public void setId_nd(String id_nd) { this.id_nd = id_nd; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getimgUrl() { return imgUrl; }
    public void setimgUrl(String imgUrl) { this.imgUrl = imgUrl; }

    public int getRole() { return role; }
    public void setRole(int role) { this.role = role; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public int getNoti() {
        return noti;
    }

    public void setNoti(int noti) {
        this.noti = noti;
    }
}