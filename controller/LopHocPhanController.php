<?php

function lophocphan_getAll($pdo) {
    $stmt = $pdo->prepare("
        SELECT l.id_lop as idLop, l.nhom, l.giang_vien as giangVien,
               m.ma_mon as maMon, m.ten_mon as tenMon, m.so_tin_chi as soTinChi
        FROM lop_hoc_phan l
        JOIN mon_hoc m ON l.ma_mon = m.ma_mon
    ");
    $stmt->execute();
    echo json_encode($stmt->fetchAll());
}
