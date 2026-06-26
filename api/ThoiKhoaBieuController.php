<?php
require_once '../config/database.php';

function thoikhoabieu_getByMaSV($pdo, $maSV) {
    $stmt = $pdo->prepare("SELECT lock_time FROM sinh_vien WHERE ma_sv = ?");
    $stmt->execute([$maSV]);
    $sv = $stmt->fetch();
    if (!$sv || !$sv['lock_time'] || strtotime($sv['lock_time']) > time()) {
        echo json_encode(["status" => "UNLOCKED"]);
        return;
    }

    $stmt = $pdo->prepare("
        SELECT tkb.thu, tkb.tiet_bat_dau as tietBatDau, tkb.so_tiet as soTiet,
               tkb.thoi_gian_hoc as thoiGianHoc, l.nhom, l.giang_vien as giangVien,
               m.ma_mon as maMon, m.ten_mon as tenMon
        FROM thoi_khoa_bieu tkb
        JOIN lop_hoc_phan l ON tkb.id_lop = l.id_lop
        JOIN dang_ky dk ON dk.id_lop = l.id_lop
        JOIN mon_hoc m ON l.ma_mon = m.ma_mon
        WHERE dk.ma_sv = ?
    ");
    $stmt->execute([$maSV]);
    echo json_encode($stmt->fetchAll());
}
?>
