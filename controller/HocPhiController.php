<?php

function hocphi_get($pdo, $maSV) {
    $stmt = $pdo->prepare("SELECT lock_time FROM sinh_vien WHERE ma_sv = ?");
    $stmt->execute([$maSV]);
    $sv = $stmt->fetch();
    if (!$sv || !$sv['lock_time'] || strtotime($sv['lock_time']) > time()) {
        echo json_encode(["status" => "UNLOCKED"]);
        return;
    }

    $stmt = $pdo->prepare("
        SELECT hp.*, sv.ho_ten, sv.lop, sv.nganh
        FROM hoc_phi hp
        JOIN sinh_vien sv ON hp.ma_sv = sv.ma_sv
        WHERE hp.ma_sv = ?
    ");
    $stmt->execute([$maSV]);
    $result = $stmt->fetchAll();
    $output = [];
    foreach ($result as $row) {
        $output[] = [
            'idHp'          => $row['id_hp'],
            'hocKy'         => $row['hoc_ky'],
            'soTienPhaiNop' => $row['so_tien_phai_nop'],
            'soTienDaNop'   => $row['so_tien_da_nop'],
            'trangThai'     => $row['trang_thai']
        ];
    }
    echo json_encode(!empty($output) ? $output[0] : null);
}

function hocphi_pay($pdo, $requestData) {
    $maSV   = $requestData['maSV'] ?? '';
    $amount = $requestData['amount'] ?? 0;

    $stmt = $pdo->prepare("SELECT * FROM sinh_vien WHERE ma_sv = ?");
    $stmt->execute([$maSV]);
    $sv = $stmt->fetch();

    if ($sv['balance'] < $amount) {
        echo json_encode(["message" => "Lỗi: Số dư tài khoản ảo không đủ!"]);
        return;
    }

    $pdo->prepare("UPDATE sinh_vien SET balance = balance - ? WHERE ma_sv = ?")->execute([$amount, $maSV]);
    $pdo->prepare("UPDATE hoc_phi SET so_tien_da_nop = so_tien_da_nop + ? WHERE ma_sv = ?")->execute([$amount, $maSV]);
    
    // Cập nhật trạng thái
    $pdo->prepare("UPDATE hoc_phi SET trang_thai = 'Đã nộp' WHERE ma_sv = ? AND so_tien_da_nop >= so_tien_phai_nop")->execute([$maSV]);
    $pdo->prepare("UPDATE hoc_phi SET trang_thai = 'Đã nộp một phần' WHERE ma_sv = ? AND so_tien_da_nop > 0 AND so_tien_da_nop < so_tien_phai_nop")->execute([$maSV]);

    echo json_encode(["message" => "Thanh toán thành công " . number_format($amount) . " VNĐ!"]);
}
