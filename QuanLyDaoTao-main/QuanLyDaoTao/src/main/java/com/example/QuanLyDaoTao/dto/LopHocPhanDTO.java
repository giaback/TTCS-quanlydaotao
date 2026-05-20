package com.example.QuanLyDaoTao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LopHocPhanDTO {
    private Integer idLop;
    private String maMon;
    private String tenMon;
    private Integer soTinChi;
    private Integer nhom;
    private String giangVien;
}