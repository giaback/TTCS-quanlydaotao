package com.example.QuanLyDaoTao.service;

import com.example.QuanLyDaoTao.entity.*;
import com.example.QuanLyDaoTao.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ThanhToanService {
    @Autowired
    private SinhVienRepository sinhVienRepository;
    @Autowired
    private HocPhiRepository hocPhiRepository;

    @Transactional
    public String thanhToan(String maSV, Double soTienDong) {
        SinhVien sv = sinhVienRepository.findById(maSV).orElse(null);
        if (sv == null) return "Sinh viên không tồn tại";
        
        List<HocPhi> dsHocPhi = hocPhiRepository.findBySinhVien_MaSV(maSV);
        if (dsHocPhi == null || dsHocPhi.isEmpty()) {
            return "Sinh viên chưa có công nợ học phí";
        }

        double remainingAmount = soTienDong;
        boolean hasDebt = false;

        for (HocPhi hp : dsHocPhi) {
            if (!"Đã nộp".equals(hp.getTrangThai())) {
                hasDebt = true;
                long canNop = hp.getSoTienPhaiNop() - hp.getSoTienDaNop();
                if (remainingAmount >= canNop) {
                    hp.setSoTienDaNop(hp.getSoTienPhaiNop());
                    hp.setTrangThai("Đã nộp");
                    remainingAmount -= canNop;
                } else {
                    hp.setSoTienDaNop(hp.getSoTienDaNop() + (long)remainingAmount);
                    hp.setTrangThai("Đã nộp một phần");
                    remainingAmount = 0;
                }
                hocPhiRepository.save(hp);
                if (remainingAmount <= 0) break;
            }
        }

        if (!hasDebt) {
            return "Sinh viên đã hoàn thành học phí, không cần đóng thêm!";
        }

        return "Thanh toán thành công!";
    }
}