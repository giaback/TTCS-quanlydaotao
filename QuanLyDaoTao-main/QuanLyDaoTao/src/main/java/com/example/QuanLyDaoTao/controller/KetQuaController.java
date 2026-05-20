package com.example.QuanLyDaoTao.controller;

import com.example.QuanLyDaoTao.entity.KetQuaHocTap;
import com.example.QuanLyDaoTao.repository.KetQuaHocTapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/ket-qua") // Khớp với lệnh gọi trong HTML
@CrossOrigin("*") // Cho phép trình duyệt truy cập
public class KetQuaController {

    @Autowired
    private KetQuaHocTapRepository ketQuaHocTapRepository;

    @GetMapping("/{maSv}")
    public List<KetQuaHocTap> getKetQua(@PathVariable String maSv) {
        if (maSv == null || maSv.equals("null") || maSv.isEmpty()) {
            return new ArrayList<>();
        }
        return ketQuaHocTapRepository.findByMaSv(maSv);
    }
}