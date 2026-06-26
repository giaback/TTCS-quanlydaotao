<?php
$servername = "127.0.0.1";
$username = "root";
$password = "";
$dbname = "quanlydaotao";

$conn_init = new mysqli($servername, $username, $password);

if ($conn_init->connect_error) {
    die(json_encode(["error" => "Kết nối MySQL thất bại: " . $conn_init->connect_error]));
}

$db_selected = $conn_init->select_db($dbname);

if (!$db_selected) {
    $sql_create_db = "CREATE DATABASE `$dbname` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
    if ($conn_init->query($sql_create_db) === TRUE) {
        $conn_init->select_db($dbname);

        $sql_file = __DIR__ . '/database.sql';
        if (file_exists($sql_file)) {
            $sql_content = file_get_contents($sql_file);
            if ($conn_init->multi_query($sql_content)) {
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

try {
    $pdo = new PDO("mysql:host=$servername;dbname=$dbname;charset=utf8mb4", $username, $password);
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    $pdo->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);
} catch(PDOException $e) {
    die(json_encode(["error" => "Kết nối CSDL bằng PDO thất bại: " . $e->getMessage()]));
}
?>
