<?php
$servername = "127.0.0.1";
$username = "root";
$password = "";
$dbname = "quanlydaotao";

// 1. Kết nối không dùng tên DB trước để kiểm tra DB tồn tại chưa
$conn_init = new mysqli($servername, $username, $password);

if ($conn_init->connect_error) {
    die(json_encode(["error" => "Kết nối MySQL thất bại: " . $conn_init->connect_error]));
}

// 2. Kiểm tra xem database `quanlydaotao` đã có chưa
$db_selected = $conn_init->select_db($dbname);

if (!$db_selected) {
    // Nếu chưa có, tự động tạo DB và Import file database.sql
    $sql_create_db = "CREATE DATABASE `$dbname` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
    if ($conn_init->query($sql_create_db) === TRUE) {
        $conn_init->select_db($dbname);
        
        $sql_file = __DIR__ . '/database.sql';
        if (file_exists($sql_file)) {
            $sql_content = file_get_contents($sql_file);
            if ($conn_init->multi_query($sql_content)) {
                // Đợi cho tất cả các câu lệnh SQL chạy xong
                do {
                    if ($res = $conn_init->store_result()) {
                        $res->free();
                    }
                } while ($conn_init->more_results() && $conn_init->next_result());
            }
        }
    }
}
$conn_init->close();

// 3. Tạo kết nối PDO chính thức dùng chung cho toàn hệ thống API
try {
    $pdo = new PDO("mysql:host=$servername;dbname=$dbname;charset=utf8mb4", $username, $password);
    // Bật chế độ ném lỗi Exception để dễ debug
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    // Trả kết quả truy vấn về dạng mảng (Array)
    $pdo->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);
} catch(PDOException $e) {
    die(json_encode(["error" => "Kết nối CSDL bằng PDO thất bại: " . $e->getMessage()]));
}
?>
