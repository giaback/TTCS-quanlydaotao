package com.example.QuanLyDaoTao.controller;

import com.example.QuanLyDaoTao.entity.SinhVien;
import com.example.QuanLyDaoTao.repository.SinhVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.QuanLyDaoTao.dto.UpdateInfoRequest;
import com.example.QuanLyDaoTao.dto.ChangePasswordRequest;

@RestController
@RequestMapping("/api/sinhvien")
@CrossOrigin("*")
public class SinhVienController {

    @Autowired
    private SinhVienRepository sinhVienRepository;


    @GetMapping("/{maSV}")
    public SinhVien getThongTinSinhVien(@PathVariable String maSV) {
        return sinhVienRepository.findById(maSV).orElse(null);
    }

    @PostMapping("/login")
    public String login(@RequestBody com.example.QuanLyDaoTao.dto.LoginRequest request) {

        SinhVien sv = sinhVienRepository.findByMaSVAndSoDienThoai(request.getUsername(), request.getPassword());

        if (sv != null) {
            return "SUCCESS";
        } else {
            return "FAIL";
        }
    }

    @PutMapping("/update-info")
    public String updateInfo(@RequestBody UpdateInfoRequest request) {
        SinhVien sv = sinhVienRepository.findById(request.getMaSV()).orElse(null);
        if (sv == null) {
            return "FAIL";
        }
        
        sv.setHoTen(request.getHoTen());
        sv.setNoiSinh(request.getNoiSinh());
        if (request.getNgaySinh() != null && !request.getNgaySinh().trim().isEmpty()) {
            sv.setNgaySinh(java.time.LocalDate.parse(request.getNgaySinh()));
        }
        sinhVienRepository.save(sv);
        return "SUCCESS";
    }

    @PutMapping("/change-password")
    public String changePassword(@RequestBody ChangePasswordRequest request) {
        SinhVien sv = sinhVienRepository.findByMaSVAndSoDienThoai(request.getMaSV(), request.getOldPassword());
        if (sv == null) {
            return "SAI_MAT_KHAU";
        }
        
        sv.setSoDienThoai(request.getNewPassword());
        sinhVienRepository.save(sv);
        return "SUCCESS";
    }
}