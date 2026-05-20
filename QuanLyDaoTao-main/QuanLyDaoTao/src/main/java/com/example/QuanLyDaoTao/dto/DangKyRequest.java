package com.example.QuanLyDaoTao.dto;

import lombok.Data;
import java.util.List;

@Data
public class DangKyRequest {
    private String maSV;
    private Integer idLop;
    private List<Integer> idLops;
}