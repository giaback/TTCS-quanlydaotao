package com.example.QuanLyDaoTao.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "lich_thi")
public class LichThi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lich_thi")
    private Integer idLichThi;

    @Column(name = "ma_mon")
    private String maMon;

    @Column(name = "ngay_thi")
    private String ngayThi;

    @Column(name = "ca_thi")
    private String caThi;

    @Column(name = "phong_thi")
    private String phongThi;

    @ManyToOne
    @JoinColumn(name = "ma_mon", referencedColumnName = "ma_mon", insertable = false, updatable = false)
    private MonHoc monHoc;

    public Integer getIdLichThi() { return idLichThi; }
    public void setIdLichThi(Integer idLichThi) { this.idLichThi = idLichThi; }

    public String getMaMon() { return maMon; }
    public void setMaMon(String maMon) { this.maMon = maMon; }

    public String getNgayThi() { return ngayThi; }
    public void setNgayThi(String ngayThi) { this.ngayThi = ngayThi; }

    public String getCaThi() { return caThi; }
    public void setCaThi(String caThi) { this.caThi = caThi; }

    public String getPhongThi() { return phongThi; }
    public void setPhongThi(String phongThi) { this.phongThi = phongThi; }

    public MonHoc getMonHoc() { return monHoc; }
    public void setMonHoc(MonHoc monHoc) { this.monHoc = monHoc; }
}
