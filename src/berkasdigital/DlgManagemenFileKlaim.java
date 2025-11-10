package berkasdigital;

import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.function.Predicate;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import fungsi.akses;

/**
 * Form untuk menampilkan Berkas Klaim BPJS dari data reg_periksa
 * @author SIMRS Khanza
 */
public class DlgManagemenFileKlaim extends javax.swing.JDialog {
    private DefaultTableModel TabModePasienRalan;
    private DefaultTableModel TabModePasienRanap;
    private Connection koneksi = koneksiDB.condb();
    private sekuel Sequel = new sekuel();
    private validasi Valid = new validasi();
    private PreparedStatement ps;
    private ResultSet rs;
    private int i = 0;
    private JFileChooser jfc = new JFileChooser();
    private FileNameExtensionFilter pdfFilter = new FileNameExtensionFilter("File PDF", "pdf");
    private String sql = "";
    private String berkas = "";
    private fungsi.akses akses = new fungsi.akses();

    /** Creates new form DlgManagemenFileKlaim */
    public DlgManagemenFileKlaim(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        // Inisialisasi Table Model untuk Rawat Jalan
        Object[] columnsRalan = new String[]{"P", "No Rawat", "No RM", "Nama Pasien", "Poli", "No SEP",
                                             "Tgl SEP", "Status Bayar", "Diagnosa", "Status Kirim"};
        TabModePasienRalan = new DefaultTableModel(null, columnsRalan) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return colIndex == 0;
            }
            Class[] types = new Class[]{
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        };

