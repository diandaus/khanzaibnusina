-- Script untuk membuat tabel yang hilang: tt_berkasdigital, tt_status_koding, tt_status_digital_klaim
-- Database: ibnusina
-- Generated: 2025-11-12

USE ibnusina;

-- ========================================
-- Tabel 1: tt_berkasdigital
-- Menyimpan catatan file digital berkas klaim
-- ========================================
CREATE TABLE IF NOT EXISTS `tt_berkasdigital` (
  `no_rawat` VARCHAR(17) NOT NULL,
  `jenis_file` VARCHAR(50) NOT NULL,
  `nama_file` VARCHAR(255) DEFAULT NULL,
  `tanggal_upload` DATE DEFAULT NULL,
  `jam_upload` TIME DEFAULT NULL,
  PRIMARY KEY (`no_rawat`, `jenis_file`),
  INDEX `idx_no_rawat` (`no_rawat`),
  INDEX `idx_jenis_file` (`jenis_file`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========================================
-- Tabel 2: tt_status_koding
-- Menyimpan status koding untuk setiap rawatan
-- ========================================
CREATE TABLE IF NOT EXISTS `tt_status_koding` (
  `no_rawat` VARCHAR(17) NOT NULL,
  `status` VARCHAR(50) DEFAULT 'Belum Koding',
  `tgl_koding` DATE DEFAULT NULL,
  `jam_koding` TIME DEFAULT NULL,
  `kode_icd10` VARCHAR(10) DEFAULT NULL,
  `kode_icd9` VARCHAR(10) DEFAULT NULL,
  PRIMARY KEY (`no_rawat`),
  INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========================================
-- Tabel 3: tt_status_digital_klaim
-- Tracking lengkap status perjalanan berkas digital klaim
-- ========================================
CREATE TABLE IF NOT EXISTS `tt_status_digital_klaim` (
  `no_rawat` VARCHAR(17) NOT NULL,
  `status_verif` VARCHAR(50) DEFAULT 'Belum Verif',
  `tgl_verif` DATE DEFAULT NULL,
  `jam_verif` TIME DEFAULT NULL,
  `status_koding` VARCHAR(50) DEFAULT 'Belum Koding',
  `tgl_koding` DATE DEFAULT NULL,
  `jam_koding` TIME DEFAULT NULL,
  `status_download` VARCHAR(50) DEFAULT 'Belum Download',
  `tgl_download` DATE DEFAULT NULL,
  `jam_download` TIME DEFAULT NULL,
  `status_kirim` VARCHAR(50) DEFAULT 'Belum Kirim',
  `tgl_kirim` DATE DEFAULT NULL,
  `jam_kirim` TIME DEFAULT NULL,
  PRIMARY KEY (`no_rawat`),
  INDEX `idx_status_verif` (`status_verif`),
  INDEX `idx_status_koding` (`status_koding`),
  INDEX `idx_status_kirim` (`status_kirim`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========================================
-- Menambahkan Foreign Key Constraints (opsional, jika diperlukan)
-- Uncomment jika ingin menambahkan referential integrity
-- ========================================

-- ALTER TABLE `tt_berkasdigital`
--   ADD CONSTRAINT `fk_berkasdigital_no_rawat`
--   FOREIGN KEY (`no_rawat`) REFERENCES `reg_periksa` (`no_rawat`)
--   ON DELETE CASCADE ON UPDATE CASCADE;

-- ALTER TABLE `tt_status_koding`
--   ADD CONSTRAINT `fk_status_koding_no_rawat`
--   FOREIGN KEY (`no_rawat`) REFERENCES `reg_periksa` (`no_rawat`)
--   ON DELETE CASCADE ON UPDATE CASCADE;

-- ALTER TABLE `tt_status_digital_klaim`
--   ADD CONSTRAINT `fk_status_digital_klaim_no_rawat`
--   FOREIGN KEY (`no_rawat`) REFERENCES `reg_periksa` (`no_rawat`)
--   ON DELETE CASCADE ON UPDATE CASCADE;

-- ========================================
-- Tabel 4: kartu_reg_periksa
-- Menyimpan informasi hak kelas BPJS untuk setiap rawatan
-- ========================================
CREATE TABLE IF NOT EXISTS `kartu_reg_periksa` (
  `no_rawat` VARCHAR(17) NOT NULL,
  `hakkelas` VARCHAR(20) DEFAULT 'Kelas 3',
  `no_kartu` VARCHAR(25) DEFAULT NULL,
  `tgl_berlaku` DATE DEFAULT NULL,
  `tgl_akhir` DATE DEFAULT NULL,
  PRIMARY KEY (`no_rawat`),
  INDEX `idx_hakkelas` (`hakkelas`),
  INDEX `idx_no_kartu` (`no_kartu`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ========================================
-- Menambahkan Foreign Key Constraints untuk kartu_reg_periksa
-- Uncomment jika ingin menambahkan referential integrity
-- ========================================

-- ALTER TABLE `kartu_reg_periksa`
--   ADD CONSTRAINT `fk_kartu_reg_periksa_no_rawat`
--   FOREIGN KEY (`no_rawat`) REFERENCES `reg_periksa` (`no_rawat`)
--   ON DELETE CASCADE ON UPDATE CASCADE;

-- ========================================
-- Verifikasi tabel telah dibuat
-- ========================================
SHOW TABLES LIKE 'tt_%';
SHOW TABLES LIKE 'kartu_reg_periksa';
