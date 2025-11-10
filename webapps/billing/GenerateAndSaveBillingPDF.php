<?php
/**
 * Script untuk auto-generate PDF billing dan save ke temporary folder
 * Akan dipanggil oleh Java untuk fully automatic PDF generation
 */
include '../conf/conf.php';
include '../phpqrcode/qrlib.php';

// Validasi security
reportsqlinjection();
$usere      = trim(isset($_GET['usere']))?trim($_GET['usere']):NULL;
$passwordte = trim(isset($_GET['passwordte']))?trim($_GET['passwordte']):NULL;

if((USERHYBRIDWEB==$usere)&&(PASHYBRIDWEB==$passwordte)){
    $norawat        = validTeks4(str_replace("_"," ",$_GET['norawat']),20);
    $petugas        = validTeks4(str_replace("_"," ",$_GET['petugas']),20);
    $tanggal        = validTeks4(str_replace("_"," ",$_GET['tanggal']),20);

    // Ambil data registrasi pasien
    $kodecarabayar  = getOne("select kd_pj from reg_periksa where no_rawat='$norawat'");
    $carabayar      = getOne("select png_jawab from penjab where kd_pj='$kodecarabayar'");
    $statuslanjut   = getOne("select status_lanjut from reg_periksa where no_rawat='$norawat'");

    // Setup QR code directory
    $PNG_TEMP_DIR   = dirname(__FILE__).DIRECTORY_SEPARATOR.'temp'.DIRECTORY_SEPARATOR;
    if (!file_exists($PNG_TEMP_DIR)) mkdir($PNG_TEMP_DIR);

    // Query data billing dari temporary_bayar_ranap
    $_sql = "select temp1, temp2, temp3, temp4, temp5, temp6, temp7, temp8
             from temporary_bayar_ranap
             order by no asc";
    $hasil = bukaquery($_sql);

    if(mysqli_num_rows($hasil)!=0) {
        $setting = mysqli_fetch_array(bukaquery("select nama_instansi, alamat_instansi, kabupaten, propinsi, kontak, email, logo from setting"));

        // Tentukan lebar nota
        $lebarNota = getOne("select nota1ranap from set_nota");

        // Generate QR Code untuk petugas
        $qrFilename = '';
        if(getOne("select count(nama) from petugas where nip='$petugas'")>=1){
            $qrFilename = $PNG_TEMP_DIR.$petugas.'.png';
            $errorCorrectionLevel = 'L';
            $matrixPointSize = 4;
            QRcode::png("Dikeluarkan di ".$setting["nama_instansi"].", Kabupaten/Kota ".$setting["kabupaten"]."\nDitandatangani secara elektronik oleh ".getOne("select nama from petugas where nip='$petugas'")."\nID  ".getOne3("select ifnull(sha1(sidikjari.sidikjari),'".$petugas."') from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik='".$petugas."'",$petugas)."\n".$tanggal, $qrFilename, $errorCorrectionLevel, $matrixPointSize, 2);
        }else{
            $qrFilename = $PNG_TEMP_DIR.'admin.png';
            $errorCorrectionLevel = 'L';
            $matrixPointSize = 4;
            QRcode::png("Dikeluarkan di ".$setting["nama_instansi"].", Kabupaten/Kota ".$setting["kabupaten"]."\nDitandatangani secara elektronik oleh Admin Utama\nID ADMIN\n".$tanggal, $qrFilename, $errorCorrectionLevel, $matrixPointSize, 2);
        }

        // Generate HTML content
        ob_start();
        ?>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Billing - <?php echo $norawat; ?></title>
    <style>
        @page { margin: 10mm; }
        body {
            font-family: Tahoma, Arial, sans-serif;
            margin: 0;
            padding: 0;
            font-size: 11px;
        }
        table {
            border-collapse: collapse;
            width: 100%;
        }
        .header-table td {
            vertical-align: middle;
            padding: 5px;
        }
        .center-text {
            text-align: center;
        }
        hr {
            border: 0;
            border-top: 1px solid #000;
            margin: 5px 0;
        }
        .billing-row td {
            padding: 2px 0;
            font-size: 10px;
        }
        .footer-section {
            margin-top: 20px;
        }
    </style>
</head>
<body>
    <table width='100%'>
        <tr>
            <td colspan='7'>
                <table width='100%' class='header-table'>
                    <tr>
                        <td width='15%'>
                            <img width='50' height='50' src='data:image/jpeg;base64,<?php echo base64_encode($setting['logo']); ?>'/>
                        </td>
                        <td width='70%' class='center-text'>
                            <strong style='font-size:14px;'><?php echo $setting["nama_instansi"]; ?></strong><br>
                            <span style='font-size:10px;'>
                                <?php echo $setting["alamat_instansi"].", ".$setting["kabupaten"].", ".$setting["propinsi"]; ?><br/>
                                <?php echo $setting["kontak"].", E-mail : ".$setting["email"]; ?>
                            </span>
                        </td>
                        <td width='15%' style='font-size:11px;'><?php echo $carabayar; ?></td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td colspan='7'>
                <hr/>
                <div class='center-text'><strong>BILLING</strong></div>
                <hr/>
            </td>
        </tr>
        <?php
        $z=1;
        mysqli_data_seek($hasil, 0); // Reset pointer
        while($inapdrpasien = mysqli_fetch_array($hasil)) {
            if($z<=6){
                echo "<tr class='billing-row'>
                        <td width='20%'>".htmlspecialchars($inapdrpasien[0])."</td>
                        <td colspan='6'>".htmlspecialchars($inapdrpasien[1])."</td>
                      </tr>";
            }else if($z>6){
                if(empty($inapdrpasien[6])&&empty($inapdrpasien[0])){
                    echo "<tr class='billing-row'>
                            <td width='20%'>".htmlspecialchars($inapdrpasien[0])."</td>
                            <td colspan='6' align='right'>".htmlspecialchars($inapdrpasien[1])."</td>
                          </tr>";
                }else{
                    echo "<tr class='billing-row'>
                            <td width='20%'>".htmlspecialchars($inapdrpasien[0])."</td>
                            <td width='35%'>".htmlspecialchars($inapdrpasien[1])."</td>
                            <td width='3%'>".htmlspecialchars($inapdrpasien[2])."</td>
                            <td width='12%' align='right'>".htmlspecialchars($inapdrpasien[3])."</td>
                            <td width='8%' align='right'>".htmlspecialchars($inapdrpasien[4])."</td>
                            <td width='10%' align='right'>".htmlspecialchars($inapdrpasien[5])."</td>
                            <td width='12%' align='right'>".htmlspecialchars($inapdrpasien[6])."</td>
                          </tr>";
                }
            }
            $z++;
        }
        ?>
        <tr>
            <td colspan='7' class='footer-section'>
                <table width='100%'>
                    <tr>
                        <td width='40%'>&nbsp;</td>
                        <td width='20%'>&nbsp;</td>
                        <td width='40%' class='center-text'>
                            <?php echo getOne("select kabupaten from setting").", ".$tanggal; ?><br><br>
                            <strong>Kasir</strong><br><br>
                            <?php if(file_exists($qrFilename)): ?>
                                <img width='60' height='60' src='data:image/png;base64,<?php echo base64_encode(file_get_contents($qrFilename)); ?>'/>
                            <?php endif; ?><br>
                            <small>(<?php
                                if(getOne("select count(nama) from petugas where nip='$petugas'")>=1){
                                    echo getOne("select nama from petugas where nip='$petugas'");
                                }else{
                                    echo "Admin Utama";
                                }
                            ?>)</small>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td colspan='7' style='padding-top:10px; font-size:9px;'>
                <em>NB : Mohon maaf apabila ada tagihan yang belum tertagihkan dalam perincian ini akan ditagihkan kemudian, dan apabila berlebih akan dikembalikan.</em>
            </td>
        </tr>
    </table>
</body>
</html>
        <?php
        $html_content = ob_get_clean();

        // Save HTML to temp file
        $norawat_formatted = str_replace("/", "_", $norawat);
        $html_filename = "Billing_".$norawat_formatted.".html";
        $html_file_path = $PNG_TEMP_DIR.$html_filename;
        file_put_contents($html_file_path, $html_content);

        // Return sukses dengan path file HTML
        // Java akan mengambil file HTML ini dan convert ke PDF menggunakan library Java
        header('Content-Type: application/json');
        echo json_encode([
            'success' => true,
            'html_file' => 'billing/temp/'.$html_filename,
            'suggested_pdf_name' => 'Billing_'.$norawat_formatted.'.pdf',
            'no_rawat' => $norawat,
            'message' => 'HTML billing generated successfully'
        ]);

    } else {
        header('Content-Type: application/json');
        echo json_encode([
            'success' => false,
            'message' => 'Data billing masih kosong'
        ]);
    }
}else{
    header('Content-Type: application/json');
    echo json_encode([
        'success' => false,
        'message' => 'Unauthorized access'
    ]);
}
?>
