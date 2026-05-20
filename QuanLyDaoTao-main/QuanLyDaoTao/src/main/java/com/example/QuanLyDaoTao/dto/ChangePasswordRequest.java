package com.example.QuanLyDaoTao.dto;

public class ChangePasswordRequest {
    private String maSV;
    private String oldPassword; // SĐT cũ
    private String newPassword; // SĐT mới

    // Getters and Setters
    public String getMaSV() { return maSV; }
    public void setMaSV(String maSV) { this.maSV = maSV; }

    public String getOldPassword() { return oldPassword; }
    public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
