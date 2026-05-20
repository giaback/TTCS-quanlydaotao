package com.example.QuanLyDaoTao.controller;

import com.example.QuanLyDaoTao.dto.LopHocPhanDTO;
import com.example.QuanLyDaoTao.entity.LopHocPhan;
import com.example.QuanLyDaoTao.repository.LopHocPhanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/lop-hoc-phan")
@CrossOrigin("*")
public class LopHocPhanController {

    @Autowired
    private LopHocPhanRepository lopHocPhanRepository;

    @GetMapping("/all")
    public List<LopHocPhanDTO> getAllLopHocPhan() {
        List<LopHocPhan> lhpList = lopHocPhanRepository.findAll();
        List<LopHocPhanDTO> resultList = new ArrayList<>();

        for (LopHocPhan lhp : lhpList) {
            LopHocPhanDTO dto = new LopHocPhanDTO();
            dto.setIdLop(lhp.getIdLop());
            dto.setNhom(lhp.getNhom());
            dto.setGiangVien(lhp.getGiangVien());

            if (lhp.getMonHoc() != null) {
                dto.setMaMon(lhp.getMonHoc().getMaMon());
                dto.setTenMon(lhp.getMonHoc().getTenMon());
                dto.setSoTinChi(lhp.getMonHoc().getSoTinChi());
            }

            resultList.add(dto);
        }

        return resultList;
    }
}