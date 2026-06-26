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
    $idHp   = $requestData['idHp'] ?? 0;
    $amount = $requestData['amount'] ?? 0;

    $stmt = $pdo->prepare("SELECT * FROM sinh_vien WHERE ma_sv = ?");
    $stmt->execute([$maSV]);
    $sv = $stmt->fetch();

    if ($sv['balance'] < $amount) {
        echo json_encode(["status" => "INSUFFICIENT_FUNDS"]);
        return;
    }

    $pdo->prepare("UPDATE sinh_vien SET balance = balance - ? WHERE ma_sv = ?")->execute([$amount, $maSV]);
    $pdo->prepare("UPDATE hoc_phi SET so_tien_da_nop = so_tien_da_nop + ? WHERE id_hp = ?")->execute([$amount, $idHp]);
    $pdo->prepare("UPDATE hoc_phi SET trang_thai = 'Đã hoàn thành' WHERE id_hp = ? AND so_tien_da_nop >= so_tien_phai_nop")->execute([$idHp]);

    echo json_encode(["status" => "SUCCESS"]);
}
?>

