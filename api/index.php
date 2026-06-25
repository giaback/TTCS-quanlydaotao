<?php
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type");
header("Content-Type: application/json; charset=UTF-8");

date_default_timezone_set('Asia/Ho_Chi_Minh');

if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit();
}

require_once '../config/database.php';

$path = isset($_GET['path']) ? $_GET['path'] : '';
$method = $_SERVER['REQUEST_METHOD'];

// Parse URL parts
$parts = explode('/', trim($path, '/'));
$controller = $parts[0] ?? '';
$action = $parts[1] ?? '';
$param = $parts[2] ?? '';

// Lấy body request
$inputJSON = file_get_contents('php://input');
$requestData = json_decode($inputJSON, TRUE);

// --- ROUTER CHÍNH ---

if ($controller === 'sinhvien') {
    if ($method === 'POST' && $action === 'login') {
        $username = $requestData['username'] ?? '';
        $password = $requestData['password'] ?? '';
        
        $stmt = $pdo->prepare("SELECT * FROM sinh_vien WHERE ma_sv = ? AND so_dien_thoai = ?");
        $stmt->execute([$username, $password]);
        $sv = $stmt->fetch();
        
        if ($sv) {
            echo "SUCCESS";
        } else {
            echo "FAIL";
        }
        exit();
    }
    
    if ($method === 'PUT' && $action === 'update-info') {
        $maSV = $requestData['maSV'] ?? '';
        $hoTen = $requestData['hoTen'] ?? '';
        $noiSinh = $requestData['noiSinh'] ?? '';
        $ngaySinh = $requestData['ngaySinh'] ?? null;
        
        $stmt = $pdo->prepare("SELECT * FROM sinh_vien WHERE ma_sv = ?");
        $stmt->execute([$maSV]);
        if (!$stmt->fetch()) {
            echo "FAIL"; exit();
        }
        
        $sql = "UPDATE sinh_vien SET ho_ten = ?, noi_sinh = ?";
        $params = [$hoTen, $noiSinh];
        if ($ngaySinh) {
            $sql .= ", ngay_sinh = ?";
            $params[] = $ngaySinh;
        }
        $sql .= " WHERE ma_sv = ?";
        $params[] = $maSV;
        
        $stmt = $pdo->prepare($sql);
        $stmt->execute($params);
        echo "SUCCESS";
        exit();
    }

    if ($method === 'PUT' && $action === 'change-password') {
        $maSV = $requestData['maSV'] ?? '';
        $oldPw = $requestData['oldPassword'] ?? '';
        $newPw = $requestData['newPassword'] ?? '';
        
        $stmt = $pdo->prepare("SELECT * FROM sinh_vien WHERE ma_sv = ? AND so_dien_thoai = ?");
        $stmt->execute([$maSV, $oldPw]);
        if (!$stmt->fetch()) {
            echo "SAI_MAT_KHAU"; exit();
        }
        
        $stmt = $pdo->prepare("UPDATE sinh_vien SET so_dien_thoai = ? WHERE ma_sv = ?");
        $stmt->execute([$newPw, $maSV]);
        echo "SUCCESS";
        exit();
    }
    
    // GET /api/sinhvien/{maSV}
    if ($method === 'GET' && $action !== '') {
        $maSV = $action; // vì link là api/sinhvien/B123
        $stmt = $pdo->prepare("SELECT * FROM sinh_vien WHERE ma_sv = ?");
        $stmt->execute([$maSV]);
        $sv = $stmt->fetch();
        if ($sv) {
            // map database fields to Java object fields format expected by JS
            echo json_encode([
                'maSV' => $sv['ma_sv'],
                'hoTen' => $sv['ho_ten'],
                'ngaySinh' => $sv['ngay_sinh'],
                'gioiTinh' => $sv['gioi_tinh'],
                'noiSinh' => $sv['noi_sinh'],
                'soDienThoai' => $sv['so_dien_thoai'],
                'lop' => $sv['lop'],
                'nganh' => $sv['nganh'],
                'balance' => $sv['balance'],
                'lockTime' => $sv['lock_time']
            ]);
        } else {
            echo json_encode(null);
        }
        exit();
    }
}

