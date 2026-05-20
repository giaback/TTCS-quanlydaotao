package com.example.QuanLyDaoTao.repository;

import com.example.QuanLyDaoTao.entity.SinhVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SinhVienRepository extends JpaRepository<SinhVien, String> {

    SinhVien findByMaSVAndSoDienThoai(String maSV, String soDienThoai);
}