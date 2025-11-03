/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DlgPenyakit.java
 *
 * Created on May 23, 2010, 12:57:16 AM
 */

package simrskhanza;

import inventory.DlgBarang;
import inventory.DlgCariKonversi;
import fungsi.WarnaTable2;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.akses;
import inventory.DlgCariAturanPakai;
import inventory.DlgCariMetodeRacik;
import inventory.riwayatobat;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author dosen
 */
public final class DlgInputResepPulang extends javax.swing.JDialog {
    private final DefaultTableModel tabMode,tabModeObatRacikan,tabModeDetailObatRacikan;;
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private Connection koneksi=koneksiDB.condb();
    private riwayatobat Trackobat=new riwayatobat();
    private PreparedStatement psobat;
    private PreparedStatement pscarikapasitas,psstok,psrekening,ps2,psbatch;
    private ResultSet rs;
    private ResultSet carikapasitas,rsstok,rsrekening,rs2,rsbatch;
    private WarnaTable2 warna=new WarnaTable2();
    private String aktifkanbatch="no",pilihanetiket="",nopermintaan="",aktifpcare="no",sql="",hppfarmasi="",nokunjungan="";
    private String[] no,kategori,industri,golongan,kadaluarsa;
    private boolean sukses=true;
    private DlgCariBangsal bangsal=new DlgCariBangsal(null,false);
    private DlgCariAturanPakai aturan=new DlgCariAturanPakai(null,false);
    private DlgCariMetodeRacik metoderacik=new DlgCariMetodeRacik(null,false);
    private WarnaTable2 warna2=new WarnaTable2();
    private WarnaTable2 warna3=new WarnaTable2();
    private double h_belicari=0, hargacari=0, sisacari=0,x=0,y=0,embalase=Sequel.cariIsiAngka("select set_embalase.embalase_per_obat from set_embalase"),
            tuslah=Sequel.cariIsiAngka("select set_embalase.tuslah_per_obat from set_embalase"),kenaikan,stokbarang,ttlhpp,ttljual;
    private DlgBarang barang=new DlgBarang(null,false);
    private int jml=0,i=0,index;
    private int z=0,row=0;
    private double[] jumlah,harga,stok;
    private double[] eb,ts,beli,kapasitas,kandungan,p1,p2;
    private String[] kodebarang,namabarang,kodesatuan,letakbarang,namajenis,dosis,nobatch,nofaktur;
    private boolean[] pilih;
    
    /** Creates new form DlgPenyakit
     * @param parent
     * @param modal */
    public DlgInputResepPulang(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(10,2);
        setSize(656,250);

        tabMode=new DefaultTableModel(null,new Object[]{"Jml","Aturan Pakai","Kode Barang","Nama Barang","Satuan","Kandungan","Harga(Rp)","Jenis Obat","No.Batch","No.Faktur","Stok"}){
            @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if ((colIndex==0)||(colIndex==1)||(colIndex==8)||(colIndex==9)) {
                    a=true;
                }
                return a;
             }
        };
        tbKamar.setModel(tabMode);
        //tbPenyakit.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbPenyakit.getBackground()));
        tbKamar.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbKamar.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (i = 0; i < 11; i++) {
            TableColumn column = tbKamar.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(45);
            }else if(i==1){
                column.setPreferredWidth(120);
            }else if(i==2){
                column.setPreferredWidth(85);
            }else if(i==3){
                column.setPreferredWidth(200);
            }else if(i==4){
                column.setPreferredWidth(60);
            }else if(i==5){
                column.setPreferredWidth(120);
            }else if(i==6){
                column.setPreferredWidth(90);
            }else if(i==7){
                column.setPreferredWidth(100);
            }else if(i==8){
                column.setPreferredWidth(70);
            }else if(i==9){
                column.setPreferredWidth(100);
            }else if(i==10){
                column.setPreferredWidth(40);
            }
        }
        warna.kolom=0;
        tbKamar.setDefaultRenderer(Object.class,warna);
        TCari.setDocument(new batasInput((byte)100).getKata(TCari));
        if(koneksiDB.CARICEPAT().equals("aktif")){
            TCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
                @Override
                public void changedUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
            });
        }
        
        tabModeObatRacikan=new DefaultTableModel(null,new Object[]{
                "No","Nama Racikan","Kode Racik","Metode Racik","Jml.Racik",
                "Aturan Pakai","Keterangan"
            }){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = true;
                if ((colIndex==0)||(colIndex==2)||(colIndex==3)) {
                    a=false;
                }
                return a;
             }
             Class[] types = new Class[] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };

        tbObatRacikan.setModel(tabModeObatRacikan);
        tbObatRacikan.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbObatRacikan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);        
        
        for (i = 0; i < 7; i++) {
            TableColumn column = tbObatRacikan.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(25);
            }else if(i==1){
                column.setPreferredWidth(250);
            }else if(i==2){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==3){
                column.setPreferredWidth(100);
            }else if(i==4){
                column.setPreferredWidth(60);
            }else if(i==5){
                column.setPreferredWidth(200);
            }else if(i==6){
                column.setPreferredWidth(250);
            }
        }

        warna2.kolom=4;
        tbObatRacikan.setDefaultRenderer(Object.class,warna2);
        
        tabModeDetailObatRacikan=new DefaultTableModel(null,new Object[]{
                "No","Kode Barang","Nama Barang","Satuan","Harga(Rp)","H.Beli",
                "Jenis Obat","Stok","Kps","P1","/","P2","Kandungan","Jml",
                "Embal","Tuslah","I.F.","Kategori","Golongan","No.Batch","No.Faktur","Kadaluarsa"
            }){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if ((colIndex==9)||(colIndex==11)||(colIndex==12)||(colIndex==13)||(colIndex==14)||(colIndex==15)||(colIndex==19)||(colIndex==20)) {
                    a=true;
                }
                return a;
             }
             Class[] types = new Class[] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Double.class, java.lang.Double.class, java.lang.Object.class,
                java.lang.Double.class, java.lang.Double.class, java.lang.Double.class,
                java.lang.Object.class, java.lang.Double.class, java.lang.Object.class,
                java.lang.Double.class, java.lang.Double.class, java.lang.Double.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };

        tbDetailObatRacikan.setModel(tabModeDetailObatRacikan);
        tbDetailObatRacikan.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbDetailObatRacikan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);        
        
        for (i = 0; i < 22; i++) {
            TableColumn column = tbDetailObatRacikan.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(25);
            }else if(i==1){
                column.setPreferredWidth(75);
            }else if(i==2){
                column.setPreferredWidth(200);
            }else if(i==3){
                column.setPreferredWidth(45);
            }else if(i==4){
                column.setPreferredWidth(85);
            }else if(i==5){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==6){
                column.setPreferredWidth(85);
            }else if(i==7){
                column.setPreferredWidth(40);
            }else if(i==8){
                column.setPreferredWidth(40);
            }else if(i==9){
                column.setPreferredWidth(40);
            }else if(i==10){
                column.setPreferredWidth(5);
            }else if(i==11){
                column.setPreferredWidth(40);
            }else if(i==12){
                column.setPreferredWidth(70);
            }else if(i==13){
                column.setPreferredWidth(40);
            }else if(i==14){
                column.setPreferredWidth(40);
            }else if(i==15){
                column.setPreferredWidth(50);
            }else if(i==16){
                column.setPreferredWidth(80);
            }else if(i==17){
                column.setPreferredWidth(80);
            }else if(i==18){
                column.setPreferredWidth(80);
            }else if(i==19){
                column.setPreferredWidth(70);
            }else if(i==20){
                column.setPreferredWidth(100);
            }else if(i==21){
                column.setPreferredWidth(65);
            }
        }

        warna3.kolom=12;
        tbDetailObatRacikan.setDefaultRenderer(Object.class,warna3);
        
        TCari.setDocument(new batasInput((byte)100).getKata(TCari));               
        if(koneksiDB.CARICEPAT().equals("aktif")){
            TCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        BtnCariActionPerformed(null);
                    }
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        BtnCariActionPerformed(null);
                    }
                }
                @Override
                public void changedUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        BtnCariActionPerformed(null);
                    }
                }
            });
        }
        
        bangsal.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(bangsal.getTable().getSelectedRow()!= -1){                   
                    kdgudang.setText(bangsal.getTable().getValueAt(bangsal.getTable().getSelectedRow(),0).toString());
                    nmgudang.setText(bangsal.getTable().getValueAt(bangsal.getTable().getSelectedRow(),1).toString());
                }   
                kdgudang.requestFocus();
                tampil();
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        
        aturan.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(aturan.getTable().getSelectedRow()!= -1){
                    tbKamar.setValueAt(aturan.getTable().getValueAt(aturan.getTable().getSelectedRow(),0).toString(),tbKamar.getSelectedRow(),1);
                }
                tbKamar.requestFocus();
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        
        metoderacik.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(metoderacik.getTable().getSelectedRow()!= -1){  
                    tbObatRacikan.setValueAt(metoderacik.getTable().getValueAt(metoderacik.getTable().getSelectedRow(),1).toString(),tbObatRacikan.getSelectedRow(),2);
                    tbObatRacikan.setValueAt(metoderacik.getTable().getValueAt(metoderacik.getTable().getSelectedRow(),2).toString(),tbObatRacikan.getSelectedRow(),3);
                }  
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        
        metoderacik.getTable().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_SPACE){
                    metoderacik.dispose();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        }); 
        
        try {
            aktifkanbatch = koneksiDB.AKTIFKANBATCHOBAT();
        } catch (Exception e) {
            System.out.println("E : "+e);
            aktifkanbatch = "no";
        }

        try {
            hppfarmasi = koneksiDB.HPPFARMASI();
        } catch (Exception e) {
            hppfarmasi = "dasar";
        }
    }
    


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Popup = new javax.swing.JPopupMenu();
        ppBersihkan = new javax.swing.JMenuItem();
        TNoRw = new widget.TextBox();
        TKdPny = new widget.TextBox();
        Tanggal = new widget.TextBox();
        Jam = new widget.TextBox();
        KdPj = new widget.TextBox();
        kelas = new widget.TextBox();
        TNoRM = new widget.TextBox();
        TPasien = new widget.TextBox();
        kdgudang = new widget.TextBox();
        internalFrame1 = new widget.InternalFrame();
        panelisi3 = new widget.panelisi();
        label21 = new widget.Label();
        nmgudang = new widget.TextBox();
        BtnGudang = new widget.Button();
        label9 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        BtnAll = new widget.Button();
        BtnSeek5 = new widget.Button();
        BtnSimpan = new widget.Button();
        label12 = new widget.Label();
        Jeniskelas = new widget.ComboBox();
        BtnKeluar = new widget.Button();
        BtnHapus = new widget.Button();
        BtnTambah1 = new widget.Button();
        TabRawat = new javax.swing.JTabbedPane();
        Scroll = new widget.ScrollPane();
        tbKamar = new widget.Table();
        jPanel3 = new javax.swing.JPanel();
        Scroll2 = new widget.ScrollPane();
        tbObatRacikan = new widget.Table();
        Scroll3 = new widget.ScrollPane();
        tbDetailObatRacikan = new widget.Table();

        Popup.setName("Popup"); // NOI18N

        ppBersihkan.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppBersihkan.setForeground(new java.awt.Color(50, 50, 50));
        ppBersihkan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        ppBersihkan.setText("Bersihkan Jumlah");
        ppBersihkan.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppBersihkan.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppBersihkan.setName("ppBersihkan"); // NOI18N
        ppBersihkan.setPreferredSize(new java.awt.Dimension(200, 25));
        ppBersihkan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppBersihkanActionPerformed(evt);
            }
        });
        Popup.add(ppBersihkan);

        TNoRw.setHighlighter(null);
        TNoRw.setName("TNoRw"); // NOI18N

        TKdPny.setName("TKdPny"); // NOI18N

        Tanggal.setHighlighter(null);
        Tanggal.setName("Tanggal"); // NOI18N

        Jam.setHighlighter(null);
        Jam.setName("Jam"); // NOI18N

        KdPj.setHighlighter(null);
        KdPj.setName("KdPj"); // NOI18N
        KdPj.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KdPjKeyPressed(evt);
            }
        });

        kelas.setHighlighter(null);
        kelas.setName("kelas"); // NOI18N
        kelas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kelasKeyPressed(evt);
            }
        });

        TNoRM.setHighlighter(null);
        TNoRM.setName("TNoRM"); // NOI18N

        TPasien.setHighlighter(null);
        TPasien.setName("TPasien"); // NOI18N

        kdgudang.setName("kdgudang"); // NOI18N
        kdgudang.setPreferredSize(new java.awt.Dimension(45, 23));
        kdgudang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kdgudangKeyPressed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Input Resep Pulang ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        panelisi3.setName("panelisi3"); // NOI18N
        panelisi3.setPreferredSize(new java.awt.Dimension(100, 43));
        panelisi3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 4, 9));

        label21.setText("Asal Stok :");
        label21.setName("label21"); // NOI18N
        label21.setPreferredSize(new java.awt.Dimension(60, 23));
        panelisi3.add(label21);

        nmgudang.setEditable(false);
        nmgudang.setName("nmgudang"); // NOI18N
        nmgudang.setPreferredSize(new java.awt.Dimension(130, 23));
        panelisi3.add(nmgudang);

        BtnGudang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnGudang.setMnemonic('2');
        BtnGudang.setToolTipText("Alt+2");
        BtnGudang.setName("BtnGudang"); // NOI18N
        BtnGudang.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnGudang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnGudangActionPerformed(evt);
            }
        });
        panelisi3.add(BtnGudang);

        label9.setText("Key Word :");
        label9.setName("label9"); // NOI18N
        label9.setPreferredSize(new java.awt.Dimension(68, 23));
        panelisi3.add(label9);

        TCari.setToolTipText("Alt+C");
        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(150, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKeyPressed(evt);
            }
        });
        panelisi3.add(TCari);

        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari.setMnemonic('1');
        BtnCari.setToolTipText("Alt+1");
        BtnCari.setName("BtnCari"); // NOI18N
        BtnCari.setPreferredSize(new java.awt.Dimension(28, 23));
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
        panelisi3.add(BtnCari);

        BtnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll.setMnemonic('2');
        BtnAll.setToolTipText("Alt+2");
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

        BtnSeek5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/011.png"))); // NOI18N
        BtnSeek5.setMnemonic('4');
        BtnSeek5.setToolTipText("Alt+4");
        BtnSeek5.setName("BtnSeek5"); // NOI18N
        BtnSeek5.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnSeek5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSeek5ActionPerformed(evt);
            }
        });
        BtnSeek5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnSeek5KeyPressed(evt);
            }
        });
        panelisi3.add(BtnSeek5);

        BtnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnSimpan.setMnemonic('S');
        BtnSimpan.setToolTipText("Alt+S");
        BtnSimpan.setName("BtnSimpan"); // NOI18N
        BtnSimpan.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSimpanActionPerformed(evt);
            }
        });
        panelisi3.add(BtnSimpan);

        label12.setText("Tarif :");
        label12.setName("label12"); // NOI18N
        label12.setPreferredSize(new java.awt.Dimension(40, 23));
        panelisi3.add(label12);

        Jeniskelas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Kelas 1", "Kelas 2", "Kelas 3", "Utama", "VIP", "VVIP", "Beli Luar", "Karyawan", "Harga Beli" }));
        Jeniskelas.setName("Jeniskelas"); // NOI18N
        Jeniskelas.setPreferredSize(new java.awt.Dimension(100, 23));
        Jeniskelas.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                JeniskelasItemStateChanged(evt);
            }
        });
        Jeniskelas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JeniskelasKeyPressed(evt);
            }
        });
        panelisi3.add(Jeniskelas);

        BtnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar.setMnemonic('5');
        BtnKeluar.setToolTipText("Alt+5");
        BtnKeluar.setName("BtnKeluar"); // NOI18N
        BtnKeluar.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluarActionPerformed(evt);
            }
        });
        panelisi3.add(BtnKeluar);

        BtnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        BtnHapus.setMnemonic('H');
        BtnHapus.setToolTipText("Alt+H");
        BtnHapus.setName("BtnHapus"); // NOI18N
        BtnHapus.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnHapusActionPerformed(evt);
            }
        });
        panelisi3.add(BtnHapus);

        BtnTambah1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        BtnTambah1.setMnemonic('3');
        BtnTambah1.setToolTipText("Alt+3");
        BtnTambah1.setName("BtnTambah1"); // NOI18N
        BtnTambah1.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnTambah1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnTambah1ActionPerformed(evt);
            }
        });
        panelisi3.add(BtnTambah1);

        internalFrame1.add(panelisi3, java.awt.BorderLayout.PAGE_END);

        TabRawat.setBackground(new java.awt.Color(255, 255, 253));
        TabRawat.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(241, 246, 236)));
        TabRawat.setForeground(new java.awt.Color(50, 50, 50));
        TabRawat.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        TabRawat.setName("TabRawat"); // NOI18N
        TabRawat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabRawatMouseClicked(evt);
            }
        });

        Scroll.setComponentPopupMenu(Popup);
        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);

        tbKamar.setAutoCreateRowSorter(true);
        tbKamar.setComponentPopupMenu(Popup);
        tbKamar.setName("tbKamar"); // NOI18N
        tbKamar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbKamarMouseClicked(evt);
            }
        });
        tbKamar.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tbKamarPropertyChange(evt);
            }
        });
        tbKamar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbKamarKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbKamarKeyReleased(evt);
            }
        });
        Scroll.setViewportView(tbKamar);

        TabRawat.addTab("Umum", Scroll);

        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(300, 102));
        jPanel3.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        Scroll2.setName("Scroll2"); // NOI18N
        Scroll2.setOpaque(true);
        Scroll2.setPreferredSize(new java.awt.Dimension(454, 90));

        tbObatRacikan.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbObatRacikan.setName("tbObatRacikan"); // NOI18N
        tbObatRacikan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbObatRacikanKeyPressed(evt);
            }
        });
        Scroll2.setViewportView(tbObatRacikan);

        jPanel3.add(Scroll2, java.awt.BorderLayout.PAGE_START);

        Scroll3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        Scroll3.setName("Scroll3"); // NOI18N
        Scroll3.setOpaque(true);

        tbDetailObatRacikan.setAutoCreateRowSorter(true);
        tbDetailObatRacikan.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbDetailObatRacikan.setName("tbDetailObatRacikan"); // NOI18N
        tbDetailObatRacikan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbDetailObatRacikanMouseClicked(evt);
            }
        });
        tbDetailObatRacikan.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tbDetailObatRacikanPropertyChange(evt);
            }
        });
        tbDetailObatRacikan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbDetailObatRacikanKeyPressed(evt);
            }
        });
        Scroll3.setViewportView(tbDetailObatRacikan);

        jPanel3.add(Scroll3, java.awt.BorderLayout.CENTER);

        TabRawat.addTab("Racikan", jPanel3);

        internalFrame1.add(TabRawat, java.awt.BorderLayout.CENTER);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void TCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            BtnCariActionPerformed(null);
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            tbKamar.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            BtnCari.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            BtnKeluar.requestFocus();
        }
}//GEN-LAST:event_TCariKeyPressed

    private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
        tampil();
}//GEN-LAST:event_BtnCariActionPerformed

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnCariActionPerformed(null);
        }else{
            Valid.pindah(evt, TCari, BtnAll);
        }
}//GEN-LAST:event_BtnCariKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        TCari.setText("");
        tampil();
}//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnAllActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnCari, TCari);
        }
}//GEN-LAST:event_BtnAllKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
    }//GEN-LAST:event_BtnKeluarActionPerformed

