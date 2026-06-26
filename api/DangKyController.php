<?php
require_once '../config/database.php';

function dangky_getByMaSV($pdo, $maSV) {
    $stmt = $pdo->prepare("SELECT id_lop FROM dang_ky WHERE ma_sv = ?");
    $stmt->execute([$maSV]);
    echo json_encode($stmt->fetchAll(PDO::FETCH_COLUMN));
}

function dangky_getChiTiet($pdo, $maSV) {
    $stmt = $pdo->prepare("
        SELECT dk.id_dang_ky as idDangKy, l.nhom, l.giang_vien as giangVien,
               m.ma_mon as maMon, m.ten_mon as tenMon, m.so_tin_chi as soTinChi,
               (m.so_tin_chi * 500000) as thanhTien
        FROM dang_ky dk
        JOIN lop_hoc_phan l ON dk.id_lop = l.id_lop
        JOIN mon_hoc m ON l.ma_mon = m.ma_mon
        WHERE dk.ma_sv = ?
    ");
    $stmt->execute([$maSV]);
    echo json_encode($stmt->fetchAll());
}

function dangky_save($pdo, $requestData) {
    $maSV           = $requestData['maSV'] ?? '';
    $idLops         = $requestData['idLops'] ?? [];
    $timeoutMinutes = $requestData['timeoutMinutes'] ?? null;
    $isReset        = $requestData['isReset'] ?? false;

    if ($isReset) {
        $pdo->prepare("UPDATE sinh_vien SET lock_time = NULL WHERE ma_sv = ?")->execute([$maSV]);
        echo "SUCCESS";
        return;
    }

    if (empty($idLops) && isset($requestData['idLop'])) {
        $idLops = [$requestData['idLop']];
    }

    $stmt = $pdo->prepare("SELECT lock_time FROM sinh_vien WHERE ma_sv = ?");
    $stmt->execute([$maSV]);
    $sv = $stmt->fetch();
    if ($sv && $sv['lock_time'] && strtotime($sv['lock_time']) <= time()) {
        echo "LOCKED";
        return;
    }

    if ($timeoutMinutes !== null) {
        $lockTime = date('Y-m-d H:i:s', strtotime("+$timeoutMinutes minutes"));
        $pdo->prepare("UPDATE sinh_vien SET lock_time = ? WHERE ma_sv = ?")->execute([$lockTime, $maSV]);
    }

    if (!empty($idLops)) {
        $placeholders = implode(',', array_fill(0, count($idLops), '?'));
        $stmt = $pdo->prepare("DELETE FROM dang_ky WHERE ma_sv = ? AND id_lop NOT IN ($placeholders)");
        $stmt->execute(array_merge([$maSV], $idLops));
    } else {
        $pdo->prepare("DELETE FROM dang_ky WHERE ma_sv = ?")->execute([$maSV]);
    }

    foreach ($idLops as $idLop) {
        $stmt = $pdo->prepare("SELECT * FROM dang_ky WHERE ma_sv = ? AND id_lop = ?");
        $stmt->execute([$maSV, $idLop]);
        if (!$stmt->fetch()) {
            $pdo->prepare("INSERT INTO dang_ky (ma_sv, id_lop) VALUES (?, ?)")->execute([$maSV, $idLop]);
        }
    }

    _dangky_tinhLaiHocPhi($pdo, $maSV);
    echo "SUCCESS";
}

function _dangky_tinhLaiHocPhi($pdo, $maSV) {
    $stmt = $pdo->prepare("
        SELECT SUM(m.so_tin_chi) as total
        FROM dang_ky dk
        JOIN lop_hoc_phan l ON dk.id_lop = l.id_lop
        JOIN mon_hoc m ON l.ma_mon = m.ma_mon
        WHERE dk.ma_sv = ?
    ");
    $stmt->execute([$maSV]);
    $totalTinChi = $stmt->fetchColumn() ?: 0;
    $tongTien = $totalTinChi * 500000;

    $stmt = $pdo->prepare("SELECT * FROM hoc_phi WHERE ma_sv = ?");
    $stmt->execute([$maSV]);
    $hocPhi = $stmt->fetch();

    if ($hocPhi) {
        $daNop = $hocPhi['so_tien_da_nop'];
        $trangThai = "Chưa nộp";
        if ($tongTien > 0 && $daNop >= $tongTien) $trangThai = "Đã nộp";
        elseif ($daNop > 0) $trangThai = "Đã nộp một phần";
        $pdo->prepare("UPDATE hoc_phi SET so_tien_phai_nop = ?, trang_thai = ? WHERE ma_sv = ?")->execute([$tongTien, $trangThai, $maSV]);
    } else {
        $pdo->prepare("INSERT INTO hoc_phi (ma_sv, hoc_ky, so_tien_phai_nop, so_tien_da_nop, trang_thai) VALUES (?, 'Học kỳ 1 - 2024-2025', ?, 0, 'Chưa nộp')")->execute([$maSV, $tongTien]);
    }
}
?>
