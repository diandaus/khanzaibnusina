package berkasdigital;

import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Form untuk menampilkan Berkas Klaim BPJS dari data reg_periksa
 * @author SIMRS Khanza
 */
public class FormBerkasKlaimBpjs extends javax.swing.JDialog {
    private final DefaultTableModel tabMode;
    private Connection koneksi = koneksiDB.condb();
    private sekuel Sequel = new sekuel();
    private validasi Valid = new validasi();
    private PreparedStatement ps;
    private ResultSet rs;
    private int i = 0;
    private JFileChooser jfc = new JFileChooser();
    private FileNameExtensionFilter pdfFilter = new FileNameExtensionFilter("File PDF", "pdf");
    private String kodeberkas = "";

    /** Creates new form FormBerkasKlaimBpjs */
    public FormBerkasKlaimBpjs(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        this.setLocation(10, 10);
        setSize(1250, 700);

        // Inisialisasi Table Model
        tabMode = new DefaultTableModel(null, new Object[]{
            "No.Rawat", "No.RM", "Nama Pasien", "Tgl.Registrasi", "Jam",
            "Dokter", "Poliklinik", "Jenis Bayar", "Status Lanjut",
            "Status", "Biaya Reg", "Penanggung Jawab"
        }) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };

        tbPasien.setModel(tabMode);
        tbPasien.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbPasien.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Set lebar kolom
        for (i = 0; i < 12; i++) {
            TableColumn column = tbPasien.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(120); // No.Rawat
            } else if (i == 1) {
                column.setPreferredWidth(70);  // No.RM
            } else if (i == 2) {
                column.setPreferredWidth(200); // Nama Pasien
            } else if (i == 3) {
                column.setPreferredWidth(100); // Tgl.Registrasi
            } else if (i == 4) {
                column.setPreferredWidth(70);  // Jam
            } else if (i == 5) {
                column.setPreferredWidth(180); // Dokter
            } else if (i == 6) {
                column.setPreferredWidth(150); // Poliklinik
            } else if (i == 7) {
                column.setPreferredWidth(120); // Jenis Bayar
            } else if (i == 8) {
                column.setPreferredWidth(90);  // Status Lanjut
            } else if (i == 9) {
                column.setPreferredWidth(100); // Status
            } else if (i == 10) {
                column.setPreferredWidth(80);  // Biaya Reg
            } else if (i == 11) {
                column.setPreferredWidth(180); // Penanggung Jawab
            }
        }

        tbPasien.setDefaultRenderer(Object.class, new WarnaTable());

        TCari.setDocument(new batasInput((byte) 100).getKata(TCari));

        // Auto search jika cari cepat aktif
        if (koneksiDB.CARICEPAT().equals("aktif")) {
            TCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                @Override
                public void insertUpdate(javax.swing.event.DocumentEvent e) {
                    if (TCari.getText().length() > 2) {
                        tampilPasien();
                    }
                }

                @Override
                public void removeUpdate(javax.swing.event.DocumentEvent e) {
                    if (TCari.getText().length() > 2) {
                        tampilPasien();
                    }
                }

                @Override
                public void changedUpdate(javax.swing.event.DocumentEvent e) {
                    if (TCari.getText().length() > 2) {
                        tampilPasien();
                    }
                }
            });
        }

        // Set tanggal default
        DTPCari1.setDate(new Date());
        DTPCari2.setDate(new Date());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        MnTampilkanBerkas = new javax.swing.JMenuItem();
        MnUploadFilePDF = new javax.swing.JMenuItem();
        internalFrame1 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        tbPasien = new widget.Table();
        jPanel3 = new javax.swing.JPanel();
        panelGlass8 = new widget.panelisi();
        BtnCari = new widget.Button();
        BtnAll = new widget.Button();
        BtnKeluar = new widget.Button();
        jLabel7 = new widget.Label();
        LCount = new widget.Label();
        panelGlass9 = new widget.panelisi();
        jLabel19 = new widget.Label();
        DTPCari1 = new widget.Tanggal();
        jLabel21 = new widget.Label();
        DTPCari2 = new widget.Tanggal();
        jLabel6 = new widget.Label();
        TCari = new widget.TextBox();
        jLabel8 = new widget.Label();
        CmbStatusLanjut = new widget.ComboBox();

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        MnTampilkanBerkas.setBackground(new java.awt.Color(255, 255, 254));
        MnTampilkanBerkas.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnTampilkanBerkas.setForeground(new java.awt.Color(50, 50, 50));
        MnTampilkanBerkas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnTampilkanBerkas.setText("Tampilkan Berkas Klaim");
        MnTampilkanBerkas.setName("MnTampilkanBerkas"); // NOI18N
        MnTampilkanBerkas.setPreferredSize(new java.awt.Dimension(200, 26));
        MnTampilkanBerkas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnTampilkanBerkasActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnTampilkanBerkas);

        MnUploadFilePDF.setBackground(new java.awt.Color(255, 255, 254));
        MnUploadFilePDF.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnUploadFilePDF.setForeground(new java.awt.Color(50, 50, 50));
        MnUploadFilePDF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnUploadFilePDF.setText("Upload File PDF Manual");
        MnUploadFilePDF.setName("MnUploadFilePDF"); // NOI18N
        MnUploadFilePDF.setPreferredSize(new java.awt.Dimension(200, 26));
        MnUploadFilePDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnUploadFilePDFActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnUploadFilePDF);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("::[ Form Berkas Klaim BPJS ]::");
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 153, 255), 3), "::[ Data Pasien untuk Berkas Klaim BPJS ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);

        tbPasien.setAutoCreateRowSorter(true);
        tbPasien.setComponentPopupMenu(jPopupMenu1);
        tbPasien.setToolTipText("Silahkan klik untuk memilih data");
        tbPasien.setName("tbPasien"); // NOI18N
        tbPasien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbPasienMouseClicked(evt);
            }
        });
        tbPasien.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbPasienKeyPressed(evt);
            }
        });
        Scroll.setViewportView(tbPasien);

        internalFrame1.add(Scroll, java.awt.BorderLayout.CENTER);

        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(44, 100));
        jPanel3.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass8.setName("panelGlass8"); // NOI18N
        panelGlass8.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        BtnCari.setForeground(new java.awt.Color(0, 0, 0));
        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari.setMnemonic('2');
        BtnCari.setText("Tampilkan Data");
        BtnCari.setToolTipText("Alt+2");
        BtnCari.setName("BtnCari"); // NOI18N
        BtnCari.setPreferredSize(new java.awt.Dimension(130, 30));
        BtnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCariActionPerformed(evt);
            }
        });
        BtnCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCariKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnCari);

        BtnAll.setForeground(new java.awt.Color(0, 0, 0));
        BtnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll.setMnemonic('M');
        BtnAll.setText("Semua");
        BtnAll.setToolTipText("Alt+M");
        BtnAll.setName("BtnAll"); // NOI18N
        BtnAll.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAllActionPerformed(evt);
            }
        });
        BtnAll.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAllKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnAll);

        BtnKeluar.setForeground(new java.awt.Color(0, 0, 0));
        BtnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar.setMnemonic('K');
        BtnKeluar.setText("Keluar");
        BtnKeluar.setToolTipText("Alt+K");
        BtnKeluar.setName("BtnKeluar"); // NOI18N
        BtnKeluar.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluarActionPerformed(evt);
            }
        });
        BtnKeluar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKeluarKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnKeluar);

        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Record :");
        jLabel7.setName("jLabel7"); // NOI18N
        jLabel7.setPreferredSize(new java.awt.Dimension(65, 23));
        panelGlass8.add(jLabel7);

        LCount.setForeground(new java.awt.Color(0, 0, 0));
        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(50, 23));
        panelGlass8.add(LCount);

        jPanel3.add(panelGlass8, java.awt.BorderLayout.CENTER);

        panelGlass9.setName("panelGlass9"); // NOI18N
        panelGlass9.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel19.setForeground(new java.awt.Color(0, 0, 0));
        jLabel19.setText("Tgl.Registrasi :");
        jLabel19.setName("jLabel19"); // NOI18N
        jLabel19.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass9.add(jLabel19);

        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "25-10-2025" }));
        DTPCari1.setDisplayFormat("dd-MM-yyyy");
        DTPCari1.setName("DTPCari1"); // NOI18N
        DTPCari1.setOpaque(false);
        DTPCari1.setPreferredSize(new java.awt.Dimension(100, 23));
        panelGlass9.add(DTPCari1);

        jLabel21.setForeground(new java.awt.Color(0, 0, 0));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("s.d.");
        jLabel21.setName("jLabel21"); // NOI18N
        jLabel21.setPreferredSize(new java.awt.Dimension(23, 23));
        panelGlass9.add(jLabel21);

        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "25-10-2025" }));
        DTPCari2.setDisplayFormat("dd-MM-yyyy");
        DTPCari2.setName("DTPCari2"); // NOI18N
        DTPCari2.setOpaque(false);
        DTPCari2.setPreferredSize(new java.awt.Dimension(100, 23));
        panelGlass9.add(DTPCari2);

        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Key Word :");
        jLabel6.setName("jLabel6"); // NOI18N
        jLabel6.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass9.add(jLabel6);

        TCari.setForeground(new java.awt.Color(0, 0, 0));
        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(250, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKeyPressed(evt);
            }
        });
        panelGlass9.add(TCari);

        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Status :");
        jLabel8.setName("jLabel8"); // NOI18N
        jLabel8.setPreferredSize(new java.awt.Dimension(50, 23));
        panelGlass9.add(jLabel8);

        CmbStatusLanjut.setForeground(new java.awt.Color(0, 0, 0));
        CmbStatusLanjut.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Semua", "Ralan", "Ranap" }));
        CmbStatusLanjut.setName("CmbStatusLanjut"); // NOI18N
        CmbStatusLanjut.setPreferredSize(new java.awt.Dimension(100, 23));
        panelGlass9.add(CmbStatusLanjut);

        jPanel3.add(panelGlass9, java.awt.BorderLayout.PAGE_START);

        internalFrame1.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tbPasienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbPasienMouseClicked
        if (tabMode.getRowCount() != 0) {
            try {
                getData();
            } catch (java.lang.NullPointerException e) {
            }
        }
    }//GEN-LAST:event_tbPasienMouseClicked

    private void tbPasienKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbPasienKeyPressed
        if (tabMode.getRowCount() != 0) {
            if ((evt.getKeyCode() == KeyEvent.VK_ENTER) || (evt.getKeyCode() == KeyEvent.VK_UP) || (evt.getKeyCode() == KeyEvent.VK_DOWN)) {
                try {
                    getData();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
    }//GEN-LAST:event_tbPasienKeyPressed

    private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
        tampilPasien();
    }//GEN-LAST:event_BtnCariActionPerformed

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnCariActionPerformed(null);
        } else {
            Valid.pindah(evt, TCari, BtnAll);
        }
    }//GEN-LAST:event_BtnCariKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        TCari.setText("");
        CmbStatusLanjut.setSelectedIndex(0);
        DTPCari1.setDate(new Date());
        DTPCari2.setDate(new Date());
        tampilPasien();
    }//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnAllActionPerformed(null);
        } else {
            Valid.pindah(evt, BtnCari, BtnKeluar);
        }
    }//GEN-LAST:event_BtnAllKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
    }//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnKeluarActionPerformed(null);
        } else {
            Valid.pindah(evt, BtnAll, TCari);
        }
    }//GEN-LAST:event_BtnKeluarKeyPressed

    private void TCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            BtnCariActionPerformed(null);
        } else if (evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
            BtnCari.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_PAGE_UP) {
            BtnKeluar.requestFocus();
        }
    }//GEN-LAST:event_TCariKeyPressed

    private void MnTampilkanBerkasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnTampilkanBerkasActionPerformed
        if (tbPasien.getSelectedRow() != -1) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            String noRawat = tbPasien.getValueAt(tbPasien.getSelectedRow(), 0).toString();
            String noRM = tbPasien.getValueAt(tbPasien.getSelectedRow(), 1).toString();
            String namaPasien = tbPasien.getValueAt(tbPasien.getSelectedRow(), 2).toString();

            // Path file di server
            String pathFile = "berkasrawat/pages/upload";

            // Buka viewer PDF untuk menampilkan semua berkas
            DlgViewPdf viewer = new DlgViewPdf(null, false);
            // Ukuran dan posisi (center) sudah di-set di constructor DlgViewPdf (650x900 - lebar A4)

            // Tampilkan semua PDF yang tersedia berdasarkan no_rawat
            // Urutan: SEP, Triase, Awal_Medis_IGD, SKDP, SPRI, Lab, Radiologi
            viewer.tampilMultiplePdf(noRawat, pathFile);

            viewer.setVisible(true);

            this.setCursor(Cursor.getDefaultCursor());
        } else {
            JOptionPane.showMessageDialog(null,
                "Silakan pilih data pasien terlebih dahulu!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_MnTampilkanBerkasActionPerformed

    private void MnUploadFilePDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnUploadFilePDFActionPerformed
        if (tbPasien.getSelectedRow() != -1) {
            String noRawat = tbPasien.getValueAt(tbPasien.getSelectedRow(), 0).toString();
            String noRM = tbPasien.getValueAt(tbPasien.getSelectedRow(), 1).toString();
            String namaPasien = tbPasien.getValueAt(tbPasien.getSelectedRow(), 2).toString();

            // Setup file chooser untuk memilih file PDF
            jfc.setDialogTitle("Pilih File PDF untuk di Upload");
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            jfc.setAcceptAllFileFilterUsed(false);
            jfc.setFileFilter(pdfFilter);

            if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

                File selectedFile = jfc.getSelectedFile();
                String fileName = selectedFile.getName();

                // Validasi apakah file yang dipilih adalah PDF
                if (!fileName.toLowerCase().endsWith(".pdf")) {
                    JOptionPane.showMessageDialog(null,
                        "File yang dipilih harus berformat PDF!",
                        "Peringatan",
                        JOptionPane.WARNING_MESSAGE);
                    this.setCursor(Cursor.getDefaultCursor());
                    return;
                }

                // Generate nama file dengan format custom atau gunakan nama asli
                String namaFileBaru = fileName.replace(".pdf", "");

                // Konfirmasi nama file
                String inputNama = JOptionPane.showInputDialog(null,
                    "Masukkan nama file PDF (tanpa ekstensi .pdf):\n" +
                    "No.Rawat: " + noRawat + "\n" +
                    "Pasien: " + namaPasien,
                    namaFileBaru);

                if (inputNama != null && !inputNama.trim().isEmpty()) {
                    namaFileBaru = inputNama.trim();
                    uploadManualPDF(selectedFile, namaFileBaru, noRawat);
                } else {
                    JOptionPane.showMessageDialog(null,
                        "Upload dibatalkan!",
                        "Informasi",
                        JOptionPane.INFORMATION_MESSAGE);
                }

                this.setCursor(Cursor.getDefaultCursor());
            }
        } else {
            JOptionPane.showMessageDialog(null,
                "Silakan pilih data pasien terlebih dahulu!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_MnUploadFilePDFActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            FormBerkasKlaimBpjs dialog = new FormBerkasKlaimBpjs(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private widget.Button BtnAll;
    private widget.Button BtnCari;
    private widget.Button BtnKeluar;
    private widget.ComboBox CmbStatusLanjut;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.Label LCount;
    private javax.swing.JMenuItem MnTampilkanBerkas;
    private javax.swing.JMenuItem MnUploadFilePDF;
    private widget.ScrollPane Scroll;
    private widget.TextBox TCari;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel19;
    private widget.Label jLabel21;
    private widget.Label jLabel6;
    private widget.Label jLabel7;
    private widget.Label jLabel8;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.Table tbPasien;
    // End of variables declaration//GEN-END:variables

    /**
     * Method untuk menampilkan data pasien dari reg_periksa
     */
    public void tampilPasien() {
        Valid.tabelKosong(tabMode);
        try {
            String sql = "SELECT rp.no_rawat, rp.no_rkm_medis, p.nm_pasien, " +
                    "rp.tgl_registrasi, rp.jam_reg, " +
                    "d.nm_dokter, pl.nm_poli, pj.png_jawab, " +
                    "rp.status_lanjut, rp.stts, rp.biaya_reg, rp.p_jawab " +
                    "FROM reg_periksa rp " +
                    "INNER JOIN pasien p ON rp.no_rkm_medis = p.no_rkm_medis " +
                    "INNER JOIN dokter d ON rp.kd_dokter = d.kd_dokter " +
                    "INNER JOIN poliklinik pl ON rp.kd_poli = pl.kd_poli " +
                    "INNER JOIN penjab pj ON rp.kd_pj = pj.kd_pj " +
                    "WHERE rp.tgl_registrasi BETWEEN ? AND ? " +
                    "AND rp.status_bayar = 'Sudah Bayar' ";

            // Filter berdasarkan status lanjut (Ralan/Ranap)
            if (CmbStatusLanjut.getSelectedIndex() == 1) {
                sql += "AND rp.status_lanjut = 'Ralan' ";
            } else if (CmbStatusLanjut.getSelectedIndex() == 2) {
                sql += "AND rp.status_lanjut = 'Ranap' ";
            }

            // Filter berdasarkan pencarian
            if (!TCari.getText().trim().equals("")) {
                sql += "AND (rp.no_rawat LIKE ? OR rp.no_rkm_medis LIKE ? OR p.nm_pasien LIKE ? OR d.nm_dokter LIKE ?) ";
            }

            sql += "ORDER BY rp.tgl_registrasi DESC, rp.jam_reg DESC";

            ps = koneksi.prepareStatement(sql);
            int paramIndex = 1;
            ps.setString(paramIndex++, Valid.SetTgl(DTPCari1.getSelectedItem() + ""));
            ps.setString(paramIndex++, Valid.SetTgl(DTPCari2.getSelectedItem() + ""));

            if (!TCari.getText().trim().equals("")) {
                String cari = "%" + TCari.getText().trim() + "%";
                ps.setString(paramIndex++, cari);
                ps.setString(paramIndex++, cari);
                ps.setString(paramIndex++, cari);
                ps.setString(paramIndex++, cari);
            }

            rs = ps.executeQuery();
            while (rs.next()) {
                tabMode.addRow(new Object[]{
                    rs.getString("no_rawat"),
                    rs.getString("no_rkm_medis"),
                    rs.getString("nm_pasien"),
                    rs.getString("tgl_registrasi"),
                    rs.getString("jam_reg"),
                    rs.getString("nm_dokter"),
                    rs.getString("nm_poli"),
                    rs.getString("png_jawab"),
                    rs.getString("status_lanjut"),
                    rs.getString("stts"),
                    rs.getDouble("biaya_reg"),
                    rs.getString("p_jawab")
                });
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
        LCount.setText("" + tabMode.getRowCount());
    }

    /**
     * Method untuk get data dari tabel yang diklik
     */
    private void getData() {
        if (tbPasien.getSelectedRow() != -1) {
            String noRawat = tbPasien.getValueAt(tbPasien.getSelectedRow(), 0).toString();
            String noRM = tbPasien.getValueAt(tbPasien.getSelectedRow(), 1).toString();
            String namaPasien = tbPasien.getValueAt(tbPasien.getSelectedRow(), 2).toString();

            // Tampilkan info ke console (bisa diganti dengan action lain)
            System.out.println("Data dipilih:");
            System.out.println("No.Rawat: " + noRawat);
            System.out.println("No.RM: " + noRM);
            System.out.println("Nama: " + namaPasien);

            // TODO: Implementasi action ketika data diklik
            // Misal: buka form detail, tampilkan berkas PDF, dll
        }
    }

    /**
     * Method untuk cek hak akses
     */
    public void isCek() {
        // Implementasi sesuai kebutuhan hak akses
    }

    /**
     * Method untuk upload manual file PDF ke server
     * @param sourceFile File PDF yang dipilih dari komputer
     * @param fileName Nama file tanpa ekstensi
     * @param noRawat Nomor rawat pasien
     */
    private void uploadManualPDF(File sourceFile, String fileName, String noRawat) {
        try {
            // Baca file sebagai byte array
            byte[] data = FileUtils.readFileToByteArray(sourceFile);

            // Setup HTTP client untuk upload
            HttpClient httpClient = new DefaultHttpClient();
            String uploadUrl = "http://" + koneksiDB.HOSTHYBRIDWEB() + ":" +
                             koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() +
                             "/upload.php?doc=berkasrawat/pages/upload/";

            HttpPost postRequest = new HttpPost(uploadUrl);
            ByteArrayBody fileData = new ByteArrayBody(data, fileName + ".pdf");
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            reqEntity.addPart("file", fileData);
            postRequest.setEntity(reqEntity);
            httpClient.execute(postRequest);

            // Simpan ke database berkas_digital_perawatan
            boolean uploadSuccess = false;

            // Cari kode berkas yang sesuai (bisa disesuaikan dengan kebutuhan)
            // Untuk upload manual, kita gunakan kode berkas umum atau bisa pilih dari master
            String[] options = {"SEP", "Gruper", "RiwayatPerawatan", "Triase", "Awal Medis IGD", "SPRI", "Lab", "Radiologi", "Lainnya"};
            String jenisBerkas = (String) JOptionPane.showInputDialog(
                null,
                "Pilih jenis berkas:",
                "Jenis Berkas",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
            );

            if (jenisBerkas != null) {
                // Cari kode berkas dari database
                kodeberkas = Sequel.cariIsi("SELECT kode FROM master_berkas_digital WHERE nama LIKE '%" + jenisBerkas + "%'");

                if (kodeberkas.isEmpty()) {
                    // Jika tidak ditemukan, gunakan kode default atau minta input
                    kodeberkas = JOptionPane.showInputDialog(null,
                        "Kode berkas tidak ditemukan.\nMasukkan kode berkas manual:",
                        "001");
                }

                // Cek apakah file sudah ada di database
                if (Sequel.cariInteger("SELECT COUNT(no_rawat) AS jumlah FROM berkas_digital_perawatan " +
                        "WHERE lokasi_file='pages/upload/" + fileName + ".pdf'") > 0) {
                    // Update jika sudah ada
                    uploadSuccess = Sequel.mengedittf("berkas_digital_perawatan",
                        "lokasi_file=?",
                        "no_rawat=?,kode=?,lokasi_file=?",
                        4,
                        new String[]{
                            noRawat,
                            kodeberkas,
                            "pages/upload/" + fileName + ".pdf",
                            "pages/upload/" + fileName + ".pdf"
                        });
                } else {
                    // Insert baru jika belum ada
                    uploadSuccess = Sequel.menyimpantf("berkas_digital_perawatan",
                        "?,?,?",
                        "Berkas Digital",
                        3,
                        new String[]{
                            noRawat,
                            kodeberkas,
                            "pages/upload/" + fileName + ".pdf"
                        });
                }

                // Tampilkan notifikasi hasil
                if (uploadSuccess) {
                    JOptionPane.showMessageDialog(null,
                        "Upload berhasil!\n" +
                        "File: " + fileName + ".pdf\n" +
                        "No.Rawat: " + noRawat,
                        "Informasi",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null,
                        "Upload gagal disimpan ke database!",
                        "Peringatan",
                        JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null,
                    "Upload dibatalkan! Jenis berkas tidak dipilih.",
                    "Informasi",
                    JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            System.out.println("Upload error: " + e);
            JOptionPane.showMessageDialog(null,
                "Terjadi kesalahan saat upload:\n" + e.getMessage(),
                "Kesalahan",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