elseif ($controller === 'hoc-phi') {
    if ($method === 'GET' && $action !== '') {
        $maSV = $action;
        $stmt = $pdo->prepare("SELECT lock_time FROM sinh_vien WHERE ma_sv = ?");
        $stmt->execute([$maSV]);
        $sv = $stmt->fetch();
        if (!$sv || !$sv['lock_time'] || strtotime($sv['lock_time']) > time()) {
            echo json_encode(["status" => "UNLOCKED"]);
            exit();
        }

        $stmt = $pdo->prepare("SELECT hp.*, sv.ho_ten, sv.lop, sv.nganh FROM hoc_phi hp JOIN sinh_vien sv ON hp.ma_sv = sv.ma_sv WHERE hp.ma_sv = ?");
        $stmt->execute([$maSV]);
        $result = $stmt->fetchAll();
        $output = [];
        foreach ($result as $row) {
            $output[] = [
                'idHp' => $row['id_hp'],
                'hocKy' => $row['hoc_ky'],
                'soTienPhaiNop' => $row['so_tien_phai_nop'],
                'soTienDaNop' => $row['so_tien_da_nop'],
                'trangThai' => $row['trang_thai']
            ];
        }
        echo json_encode(!empty($output) ? $output[0] : null);
        exit();
    }
    
    if ($method === 'POST' && $action === 'pay') {
        $maSV = $requestData['maSV'] ?? '';
        $idHp = $requestData['idHp'] ?? 0;
        $amount = $requestData['amount'] ?? 0;
        
        // Chức năng thanh toán cơ bản
        $stmt = $pdo->prepare("SELECT * FROM sinh_vien WHERE ma_sv = ?");
        $stmt->execute([$maSV]);
        $sv = $stmt->fetch();
        
        if ($sv['balance'] < $amount) {
            echo json_encode(["status" => "INSUFFICIENT_FUNDS"]);
            exit();
        }
        
        $pdo->prepare("UPDATE sinh_vien SET balance = balance - ? WHERE ma_sv = ?")->execute([$amount, $maSV]);
        $pdo->prepare("UPDATE hoc_phi SET so_tien_da_nop = so_tien_da_nop + ? WHERE id_hp = ?")->execute([$amount, $idHp]);
        $pdo->prepare("UPDATE hoc_phi SET trang_thai = 'Đã hoàn thành' WHERE id_hp = ? AND so_tien_da_nop >= so_tien_phai_nop")->execute([$idHp]);
        
        echo json_encode(["status" => "SUCCESS"]);
        exit();
    }
}

