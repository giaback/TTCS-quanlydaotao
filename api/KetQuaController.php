<?php
require_once '../config/database.php';

function ketqua_getByMaSV($pdo, $maSV) {
    $stmt = $pdo->prepare("
        SELECT kq.diem_thi as diemThi, kq.diem_tk_10 as diemTk10,
               kq.diem_tk_4 as diemTk4, kq.diem_tk_chu as diemTkChu,
               kq.hoc_ky as hocKy, m.ma_mon as maMon,
               m.ten_mon as tenMon, m.so_tin_chi as soTinChi
        FROM ket_qua_hoc_tap kq
        JOIN mon_hoc m ON kq.ma_mon = m.ma_mon
        WHERE kq.ma_sv = ?
    ");
    $stmt->execute([$maSV]);
    echo json_encode($stmt->fetchAll());
}
?>