private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
    if(aktifkanbatch.equals("yes")){
        index=0;
        jml=tbKamar.getRowCount();
        for(i=0;i<jml;i++){
            if((Valid.SetAngka(tbKamar.getValueAt(i,0).toString())>0)&&(tbKamar.getValueAt(i,8).toString().trim().equals("")||tbKamar.getValueAt(i,9).toString().trim().equals(""))){
                index++;
            }
        }
    }    
    if(TNoRw.getText().trim().equals("")||TKdPny.getText().trim().equals("")){
        Valid.textKosong(TCari,"Data");
    }else if(aktifkanbatch.equals("yes")&&(index>0)){
        Valid.textKosong(TCari,"No.Batch/No.Faktur");
    }else if(kdgudang.getText().equals("")){
        Valid.textKosong(TCari,"Lokasi");
    }else{
        Sequel.AutoComitFalse();
        sukses=true;

        for(i=0;i<tbKamar.getRowCount();i++){
            if(Valid.SetAngka(tbKamar.getValueAt(i,0).toString())>0){
                if(Sequel.menyimpantf("resep_pulang","?,?,?,?,?,?,?,?,?,?,?","data",11,new String[]{
                        TNoRw.getText(),tbKamar.getValueAt(i,2).toString(),tbKamar.getValueAt(i,0).toString(),
                        tbKamar.getValueAt(i,6).toString(),""+Double.parseDouble(tbKamar.getValueAt(i,6).toString())*Double.parseDouble(tbKamar.getValueAt(i,0).toString()),
                        tbKamar.getValueAt(i,1).toString(),Tanggal.getText(),Jam.getText(),kdgudang.getText(),
                        tbKamar.getValueAt(i,8).toString(),tbKamar.getValueAt(i,9).toString()
                    })==true){
                        if(aktifkanbatch.equals("yes")){
                            Sequel.mengedit3("data_batch","no_batch=? and kode_brng=? and no_faktur=?","sisa=sisa-?",4,new String[]{
                                ""+tabMode.getValueAt(i,0).toString(),tabMode.getValueAt(i,8).toString(),tabMode.getValueAt(i,2).toString(),tabMode.getValueAt(i,9).toString()
                            });
                            Trackobat.catatRiwayat(tbKamar.getValueAt(i,2).toString(),0,Valid.SetAngka(tbKamar.getValueAt(i,0).toString()),"Resep Pulang",akses.getkode(),kdgudang.getText(),"Simpan",tbKamar.getValueAt(i,8).toString(),tbKamar.getValueAt(i,9).toString(),TNoRw.getText()+" "+TNoRM.getText()+" "+TPasien.getText());
                            Sequel.menyimpan("gudangbarang","'"+tbKamar.getValueAt(i,2).toString()+"','"+kdgudang.getText()+"','-"+tbKamar.getValueAt(i,0).toString()+"','"+tabMode.getValueAt(i,8).toString()+"','"+tabMode.getValueAt(i,9).toString()+"'",
                                         "stok=stok-'"+tbKamar.getValueAt(i,0).toString()+"'","kode_brng='"+tbKamar.getValueAt(i,2).toString()+"' and kd_bangsal='"+kdgudang.getText()+"' and no_batch='"+tabMode.getValueAt(i,8).toString()+"' and no_faktur='"+tabMode.getValueAt(i,9).toString()+"'");
                        }else{
                            Trackobat.catatRiwayat(tbKamar.getValueAt(i,2).toString(),0,Valid.SetAngka(tbKamar.getValueAt(i,0).toString()),"Resep Pulang",akses.getkode(),kdgudang.getText(),"Simpan","","",TNoRw.getText()+" "+TNoRM.getText()+" "+TPasien.getText());
                            Sequel.menyimpan("gudangbarang","'"+tbKamar.getValueAt(i,2).toString()+"','"+kdgudang.getText()+"','-"+tbKamar.getValueAt(i,0).toString()+"','',''",
                                         "stok=stok-'"+tbKamar.getValueAt(i,0).toString()+"'","kode_brng='"+tbKamar.getValueAt(i,2).toString()+"' and kd_bangsal='"+kdgudang.getText()+"' and no_batch='' and no_faktur=''");
                        }

                }else{
                    sukses=false;
                }
            }
        }

        // Simpan racikan ke resep_pulang dan kurangi stok
        if(!nopermintaan.equals("")){
            for(i=0;i<tbDetailObatRacikan.getRowCount();i++){
                if(Valid.SetAngka(tbDetailObatRacikan.getValueAt(i,13).toString())>0){
                    // Cari aturan pakai dari header racikan
                    String aturanPakai = "";
                    String noRacik = tbDetailObatRacikan.getValueAt(i,0).toString();
                    for(int j=0;j<tbObatRacikan.getRowCount();j++){
                        if(tbObatRacikan.getValueAt(j,0).toString().equals(noRacik)){
                            aturanPakai = tbObatRacikan.getValueAt(j,5).toString();
                            break;
                        }
                    }

                    // Simpan ke resep_pulang
                    if(Sequel.menyimpantf("resep_pulang","?,?,?,?,?,?,?,?,?,?,?","data",11,new String[]{
                            TNoRw.getText(),tbDetailObatRacikan.getValueAt(i,1).toString(),tbDetailObatRacikan.getValueAt(i,13).toString(),
                            tbDetailObatRacikan.getValueAt(i,4).toString(),""+Double.parseDouble(tbDetailObatRacikan.getValueAt(i,4).toString())*Double.parseDouble(tbDetailObatRacikan.getValueAt(i,13).toString()),
                            aturanPakai,Tanggal.getText(),Jam.getText(),kdgudang.getText(),
                            tbDetailObatRacikan.getValueAt(i,19).toString(),tbDetailObatRacikan.getValueAt(i,20).toString()
                        })==true){
                            // Kurangi stok racikan
                            if(aktifkanbatch.equals("yes")){
                                Sequel.mengedit3("data_batch","no_batch=? and kode_brng=? and no_faktur=?","sisa=sisa-?",4,new String[]{
                                    ""+tbDetailObatRacikan.getValueAt(i,13).toString(),tbDetailObatRacikan.getValueAt(i,19).toString(),tbDetailObatRacikan.getValueAt(i,1).toString(),tbDetailObatRacikan.getValueAt(i,20).toString()
                                });
                                Trackobat.catatRiwayat(tbDetailObatRacikan.getValueAt(i,1).toString(),0,Valid.SetAngka(tbDetailObatRacikan.getValueAt(i,13).toString()),"Resep Pulang",akses.getkode(),kdgudang.getText(),"Simpan",tbDetailObatRacikan.getValueAt(i,19).toString(),tbDetailObatRacikan.getValueAt(i,20).toString(),TNoRw.getText()+" "+TNoRM.getText()+" "+TPasien.getText());
                                Sequel.menyimpan("gudangbarang","'"+tbDetailObatRacikan.getValueAt(i,1).toString()+"','"+kdgudang.getText()+"','-"+tbDetailObatRacikan.getValueAt(i,13).toString()+"','"+tbDetailObatRacikan.getValueAt(i,19).toString()+"','"+tbDetailObatRacikan.getValueAt(i,20).toString()+"'",
                                             "stok=stok-'"+tbDetailObatRacikan.getValueAt(i,13).toString()+"'","kode_brng='"+tbDetailObatRacikan.getValueAt(i,1).toString()+"' and kd_bangsal='"+kdgudang.getText()+"' and no_batch='"+tbDetailObatRacikan.getValueAt(i,19).toString()+"' and no_faktur='"+tbDetailObatRacikan.getValueAt(i,20).toString()+"'");
                            }else{
                                Trackobat.catatRiwayat(tbDetailObatRacikan.getValueAt(i,1).toString(),0,Valid.SetAngka(tbDetailObatRacikan.getValueAt(i,13).toString()),"Resep Pulang",akses.getkode(),kdgudang.getText(),"Simpan","","",TNoRw.getText()+" "+TNoRM.getText()+" "+TPasien.getText());
                                Sequel.menyimpan("gudangbarang","'"+tbDetailObatRacikan.getValueAt(i,1).toString()+"','"+kdgudang.getText()+"','-"+tbDetailObatRacikan.getValueAt(i,13).toString()+"','',''",
                                             "stok=stok-'"+tbDetailObatRacikan.getValueAt(i,13).toString()+"'","kode_brng='"+tbDetailObatRacikan.getValueAt(i,1).toString()+"' and kd_bangsal='"+kdgudang.getText()+"' and no_batch='' and no_faktur=''");
                            }

                    }else{
                        sukses=false;
                    }
                }
            }
        }

        if(!nopermintaan.equals("")){
            Sequel.mengedit("permintaan_resep_pulang","no_permintaan='"+nopermintaan+"'","tgl_validasi='"+Tanggal.getText()+"',jam_validasi='"+Jam.getText()+"',status='Sudah'");
        }

        if(sukses==true){
            Sequel.Commit();
            
            Map<String, Object> param = new HashMap<>();  
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());
            pilihanetiket = (String)JOptionPane.showInputDialog(null,"Silahkan pilih cetak aturan pakai..!!","Cetak Aturan Pakai",JOptionPane.QUESTION_MESSAGE,null,new Object[]{"Cetak Aturan Pakai Model 1","Cetak Aturan Pakai Model 2","Cetak Aturan Pakai Model 3","Cetak Label Obat","Cetak Aturan Pakai Model 1 & Cetak Label Obat","Cetak Aturan Pakai Model 2 & Cetak Label Obat","Cetak Aturan Pakai Model 3 & Cetak Label Obat"},"Cetak Aturan Pakai Model 1");
            switch (pilihanetiket) {
                case "Cetak Aturan Pakai Model 1": 
                    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    if(Sequel.cariInteger(
                            "select count(*) from resep_pulang where resep_pulang.no_rawat=? and resep_pulang.dosis<>''",TNoRw.getText())>0){
                        param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
                        Valid.MyReportqry("rptItemResepPulang.jasper","report","::[ Aturan Pakai Obat ]::",
                            "select resep_pulang.no_rawat,resep_pulang.tanggal, "+
                            "reg_periksa.no_rkm_medis,pasien.nm_pasien,databarang.nama_brng,"+
                            "resep_pulang.dosis,resep_pulang.jml_barang,kodesatuan.satuan,DATE_FORMAT(pasien.tgl_lahir,'%d-%m-%Y')as tgl_lahir "+
                            "from resep_pulang inner join reg_periksa on resep_pulang.no_rawat=reg_periksa.no_rawat "+
                            "inner join databarang on resep_pulang.kode_brng=databarang.kode_brng "+
                            "inner join kodesatuan on databarang.kode_sat=kodesatuan.kode_sat "+
                            "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                            "where resep_pulang.no_rawat='"+TNoRw.getText()+"' and resep_pulang.dosis<>''",param);
                    }            
                    this.setCursor(Cursor.getDefaultCursor());
                    break;
                case "Cetak Aturan Pakai Model 2": 
                    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    if(Sequel.cariInteger(
                            "select count(*) from resep_pulang where resep_pulang.no_rawat=? and resep_pulang.dosis<>''",TNoRw.getText())>0){
                        param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
                        Valid.MyReportqry("rptItemResepPulang2.jasper","report","::[ Aturan Pakai Obat ]::",
                            "select resep_pulang.no_rawat,resep_pulang.tanggal,jenis.nama,"+
                            "reg_periksa.no_rkm_medis,pasien.nm_pasien,databarang.nama_brng,"+
                            "resep_pulang.dosis,resep_pulang.jml_barang,kodesatuan.satuan "+
                            "from resep_pulang inner join reg_periksa on resep_pulang.no_rawat=reg_periksa.no_rawat "+
                            "inner join databarang on resep_pulang.kode_brng=databarang.kode_brng "+
                            "inner join kodesatuan on databarang.kode_sat=kodesatuan.kode_sat "+
                            "inner join jenis on databarang.kdjns= jenis.kdjns "+
                            "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                            "where resep_pulang.no_rawat='"+TNoRw.getText()+"' and resep_pulang.dosis<>''",param);
                    }           
                    this.setCursor(Cursor.getDefaultCursor());
                    break;
                case "Cetak Aturan Pakai Model 3": 
                    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    if(Sequel.cariInteger(
                            "select count(*) from resep_pulang where resep_pulang.no_rawat=? and resep_pulang.dosis<>''",TNoRw.getText())>0){
                        param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
                        Valid.MyReportqry("rptItemResepPulang3.jasper","report","::[ Aturan Pakai Obat ]::",
                            "select resep_pulang.no_rawat,resep_pulang.tanggal, "+
                            "reg_periksa.no_rkm_medis,pasien.nm_pasien,databarang.nama_brng,"+
                            "resep_pulang.dosis,resep_pulang.jml_barang,kodesatuan.satuan "+
                            "from resep_pulang inner join reg_periksa on resep_pulang.no_rawat=reg_periksa.no_rawat "+
                            "inner join databarang on resep_pulang.kode_brng=databarang.kode_brng "+
                            "inner join kodesatuan on databarang.kode_sat=kodesatuan.kode_sat "+
                            "inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                            "where resep_pulang.no_rawat='"+TNoRw.getText()+"' and resep_pulang.dosis<>''",param);
                    }             
                    this.setCursor(Cursor.getDefaultCursor());
                    break;
                default:
                    break;
            }
        }else{
            sukses=false;
            JOptionPane.showMessageDialog(null,"Terjadi kesalahan saat pemrosesan data, transaksi dibatalkan.\nPeriksa kembali data sebelum melanjutkan menyimpan..!!");
            Sequel.RollBack();
        }
        Sequel.AutoComitTrue();
        if(sukses==true){
            dispose();
            ppBersihkanActionPerformed(null);
        }
    }
}//GEN-LAST:event_BtnSimpanActionPerformed

private void BtnSeek5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSeek5ActionPerformed
    DlgCariKonversi carikonversi=new DlgCariKonversi(null,false);
    carikonversi.setLocationRelativeTo(internalFrame1);
    carikonversi.setVisible(true);
}//GEN-LAST:event_BtnSeek5ActionPerformed

