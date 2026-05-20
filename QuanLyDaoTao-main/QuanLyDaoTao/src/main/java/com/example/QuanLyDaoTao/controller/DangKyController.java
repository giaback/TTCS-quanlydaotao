package com.example.QuanLyDaoTao.controller;

import com.example.QuanLyDaoTao.dto.DangKyRequest;
import com.example.QuanLyDaoTao.entity.DangKy;
import com.example.QuanLyDaoTao.entity.HocPhi;
import com.example.QuanLyDaoTao.entity.LopHocPhan;
import com.example.QuanLyDaoTao.entity.SinhVien;
import com.example.QuanLyDaoTao.repository.DangKyRepository;
import com.example.QuanLyDaoTao.repository.HocPhiRepository;
import com.example.QuanLyDaoTao.repository.LopHocPhanRepository;
import com.example.QuanLyDaoTao.repository.SinhVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dang-ky")
@CrossOrigin("*")
public class DangKyController {

    @Autowired
    private DangKyRepository dangKyRepository;

    @Autowired
    private SinhVienRepository sinhVienRepository;

    @Autowired
    private LopHocPhanRepository lopHocPhanRepository;

    @Autowired
    private HocPhiRepository hocPhiRepository;

    @GetMapping("/{maSV}")
    public List<Integer> getDaDangKy(@PathVariable String maSV) {
        List<DangKy> oldRegistrations = dangKyRepository.findByMaSv(maSV);
        return oldRegistrations.stream()
                .map(dk -> dk.getLopHocPhan() != null ? dk.getLopHocPhan().getIdLop() : null)
                .filter(id -> id != null)
                .toList();
    }

    @GetMapping("/chi-tiet/{maSV}")
    public List<java.util.Map<String, Object>> getChiTietDangKy(@PathVariable String maSV) {
        List<DangKy> oldRegistrations = dangKyRepository.findByMaSv(maSV);
        return oldRegistrations.stream()
                .filter(dk -> dk.getLopHocPhan() != null && dk.getLopHocPhan().getMonHoc() != null)
                .map(dk -> {
                    java.util.Map<String, Object> map = new java.util.HashMap<>();
                    map.put("maMon", dk.getLopHocPhan().getMonHoc().getMaMon());
                    map.put("tenMon", dk.getLopHocPhan().getMonHoc().getTenMon());
                    map.put("soTinChi", dk.getLopHocPhan().getMonHoc().getSoTinChi());
                    map.put("thanhTien", dk.getLopHocPhan().getMonHoc().getSoTinChi() * 500000L);
                    return map;
                })
                .toList();
    }

    @PostMapping
    @Transactional
    public String dangKyMonHoc(@RequestBody DangKyRequest request) {
        SinhVien sv = sinhVienRepository.findById(request.getMaSV()).orElse(null);
        if (sv == null) {
            return "Lỗi: Sinh viên không tồn tại";
        }

        List<Integer> idLops = request.getIdLops();
        if (idLops == null || idLops.isEmpty()) {
            if (request.getIdLop() != null) {
                idLops = List.of(request.getIdLop());
            } else {
                return "Lỗi: Không có môn học nào được chọn";
            }
        }

        List<DangKy> oldRegistrations = dangKyRepository.findByMaSv(request.getMaSV());
        
        if (idLops.size() < 4) {
            return "Lỗi: Tổng số môn học đăng ký tối thiểu phải là 4 môn";
        }

        List<Integer> oldIdLops = oldRegistrations.stream()
                .filter(dk -> dk.getLopHocPhan() != null)
                .map(dk -> dk.getLopHocPhan().getIdLop())
                .toList();

        // 1. Xóa các môn học bị bỏ chọn (có trong old nhưng không có trong mới)
        for (DangKy dk : oldRegistrations) {
            if (dk.getLopHocPhan() == null) {
                dangKyRepository.deleteById(dk.getIdDangKy()); // Xóa rác
            } else if (!idLops.contains(dk.getLopHocPhan().getIdLop())) {
                dangKyRepository.deleteById(dk.getIdDangKy());
            }
        }

        // 2. Thêm các môn học mới (có trong mới nhưng không có trong old)
        for (Integer idLop : idLops) {
            if (!oldIdLops.contains(idLop)) {
                LopHocPhan lhp = lopHocPhanRepository.findById(idLop).orElse(null);
                if (lhp != null) {
                    DangKy dangKy = new DangKy();
                    dangKy.setMaSv(sv.getMaSV());
                    dangKy.setSinhVien(sv);
                    dangKy.setLopHocPhan(lhp);
                    dangKyRepository.save(dangKy);
                }
            }
        }

        // 3. Cập nhật lại tính toán học phí trực tiếp từ danh sách idLops hiện tại
        int totalTinChi = 0;
        for (Integer idLop : idLops) {
            LopHocPhan lhp = lopHocPhanRepository.findById(idLop).orElse(null);
            if (lhp != null && lhp.getMonHoc() != null && lhp.getMonHoc().getSoTinChi() != null) {
                totalTinChi += lhp.getMonHoc().getSoTinChi();
            }
        }

        // Tính tiền học phí (giả sử 500,000 VNĐ / 1 tín chỉ)
        long tongTien = totalTinChi * 500000L;

        // Cập nhật hoặc tạo mới công nợ học phí cho sinh viên
        List<HocPhi> dsHocPhi = hocPhiRepository.findBySinhVien_MaSV(sv.getMaSV());
        HocPhi hocPhi;
        if (dsHocPhi != null && !dsHocPhi.isEmpty()) {
            hocPhi = dsHocPhi.get(0);
        } else {
            hocPhi = new HocPhi();
            hocPhi.setSinhVien(sv);
            hocPhi.setHocKy("Học kỳ 1 - 2024-2025");
            hocPhi.setSoTienDaNop(0L);
        }

        hocPhi.setSoTienPhaiNop(tongTien);
        
        if (hocPhi.getSoTienDaNop() >= hocPhi.getSoTienPhaiNop()) {
            hocPhi.setTrangThai("Đã nộp");
        } else if (hocPhi.getSoTienDaNop() > 0) {
            hocPhi.setTrangThai("Đã nộp một phần");
        } else {
            hocPhi.setTrangThai("Chưa nộp");
        }

        hocPhiRepository.save(hocPhi);

        return "SUCCESS";
    }
}