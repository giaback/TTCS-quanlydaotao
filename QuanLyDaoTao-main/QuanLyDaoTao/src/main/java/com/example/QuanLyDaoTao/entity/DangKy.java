package com.example.QuanLyDaoTao.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "dang_ky")
public class DangKy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dang_ky")
    private Integer idDangKy;

    // PHẢI THÊM DÒNG NÀY: Để Repository tìm được theo tên "maSv"
    @Column(name = "ma_sv")
    private String maSv;

    @ManyToOne
    @JoinColumn(name = "ma_sv", insertable = false, updatable = false)
    private SinhVien sinhVien;

    @ManyToOne
    @JoinColumn(name = "id_lop")
    private LopHocPhan lopHocPhan;

    // --- GETTER VÀ SETTER (Viết đầy đủ cho bạn dễ nhìn) ---

    public Integer getIdDangKy() {
        return idDangKy;
    }

    public void setIdDangKy(Integer idDangKy) {
        this.idDangKy = idDangKy;
    }

    public String getMaSv() {
        return maSv;
    }

    public void setMaSv(String maSv) {
        this.maSv = maSv;
    }

    public SinhVien getSinhVien() {
        return sinhVien;
    }

    public void setSinhVien(SinhVien sinhVien) {
        this.sinhVien = sinhVien;
    }

    public LopHocPhan getLopHocPhan() {
        return lopHocPhan;
    }

    public void setLopHocPhan(LopHocPhan lopHocPhan) {
        this.lopHocPhan = lopHocPhan;
    }
}