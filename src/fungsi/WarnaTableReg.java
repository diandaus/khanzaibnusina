/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package fungsi;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author Owner
 */
public class WarnaTableReg extends DefaultTableCellRenderer {

    // Cache untuk menyimpan hasil pengecekan mobile JKN agar tidak query berulang
    private static Map<String, Boolean> mobileJKNCache = new HashMap<>();
    private static long lastCacheUpdate = 0;
    private static final long CACHE_DURATION = 30000; // 30 detik

    // Method untuk mengecek apakah pasien daftar via Mobile JKN
    private boolean hasMobileJKNData(String noRawat) {
        // Reset cache jika sudah expired
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCacheUpdate > CACHE_DURATION) {
            mobileJKNCache.clear();
            lastCacheUpdate = currentTime;
        }

        // Cek cache terlebih dahulu
        if (mobileJKNCache.containsKey(noRawat)) {
            return mobileJKNCache.get(noRawat);
        }

        boolean hasMobileJKN = false;
        try {
            Connection conn = koneksiDB.condb();
            PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) as jumlah FROM referensi_mobilejkn_bpjs WHERE no_rawat = ?"
            );
            ps.setString(1, noRawat);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                hasMobileJKN = rs.getInt("jumlah") > 0;
            }

            rs.close();
            ps.close();

            // Simpan hasil ke cache
            mobileJKNCache.put(noRawat, hasMobileJKN);

        } catch (Exception e) {
            System.out.println("Error checking Mobile JKN data: " + e);
        }
        return hasMobileJKN;
    }

    // Method untuk clear cache
    public static void clearCache() {
        mobileJKNCache.clear();
        lastCacheUpdate = 0;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Ambil no_rawat dari kolom ke-2 (index 2)
        String noRawat = table.getValueAt(row, 2).toString();

        if (row % 2 == 1){
            component.setBackground(new Color(255,244,244));
            component.setForeground(new Color(50,50,50));
        }else{
            component.setBackground(new Color(255,255,255));
            component.setForeground(new Color(50,50,50));
        }
        if(table.getValueAt(row,23).toString().equals("Sudah Bayar")){
            component.setBackground(new Color(50,50,50));
            component.setForeground(new Color(255,255,255));
        }
        // Ambil No SEP dari kolom ke-24 (index 24)
        String noSEP = "";
        if (table.getColumnCount() > 24) {
            Object sepValue = table.getValueAt(row, 24);
            noSEP = sepValue != null ? sepValue.toString().trim() : "";
        }
        // Jika sudah ada No SEP, buat seluruh baris berwarna hijau terang (prioritas tertinggi)
        if (!noSEP.isEmpty() && !noSEP.equals("-")) {
            component.setBackground(new Color(144, 238, 144)); // Light green
            component.setForeground(new Color(0, 100, 0)); // Dark green text
        }

        // Penanda khusus untuk kolom No.Rawat (kolom ke-2, index 2) jika pasien daftar via Mobile JKN
        if (column == 2 && hasMobileJKNData(noRawat)) {
            component.setBackground(new Color(255, 0, 0)); // Warna merah
            component.setForeground(new Color(255, 255, 255)); // Teks putih
        }

        return component;
    }

}
