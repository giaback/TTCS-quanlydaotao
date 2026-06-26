<?php
require_once '../config/database.php'; // Included via index.php

function sinhvien_login($pdo, $requestData) {
    $username = $requestData['username'] ?? '';
    $password = $requestData['password'] ?? '';
    $stmt = $pdo->prepare("SELECT * FROM sinh_vien WHERE ma_sv = ? AND so_dien_thoai = ?");
    $stmt->execute([$username, $password]);
    echo $stmt->fetch() ? "SUCCESS" : "FAIL";
}

function sinhvien_getById($pdo, $maSV) {
    $stmt = $pdo->prepare("SELECT * FROM sinh_vien WHERE ma_sv = ?");
    $stmt->execute([$maSV]);
    $sv = $stmt->fetch();
    if ($sv) {
        echo json_encode([
            'maSV'        => $sv['ma_sv'],
            'hoTen'       => $sv['ho_ten'],
            'ngaySinh'    => $sv['ngay_sinh'],
            'gioiTinh'    => $sv['gioi_tinh'],
            'noiSinh'     => $sv['noi_sinh'],
            'soDienThoai' => $sv['so_dien_thoai'],
            'lop'         => $sv['lop'],
            'nganh'       => $sv['nganh'],
            'balance'     => $sv['balance'],
            'lockTime'    => $sv['lock_time']
        ]);
    } else {
        echo json_encode(null);
    }
}

function sinhvien_updateInfo($pdo, $requestData) {
    $maSV     = $requestData['maSV'] ?? '';
    $hoTen    = $requestData['hoTen'] ?? '';
    $noiSinh  = $requestData['noiSinh'] ?? '';
    $ngaySinh = $requestData['ngaySinh'] ?? null;

    $stmt = $pdo->prepare("SELECT * FROM sinh_vien WHERE ma_sv = ?");
    $stmt->execute([$maSV]);
    if (!$stmt->fetch()) { echo "FAIL"; return; }

    $sql = "UPDATE sinh_vien SET ho_ten = ?, noi_sinh = ?";
    $params = [$hoTen, $noiSinh];
    if ($ngaySinh) {
        $sql .= ", ngay_sinh = ?";
        $params[] = $ngaySinh;
    }
    $sql .= " WHERE ma_sv = ?";
    $params[] = $maSV;

    $pdo->prepare($sql)->execute($params);
    echo "SUCCESS";
}

function sinhvien_changePassword($pdo, $requestData) {
    $maSV  = $requestData['maSV'] ?? '';
    $oldPw = $requestData['oldPassword'] ?? '';
    $newPw = $requestData['newPassword'] ?? '';

    $stmt = $pdo->prepare("SELECT * FROM sinh_vien WHERE ma_sv = ? AND so_dien_thoai = ?");
    $stmt->execute([$maSV, $oldPw]);
    if (!$stmt->fetch()) { echo "SAI_MAT_KHAU"; return; }

    $pdo->prepare("UPDATE sinh_vien SET so_dien_thoai = ? WHERE ma_sv = ?")->execute([$newPw, $maSV]);
    echo "SUCCESS";
}
?>


