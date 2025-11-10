<?php
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
    $PNG_WEB_DIR    = 'temp/';
    if (!file_exists($PNG_TEMP_DIR)) mkdir($PNG_TEMP_DIR);

    // Query data billing dari temporary_bayar_ranap
    $_sql = "select temp1, temp2, temp3, temp4, temp5, temp6, temp7, temp8, temp9, temp10, temp11, temp12, temp13, temp14
             from temporary_bayar_ranap
             order by no asc";
    $hasil = bukaquery($_sql);

    if(mysqli_num_rows($hasil)!=0) {
        $setting = mysqli_fetch_array(bukaquery("select nama_instansi, alamat_instansi, kabupaten, propinsi, kontak, email, logo from setting"));

        // Tentukan lebar nota
        $lebarNota = getOne("select nota1ranap from set_nota");

        // Generate HTML content
        ob_start();
        ?>
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <style>
                body { font-family: Tahoma, Arial, sans-serif; margin: 0; padding: 20px; }
                table { border-collapse: collapse; }
                .isi12 { font-size: 11px; }
                hr { border: 0; border-top: 1px solid #000; }
            </style>
        </head>
        <body>
            <table width='<?php echo $lebarNota; ?>' bgcolor='#ffffff' align='left' border='0' cellspacing='0' cellpadding='0'>
                <tr class='isi12'>
                    <td colspan='7'>
                        <table width='100%' bgcolor='#ffffff' align='left' border='0' cellspacing='0' cellpadding='0'>
                            <tr>
                                <td width='20%'>
                                    <img width='45' height='45' src='data:image/jpeg;base64,<?php echo base64_encode($setting['logo']); ?>'/>
                                </td>
                                <td style='text-align:center;'>
                                    <font size='3' face='Tahoma'><?php echo $setting["nama_instansi"]; ?></font><br>
                                    <font size='1' face='Tahoma'>
                                        <?php echo $setting["alamat_instansi"].", ".$setting["kabupaten"].", ".$setting["propinsi"]; ?><br/>
                                        <?php echo $setting["kontak"].", E-mail : ".$setting["email"]; ?>
                                    </font>
                                </td>
                                <td width='20%'><font size='2' face='Tahoma'><?php echo $carabayar; ?></font></td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td colspan='7'>
                        <hr/>
                        <center><font size='1' face='Tahoma'>BILLING</font></center>
                    </td>
                </tr>
                <?php
                $z=1;
                while($inapdrpasien = mysqli_fetch_array($hasil)) {
                    if($z<=6){
                        echo "<tr class='isi12'>
                                <td width='18%'><font size='1' face='Tahoma'>".str_replace("  ","&nbsp;&nbsp;",$inapdrpasien[0])."</font></td>
                                <td width='40%' colspan='6'><font size='1' face='Tahoma'>".$inapdrpasien[1]."</font></td>
                              </tr>";
                    }else if($z>6){
                        if(empty($inapdrpasien[6])&&empty($inapdrpasien[0])){
                            echo "<tr class='isi12'>
                                    <td width='18%'><font size='1' face='Tahoma'>".str_replace("   ","&nbsp;&nbsp;",$inapdrpasien[0])."</font></td>
                                    <td width='40%' colspan='6' align='right'><font size='1' face='Tahoma'>".$inapdrpasien[1]."</font></td>
                                  </tr>";
                        }else{
                            echo "<tr class='isi12'>
                                    <td width='18%'><font size='1' face='Tahoma'>".str_replace("   ","&nbsp;&nbsp;",$inapdrpasien[0])."</font></td>
                                    <td width='40%'><font size='1' face='Tahoma'>".str_replace("  ","&nbsp;&nbsp;",$inapdrpasien[1])."</font></td>
                                    <td width='2%'><font size='1' face='Tahoma'>".$inapdrpasien[2]."</font></td>
                                    <td width='10%' align='right'><font size='1' face='Tahoma'>".$inapdrpasien[3]."</font></td>
                                    <td width='5%' align='right'><font size='1' face='Tahoma'>".$inapdrpasien[4]."</font></td>
                                    <td width='10%' align='right'><font size='1' face='Tahoma'>".$inapdrpasien[5]."</font></td>
                                    <td width='15%' align='right'><font size='1' face='Tahoma'>".$inapdrpasien[6]."</font></td>
                                  </tr>";
                        }
                    }
                    $z++;
                }
                ?>
                <tr class='isi12'>
                    <td colspan='7'>
                        <table width='100%' bgcolor='#ffffff' align='left' border='0' cellspacing='0' cellpadding='0'>
                            <tr class='isi12'>
                                <td width='40%' align='center'><font size='1' face='Tahoma'>&nbsp;</font></td>
                                <td width='20%' align='center'><font size='1' face='Tahoma'>&nbsp;</font></td>
                                <td width='40%' align='center'><font size='1' face='Tahoma'>&nbsp;</font></td>
                            </tr>
                            <tr class='isi12'>
                                <td width='40%' align='center'><font size='1' face='Tahoma'><br></font></td>
                                <td width='20%' align='center'><font size='1' face='Tahoma'>&nbsp;</font></td>
                                <td width='40%' align='center'><font size='1' face='Tahoma'><?php echo getOne("select kabupaten from setting").", ".$tanggal; ?></font></td>
                            </tr>
                            <tr class='isi12'>
                                <td width='40%' align='center'><font size='1' face='Tahoma'></font></td>
                                <td width='20%' align='center'><font size='1' face='Tahoma'>&nbsp;</font></td>
                                <td width='40%' align='center'><font size='1' face='Tahoma'>Kasir</font></td>
                            </tr>
                            <tr class='isi12'>
                                <td width='40%' align='center'><font size='1' face='Tahoma'></font></td>
                                <td width='20%' align='center'><font size='1' face='Tahoma'>&nbsp;</font></td>
                                <td width='40%' align='center'><font size='1' face='Tahoma'>
                                    <?php
                                    if(getOne("select count(nama) from petugas where nip='$petugas'")>=1){
                                        $filename = $PNG_TEMP_DIR.$petugas.'.png';
                                        $errorCorrectionLevel = 'L';
                                        $matrixPointSize = 4;
                                        QRcode::png("Dikeluarkan di ".$setting["nama_instansi"].", Kabupaten/Kota ".$setting["kabupaten"]."\nDitandatangani secara elektronik oleh ".getOne("select nama from petugas where nip='$petugas'")."\nID  ".getOne3("select ifnull(sha1(sidikjari.sidikjari),'".$petugas."') from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik='".$petugas."'",$petugas)."\n".$tanggal, $filename, $errorCorrectionLevel, $matrixPointSize, 2);
                                        echo "<img width='50' height='50' src='".$PNG_WEB_DIR.basename($filename)."'/><br>( ".getOne("select nama from petugas where nip='$petugas'")." )";
                                    }else{
                                        $filename = $PNG_TEMP_DIR.$petugas.'.png';
                                        $errorCorrectionLevel = 'L';
                                        $matrixPointSize = 4;
                                        QRcode::png("Dikeluarkan di ".$setting["nama_instansi"].", Kabupaten/Kota ".$setting["kabupaten"]."\nDitandatangani secara elektronik oleh Admin Utama\nID ADMIN\n".$tanggal, $filename, $errorCorrectionLevel, $matrixPointSize, 2);
                                        echo "<img width='45' height='45' src='".$PNG_WEB_DIR.basename($filename)."'/><br>( Admin Utama )";
                                    }
                                    ?>
                                </font></td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr class='isi12'>
                    <td colspan='7'>&nbsp;</td>
                </tr>
                <tr class='isi12'>
                    <td colspan='7'><font size='1' face='Tahoma'>NB : Mohon maaf apabila ada tagihan yang belum tertagihkan dalam perincian ini akan ditagihkan kemudian, dan apabila berlebih akan dikembalikan.</font></td>
                </tr>
            </table>
        </body>
        </html>
        <?php
        $html_content = ob_get_clean();

        // Save HTML as temporary file (akan diconvert ke PDF oleh Java menggunakan library)
        // Atau bisa menggunakan wkhtmltopdf jika ter-install di server

        // Format nama file
        $norawat_formatted = str_replace("/", "_", $norawat);
        $pdf_filename = "Billing_".$norawat_formatted.".html";
        $temp_file_path = $PNG_TEMP_DIR.$pdf_filename;

        // Save HTML to temp
        file_put_contents($temp_file_path, $html_content);

        // Return JSON response dengan path file
        header('Content-Type: application/json');
        echo json_encode([
            'success' => true,
            'file_path' => 'billing/temp/'.$pdf_filename,
            'file_name' => $pdf_filename,
            'no_rawat' => $norawat
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
