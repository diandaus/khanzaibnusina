/*
  Dilarang keras menggandakan/mengcopy/menyebarkan/membajak/mendecompile 
  Software ini dalam bentuk apapun tanpa seijin pembuat software
  (Khanza.Soft Media). Bagi yang sengaja membajak softaware ini ta
  npa ijin, kami sumpahi sial 1000 turunan, miskin sampai 500 turu
  nan. Selalu mendapat kecelakaan sampai 400 turunan. Anak pertama
  nya cacat tidak punya kaki sampai 300 turunan. Susah cari jodoh
  sampai umur 50 tahun sampai 200 turunan. Ya Alloh maafkan kami 
  karena telah berdoa buruk, semua ini kami lakukan karena kami ti
  dak pernah rela karya kami dibajak tanpa ijin.
 */

package kepegawaian;

import bridging.ApiPeruri;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.akses;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import restore.DlgRestorePetugas;
import simrskhanza.DlgCariJabatan;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets; // Untuk charset UTF-8
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import javax.mail.Authenticator;

/**
 *
 * @author dosen
 */
public final class DlgRegistrasiAkunPeruri extends javax.swing.JDialog {
    private final DefaultTableModel tabMode;
    private Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private PreparedStatement ps;
    private ResultSet rs;
    private DlgCariPegawai pegawai=new DlgCariPegawai(null,false);
    private String ktpPhotoBase64;
    private String npwpPhotoBase64;
    private String selfPhotoBase64;

    /** Creates new form DlgPetugas
     * @param parent
     * @param modal */
    public DlgRegistrasiAkunPeruri(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        this.setLocation(8,1);
        setSize(885,674);

        Object[] row={"Name", "Phone", "Email", "Password", "Type", "KTP", "KTP Photo", 
                "NPWP", "NPWP Photo", "Self Photo", "Address", "City", "Province", 
                "Gender", "Place of Birth", "Date of Birth", "Org Unit", "Work Unit", 
                "Position"};
        tabMode=new DefaultTableModel(null,row){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbPetugas.setModel(tabMode);


        //tbPetugas.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbPetugas.getBackground()));
        tbPetugas.setPreferredScrollableViewportSize(new Dimension(800,800));
        tbPetugas.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int i = 0; i < 19; i++) {
            TableColumn column = tbPetugas.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(200);
            }else if(i==1){
                column.setPreferredWidth(150);
            }else if(i==2){
                column.setPreferredWidth(200);
            }else if(i==3){
                column.setPreferredWidth(100);
            }else if(i==4){
                column.setPreferredWidth(200);
            }else if(i==5){
                column.setPreferredWidth(200);
            }else if(i==6){
                column.setPreferredWidth(200);
            }else if(i==7){
                column.setPreferredWidth(200);
            }else if(i==8){
                column.setPreferredWidth(200);
            }else if(i==9){
                column.setPreferredWidth(200);
            }else if(i==10){
                column.setPreferredWidth(200);
            }else if(i==11){
                column.setPreferredWidth(200);
            }else if(i==12){
                column.setPreferredWidth(200);
            }else if(i==13){
                column.setPreferredWidth(200);
            }else if(i==14){
                column.setPreferredWidth(200);
            }else if(i==15){
                column.setPreferredWidth(200);
            }else if(i==16){
                column.setPreferredWidth(200);
            }else if(i==17){
                column.setPreferredWidth(200);
            }else if(i==18){
                column.setPreferredWidth(200);
            }else if(i==19){
                column.setPreferredWidth(200);
            }
        }
        tbPetugas.setDefaultRenderer(Object.class, new WarnaTable());

        Name.setDocument(new batasInput((byte)50).getKata(Name));  
        Phone.setDocument(new batasInput((byte)15).getOnlyAngka(Phone));  
        Email.setDocument(new batasInput((byte)50).getKata(Email));  
        Password.setDocument(new batasInput((byte)20).getKata(Password));  
        Ktp.setDocument(new batasInput((byte)16).getOnlyAngka(Ktp)); 
        Npwp.setDocument(new batasInput((byte)15).getOnlyAngka(Npwp));  
        Address.setDocument(new batasInput((byte)100).getKata(Address));  
        City.setDocument(new batasInput((byte)50).getKata(City));  
        Province.setDocument(new batasInput((byte)50).getKata(Province));  
        PlaceOfBirth.setDocument(new batasInput((byte)50).getKata(PlaceOfBirth)); 
        OrgUnit.setDocument(new batasInput((byte)50).getKata(OrgUnit));  
        WorkUnit.setDocument(new batasInput((byte)50).getKata(WorkUnit));  
        Position.setDocument(new batasInput((byte)50).getKata(Position));
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
        
                       
        pegawai.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(pegawai.getTable().getSelectedRow()!= -1){                   
                    Name.setText(pegawai.tbKamar.getValueAt(pegawai.tbKamar.getSelectedRow(),1).toString());
                    OrgUnit.setText(pegawai.tbKamar.getValueAt(pegawai.tbKamar.getSelectedRow(),6).toString());
                    WorkUnit.setText(pegawai.tbKamar.getValueAt(pegawai.tbKamar.getSelectedRow(),5).toString());
                    Position.setText(pegawai.tbKamar.getValueAt(pegawai.tbKamar.getSelectedRow(),3).toString());
                    Ktp.setText(pegawai.tbKamar.getValueAt(pegawai.tbKamar.getSelectedRow(),23).toString());
                    Npwp.setText(pegawai.tbKamar.getValueAt(pegawai.tbKamar.getSelectedRow(),9).toString());
                    Gender.setSelectedItem(pegawai.tbKamar.getValueAt(pegawai.tbKamar.getSelectedRow(),2).toString().replaceAll("Wanita","PEREMPUAN").replaceAll("Pria","LAKI-LAKI"));
                    PlaceOfBirth.setText(pegawai.tbKamar.getValueAt(pegawai.tbKamar.getSelectedRow(),11).toString());
                    City.setText(pegawai.tbKamar.getValueAt(pegawai.tbKamar.getSelectedRow(),14).toString());
                    Address.setText(pegawai.tbKamar.getValueAt(pegawai.tbKamar.getSelectedRow(),13).toString());
                    Valid.SetTgl(DateOfBirth,pegawai.tbKamar.getValueAt(pegawai.tbKamar.getSelectedRow(),12).toString());
                }   
                Name.requestFocus();
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
        