elseif ($controller === 'dang-ky') {
    if ($method === 'GET') {
        if ($action === 'chi-tiet') {
            $maSV = $param; // api/dang-ky/chi-tiet/B123
            $stmt = $pdo->prepare("
                SELECT dk.id_dang_ky as idDangKy, l.nhom, l.giang_vien as giangVien, m.ma_mon as maMon, m.ten_mon as tenMon, m.so_tin_chi as soTinChi, (m.so_tin_chi * 500000) as thanhTien
                FROM dang_ky dk
                JOIN lop_hoc_phan l ON dk.id_lop = l.id_lop
                JOIN mon_hoc m ON l.ma_mon = m.ma_mon
                WHERE dk.ma_sv = ?
            ");
            $stmt->execute([$maSV]);
            echo json_encode($stmt->fetchAll());
            exit();
        } else {
            $maSV = $action; // api/dang-ky/B123
            $stmt = $pdo->prepare("SELECT id_lop FROM dang_ky WHERE ma_sv = ?");
            $stmt->execute([$maSV]);
            $list = $stmt->fetchAll(PDO::FETCH_COLUMN);
            echo json_encode($list);
            exit();
        }
    }
    
    if ($method === 'POST') {
        $maSV = $requestData['maSV'] ?? '';
        $idLops = $requestData['idLops'] ?? [];
        $timeoutMinutes = $requestData['timeoutMinutes'] ?? null;
        $isReset = $requestData['isReset'] ?? false;

        if ($isReset) {
            $pdo->prepare("UPDATE sinh_vien SET lock_time = NULL WHERE ma_sv = ?")->execute([$maSV]);
            echo "SUCCESS";
            exit();
        }

        if (empty($idLops) && isset($requestData['idLop'])) {
            $idLops = [$requestData['idLop']];
        }
        
        $stmt = $pdo->prepare("SELECT lock_time FROM sinh_vien WHERE ma_sv = ?");
        $stmt->execute([$maSV]);
        $sv = $stmt->fetch();
        if ($sv && $sv['lock_time']) {
            if (strtotime($sv['lock_time']) <= time()) {
                echo "LOCKED";
                exit();
            }
        }
        
        if ($timeoutMinutes !== null) {
            $lockTime = date('Y-m-d H:i:s', strtotime("+$timeoutMinutes minutes"));
            $pdo->prepare("UPDATE sinh_vien SET lock_time = ? WHERE ma_sv = ?")->execute([$lockTime, $maSV]);
        }
        
        // 1. Xóa các môn cũ không nằm trong danh sách mới
        if (!empty($idLops)) {
            $placeholders = implode(',', array_fill(0, count($idLops), '?'));
            $stmt = $pdo->prepare("DELETE FROM dang_ky WHERE ma_sv = ? AND id_lop NOT IN ($placeholders)");
            $params = array_merge([$maSV], $idLops);
            $stmt->execute($params);
        } else {
            $pdo->prepare("DELETE FROM dang_ky WHERE ma_sv = ?")->execute([$maSV]);
        }
        
        // 2. Thêm các môn mới
        foreach ($idLops as $idLop) {
            $stmt = $pdo->prepare("SELECT * FROM dang_ky WHERE ma_sv = ? AND id_lop = ?");
            $stmt->execute([$maSV, $idLop]);
            if (!$stmt->fetch()) {
                $pdo->prepare("INSERT INTO dang_ky (ma_sv, id_lop) VALUES (?, ?)")->execute([$maSV, $idLop]);
            }
        }
        
        // 3. Tính lại tổng học phí
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
        
        // 4. Cập nhật hoặc tạo mới bảng hoc_phi
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
        
        echo "SUCCESS";
        exit();
    }
}

elseif ($controller === 'lop-hoc-phan') {
    if ($method === 'GET' && $action === 'all') {
        $stmt = $pdo->prepare("
            SELECT l.id_lop as idLop, l.nhom, l.giang_vien as giangVien, m.ma_mon as maMon, m.ten_mon as tenMon, m.so_tin_chi as soTinChi
            FROM lop_hoc_phan l
            JOIN mon_hoc m ON l.ma_mon = m.ma_mon
        ");
        $stmt->execute();
        echo json_encode($stmt->fetchAll());
        exit();
    }
}

elseif ($controller === 'ket-qua') {
    if ($method === 'GET' && $action !== '') {
        $maSV = $action;
        $stmt = $pdo->prepare("
            SELECT kq.diem_thi as diemThi, kq.diem_tk_10 as diemTk10, kq.diem_tk_4 as diemTk4, kq.diem_tk_chu as diemTkChu, kq.hoc_ky as hocKy, m.ma_mon as maMon, m.ten_mon as tenMon, m.so_tin_chi as soTinChi
            FROM ket_qua_hoc_tap kq
            JOIN mon_hoc m ON kq.ma_mon = m.ma_mon
            WHERE kq.ma_sv = ?
        ");
        $stmt->execute([$maSV]);
        echo json_encode($stmt->fetchAll());
        exit();
    }
}

elseif ($controller === 'lich-thi') {
    if ($method === 'GET' && $action !== '') {
        $maSV = $action;
        $stmt = $pdo->prepare("SELECT lock_time FROM sinh_vien WHERE ma_sv = ?");
        $stmt->execute([$maSV]);
        $sv = $stmt->fetch();
        if (!$sv || !$sv['lock_time'] || strtotime($sv['lock_time']) > time()) {
            echo json_encode(["status" => "UNLOCKED"]);
            exit();
        }

        $stmt = $pdo->prepare("
            SELECT lt.ngay_thi as ngayThi, lt.ca_thi as caThi, lt.phong_thi as phongThi, m.ma_mon as maMon, m.ten_mon as tenMon
            FROM lich_thi lt
            JOIN mon_hoc m ON lt.ma_mon = m.ma_mon
            JOIN lop_hoc_phan l ON l.ma_mon = m.ma_mon
            JOIN dang_ky dk ON dk.id_lop = l.id_lop
            WHERE dk.ma_sv = ?
            GROUP BY lt.id_lich_thi
        ");
        $stmt->execute([$maSV]);
        echo json_encode($stmt->fetchAll());
        exit();
    }
}

elseif ($controller === 'thoi-khoa-bieu') {
    if ($method === 'GET' && $action !== '') {
        $maSV = $action;
        $stmt = $pdo->prepare("SELECT lock_time FROM sinh_vien WHERE ma_sv = ?");
        $stmt->execute([$maSV]);
        $sv = $stmt->fetch();
        if (!$sv || !$sv['lock_time'] || strtotime($sv['lock_time']) > time()) {
            echo json_encode(["status" => "UNLOCKED"]);
            exit();
        }

        $stmt = $pdo->prepare("
            SELECT tkb.thu, tkb.tiet_bat_dau as tietBatDau, tkb.so_tiet as soTiet, tkb.thoi_gian_hoc as thoiGianHoc, 
                   l.nhom, l.giang_vien as giangVien, m.ma_mon as maMon, m.ten_mon as tenMon
            FROM thoi_khoa_bieu tkb
            JOIN lop_hoc_phan l ON tkb.id_lop = l.id_lop
            JOIN dang_ky dk ON dk.id_lop = l.id_lop
            JOIN mon_hoc m ON l.ma_mon = m.ma_mon
            WHERE dk.ma_sv = ?
        ");
        $stmt->execute([$maSV]);
        echo json_encode($stmt->fetchAll());
        exit();
    }
}

// 404 Route Not Found
http_response_code(404);
echo json_encode(["error" => "Route not found"]);
?>
