package com.example.QuanLyDaoTao.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "mon_hoc")
public class MonHoc {
    @Id
    @Column(name = "ma_mon")
    private String maMon;

    @Column(name = "ten_mon")
    private String tenMon;

    @Column(name = "so_tin_chi")
    private Integer soTinChi;

    public String getMaMon() {
        return maMon;
    }

    public void setMaMon(String maMon) {
        this.maMon = maMon;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public Integer getSoTinChi() {
        return soTinChi;
    }

    public void setSoTinChi(Integer soTinChi) {
        this.soTinChi = soTinChi;
    }


}