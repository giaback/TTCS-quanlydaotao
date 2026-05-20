package com.example.QuanLyDaoTao.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ket_qua_hoc_tap")
public class KetQuaHocTap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_kq")
    private Integer idKq;

    @Column(name = "ma_sv")
    private String maSv;

    // THÊM DÒNG NÀY: Để JavaScript nhận được mã môn ở lớp ngoài cùng
    @Column(name = "ma_mon")
    private String maMon;

    @ManyToOne
    @JoinColumn(name = "ma_sv", referencedColumnName = "ma_sv", insertable = false, updatable = false)
    private SinhVien sinhVien;

    @ManyToOne
    @JoinColumn(name = "ma_mon", referencedColumnName = "ma_mon", insertable = false, updatable = false)
    private MonHoc monHoc;

    @Column(name = "diem_thi")
    private Double diemThi;

    @Column(name = "diem_tk_10")
    private Double diemTk10;

    @Column(name = "diem_tk_4")
    private Double diemTk4;

    @Column(name = "diem_tk_chu")
    private String diemTkChu;

    @Column(name = "hoc_ky")
    private String hocKy;

    // --- GETTER VÀ SETTER ---

    public Integer getIdKq() { return idKq; }
    public void setIdKq(Integer idKq) { this.idKq = idKq; }

    public String getMaSv() { return maSv; }
    public void setMaSv(String maSv) { this.maSv = maSv; }

    // Thêm Getter/Setter cho maMon
    public String getMaMon() { return maMon; }
    public void setMaMon(String maMon) { this.maMon = maMon; }

    public SinhVien getSinhVien() { return sinhVien; }
    public void setSinhVien(SinhVien sinhVien) { this.sinhVien = sinhVien; }

    public MonHoc getMonHoc() { return monHoc; }
    public void setMonHoc(MonHoc monHoc) { this.monHoc = monHoc; }

    public Double getDiemThi() { return diemThi; }
    public void setDiemThi(Double diemThi) { this.diemThi = diemThi; }

    public Double getDiemTk10() { return diemTk10; }
    public void setDiemTk10(Double diemTk10) { this.diemTk10 = diemTk10; }

    public Double getDiemTk4() { return diemTk4; }
    public void setDiemTk4(Double diemTk4) { this.diemTk4 = diemTk4; }

    public String getDiemTkChu() { return diemTkChu; }
    public void setDiemTkChu(String diemTkChu) { this.diemTkChu = diemTkChu; }

    public String getHocKy() { return hocKy; }
    public void setHocKy(String hocKy) { this.hocKy = hocKy; }
}