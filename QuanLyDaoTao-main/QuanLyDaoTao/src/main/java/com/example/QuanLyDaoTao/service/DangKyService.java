package com.example.QuanLyDaoTao.service;

import com.example.QuanLyDaoTao.entity.DangKy;
import com.example.QuanLyDaoTao.entity.HocPhi;
import com.example.QuanLyDaoTao.entity.LopHocPhan;
import com.example.QuanLyDaoTao.entity.SinhVien;
import com.example.QuanLyDaoTao.repository.DangKyRepository;
import com.example.QuanLyDaoTao.repository.HocPhiRepository;
import com.example.QuanLyDaoTao.repository.LopHocPhanRepository;
import com.example.QuanLyDaoTao.repository.SinhVienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DangKyService {

    @Autowired
    private DangKyRepository dangKyRepository;

    @Autowired
    private SinhVienRepository sinhVienRepository;

    @Autowired
    private LopHocPhanRepository lopHocPhanRepository;

    @Autowired
    private HocPhiRepository hocPhiRepository;

    /**
     * Lấy danh sách ID lớp học phần đã đăng ký của sinh viên.
     */
    public List<Integer> getDaDangKy(String maSV) {
        List<DangKy> oldRegistrations = dangKyRepository.findByMaSv(maSV);
        return oldRegistrations.stream()
                .map(dk -> dk.getLopHocPhan() != null ? dk.getLopHocPhan().getIdLop() : null)
                .filter(id -> id != null)
                .toList();
    }

    /**
     * Lấy chi tiết các môn đã đăng ký (mã môn, tên môn, số tín chỉ, thành tiền).
     */
    public List<java.util.Map<String, Object>> getChiTietDangKy(String maSV) {
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

    /**
     * Xử lý nghiệp vụ đăng ký môn học.
     * - Kiểm tra sinh viên tồn tại
     * - Validate tối thiểu 4 môn
     * - Xóa các môn bị bỏ chọn, thêm các môn mới
     * - Tính lại và cập nhật công nợ học phí
     */
    @Transactional
    public String dangKyMonHoc(String maSV, List<Integer> idLops) {
        // 1. Kiểm tra sinh viên tồn tại
        SinhVien sv = sinhVienRepository.findById(maSV).orElse(null);
        if (sv == null) {
            return "Lỗi: Sinh viên không tồn tại";
        }

        // 2. Validate: phải chọn tối thiểu 4 môn
        if (idLops == null || idLops.size() < 4) {
            throw new IllegalArgumentException("Bạn phải chọn tối thiểu 4 môn học!");
        }

        // 3. Lấy danh sách đăng ký cũ
        List<DangKy> oldRegistrations = dangKyRepository.findByMaSv(maSV);
        List<Integer> oldIdLops = oldRegistrations.stream()
                .filter(dk -> dk.getLopHocPhan() != null)
                .map(dk -> dk.getLopHocPhan().getIdLop())
                .toList();

        // 4. Xóa các môn học bị bỏ chọn (có trong old nhưng không có trong mới)
        for (DangKy dk : oldRegistrations) {
            if (dk.getLopHocPhan() == null) {
                dangKyRepository.deleteById(dk.getIdDangKy()); // Xóa rác
            } else if (!idLops.contains(dk.getLopHocPhan().getIdLop())) {
                dangKyRepository.deleteById(dk.getIdDangKy());
            }
        }

        // 5. Thêm các môn học mới (có trong mới nhưng không có trong old)
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

        // 6. Tính lại tổng tín chỉ và học phí
        int totalTinChi = tinhTongTinChi(idLops);
        long tongTien = totalTinChi * 500000L; // 500,000 VNĐ / 1 tín chỉ

        // 7. Cập nhật hoặc tạo mới công nợ học phí
        capNhatHocPhi(sv, tongTien);

        return "SUCCESS";
    }

    /**
     * Tính tổng số tín chỉ từ danh sách ID lớp học phần.
     */
    private int tinhTongTinChi(List<Integer> idLops) {
        int totalTinChi = 0;
        for (Integer idLop : idLops) {
            LopHocPhan lhp = lopHocPhanRepository.findById(idLop).orElse(null);
            if (lhp != null && lhp.getMonHoc() != null && lhp.getMonHoc().getSoTinChi() != null) {
                totalTinChi += lhp.getMonHoc().getSoTinChi();
            }
        }
        return totalTinChi;
    }

    /**
     * Cập nhật hoặc tạo mới bản ghi công nợ học phí cho sinh viên.
     */
    private void capNhatHocPhi(SinhVien sv, long tongTien) {
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
    }
}