        pegawai.getTable().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_SPACE){
                    pegawai.dispose();
                }                
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });
        ChkInput.setSelected(false);
        isForm(); 
    }
    
    private DlgCariJabatan jabatan=new DlgCariJabatan(null,false);

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Popup = new javax.swing.JPopupMenu();
        MnSendLinkRegistration = new javax.swing.JMenuItem();
        MnSendSpeciment = new javax.swing.JMenuItem();
        MnCheckCertificate = new javax.swing.JMenuItem();
        internalFrame1 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        tbPetugas = new widget.Table();
        jPanel3 = new javax.swing.JPanel();
        panelGlass8 = new widget.panelisi();
        BtnSimpan = new widget.Button();
        BtnBatal = new widget.Button();
        BtnHapus = new widget.Button();
        BtnEdit = new widget.Button();
        BtnPrint = new widget.Button();
        BtnAll = new widget.Button();
        jLabel10 = new widget.Label();
        LCount = new widget.Label();
        BtnKeluar = new widget.Button();
        panelGlass9 = new widget.panelisi();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel6 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        PanelInput = new javax.swing.JPanel();
        FormInput = new widget.PanelBiasa();
        jLabel4 = new widget.Label();
        PlaceOfBirth = new widget.TextBox();
        Gender = new widget.ComboBox();
        jLabel8 = new widget.Label();
        jLabel13 = new widget.Label();
        DateOfBirth = new widget.Tanggal();
        Type = new widget.ComboBox();
        jLabel12 = new widget.Label();
        Name = new widget.TextBox();
        BtnCariPhoto = new widget.Button();
        jLabel5 = new widget.Label();
        jLabel15 = new widget.Label();
        City = new widget.TextBox();
        jLabel17 = new widget.Label();
        jLabel22 = new widget.Label();
        jLabel23 = new widget.Label();
        jLabel24 = new widget.Label();
        jLabel25 = new widget.Label();
        jLabel26 = new widget.Label();
        jLabel27 = new widget.Label();
        jLabel28 = new widget.Label();
        jLabel29 = new widget.Label();
        jLabel30 = new widget.Label();
        Phone = new widget.TextBox();
        Email = new widget.TextBox();
        Password = new widget.TextBox();
        Ktp = new widget.TextBox();
        KtpPhoto = new widget.TextBox();
        Npwp = new widget.TextBox();
        NpwpPhoto = new widget.TextBox();
        SelfPhoto = new widget.TextBox();
        Address = new widget.TextBox();
        Province = new widget.TextBox();
        BtnCariPegawai = new widget.Button();
        BtnCariPhotoKtp = new widget.Button();
        jLabel31 = new widget.Label();
        jLabel32 = new widget.Label();
        OrgUnit = new widget.TextBox();
        WorkUnit = new widget.TextBox();
        BtnCariPhotoNpwp = new widget.Button();
        Position = new widget.TextBox();
        ChkInput = new widget.CekBox();

        Popup.setName("Popup"); // NOI18N

        MnSendLinkRegistration.setBackground(new java.awt.Color(255, 255, 254));
        MnSendLinkRegistration.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnSendLinkRegistration.setForeground(new java.awt.Color(50, 50, 50));
        MnSendLinkRegistration.setText("Kirim Link Registrasi");
        MnSendLinkRegistration.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        MnSendLinkRegistration.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        MnSendLinkRegistration.setName("MnSendLinkRegistration"); // NOI18N
        MnSendLinkRegistration.setPreferredSize(new java.awt.Dimension(180, 28));
        MnSendLinkRegistration.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnSendLinkRegistrationActionPerformed(evt);
            }
        });
        Popup.add(MnSendLinkRegistration);

        MnSendSpeciment.setBackground(new java.awt.Color(255, 255, 254));
        MnSendSpeciment.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnSendSpeciment.setForeground(new java.awt.Color(50, 50, 50));
        MnSendSpeciment.setText("Masukan Barcode/TTD");
        MnSendSpeciment.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        MnSendSpeciment.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        MnSendSpeciment.setName("MnSendSpeciment"); // NOI18N
        MnSendSpeciment.setPreferredSize(new java.awt.Dimension(180, 28));
        MnSendSpeciment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnSendSpecimentActionPerformed(evt);
            }
        });
        Popup.add(MnSendSpeciment);

        MnCheckCertificate.setBackground(new java.awt.Color(255, 255, 254));
        MnCheckCertificate.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnCheckCertificate.setForeground(new java.awt.Color(50, 50, 50));
        MnCheckCertificate.setText("Cek Status Sertifikat");
        MnCheckCertificate.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        MnCheckCertificate.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        MnCheckCertificate.setName("MnCheckCertificate"); // NOI18N
        MnCheckCertificate.setPreferredSize(new java.awt.Dimension(180, 28));
        MnCheckCertificate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnCheckCertificateActionPerformed(evt);
            }
        });
        Popup.add(MnCheckCertificate);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Data Petugas ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setComponentPopupMenu(Popup);
        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);

        tbPetugas.setAutoCreateRowSorter(true);
        tbPetugas.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbPetugas.setComponentPopupMenu(Popup);
        tbPetugas.setName("tbPetugas"); // NOI18N
        tbPetugas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbPetugasMouseClicked(evt);
            }
        });
        tbPetugas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbPetugasKeyReleased(evt);
            }
        });
        Scroll.setViewportView(tbPetugas);

        internalFrame1.add(Scroll, java.awt.BorderLayout.CENTER);

        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(44, 100));
        jPanel3.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass8.setName("panelGlass8"); // NOI18N
        panelGlass8.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        BtnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnSimpan.setMnemonic('S');
        BtnSimpan.setText("Simpan");
        BtnSimpan.setToolTipText("Alt+S");
        BtnSimpan.setName("BtnSimpan"); // NOI18N
        BtnSimpan.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSimpanActionPerformed(evt);
            }
        });
        BtnSimpan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnSimpanKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnSimpan);

        BtnBatal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Cancel-2-16x16.png"))); // NOI18N
        BtnBatal.setMnemonic('B');
        BtnBatal.setText("Baru");
        BtnBatal.setToolTipText("Alt+B");
        BtnBatal.setName("BtnBatal"); // NOI18N
        BtnBatal.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnBatalActionPerformed(evt);
            }
        });
        BtnBatal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnBatalKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnBatal);

        BtnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        BtnHapus.setMnemonic('H');
        BtnHapus.setText("Hapus");
        BtnHapus.setToolTipText("Alt+H");
        BtnHapus.setName("BtnHapus"); // NOI18N
        BtnHapus.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnHapusActionPerformed(evt);
            }
        });
        BtnHapus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnHapusKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnHapus);

        BtnEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/inventaris.png"))); // NOI18N
        BtnEdit.setMnemonic('G');
        BtnEdit.setText("Ganti");
        BtnEdit.setToolTipText("Alt+G");
        BtnEdit.setName("BtnEdit"); // NOI18N
        BtnEdit.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnEditActionPerformed(evt);
            }
        });
        BtnEdit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnEditKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnEdit);

        BtnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/b_print.png"))); // NOI18N
        BtnPrint.setMnemonic('T');
        BtnPrint.setText("Cetak");
        BtnPrint.setToolTipText("Alt+T");
        BtnPrint.setName("BtnPrint"); // NOI18N
        BtnPrint.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPrintActionPerformed(evt);
            }
        });
        BtnPrint.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnPrintKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnPrint);

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

        jLabel10.setText("Record :");
        jLabel10.setName("jLabel10"); // NOI18N
        jLabel10.setPreferredSize(new java.awt.Dimension(70, 30));
        panelGlass8.add(jLabel10);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(72, 30));
        panelGlass8.add(LCount);

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

        jPanel3.add(panelGlass8, java.awt.BorderLayout.CENTER);

        panelGlass9.setName("panelGlass9"); // NOI18N
        panelGlass9.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 10));

        jSeparator5.setBackground(new java.awt.Color(220, 225, 215));
        jSeparator5.setForeground(new java.awt.Color(220, 225, 215));
        jSeparator5.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator5.setName("jSeparator5"); // NOI18N
        jSeparator5.setOpaque(true);
        jSeparator5.setPreferredSize(new java.awt.Dimension(1, 23));
        panelGlass9.add(jSeparator5);

        jLabel6.setText("Key Word :");
        jLabel6.setName("jLabel6"); // NOI18N
        jLabel6.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass9.add(jLabel6);

        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(240, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKeyPressed(evt);
            }
        });
        panelGlass9.add(TCari);

        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari.setMnemonic('2');
        BtnCari.setToolTipText("Alt+2");
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
        panelGlass9.add(BtnCari);

        jPanel3.add(panelGlass9, java.awt.BorderLayout.PAGE_START);

        internalFrame1.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        PanelInput.setName("PanelInput"); // NOI18N
        PanelInput.setOpaque(false);
        PanelInput.setLayout(new java.awt.BorderLayout(1, 1));

        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(850, 137));
        FormInput.setLayout(null);

        jLabel4.setText("Nama Petugas :");
        jLabel4.setName("jLabel4"); // NOI18N
        FormInput.add(jLabel4);
        jLabel4.setBounds(10, 10, 95, 23);

        PlaceOfBirth.setName("PlaceOfBirth"); // NOI18N
        PlaceOfBirth.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PlaceOfBirthKeyPressed(evt);
            }
        });
        FormInput.add(PlaceOfBirth);
        PlaceOfBirth.setBounds(460, 160, 200, 23);

        Gender.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "LAKI-LAKI", "PEREMPUAN" }));
        Gender.setName("Gender"); // NOI18N
        Gender.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                GenderKeyPressed(evt);
            }
        });
        FormInput.add(Gender);
        Gender.setBounds(470, 130, 140, 23);

        jLabel8.setText("Jenis Kelamin :");
        jLabel8.setName("jLabel8"); // NOI18N
        FormInput.add(jLabel8);
        jLabel8.setBounds(360, 130, 95, 23);

        jLabel13.setText("Tmp/Tgl. Lahir :");
        jLabel13.setName("jLabel13"); // NOI18N
        FormInput.add(jLabel13);
        jLabel13.setBounds(360, 160, 95, 23);

        DateOfBirth.setForeground(new java.awt.Color(50, 70, 50));
        DateOfBirth.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "03-01-2025" }));
        DateOfBirth.setDisplayFormat("dd-MM-yyyy");
        DateOfBirth.setName("DateOfBirth"); // NOI18N
        DateOfBirth.setOpaque(false);
        DateOfBirth.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DateOfBirthKeyPressed(evt);
            }
        });
        FormInput.add(DateOfBirth);
        DateOfBirth.setBounds(670, 160, 100, 23);

        Type.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "INDIVIDUAL" }));
        Type.setLightWeightPopupEnabled(false);
        Type.setName("Type"); // NOI18N
        Type.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TypeKeyPressed(evt);
            }
        });
        FormInput.add(Type);
        Type.setBounds(120, 130, 125, 23);

        jLabel12.setText("Jabatan  :");
        jLabel12.setName("jLabel12"); // NOI18N
        FormInput.add(jLabel12);
        jLabel12.setBounds(375, 250, 80, 23);

        Name.setHighlighter(null);
        Name.setName("Name"); // NOI18N
        Name.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NameKeyPressed(evt);
            }
        });
        FormInput.add(Name);
        Name.setBounds(110, 10, 200, 23);

        BtnCariPhoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnCariPhoto.setMnemonic('1');
        BtnCariPhoto.setToolTipText("ALt+1");
        BtnCariPhoto.setName("BtnCariPhoto"); // NOI18N
        BtnCariPhoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCariPhotoActionPerformed(evt);
            }
        });
        FormInput.add(BtnCariPhoto);
        BtnCariPhoto.setBounds(660, 10, 28, 23);

        jLabel5.setText("Email :");
        jLabel5.setName("jLabel5"); // NOI18N
        FormInput.add(jLabel5);
        jLabel5.setBounds(20, 70, 80, 23);

        jLabel15.setText("type :");
        jLabel15.setName("jLabel15"); // NOI18N
        FormInput.add(jLabel15);
        jLabel15.setBounds(20, 130, 80, 23);

        City.setHighlighter(null);
        City.setName("City"); // NOI18N
        City.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CityKeyPressed(evt);
            }
        });
        FormInput.add(City);
        City.setBounds(460, 70, 200, 23);

        jLabel17.setText("Phone :");
        jLabel17.setName("jLabel17"); // NOI18N
        FormInput.add(jLabel17);
        jLabel17.setBounds(20, 40, 80, 23);

        jLabel22.setText("Password :");
        jLabel22.setName("jLabel22"); // NOI18N
        FormInput.add(jLabel22);
        jLabel22.setBounds(20, 100, 80, 23);

        jLabel23.setText("KTP :");
        jLabel23.setName("jLabel23"); // NOI18N
        FormInput.add(jLabel23);
        jLabel23.setBounds(20, 160, 80, 23);

        jLabel24.setText("Foto KTP :");
        jLabel24.setName("jLabel24"); // NOI18N
        FormInput.add(jLabel24);
        jLabel24.setBounds(20, 190, 80, 23);

        jLabel25.setText("NPWP :");
        jLabel25.setName("jLabel25"); // NOI18N
        FormInput.add(jLabel25);
        jLabel25.setBounds(20, 220, 80, 23);

        jLabel26.setText("Foto NPWP :");
        jLabel26.setName("jLabel26"); // NOI18N
        FormInput.add(jLabel26);
        jLabel26.setBounds(20, 250, 80, 23);

        jLabel27.setText("Foto Profil :");
        jLabel27.setName("jLabel27"); // NOI18N
        FormInput.add(jLabel27);
        jLabel27.setBounds(370, 10, 80, 23);

        jLabel28.setText("Alamat :");
        jLabel28.setName("jLabel28"); // NOI18N
        FormInput.add(jLabel28);
        jLabel28.setBounds(370, 40, 80, 23);

        jLabel29.setText("Kota :");
        jLabel29.setName("jLabel29"); // NOI18N
        FormInput.add(jLabel29);
        jLabel29.setBounds(370, 70, 80, 23);

        jLabel30.setText("Provinsi :");
        jLabel30.setName("jLabel30"); // NOI18N
        FormInput.add(jLabel30);
        jLabel30.setBounds(370, 100, 80, 23);

        Phone.setHighlighter(null);
        Phone.setName("Phone"); // NOI18N
        Phone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PhoneKeyPressed(evt);
            }
        });
        FormInput.add(Phone);
        Phone.setBounds(110, 40, 200, 23);

        Email.setHighlighter(null);
        Email.setName("Email"); // NOI18N
        Email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EmailActionPerformed(evt);
            }
        });
        Email.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                EmailKeyPressed(evt);
            }
        });
        FormInput.add(Email);
        Email.setBounds(110, 70, 200, 23);

        Password.setHighlighter(null);
        Password.setName("Password"); // NOI18N
        Password.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PasswordKeyPressed(evt);
            }
        });
        FormInput.add(Password);
        Password.setBounds(110, 100, 200, 23);

        Ktp.setHighlighter(null);
        Ktp.setName("Ktp"); // NOI18N
        Ktp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KtpKeyPressed(evt);
            }
        });
        FormInput.add(Ktp);
        Ktp.setBounds(110, 160, 200, 23);

        KtpPhoto.setHighlighter(null);
        KtpPhoto.setName("KtpPhoto"); // NOI18N
        KtpPhoto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KtpPhotoKeyPressed(evt);
            }
        });
        FormInput.add(KtpPhoto);
        KtpPhoto.setBounds(110, 190, 200, 23);

        Npwp.setHighlighter(null);
        Npwp.setName("Npwp"); // NOI18N
        Npwp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NpwpKeyPressed(evt);
            }
        });
        FormInput.add(Npwp);
        Npwp.setBounds(110, 220, 200, 23);

        NpwpPhoto.setHighlighter(null);
        NpwpPhoto.setName("NpwpPhoto"); // NOI18N
        NpwpPhoto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NpwpPhotoKeyPressed(evt);
            }
        });
        FormInput.add(NpwpPhoto);
        NpwpPhoto.setBounds(110, 250, 200, 23);

        SelfPhoto.setHighlighter(null);
        SelfPhoto.setName("SelfPhoto"); // NOI18N
        SelfPhoto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                SelfPhotoKeyPressed(evt);
            }
        });
        FormInput.add(SelfPhoto);
        SelfPhoto.setBounds(460, 10, 200, 23);

        Address.setHighlighter(null);
        Address.setName("Address"); // NOI18N
        Address.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                AddressKeyPressed(evt);
            }
        });
        FormInput.add(Address);
        Address.setBounds(460, 40, 200, 23);

        Province.setHighlighter(null);
        Province.setName("Province"); // NOI18N
        Province.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ProvinceKeyPressed(evt);
            }
        });
        FormInput.add(Province);
        Province.setBounds(460, 100, 200, 23);

        BtnCariPegawai.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnCariPegawai.setMnemonic('1');
        BtnCariPegawai.setToolTipText("ALt+1");
        BtnCariPegawai.setName("BtnCariPegawai"); // NOI18N
        BtnCariPegawai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCariPegawaiActionPerformed(evt);
            }
        });
        FormInput.add(BtnCariPegawai);
        BtnCariPegawai.setBounds(310, 10, 28, 23);

        BtnCariPhotoKtp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnCariPhotoKtp.setMnemonic('1');
        BtnCariPhotoKtp.setToolTipText("ALt+1");
        BtnCariPhotoKtp.setName("BtnCariPhotoKtp"); // NOI18N
        BtnCariPhotoKtp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCariPhotoKtpActionPerformed(evt);
            }
        });
        FormInput.add(BtnCariPhotoKtp);
        BtnCariPhotoKtp.setBounds(310, 190, 28, 23);

        jLabel31.setText("Unit Organisasi :");
        jLabel31.setName("jLabel31"); // NOI18N
        FormInput.add(jLabel31);
        jLabel31.setBounds(350, 190, 105, 23);

        jLabel32.setText("Unit Kerja :");
        jLabel32.setName("jLabel32"); // NOI18N
        FormInput.add(jLabel32);
        jLabel32.setBounds(350, 220, 105, 23);

        OrgUnit.setHighlighter(null);
        OrgUnit.setName("OrgUnit"); // NOI18N
        OrgUnit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                OrgUnitKeyPressed(evt);
            }
        });
        FormInput.add(OrgUnit);
        OrgUnit.setBounds(460, 190, 200, 23);

        WorkUnit.setHighlighter(null);
        WorkUnit.setName("WorkUnit"); // NOI18N
        WorkUnit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                WorkUnitKeyPressed(evt);
            }
        });
        FormInput.add(WorkUnit);
        WorkUnit.setBounds(460, 220, 200, 23);

        BtnCariPhotoNpwp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnCariPhotoNpwp.setMnemonic('1');
        BtnCariPhotoNpwp.setToolTipText("ALt+1");
        BtnCariPhotoNpwp.setName("BtnCariPhotoNpwp"); // NOI18N
        BtnCariPhotoNpwp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCariPhotoNpwpActionPerformed(evt);
            }
        });
        FormInput.add(BtnCariPhotoNpwp);
        BtnCariPhotoNpwp.setBounds(310, 250, 28, 23);

        Position.setHighlighter(null);
        Position.setName("Position"); // NOI18N
        Position.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                PositionKeyPressed(evt);
            }
        });
        FormInput.add(Position);
        Position.setBounds(460, 250, 200, 23);

        PanelInput.add(FormInput, java.awt.BorderLayout.CENTER);

        ChkInput.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput.setMnemonic('I');
        ChkInput.setText(".: Input Data");
        ChkInput.setToolTipText("Alt+I");
        ChkInput.setBorderPainted(true);
        ChkInput.setBorderPaintedFlat(true);
        ChkInput.setFocusable(false);
        ChkInput.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ChkInput.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ChkInput.setName("ChkInput"); // NOI18N
        ChkInput.setPreferredSize(new java.awt.Dimension(192, 20));
        ChkInput.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkInputActionPerformed(evt);
            }
        });
        PanelInput.add(ChkInput, java.awt.BorderLayout.PAGE_END);

        internalFrame1.add(PanelInput, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);
        internalFrame1.getAccessibleContext().setAccessibleName("::[ Data Registrasi Akun Peruri ]::");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void PlaceOfBirthKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PlaceOfBirthKeyPressed
        Valid.pindah(evt,Province,DateOfBirth);
}//GEN-LAST:event_PlaceOfBirthKeyPressed

    private void GenderKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_GenderKeyPressed
        Valid.pindah(evt,Province,PlaceOfBirth);
}//GEN-LAST:event_GenderKeyPressed

    private void DateOfBirthKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DateOfBirthKeyPressed
        Valid.pindah(evt,PlaceOfBirth,OrgUnit);
}//GEN-LAST:event_DateOfBirthKeyPressed

    private void TypeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TypeKeyPressed
        Valid.pindah(evt,Password,Ktp);
}//GEN-LAST:event_TypeKeyPressed

    private void NameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NameKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_UP){  
            BtnCariPegawaiActionPerformed(null);
        }else{
            Valid.pindah(evt,TCari,Phone,TCari);
        }
}//GEN-LAST:event_NameKeyPressed
       
    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
      if (Name.getText().trim().isEmpty()) {
        Valid.textKosong(Name, "Name");
        return; // Hentikan eksekusi jika field kosong
    }
    if (Phone.getText().trim().isEmpty()) {
        Valid.textKosong(Phone, "Phone");
        return; // Hentikan eksekusi jika field kosong
    }
    if (Email.getText().trim().isEmpty()) {
        Valid.textKosong(Email, "Email");
        return; // Hentikan eksekusi jika field kosong
    }
    if (Password.getText().trim().isEmpty()) {
        Valid.textKosong(Password, "Password");
        return; // Hentikan eksekusi jika field kosong
    }
    if (Ktp.getText().trim().isEmpty()) {
        Valid.textKosong(Ktp, "KTP");
        return; // Hentikan eksekusi jika field kosong
    }
    if (PlaceOfBirth.getText().trim().isEmpty()) {
        Valid.textKosong(PlaceOfBirth, "Place of Birth");
        return; // Hentikan eksekusi jika field kosong
    }
    if (DateOfBirth.getDate() == null) {
        Valid.textKosong(DateOfBirth, "Date of Birth");
        return; // Hentikan eksekusi jika field kosong
    }

    // Menentukan nilai gender
    String genderValue = "M"; // Default ke 'M' (LAKI-LAKI)
    if (Gender.getSelectedItem() != null) {
        if (Gender.getSelectedItem().toString().equalsIgnoreCase("PEREMPUAN")) {
            genderValue = "F"; // Set ke 'F' jika PEREMPUAN
        }
    }

    // Mengonversi tanggal lahir ke format yang benar
    String dateOfBirthValue = null;
    if (DateOfBirth.getDate() != null) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        dateOfBirthValue = sdf.format(DateOfBirth.getDate());
    }

    // Mengonversi foto ke Base64
    String ktpPhotoBase64 = convertImageToBase64(KtpPhoto.getText());
    String npwpPhotoBase64 = convertImageToBase64(NpwpPhoto.getText());
    String selfPhotoBase64 = convertImageToBase64(SelfPhoto.getText());

    // Simpan data ke database
    if (Sequel.menyimpantf("akun_peruri", "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?", "Registrasi Akun", 19, new String[]{
        Name.getText(), Phone.getText(), Email.getText(), Password.getText(), Type.getSelectedItem().toString(),
        Ktp.getText(), ktpPhotoBase64, Npwp.getText(), npwpPhotoBase64, selfPhotoBase64,
        Address.getText(), City.getText(), Province.getText(), genderValue,
        PlaceOfBirth.getText(), dateOfBirthValue,
        OrgUnit.getText(), WorkUnit.getText(), Position.getText()
    }) == true) {

        // Tambahkan data ke tabMode
        tabMode.addRow(new String[]{
            Name.getText(), Phone.getText(), Email.getText(), Password.getText(), Type.getSelectedItem().toString(),
            Ktp.getText(), ktpPhotoBase64, Npwp.getText(), npwpPhotoBase64, selfPhotoBase64,
            Address.getText(), City.getText(), Province.getText(), genderValue,
            PlaceOfBirth.getText(), dateOfBirthValue,
            OrgUnit.getText(), WorkUnit.getText(), Position.getText()
        });

        emptTeks();
        LCount.setText("" + tabMode.getRowCount());

        // Tampilkan pesan sukses jika data berhasil disimpan
        JOptionPane.showMessageDialog(this, "Data berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
    } else {
        JOptionPane.showMessageDialog(this, "Data gagal disimpan.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
    }
}//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnSimpanActionPerformed(null);
        }else{
            Valid.pindah(evt,Position,BtnBatal);
        }
}//GEN-LAST:event_BtnSimpanKeyPressed

    private void BtnBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnBatalActionPerformed
        ChkInput.setSelected(true);
        isForm(); 
        emptTeks();        
}//GEN-LAST:event_BtnBatalActionPerformed

    private void BtnBatalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnBatalKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            emptTeks();
        }else{Valid.pindah(evt, BtnSimpan, BtnHapus);}
}//GEN-LAST:event_BtnBatalKeyPressed

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed

}//GEN-LAST:event_BtnHapusActionPerformed

    private void BtnHapusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapusKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnHapusActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnBatal, BtnEdit);
        }
}//GEN-LAST:event_BtnHapusKeyPressed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
        /*if(tabMode.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis. Tidak ada data yang bisa anda print...!!!!");
            BtnBatal.requestFocus();
        }else if(tabMode.getRowCount()!=0){
                this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                Map<String, Object> param = new HashMap<>();  
                param.put("namars",akses.getnamars());
                param.put("alamatrs",akses.getalamatrs());
                param.put("kotars",akses.getkabupatenrs());
                param.put("propinsirs",akses.getpropinsirs());
                param.put("kontakrs",akses.getkontakrs());
                param.put("emailrs",akses.getemailrs());   
                param.put("logo",Sequel.cariGambar("select setting.logo from setting")); 
                Valid.MyReportqry("rptPetugas.jasper","report","::[ Data Petugas ]::","select petugas.nip,petugas.nama,petugas.jk,petugas.tmp_lahir,petugas.tgl_lahir, "+
                    "petugas.gol_darah,petugas.agama,petugas.stts_nikah,petugas.alamat,jabatan.nm_jbtn,petugas.no_telp "+
                    "from petugas inner join jabatan on jabatan.kd_jbtn=petugas.kd_jbtn "+
                    "where petugas.status='1' and petugas.jk like '%"+cmbCrJk.getSelectedItem().toString().replaceAll("LAKI-LAKI","L").replaceAll("PEREMPUAN","P").trim()+"%' and petugas.gol_darah like '%"+CmbCrGd.getSelectedItem().toString().trim()+"%' and petugas.stts_nikah like '%"+CmbCrStts.getSelectedItem().toString().trim()+"%' and petugas.nip like '%"+TCari.getText().trim()+"%' or "+
                    " petugas.status='1' and petugas.jk like '%"+cmbCrJk.getSelectedItem().toString().replaceAll("LAKI-LAKI","L").replaceAll("PEREMPUAN","P").trim()+"%' and petugas.gol_darah like '%"+CmbCrGd.getSelectedItem().toString().trim()+"%' and petugas.stts_nikah like '%"+CmbCrStts.getSelectedItem().toString().trim()+"%' and petugas.nama like '%"+TCari.getText().trim()+"%' or "+
                    " petugas.status='1' and petugas.jk like '%"+cmbCrJk.getSelectedItem().toString().replaceAll("LAKI-LAKI","L").replaceAll("PEREMPUAN","P").trim()+"%' and petugas.gol_darah like '%"+CmbCrGd.getSelectedItem().toString().trim()+"%' and petugas.stts_nikah like '%"+CmbCrStts.getSelectedItem().toString().trim()+"%' and petugas.jk like '%"+TCari.getText().trim()+"%' or "+
                    " petugas.status='1' and petugas.jk like '%"+cmbCrJk.getSelectedItem().toString().replaceAll("LAKI-LAKI","L").replaceAll("PEREMPUAN","P").trim()+"%' and petugas.gol_darah like '%"+CmbCrGd.getSelectedItem().toString().trim()+"%' and petugas.stts_nikah like '%"+CmbCrStts.getSelectedItem().toString().trim()+"%' and petugas.tmp_lahir like '%"+TCari.getText().trim()+"%' or "+
                    " petugas.status='1' and petugas.jk like '%"+cmbCrJk.getSelectedItem().toString().replaceAll("LAKI-LAKI","L").replaceAll("PEREMPUAN","P").trim()+"%' and petugas.gol_darah like '%"+CmbCrGd.getSelectedItem().toString().trim()+"%' and petugas.stts_nikah like '%"+CmbCrStts.getSelectedItem().toString().trim()+"%' and petugas.tgl_lahir like '%"+TCari.getText().trim()+"%' or "+
                    " petugas.status='1' and petugas.jk like '%"+cmbCrJk.getSelectedItem().toString().replaceAll("LAKI-LAKI","L").replaceAll("PEREMPUAN","P").trim()+"%' and petugas.gol_darah like '%"+CmbCrGd.getSelectedItem().toString().trim()+"%' and petugas.stts_nikah like '%"+CmbCrStts.getSelectedItem().toString().trim()+"%' and petugas.gol_darah like '%"+TCari.getText().trim()+"%' or "+
                    " petugas.status='1' and petugas.jk like '%"+cmbCrJk.getSelectedItem().toString().replaceAll("LAKI-LAKI","L").replaceAll("PEREMPUAN","P").trim()+"%' and petugas.gol_darah like '%"+CmbCrGd.getSelectedItem().toString().trim()+"%' and petugas.stts_nikah like '%"+CmbCrStts.getSelectedItem().toString().trim()+"%' and petugas.agama like '%"+TCari.getText().trim()+"%' or "+
                    " petugas.status='1' and petugas.jk like '%"+cmbCrJk.getSelectedItem().toString().replaceAll("LAKI-LAKI","L").replaceAll("PEREMPUAN","P").trim()+"%' and petugas.gol_darah like '%"+CmbCrGd.getSelectedItem().toString().trim()+"%' and petugas.stts_nikah like '%"+CmbCrStts.getSelectedItem().toString().trim()+"%' and petugas.alamat like '%"+TCari.getText().trim()+"%' or "+
                    " petugas.status='1' and petugas.jk like '%"+cmbCrJk.getSelectedItem().toString().replaceAll("LAKI-LAKI","L").replaceAll("PEREMPUAN","P").trim()+"%' and petugas.gol_darah like '%"+CmbCrGd.getSelectedItem().toString().trim()+"%' and petugas.stts_nikah like '%"+CmbCrStts.getSelectedItem().toString().trim()+"%' and petugas.no_telp like '%"+TCari.getText().trim()+"%' or "+
                    " petugas.status='1' and petugas.jk like '%"+cmbCrJk.getSelectedItem().toString().replaceAll("LAKI-LAKI","L").replaceAll("PEREMPUAN","P").trim()+"%' and petugas.gol_darah like '%"+CmbCrGd.getSelectedItem().toString().trim()+"%' and petugas.stts_nikah like '%"+CmbCrStts.getSelectedItem().toString().trim()+"%' and jabatan.nm_jbtn like '%"+TCari.getText().trim()+"%' order by petugas.nip",param);
                this.setCursor(Cursor.getDefaultCursor());
        }*/
        
}//GEN-LAST:event_BtnPrintActionPerformed

    private void BtnPrintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPrintKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnPrintActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnHapus, BtnAll);
        }
}//GEN-LAST:event_BtnPrintKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            dispose();
        }else{Valid.pindah(evt,BtnPrint,TCari);}
}//GEN-LAST:event_BtnKeluarKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
          TCari.setText("");
        tampil();
}//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnAllActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnPrint, BtnKeluar);
        }
}//GEN-LAST:event_BtnAllKeyPressed

    private void BtnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnEditActionPerformed
     int selectedRow = tbPetugas.getSelectedRow(); // Ambil baris yang dipilih dari JTable
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Silakan pilih baris dari tabel untuk mengedit.", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return; // Hentikan eksekusi jika tidak ada baris yang dipilih
    }

    // Ambil data dari tabel dan masukkan ke dalam form input
    Name.setText(tabMode.getValueAt(selectedRow, 0).toString());
    Phone.setText(tabMode.getValueAt(selectedRow, 1).toString());
    Email.setText(tabMode.getValueAt(selectedRow, 2).toString());
    Password.setText(tabMode.getValueAt(selectedRow, 3).toString());
    Type.setSelectedItem(tabMode.getValueAt(selectedRow, 4).toString());
    Ktp.setText(tabMode.getValueAt(selectedRow, 5).toString());
    KtpPhoto.setText(tabMode.getValueAt(selectedRow, 6).toString());
    Npwp.setText(tabMode.getValueAt(selectedRow, 7).toString());
    NpwpPhoto.setText(tabMode.getValueAt(selectedRow, 8).toString());
    SelfPhoto.setText(tabMode.getValueAt(selectedRow, 9).toString());
    Address.setText(tabMode.getValueAt(selectedRow, 10).toString());
    City.setText(tabMode.getValueAt(selectedRow, 11).toString());
    Province.setText(tabMode.getValueAt(selectedRow, 12).toString());
    String gender = tabMode.getValueAt(selectedRow, 13).toString();
    Gender.setSelectedItem(gender.equals("L") ? "LAKI-LAKI" : "PEREMPUAN");
    PlaceOfBirth.setText(tabMode.getValueAt(selectedRow, 14).toString());
    
    // Konversi string tanggal lahir ke Date
    try {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateOfBirth = sdf.parse(tabMode.getValueAt(selectedRow, 15).toString());
        DateOfBirth.setDate(dateOfBirth);
    } catch (ParseException e) {
        e.printStackTrace();
    }
    
    OrgUnit.setText(tabMode.getValueAt(selectedRow, 16).toString());
    WorkUnit.setText(tabMode.getValueAt(selectedRow, 17).toString());
    Position.setText(tabMode.getValueAt(selectedRow, 18).toString());

    // Setelah mengisi form, Anda bisa mengubah tombol simpan menjadi tombol update
    BtnSimpan.setEnabled(false); // Nonaktifkan tombol simpan
    
    BtnEdit.setEnabled(true); // Aktifkan tombol update (Anda perlu menambahkan tombol ini di GUI)
    
        /* if(Name.getText().trim().equals("")){
            Valid.textKosong(Name,"NIP");
        }else if(TNm.getText().trim().equals("")){
            Valid.textKosong(TNm,"nama petugas");
        }else if(Position.getText().trim().equals("")||KdJbtn.getText().trim().equals("")){
            Valid.textKosong(KdJbtn,"jabatan");
        }else{
            try {
                koneksi.setAutoCommit(false);
                Sequel.mengedit("pegawai","nik='"+tbPetugas.getValueAt(tbPetugas.getSelectedRow(),0).toString()+"'",
                    "nik='"+Name.getText()+"',nama='"+TNm.getText()+"',jk='"+Gender.getSelectedItem().toString().replaceAll("PEREMPUAN","Wanita").replaceAll("LAKI-LAKI","Pria")+"',"+
                    "tmp_lahir='"+PlaceOfBirth.getText()+"',tgl_lahir='"+Valid.SetTgl(DateOfBirth.getSelectedItem()+"")+"',alamat='"+TAlmt.getText()+"'");
                Sequel.mengedit("petugas","nip='"+tbPetugas.getValueAt(tbPetugas.getSelectedRow(),0).toString()+"'",
                        "nip='"+Name.getText()+"',nama='"+TNm.getText()+
                        "',jk='"+Gender.getSelectedItem().toString().replaceAll("LAKI-LAKI","L").replaceAll("PEREMPUAN","P").trim()+
                        "',tmp_lahir='"+PlaceOfBirth.getText()+
                        "',tgl_lahir='"+Valid.SetTgl(DateOfBirth.getSelectedItem()+"")+
                        "',gol_darah='"+CMbGd.getSelectedItem()+
                        "',agama='"+Type.getSelectedItem()+
                        "',stts_nikah='"+CmbStts.getSelectedItem()+
                        "',alamat='"+TAlmt.getText()+
                        "',kd_jbtn='"+KdJbtn.getText()+
                        "',no_telp='"+TTlp.getText()+"'");
                koneksi.setAutoCommit(true);
                if(tabMode.getRowCount()!=0){tampil();}
                emptTeks();
            } catch (SQLException ex) {
                return;
            }            
        }*/
}//GEN-LAST:event_BtnEditActionPerformed

    private void BtnEditKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnEditKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnEditActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnHapus, BtnPrint);
        }
}//GEN-LAST:event_BtnEditKeyPressed

    private void TCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKeyPressed
         if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            BtnCariActionPerformed(null);
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

    private void tbPetugasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbPetugasMouseClicked
        if(tabMode.getRowCount()!=0){
            try {
                getData();
            } catch (java.lang.NullPointerException e) {
            }
        }
}//GEN-LAST:event_tbPetugasMouseClicked

