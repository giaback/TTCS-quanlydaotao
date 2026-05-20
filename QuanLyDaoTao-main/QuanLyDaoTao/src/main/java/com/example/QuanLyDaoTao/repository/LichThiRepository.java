package com.example.QuanLyDaoTao.repository;

import com.example.QuanLyDaoTao.entity.LichThi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LichThiRepository extends JpaRepository<LichThi, Integer> {
    Optional<LichThi> findByMaMon(String maMon);
}
