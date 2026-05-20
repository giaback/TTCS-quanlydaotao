package com.example.QuanLyDaoTao.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "thoi_khoa_bieu")
public class ThoiKhoaBieu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tkb")
    private Integer idTkb;

    @ManyToOne
    @JoinColumn(name = "id_lop")
    private LopHocPhan lopHocPhan;

    @Column(name = "thu")
    private String thu;

    @Column(name = "tiet_bat_dau")
    private Integer tietBatDau;

    @Column(name = "so_tiet")
    private Integer soTiet;

    @Column(name = "thoi_gian_hoc")
    private String thoiGianHoc;

    public Integer getIdTkb() {
        return idTkb;
    }

    public void setIdTkb(Integer idTkb) {
        this.idTkb = idTkb;
    }

    public LopHocPhan getLopHocPhan() {
        return lopHocPhan;
    }

    public void setLopHocPhan(LopHocPhan lopHocPhan) {
        this.lopHocPhan = lopHocPhan;
    }

    public String getThu() {
        return thu;
    }

    public void setThu(String thu) {
        this.thu = thu;
    }

    public Integer getTietBatDau() {
        return tietBatDau;
    }

    public void setTietBatDau(Integer tietBatDau) {
        this.tietBatDau = tietBatDau;
    }

    public Integer getSoTiet() {
        return soTiet;
    }

    public void setSoTiet(Integer soTiet) {
        this.soTiet = soTiet;
    }

    public String getThoiGianHoc() {
        return thoiGianHoc;
    }

    public void setThoiGianHoc(String thoiGianHoc) {
        this.thoiGianHoc = thoiGianHoc;
    }
}
