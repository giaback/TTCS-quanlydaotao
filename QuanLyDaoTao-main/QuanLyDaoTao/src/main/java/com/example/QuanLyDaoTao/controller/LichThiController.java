package com.example.QuanLyDaoTao.controller;

import com.example.QuanLyDaoTao.entity.DangKy;
import com.example.QuanLyDaoTao.repository.DangKyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lich-thi")
@CrossOrigin("*")
public class LichThiController {

    @Autowired
    private DangKyRepository dangKyRepository;

    @Autowired
    private com.example.QuanLyDaoTao.repository.LichThiRepository lichThiRepository;

    @GetMapping("/{maSV}")
    public List<Map<String, Object>> getLichThi(@PathVariable String maSV) {
        List<DangKy> dsDangKy = dangKyRepository.findByMaSv(maSV);
        List<Map<String, Object>> lichThiList = new ArrayList<>();

        for (DangKy dk : dsDangKy) {
            if (dk.getLopHocPhan() != null && dk.getLopHocPhan().getMonHoc() != null) {
                Map<String, Object> lichThiMap = new HashMap<>();
                String maMon = dk.getLopHocPhan().getMonHoc().getMaMon();
                
                lichThiMap.put("maMon", maMon);
                lichThiMap.put("tenMon", dk.getLopHocPhan().getMonHoc().getTenMon());
                lichThiMap.put("nhom", dk.getLopHocPhan().getNhom());
                
                // Tìm lịch thi trong Database
                com.example.QuanLyDaoTao.entity.LichThi lt = lichThiRepository.findByMaMon(maMon).orElse(null);
                
                if (lt != null) {
                    lichThiMap.put("ngayThi", lt.getNgayThi());
                    lichThiMap.put("caThi", lt.getCaThi());
                    lichThiMap.put("phongThi", lt.getPhongThi());
                } else {
                    lichThiMap.put("ngayThi", "Chưa có lịch");
                    lichThiMap.put("caThi", "Chưa có lịch");
                    lichThiMap.put("phongThi", "Chưa có lịch");
                }
                
                lichThiList.add(lichThiMap);
            }
        }

        return lichThiList;
    }
}
