package com.example.QuanLyDaoTao.controller;

import com.example.QuanLyDaoTao.entity.HocPhi;
import com.example.QuanLyDaoTao.repository.HocPhiRepository;
import com.example.QuanLyDaoTao.service.ThanhToanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hoc-phi")
@CrossOrigin("*")
public class HocPhiController {

    @Autowired
    private HocPhiRepository hocPhiRepository;

    @Autowired
    private ThanhToanService thanhToanService;

    @GetMapping("/{maSV}")
    public HocPhi getHocPhi(@PathVariable String maSV) {
        List<HocPhi> listHocPhi = hocPhiRepository.findBySinhVien_MaSV(maSV);
        if (listHocPhi != null && !listHocPhi.isEmpty()) {
            return listHocPhi.get(0);
        }
        return null;
    }

    // === QUAN TRỌNG: ĐÂY LÀ PHẦN SẼ SỬA LỖI 'POST NOT SUPPORTED' ===
    @PostMapping("/pay")
    public ResponseEntity<Map<String, String>> payTuition(@RequestBody Map<String, Object> payload) {
        String maSV = (String) payload.get("maSV");
        // Chuyển đổi số tiền an toàn
        Double amount = Double.valueOf(payload.get("amount").toString());
        
        String result = thanhToanService.thanhToan(maSV, amount);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", result);
        
        return ResponseEntity.ok(response);
    }
}