private void BtnSeek5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSeek5KeyPressed
// TODO add your handling code here:
}//GEN-LAST:event_BtnSeek5KeyPressed

private void ppBersihkanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppBersihkanActionPerformed
            for(i=0;i<tbKamar.getRowCount();i++){
                tbKamar.setValueAt("",i,0);
            }
            for(i=0;i<tbObatRacikan.getRowCount();i++){
                tbObatRacikan.setValueAt("",i,0);
                tbObatRacikan.setValueAt("",i,1);
                tbObatRacikan.setValueAt("",i,2);
                tbObatRacikan.setValueAt("",i,3);
                tbObatRacikan.setValueAt(0,i,4);
                tbObatRacikan.setValueAt("",i,5);
                tbObatRacikan.setValueAt("",i,6);
            }
            Valid.tabelKosong(tabModeDetailObatRacikan);
}//GEN-LAST:event_ppBersihkanActionPerformed

private void JeniskelasItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_JeniskelasItemStateChanged
       tampil(); 
}//GEN-LAST:event_JeniskelasItemStateChanged

private void JeniskelasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JeniskelasKeyPressed
        Valid.pindah(evt, TCari,BtnKeluar);
}//GEN-LAST:event_JeniskelasKeyPressed

    private void tbKamarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbKamarKeyPressed
        if(tabMode.getRowCount()!=0){
            if(evt.getKeyCode()==KeyEvent.VK_SPACE){
                if(tbKamar.getSelectedColumn()!=1){
                    dispose();
                }
            }else if(evt.getKeyCode()==KeyEvent.VK_SHIFT){
                TCari.setText("");
                TCari.requestFocus();
            }else if(evt.getKeyCode()==KeyEvent.VK_RIGHT){
                i=tbKamar.getSelectedColumn();
                if(i==1){
                    aturan.setSize(internalFrame1.getWidth(),internalFrame1.getHeight());
                    aturan.setLocationRelativeTo(internalFrame1);
                    aturan.setVisible(true);
                }
            }
        }
    }//GEN-LAST:event_tbKamarKeyPressed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        emptTeks();
    }//GEN-LAST:event_formWindowActivated

    private void KdPjKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KdPjKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_KdPjKeyPressed

    private void kelasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kelasKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_kelasKeyPressed

    private void tbKamarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbKamarKeyReleased
        if(tabMode.getRowCount()!=0){
            if(evt.getKeyCode()==KeyEvent.VK_ENTER){
                try {                                     
                    getData();                     
                    TCari.setText("");
                    TCari.requestFocus();
                } catch (java.lang.NullPointerException e) {
                }
            }else if((evt.getKeyCode()==KeyEvent.VK_RIGHT)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {                                     
                    getData();           
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
    }//GEN-LAST:event_tbKamarKeyReleased

    private void tbKamarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbKamarMouseClicked
        if(tabMode.getRowCount()!=0){
            try {                  
                getData();
            } catch (java.lang.NullPointerException e) {
            }
        }
    }//GEN-LAST:event_tbKamarMouseClicked

    private void tbKamarPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tbKamarPropertyChange
        if(this.isVisible()==true){
              getData();
        }
    }//GEN-LAST:event_tbKamarPropertyChange

    private void kdgudangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kdgudangKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            nmgudang.setText(bangsal.tampil3(kdgudang.getText())); 
            tampil();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            nmgudang.setText(bangsal.tampil3(kdgudang.getText())); 
            tampil();
            BtnKeluar.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            nmgudang.setText(bangsal.tampil3(kdgudang.getText())); 
            tampil();
            BtnSimpan.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            BtnGudangActionPerformed(null);
        }
    }//GEN-LAST:event_kdgudangKeyPressed

    private void BtnGudangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnGudangActionPerformed
        bangsal.isCek();
        bangsal.emptTeks();
        bangsal.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        bangsal.setLocationRelativeTo(internalFrame1);
        bangsal.setAlwaysOnTop(false);
        bangsal.setVisible(true);
    }//GEN-LAST:event_BtnGudangActionPerformed

    private void tbObatRacikanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbObatRacikanKeyPressed
        if(tbObatRacikan.getRowCount()!=0){
            i=tbObatRacikan.getSelectedColumn();
            if(evt.getKeyCode()==KeyEvent.VK_RIGHT){
                if(i==5){
                    akses.setform("DlgCariObat");
                    aturan.setSize(internalFrame1.getWidth(),internalFrame1.getHeight());
                    aturan.setLocationRelativeTo(internalFrame1);
                    aturan.setVisible(true);
                }else if(i==3){
                    if(tbObatRacikan.getValueAt(tbObatRacikan.getSelectedRow(),1).equals("")){
                        JOptionPane.showMessageDialog(null,"Silahkan masukkan nama racikan..!!");
                        tbObatRacikan.requestFocus();
                    }else{
                        metoderacik.isCek();
                        metoderacik.setSize(internalFrame1.getWidth(),internalFrame1.getHeight());
                        metoderacik.setLocationRelativeTo(internalFrame1);
                        metoderacik.setVisible(true);
                    }
                }
            }else if(evt.getKeyCode()==KeyEvent.VK_SHIFT){
                if((i==4)){
                    TCari.requestFocus();
                }
            }else if(evt.getKeyCode()==KeyEvent.VK_ENTER){
                if((i==6)){
                    if(tbObatRacikan.getSelectedRow()!= -1){
                        if(tbObatRacikan.getValueAt(tbObatRacikan.getSelectedRow(),0).toString().equals("")||
                            tbObatRacikan.getValueAt(tbObatRacikan.getSelectedRow(),1).toString().equals("")||
                            tbObatRacikan.getValueAt(tbObatRacikan.getSelectedRow(),2).toString().equals("")||
                            tbObatRacikan.getValueAt(tbObatRacikan.getSelectedRow(),3).toString().equals("")||
                            tbObatRacikan.getValueAt(tbObatRacikan.getSelectedRow(),4).toString().equals("")||
                            tbObatRacikan.getValueAt(tbObatRacikan.getSelectedRow(),5).toString().equals("")){
                            JOptionPane.showMessageDialog(null,"Silahkan lengkapi data racikan..!!");
                        }else{
                            tampildetailracikanobat();
                            TCari.requestFocus();
                        }
                    }else{
                        JOptionPane.showMessageDialog(null,"Silahkan pilih racikan..!!");
                    }
                }
            }
        }
    }//GEN-LAST:event_tbObatRacikanKeyPressed

    private void tbDetailObatRacikanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbDetailObatRacikanMouseClicked
        if(tbKamar.getRowCount()!=0){
            try {
                getDatadetailobatracikan();
            } catch (Exception e) {
            }
        }
    }//GEN-LAST:event_tbDetailObatRacikanMouseClicked

    private void tbDetailObatRacikanPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tbDetailObatRacikanPropertyChange
        if(this.isVisible()==true){
            getDatadetailobatracikan();
        }
    }//GEN-LAST:event_tbDetailObatRacikanPropertyChange

    private void tbDetailObatRacikanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbDetailObatRacikanKeyPressed
        if(tbObatRacikan.getSelectedRow()!= -1){
            if(tbDetailObatRacikan.getRowCount()!=0){
                i=tbDetailObatRacikan.getSelectedColumn();
                if(evt.getKeyCode()==KeyEvent.VK_ENTER){
                    try {
                        if(i==11){
                            try {
                                if(tbDetailObatRacikan.getValueAt(tbDetailObatRacikan.getSelectedRow(),11).toString().equals("0")||tbDetailObatRacikan.getValueAt(tbDetailObatRacikan.getSelectedRow(),11).toString().equals("")||tbDetailObatRacikan.getValueAt(tbDetailObatRacikan.getSelectedRow(),11).toString().equals("0.0")||tbDetailObatRacikan.getValueAt(tbDetailObatRacikan.getSelectedRow(),11).toString().equals("0,0")) {
                                    tbDetailObatRacikan.setValueAt(embalase,tbDetailObatRacikan.getSelectedRow(),11);
                                }
                            } catch (Exception e) {
                                tbDetailObatRacikan.setValueAt(0,tbDetailObatRacikan.getSelectedRow(),11);
                            }
                        }else if(i==12){
                            try {
                                if(tbDetailObatRacikan.getValueAt(tbDetailObatRacikan.getSelectedRow(),12).toString().equals("0")||tbDetailObatRacikan.getValueAt(tbDetailObatRacikan.getSelectedRow(),12).toString().equals("")||tbDetailObatRacikan.getValueAt(tbDetailObatRacikan.getSelectedRow(),12).toString().equals("0.0")||tbDetailObatRacikan.getValueAt(tbDetailObatRacikan.getSelectedRow(),12).toString().equals("0,0")) {
                                    tbDetailObatRacikan.setValueAt(tuslah,tbDetailObatRacikan.getSelectedRow(),12);
                                }
                            } catch (Exception e) {
                                tbDetailObatRacikan.setValueAt(0,tbDetailObatRacikan.getSelectedRow(),12);
                            }

                            TCari.setText("");
                            TCari.requestFocus();
                        }else if((i==9)||(i==10)){
                            try {
                                if(!tabModeDetailObatRacikan.getValueAt(tbDetailObatRacikan.getSelectedRow(),9).toString().equals("")){
                                    tbDetailObatRacikan.setValueAt(
                                        Valid.SetAngka8((Double.parseDouble(tbObatRacikan.getValueAt(tbObatRacikan.getSelectedRow(),4).toString())
                                            *Double.parseDouble(tbDetailObatRacikan.getValueAt(tbDetailObatRacikan.getSelectedRow(),9).toString()))
                                        /Double.parseDouble(tbDetailObatRacikan.getValueAt(tbDetailObatRacikan.getSelectedRow(),8).toString()),1)
                                    ,tbDetailObatRacikan.getSelectedRow(),10);
                                getDatadetailobatracikan();
                            }
                        } catch (Exception e) {
                            tbDetailObatRacikan.setValueAt(0,tbDetailObatRacikan.getSelectedRow(),10);
                            tbDetailObatRacikan.setValueAt(0,tbDetailObatRacikan.getSelectedRow(),11);
                            tbDetailObatRacikan.setValueAt(0,tbDetailObatRacikan.getSelectedRow(),12);
                        }

                        TCari.setText("");
                        TCari.requestFocus();
                    }else if(i==11){
                        TCari.setText("");
                        TCari.requestFocus();
                    }
                } catch (java.lang.NullPointerException e) {
                }
            }else if((evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    if((i==9)||(i==10)){
                        if(!tabModeDetailObatRacikan.getValueAt(tbDetailObatRacikan.getSelectedRow(),9).toString().equals("")){
                            tbDetailObatRacikan.setValueAt(
                                Valid.SetAngka8((Double.parseDouble(tbObatRacikan.getValueAt(tbObatRacikan.getSelectedRow(),4).toString())
                                    *Valid.SetAngka(tbDetailObatRacikan.getValueAt(row,9).toString()))
                                /Valid.SetAngka(tbDetailObatRacikan.getValueAt(row,8).toString()),1),row,10
                        );
                        getDatadetailobatracikan();
                    }
                }
            } catch (java.lang.NullPointerException e) {
            }
        }else if(evt.getKeyCode()==KeyEvent.VK_DELETE){
            if((i==9)||(i==10)){
                if(tbDetailObatRacikan.getSelectedRow()!= -1){
                    tbDetailObatRacikan.setValueAt("",tbDetailObatRacikan.getSelectedRow(),9);
                    tbDetailObatRacikan.setValueAt(0,tbDetailObatRacikan.getSelectedRow(),10);
                    tbDetailObatRacikan.setValueAt(0,tbDetailObatRacikan.getSelectedRow(),11);
                    tbDetailObatRacikan.setValueAt(0,tbDetailObatRacikan.getSelectedRow(),12);
                }
            }

        }else if(evt.getKeyCode()==KeyEvent.VK_SHIFT){
            if(i!=9){
                TCari.requestFocus();
            }
        }else if(evt.getKeyCode()==KeyEvent.VK_RIGHT){
            if((i==9)||(i==10)){
                try {
                    if(!tabModeDetailObatRacikan.getValueAt(tbDetailObatRacikan.getSelectedRow(),9).toString().equals("")){
                        tbDetailObatRacikan.setValueAt(
                            Valid.SetAngka8((Double.parseDouble(tbObatRacikan.getValueAt(tbObatRacikan.getSelectedRow(),4).toString())
                                *Valid.SetAngka(tbDetailObatRacikan.getValueAt(row,9).toString()))
                            /Valid.SetAngka(tbDetailObatRacikan.getValueAt(row,8).toString()),1),row,10
                    );
                }

                try {
                    if(tbDetailObatRacikan.getValueAt(tbDetailObatRacikan.getSelectedRow(),11).toString().equals("0")||tbDetailObatRacikan.getValueAt(tbDetailObatRacikan.getSelectedRow(),11).toString().equals("")||tbDetailObatRacikan.getValueAt(tbDetailObatRacikan.getSelectedRow(),11).toString().equals("0.0")||tbDetailObatRacikan.getValueAt(tbDetailObatRacikan.getSelectedRow(),11).toString().equals("0,0")) {
                        tbDetailObatRacikan.setValueAt(embalase,tbDetailObatRacikan.getSelectedRow(),11);
                    }
                } catch (Exception e) {
                    tbDetailObatRacikan.setValueAt(0,tbDetailObatRacikan.getSelectedRow(),11);
                }

                try {
                    if(tbDetailObatRacikan.getValueAt(tbDetailObatRacikan.getSelectedRow(),12).toString().equals("0")||tbDetailObatRacikan.getValueAt(tbDetailObatRacikan.getSelectedRow(),12).toString().equals("")||tbDetailObatRacikan.getValueAt(tbDetailObatRacikan.getSelectedRow(),12).toString().equals("0.0")||tbDetailObatRacikan.getValueAt(tbDetailObatRacikan.getSelectedRow(),12).toString().equals("0,0")) {
                        tbDetailObatRacikan.setValueAt(tuslah,tbDetailObatRacikan.getSelectedRow(),12);
                    }
                } catch (Exception e) {
                    tbDetailObatRacikan.setValueAt(0,tbDetailObatRacikan.getSelectedRow(),12);
                }
                getDatadetailobatracikan();
            } catch (Exception e) {
                tbDetailObatRacikan.setValueAt(0,tbDetailObatRacikan.getSelectedRow(),10);
                tbDetailObatRacikan.setValueAt(0,tbDetailObatRacikan.getSelectedRow(),11);
                tbDetailObatRacikan.setValueAt(0,tbDetailObatRacikan.getSelectedRow(),12);
            }
        }
        }
        }
        }else{
            JOptionPane.showMessageDialog(null,"Silahkan pilih No.Racikan terlebih dahulu");
        }
    }//GEN-LAST:event_tbDetailObatRacikanKeyPressed

    private void TabRawatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabRawatMouseClicked
        if(TabRawat.getSelectedIndex()==0){
            BtnTambah1.setVisible(false);
            BtnHapus.setVisible(false);
        }else if(TabRawat.getSelectedIndex()==1){
            BtnTambah1.setVisible(true);
            BtnHapus.setVisible(true);
        }
    }//GEN-LAST:event_TabRawatMouseClicked

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
        if(tbObatRacikan.getValueAt(tbObatRacikan.getSelectedRow(),1).equals("")&&tbObatRacikan.getValueAt(tbObatRacikan.getSelectedRow(),4).equals("")&&tbObatRacikan.getValueAt(tbObatRacikan.getSelectedRow(),5).equals("")&&tbObatRacikan.getValueAt(tbObatRacikan.getSelectedRow(),6).equals("")){
            tabModeObatRacikan.removeRow(tbObatRacikan.getSelectedRow());
        }else{
            JOptionPane.showMessageDialog(null,"Maaf sudah terisi, gak boleh dihapus..!!");
        }
    }//GEN-LAST:event_BtnHapusActionPerformed

    private void BtnTambah1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnTambah1ActionPerformed
        i=tabModeObatRacikan.getRowCount()+1;
        if(i==99){
            JOptionPane.showMessageDialog(null,"Maksimal 98 Racikan..!!");
        }else{
            tabModeObatRacikan.addRow(new Object[]{""+i,"","","","","",""});
        }
    }//GEN-LAST:event_BtnTambah1ActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            DlgInputResepPulang dialog = new DlgInputResepPulang(new javax.swing.JFrame(), true);
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
    private widget.Button BtnGudang;
    private widget.Button BtnHapus;
    private widget.Button BtnKeluar;
    private widget.Button BtnSeek5;
    private widget.Button BtnSimpan;
    private widget.Button BtnTambah1;
    private widget.TextBox Jam;
    private widget.ComboBox Jeniskelas;
    private widget.TextBox KdPj;
    private javax.swing.JPopupMenu Popup;
    private widget.ScrollPane Scroll;
    private widget.ScrollPane Scroll2;
    private widget.ScrollPane Scroll3;
    private widget.TextBox TCari;
    private widget.TextBox TKdPny;
    private widget.TextBox TNoRM;
    private widget.TextBox TNoRw;
    private widget.TextBox TPasien;
    private javax.swing.JTabbedPane TabRawat;
    private widget.TextBox Tanggal;
    private widget.InternalFrame internalFrame1;
    private javax.swing.JPanel jPanel3;
    private widget.TextBox kdgudang;
    private widget.TextBox kelas;
    private widget.Label label12;
    private widget.Label label21;
    private widget.Label label9;
    private widget.TextBox nmgudang;
    private widget.panelisi panelisi3;
    private javax.swing.JMenuItem ppBersihkan;
    private widget.Table tbDetailObatRacikan;
    private widget.Table tbKamar;
    private widget.Table tbObatRacikan;
    // End of variables declaration//GEN-END:variables

    public void tampil() {
        jml=0;
        for(i=0;i<tbKamar.getRowCount();i++){
            if(!tbKamar.getValueAt(i,0).toString().equals("")){
                jml++;
            }
        }
        
        jumlah=null;
        jumlah=new double[jml];
        harga=null;
        harga=new double[jml];
        kodebarang=null;
        kodebarang=new String[jml];
        namabarang=null;
        namabarang=new String[jml];
        kodesatuan=null;
        kodesatuan=new String[jml];
        letakbarang=null;
        letakbarang=new String[jml];
        namajenis=null;
        namajenis=new String[jml];
        dosis=null;
        dosis=new String[jml];
        nobatch=null;
        nobatch=new String[jml];
        nofaktur=null;
        nofaktur=new String[jml];
        stok=null;
        stok=new double[jml];
        
        index=0;
        for(i=0;i<tbKamar.getRowCount();i++){
            if(!tbKamar.getValueAt(i,0).toString().equals("")){
                jumlah[index]=Double.parseDouble(tbKamar.getValueAt(i,0).toString());
                dosis[index]=tbKamar.getValueAt(i,1).toString();
                kodebarang[index]=tbKamar.getValueAt(i,2).toString();
                namabarang[index]=tbKamar.getValueAt(i,3).toString();
                kodesatuan[index]=tbKamar.getValueAt(i,4).toString();
                letakbarang[index]=tbKamar.getValueAt(i,5).toString();
                harga[index]=Double.parseDouble(tbKamar.getValueAt(i,6).toString());
                namajenis[index]=tbKamar.getValueAt(i,7).toString();
                nobatch[index]=tbKamar.getValueAt(i,8).toString();
                nofaktur[index]=tbKamar.getValueAt(i,9).toString();
                stok[index]=Double.parseDouble(tbKamar.getValueAt(i,10).toString());
                index++;
            }
        }
        
        Valid.tabelKosong(tabMode);
        
        for(i=0;i<jml;i++){
            tabMode.addRow(new Object[]{jumlah[i],dosis[i],kodebarang[i],namabarang[i],kodesatuan[i],letakbarang[i],harga[i],namajenis[i],nobatch[i],nofaktur[i],stok[i]});
        }
        
        try{
            if(aktifkanbatch.equals("yes")){
                if(kenaikan>0){
                    psobat=koneksi.prepareStatement(
                            " select data_batch.kode_brng, databarang.nama_brng,jenis.nama, databarang.kode_sat,(data_batch.h_beli+(data_batch.h_beli*?)) as harga,gudangbarang.stok,"+
                            " databarang.letak_barang,data_batch.no_batch,data_batch.no_faktur from data_batch inner join databarang on data_batch.kode_brng=databarang.kode_brng inner join jenis on databarang.kdjns=jenis.kdjns "+
                            " inner join gudangbarang on gudangbarang.kode_brng=data_batch.kode_brng and gudangbarang.no_batch=data_batch.no_batch and gudangbarang.no_faktur=data_batch.no_faktur "+
                            " where gudangbarang.stok>0 and gudangbarang.kd_bangsal=? and "+
                            " (databarang.kode_brng like ? or databarang.nama_brng like ? or jenis.nama like ?) order by databarang.nama_brng");  
                }else{
                    psobat=koneksi.prepareStatement(
                            " select data_batch.kode_brng, databarang.nama_brng,jenis.nama, databarang.kode_sat,data_batch.kelas1,data_batch.kelas2,data_batch.kelas3,data_batch.utama,"+
                            " data_batch.vip,data_batch.vvip,data_batch.beliluar,data_batch.karyawan,data_batch.h_beli,databarang.letak_barang,gudangbarang.stok,data_batch.no_batch,data_batch.no_faktur "+
                            " from data_batch inner join databarang on data_batch.kode_brng=databarang.kode_brng inner join jenis on databarang.kdjns=jenis.kdjns "+
                            " inner join gudangbarang on gudangbarang.kode_brng=data_batch.kode_brng and gudangbarang.no_batch=data_batch.no_batch and gudangbarang.no_faktur=data_batch.no_faktur "+
                            " where gudangbarang.stok>0 and gudangbarang.kd_bangsal=? and "+
                            " (databarang.kode_brng like ? or databarang.nama_brng like ? or jenis.nama like ?) order by databarang.nama_brng"); 
                }   

                try {
                    if(kenaikan>0){
                        psobat.setDouble(1,kenaikan);
                        psobat.setString(2,kdgudang.getText());
                        psobat.setString(3,"%"+TCari.getText().trim()+"%");
                        psobat.setString(4,"%"+TCari.getText().trim()+"%");
                        psobat.setString(5,"%"+TCari.getText().trim()+"%");
                        rs=psobat.executeQuery();
                        while(rs.next()){
                            tabMode.addRow(new Object[]{"","",rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("harga"),100)),rs.getString("nama"),rs.getString("no_batch"),rs.getString("no_faktur"),rs.getDouble("stok")});
                        }
                    }else{
                        psobat.setString(1,kdgudang.getText());
                        psobat.setString(2,"%"+TCari.getText().trim()+"%");
                        psobat.setString(3,"%"+TCari.getText().trim()+"%");
                        psobat.setString(4,"%"+TCari.getText().trim()+"%");
                        rs=psobat.executeQuery();
                        if(Jeniskelas.getSelectedItem().equals("Kelas 1")){
                            while(rs.next()){
                                tabMode.addRow(new Object[]{"","",rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("kelas1"),100)),rs.getString("nama"),rs.getString("no_batch"),rs.getString("no_faktur"),rs.getDouble("stok")});
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("Kelas 2")){
                            while(rs.next()){
                                tabMode.addRow(new Object[]{"","",rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("kelas2"),100)),rs.getString("nama"),rs.getString("no_batch"),rs.getString("no_faktur"),rs.getDouble("stok")});
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("Kelas 3")){
                            while(rs.next()){
                                tabMode.addRow(new Object[]{"","",rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("kelas3"),100)),rs.getString("nama"),rs.getString("no_batch"),rs.getString("no_faktur"),rs.getDouble("stok")});
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("Utama")){
                            while(rs.next()){
                                tabMode.addRow(new Object[]{"","",rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("utama"),100)),rs.getString("nama"),rs.getString("no_batch"),rs.getString("no_faktur"),rs.getDouble("stok")});
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("VIP")){
                            while(rs.next()){
                                tabMode.addRow(new Object[]{"","",rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("vip"),100)),rs.getString("nama"),rs.getString("no_batch"),rs.getString("no_faktur"),rs.getDouble("stok")});
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("VVIP")){
                            while(rs.next()){
                                tabMode.addRow(new Object[]{"","",rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("vvip"),100)),rs.getString("nama"),rs.getString("no_batch"),rs.getString("no_faktur"),rs.getDouble("stok")});
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("Beli Luar")){
                            while(rs.next()){
                                tabMode.addRow(new Object[]{"","",rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("beliluar"),100)),rs.getString("nama"),rs.getString("no_batch"),rs.getString("no_faktur"),rs.getDouble("stok")});
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("Karyawan")){
                            while(rs.next()){
                                tabMode.addRow(new Object[]{"","",rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("karyawan"),100)),rs.getString("nama"),rs.getString("no_batch"),rs.getString("no_faktur"),rs.getDouble("stok")});
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("Harga Beli")){
                            while(rs.next()){
                                tabMode.addRow(new Object[]{"","",rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("h_beli"),100)),rs.getString("nama"),rs.getString("no_batch"),rs.getString("no_faktur"),rs.getDouble("stok")});
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Notif obat2 : "+e);
                } finally{
                    if(rs!=null){
                        rs.close();
                    }
                    if(psobat!=null){
                        psobat.close();
                    }
                }
            }else{
                if(kenaikan>0){
                    psobat=koneksi.prepareStatement(
                            " select databarang.kode_brng, databarang.nama_brng,jenis.nama, databarang.kode_sat,(databarang.h_beli+(databarang.h_beli*?)) as harga,gudangbarang.stok,"+
                            " databarang.letak_barang from databarang inner join jenis on databarang.kdjns=jenis.kdjns inner join gudangbarang on databarang.kode_brng=gudangbarang.kode_brng "+
                            " where gudangbarang.no_batch='' and gudangbarang.no_faktur='' and gudangbarang.stok>0 and gudangbarang.kd_bangsal=? and databarang.status='1' and "+
                            " (databarang.kode_brng like ? or databarang.nama_brng like ? or jenis.nama like ?) order by databarang.nama_brng");  
                }else{
                    psobat=koneksi.prepareStatement(
                            " select databarang.kode_brng, databarang.nama_brng,jenis.nama, databarang.kode_sat,databarang.kelas1,databarang.kelas2,databarang.kelas3,databarang.utama,"+
                            " databarang.vip,databarang.vvip,databarang.beliluar,databarang.karyawan,databarang.h_beli,databarang.letak_barang,gudangbarang.stok "+
                            " from databarang inner join jenis on databarang.kdjns=jenis.kdjns inner join gudangbarang on databarang.kode_brng=gudangbarang.kode_brng "+
                            " where gudangbarang.no_batch='' and gudangbarang.no_faktur='' and gudangbarang.stok>0 and gudangbarang.kd_bangsal=? and databarang.status='1' and "+
                            " (databarang.kode_brng like ? or databarang.nama_brng like ? or jenis.nama like ?) order by databarang.nama_brng"); 
                }   

                try {
                    if(kenaikan>0){
                        psobat.setDouble(1,kenaikan);
                        psobat.setString(2,kdgudang.getText());
                        psobat.setString(3,"%"+TCari.getText().trim()+"%");
                        psobat.setString(4,"%"+TCari.getText().trim()+"%");
                        psobat.setString(5,"%"+TCari.getText().trim()+"%");
                        rs=psobat.executeQuery();
                        while(rs.next()){
                            tabMode.addRow(new Object[]{"","",rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("harga"),100)),rs.getString("nama"),"","",rs.getDouble("stok")});
                        }
                    }else{
                        psobat.setString(1,kdgudang.getText());
                        psobat.setString(2,"%"+TCari.getText().trim()+"%");
                        psobat.setString(3,"%"+TCari.getText().trim()+"%");
                        psobat.setString(4,"%"+TCari.getText().trim()+"%");
                        rs=psobat.executeQuery();
                        if(Jeniskelas.getSelectedItem().equals("Kelas 1")){
                            while(rs.next()){
                                tabMode.addRow(new Object[]{"","",rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("kelas1"),100)),rs.getString("nama"),"","",rs.getDouble("stok")});
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("Kelas 2")){
                            while(rs.next()){
                                tabMode.addRow(new Object[]{"","",rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("kelas2"),100)),rs.getString("nama"),"","",rs.getDouble("stok")});
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("Kelas 3")){
                            while(rs.next()){
                                tabMode.addRow(new Object[]{"","",rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("kelas3"),100)),rs.getString("nama"),"","",rs.getDouble("stok")});
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("Utama")){
                            while(rs.next()){
                                tabMode.addRow(new Object[]{"","",rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("utama"),100)),rs.getString("nama"),"","",rs.getDouble("stok")});
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("VIP")){
                            while(rs.next()){
                                tabMode.addRow(new Object[]{"","",rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("vip"),100)),rs.getString("nama"),"","",rs.getDouble("stok")});
                            }
                         }else if(Jeniskelas.getSelectedItem().equals("VVIP")){
                            while(rs.next()){
                                tabMode.addRow(new Object[]{"","",rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("vvip"),100)),rs.getString("nama"),"","",rs.getDouble("stok")});
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("Beli Luar")){
                            while(rs.next()){
                                tabMode.addRow(new Object[]{"","",rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("beliluar"),100)),rs.getString("nama"),"","",rs.getDouble("stok")});
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("Karyawan")){
                            while(rs.next()){
                                tabMode.addRow(new Object[]{"","",rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("karyawan"),100)),rs.getString("nama"),"","",rs.getDouble("stok")});
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("Harga Beli")){
                            while(rs.next()){
                                tabMode.addRow(new Object[]{"","",rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("h_beli"),100)),rs.getString("nama"),"","",rs.getDouble("stok")});
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Notif obat2 : "+e);
                } finally{
                    if(rs!=null){
                        rs.close();
                    }
                    if(psobat!=null){
                        psobat.close();
                    }
                }
            } 
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
    }

    public void emptTeks() {
        TCari.setText("");
        TCari.requestFocus();
    }

    public JTable getTable(){
        return tbKamar;
    }
    
    public void isCek(){        
        BtnSimpan.setEnabled(akses.getresep_pulang());
        if(akses.getakses_depo_obat()==true){
            kdgudang.setEditable(true);
            nmgudang.setEditable(true);
            BtnGudang.setEnabled(true);
        }else{
            kdgudang.setEditable(false);
            nmgudang.setEditable(false);
            BtnGudang.setEnabled(false);
        }
    }
    
    public void setNoRm(String norwt,String norm,String pasien,String penyakit, String tanggal, String jam) {    
        aktifpcare="no";
        kdgudang.setText(akses.getkdbangsal());
        nmgudang.setText(Sequel.cariIsi("select bangsal.nm_bangsal from bangsal where bangsal.kd_bangsal=?",akses.getkdbangsal()));
        TKdPny.setText(penyakit);
        TNoRw.setText(norwt);
        TNoRM.setText(norm);
        TPasien.setText(pasien);
        Tanggal.setText(Valid.SetTgl(tanggal));
        Jam.setText(jam);
        KdPj.setText(Sequel.cariIsi("select reg_periksa.kd_pj from reg_periksa where reg_periksa.no_rawat=?",norwt));
        kelas.setText(Sequel.cariIsi(
                "select kamar.kelas from kamar inner join kamar_inap on kamar.kd_kamar=kamar_inap.kd_kamar "+
                "where kamar_inap.no_rawat=? and kamar_inap.stts_pulang='-' order by STR_TO_DATE(concat(kamar_inap.tgl_masuk,' ',kamar_inap.jam_masuk),'%Y-%m-%d %H:%i:%s') desc limit 1",norwt));
        if(kelas.getText().equals("Kelas 1")){
            Jeniskelas.setSelectedItem("Kelas 1");
        }else if(kelas.getText().equals("Kelas 2")){
            Jeniskelas.setSelectedItem("Kelas 2");
        }else if(kelas.getText().equals("Kelas 3")){
            Jeniskelas.setSelectedItem("Kelas 3");
        }else if(kelas.getText().equals("Kelas Utama")){
            Jeniskelas.setSelectedItem("Utama");
        }else if(kelas.getText().equals("Kelas VIP")){
            Jeniskelas.setSelectedItem("VIP");
        }else if(kelas.getText().equals("Kelas VVIP")){
            Jeniskelas.setSelectedItem("VVIP");
        }        
        kenaikan=Sequel.cariIsiAngka("select (hargajual/100) from set_harga_obat_ranap where kd_pj='"+KdPj.getText()+"' and kelas='"+kelas.getText()+"'");
        this.nopermintaan="";
    }
    
    public void tampil2(String nopermintaan) {
        this.nopermintaan=nopermintaan;
        Valid.tabelKosong(tabMode);
        try{
            if(aktifkanbatch.equals("yes")){
                if(kenaikan>0){
                    psobat=koneksi.prepareStatement(
                            " select data_batch.kode_brng, databarang.nama_brng,jenis.nama, databarang.kode_sat,(data_batch.h_beli+(data_batch.h_beli*?)) as harga,gudangbarang.stok,"+
                            " databarang.letak_barang,data_batch.no_batch,data_batch.no_faktur,detail_permintaan_resep_pulang.jml,detail_permintaan_resep_pulang.dosis, "+
                            " if(gudangbarang.stok>detail_permintaan_resep_pulang.jml,detail_permintaan_resep_pulang.jml,gudangbarang.stok) as sisa "+
                            " from data_batch inner join databarang on data_batch.kode_brng=databarang.kode_brng inner join jenis on databarang.kdjns=jenis.kdjns "+
                            " inner join gudangbarang on gudangbarang.kode_brng=data_batch.kode_brng and gudangbarang.no_batch=data_batch.no_batch and gudangbarang.no_faktur=data_batch.no_faktur "+
                            " inner join detail_permintaan_resep_pulang on detail_permintaan_resep_pulang.kode_brng=databarang.kode_brng "+
                            " where gudangbarang.stok>0 and gudangbarang.kd_bangsal=? and detail_permintaan_resep_pulang.no_permintaan=? order by databarang.nama_brng");  
                }else{
                    psobat=koneksi.prepareStatement(
                            " select data_batch.kode_brng, databarang.nama_brng,jenis.nama, databarang.kode_sat,data_batch.kelas1,data_batch.kelas2,data_batch.kelas3,data_batch.utama,"+
                            " data_batch.vip,data_batch.vvip,data_batch.beliluar,data_batch.karyawan,data_batch.h_beli,databarang.letak_barang,gudangbarang.stok,data_batch.no_batch,"+
                            " data_batch.no_faktur,detail_permintaan_resep_pulang.jml,detail_permintaan_resep_pulang.dosis, "+
                            " if(gudangbarang.stok>detail_permintaan_resep_pulang.jml,detail_permintaan_resep_pulang.jml,gudangbarang.stok) as sisa "+
                            " from data_batch inner join databarang on data_batch.kode_brng=databarang.kode_brng inner join jenis on databarang.kdjns=jenis.kdjns "+
                            " inner join gudangbarang on gudangbarang.kode_brng=data_batch.kode_brng and gudangbarang.no_batch=data_batch.no_batch and gudangbarang.no_faktur=data_batch.no_faktur "+
                            " inner join detail_permintaan_resep_pulang on detail_permintaan_resep_pulang.kode_brng=databarang.kode_brng "+
                            " where gudangbarang.stok>0 and gudangbarang.kd_bangsal=? and detail_permintaan_resep_pulang.no_permintaan=? order by databarang.nama_brng"); 
                }   

                try {
                    if(kenaikan>0){
                        psobat.setDouble(1,kenaikan);
                        psobat.setString(2,kdgudang.getText());
                        psobat.setString(3,nopermintaan);
                        rs=psobat.executeQuery();
                        while(rs.next()){
                            if(rs.getDouble("jml")>rs.getDouble("stok")){
                                JOptionPane.showMessageDialog(rootPane,"Maaf stok tidak mencukupi..!!");
                            }
                            tabMode.addRow(new Object[]{rs.getString("sisa"),rs.getString("dosis"),rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("harga"),100)),rs.getString("nama"),rs.getString("no_batch"),rs.getString("no_faktur"),rs.getDouble("stok")});
                        }
                    }else{
                        psobat.setString(1,kdgudang.getText());
                        psobat.setString(2,nopermintaan);
                        rs=psobat.executeQuery();
                        if(Jeniskelas.getSelectedItem().equals("Kelas 1")){
                            while(rs.next()){
                                if(rs.getDouble("jml")>rs.getDouble("stok")){
                                    JOptionPane.showMessageDialog(rootPane,"Maaf stok tidak mencukupi..!!");
                                }
                                tabMode.addRow(new Object[]{rs.getString("sisa"),rs.getString("dosis"),rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("kelas1"),100)),rs.getString("nama"),rs.getString("no_batch"),rs.getString("no_faktur"),rs.getDouble("stok")});
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("Kelas 2")){
                            while(rs.next()){
                                if(rs.getDouble("jml")>rs.getDouble("stok")){
                                    JOptionPane.showMessageDialog(rootPane,"Maaf stok tidak mencukupi..!!");
                                }
                                tabMode.addRow(new Object[]{rs.getString("sisa"),rs.getString("dosis"),rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("kelas2"),100)),rs.getString("nama"),rs.getString("no_batch"),rs.getString("no_faktur"),rs.getDouble("stok")});
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("Kelas 3")){
                            while(rs.next()){
                                if(rs.getDouble("jml")>rs.getDouble("stok")){
                                    JOptionPane.showMessageDialog(rootPane,"Maaf stok tidak mencukupi..!!");
                                }
                                tabMode.addRow(new Object[]{rs.getString("sisa"),rs.getString("dosis"),rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("kelas3"),100)),rs.getString("nama"),rs.getString("no_batch"),rs.getString("no_faktur"),rs.getDouble("stok")});
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("Utama")){
                            while(rs.next()){
                                if(rs.getDouble("jml")>rs.getDouble("stok")){
                                    JOptionPane.showMessageDialog(rootPane,"Maaf stok tidak mencukupi..!!");
                                }
                                tabMode.addRow(new Object[]{rs.getString("sisa"),rs.getString("dosis"),rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("utama"),100)),rs.getString("nama"),rs.getString("no_batch"),rs.getString("no_faktur"),rs.getDouble("stok")});
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("VIP")){
                            while(rs.next()){
                                if(rs.getDouble("jml")>rs.getDouble("stok")){
                                    JOptionPane.showMessageDialog(rootPane,"Maaf stok tidak mencukupi..!!");
                                }
                                tabMode.addRow(new Object[]{rs.getString("sisa"),rs.getString("dosis"),rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("vip"),100)),rs.getString("nama"),rs.getString("no_batch"),rs.getString("no_faktur"),rs.getDouble("stok")});
                            }
                         }else if(Jeniskelas.getSelectedItem().equals("VVIP")){
                            while(rs.next()){
                                if(rs.getDouble("jml")>rs.getDouble("stok")){
                                    JOptionPane.showMessageDialog(rootPane,"Maaf stok tidak mencukupi..!!");
                                }
                                tabMode.addRow(new Object[]{rs.getString("sisa"),rs.getString("dosis"),rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("vvip"),100)),rs.getString("nama"),rs.getString("no_batch"),rs.getString("no_faktur"),rs.getDouble("stok")});
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("Beli Luar")){
                            while(rs.next()){
                                if(rs.getDouble("jml")>rs.getDouble("stok")){
                                    JOptionPane.showMessageDialog(rootPane,"Maaf stok tidak mencukupi..!!");
                                }
                                tabMode.addRow(new Object[]{rs.getString("sisa"),rs.getString("dosis"),rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("beliluar"),100)),rs.getString("nama"),rs.getString("no_batch"),rs.getString("no_faktur"),rs.getDouble("stok")});
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("Karyawan")){
                            while(rs.next()){
                                if(rs.getDouble("jml")>rs.getDouble("stok")){
                                    JOptionPane.showMessageDialog(rootPane,"Maaf stok tidak mencukupi..!!");
                                }
                                tabMode.addRow(new Object[]{rs.getString("sisa"),rs.getString("dosis"),rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("karyawan"),100)),rs.getString("nama"),rs.getString("no_batch"),rs.getString("no_faktur"),rs.getDouble("stok")});
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("Harga Beli")){
                            while(rs.next()){
                                if(rs.getDouble("jml")>rs.getDouble("stok")){
                                    JOptionPane.showMessageDialog(rootPane,"Maaf stok tidak mencukupi..!!");
                                }
                                tabMode.addRow(new Object[]{rs.getString("sisa"),rs.getString("dosis"),rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("h_beli"),100)),rs.getString("nama"),rs.getString("no_batch"),rs.getString("no_faktur"),rs.getDouble("stok")});
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Notif obat2 : "+e);
                } finally{
                    if(rs!=null){
                        rs.close();
                    }
                    if(psobat!=null){
                        psobat.close();
                    }
                }
            }else{
                if(kenaikan>0){
                    psobat=koneksi.prepareStatement(
                            " select databarang.kode_brng, databarang.nama_brng,jenis.nama, databarang.kode_sat,(databarang.h_beli+(databarang.h_beli*?)) as harga,gudangbarang.stok,"+
                            " databarang.letak_barang,detail_permintaan_resep_pulang.jml,detail_permintaan_resep_pulang.dosis, "+
                            " if(gudangbarang.stok>detail_permintaan_resep_pulang.jml,detail_permintaan_resep_pulang.jml,gudangbarang.stok) as sisa "+
                            " from databarang inner join jenis on databarang.kdjns=jenis.kdjns inner join gudangbarang on databarang.kode_brng=gudangbarang.kode_brng "+
                            " inner join detail_permintaan_resep_pulang on detail_permintaan_resep_pulang.kode_brng=databarang.kode_brng "+
                            " where gudangbarang.no_batch='' and gudangbarang.no_faktur='' and gudangbarang.stok>0 and gudangbarang.kd_bangsal=? "+
                            " and databarang.status='1' and detail_permintaan_resep_pulang.no_permintaan=? order by databarang.nama_brng");  
                }else{
                    psobat=koneksi.prepareStatement(
                            " select databarang.kode_brng, databarang.nama_brng,jenis.nama, databarang.kode_sat,databarang.kelas1,databarang.kelas2,databarang.kelas3,databarang.utama,"+
                            " databarang.vip,databarang.vvip,databarang.beliluar,databarang.karyawan,databarang.h_beli,databarang.letak_barang,gudangbarang.stok,"+
                            " detail_permintaan_resep_pulang.jml,detail_permintaan_resep_pulang.dosis, "+
                            " if(gudangbarang.stok>detail_permintaan_resep_pulang.jml,detail_permintaan_resep_pulang.jml,gudangbarang.stok) as sisa "+
                            " from databarang inner join jenis on databarang.kdjns=jenis.kdjns inner join gudangbarang on databarang.kode_brng=gudangbarang.kode_brng "+
                            " inner join detail_permintaan_resep_pulang on detail_permintaan_resep_pulang.kode_brng=databarang.kode_brng "+
                            " where gudangbarang.no_batch='' and gudangbarang.no_faktur='' and gudangbarang.stok>0 and gudangbarang.kd_bangsal=? and databarang.status='1' "+
                            " and detail_permintaan_resep_pulang.no_permintaan=? order by databarang.nama_brng"); 
                }   

                try {
                    if(kenaikan>0){
                        psobat.setDouble(1,kenaikan);
                        psobat.setString(2,kdgudang.getText());
                        psobat.setString(3,nopermintaan);
                        rs=psobat.executeQuery();
                        while(rs.next()){
                            tabMode.addRow(new Object[]{rs.getString("sisa"),rs.getString("dosis"),rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("harga"),100)),rs.getString("nama"),"","",rs.getDouble("stok")});
                        }
                    }else{
                        psobat.setString(1,kdgudang.getText());
                        psobat.setString(2,nopermintaan);
                        rs=psobat.executeQuery();
                        if(Jeniskelas.getSelectedItem().equals("Kelas 1")){
                            while(rs.next()){
                                tabMode.addRow(new Object[]{rs.getString("sisa"),rs.getString("dosis"),rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("kelas1"),100)),rs.getString("nama"),"","",rs.getDouble("stok")});
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("Kelas 2")){
                            while(rs.next()){
                                tabMode.addRow(new Object[]{rs.getString("sisa"),rs.getString("dosis"),rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("kelas2"),100)),rs.getString("nama"),"","",rs.getDouble("stok")});
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("Kelas 3")){
                            while(rs.next()){
                                tabMode.addRow(new Object[]{rs.getString("sisa"),rs.getString("dosis"),rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("kelas3"),100)),rs.getString("nama"),"","",rs.getDouble("stok")});
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("Utama")){
                            while(rs.next()){
                                tabMode.addRow(new Object[]{rs.getString("sisa"),rs.getString("dosis"),rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("utama"),100)),rs.getString("nama"),"","",rs.getDouble("stok")});
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("VIP")){
                            while(rs.next()){
                                tabMode.addRow(new Object[]{rs.getString("sisa"),rs.getString("dosis"),rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("vip"),100)),rs.getString("nama"),"","",rs.getDouble("stok")});
                            }
                         }else if(Jeniskelas.getSelectedItem().equals("VVIP")){
                            while(rs.next()){
                                tabMode.addRow(new Object[]{rs.getString("sisa"),rs.getString("dosis"),rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("vvip"),100)),rs.getString("nama"),"","",rs.getDouble("stok")});
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("Beli Luar")){
                            while(rs.next()){
                                tabMode.addRow(new Object[]{rs.getString("sisa"),rs.getString("dosis"),rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("beliluar"),100)),rs.getString("nama"),"","",rs.getDouble("stok")});
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("Karyawan")){
                            while(rs.next()){
                                tabMode.addRow(new Object[]{rs.getString("sisa"),rs.getString("dosis"),rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("karyawan"),100)),rs.getString("nama"),"","",rs.getDouble("stok")});
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("Harga Beli")){
                            while(rs.next()){
                                tabMode.addRow(new Object[]{rs.getString("sisa"),rs.getString("dosis"),rs.getString("kode_brng"),rs.getString("nama_brng"),rs.getString("kode_sat"),rs.getString("letak_barang"),Valid.SetAngka2(Valid.roundUp(rs.getDouble("h_beli"),100)),rs.getString("nama"),"","",rs.getDouble("stok")});
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Notif obat2 : "+e);
                } finally{
                    if(rs!=null){
                        rs.close();
                    }
                    if(psobat!=null){
                        psobat.close();
                    }
                }
            }

            // Load racikan headers
            try{
                Valid.tabelKosong(tabModeObatRacikan);
                psobat=koneksi.prepareStatement(
                    "select resep_pulang_racikan.no_racik,resep_pulang_racikan.nama_racik,"+
                    "resep_pulang_racikan.kd_racik,metode_racik.nm_racik,"+
                    "resep_pulang_racikan.jml_dr,resep_pulang_racikan.aturan_pakai,"+
                    "resep_pulang_racikan.keterangan "+
                    "from resep_pulang_racikan left join metode_racik on resep_pulang_racikan.kd_racik=metode_racik.kd_racik "+
                    "where resep_pulang_racikan.no_resep=? order by resep_pulang_racikan.no_racik");
                psobat.setString(1,nopermintaan);
                rs=psobat.executeQuery();
                while(rs.next()){
                    tabModeObatRacikan.addRow(new Object[]{
                        rs.getString("no_racik"),
                        rs.getString("nama_racik"),
                        rs.getString("kd_racik"),
                        rs.getString("nm_racik"),
                        rs.getInt("jml_dr"),
                        rs.getString("aturan_pakai"),
                        rs.getString("keterangan")
                    });
                }
            }catch(Exception e){
                System.out.println("Notif Racikan : "+e);
            }finally{
                if(rs!=null){
                    rs.close();
                }
                if(psobat!=null){
                    psobat.close();
                }
            }

            // Load racikan details
            try{
                Valid.tabelKosong(tabModeDetailObatRacikan);
                if(aktifkanbatch.equals("yes")){
                    psobat=koneksi.prepareStatement(
                        "select resep_pulang_racikan_detail.no_racik,resep_pulang_racikan_detail.kode_brng,"+
                        "databarang.nama_brng,databarang.kode_sat,databarang.h_beli,databarang."+hppfarmasi+" as dasar,"+
                        "jenis.nama,sum(gudangbarang.stok) as stok,databarang.kapasitas,resep_pulang_racikan_detail.p1,"+
                        "resep_pulang_racikan_detail.p2,resep_pulang_racikan_detail.kandungan,"+
                        "resep_pulang_racikan_detail.jml,industrifarmasi.nama_industri,kategori_barang.nama as kategori,"+
                        "golongan_barang.nama as golongan,data_batch.no_batch,data_batch.no_faktur,data_batch.tgl_kadaluarsa "+
                        "from resep_pulang_racikan_detail inner join databarang on resep_pulang_racikan_detail.kode_brng=databarang.kode_brng "+
                        "inner join jenis on databarang.kdjns=jenis.kdjns "+
                        "inner join gudangbarang on resep_pulang_racikan_detail.kode_brng=gudangbarang.kode_brng and gudangbarang.kd_bangsal=? "+
                        "inner join industrifarmasi on databarang.kode_industri=industrifarmasi.kode_industri "+
                        "inner join kategori_barang on databarang.kode_kategori=kategori_barang.kode "+
                        "inner join golongan_barang on databarang.kode_golongan=golongan_barang.kode "+
                        "inner join data_batch on gudangbarang.kode_brng=data_batch.kode_brng and gudangbarang.no_batch=data_batch.no_batch and gudangbarang.no_faktur=data_batch.no_faktur "+
                        "where resep_pulang_racikan_detail.no_resep=? and gudangbarang.no_batch<>'' and gudangbarang.no_faktur<>'' "+
                        "group by resep_pulang_racikan_detail.kode_brng,resep_pulang_racikan_detail.no_racik order by resep_pulang_racikan_detail.no_racik");
                }else{
                    psobat=koneksi.prepareStatement(
                        "select resep_pulang_racikan_detail.no_racik,resep_pulang_racikan_detail.kode_brng,"+
                        "databarang.nama_brng,databarang.kode_sat,databarang.h_beli,databarang."+hppfarmasi+" as dasar,"+
                        "jenis.nama,gudangbarang.stok,databarang.kapasitas,resep_pulang_racikan_detail.p1,"+
                        "resep_pulang_racikan_detail.p2,resep_pulang_racikan_detail.kandungan,"+
                        "resep_pulang_racikan_detail.jml,industrifarmasi.nama_industri,kategori_barang.nama as kategori,"+
                        "golongan_barang.nama as golongan "+
                        "from resep_pulang_racikan_detail inner join databarang on resep_pulang_racikan_detail.kode_brng=databarang.kode_brng "+
                        "inner join jenis on databarang.kdjns=jenis.kdjns "+
                        "inner join gudangbarang on resep_pulang_racikan_detail.kode_brng=gudangbarang.kode_brng and gudangbarang.kd_bangsal=? "+
                        "inner join industrifarmasi on databarang.kode_industri=industrifarmasi.kode_industri "+
                        "inner join kategori_barang on databarang.kode_kategori=kategori_barang.kode "+
                        "inner join golongan_barang on databarang.kode_golongan=golongan_barang.kode "+
                        "where resep_pulang_racikan_detail.no_resep=? and gudangbarang.no_batch='' and gudangbarang.no_faktur='' "+
                        "order by resep_pulang_racikan_detail.no_racik");
                }
                psobat.setString(1,kdgudang.getText());
                psobat.setString(2,nopermintaan);
                rs=psobat.executeQuery();
                while(rs.next()){
                    if(aktifkanbatch.equals("yes")){
                        tabModeDetailObatRacikan.addRow(new Object[]{
                            rs.getString("no_racik"),rs.getString("kode_brng"),
                            rs.getString("nama_brng"),rs.getString("kode_sat"),
                            rs.getDouble("h_beli"),rs.getDouble("dasar"),
                            rs.getString("nama"),rs.getDouble("stok"),
                            rs.getDouble("kapasitas"),rs.getDouble("p1"),"/",rs.getDouble("p2"),
                            rs.getString("kandungan"),rs.getDouble("jml"),0,0,
                            rs.getString("nama_industri"),rs.getString("kategori"),
                            rs.getString("golongan"),rs.getString("no_batch"),rs.getString("no_faktur"),
                            rs.getString("tgl_kadaluarsa")
                        });
                    }else{
                        tabModeDetailObatRacikan.addRow(new Object[]{
                            rs.getString("no_racik"),rs.getString("kode_brng"),
                            rs.getString("nama_brng"),rs.getString("kode_sat"),
                            rs.getDouble("h_beli"),rs.getDouble("dasar"),
                            rs.getString("nama"),rs.getDouble("stok"),
                            rs.getDouble("kapasitas"),rs.getDouble("p1"),"/",rs.getDouble("p2"),
                            rs.getString("kandungan"),rs.getDouble("jml"),0,0,
                            rs.getString("nama_industri"),rs.getString("kategori"),
                            rs.getString("golongan"),"","",""
                        });
                    }
                }
            }catch(Exception e){
                System.out.println("Notif Racikan Detail : "+e);
            }finally{
                if(rs!=null){
                    rs.close();
                }
                if(psobat!=null){
                    psobat.close();
                }
            }
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
    }

    private void getData() {
        int row=tbKamar.getSelectedRow();
        if(kdgudang.getText().trim().equals("")){
             Valid.textKosong(TCari,"Asal Stok");
        }else if(row!= -1){            
             if(!tabMode.getValueAt(row,0).toString().equals("")){
                try {
                    if(Double.parseDouble(tabMode.getValueAt(row,0).toString())>0){
                        stokbarang=0;
                        if(aktifkanbatch.equals("yes")){
                            psobat=koneksi.prepareStatement("select ifnull(gudangbarang.stok,'0') from gudangbarang where gudangbarang.kd_bangsal=? and gudangbarang.kode_brng=? and gudangbarang.no_batch=? and gudangbarang.no_faktur=?");
                            try {
                                psobat.setString(1,kdgudang.getText());
                                psobat.setString(2,tbKamar.getValueAt(row,2).toString());
                                psobat.setString(3,tbKamar.getValueAt(row,8).toString());
                                psobat.setString(4,tbKamar.getValueAt(row,9).toString());
                                rs=psobat.executeQuery();
                                if(rs.next()){
                                    stokbarang=rs.getDouble(1);
                                }
                            } catch (Exception e) {
                                System.out.println("Notifikasi : "+e);
                            } finally{
                                if(rs!=null){
                                    rs.close();
                                }
                                if(psobat!=null){
                                    psobat.close();
                                }
                            }  
                        }else{
                            psobat=koneksi.prepareStatement("select ifnull(gudangbarang.stok,'0') from gudangbarang where gudangbarang.kd_bangsal=? and gudangbarang.kode_brng=? and gudangbarang.no_batch='' and gudangbarang.no_faktur=''");
                            try {
                                psobat.setString(1,kdgudang.getText());
                                psobat.setString(2,tbKamar.getValueAt(row,2).toString());
                                rs=psobat.executeQuery();
                                if(rs.next()){
                                    stokbarang=rs.getDouble(1);
                                }
                            } catch (Exception e) {
                                System.out.println("Notifikasi : "+e);
                            } finally{
                                if(rs!=null){
                                    rs.close();
                                }
                                if(psobat!=null){
                                    psobat.close();
                                }
                            }  
                        }   

                        tbKamar.setValueAt(stokbarang,row,10);
                        y=0;
                        try {
                            y=Double.parseDouble(tabMode.getValueAt(row,0).toString());
                        } catch (Exception e) {
                            y=0;
                        }
                        
                        if(stokbarang<y){
                              JOptionPane.showMessageDialog(null,"Maaf, Stok tidak cukup....!!!");
                              TCari.requestFocus();
                              tabMode.setValueAt("", row,0);
                        } 
                    }
                } catch (Exception e) {
                    tabMode.setValueAt("", row,0);
                }                                       
             } 
        }
    }
    
    private void getDatadetailobatracikan() {
        if(tbDetailObatRacikan.getSelectedRow()!= -1){
            row=tbDetailObatRacikan.getSelectedRow();
            try {
                if(Valid.SetAngka(tbDetailObatRacikan.getValueAt(row,10).toString())>0){
                    stokbarang=0;  
                    if(aktifkanbatch.equals("yes")){
                        psstok=koneksi.prepareStatement("select ifnull(gudangbarang.stok,'0') from gudangbarang where gudangbarang.kd_bangsal=? and gudangbarang.kode_brng=? and gudangbarang.no_batch=? and gudangbarang.no_faktur=?");
                        try {
                            psstok.setString(1,kdgudang.getText());
                            psstok.setString(2,tbDetailObatRacikan.getValueAt(row,1).toString());
                            psstok.setString(3,tbDetailObatRacikan.getValueAt(row,16).toString());
                            psstok.setString(4,tbDetailObatRacikan.getValueAt(row,17).toString());
                            rsstok=psstok.executeQuery();
                            if(rsstok.next()){
                                stokbarang=rsstok.getDouble(1);
                            }                                
                        } catch (Exception e) {
                            stokbarang=0;
                            System.out.println("Notifikasi : "+e);
                        }finally{
                            if(rsstok != null){
                                rsstok.close();
                            }
                            if(psstok != null){
                                psstok.close();
                            }
                        }
                    }else{
                        psstok=koneksi.prepareStatement("select ifnull(gudangbarang.stok,'0') from gudangbarang where gudangbarang.kd_bangsal=? and gudangbarang.kode_brng=? and gudangbarang.no_batch='' and gudangbarang.no_faktur=''");
                        try {
                            psstok.setString(1,kdgudang.getText());
                            psstok.setString(2,tbDetailObatRacikan.getValueAt(row,1).toString());
                            rsstok=psstok.executeQuery();
                            if(rsstok.next()){
                                stokbarang=rsstok.getDouble(1);
                            }                                
                        } catch (Exception e) {
                            stokbarang=0;
                            System.out.println("Notifikasi : "+e);
                        }finally{
                            if(rsstok != null){
                                rsstok.close();
                            }
                            if(psstok != null){
                                psstok.close();
                            }
                        }
                    }

                    tbDetailObatRacikan.setValueAt(stokbarang,row,7);
                    
                    y=0;
                    try {
                        y=Double.parseDouble(tbDetailObatRacikan.getValueAt(row,10).toString());
                    } catch (Exception e) {
                        y=0;
                    }
                    
                    stokbarang=0;
                    try {
                        stokbarang=Double.parseDouble(tbDetailObatRacikan.getValueAt(row,7).toString());
                    } catch (Exception e) {
                        stokbarang=0;
                    }

                    if(stokbarang<y){
                        JOptionPane.showMessageDialog(rootPane,"Maaf stok tidak mencukupi..!!");
                        tbDetailObatRacikan.setValueAt("",row,9);
                        tbDetailObatRacikan.setValueAt(0,row,10);
                        tbDetailObatRacikan.setValueAt(0,row,11);
                        tbDetailObatRacikan.setValueAt(0,row,12);
                    }
                }    
                if((tbDetailObatRacikan.getSelectedColumn()==16)||(tbDetailObatRacikan.getSelectedColumn()==17)){
                    //cariBatch();   
                    //getData2();
                }
            } catch (Exception e) {
                System.out.println("Notif Racikan : "+e);
                tbDetailObatRacikan.setValueAt(0,row,10);
                tbDetailObatRacikan.setValueAt(0,row,11);
                tbDetailObatRacikan.setValueAt(0,row,12);
            }   
        }
    }
    
    public void tampildetailracikanobat() {
        z=0;
        for(i=0;i<tbDetailObatRacikan.getRowCount();i++){
            if(Valid.SetAngka(tbDetailObatRacikan.getValueAt(i,13).toString())>0){
                z++;
            }
        }

        pilih=null;
        pilih=new boolean[z];
        jumlah=null;
        jumlah=new double[z];
        harga=null;
        harga=new double[z];
        eb=null;
        eb=new double[z];
        ts=null;
        ts=new double[z];
        stok=null;
        stok=new double[z];
        kodebarang=null;
        kodebarang=new String[z];
        namabarang=null;
        namabarang=new String[z];
        kodesatuan=null;
        kodesatuan=new String[z];
        letakbarang=null;
        letakbarang=new String[z];
        no=null;
        no=new String[z];
        namajenis=null;
        namajenis=new String[z];
        industri=null;
        industri=new String[z];
        beli=null;
        beli=new double[z];
        kategori=null;
        kategori=new String[z];
        golongan=null;
        golongan=new String[z];
        kapasitas=null;
        kapasitas=new double[z];
        p1=null;
        p1=new double[z];
        p2=null;
        p2=new double[z];
        kandungan=null;
        kandungan=new double[z];
        nobatch=new String[z];
        nofaktur=new String[z];
        kadaluarsa=new String[z];
        z=0;
        for(i=0;i<tbDetailObatRacikan.getRowCount();i++){
            if(Valid.SetAngka(tbDetailObatRacikan.getValueAt(i,13).toString())>0){
                no[z]=tbDetailObatRacikan.getValueAt(i,0).toString();
                kodebarang[z]=tbDetailObatRacikan.getValueAt(i,1).toString();
                namabarang[z]=tbDetailObatRacikan.getValueAt(i,2).toString();
                kodesatuan[z]=tbDetailObatRacikan.getValueAt(i,3).toString();
                try {
                    harga[z]=Double.parseDouble(tbDetailObatRacikan.getValueAt(i,4).toString());
                } catch (Exception e) {
                    harga[z]=0;
                }
                try {
                    beli[z]=Double.parseDouble(tbDetailObatRacikan.getValueAt(i,5).toString());
                } catch (Exception e) {
                    beli[z]=0;
                }
                namajenis[z]=tbDetailObatRacikan.getValueAt(i,6).toString();
                try {
                    stok[z]=Double.parseDouble(tbDetailObatRacikan.getValueAt(i,7).toString());
                } catch (Exception e) {
                    stok[z]=0;
                }
                try {
                    kapasitas[z]=Double.parseDouble(tbDetailObatRacikan.getValueAt(i,8).toString());
                } catch (Exception e) {
                    kapasitas[z]=0;
                }
                try {
                    p1[z]=Double.parseDouble(tbDetailObatRacikan.getValueAt(i,9).toString());
                } catch (Exception e) {
                    p1[z]=0;
                }
                try {
                    p2[z]=Double.parseDouble(tbDetailObatRacikan.getValueAt(i,11).toString());
                } catch (Exception e) {
                    p2[z]=0;
                }
                try {
                    kandungan[z]=Double.parseDouble(tbDetailObatRacikan.getValueAt(i,12).toString());
                } catch (Exception e) {
                    kandungan[z]=0;
                }
                try {
                    jumlah[z]=Double.parseDouble(tbDetailObatRacikan.getValueAt(i,13).toString());
                } catch (Exception e) {
                    jumlah[z]=0;
                }
                try {
                    eb[z]=Double.parseDouble(tbDetailObatRacikan.getValueAt(i,14).toString());
                } catch (Exception e) {
                    eb[z]=0;
                }
                try {
                    ts[z]=Double.parseDouble(tbDetailObatRacikan.getValueAt(i,15).toString());
                } catch (Exception e) {
                    ts[z]=0;
                }
                industri[z]=tbDetailObatRacikan.getValueAt(i,16).toString();
                kategori[z]=tbDetailObatRacikan.getValueAt(i,17).toString();
                golongan[z]=tbDetailObatRacikan.getValueAt(i,18).toString();
                nobatch[z]=tbDetailObatRacikan.getValueAt(i,19).toString();
                nofaktur[z]=tbDetailObatRacikan.getValueAt(i,20).toString();
                try {
                    kadaluarsa[z]=tbDetailObatRacikan.getValueAt(i,21).toString();
                } catch (Exception e) {
                    kadaluarsa[z]="0000-00-00";
                }
                z++;
            }
        }

        Valid.tabelKosong(tabModeDetailObatRacikan);

        for(i=0;i<z;i++){
            tabModeDetailObatRacikan.addRow(new Object[] {
                no[i],kodebarang[i],namabarang[i],kodesatuan[i],harga[i],beli[i],
                namajenis[i],stok[i],kapasitas[i],p1[i],"/",p2[i],kandungan[i],jumlah[i],eb[i],
                ts[i],industri[i],kategori[i],golongan[i],nobatch[i],nofaktur[i],kadaluarsa[i]
            });
        }
        
        try {
            if(kenaikan>0){
                if(aktifkanbatch.equals("yes")){
                    if(aktifpcare.equals("yes")){
                        sql="select data_batch.kode_brng, databarang.nama_brng,jenis.nama, databarang.kode_sat,(data_batch.h_beli+(data_batch.h_beli*?)) as harga,"+
                            " databarang.letak_barang,industrifarmasi.nama_industri,databarang.h_beli,kategori_barang.nama as kategori,golongan_barang.nama as golongan,databarang.kapasitas, "+
                            " data_batch.no_batch,data_batch.no_faktur,data_batch.tgl_kadaluarsa,gudangbarang.stok,data_batch."+hppfarmasi+" as dasar "+
                            " from data_batch inner join databarang on data_batch.kode_brng=databarang.kode_brng "+
                            " inner join jenis on databarang.kdjns=jenis.kdjns "+
                            " inner join industrifarmasi on industrifarmasi.kode_industri=databarang.kode_industri "+
                            " inner join golongan_barang on databarang.kode_golongan=golongan_barang.kode "+
                            " inner join kategori_barang on databarang.kode_kategori=kategori_barang.kode "+
                            " inner join maping_obat_pcare on maping_obat_pcare.kode_brng=databarang.kode_brng "+
                            " inner join gudangbarang on gudangbarang.kode_brng=data_batch.kode_brng and gudangbarang.no_batch=data_batch.no_batch and gudangbarang.no_faktur=data_batch.no_faktur ";
                    }else{
                        sql="select data_batch.kode_brng, databarang.nama_brng,jenis.nama, databarang.kode_sat,(data_batch.h_beli+(data_batch.h_beli*?)) as harga,"+
                            " databarang.letak_barang,industrifarmasi.nama_industri,databarang.h_beli,kategori_barang.nama as kategori,golongan_barang.nama as golongan,databarang.kapasitas, "+
                            " data_batch.no_batch,data_batch.no_faktur,data_batch.tgl_kadaluarsa,gudangbarang.stok,data_batch."+hppfarmasi+" as dasar "+
                            " from data_batch inner join databarang on data_batch.kode_brng=databarang.kode_brng "+
                            " inner join jenis on databarang.kdjns=jenis.kdjns "+
                            " inner join industrifarmasi on industrifarmasi.kode_industri=databarang.kode_industri "+
                            " inner join golongan_barang on databarang.kode_golongan=golongan_barang.kode "+
                            " inner join kategori_barang on databarang.kode_kategori=kategori_barang.kode "+
                            " inner join gudangbarang on gudangbarang.kode_brng=data_batch.kode_brng and gudangbarang.no_batch=data_batch.no_batch and gudangbarang.no_faktur=data_batch.no_faktur ";
                    }
                    psobat=koneksi.prepareStatement(
                        sql+" where gudangbarang.stok>0 and gudangbarang.kd_bangsal=? and databarang.kode_brng like ? or "+
                        " gudangbarang.stok>0 and gudangbarang.kd_bangsal=? and databarang.nama_brng like ? or "+
                        " gudangbarang.stok>0 and gudangbarang.kd_bangsal=? and kategori_barang.nama like ? or "+
                        " gudangbarang.stok>0 and gudangbarang.kd_bangsal=? and golongan_barang.nama like ? or "+
                        " gudangbarang.stok>0 and gudangbarang.kd_bangsal=? and data_batch.no_batch like ? or "+
                        " gudangbarang.stok>0 and gudangbarang.kd_bangsal=? and data_batch.no_faktur like ? or "+
                        " gudangbarang.stok>0 and gudangbarang.kd_bangsal=? and jenis.nama like ? order by data_batch.tgl_kadaluarsa asc");
                    try {
                        psobat.setDouble(1,kenaikan);
                        psobat.setString(2,kdgudang.getText());
                        psobat.setString(3,"%"+TCari.getText().trim()+"%");
                        psobat.setString(4,kdgudang.getText());
                        psobat.setString(5,"%"+TCari.getText().trim()+"%");
                        psobat.setString(6,kdgudang.getText());
                        psobat.setString(7,"%"+TCari.getText().trim()+"%");
                        psobat.setString(8,kdgudang.getText());
                        psobat.setString(9,"%"+TCari.getText().trim()+"%");
                        psobat.setString(10,kdgudang.getText());
                        psobat.setString(11,"%"+TCari.getText().trim()+"%");
                        psobat.setString(12,kdgudang.getText());
                        psobat.setString(13,"%"+TCari.getText().trim()+"%");
                        psobat.setString(14,kdgudang.getText());
                        psobat.setString(15,"%"+TCari.getText().trim()+"%");
                        rs=psobat.executeQuery();
                        while(rs.next()){
                            tabModeDetailObatRacikan.addRow(new Object[] {
                                tbObatRacikan.getValueAt(tbObatRacikan.getSelectedRow(),0).toString(),
                                rs.getString("kode_brng"),rs.getString("nama_brng"),
                                rs.getString("kode_sat"),Valid.roundUp(rs.getDouble("harga"),100),
                                rs.getDouble("dasar"),rs.getString("nama"),rs.getDouble("stok"),
                                rs.getDouble("kapasitas"),0,"/",0,"",0,0,0,rs.getString("nama_industri"),
                                rs.getString("kategori"),rs.getString("golongan"),
                                rs.getString("no_batch"),rs.getString("no_faktur"),rs.getString("tgl_kadaluarsa")
                            });
                        } 
                    }catch(Exception e){
                        System.out.println("Notifikasi : "+e);
                    }finally{
                        if(rs != null){
                            rs.close();
                        }
                        if(psobat != null){
                            psobat.close();
                        }
                    }
                }else{
                    if(aktifpcare.equals("yes")){
                        sql="select databarang.kode_brng, databarang.nama_brng,jenis.nama, databarang.kode_sat,(databarang.h_beli+(databarang.h_beli*?)) as harga,databarang."+hppfarmasi+" as dasar,gudangbarang.stok,"+
                            " databarang.letak_barang,industrifarmasi.nama_industri,databarang.h_beli,kategori_barang.nama as kategori,golongan_barang.nama as golongan,databarang.kapasitas "+
                            " from databarang inner join jenis on databarang.kdjns=jenis.kdjns "+
                            " inner join industrifarmasi on industrifarmasi.kode_industri=databarang.kode_industri "+
                            " inner join golongan_barang on databarang.kode_golongan=golongan_barang.kode "+
                            " inner join kategori_barang on databarang.kode_kategori=kategori_barang.kode "+
                            " inner join maping_obat_pcare on maping_obat_pcare.kode_brng=databarang.kode_brng "+
                            " inner join gudangbarang on databarang.kode_brng=gudangbarang.kode_brng ";
                    }else{
                        sql="select databarang.kode_brng, databarang.nama_brng,jenis.nama, databarang.kode_sat,(databarang.h_beli+(databarang.h_beli*?)) as harga,databarang."+hppfarmasi+" as dasar,gudangbarang.stok,"+
                            " databarang.letak_barang,industrifarmasi.nama_industri,databarang.h_beli,kategori_barang.nama as kategori,golongan_barang.nama as golongan,databarang.kapasitas "+
                            " from databarang inner join jenis on databarang.kdjns=jenis.kdjns "+
                            " inner join industrifarmasi on industrifarmasi.kode_industri=databarang.kode_industri "+
                            " inner join golongan_barang on databarang.kode_golongan=golongan_barang.kode "+
                            " inner join kategori_barang on databarang.kode_kategori=kategori_barang.kode "+
                            " inner join gudangbarang on databarang.kode_brng=gudangbarang.kode_brng ";
                    }
                    psobat=koneksi.prepareStatement(
                        sql+" where gudangbarang.no_batch='' and gudangbarang.no_faktur='' and gudangbarang.stok>0 and gudangbarang.kd_bangsal=? and databarang.status='1' and databarang.kode_brng like ? or "+
                        " gudangbarang.no_batch='' and gudangbarang.no_faktur='' and gudangbarang.stok>0 and gudangbarang.kd_bangsal=? and databarang.status='1' and databarang.nama_brng like ? or "+
                        " gudangbarang.no_batch='' and gudangbarang.no_faktur='' and gudangbarang.stok>0 and gudangbarang.kd_bangsal=? and databarang.status='1' and kategori_barang.nama like ? or "+
                        " gudangbarang.no_batch='' and gudangbarang.no_faktur='' and gudangbarang.stok>0 and gudangbarang.kd_bangsal=? and databarang.status='1' and golongan_barang.nama like ? or "+
                        " gudangbarang.no_batch='' and gudangbarang.no_faktur='' and gudangbarang.stok>0 and gudangbarang.kd_bangsal=? and databarang.status='1' and jenis.nama like ? order by databarang.nama_brng");
                    try {
                        psobat.setDouble(1,kenaikan);
                        psobat.setString(2,kdgudang.getText());
                        psobat.setString(3,"%"+TCari.getText().trim()+"%");
                        psobat.setString(4,kdgudang.getText());
                        psobat.setString(5,"%"+TCari.getText().trim()+"%");
                        psobat.setString(6,kdgudang.getText());
                        psobat.setString(7,"%"+TCari.getText().trim()+"%");
                        psobat.setString(8,kdgudang.getText());
                        psobat.setString(9,"%"+TCari.getText().trim()+"%");
                        psobat.setString(10,kdgudang.getText());
                        psobat.setString(11,"%"+TCari.getText().trim()+"%");
                        rs=psobat.executeQuery();
                        while(rs.next()){
                            tabModeDetailObatRacikan.addRow(new Object[] {
                                tbObatRacikan.getValueAt(tbObatRacikan.getSelectedRow(),0).toString(),
                                rs.getString("kode_brng"),rs.getString("nama_brng"),
                                rs.getString("kode_sat"),Valid.roundUp(rs.getDouble("harga"),100),
                                rs.getDouble("dasar"),rs.getString("nama"),rs.getDouble("stok"),
                                rs.getDouble("kapasitas"),0,"/",0,"",0,0,0,rs.getString("nama_industri"),
                                rs.getString("kategori"),rs.getString("golongan"),"","",""
                            });
                        } 
                    }catch(Exception e){
                        System.out.println("Notifikasi : "+e);
                    }finally{
                        if(rs != null){
                            rs.close();
                        }
                        if(psobat != null){
                            psobat.close();
                        }
                    }
                }
            }else{
                if(aktifkanbatch.equals("yes")){
                    if(aktifpcare.equals("yes")){
                        sql="select data_batch.kode_brng, databarang.nama_brng,jenis.nama, databarang.kode_sat,data_batch.kelas1,data_batch.kelas2,"+
                            " data_batch.kelas3,data_batch.utama,data_batch.vip,data_batch.vvip,data_batch.beliluar,data_batch.karyawan,"+
                            " databarang.letak_barang,industrifarmasi.nama_industri,data_batch.h_beli,kategori_barang.nama as kategori,golongan_barang.nama as golongan,databarang.kapasitas,  "+
                            " data_batch.no_batch,data_batch.no_faktur,data_batch.tgl_kadaluarsa,gudangbarang.stok,data_batch."+hppfarmasi+" as dasar "+
                            " from data_batch inner join databarang on data_batch.kode_brng=databarang.kode_brng "+
                            " inner join jenis on databarang.kdjns=jenis.kdjns "+
                            " inner join industrifarmasi on industrifarmasi.kode_industri=databarang.kode_industri "+
                            " inner join golongan_barang on databarang.kode_golongan=golongan_barang.kode "+
                            " inner join kategori_barang on databarang.kode_kategori=kategori_barang.kode "+
                            " inner join maping_obat_pcare on maping_obat_pcare.kode_brng=databarang.kode_brng "+
                            " inner join gudangbarang on gudangbarang.kode_brng=data_batch.kode_brng and gudangbarang.no_batch=data_batch.no_batch and gudangbarang.no_faktur=data_batch.no_faktur ";
                    }else{
                        sql="select data_batch.kode_brng, databarang.nama_brng,jenis.nama, databarang.kode_sat,data_batch.kelas1,data_batch.kelas2,"+
                            " data_batch.kelas3,data_batch.utama,data_batch.vip,data_batch.vvip,data_batch.beliluar,data_batch.karyawan,"+
                            " databarang.letak_barang,industrifarmasi.nama_industri,data_batch.h_beli,kategori_barang.nama as kategori,golongan_barang.nama as golongan,databarang.kapasitas,  "+
                            " data_batch.no_batch,data_batch.no_faktur,data_batch.tgl_kadaluarsa,gudangbarang.stok,data_batch."+hppfarmasi+" as dasar "+
                            " from data_batch inner join databarang on data_batch.kode_brng=databarang.kode_brng "+
                            " inner join jenis on databarang.kdjns=jenis.kdjns "+
                            " inner join industrifarmasi on industrifarmasi.kode_industri=databarang.kode_industri "+
                            " inner join golongan_barang on databarang.kode_golongan=golongan_barang.kode "+
                            " inner join kategori_barang on databarang.kode_kategori=kategori_barang.kode "+
                            " inner join gudangbarang on gudangbarang.kode_brng=data_batch.kode_brng and gudangbarang.no_batch=data_batch.no_batch and gudangbarang.no_faktur=data_batch.no_faktur ";
                    }
                    psobat=koneksi.prepareStatement(
                        sql+" where gudangbarang.stok>0 and gudangbarang.kd_bangsal=? and databarang.kode_brng like ? or "+
                        " gudangbarang.stok>0 and gudangbarang.kd_bangsal=? and databarang.nama_brng like ? or "+
                        " gudangbarang.stok>0 and gudangbarang.kd_bangsal=? and kategori_barang.nama like ? or "+
                        " gudangbarang.stok>0 and gudangbarang.kd_bangsal=? and golongan_barang.nama like ? or "+
                        " gudangbarang.stok>0 and gudangbarang.kd_bangsal=? and data_batch.no_batch like ? or "+
                        " gudangbarang.stok>0 and gudangbarang.kd_bangsal=? and data_batch.no_faktur like ? or "+
                        " gudangbarang.stok>0 and gudangbarang.kd_bangsal=? and jenis.nama like ? order by data_batch.tgl_kadaluarsa");
                    try{    
                        psobat.setString(1,kdgudang.getText());
                        psobat.setString(2,"%"+TCari.getText().trim()+"%");
                        psobat.setString(3,kdgudang.getText());
                        psobat.setString(4,"%"+TCari.getText().trim()+"%");
                        psobat.setString(5,kdgudang.getText());
                        psobat.setString(6,"%"+TCari.getText().trim()+"%");
                        psobat.setString(7,kdgudang.getText());
                        psobat.setString(8,"%"+TCari.getText().trim()+"%");
                        psobat.setString(9,kdgudang.getText());
                        psobat.setString(10,"%"+TCari.getText().trim()+"%");
                        psobat.setString(11,kdgudang.getText());
                        psobat.setString(12,"%"+TCari.getText().trim()+"%");
                        psobat.setString(13,kdgudang.getText());
                        psobat.setString(14,"%"+TCari.getText().trim()+"%");
                        rs=psobat.executeQuery();
                        if(Jeniskelas.getSelectedItem().equals("Kelas 1")){
                            while(rs.next()){
                                tabModeDetailObatRacikan.addRow(new Object[] {
                                    tbObatRacikan.getValueAt(tbObatRacikan.getSelectedRow(),0).toString(),
                                    rs.getString("kode_brng"),rs.getString("nama_brng"),
                                    rs.getString("kode_sat"),Valid.roundUp(rs.getDouble("kelas1"),100),
                                    rs.getDouble("dasar"),rs.getString("nama"),rs.getDouble("stok"),
                                    rs.getDouble("kapasitas"),0,"/",0,"",0,0,0,rs.getString("nama_industri"),
                                    rs.getString("kategori"),rs.getString("golongan"),
                                    rs.getString("no_batch"),rs.getString("no_faktur"),rs.getString("tgl_kadaluarsa")
                                });
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("Kelas 2")){
                            while(rs.next()){
                                tabModeDetailObatRacikan.addRow(new Object[] {
                                    tbObatRacikan.getValueAt(tbObatRacikan.getSelectedRow(),0).toString(),
                                    rs.getString("kode_brng"),rs.getString("nama_brng"),
                                    rs.getString("kode_sat"),Valid.roundUp(rs.getDouble("kelas2"),100),
                                    rs.getDouble("dasar"),rs.getString("nama"),rs.getDouble("stok"),
                                    rs.getDouble("kapasitas"),0,"/",0,"",0,0,0,rs.getString("nama_industri"),
                                    rs.getString("kategori"),rs.getString("golongan"),
                                    rs.getString("no_batch"),rs.getString("no_faktur"),rs.getString("tgl_kadaluarsa")
                                });
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("Kelas 3")){
                            while(rs.next()){
                                    tabModeDetailObatRacikan.addRow(new Object[] {
                                        tbObatRacikan.getValueAt(tbObatRacikan.getSelectedRow(),0).toString(),
                                        rs.getString("kode_brng"),rs.getString("nama_brng"),
                                        rs.getString("kode_sat"),Valid.roundUp(rs.getDouble("kelas3"),100),
                                        rs.getDouble("dasar"),rs.getString("nama"),rs.getDouble("stok"),
                                        rs.getDouble("kapasitas"),0,"/",0,"",0,0,0,rs.getString("nama_industri"),
                                        rs.getString("kategori"),rs.getString("golongan"),
                                        rs.getString("no_batch"),rs.getString("no_faktur"),rs.getString("tgl_kadaluarsa")
                                    });
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("Utama/BPJS")){
                            while(rs.next()){
                                tabModeDetailObatRacikan.addRow(new Object[] {
                                    tbObatRacikan.getValueAt(tbObatRacikan.getSelectedRow(),0).toString(),
                                    rs.getString("kode_brng"),rs.getString("nama_brng"),
                                    rs.getString("kode_sat"),Valid.roundUp(rs.getDouble("utama"),100),
                                    rs.getDouble("dasar"),rs.getString("nama"),rs.getDouble("stok"),
                                    rs.getDouble("kapasitas"),0,"/",0,"",0,0,0,rs.getString("nama_industri"),
                                    rs.getString("kategori"),rs.getString("golongan"),
                                    rs.getString("no_batch"),rs.getString("no_faktur"),rs.getString("tgl_kadaluarsa")
                                });
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("VIP")){
                            while(rs.next()){
                                tabModeDetailObatRacikan.addRow(new Object[] {
                                    tbObatRacikan.getValueAt(tbObatRacikan.getSelectedRow(),0).toString(),
                                    rs.getString("kode_brng"),rs.getString("nama_brng"),
                                    rs.getString("kode_sat"),Valid.roundUp(rs.getDouble("vip"),100),
                                    rs.getDouble("dasar"),rs.getString("nama"),rs.getDouble("stok"),
                                    rs.getDouble("kapasitas"),0,"/",0,"",0,0,0,rs.getString("nama_industri"),
                                    rs.getString("kategori"),rs.getString("golongan"),
                                    rs.getString("no_batch"),rs.getString("no_faktur"),rs.getString("tgl_kadaluarsa")
                                });
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("VVIP")){
                            while(rs.next()){
                                tabModeDetailObatRacikan.addRow(new Object[] {
                                    tbObatRacikan.getValueAt(tbObatRacikan.getSelectedRow(),0).toString(),
                                    rs.getString("kode_brng"),rs.getString("nama_brng"),
                                    rs.getString("kode_sat"),Valid.roundUp(rs.getDouble("vvip"),100),
                                    rs.getDouble("dasar"),rs.getString("nama"),rs.getDouble("stok"),
                                    rs.getDouble("kapasitas"),0,"/",0,"",0,0,0,rs.getString("nama_industri"),
                                    rs.getString("kategori"),rs.getString("golongan"),
                                    rs.getString("no_batch"),rs.getString("no_faktur"),rs.getString("tgl_kadaluarsa")
                                });
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("Beli Luar")){
                            while(rs.next()){
                                tabModeDetailObatRacikan.addRow(new Object[] {
                                    tbObatRacikan.getValueAt(tbObatRacikan.getSelectedRow(),0).toString(),
                                    rs.getString("kode_brng"),rs.getString("nama_brng"),
                                    rs.getString("kode_sat"),Valid.roundUp(rs.getDouble("beliluar"),100),
                                    rs.getDouble("dasar"),rs.getString("nama"),rs.getDouble("stok"),
                                    rs.getDouble("kapasitas"),0,"/",0,"",0,0,0,rs.getString("nama_industri"),
                                    rs.getString("kategori"),rs.getString("golongan"),
                                    rs.getString("no_batch"),rs.getString("no_faktur"),rs.getString("tgl_kadaluarsa")
                                });
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("Karyawan")){
                            while(rs.next()){
                                tabModeDetailObatRacikan.addRow(new Object[] {
                                    tbObatRacikan.getValueAt(tbObatRacikan.getSelectedRow(),0).toString(),
                                    rs.getString("kode_brng"),rs.getString("nama_brng"),
                                    rs.getString("kode_sat"),Valid.roundUp(rs.getDouble("karyawan"),100),
                                    rs.getDouble("dasar"),rs.getString("nama"),rs.getDouble("stok"),
                                    rs.getDouble("kapasitas"),0,"/",0,"",0,0,0,rs.getString("nama_industri"),
                                    rs.getString("kategori"),rs.getString("golongan"),
                                    rs.getString("no_batch"),rs.getString("no_faktur"),rs.getString("tgl_kadaluarsa")
                                });
                            }
                        } 
                    }catch(Exception e){
                        System.out.println("Notifikasi : "+e);
                    }finally{
                        if(rs != null){
                            rs.close();
                        }
                        if(psobat != null){
                            psobat.close();
                        }
                    }
                }else{
                    if(aktifpcare.equals("yes")){
                        sql="select databarang.kode_brng, databarang.nama_brng,jenis.nama, databarang.kode_sat,databarang.kelas1,databarang.kelas2,databarang.kelas3,"+
                            " databarang.utama,databarang.vip,databarang.vvip,databarang.beliluar,databarang.karyawan,databarang."+hppfarmasi+" as dasar,gudangbarang.stok,"+
                            " databarang.letak_barang,industrifarmasi.nama_industri,databarang.h_beli,kategori_barang.nama as kategori,golongan_barang.nama as golongan,databarang.kapasitas  "+
                            " from databarang inner join jenis on databarang.kdjns=jenis.kdjns "+
                            " inner join industrifarmasi on industrifarmasi.kode_industri=databarang.kode_industri "+
                            " inner join golongan_barang on databarang.kode_golongan=golongan_barang.kode "+
                            " inner join kategori_barang on databarang.kode_kategori=kategori_barang.kode "+
                            " inner join maping_obat_pcare on maping_obat_pcare.kode_brng=databarang.kode_brng "+
                            " inner join gudangbarang on databarang.kode_brng=gudangbarang.kode_brng ";
                    }else{
                        sql="select databarang.kode_brng, databarang.nama_brng,jenis.nama, databarang.kode_sat,databarang.kelas1,databarang.kelas2,databarang.kelas3,"+
                            " databarang.utama,databarang.vip,databarang.vvip,databarang.beliluar,databarang.karyawan,databarang."+hppfarmasi+" as dasar,gudangbarang.stok,"+
                            " databarang.letak_barang,industrifarmasi.nama_industri,databarang.h_beli,kategori_barang.nama as kategori,golongan_barang.nama as golongan,databarang.kapasitas  "+
                            " from databarang inner join jenis on databarang.kdjns=jenis.kdjns "+
                            " inner join industrifarmasi on industrifarmasi.kode_industri=databarang.kode_industri "+
                            " inner join golongan_barang on databarang.kode_golongan=golongan_barang.kode "+
                            " inner join kategori_barang on databarang.kode_kategori=kategori_barang.kode "+
                            " inner join gudangbarang on databarang.kode_brng=gudangbarang.kode_brng ";
                    }
                    psobat=koneksi.prepareStatement(
                        sql+" where gudangbarang.no_batch='' and gudangbarang.no_faktur='' and gudangbarang.stok>0 and gudangbarang.kd_bangsal=? and databarang.status='1' and databarang.kode_brng like ? or "+
                        " gudangbarang.no_batch='' and gudangbarang.no_faktur='' and gudangbarang.stok>0 and gudangbarang.kd_bangsal=? and databarang.status='1' and databarang.nama_brng like ? or "+
                        " gudangbarang.no_batch='' and gudangbarang.no_faktur='' and gudangbarang.stok>0 and gudangbarang.kd_bangsal=? and databarang.status='1' and kategori_barang.nama like ? or "+
                        " gudangbarang.no_batch='' and gudangbarang.no_faktur='' and gudangbarang.stok>0 and gudangbarang.kd_bangsal=? and databarang.status='1' and golongan_barang.nama like ? or "+
                        " gudangbarang.no_batch='' and gudangbarang.no_faktur='' and gudangbarang.stok>0 and gudangbarang.kd_bangsal=? and databarang.status='1' and jenis.nama like ? order by databarang.nama_brng");
                    try{    
                        psobat.setString(1,kdgudang.getText());
                        psobat.setString(2,"%"+TCari.getText().trim()+"%");
                        psobat.setString(3,kdgudang.getText());
                        psobat.setString(4,"%"+TCari.getText().trim()+"%");
                        psobat.setString(5,kdgudang.getText());
                        psobat.setString(6,"%"+TCari.getText().trim()+"%");
                        psobat.setString(7,kdgudang.getText());
                        psobat.setString(8,"%"+TCari.getText().trim()+"%");
                        psobat.setString(9,kdgudang.getText());
                        psobat.setString(10,"%"+TCari.getText().trim()+"%");
                        rs=psobat.executeQuery();
                        if(Jeniskelas.getSelectedItem().equals("Kelas 1")){
                            while(rs.next()){
                                tabModeDetailObatRacikan.addRow(new Object[] {
                                    tbObatRacikan.getValueAt(tbObatRacikan.getSelectedRow(),0).toString(),
                                    rs.getString("kode_brng"),rs.getString("nama_brng"),
                                    rs.getString("kode_sat"),Valid.roundUp(rs.getDouble("kelas1"),100),
                                    rs.getDouble("dasar"),rs.getString("nama"),rs.getDouble("stok"),
                                    rs.getDouble("kapasitas"),0,"/",0,"",0,0,0,rs.getString("nama_industri"),
                                    rs.getString("kategori"),rs.getString("golongan"),"","",""
                                });
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("Kelas 2")){
                            while(rs.next()){
                                tabModeDetailObatRacikan.addRow(new Object[] {
                                    tbObatRacikan.getValueAt(tbObatRacikan.getSelectedRow(),0).toString(),
                                    rs.getString("kode_brng"),rs.getString("nama_brng"),
                                    rs.getString("kode_sat"),Valid.roundUp(rs.getDouble("kelas2"),100),
                                    rs.getDouble("dasar"),rs.getString("nama"),rs.getDouble("stok"),
                                    rs.getDouble("kapasitas"),0,"/",0,"",0,0,0,rs.getString("nama_industri"),
                                    rs.getString("kategori"),rs.getString("golongan"),"","",""
                                });
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("Kelas 3")){
                            while(rs.next()){
                                tabModeDetailObatRacikan.addRow(new Object[] {
                                    tbObatRacikan.getValueAt(tbObatRacikan.getSelectedRow(),0).toString(),
                                    rs.getString("kode_brng"),rs.getString("nama_brng"),
                                    rs.getString("kode_sat"),Valid.roundUp(rs.getDouble("kelas3"),100),
                                    rs.getDouble("dasar"),rs.getString("nama"),rs.getDouble("stok"),
                                    rs.getDouble("kapasitas"),0,"/",0,"",0,0,0,rs.getString("nama_industri"),
                                    rs.getString("kategori"),rs.getString("golongan"),"","",""
                                });
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("Utama/BPJS")){
                            while(rs.next()){
                                tabModeDetailObatRacikan.addRow(new Object[] {
                                    tbObatRacikan.getValueAt(tbObatRacikan.getSelectedRow(),0).toString(),
                                    rs.getString("kode_brng"),rs.getString("nama_brng"),
                                    rs.getString("kode_sat"),Valid.roundUp(rs.getDouble("utama"),100),
                                    rs.getDouble("dasar"),rs.getString("nama"),rs.getDouble("stok"),
                                    rs.getDouble("kapasitas"),0,"/",0,"",0,0,0,rs.getString("nama_industri"),
                                    rs.getString("kategori"),rs.getString("golongan"),"","",""
                                });
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("VIP")){
                            while(rs.next()){
                                tabModeDetailObatRacikan.addRow(new Object[] {
                                    tbObatRacikan.getValueAt(tbObatRacikan.getSelectedRow(),0).toString(),
                                    rs.getString("kode_brng"),rs.getString("nama_brng"),
                                    rs.getString("kode_sat"),Valid.roundUp(rs.getDouble("vip"),100),
                                    rs.getDouble("dasar"),rs.getString("nama"),rs.getDouble("stok"),
                                    rs.getDouble("kapasitas"),0,"/",0,"",0,0,0,rs.getString("nama_industri"),
                                    rs.getString("kategori"),rs.getString("golongan"),"","",""
                                });
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("VVIP")){
                            while(rs.next()){
                                tabModeDetailObatRacikan.addRow(new Object[] {
                                    tbObatRacikan.getValueAt(tbObatRacikan.getSelectedRow(),0).toString(),
                                    rs.getString("kode_brng"),rs.getString("nama_brng"),
                                    rs.getString("kode_sat"),Valid.roundUp(rs.getDouble("vvip"),100),
                                    rs.getDouble("dasar"),rs.getString("nama"),rs.getDouble("stok"),
                                    rs.getDouble("kapasitas"),0,"/",0,"",0,0,0,rs.getString("nama_industri"),
                                    rs.getString("kategori"),rs.getString("golongan"),"","",""
                                });
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("Beli Luar")){
                            while(rs.next()){
                                tabModeDetailObatRacikan.addRow(new Object[] {
                                    tbObatRacikan.getValueAt(tbObatRacikan.getSelectedRow(),0).toString(),
                                    rs.getString("kode_brng"),rs.getString("nama_brng"),
                                    rs.getString("kode_sat"),Valid.roundUp(rs.getDouble("beliluar"),100),
                                    rs.getDouble("dasar"),rs.getString("nama"),rs.getDouble("stok"),
                                    rs.getDouble("kapasitas"),0,"/",0,"",0,0,0,rs.getString("nama_industri"),
                                    rs.getString("kategori"),rs.getString("golongan"),"","",""
                                });
                            }
                        }else if(Jeniskelas.getSelectedItem().equals("Karyawan")){
                            while(rs.next()){
                                tabModeDetailObatRacikan.addRow(new Object[] {
                                    tbObatRacikan.getValueAt(tbObatRacikan.getSelectedRow(),0).toString(),
                                    rs.getString("kode_brng"),rs.getString("nama_brng"),
                                    rs.getString("kode_sat"),Valid.roundUp(rs.getDouble("karyawan"),100),
                                    rs.getDouble("dasar"),rs.getString("nama"),rs.getDouble("stok"),
                                    rs.getDouble("kapasitas"),0,"/",0,"",0,0,0,rs.getString("nama_industri"),
                                    rs.getString("kategori"),rs.getString("golongan"),"","",""
                                });
                            }
                        } 
                    }catch(Exception e){
                        System.out.println("Notifikasi : "+e);
                    }finally{
                        if(rs != null){
                            rs.close();
                        }
                        if(psobat != null){
                            psobat.close();
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }            
    }
   
    public void setPCare(String aktif,String nokunjung){
        aktifpcare=aktif;
        nokunjungan=nokunjung;
    }
}