private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
  isForm();                
}//GEN-LAST:event_ChkInputActionPerformed

    private void BtnCariPhotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariPhotoActionPerformed
       JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Pilih Foto Diri");

    // Mengatur filter untuk hanya menampilkan file gambar
    FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif");
    fileChooser.addChoosableFileFilter(filter);
    fileChooser.setFileFilter(filter);

    int userSelection = fileChooser.showOpenDialog(this);

    if (userSelection == JFileChooser.APPROVE_OPTION) {
        File fileToUpload = fileChooser.getSelectedFile();

        // Menampilkan path file yang dipilih di JTextField SelfPhoto
        SelfPhoto.setText(fileToUpload.getAbsolutePath());

        // Konversi file gambar ke Base64
        String base64Image = convertImageToBase64(fileToUpload.getAbsolutePath());
        if (base64Image != null) {
            // Simpan Base64 ke database atau ke variabel lain untuk digunakan saat menyimpan ke database
            // Misalnya, Anda bisa menyimpan base64Image ke JTextField atau variabel lain
            // Contoh: SelfPhotoBase64.setText(base64Image); // Jika Anda ingin menampilkannya di JTextField
        } else {
            JOptionPane.showMessageDialog(this, "Gagal mengonversi gambar ke Base64.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Jika Anda ingin menampilkan preview gambar, Anda bisa menambahkan kode di sini
        // ImageIcon icon = new ImageIcon(fileToUpload.getAbsolutePath());
        // lblPreviewSelfPhoto.setIcon(icon); // lblPreviewSelfPhoto adalah JLabel untuk menampilkan gambar foto diri
    }
    }//GEN-LAST:event_BtnCariPhotoActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        tampil();
    }//GEN-LAST:event_formWindowOpened

    private void MnSendLinkRegistrationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnSendLinkRegistrationActionPerformed
  int selectedRow = tbPetugas.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Silakan pilih baris dari tabel untuk registrasi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try {
        // Ambil email penerima dari baris yang dipilih
        String recipientEmail = tabMode.getValueAt(selectedRow, 2).toString();
        String name = tabMode.getValueAt(selectedRow, 0).toString();

        // Konfigurasi email
        final String senderEmail = "dianfirdaus73@gmail.com";
        final String senderPassword = "yudz sfya vboo fqhy"; // Ganti dengan password aplikasi Gmail Anda

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Gunakan javax.mail.PasswordAuthentication
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        // Buat pesan email
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
        message.setSubject("Link Registrasi Akun Peruri");

        // Isi email dalam format HTML
        String emailContent = String.format(
            "<html><body>" +
            "<p>Yth. %s,</p>" +
            "<p>Silahkan klik tautan berikut untuk melanjutkan registrasi tanda tangan elektronik:</p>" +
            "<p><a href='http://192.168.1.174/registration'>http://192.168.1.174/registration</a></p>" +
            "<p>Terima kasih.</p>" +
            "</body></html>",
            name
        );

        message.setContent(emailContent, "text/html; charset=utf-8");

        // Kirim email
        Transport.send(message);

        JOptionPane.showMessageDialog(this, "Link registrasi berhasil dikirim ke email: " + recipientEmail, 
            "Sukses", JOptionPane.INFORMATION_MESSAGE);

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat mengirim email: " + e.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_MnSendLinkRegistrationActionPerformed

    private void tbPetugasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbPetugasKeyReleased
        if(tabMode.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    getData();
                } catch (java.lang.NullPointerException e) {
                }
            }            
        }
    }//GEN-LAST:event_tbPetugasKeyReleased

    private void MnSendSpecimentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnSendSpecimentActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Pilih File Spesimen");
        int userSelection = fileChooser.showOpenDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToUpload = fileChooser.getSelectedFile();
            String email = Email.getText(); // Ambil email dari field Email

            try {
                // Mengonversi file spesimen ke base64
                byte[] fileContent = Files.readAllBytes(fileToUpload.toPath());
                String base64Specimen = Base64.getEncoder().encodeToString(fileContent);

                // Mengirim spesimen ke API Peruri
                ApiPeruri apiPeruri = new ApiPeruri();
                String response = apiPeruri.sendSpeciment(email, base64Specimen);
                System.out.println("Response dari API Send Specimen: " + response);
            } catch (IOException e) {
                System.err.println("Error membaca file: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Error saat mengirim spesimen ke API Peruri: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_MnSendSpecimentActionPerformed

    private void CityKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CityKeyPressed
        Valid.pindah(evt, TCari, Province);
    }//GEN-LAST:event_CityKeyPressed

    private void PhoneKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PhoneKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PhoneKeyPressed

    private void EmailKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_EmailKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_EmailKeyPressed

    private void PasswordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PasswordKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PasswordKeyPressed

    private void KtpKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KtpKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_KtpKeyPressed

    private void KtpPhotoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KtpPhotoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_KtpPhotoKeyPressed

    private void NpwpKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NpwpKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_NpwpKeyPressed

    private void NpwpPhotoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NpwpPhotoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_NpwpPhotoKeyPressed

    private void SelfPhotoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_SelfPhotoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_SelfPhotoKeyPressed

    private void AddressKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_AddressKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_AddressKeyPressed

    private void ProvinceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ProvinceKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_ProvinceKeyPressed

    private void BtnCariPegawaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariPegawaiActionPerformed
  pegawai.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        pegawai.setLocationRelativeTo(internalFrame1);
        pegawai.setAlwaysOnTop(false);
        pegawai.setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_BtnCariPegawaiActionPerformed

    private void BtnCariPhotoKtpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariPhotoKtpActionPerformed
         JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Pilih Foto KTP");

    // Mengatur filter untuk hanya menampilkan file gambar
    FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif");
    fileChooser.addChoosableFileFilter(filter);
    fileChooser.setFileFilter(filter);

    int userSelection = fileChooser.showOpenDialog(this);

    if (userSelection == JFileChooser.APPROVE_OPTION) {
        File fileToUpload = fileChooser.getSelectedFile();

        // Menampilkan path file yang dipilih di JTextField KtpPhoto
        KtpPhoto.setText(fileToUpload.getAbsolutePath());

        // Konversi file gambar ke Base64
        String base64Image = convertImageToBase64(fileToUpload.getAbsolutePath());
        if (base64Image != null) {
            // Simpan Base64 ke database
            // Misalnya, simpan ke JTextField atau variabel lain untuk digunakan saat menyimpan ke database
            // Anda bisa menyimpan base64Image ke database di sini
            // Contoh: NpwpPhoto.setText(base64Image); // Jika Anda ingin menampilkannya di JTextField
        } else {
            JOptionPane.showMessageDialog(this, "Gagal mengonversi gambar ke Base64.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    }//GEN-LAST:event_BtnCariPhotoKtpActionPerformed

    private void OrgUnitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_OrgUnitKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_OrgUnitKeyPressed

    private void WorkUnitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_WorkUnitKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_WorkUnitKeyPressed

    private void BtnCariPhotoNpwpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariPhotoNpwpActionPerformed
     JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Pilih Foto NPWP");
    
    // Mengatur filter untuk hanya menampilkan file gambar
    FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png", "gif");
    fileChooser.addChoosableFileFilter(filter);
    fileChooser.setFileFilter(filter);
    
    int userSelection = fileChooser.showOpenDialog(this);
    
    if (userSelection == JFileChooser.APPROVE_OPTION) {
        File fileToUpload = fileChooser.getSelectedFile();
        
        // Menampilkan path file yang dipilih di JTextField NpwpPhoto
        NpwpPhoto.setText(fileToUpload.getAbsolutePath());
        
        // Konversi file gambar ke Base64
        String base64Image = convertImageToBase64(fileToUpload.getAbsolutePath());
        if (base64Image != null) {
            // Simpan Base64 ke database atau ke variabel lain untuk digunakan saat menyimpan ke database
            // Misalnya, Anda bisa menyimpan base64Image ke JTextField atau variabel lain
            // Contoh: SelfPhoto.setText(base64Image); // Jika Anda ingin menampilkannya di JTextField
        } else {
            JOptionPane.showMessageDialog(this, "Gagal mengonversi gambar ke Base64.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        // Jika Anda ingin menampilkan preview gambar, Anda bisa menambahkan kode di sini
        // ImageIcon icon = new ImageIcon(fileToUpload.getAbsolutePath());
        // lblPreviewNpwp.setIcon(icon); // lblPreviewNpwp adalah JLabel untuk menampilkan gambar NPWP
    }
    }//GEN-LAST:event_BtnCariPhotoNpwpActionPerformed

    private void MnCheckCertificateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnCheckCertificateActionPerformed
        // Ambil baris yang dipilih dari tabel
    int selectedRow = tbPetugas.getSelectedRow(); // tbPetugas adalah JTable yang menampilkan data

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Silakan pilih baris dari tabel untuk memeriksa sertifikat.", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Ambil email dari kolom yang sesuai (misalnya kolom ke-2)
    String email = tabMode.getValueAt(selectedRow, 2).toString(); // Kolom ke-2 adalah email

    ApiPeruri apiPeruri = new ApiPeruri();
    try {
        // Mengambil JWT token
        String jwtToken = apiPeruri.generateJwtToken();
        if (jwtToken != null) {
            apiPeruri.setJwtToken(jwtToken);
        } else {
            JOptionPane.showMessageDialog(this, "Gagal memperoleh JWT token.", "Kesalahan", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Memanggil metode checkCertificate dengan email dan systemId
        String response = apiPeruri.checkCertificate(email); // Pastikan email dan systemId dikirim sebagai parameter
        System.out.println("Response dari Check Certificate API: " + response);

        // Mengolah respons JSON
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> responseMap = mapper.readValue(response, new TypeReference<Map<String, Object>>() {});

        // Memeriksa hasil respons
        if ("0".equals(responseMap.get("resultCode"))) {
            Map<String, Object> data = (Map<String, Object>) responseMap.get("data");
            String phone = (String) data.get("phone");
            String isExpired = (String) data.get("isExpired");
            String nik = (String) data.get("nik");

            // Tampilkan informasi kepada pengguna
            JOptionPane.showMessageDialog(this, "Email: " + email + "\nPhone: " + phone + "\nIs Expired: " + isExpired+ "\nNIK: " + nik);
        } else {
            String resultDesc = (String) responseMap.get("resultDesc");
            JOptionPane.showMessageDialog(this, "Gagal memeriksa sertifikat: " + resultDesc);
        }
    } catch (Exception e) {
        System.err.println("Error: " + e.getMessage());
        JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat memeriksa sertifikat: " + e.getMessage());
    }
        // TODO add your handling code here:
    }//GEN-LAST:event_MnCheckCertificateActionPerformed

    private void EmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EmailActionPerformed

    private void PositionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_PositionKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_PositionKeyPressed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            DlgRegistrasiAkunPeruri dialog = new DlgRegistrasiAkunPeruri(new javax.swing.JFrame(), true);
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
    private widget.TextBox Address;
    private widget.Button BtnAll;
    private widget.Button BtnBatal;
    private widget.Button BtnCari;
    private widget.Button BtnCariPegawai;
    private widget.Button BtnCariPhoto;
    private widget.Button BtnCariPhotoKtp;
    private widget.Button BtnCariPhotoNpwp;
    private widget.Button BtnEdit;
    private widget.Button BtnHapus;
    private widget.Button BtnKeluar;
    private widget.Button BtnPrint;
    private widget.Button BtnSimpan;
    private widget.CekBox ChkInput;
    private widget.TextBox City;
    private widget.Tanggal DateOfBirth;
    private widget.TextBox Email;
    private widget.PanelBiasa FormInput;
    private widget.ComboBox Gender;
    private widget.TextBox Ktp;
    private widget.TextBox KtpPhoto;
    private widget.Label LCount;
    private javax.swing.JMenuItem MnCheckCertificate;
    private javax.swing.JMenuItem MnSendLinkRegistration;
    private javax.swing.JMenuItem MnSendSpeciment;
    private widget.TextBox Name;
    private widget.TextBox Npwp;
    private widget.TextBox NpwpPhoto;
    private widget.TextBox OrgUnit;
    private javax.swing.JPanel PanelInput;
    private widget.TextBox Password;
    private widget.TextBox Phone;
    private widget.TextBox PlaceOfBirth;
    private javax.swing.JPopupMenu Popup;
    private widget.TextBox Position;
    private widget.TextBox Province;
    private widget.ScrollPane Scroll;
    private widget.TextBox SelfPhoto;
    private widget.TextBox TCari;
    private widget.ComboBox Type;
    private widget.TextBox WorkUnit;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel10;
    private widget.Label jLabel12;
    private widget.Label jLabel13;
    private widget.Label jLabel15;
    private widget.Label jLabel17;
    private widget.Label jLabel22;
    private widget.Label jLabel23;
    private widget.Label jLabel24;
    private widget.Label jLabel25;
    private widget.Label jLabel26;
    private widget.Label jLabel27;
    private widget.Label jLabel28;
    private widget.Label jLabel29;
    private widget.Label jLabel30;
    private widget.Label jLabel31;
    private widget.Label jLabel32;
    private widget.Label jLabel4;
    private widget.Label jLabel5;
    private widget.Label jLabel6;
    private widget.Label jLabel8;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator5;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.Table tbPetugas;
    // End of variables declaration//GEN-END:variables

    private void tampil() {
         // Menghapus semua baris yang ada di tabel
    tabMode.setRowCount(0);
    
    try {
        // Membuat query untuk mengambil data dari tabel akun_peruri
        String sql = "SELECT * FROM akun_peruri";
        PreparedStatement ps = koneksi.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        
        // Mengisi tabel dengan data dari ResultSet
        while (rs.next()) {
            String[] data = {
                rs.getString("name"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("type"),
                rs.getString("ktp"),
                rs.getString("ktp_photo"),
                rs.getString("npwp"),
                rs.getString("npwp_photo"),
                rs.getString("self_photo"),
                rs.getString("address"),
                rs.getString("city"),
                rs.getString("province"),
                rs.getString("gender"),
                rs.getString("place_of_birth"),
                rs.getString("date_of_birth"),
                rs.getString("org_unit"),
                rs.getString("work_unit"),
                rs.getString("position")
            };
            tabMode.addRow(data); // Menambahkan data ke tabel
        }
        
        // Menutup ResultSet dan PreparedStatement
        rs.close();
        ps.close();
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }

    }

    public void emptTeks() {  
    Name.setText("");  
    Phone.setText("");  
    Email.setText("");  
    Password.setText("");  
    Type.setSelectedIndex(0);  
    Ktp.setText("");  
    KtpPhoto.setText("");  
    Npwp.setText("");  
    NpwpPhoto.setText("");  
    SelfPhoto.setText("");  
    Address.setText("");  
    City.setText("");  
    Province.setText("");  
    Gender.setSelectedIndex(0);  
    PlaceOfBirth.setText("");  
    DateOfBirth.setDate(new Date());  
    OrgUnit.setText("");  
    WorkUnit.setText("");  
    Position.setText("");  
    Name.requestFocus();  
}

    private void getData() {
      
    int row=tbPetugas.getSelectedRow(); 
    if (row != -1) {  
        Name.setText(tabMode.getValueAt(row, 0).toString());  
        Phone.setText(tabMode.getValueAt(row, 1).toString());  
        Email.setText(tabMode.getValueAt(row, 2).toString());  
        Password.setText(tabMode.getValueAt(row, 3).toString());  
        Type.setSelectedItem(tabMode.getValueAt(row, 4).toString());  
        Ktp.setText(tabMode.getValueAt(row, 5).toString());  
        KtpPhoto.setText(tabMode.getValueAt(row, 6).toString());  
        Npwp.setText(tabMode.getValueAt(row, 7).toString());  
        NpwpPhoto.setText(tabMode.getValueAt(row, 8).toString());  
        SelfPhoto.setText(tabMode.getValueAt(row, 9).toString());  
        Address.setText(tabMode.getValueAt(row, 10).toString());  
        City.setText(tabMode.getValueAt(row, 11).toString());  
        Province.setText(tabMode.getValueAt(row, 12).toString());  
        Gender.setSelectedItem(tabMode.getValueAt(row, 13).toString());  
        PlaceOfBirth.setText(tabMode.getValueAt(row, 14).toString());  
        Valid.SetTgl(DateOfBirth,tbPetugas.getValueAt(row,15).toString()); 
        OrgUnit.setText(tabMode.getValueAt(row, 16).toString());  
        WorkUnit.setText(tabMode.getValueAt(row, 17).toString());  
        Position.setText(tabMode.getValueAt(row, 18).toString());  
    }  
    }

    
    public JTextField getTextField(){
        return Name;
    }

    public JTable getTable(){
        return tbPetugas;
    }
    
    private void isForm(){
        if(ChkInput.isSelected()==true){
            ChkInput.setVisible(false);
            PanelInput.setPreferredSize(new Dimension(WIDTH,350));
            FormInput.setVisible(true);      
            ChkInput.setVisible(true);
        }else if(ChkInput.isSelected()==false){           
            ChkInput.setVisible(false);            
            PanelInput.setPreferredSize(new Dimension(WIDTH,20));
            FormInput.setVisible(false);      
            ChkInput.setVisible(true);
        }
    }
    
    public void isCek(){
        BtnSimpan.setEnabled(akses.getpetugas());
        BtnHapus.setEnabled(akses.getpetugas());
        BtnEdit.setEnabled(akses.getpetugas());
        BtnPrint.setEnabled(akses.getpetugas());
        if(akses.getkode().equals("Admin Utama")){
            MnSendLinkRegistration.setEnabled(true);
        }else{
            MnSendLinkRegistration.setEnabled(false);
        } 
    }
    
     private String convertImageToBase64(String filePath) {
    try {
        // Debugging: Cek apakah file ada
        File file = new File(filePath);
        if (!file.exists()) {
            System.err.println("File tidak ditemukan: " + filePath);
            return null; // Mengembalikan null jika file tidak ada
        }

        // Membaca file gambar ke dalam byte array
        byte[] fileContent = Files.readAllBytes(file.toPath());
        // Mengonversi byte array ke string Base64
        return Base64.getEncoder().encodeToString(fileContent);
    } catch (IOException e) {
        System.err.println("Error converting image to Base64: " + e.getMessage());
        return null; // Mengembalikan null jika terjadi kesalahan
    }
}
}
