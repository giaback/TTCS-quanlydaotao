CREATE DATABASE IF NOT EXISTS `quanlydaotao` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `quanlydaotao`;

CREATE TABLE IF NOT EXISTS `sinh_vien` (
    `ma_sv` VARCHAR(20) NOT NULL PRIMARY KEY,
    `ho_ten` VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    `ngay_sinh` DATE,
    `gioi_tinh` VARCHAR(10),
    `noi_sinh` VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    `so_dien_thoai` VARCHAR(15),
    `lop` VARCHAR(255),
    `nganh` VARCHAR(255),
    `balance` DOUBLE DEFAULT 10000000.0,
    `lock_time` DATETIME DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `mon_hoc` (
    `ma_mon` VARCHAR(255) NOT NULL PRIMARY KEY,
    `ten_mon` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    `so_tin_chi` INT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `lop_hoc_phan` (
    `id_lop` INT AUTO_INCREMENT PRIMARY KEY,
    `ma_mon` VARCHAR(255),
    `nhom` INT,
    `giang_vien` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    FOREIGN KEY (`ma_mon`) REFERENCES `mon_hoc`(`ma_mon`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `dang_ky` (
    `id_dang_ky` INT AUTO_INCREMENT PRIMARY KEY,
    `ma_sv` VARCHAR(20),
    `id_lop` INT,
    FOREIGN KEY (`ma_sv`) REFERENCES `sinh_vien`(`ma_sv`) ON DELETE CASCADE,
    FOREIGN KEY (`id_lop`) REFERENCES `lop_hoc_phan`(`id_lop`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `thoi_khoa_bieu` (
    `id_tkb` INT AUTO_INCREMENT PRIMARY KEY,
    `id_lop` INT,
    `thu` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    `tiet_bat_dau` INT,
    `so_tiet` INT,
    `thoi_gian_hoc` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    FOREIGN KEY (`id_lop`) REFERENCES `lop_hoc_phan`(`id_lop`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `lich_thi` (
    `id_lich_thi` INT AUTO_INCREMENT PRIMARY KEY,
    `ma_mon` VARCHAR(255),
    `ngay_thi` VARCHAR(255),
    `ca_thi` VARCHAR(255),
    `phong_thi` VARCHAR(255),
    FOREIGN KEY (`ma_mon`) REFERENCES `mon_hoc`(`ma_mon`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `ket_qua_hoc_tap` (
    `id_kq` INT AUTO_INCREMENT PRIMARY KEY,
    `ma_sv` VARCHAR(20),
    `ma_mon` VARCHAR(255),
    `diem_thi` DOUBLE,
    `diem_tk_10` DOUBLE,
    `diem_tk_4` DOUBLE,
    `diem_tk_chu` VARCHAR(10),
    `hoc_ky` VARCHAR(255),
    FOREIGN KEY (`ma_sv`) REFERENCES `sinh_vien`(`ma_sv`) ON DELETE CASCADE,
    FOREIGN KEY (`ma_mon`) REFERENCES `mon_hoc`(`ma_mon`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `hoc_phi` (
    `id_hp` INT AUTO_INCREMENT PRIMARY KEY,
    `ma_sv` VARCHAR(20),
    `hoc_ky` VARCHAR(255),
    `so_tien_phai_nop` BIGINT,
    `trang_thai` VARCHAR(50) DEFAULT 'Chưa nộp',
    `so_tien_da_nop` BIGINT DEFAULT 0,
    FOREIGN KEY (`ma_sv`) REFERENCES `sinh_vien`(`ma_sv`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Thêm một user admin giả lập
INSERT IGNORE INTO `sinh_vien` (`ma_sv`, `ho_ten`, `ngay_sinh`, `gioi_tinh`, `noi_sinh`, `so_dien_thoai`, `lop`, `nganh`, `balance`) VALUES
('admin', 'Administrator', '2000-01-01', 'Nam', 'Hà Nội', '0123456789', 'Admin', 'Admin', 10000000.0);
