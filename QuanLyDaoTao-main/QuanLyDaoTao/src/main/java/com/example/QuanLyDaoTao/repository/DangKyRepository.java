package com.example.QuanLyDaoTao.repository;

import com.example.QuanLyDaoTao.entity.DangKy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DangKyRepository extends JpaRepository<DangKy, Integer> {
    // Spring sẽ tự tìm theo cột ma_sv vì đã có khai báo ở Entity
    List<DangKy> findByMaSv(String maSv);
}