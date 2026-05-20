package com.example.QuanLyDaoTao.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "hoc_phi")
public class HocPhi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_hp")
    private Integer idHp;

    @ManyToOne
    @JoinColumn(name = "ma_sv")
    private SinhVien sinhVien;

    @Column(name = "hoc_ky")
    private String hocKy;

    @Column(name = "so_tien_phai_nop")
    private Long soTienPhaiNop;

    // === CHỈ THÊM TRƯỜNG NÀY ===
    @Column(name = "trang_thai", columnDefinition = "nvarchar(50) default 'Chưa nộp'")
    private String trangThai = "Chưa nộp";

    @Column(name = "so_tien_da_nop")
    private Long soTienDaNop = 0L;

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public Long getSoTienDaNop() { return soTienDaNop == null ? 0L : soTienDaNop; }
    public void setSoTienDaNop(Long soTienDaNop) { this.soTienDaNop = soTienDaNop; }

    public Integer getIdHp() { return idHp; }
    public void setIdHp(Integer idHp) { this.idHp = idHp; }
    public SinhVien getSinhVien() { return sinhVien; }
    public void setSinhVien(SinhVien sinhVien) { this.sinhVien = sinhVien; }
    public String getHocKy() { return hocKy; }
    public void setHocKy(String hocKy) { this.hocKy = hocKy; }
    public Long getSoTienPhaiNop() { return soTienPhaiNop == null ? 0L : soTienPhaiNop; }
    public void setSoTienPhaiNop(Long soTienPhaiNop) { this.soTienPhaiNop = soTienPhaiNop; }
}