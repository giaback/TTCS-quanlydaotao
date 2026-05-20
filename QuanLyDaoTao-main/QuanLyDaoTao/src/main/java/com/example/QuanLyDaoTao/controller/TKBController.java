package com.example.QuanLyDaoTao.controller;

import com.example.QuanLyDaoTao.entity.ThoiKhoaBieu;
import com.example.QuanLyDaoTao.repository.ThoiKhoaBieuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tkb")
@CrossOrigin("*")
public class TKBController {

    @Autowired
    private ThoiKhoaBieuRepository tkbRepository;

    @GetMapping("/{maSV}")
    public List<ThoiKhoaBieu> getTkbByMaSV(@PathVariable String maSV) {
        return tkbRepository.findByMaSV(maSV);
    }
}