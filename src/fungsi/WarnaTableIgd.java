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
public class WarnaTableIgd extends DefaultTableCellRenderer {
    
    // Cache untuk menyimpan hasil pengecekan triase agar tidak query berulang
    private static Map<String, Boolean> triaseCache = new HashMap<>();
    private static Map<String, Boolean> medisCache = new HashMap<>();
    private static long lastCacheUpdate = 0;
    private static final long CACHE_DURATION = 30000; // 30 detik
    
    // Method untuk mengecek apakah pasien sudah memiliki data triase
    private boolean hasTriaseData(String noRawat) {
        // Reset cache jika sudah expired
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCacheUpdate > CACHE_DURATION) {
            triaseCache.clear();
            medisCache.clear();
            lastCacheUpdate = currentTime;
        }
        
        // Cek cache terlebih dahulu
        if (triaseCache.containsKey(noRawat)) {
            return triaseCache.get(noRawat);
        }
        
        boolean hasTriase = false;
        try {
            Connection conn = koneksiDB.condb();
            PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) as jumlah FROM data_triase_igd WHERE no_rawat = ?"
            );
            ps.setString(1, noRawat);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                hasTriase = rs.getInt("jumlah") > 0;
            }
            
            rs.close();
            ps.close();
            
            // Simpan hasil ke cache
            triaseCache.put(noRawat, hasTriase);
            
        } catch (Exception e) {
            System.out.println("Error checking triase data: " + e);
        }
        return hasTriase;
    }
    
    // Method untuk mengecek apakah pasien sudah memiliki data penilaian medis IGD
    private boolean hasMedisData(String noRawat) {
        // Reset cache jika sudah expired
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCacheUpdate > CACHE_DURATION) {
            triaseCache.clear();
            medisCache.clear();
            lastCacheUpdate = currentTime;
        }
        
        // Cek cache terlebih dahulu
        if (medisCache.containsKey(noRawat)) {
            return medisCache.get(noRawat);
        }
        
        boolean hasMedis = false;
        try {
            Connection conn = koneksiDB.condb();
            PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) as jumlah FROM penilaian_medis_igd WHERE no_rawat = ?"
            );
            ps.setString(1, noRawat);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                hasMedis = rs.getInt("jumlah") > 0;
            }
            
            rs.close();
            ps.close();
            
            // Simpan hasil ke cache
            medisCache.put(noRawat, hasMedis);
            
        } catch (Exception e) {
            System.out.println("Error checking medis data: " + e);
        }
        return hasMedis;
    }
    
    // Method untuk clear cache (bisa dipanggil ketika ada update data)
    public static void clearAllCache() {
        triaseCache.clear();
        medisCache.clear();
        lastCacheUpdate = 0;
    }
    
    // Method untuk clear cache triase saja (backward compatibility)
    public static void clearTriaseCache() {
        triaseCache.clear();
        lastCacheUpdate = 0;
    }
    
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        // Ambil no_rawat dari kolom ke-2 (index 2)
        String noRawat = table.getValueAt(row, 2).toString();
        
        // Set warna default berdasarkan baris
        if (row % 2 == 1){
            component.setBackground(new Color(255,244,244));
            component.setForeground(new Color(50,50,50));
        }else{
            component.setBackground(new Color(255,255,255));
            component.setForeground(new Color(50,50,50));
        } 
        
        // Penanda khusus untuk kolom No.Rawat (kolom ke-2, index 2) jika sudah ada data triase
        if (column == 2 && hasTriaseData(noRawat)) {
            component.setBackground(new Color(255, 0, 0)); // Warna merah
            component.setForeground(new Color(255, 255, 255)); // Teks putih
        }
        
        // Penanda khusus untuk kolom Dokter Dituju (kolom ke-6, index 6) jika sudah ada data penilaian medis IGD
        if (column == 6 && hasMedisData(noRawat)) {
            component.setBackground(new Color(0, 128, 0)); // Warna hijau
            component.setForeground(new Color(255, 255, 255)); // Teks putih
        }
        
        // Kondisi warna berdasarkan status rawat
        if(table.getValueAt(row,18).toString().equals("Sudah")){
            component.setBackground(new Color(255,182,193));
            component.setForeground(new Color(50,50,50));
        }else if(table.getValueAt(row,18).toString().equals("Batal")){
            component.setBackground(new Color(159,197,232));
            component.setForeground(new Color(50,50,50));
        }else if(table.getValueAt(row,18).toString().equals("Dirujuk")||table.getValueAt(row,10).toString().equals("Meninggal")||table.getValueAt(row,10).toString().equals("Pulang Paksa")){
            component.setBackground(new Color(152,152,156));
            component.setForeground(new Color(245,245,255));
        }else if(table.getValueAt(row,18).toString().equals("Dirawat")){
            component.setBackground(new Color(246,178,107));
            component.setForeground(new Color(50,50,50));
        }
        
        // Override warna khusus untuk kolom tertentu (prioritas tertinggi)
        // Kolom No.Rawat (index 2) - Merah jika sudah ada triase
        if (column == 2 && hasTriaseData(noRawat)) {
            component.setBackground(new Color(255, 0, 0)); // Warna merah
            component.setForeground(new Color(255, 255, 255)); // Teks putih
        }
        // Kolom Dokter Dituju (index 6) - Hijau jika sudah ada penilaian medis IGD
        if (column == 6 && hasMedisData(noRawat)) {
            component.setBackground(new Color(0, 128, 0)); // Warna hijau
            component.setForeground(new Color(255, 255, 255)); // Teks putih
        }
        
/*        if(table.getValueAt(row,15).toString().equals("Sudah Bayar")){
            component.setBackground(new Color(0,255,255));
            component.setForeground(new Color(50,50,50));
        } */
        return component;
    }

}
