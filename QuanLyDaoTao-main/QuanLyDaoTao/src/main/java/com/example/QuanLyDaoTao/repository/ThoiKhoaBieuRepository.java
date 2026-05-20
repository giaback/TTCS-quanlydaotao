package com.example.QuanLyDaoTao.repository;

import com.example.QuanLyDaoTao.entity.ThoiKhoaBieu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ThoiKhoaBieuRepository extends JpaRepository<ThoiKhoaBieu, Integer> {
    @Query("SELECT tkb FROM ThoiKhoaBieu tkb " +
            "JOIN tkb.lopHocPhan lhp " +
            "JOIN DangKy dk ON dk.lopHocPhan.idLop = lhp.idLop " +
            "WHERE dk.sinhVien.maSV = :maSV")
    List<ThoiKhoaBieu> findByMaSV(String maSV);
}