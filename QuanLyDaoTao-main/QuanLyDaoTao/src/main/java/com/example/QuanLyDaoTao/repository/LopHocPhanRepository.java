package com.example.QuanLyDaoTao.repository;

import com.example.QuanLyDaoTao.entity.LopHocPhan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LopHocPhanRepository extends JpaRepository<LopHocPhan, Integer> {
}