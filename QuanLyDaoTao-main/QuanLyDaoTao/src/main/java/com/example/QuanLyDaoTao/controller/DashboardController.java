package com.example.QuanLyDaoTao.controller;

import com.example.QuanLyDaoTao.entity.DangKy;
import com.example.QuanLyDaoTao.entity.KetQuaHocTap;
import com.example.QuanLyDaoTao.repository.DangKyRepository;
import com.example.QuanLyDaoTao.repository.KetQuaHocTapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin("*")
public class DashboardController {

    @Autowired
    private DangKyRepository dangKyRepository;

    @Autowired
    private KetQuaHocTapRepository ketQuaHocTapRepository;

    @GetMapping("/dangky/{maSV}")
    public List<DangKy> getDangKy(@PathVariable String maSV) {
        return dangKyRepository.findByMaSv(maSV);
    }

    @GetMapping("/hocphi/{maSV}")
    public Map<String, Object> getHocPhi(@PathVariable String maSV) {
        List<DangKy> danhSachDangKy = dangKyRepository.findByMaSv(maSV);
        int tongSoTinChi = 0;
        for (DangKy dk : danhSachDangKy) {
            if (dk.getLopHocPhan() != null && dk.getLopHocPhan().getMonHoc() != null) {
                tongSoTinChi += dk.getLopHocPhan().getMonHoc().getSoTinChi();
            }
        }
        Map<String, Object> response = new HashMap<>();
        response.put("tongSoTinChi", tongSoTinChi);
        response.put("soTienPhaiNop", (long) tongSoTinChi * 1600000);
        return response;
    }

    @GetMapping("/ketqua/{maSV}")
    public List<KetQuaHocTap> getKetQuaHocTap(@PathVariable String maSV) {
        return ketQuaHocTapRepository.findByMaSv(maSV);
    }
}