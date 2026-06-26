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
require_once '../controller/SinhVienController.php';
require_once '../controller/DangKyController.php';
require_once '../controller/HocPhiController.php';
require_once '../controller/LopHocPhanController.php';
require_once '../controller/KetQuaController.php';
require_once '../controller/LichThiController.php';
require_once '../controller/ThoiKhoaBieuController.php';

$path   = isset($_GET['path']) ? $_GET['path'] : '';
$method = $_SERVER['REQUEST_METHOD'];

$parts      = explode('/', trim($path, '/'));
$controller = $parts[0] ?? '';
$action     = $parts[1] ?? '';
$param      = $parts[2] ?? '';

$inputJSON   = file_get_contents('php://input');
$requestData = json_decode($inputJSON, TRUE);

if ($controller === 'sinhvien') {

    if ($method === 'POST' && $action === 'login') {
        sinhvien_login($pdo, $requestData);
    } elseif ($method === 'PUT' && $action === 'update-info') {
        sinhvien_updateInfo($pdo, $requestData);
    } elseif ($method === 'PUT' && $action === 'change-password') {
        sinhvien_changePassword($pdo, $requestData);
    } elseif ($method === 'GET' && $action !== '') {
        sinhvien_getById($pdo, $action);
    }

} elseif ($controller === 'dang-ky') {

    if ($method === 'GET' && $action === 'chi-tiet') {
        dangky_getChiTiet($pdo, $param);
    } elseif ($method === 'GET' && $action !== '') {
        dangky_getByMaSV($pdo, $action);
    } elseif ($method === 'POST') {
        dangky_save($pdo, $requestData);
    }

} elseif ($controller === 'hoc-phi') {

    if ($method === 'GET' && $action !== '') {
        hocphi_get($pdo, $action);
    } elseif ($method === 'POST' && $action === 'pay') {
        hocphi_pay($pdo, $requestData);
    }

} elseif ($controller === 'lop-hoc-phan') {

    if ($method === 'GET' && $action === 'all') {
        lophocphan_getAll($pdo);
    }

} elseif ($controller === 'ket-qua') {

    if ($method === 'GET' && $action !== '') {
        ketqua_getByMaSV($pdo, $action);
    }

} elseif ($controller === 'lich-thi') {

    if ($method === 'GET' && $action !== '') {
        lichthi_getByMaSV($pdo, $action);
    }

} elseif ($controller === 'thoi-khoa-bieu') {

    if ($method === 'GET' && $action !== '') {
        thoikhoabieu_getByMaSV($pdo, $action);
    }

} else {
    http_response_code(404);
    echo json_encode(["error" => "Route not found"]);
}
?>
