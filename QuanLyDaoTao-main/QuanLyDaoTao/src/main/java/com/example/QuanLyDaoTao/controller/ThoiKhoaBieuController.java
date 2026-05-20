package com.example.QuanLyDaoTao.controller;

import com.example.QuanLyDaoTao.entity.ThoiKhoaBieu;
import com.example.QuanLyDaoTao.repository.ThoiKhoaBieuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/thoi-khoa-bieu")
@CrossOrigin("*")
public class ThoiKhoaBieuController {

    @Autowired
    private ThoiKhoaBieuRepository thoiKhoaBieuRepository;

    @GetMapping("/{maSV}")
    public List<Map<String, Object>> getThoiKhoaBieu(@PathVariable String maSV) {
        List<ThoiKhoaBieu> dsTkb = thoiKhoaBieuRepository.findByMaSV(maSV);
        List<Map<String, Object>> result = new ArrayList<>();

        for (ThoiKhoaBieu tkb : dsTkb) {
            Map<String, Object> map = new HashMap<>();
            if (tkb.getLopHocPhan() != null && tkb.getLopHocPhan().getMonHoc() != null) {
                map.put("maMon", tkb.getLopHocPhan().getMonHoc().getMaMon());
                map.put("tenMon", tkb.getLopHocPhan().getMonHoc().getTenMon());
                map.put("nhom", tkb.getLopHocPhan().getNhom());
                map.put("giangVien", tkb.getLopHocPhan().getGiangVien());
            } else {
                map.put("maMon", "N/A");
                map.put("tenMon", "N/A");
                map.put("nhom", "N/A");
                map.put("giangVien", "N/A");
            }
            map.put("thu", tkb.getThu());
            map.put("tietBatDau", tkb.getTietBatDau());
            map.put("soTiet", tkb.getSoTiet());
            map.put("thoiGianHoc", tkb.getThoiGianHoc());
            
            result.add(map);
        }

        return result;
    }
}
