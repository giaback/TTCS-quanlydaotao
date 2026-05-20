package com.example.QuanLyDaoTao.controller;

import com.example.QuanLyDaoTao.dto.DangKyRequest;
import com.example.QuanLyDaoTao.service.DangKyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dang-ky")
@CrossOrigin("*")
public class DangKyController {

    @Autowired
    private DangKyService dangKyService;

    @GetMapping("/{maSV}")
    public List<Integer> getDaDangKy(@PathVariable String maSV) {
        return dangKyService.getDaDangKy(maSV);
    }

    @GetMapping("/chi-tiet/{maSV}")
    public List<java.util.Map<String, Object>> getChiTietDangKy(@PathVariable String maSV) {
        return dangKyService.getChiTietDangKy(maSV);
    }

    @PostMapping
    public String dangKyMonHoc(@RequestBody DangKyRequest request) {
        List<Integer> idLops = request.getIdLops();
        if ((idLops == null || idLops.isEmpty()) && request.getIdLop() != null) {
            idLops = List.of(request.getIdLop());
        }

        try {
            return dangKyService.dangKyMonHoc(request.getMaSV(), idLops);
        } catch (IllegalArgumentException e) {
            return "Lỗi: " + e.getMessage();
        }
    }
}