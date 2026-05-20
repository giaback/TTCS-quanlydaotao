package com.example.QuanLyDaoTao.repository;

import com.example.QuanLyDaoTao.entity.KetQuaHocTap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface KetQuaHocTapRepository extends JpaRepository<KetQuaHocTap, Integer> {
    // Tìm danh sách điểm theo Mã SV (chuỗi String) thay vì ID số
    List<KetQuaHocTap> findByMaSv(String maSv);
}