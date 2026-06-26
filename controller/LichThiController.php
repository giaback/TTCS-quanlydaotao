<?php

function lichthi_getByMaSV($pdo, $maSV) {
    $stmt = $pdo->prepare("SELECT lock_time FROM sinh_vien WHERE ma_sv = ?");
    $stmt->execute([$maSV]);
    $sv = $stmt->fetch();
    if (!$sv || !$sv['lock_time'] || strtotime($sv['lock_time']) > time()) {
        echo json_encode(["status" => "UNLOCKED"]);
        return;
    }

    $stmt = $pdo->prepare("
        SELECT lt.ngay_thi as ngayThi, lt.ca_thi as caThi, lt.phong_thi as phongThi,
               m.ma_mon as maMon, m.ten_mon as tenMon
        FROM lich_thi lt
        JOIN mon_hoc m ON lt.ma_mon = m.ma_mon
        JOIN lop_hoc_phan l ON l.ma_mon = m.ma_mon
        JOIN dang_ky dk ON dk.id_lop = l.id_lop
        WHERE dk.ma_sv = ?
        GROUP BY lt.id_lich_thi
    ");
    $stmt->execute([$maSV]);
    echo json_encode($stmt->fetchAll());
}
?>