        tbListPasienRalan.setModel(TabModePasienRalan);
        tbListPasienRalan.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbListPasienRalan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int i = 0; i < 10; i++) {
            TableColumn column = tbListPasienRalan.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(33);
            } else if (i == 1) {
                column.setPreferredWidth(105);
            } else if (i == 2) {
                column.setPreferredWidth(45);
            } else if (i == 3) {
                column.setPreferredWidth(250);
            } else if (i == 4) {
                column.setPreferredWidth(200);
            } else if (i == 5) {
                column.setPreferredWidth(125);
            } else if (i == 6) {
                column.setPreferredWidth(65);
            } else if (i == 7) {
                column.setPreferredWidth(70);
            } else if (i == 8) {
                column.setPreferredWidth(150);
            } else if (i == 9) {
                column.setPreferredWidth(155);
            }
        }
        tbListPasienRalan.setDefaultRenderer(Object.class, new WarnaTable());

        // Inisialisasi Table Model untuk Rawat Inap
        Object[] columnsRanap = new String[]{"P", "No Rawat", "No RM", "Nama Pasien", "Poli", "No SEP",
                                             "Tgl SEP", "Status Bayar", "Diagnosa", "Status Kirim"};
        TabModePasienRanap = new DefaultTableModel(null, columnsRanap) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return colIndex == 0;
            }
            Class[] types = new Class[]{
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        };

        tbListPasienRanap.setModel(TabModePasienRanap);
        tbListPasienRanap.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbListPasienRanap.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int i = 0; i < 10; i++) {
            TableColumn column = tbListPasienRanap.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(33);
            } else if (i == 1) {
                column.setPreferredWidth(105);
            } else if (i == 2) {
                column.setPreferredWidth(45);
            } else if (i == 3) {
                column.setPreferredWidth(250);
            } else if (i == 4) {
                column.setPreferredWidth(200);
            } else if (i == 5) {
                column.setPreferredWidth(125);
            } else if (i == 6) {
                column.setPreferredWidth(65);
            } else if (i == 7) {
                column.setPreferredWidth(70);
            } else if (i == 8) {
                column.setPreferredWidth(150);
            } else if (i == 9) {
                column.setPreferredWidth(155);
            }
        }
        tbListPasienRanap.setDefaultRenderer(Object.class, new WarnaTable());

        // Set tanggal default
        DTPTglAwal.setDate(new Date());
        DTPTglAkhir.setDate(new Date());

        // Inisialisasi komponen untuk Panel Accordion
        FormBilling = new javax.swing.JPanel();
        FormBilling.setLayout(new java.awt.BorderLayout());
        FormBilling.setBackground(new java.awt.Color(255, 255, 255));

        // Header panel
        widget.panelisi headerPanel = new widget.panelisi();
        headerPanel.setLayout(new java.awt.BorderLayout());
        headerPanel.setPreferredSize(new java.awt.Dimension(400, 50));

        widget.Label lblHeader = new widget.Label();
        lblHeader.setText("  Billing Pasien");
        lblHeader.setFont(new java.awt.Font("Tahoma", 1, 11));
        lblHeader.setForeground(new java.awt.Color(50, 50, 50));
        headerPanel.add(lblHeader, java.awt.BorderLayout.NORTH);

        FormBilling.add(headerPanel, java.awt.BorderLayout.NORTH);

        loadBillingHTML = new javax.swing.JEditorPane();
        loadBillingHTML.setContentType("text/html");
        loadBillingHTML.setEditable(false);
        loadBillingHTML.setBackground(new java.awt.Color(255, 255, 255));

        widget.ScrollPane scrollBilling = new widget.ScrollPane();
        scrollBilling.setViewportView(loadBillingHTML);
        FormBilling.add(scrollBilling, java.awt.BorderLayout.CENTER);

        lblNoRawat = new widget.Label();
        lblNoRawat.setText("");
        lblNoRawat.setName("lblNoRawat");
        lblNoRawat.setVisible(false);

        // Tambahkan FormBilling ke ScrollMenu
        ScrollMenu.setViewportView(FormBilling);

        // Set initial state accordion (collapsed)
        PanelAccor.setPreferredSize(new Dimension(20, 700));
        FormBilling.setVisible(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Popup = new javax.swing.JPopupMenu();
        MnTampilkanBerkas = new javax.swing.JMenuItem();
        MnUploadFilePDF = new javax.swing.JMenuItem();
        TNoRw = new widget.TextBox();
        internalFrame1 = new widget.InternalFrame();
        panelisi3 = new widget.panelisi();
        jLabel7 = new widget.Label();
        DTPTglAwal = new widget.Tanggal();
        jLabel8 = new widget.Label();
        DTPTglAkhir = new widget.Tanggal();
        label11 = new widget.Label();
        label9 = new widget.Label();
        TCariKunjungan = new widget.TextBox();
        BtnCariTindakan = new widget.Button();
        BtnAll = new widget.Button();
        label10 = new widget.Label();
        LCount = new widget.Label();
        BtnKeluar = new widget.Button();
        TabRawat = new javax.swing.JTabbedPane();
        Scroll2 = new widget.ScrollPane();
        tbListPasienRalan = new widget.Table();
        Scroll1 = new widget.ScrollPane();
        tbListPasienRanap = new widget.Table();
        PanelAccor = new widget.PanelBiasa();
        ChkAccor = new widget.CekBox();
        ScrollMenu = new widget.ScrollPane();

        Popup.setName("Popup"); // NOI18N

        MnTampilkanBerkas.setBackground(new java.awt.Color(255, 255, 254));
        MnTampilkanBerkas.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnTampilkanBerkas.setForeground(new java.awt.Color(50, 50, 50));
        MnTampilkanBerkas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnTampilkanBerkas.setText("Tampilkan File Klaim");
        MnTampilkanBerkas.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        MnTampilkanBerkas.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        MnTampilkanBerkas.setName("MnTampilkanBerkas"); // NOI18N
        MnTampilkanBerkas.setPreferredSize(new java.awt.Dimension(160, 26));
        MnTampilkanBerkas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnTampilkanBerkasActionPerformed(evt);
            }
        });
        Popup.add(MnTampilkanBerkas);

        MnUploadFilePDF.setBackground(new java.awt.Color(255, 255, 254));
        MnUploadFilePDF.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnUploadFilePDF.setForeground(new java.awt.Color(50, 50, 50));
        MnUploadFilePDF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnUploadFilePDF.setText("Upload File Klaim");
        MnUploadFilePDF.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        MnUploadFilePDF.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        MnUploadFilePDF.setName("MnUploadFilePDF"); // NOI18N
        MnUploadFilePDF.setPreferredSize(new java.awt.Dimension(160, 26));
        MnUploadFilePDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnUploadFilePDFActionPerformed(evt);
            }
        });
        Popup.add(MnUploadFilePDF);

        TNoRw.setHighlighter(null);
        TNoRw.setName("TNoRw"); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Manajemen File Klaim ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        panelisi3.setName("panelisi3"); // NOI18N
        panelisi3.setPreferredSize(new java.awt.Dimension(100, 43));
        panelisi3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 4, 9));

        jLabel7.setText("Tanggal :");
        jLabel7.setName("jLabel7"); // NOI18N
        panelisi3.add(jLabel7);

        DTPTglAwal.setForeground(new java.awt.Color(50, 70, 50));
        DTPTglAwal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "10-11-2025" }));
        DTPTglAwal.setDisplayFormat("dd-MM-yyyy");
        DTPTglAwal.setName("DTPTglAwal"); // NOI18N
        DTPTglAwal.setOpaque(false);
        DTPTglAwal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DTPTglAwalKeyPressed(evt);
            }
        });
        panelisi3.add(DTPTglAwal);

        jLabel8.setText("s/d");
        jLabel8.setName("jLabel8"); // NOI18N
        panelisi3.add(jLabel8);

        DTPTglAkhir.setForeground(new java.awt.Color(50, 70, 50));
        DTPTglAkhir.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "10-11-2025" }));
        DTPTglAkhir.setDisplayFormat("dd-MM-yyyy");
        DTPTglAkhir.setName("DTPTglAkhir"); // NOI18N
        DTPTglAkhir.setOpaque(false);
        DTPTglAkhir.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DTPTglAkhirKeyPressed(evt);
            }
        });
        panelisi3.add(DTPTglAkhir);

        label11.setName("label11"); // NOI18N
        label11.setPreferredSize(new java.awt.Dimension(23, 23));
        panelisi3.add(label11);

        label9.setText("Key Word :");
        label9.setName("label9"); // NOI18N
        label9.setPreferredSize(new java.awt.Dimension(68, 23));
        panelisi3.add(label9);

        TCariKunjungan.setName("TCariKunjungan"); // NOI18N
        TCariKunjungan.setPreferredSize(new java.awt.Dimension(168, 23));
        TCariKunjungan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TCariKunjunganActionPerformed(evt);
            }
        });
        TCariKunjungan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKunjunganKeyPressed(evt);
            }
        });
        panelisi3.add(TCariKunjungan);

        BtnCariTindakan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCariTindakan.setMnemonic('1');
        BtnCariTindakan.setToolTipText("Alt+1");
        BtnCariTindakan.setName("BtnCariTindakan"); // NOI18N
        BtnCariTindakan.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCariTindakan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCariTindakanActionPerformed(evt);
            }
        });
        BtnCariTindakan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCariTindakanKeyPressed(evt);
            }
        });
        panelisi3.add(BtnCariTindakan);

        BtnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll.setMnemonic('M');
        BtnAll.setToolTipText("Alt+M");
        BtnAll.setName("BtnAll"); // NOI18N
        BtnAll.setPreferredSize(new java.awt.Dimension(28, 23));
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
        panelisi3.add(BtnAll);

        label10.setText("Record :");
        label10.setName("label10"); // NOI18N
        label10.setPreferredSize(new java.awt.Dimension(60, 23));
        panelisi3.add(label10);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(55, 23));
        panelisi3.add(LCount);

        BtnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar.setMnemonic('4');
        BtnKeluar.setText("Keluar");
        BtnKeluar.setToolTipText("Alt+4");
        BtnKeluar.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        BtnKeluar.setName("BtnKeluar"); // NOI18N
        BtnKeluar.setPreferredSize(new java.awt.Dimension(90, 23));
        BtnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluarActionPerformed(evt);
            }
        });
        panelisi3.add(BtnKeluar);

        internalFrame1.add(panelisi3, java.awt.BorderLayout.PAGE_END);

        TabRawat.setBackground(new java.awt.Color(255, 255, 254));
        TabRawat.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(241, 246, 236)));
        TabRawat.setForeground(new java.awt.Color(50, 50, 50));
        TabRawat.setFocusCycleRoot(true);
        TabRawat.setName("TabRawat"); // NOI18N
        TabRawat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabRawatMouseClicked(evt);
            }
        });

        Scroll2.setComponentPopupMenu(Popup);
        Scroll2.setName("Scroll2"); // NOI18N
        Scroll2.setOpaque(true);

        tbListPasienRalan.setAutoCreateRowSorter(true);
        tbListPasienRalan.setToolTipText("");
        tbListPasienRalan.setComponentPopupMenu(Popup);
        tbListPasienRalan.setName("tbListPasienRalan"); // NOI18N
        tbListPasienRalan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbListPasienRalanMouseClicked(evt);
            }
        });
        tbListPasienRalan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbListPasienRalanKeyPressed(evt);
            }
        });
        Scroll2.setViewportView(tbListPasienRalan);

        TabRawat.addTab("Rawat Jalan", Scroll2);

        Scroll1.setComponentPopupMenu(Popup);
        Scroll1.setName("Scroll1"); // NOI18N
        Scroll1.setOpaque(true);

        tbListPasienRanap.setAutoCreateRowSorter(true);
        tbListPasienRanap.setToolTipText("");
        tbListPasienRanap.setComponentPopupMenu(Popup);
        tbListPasienRanap.setName("tbListPasienRanap"); // NOI18N
        tbListPasienRanap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbListPasienRanapMouseClicked(evt);
            }
        });
        tbListPasienRanap.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbListPasienRanapKeyPressed(evt);
            }
        });
        Scroll1.setViewportView(tbListPasienRanap);

        TabRawat.addTab("Rawat Inap", Scroll1);

        internalFrame1.add(TabRawat, java.awt.BorderLayout.CENTER);

        PanelAccor.setBackground(new java.awt.Color(255, 255, 255));
        PanelAccor.setName("PanelAccor"); // NOI18N
        PanelAccor.setPreferredSize(new java.awt.Dimension(445, 43));
        PanelAccor.setLayout(new java.awt.BorderLayout(1, 1));

        ChkAccor.setBackground(new java.awt.Color(255, 250, 250));
        ChkAccor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/kiri.png"))); // NOI18N
        ChkAccor.setSelected(false);
        ChkAccor.setFocusable(false);
        ChkAccor.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChkAccor.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ChkAccor.setName("ChkAccor"); // NOI18N
        ChkAccor.setPreferredSize(new java.awt.Dimension(15, 20));
        ChkAccor.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/kiri.png"))); // NOI18N
        ChkAccor.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/kanan.png"))); // NOI18N
        ChkAccor.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/kanan.png"))); // NOI18N
        ChkAccor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkAccorActionPerformed(evt);
            }
        });
        PanelAccor.add(ChkAccor, java.awt.BorderLayout.WEST);

        ScrollMenu.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        ScrollMenu.setName("ScrollMenu"); // NOI18N
        ScrollMenu.setOpaque(true);
        ScrollMenu.setPreferredSize(new java.awt.Dimension(407, 1075));
        PanelAccor.add(ScrollMenu, java.awt.BorderLayout.CENTER);

        internalFrame1.add(PanelAccor, java.awt.BorderLayout.EAST);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void TCariKunjunganKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKunjunganKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            BtnCariTindakanActionPerformed(null);
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
//            tbListPasienRalan.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
            BtnCariTindakan.requestFocus();
        }
}//GEN-LAST:event_TCariKunjunganKeyPressed

    private void BtnCariTindakanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariTindakanActionPerformed
        if (TabRawat.getSelectedIndex() == 0) {
            tampilRalan();
        } else if (TabRawat.getSelectedIndex() == 1) {
            tampilRanap();
        }
}//GEN-LAST:event_BtnCariTindakanActionPerformed

    private void BtnCariTindakanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariTindakanKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnCariTindakanActionPerformed(null);
        } else {
//            Valid.pindah(evt, TCariTindakan, BtnAllTindakan);
        }
}//GEN-LAST:event_BtnCariTindakanKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();

    }//GEN-LAST:event_BtnKeluarActionPerformed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        TCariKunjungan.requestFocus();
    }//GEN-LAST:event_formWindowActivated

    private void DTPTglAwalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DTPTglAwalKeyPressed
