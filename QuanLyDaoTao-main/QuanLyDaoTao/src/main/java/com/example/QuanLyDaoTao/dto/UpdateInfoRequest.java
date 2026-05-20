package com.example.QuanLyDaoTao.dto;

public class UpdateInfoRequest {
    private String maSV;
    private String hoTen;
    private String noiSinh;
    private String ngaySinh;

    // Getters and Setters
    public String getMaSV() { return maSV; }
    public void setMaSV(String maSV) { this.maSV = maSV; }

    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }

    public String getNoiSinh() { return noiSinh; }
    public void setNoiSinh(String noiSinh) { this.noiSinh = noiSinh; }

    public String getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(String ngaySinh) { this.ngaySinh = ngaySinh; }
}
