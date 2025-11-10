<?php
    include '../conf/conf.php';
    include '../phpqrcode/qrlib.php';

    $petugas = validTeks(str_replace("_"," ",$_GET['petugas']));
    $tanggal = validTeks(str_replace("_"," ",$_GET['tanggal']));

    if(isset($petugas)){
        $PNG_TEMP_DIR   = dirname(__FILE__).DIRECTORY_SEPARATOR.'temp'.DIRECTORY_SEPARATOR;
        $PNG_WEB_DIR    = 'temp/';
        if (!file_exists($PNG_TEMP_DIR)) mkdir($PNG_TEMP_DIR);
        $filename              = $PNG_TEMP_DIR.$petugas.'.png';
        $errorCorrectionLevel  = 'L';
        $matrixPointSize       = 4;
        $setting               = mysqli_fetch_array(bukaquery("select nama_instansi,kabupaten from setting"));

        if(getOne("select count(petugas.nama) from petugas where petugas.nip='$petugas'")>=1){
            QRcode::png("Dikeluarkan di ".$setting["nama_instansi"].", Kabupaten/Kota ".$setting["kabupaten"]."\nDitandatangani secara elektronik oleh ".getOne("select petugas.nama from petugas where petugas.nip='$petugas'")."\nID  ".getOne3("select ifnull(sha1(sidikjari.sidikjari),'".$petugas."') from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik='".$petugas."'",$petugas)."\n".$tanggal, $filename, $errorCorrectionLevel, $matrixPointSize, 2);
        }else{
            QRcode::png("Dikeluarkan di ".$setting["nama_instansi"].", Kabupaten/Kota ".$setting["kabupaten"]."\nDitandatangani secara elektronik oleh Admin Utama\nID ADMIN\n".$tanggal, $filename, $errorCorrectionLevel, $matrixPointSize, 2);
        }
    }
?>