//        Valid.pindah(evt,TCariTindakan,cmbJam);
    }//GEN-LAST:event_DTPTglAwalKeyPressed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
//       xw tampil();
    }//GEN-LAST:event_formWindowOpened

    private void DTPTglAkhirKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DTPTglAkhirKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_DTPTglAkhirKeyPressed

    private void TabRawatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabRawatMouseClicked
        if (TabRawat.getSelectedIndex() == 0) {
            TCariKunjungan.setText("");
            tampilRalan();            
        } else if (TabRawat.getSelectedIndex() == 1) {
            TCariKunjungan.setText("");    
            tampilRanap();            
        }
    }//GEN-LAST:event_TabRawatMouseClicked

    private void tbListPasienRanapMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbListPasienRanapMouseClicked
        if (tbListPasienRanap.getSelectedRow() != -1) {
            String noRawat = tbListPasienRanap.getValueAt(tbListPasienRanap.getSelectedRow(), 1).toString();
            lblNoRawat.setText(noRawat);

            // Jika accordion terbuka, tampilkan billing
            if (ChkAccor.isSelected()) {
                tampilBilling();
            }
        }
    }//GEN-LAST:event_tbListPasienRanapMouseClicked

    private void tbListPasienRanapKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbListPasienRanapKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tbListPasienRanapKeyPressed

    private void tbListPasienRalanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbListPasienRalanMouseClicked
        if (tbListPasienRalan.getSelectedRow() != -1) {
            String noRawat = tbListPasienRalan.getValueAt(tbListPasienRalan.getSelectedRow(), 1).toString();
            lblNoRawat.setText(noRawat);

            // Jika accordion terbuka, tampilkan billing
            if (ChkAccor.isSelected()) {
                tampilBilling();
            }
        }
    }//GEN-LAST:event_tbListPasienRalanMouseClicked

    private void tbListPasienRalanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbListPasienRalanKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tbListPasienRalanKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        TCariKunjungan.setText("");
                if (TabRawat.getSelectedIndex() == 0) {
                tampilRalan();
                } else if (TabRawat.getSelectedIndex() == 1) {
                tampilRanap();
            }
    }//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
           if (TabRawat.getSelectedIndex() == 0) {
            tampilRalan();
            } else if (TabRawat.getSelectedIndex() == 1) {
            tampilRanap();
            TCariKunjungan.setText("");
            }
        }
    
    }//GEN-LAST:event_BtnAllKeyPressed

    private void TCariKunjunganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TCariKunjunganActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TCariKunjunganActionPerformed

    private void MnTampilkanBerkasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnTampilkanBerkasActionPerformed
        // Tentukan tabel mana yang sedang aktif berdasarkan tab yang dipilih
        JTable activeTable;
        if (TabRawat.getSelectedIndex() == 0) {
            activeTable = tbListPasienRalan;
        } else {
            activeTable = tbListPasienRanap;
        }

        if (activeTable.getSelectedRow() != -1) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            String noRawat = activeTable.getValueAt(activeTable.getSelectedRow(), 1).toString();
            String noRM = activeTable.getValueAt(activeTable.getSelectedRow(), 2).toString();
            String namaPasien = activeTable.getValueAt(activeTable.getSelectedRow(), 3).toString();

            // Path file di server
            String pathFile = "berkasrawat/pages/upload";

            // Buka viewer PDF untuk menampilkan semua berkas
            DlgViewPdf viewer = new DlgViewPdf(null, false);
            // Ukuran dan posisi (center) sudah di-set di constructor DlgViewPdf (650x900 - lebar A4)

            // Tampilkan semua PDF yang tersedia berdasarkan no_rawat
            // Urutan: SEP, Triase, Awal_Medis_IGD, SKDP, SPRI, Lab, Radiologi
            boolean hasFiles = viewer.tampilMultiplePdf(noRawat, pathFile);

            // Hanya buka viewer jika ada file yang ditemukan
            if (hasFiles) {
                viewer.setVisible(true);
            } else {
                viewer.dispose(); // Tutup viewer jika tidak ada file
            }

            this.setCursor(Cursor.getDefaultCursor());
        } else {
            JOptionPane.showMessageDialog(null,
                "Silakan pilih data pasien terlebih dahulu!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_MnTampilkanBerkasActionPerformed

    private void MnUploadFilePDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnUploadFilePDFActionPerformed
        // Tentukan tabel mana yang sedang aktif berdasarkan tab yang dipilih
        JTable activeTable;
        if (TabRawat.getSelectedIndex() == 0) {
            activeTable = tbListPasienRalan;
        } else {
            activeTable = tbListPasienRanap;
        }

        if (activeTable.getSelectedRow() != -1) {
            String noRawat = activeTable.getValueAt(activeTable.getSelectedRow(), 1).toString();
            String noRM = activeTable.getValueAt(activeTable.getSelectedRow(), 2).toString();
            String namaPasien = activeTable.getValueAt(activeTable.getSelectedRow(), 3).toString();

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

                // Pilih jenis berkas terlebih dahulu
                String[] options = {"SEP", "Gruper", "RiwayatPerawatan", "Resume", "Triase", "Awal Medis IGD", "SKDP", "SPRI", "Lab", "Radiologi", "Lainnya"};
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
                    // Format noRawat: ganti / dengan _
                    String noRawatFormatted = noRawat.replaceAll("/", "_");

                    // Generate nama file otomatis: {JenisBerkas}_{NoRawat}
                    String namaFileBaru = jenisBerkas + "_" + noRawatFormatted;

                    // Konfirmasi nama file yang akan diupload
                    int confirm = JOptionPane.showConfirmDialog(null,
                        "File akan diupload dengan nama:\n" +
                        namaFileBaru + ".pdf\n\n" +
                        "No.Rawat: " + noRawat + "\n" +
                        "Pasien: " + namaPasien + "\n" +
                        "Jenis: " + jenisBerkas + "\n\n" +
                        "Lanjutkan upload?",
                        "Konfirmasi Upload",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                    if (confirm == JOptionPane.YES_OPTION) {
                        uploadManualPDF(selectedFile, namaFileBaru, noRawat, jenisBerkas);
                    } else {
                        JOptionPane.showMessageDialog(null,
                            "Upload dibatalkan!",
                            "Informasi",
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null,
                        "Upload dibatalkan! Jenis berkas tidak dipilih.",
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
        }        // TODO add your handling code here:
    }//GEN-LAST:event_MnUploadFilePDFActionPerformed

    private void ChkAccorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkAccorActionPerformed
         isMenu();
    }//GEN-LAST:event_ChkAccorActionPerformed
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            DlgManagemenFileKlaim dialog = new DlgManagemenFileKlaim(new javax.swing.JFrame(), true);
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
    private widget.Button BtnCariTindakan;
    private widget.Button BtnKeluar;
    private widget.CekBox ChkAccor;
    private widget.Tanggal DTPTglAkhir;
    private widget.Tanggal DTPTglAwal;
    private widget.Label LCount;
    private javax.swing.JMenuItem MnTampilkanBerkas;
    private javax.swing.JMenuItem MnUploadFilePDF;
    private widget.PanelBiasa PanelAccor;
    private javax.swing.JPopupMenu Popup;
    private widget.ScrollPane Scroll1;
    private widget.ScrollPane Scroll2;
    private widget.ScrollPane ScrollMenu;
    private widget.TextBox TCariKunjungan;
    private widget.TextBox TNoRw;
    private javax.swing.JTabbedPane TabRawat;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel7;
    private widget.Label jLabel8;
    private widget.Label label10;
    private widget.Label label11;
    private widget.Label label9;
    private widget.panelisi panelisi3;
    private widget.Table tbListPasienRalan;
    private widget.Table tbListPasienRanap;
    // End of variables declaration//GEN-END:variables
    private javax.swing.JPanel FormBilling;
    private javax.swing.JEditorPane loadBillingHTML;
    private widget.Label lblNoRawat;
    
    private void tampilRalan() {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            Valid.tabelKosong(TabModePasienRalan);
            String sql = "SELECT reg_periksa.no_rawat, reg_periksa.tgl_registrasi, reg_periksa.no_rkm_medis, pasien.nm_pasien, poliklinik.nm_poli, reg_periksa.status_bayar " +
                         "FROM reg_periksa " +
                         "JOIN pasien ON reg_periksa.no_rkm_medis = pasien.no_rkm_medis " +
                         "JOIN poliklinik ON reg_periksa.kd_poli = poliklinik.kd_poli " +
                         "WHERE reg_periksa.status_lanjut = 'Ralan' " +
                         "AND reg_periksa.tgl_registrasi BETWEEN ? AND ? " +
                         "AND (reg_periksa.no_rawat LIKE ? OR pasien.nm_pasien LIKE ? OR poliklinik.nm_poli LIKE ? OR reg_periksa.status_bayar LIKE ?) " +
                         "AND reg_periksa.stts <> 'Batal' ORDER BY poliklinik.nm_poli ASC, pasien.nm_pasien ASC";
            try (PreparedStatement ps = koneksi.prepareStatement(sql)) {
                ps.setString(1, Valid.SetTgl(DTPTglAwal.getSelectedItem() + ""));
                ps.setString(2, Valid.SetTgl(DTPTglAkhir.getSelectedItem() + ""));
                ps.setString(3, "%" + TCariKunjungan.getText() + "%");
                ps.setString(4, "%" + TCariKunjungan.getText() + "%");
                ps.setString(5, "%" + TCariKunjungan.getText() + "%");
                ps.setString(6, "%" + TCariKunjungan.getText() + "%");

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String StatusKoding, noSep, tglSep, StatusKirim;

                        int statusKoding = Sequel.cariInteger("SELECT COUNT(no_rawat) FROM diagnosa_pasien WHERE no_rawat='" + rs.getString("no_rawat") + "'");
                        if (statusKoding > 0) {
                            StatusKoding = Sequel.cariIsi("SELECT GROUP_CONCAT(kd_penyakit SEPARATOR ', ') FROM diagnosa_pasien WHERE no_rawat='" + rs.getString("no_rawat") + "'");
                        } else {
                            StatusKoding = "Kosong";
                        }

                        int sep = Sequel.cariInteger("SELECT COUNT(no_rawat) FROM bridging_sep WHERE no_rawat='" + rs.getString("no_rawat") + "'");
                        if (sep > 0) {
                            noSep = Sequel.cariIsi("SELECT no_sep FROM bridging_sep WHERE no_rawat='" + rs.getString("no_rawat") + "'");
                            tglSep = Sequel.cariIsi("SELECT tglsep FROM bridging_sep WHERE no_rawat='" + rs.getString("no_rawat") + "'");
                            int statusKirim = Sequel.cariInteger("SELECT COUNT(*) FROM ("
                                + "SELECT no_sep FROM inacbg_data_terkirim WHERE no_sep='" + noSep + "'"
                                + " UNION ALL "
                                + "SELECT no_sep FROM inacbg_data_terkirim2 WHERE no_sep='" + noSep + "') AS combined");
                            if (statusKirim > 0) {
                                StatusKirim = "Sudah Kirim";
                            } else {
                                StatusKirim = "Belum Kirim";
                            }
                        } else {
                            noSep = "-";
                            tglSep = "-";
                            StatusKirim = "Belum Ada SEP/Umum/Lainnya";
                        }

                        TabModePasienRalan.addRow(new Object[]{
                            false, rs.getString("no_rawat"), rs.getString("no_rkm_medis"), rs.getString("nm_pasien"), rs.getString("nm_poli"), noSep, tglSep, rs.getString("status_bayar"),  StatusKoding, StatusKirim
                        });
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
        LCount.setText("" + tbListPasienRalan.getRowCount());
        this.setCursor(Cursor.getDefaultCursor());
    }

    private void tampilRanap() {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            Valid.tabelKosong(TabModePasienRanap);
            String sql = "SELECT reg_periksa.no_rawat, reg_periksa.tgl_registrasi, reg_periksa.no_rkm_medis, pasien.nm_pasien, poliklinik.nm_poli, reg_periksa.status_bayar " +
                         "FROM reg_periksa " +
                         "JOIN pasien ON reg_periksa.no_rkm_medis = pasien.no_rkm_medis " +
                         "JOIN poliklinik ON reg_periksa.kd_poli = poliklinik.kd_poli " +
                         "WHERE reg_periksa.status_lanjut = 'Ranap' " +
                         "AND reg_periksa.tgl_registrasi BETWEEN ? AND ? " +
                         "AND (reg_periksa.no_rawat LIKE ? OR pasien.nm_pasien LIKE ? OR poliklinik.nm_poli LIKE ? OR reg_periksa.status_bayar LIKE ?) " +
                         "AND reg_periksa.stts <> 'Batal' ORDER BY poliklinik.nm_poli ASC, pasien.nm_pasien ASC";
            try (PreparedStatement ps = koneksi.prepareStatement(sql)) {
                ps.setString(1, Valid.SetTgl(DTPTglAwal.getSelectedItem() + ""));
                ps.setString(2, Valid.SetTgl(DTPTglAkhir.getSelectedItem() + ""));
                ps.setString(3, "%" + TCariKunjungan.getText() + "%");
                ps.setString(4, "%" + TCariKunjungan.getText() + "%");
                ps.setString(5, "%" + TCariKunjungan.getText() + "%");
                ps.setString(6, "%" + TCariKunjungan.getText() + "%");

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String StatusKoding, kamar, noSep, tglSep, StatusKirim;

                        int statusKoding = Sequel.cariInteger("SELECT COUNT(no_rawat) FROM diagnosa_pasien WHERE no_rawat='" + rs.getString("no_rawat") + "'");
                        if (statusKoding > 0) {
                            StatusKoding = Sequel.cariIsi("SELECT GROUP_CONCAT(kd_penyakit SEPARATOR ', ') FROM diagnosa_pasien WHERE no_rawat='" + rs.getString("no_rawat") + "'");
                        } else {
                            StatusKoding = "Kosong";
                        }

                        kamar = Sequel.cariIsi("SELECT nm_bangsal FROM kamar_inap JOIN kamar ON kamar_inap.kd_kamar=kamar.kd_kamar JOIN bangsal ON kamar.kd_bangsal=bangsal.kd_bangsal WHERE no_rawat='" + rs.getString("no_rawat") + "' ORDER BY tgl_keluar DESC");

                        int sep = Sequel.cariInteger("SELECT COUNT(no_rawat) FROM bridging_sep WHERE no_rawat='" + rs.getString("no_rawat") + "'");
                        if (sep > 0) {
                            noSep = Sequel.cariIsi("SELECT no_sep FROM bridging_sep WHERE no_rawat='" + rs.getString("no_rawat") + "'");
                            tglSep = Sequel.cariIsi("SELECT tglsep FROM bridging_sep WHERE no_rawat='" + rs.getString("no_rawat") + "'");
                            int statusKirim = Sequel.cariInteger("SELECT COUNT(*) FROM ("
                                + "SELECT no_sep FROM inacbg_data_terkirim WHERE no_sep='" + noSep + "'"
                                + " UNION ALL "
                                + "SELECT no_sep FROM inacbg_data_terkirim2 WHERE no_sep='" + noSep + "') AS combined");
                            if (statusKirim > 0) {
                                StatusKirim = "Sudah Kirim";
                            } else {
                                StatusKirim = "Belum Kirim";
                            }
                        } else {
                            noSep = "-";
                            tglSep = "-";
                            StatusKirim = "Belum Ada SEP/Umum/Lainnya";
                        }

                        TabModePasienRanap.addRow(new Object[]{
                            false, rs.getString("no_rawat"), rs.getString("no_rkm_medis"), rs.getString("nm_pasien"), kamar, noSep, tglSep, rs.getString("status_bayar"), StatusKoding, StatusKirim
                        });
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
        LCount.setText("" + tbListPasienRanap.getRowCount());
        this.setCursor(Cursor.getDefaultCursor());
    }
  
    private void uploadManualPDF(File sourceFile, String fileName, String noRawat, String jenisBerkas) {
        try {
            // Baca file sebagai byte array
            byte[] data = FileUtils.readFileToByteArray(sourceFile);

            // Setup HTTP client untuk upload (gunakan fully qualified name untuk menghindari conflict)
            org.apache.http.client.HttpClient httpClient = new DefaultHttpClient();
            String uploadUrl = "http://" + koneksiDB.HOSTHYBRIDWEB() + ":" +
                             koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() +
                             "/upload.php?doc=berkasrawat/pages/upload/";

            HttpPost postRequest = new HttpPost(uploadUrl);
            ByteArrayBody fileData = new ByteArrayBody(data, fileName + ".pdf");
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
            reqEntity.addPart("file", fileData);
            postRequest.setEntity(reqEntity);
            httpClient.execute(postRequest);

            // Tampilkan notifikasi hasil upload
            JOptionPane.showMessageDialog(null,
                "Upload berhasil!\n" +
                "File: " + fileName + ".pdf\n" +
                "Jenis: " + jenisBerkas + "\n" +
                "No.Rawat: " + noRawat + "\n" +
                "Lokasi: berkasrawat/pages/upload/",
                "Informasi",
                JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            System.out.println("Upload error: " + e);
            JOptionPane.showMessageDialog(null,
                "Terjadi kesalahan saat upload:\n" + e.getMessage(),
                "Kesalahan",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
     private void isMenu() {
        if (ChkAccor.isSelected() == true) {
            ChkAccor.setVisible(false);
            PanelAccor.setPreferredSize(new Dimension(650, internalFrame1.getHeight()));
            FormBilling.setVisible(true);
            ChkAccor.setVisible(true);

            // Tampilkan billing saat accordion dibuka
            if (!lblNoRawat.getText().isEmpty()) {
                tampilBilling();
            }
        } else {
            ChkAccor.setVisible(false);
            PanelAccor.setPreferredSize(new Dimension(20, internalFrame1.getHeight()));
            FormBilling.setVisible(false);
            ChkAccor.setVisible(true);
        }
        getContentPane().validate();
        getContentPane().repaint();
    }
     
     private void tampilBilling() {
        try {
            try (PreparedStatement ps = koneksi.prepareStatement(
                "select b.no, b.nm_perawatan, b.pemisah, b.biaya, b.jumlah, " +
                "b.tambahan, b.totalbiaya from billing b where b.no_rawat = ?"
            )) {
                ps.setString(1, lblNoRawat.getText());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        int row = 0;
                        double total = 0;
                        StringBuilder sb = new StringBuilder();
                        sb.append("<html><head><style>")
                            .append("body { font-family: Tahoma, Arial, sans-serif; font-size: 9px; padding: 5px; }")
                            .append("table { width: 100%; border-collapse: collapse; font-size: 9px; }")
                            .append("td { padding: 2px; border-bottom: 1px solid #e0e0e0; font-size: 9px; }")
                            .append(".header-row { background-color: #f5f5f5; font-weight: bold; }")
                            .append(".total-row { background-color: #fff4e0; font-weight: bold; border-top: 2px solid #333; font-size: 10px; }")
                            .append("</style></head><body><table>");
                        do {
                            total += rs.getDouble("totalbiaya");
                            if (row++ < 6) {
                                sb.append("<tr><td width=\"20%\">")
                                    .append(rs.getString("no").trim())
                                    .append("</td><td width=\"40%\" colspan=\"5\">")
                                    .append(rs.getString("nm_perawatan").trim())
                                    .append("</td></tr>");
                            } else {
                                if (rs.getString("no").isBlank() && rs.getDouble("biaya") == 0) {
                                    sb.append("<tr><td width=\"20%\">")
                                        .append(rs.getString("no").trim());
                                    if (rs.getString("nm_perawatan").startsWith("Total")) {
                                        sb.append("</td><td colspan=\"5\" align=\"right\">");
                                    } else {
                                        sb.append("</td><td colspan=\"5\">");
                                    }
                                    sb.append(rs.getString("nm_perawatan").trim()).append("</td></tr>");
                                } else {
                                    sb.append("<tr><td width=\"20%\">").append(rs.getString("no")).append("</td><td width=\"48%\">").append(rs.getString("nm_perawatan"))
                                        .append("</td><td width=\"9%\" align=\"right\">").append(rs.getDouble("biaya") == 0 ? "" : Valid.SetAngka(rs.getDouble("biaya")))
                                        .append("</td><td width=\"2%\" align=\"right\">").append(rs.getDouble("jumlah") == 0 ? "" : Valid.SetAngka(rs.getDouble("jumlah")))
                                        .append("</td><td width=\"9%\" align=\"right\">").append(rs.getDouble("tambahan") == 0 ? "" : Valid.SetAngka(rs.getDouble("tambahan")))
                                        .append("</td><td width=\"10%\" align=\"right\">").append(rs.getDouble("totalbiaya") == 0 ? "" : Valid.SetAngka(rs.getDouble("totalbiaya")))
                                        .append("</td></tr>");
                                }
                            }
                        } while (rs.next());
                        sb.append("<tr class=\"total-row\"><td width=\"20%\">TOTAL BIAYA</td><td>:</td><td colspan=\"4\" align=\"right\">")
                            .append(Valid.SetAngka(total))
                            .append("</td></tr></table></body></html>");
                        loadBillingHTML.setText(sb.toString());
                    } else {
                        loadBillingHTML.setText("<html><body style='font-family: Tahoma; font-size: 9px; padding: 20px; text-align: center; color: #999;'>"
                            + "<p>Tidak ada data billing untuk nomor rawat: " + lblNoRawat.getText() + "</p>"
                            + "</body></html>");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
        }
    }
}
