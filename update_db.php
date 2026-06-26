<?php
require 'config/database.php';
try {
    $pdo->query('ALTER TABLE sinh_vien ADD COLUMN lock_time DATETIME DEFAULT NULL;');
    echo "SUCCESS";
} catch(Exception $e) {
    echo "ERROR: " . $e->getMessage();
}
?>
