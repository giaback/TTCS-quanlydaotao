package com.example.QuanLyDaoTao.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "lop_hoc_phan")
public class LopHocPhan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lop")
    private Integer idLop;

    @ManyToOne
    @JoinColumn(name = "ma_mon") // Khóa ngoại liên kết sang bảng mon_hoc
    private MonHoc monHoc;

    @Column(name = "nhom")
    private Integer nhom;

    @Column(name = "giang_vien")
    private String giangVien;

    // --- CÁC HÀM LẤY DỮ LIỆU TỪ DATABASE CHO FRONTEND ---

    // Lấy Mã Môn từ đối tượng MonHoc liên kết
    public String getMaMon() {
        return (monHoc != null) ? monHoc.getMaMon() : "N/A";
    }

    // Lấy Tên Môn từ đối tượng MonHoc liên kết
    public String getTenMon() {
        return (monHoc != null) ? monHoc.getTenMon() : "Chưa cập nhật";
    }

    // --- GETTER & SETTER GỐC ---
    public Integer getIdLop() { return idLop; }
    public void setIdLop(Integer idLop) { this.idLop = idLop; }

    public MonHoc getMonHoc() { return monHoc; }
    public void setMonHoc(MonHoc monHoc) { this.monHoc = monHoc; }

    public Integer getNhom() { return nhom; }
    public void setNhom(Integer nhom) { this.nhom = nhom; }

    public String getGiangVien() { return giangVien; }
    public void setGiangVien(String giangVien) { this.giangVien = giangVien; }
}