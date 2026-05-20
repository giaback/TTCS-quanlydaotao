package com.example.QuanLyDaoTao.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "sinh_vien")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SinhVien {

    @Id
    @Column(name = "ma_sv", length = 20)
    private String maSV;

    @Column(name = "ho_ten", columnDefinition = "nvarchar(100)")
    private String hoTen;

    @Column(name = "ngay_sinh")
    private LocalDate ngaySinh;

    @Column(name = "gioi_tinh", length = 10)
    private String gioiTinh;

    @Column(name = "noi_sinh", columnDefinition = "nvarchar(100)")
    private String noiSinh;

    @Column(name = "so_dien_thoai", length = 15)
    private String soDienThoai;

    @Column(name = "lop")
    private String lop;

    @Column(name = "nganh")
    private String nganh;

    // === CHỈ THÊM TRƯỜNG NÀY ===
    @Column(name = "balance")
    private Double balance = 10000000.0; 

    public Double getBalance() { return balance; }
    public void setBalance(Double balance) { this.balance = balance; }
}