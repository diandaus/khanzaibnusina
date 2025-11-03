/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DlgLhtBiaya.java
 *
 * Created on 12 Jul 10, 16:21:34
 */

package rekammedis;

import fungsi.WarnaTable;
import fungsi.akses;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.HyperlinkEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import simrskhanza.DlgCariPasien;

/**
 *
 * @author windiarto
 */
public final class RMVerifikasiCPPT extends javax.swing.JDialog {    
    private validasi Valid=new validasi();    
    private final sekuel Sequel=new sekuel();
   // private final DefaultTableModel tabModeRegistrasi;
    private PreparedStatement ps,ps2;
    private ResultSet rs,rs2,rs3,rs4;
    private Connection koneksi=koneksiDB.condb();
    private int i=0,urut=0,w=0,s=0,urutdpjp=0;
    private double biayaperawatan=0;
    private String kddpjp="",dpjp="",dokterrujukan="",polirujukan="",keputusan="",ke1="",ke2="",ke3="",ke4="",ke5="",ke6="",file="";
    private StringBuilder htmlContent;
    private HttpClient http = new HttpClient();
    private GetMethod get;
    private DlgCariPasien pasien=new DlgCariPasien(null,true);

    /** Creates new form DlgLhtBiaya
     * @param parent
     * @param modal */
    public RMVerifikasiCPPT(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(8,1);
        setSize(885,674);
        
       /* tabModeRegistrasi=new DefaultTableModel(null,new Object[]{
                "No.","No.Rawat","Tanggal","Jam","Kd.Dokter","Dokter Dituju/DPJP","Umur","Poliklinik/Kamar","Jenis Bayar"
            }){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };

         */
        NoRM.setDocument(new batasInput((byte)20).getKata(NoRM));
       
        pasien.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(pasien.getTable().getSelectedRow()!= -1){                   
                    NoRM.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),0).toString());
                    NmPasien.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),1).toString());
                    Jk.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),3).toString());
                    TempatLahir.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),4).toString());
                    TanggalLahir.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),5).toString());
                    IbuKandung.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),6).toString());
                    Alamat.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),7).toString());
                    GD.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),8).toString());
                    StatusNikah.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),10).toString());
                    Agama.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),11).toString());
                    Pendidikan.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),15).toString());
                    Bahasa.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),26).toString());
                    CacatFisik.setText(pasien.getTable().getValueAt(pasien.getTable().getSelectedRow(),32).toString());
                }    
                NoRM.requestFocus();
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
        
        pasien.getTable().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_SPACE){
                    pasien.dispose();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        });
        
        HTMLEditorKit kit = new HTMLEditorKit();
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule(
            "body { font-family: Tahoma, Arial, sans-serif; font-size: 9px; } " +
            "table.tbl_form { border-collapse: collapse; width: 100%; } " +
            ".isi td { border-right: 1px solid #e2e7dd; font: 9px tahoma; padding: 3px; border-bottom: 1px solid #e2e7dd; background: #ffffff; color: #323232; vertical-align: top; } " +
            ".isi a { text-decoration: none; color: white; padding: 6px 12px; font-family: Tahoma; font-size: 9px; background-color: #4CAF50; border-radius: 3px; display: inline-block; font-weight: bold; }"
        );
        Document doc = kit.createDefaultDocument();
        LoadHTMLSOAPI.setEditorKit(kit);
        LoadHTMLSOAPI.setDocument(doc);
        LoadHTMLSOAPI.setEditable(false);
        LoadHTMLSOAPI.setContentType("text/html");
        LoadHTMLSOAPI.addHyperlinkListener(e -> {
            if (HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) {
              try {
                // Gunakan getDescription() karena getURL() bisa null untuk custom scheme
                String url = e.getDescription();
                if (url != null && url.startsWith("verifikasi://")) {
                  // Handle verifikasi CPPT
                  String data = url.substring(13); // Remove "verifikasi://"
                  String[] params = data.split("\\|");
                  if (params.length == 4) {
                    prosesVerifikasi(params[0], params[1], params[2], params[3]);
                  }
                } else if (e.getURL() != null) {
                  Desktop desktop = Desktop.getDesktop();
                  desktop.browse(e.getURL().toURI());
                }
              } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("Error hyperlink: " + ex.getMessage());
              }
            }
        });
    }    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        Pekerjaan = new widget.TextBox();
        internalFrame1 = new widget.InternalFrame();
        panelGlass5 = new widget.panelisi();
        R1 = new widget.RadioButton();
        R2 = new widget.RadioButton();
        R3 = new widget.RadioButton();
        Tgl1 = new widget.Tanggal();
        label18 = new widget.Label();
        Tgl2 = new widget.Tanggal();
        label19 = new widget.Label();
        BtnPrint = new widget.Button();
        BtnKeluar = new widget.Button();
        TabRawat = new javax.swing.JTabbedPane();
        Scroll2 = new widget.ScrollPane();
        LoadHTMLSOAPI = new widget.editorpane();
        PanelInput = new javax.swing.JPanel();
        ChkInput = new widget.CekBox();
        FormInput = new widget.panelisi();
        label17 = new widget.Label();
        NoRM = new widget.TextBox();
        NmPasien = new widget.TextBox();
        BtnPasien = new widget.Button();
        label20 = new widget.Label();
        Jk = new widget.TextBox();
        label21 = new widget.Label();
        TempatLahir = new widget.TextBox();
        label22 = new widget.Label();
        Alamat = new widget.TextBox();
        label23 = new widget.Label();
        GD = new widget.TextBox();
        label24 = new widget.Label();
        IbuKandung = new widget.TextBox();
        TanggalLahir = new widget.TextBox();
        label25 = new widget.Label();
        Agama = new widget.TextBox();
        StatusNikah = new widget.TextBox();
        label26 = new widget.Label();
        Pendidikan = new widget.TextBox();
        label27 = new widget.Label();
        label28 = new widget.Label();
        Bahasa = new widget.TextBox();
        label29 = new widget.Label();
        CacatFisik = new widget.TextBox();

        Pekerjaan.setEditable(false);
        Pekerjaan.setName("Pekerjaan"); // NOI18N
        Pekerjaan.setPreferredSize(new java.awt.Dimension(100, 23));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ CPPT (Catatan Perkembangan Pasien Terintegrasi ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass5.setName("panelGlass5"); // NOI18N
        panelGlass5.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        R1.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.pink));
        buttonGroup1.add(R1);
        R1.setSelected(true);
        R1.setText("5 CPPT Terakhir");
        R1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        R1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        R1.setName("R1"); // NOI18N
        R1.setPreferredSize(new java.awt.Dimension(120, 23));
        R1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                R1ActionPerformed(evt);
            }
        });
        panelGlass5.add(R1);

        R2.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.pink));
        buttonGroup1.add(R2);
        R2.setText("Semua CPPT");
        R2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        R2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        R2.setName("R2"); // NOI18N
        R2.setPreferredSize(new java.awt.Dimension(104, 23));
        panelGlass5.add(R2);

        R3.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.pink));
        buttonGroup1.add(R3);
        R3.setText("Tanggal :");
        R3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        R3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        R3.setName("R3"); // NOI18N
        R3.setPreferredSize(new java.awt.Dimension(75, 23));
        panelGlass5.add(R3);

        Tgl1.setDisplayFormat("dd-MM-yyyy");
        Tgl1.setName("Tgl1"); // NOI18N
        Tgl1.setPreferredSize(new java.awt.Dimension(90, 23));
        Tgl1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Tgl1KeyPressed(evt);
            }
        });
        panelGlass5.add(Tgl1);

        label18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label18.setText("s.d.");
        label18.setName("label18"); // NOI18N
        label18.setPreferredSize(new java.awt.Dimension(25, 23));
        panelGlass5.add(label18);

        Tgl2.setDisplayFormat("dd-MM-yyyy");
        Tgl2.setName("Tgl2"); // NOI18N
        Tgl2.setPreferredSize(new java.awt.Dimension(90, 23));
        Tgl2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                Tgl2KeyPressed(evt);
            }
        });
        panelGlass5.add(Tgl2);

        label19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        label19.setName("label19"); // NOI18N
        label19.setPreferredSize(new java.awt.Dimension(15, 23));
        panelGlass5.add(label19);

        BtnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/b_print.png"))); // NOI18N
        BtnPrint.setMnemonic('T');
        BtnPrint.setText("Cetak");
        BtnPrint.setToolTipText("Alt+T");
        BtnPrint.setIconTextGap(3);
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
        panelGlass5.add(BtnPrint);

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
        panelGlass5.add(BtnKeluar);

        internalFrame1.add(panelGlass5, java.awt.BorderLayout.PAGE_END);

        TabRawat.setBackground(new java.awt.Color(255, 255, 254));
        TabRawat.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(241, 246, 236)));
        TabRawat.setForeground(new java.awt.Color(50, 50, 50));
        TabRawat.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        TabRawat.setName("TabRawat"); // NOI18N
        TabRawat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabRawatMouseClicked(evt);
            }
        });

        Scroll2.setBorder(null);
        Scroll2.setName("Scroll2"); // NOI18N
        Scroll2.setOpaque(true);

        LoadHTMLSOAPI.setBorder(null);
        LoadHTMLSOAPI.setName("LoadHTMLSOAPI"); // NOI18N
        Scroll2.setViewportView(LoadHTMLSOAPI);

        TabRawat.addTab("Riwayat CPPT", Scroll2);

        internalFrame1.add(TabRawat, java.awt.BorderLayout.CENTER);

        PanelInput.setBackground(new java.awt.Color(255, 255, 255));
        PanelInput.setName("PanelInput"); // NOI18N
        PanelInput.setOpaque(false);
        PanelInput.setLayout(new java.awt.BorderLayout(1, 1));

        ChkInput.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput.setMnemonic('M');
        ChkInput.setSelected(true);
        ChkInput.setText(".: Tampilkan/Sembunyikan Data Pasien");
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

        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(100, 104));
        FormInput.setLayout(null);

        label17.setText("Pasien :");
        label17.setName("label17"); // NOI18N
        label17.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label17);
        label17.setBounds(5, 10, 55, 23);

        NoRM.setName("NoRM"); // NOI18N
        NoRM.setPreferredSize(new java.awt.Dimension(100, 23));
        NoRM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NoRMKeyPressed(evt);
            }
        });
        FormInput.add(NoRM);
        NoRM.setBounds(64, 10, 100, 23);

        NmPasien.setEditable(false);
        NmPasien.setName("NmPasien"); // NOI18N
        NmPasien.setPreferredSize(new java.awt.Dimension(220, 23));
        FormInput.add(NmPasien);
        NmPasien.setBounds(167, 10, 220, 23);

        BtnPasien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnPasien.setMnemonic('3');
        BtnPasien.setToolTipText("Alt+3");
        BtnPasien.setName("BtnPasien"); // NOI18N
        BtnPasien.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnPasienActionPerformed(evt);
            }
        });
        BtnPasien.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnPasienKeyPressed(evt);
            }
        });
        FormInput.add(BtnPasien);
        BtnPasien.setBounds(390, 10, 28, 23);

        label20.setText("J.K. :");
        label20.setName("label20"); // NOI18N
        label20.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label20);
        label20.setBounds(436, 10, 30, 23);

        Jk.setEditable(false);
        Jk.setName("Jk"); // NOI18N
        Jk.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(Jk);
        Jk.setBounds(470, 10, 40, 23);

        label21.setText("Tempat & Tgl.Lahir :");
        label21.setName("label21"); // NOI18N
        label21.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label21);
        label21.setBounds(523, 10, 110, 23);

        TempatLahir.setEditable(false);
        TempatLahir.setName("TempatLahir"); // NOI18N
        TempatLahir.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(TempatLahir);
        TempatLahir.setBounds(637, 10, 140, 23);

        label22.setText("Alamat :");
        label22.setName("label22"); // NOI18N
        label22.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label22);
        label22.setBounds(5, 40, 55, 23);

        Alamat.setEditable(false);
        Alamat.setName("Alamat"); // NOI18N
        Alamat.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(Alamat);
        Alamat.setBounds(64, 40, 354, 23);

        label23.setText("G.D. :");
        label23.setName("label23"); // NOI18N
        label23.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label23);
        label23.setBounds(436, 40, 30, 23);

        GD.setEditable(false);
        GD.setName("GD"); // NOI18N
        GD.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(GD);
        GD.setBounds(470, 40, 40, 23);

        label24.setText("Nama Ibu Kandung :");
        label24.setName("label24"); // NOI18N
        label24.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label24);
        label24.setBounds(523, 40, 110, 23);

        IbuKandung.setEditable(false);
        IbuKandung.setName("IbuKandung"); // NOI18N
        IbuKandung.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(IbuKandung);
        IbuKandung.setBounds(637, 40, 225, 23);

        TanggalLahir.setEditable(false);
        TanggalLahir.setName("TanggalLahir"); // NOI18N
        TanggalLahir.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(TanggalLahir);
        TanggalLahir.setBounds(779, 10, 83, 23);

        label25.setText("Agama :");
        label25.setName("label25"); // NOI18N
        label25.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label25);
        label25.setBounds(5, 70, 55, 23);

        Agama.setEditable(false);
        Agama.setName("Agama"); // NOI18N
        Agama.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(Agama);
        Agama.setBounds(64, 70, 100, 23);

        StatusNikah.setEditable(false);
        StatusNikah.setName("StatusNikah"); // NOI18N
        StatusNikah.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(StatusNikah);
        StatusNikah.setBounds(245, 70, 100, 23);

        label26.setText("Stts.Nikah :");
        label26.setName("label26"); // NOI18N
        label26.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label26);
        label26.setBounds(176, 70, 65, 23);

        Pendidikan.setEditable(false);
        Pendidikan.setName("Pendidikan"); // NOI18N
        Pendidikan.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(Pendidikan);
        Pendidikan.setBounds(429, 70, 80, 23);

        label27.setText("Pendidikan :");
        label27.setName("label27"); // NOI18N
        label27.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label27);
        label27.setBounds(355, 70, 70, 23);

        label28.setText("Bahasa :");
        label28.setName("label28"); // NOI18N
        label28.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label28);
        label28.setBounds(520, 70, 50, 23);

        Bahasa.setEditable(false);
        Bahasa.setName("Bahasa"); // NOI18N
        Bahasa.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(Bahasa);
        Bahasa.setBounds(574, 70, 100, 23);

        label29.setText("Cacat Fisik :");
        label29.setName("label29"); // NOI18N
        label29.setPreferredSize(new java.awt.Dimension(55, 23));
        FormInput.add(label29);
        label29.setBounds(683, 70, 70, 23);

        CacatFisik.setEditable(false);
        CacatFisik.setName("CacatFisik"); // NOI18N
        CacatFisik.setPreferredSize(new java.awt.Dimension(100, 23));
        FormInput.add(CacatFisik);
        CacatFisik.setBounds(757, 70, 105, 23);

        PanelInput.add(FormInput, java.awt.BorderLayout.CENTER);

        internalFrame1.add(PanelInput, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);
        internalFrame1.getAccessibleContext().setAccessibleName("::[ Data CPPT ]::");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            dispose();
        }else{Valid.pindah(evt,Tgl1,NoRM);}
}//GEN-LAST:event_BtnKeluarKeyPressed

private void NoRMKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NoRMKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isPasien();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            isPasien();
            BtnKeluar.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            BtnPasienActionPerformed(null);
        }else if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            isPasien();
            
        }
}//GEN-LAST:event_NoRMKeyPressed

private void BtnPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPasienActionPerformed
    if(akses.getpasien()==true){
        pasien.isCek();
        pasien.emptTeks();
        pasien.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        pasien.setLocationRelativeTo(internalFrame1);
        pasien.setVisible(true);
    }   
}//GEN-LAST:event_BtnPasienActionPerformed

private void BtnPasienKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPasienKeyPressed
    //Valid.pindah(evt,Tgl2,TKd);
}//GEN-LAST:event_BtnPasienKeyPressed

    private void Tgl1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Tgl1KeyPressed
        Valid.pindah(evt, BtnKeluar, Tgl2);
    }//GEN-LAST:event_Tgl1KeyPressed

    private void Tgl2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_Tgl2KeyPressed
        Valid.pindah(evt, Tgl1,NoRM);
    }//GEN-LAST:event_Tgl2KeyPressed

    private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
        isForm();
    }//GEN-LAST:event_ChkInputActionPerformed

    private void BtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnPrintActionPerformed
        if(NoRM.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null,"Silahkan pilih pasien terlebih dahulu!");
            return;
        }

        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        String pas=" and reg_periksa.no_rkm_medis = '"+NoRM.getText().trim()+"' ";
        String tgl=" reg_periksa.tgl_registrasi between '"+Valid.SetTgl(Tgl1.getSelectedItem()+"")+"' and '"+Valid.SetTgl(Tgl2.getSelectedItem()+"")+"' "+pas;

        // Deklarasi variabel di luar block try
        String currentNoRawat = "-";
        String currentNoRM = "-";
        String currentNamaPasien = "-";

        // Initialize HTTP Client untuk QR Code
        HttpClient http = new HttpClient();
        GetMethod get;

        // Konversi logo ke Base64 untuk embed di HTML
        String logoBase64 = "";
        try {
            java.io.ByteArrayInputStream logoStream = Sequel.cariGambar("select setting.logo from setting");
            if (logoStream != null) {
                java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream();
                int nRead;
                byte[] data = new byte[1024];
                while ((nRead = logoStream.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();
                byte[] logoBytes = buffer.toByteArray();

                if (logoBytes.length > 0) {
                    logoBase64 = "data:image/png;base64," + java.util.Base64.getEncoder().encodeToString(logoBytes);
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading logo: " + e);
        }

        String htmlContent = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset='UTF-8'>\n" +
                "    <title>CCPT (Catatan Perkembagan Pasien Terintergrasi</title>\n" +
                "    <style>\n" +
                "        @page {\n" +
                "            size: A4 portrait;\n" +
                "            margin: 10mm;\n" +
                "        }\n" +
                "        * {\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "            box-sizing: border-box;\n" +
                "        }\n" +
                "        body {\n" +
                "            font-family: 'Tahoma', 'Arial', sans-serif;\n" +
                "            font-size: 9pt;\n" +
                "            margin: 15px;\n" +
                "            background: white;\n" +
                "        }\n" +
                "        .header {\n" +
                "            position: relative;\n" +
                "            text-align: center;\n" +
                "            margin-bottom: 15px;\n" +
                "            border-bottom: 3px double #000;\n" +
                "            padding-bottom: 10px;\n" +
                "            padding-top: 5px;\n" +
                "        }\n" +
                "        .header .logo {\n" +
                "            position: absolute;\n" +
                "            left: 0;\n" +
                "            top: 50%;\n" +
                "            transform: translateY(-50%);\n" +
                "            max-width: 80px;\n" +
                "            max-height: 80px;\n" +
                "        }\n" +
                "        .header .header-content {\n" +
                "            display: inline-block;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .header h2 {\n" +
                "            font-size: 16pt;\n" +
                "            font-weight: bold;\n" +
                "            margin-bottom: 5px;\n" +
                "            color: #000;\n" +
                "        }\n" +
                "        .header p {\n" +
                "            font-size: 9pt;\n" +
                "            margin: 2px 0;\n" +
                "            color: #333;\n" +
                "        }\n" +
                "        .header h3 {\n" +
                "            font-size: 12pt;\n" +
                "            font-weight: bold;\n" +
                "            margin-top: 10px;\n" +
                "            margin-bottom: 3px;\n" +
                "            color: #000;\n" +
                "        }\n" +
                "        .info-periode {\n" +
                "            font-size: 9pt;\n" +
                "            margin: 5px 0 10px 0;\n" +
                "            font-weight: normal;\n" +
                "        }\n" +
                "        .patient-info {\n" +
                "            margin: 10px 0;\n" +
                "            padding: 10px;\n" +
                "            border: 1px solid #000;\n" +
                "            background-color: #f9f9f9;\n" +
                "        }\n" +
                "        .info-table {\n" +
                "            width: 100%;\n" +
                "            border-collapse: collapse;\n" +
                "            font-size: 9pt;\n" +
                "        }\n" +
                "        .info-table td {\n" +
                "            padding: 3px 5px;\n" +
                "            border: none;\n" +
                "        }\n" +
                "        .document-title {\n" +
                "            text-align: center;\n" +
                "            font-size: 12pt;\n" +
                "            font-weight: bold;\n" +
                "            margin: 15px 0 10px 0;\n" +
                "            padding: 8px;\n" +
                "            background: linear-gradient(to bottom, #f0f0f0 0%, #d0d0d0 100%);\n" +
                "            border: 1px solid #000;\n" +
                "        }\n" +
                "        table {\n" +
                "            width: 100%;\n" +
                "            border-collapse: collapse;\n" +
                "            margin-top: 5px;\n" +
                "            font-size: 8pt;\n" +
                "        }\n" +
                "        th {\n" +
                "            background: linear-gradient(to bottom, #f0f0f0 0%, #d0d0d0 100%);\n" +
                "            color: #000;\n" +
                "            font-weight: bold;\n" +
                "            padding: 6px 4px;\n" +
                "            text-align: center;\n" +
                "            border: 1px solid #000;\n" +
                "            font-size: 8pt;\n" +
                "            vertical-align: middle;\n" +
                "        }\n" +
                "        td {\n" +
                "            padding: 6px 8px;\n" +
                "            border: 1px solid #666;\n" +
                "            vertical-align: top;\n" +
                "            font-size: 9pt;\n" +
                "            line-height: 1.6;\n" +
                "        }\n" +
                "        tr:nth-child(even) {\n" +
                "            background-color: #f9f9f9;\n" +
                "        }\n" +
                "        tr:hover {\n" +
                "            background-color: #ffffcc;\n" +
                "        }\n" +
                "        .center {\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .right {\n" +
                "            text-align: right;\n" +
                "        }\n" +
                "        .nowrap {\n" +
                "            white-space: nowrap;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            margin-top: 15px;\n" +
                "            font-size: 8pt;\n" +
                "            text-align: right;\n" +
                "            color: #666;\n" +
                "        }\n" +
                "        @media print {\n" +
                "            body {\n" +
                "                margin: 0;\n" +
                "                padding: 10px;\n" +
                "            }\n" +
                "            .no-print {\n" +
                "                display: none;\n" +
                "            }\n" +
                "            tr {\n" +
                "                page-break-inside: avoid;\n" +
                "            }\n" +
                "        }\n" +
                "        .btn-print {\n" +
                "            background-color: #4CAF50;\n" +
                "            color: white;\n" +
                "            padding: 10px 20px;\n" +
                "            border: none;\n" +
                "            cursor: pointer;\n" +
                "            font-size: 10pt;\n" +
                "            margin: 10px 5px;\n" +
                "            border-radius: 4px;\n" +
                "        }\n" +
                "        .btn-print:hover {\n" +
                "            background-color: #45a049;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class='no-print' style='text-align: center; margin-bottom: 10px;'>\n" +
                "        <button class='btn-print' onclick='window.print()'>🖨️ Print</button>\n" +
                "        <button class='btn-print' style='background-color: #2196F3;' onclick='window.close()'>✖ Tutup</button>\n" +
                "    </div>\n" +
                "    <table width='100%' border='1' cellpadding='5' cellspacing='0' style='border-collapse:collapse; margin-bottom:15px;'>\n" +
                "        <tr>\n" +
                "            <td width='50%' style='vertical-align:middle; text-align:center; border:1px solid #000; padding:8px;'>\n" +
                "                <table width='100%' border='0' cellpadding='0' cellspacing='0' style='border:none;'>\n" +
                "                    <tr>\n" +
                (logoBase64.isEmpty() ? "" : "                        <td width='70' style='vertical-align:middle; text-align:center; border:none;'><img src='"+logoBase64+"' width='60' height='60' alt='Logo RS'></td>\n") +
                "                        <td style='vertical-align:middle; text-align:center; border:none;'>\n" +
                "                            <b style='font-size:14pt;'>"+akses.getnamars().toUpperCase()+"</b><br>\n" +
                "                            <span style='font-size:9pt;'>"+akses.getalamatrs()+","+akses.getkabupatenrs()+", "+akses.getpropinsirs()+"<br>\n" +
                "                            Telp: "+akses.getkontakrs()+" | Email: "+akses.getemailrs()+"</span>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "            </td>\n" +
                "            <td width='50%' style='vertical-align:middle; border:1px solid #000; padding:8px; font-size:9pt;'>\n" +
                "                <table width='100%' border='0' cellpadding='2' cellspacing='0' style='border:none;'>\n" +
                "                    <tr>\n" +
                "                        <td style='border:none;'><b>No. Rekam Medis</b></td>\n" +
                "                        <td style='border:none;'>:</td>\n" +
                "                        <td style='border:none;'>"+NoRM.getText()+"</td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td style='border:none;'><b>Nama Pasien</b></td>\n" +
                "                        <td style='border:none;'>:</td>\n" +
                "                        <td style='border:none;'>"+NmPasien.getText()+"<span style='margin-left: 20px;'>/"+Jk.getText()+"</span></td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td style='border:none;'><b>Tanggal Lahir</b></td>\n" +
                "                        <td style='border:none;'>:</td>\n" +
                "                        <td style='border:none;'>"+TanggalLahir.getText()+"</td>\n" +
                "                    </tr>\n" +
                "                </table>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "    <h3 class='document-title'>CATATAN PERKEMBANGAN PASIEN TERINTEGRASI</h3>\n" +
                "    <table>\n" +
                "        <thead>\n" +
                "            <tr>\n" +
                "                <th style='width: 10%;'>Tgl/Jam</th>\n" +
                "                <th style='width: 15%;'>Profesional Pemberi Asuhan</th>\n" +
                "                <th style='width: 38%;'>Hasil Asesmen Penatalaksanaan Pasien<br>(Tulis dengan format SOAP, disertai sasaran, Tulis nama, beri paraf/tanda akhir catatan)</th>\n" +
                "                <th style='width: 17%;'>Instruksi PPA<br>(Intruksi ditulis dengan rinci dan jelas)<br>termasuk pasca bedah</th>\n" +
                "                <th style='width: 20%;'>Verifikasi DPJP<br>(Tulis Nama, Beri paraf, Tgl, Jam)<br>(DPJP harus membaca/menreview seluruh rencana asuhan)</th>\n" +
                "            </tr>\n" +
                "        </thead>\n" +
                "        <tbody>\n";

        try {
            // Query untuk menampilkan SEMUA data pemeriksaan yang SUDAH TERVERIFIKASI untuk pasien ini
            String querySQL =
                "select pr.no_rawat, rp.no_rkm_medis, p.nm_pasien, pr.tgl_perawatan, pr.jam_rawat, " +
                "pr.suhu_tubuh, pr.tensi, pr.nadi, pr.respirasi, pr.tinggi, pr.berat, pr.spo2, pr.gcs, pr.kesadaran, " +
                "pr.keluhan, pr.pemeriksaan, pr.alergi, pr.penilaian, pr.rtl, pr.instruksi, pr.evaluasi, pr.nip, pg.nama, pg.jbtn, " +
                "'Ralan' as jns_rawat, v.nm_dokter as nm_dokter_verif, DATE_FORMAT(v.tgl_verifikasi,'%d-%m-%Y %H:%i:%s') as tgl_verif " +
                "from pemeriksaan_ralan pr " +
                "inner join reg_periksa rp on pr.no_rawat=rp.no_rawat " +
                "inner join pasien p on rp.no_rkm_medis=p.no_rkm_medis " +
                "inner join pegawai pg on pr.nip=pg.nik " +
                "inner join verifikasi_cppt_dpjp v on pr.no_rawat=v.no_rawat and pr.tgl_perawatan=v.tgl_perawatan and pr.jam_rawat=v.jam_rawat and v.jenis_rawat='Ralan' " +
                "where rp.no_rkm_medis='"+NoRM.getText().trim()+"' " +
                "union all " +
                "select pr.no_rawat, rp.no_rkm_medis, p.nm_pasien, pr.tgl_perawatan, pr.jam_rawat, " +
                "pr.suhu_tubuh, pr.tensi, pr.nadi, pr.respirasi, pr.tinggi, pr.berat, pr.spo2, pr.gcs, pr.kesadaran, " +
                "pr.keluhan, pr.pemeriksaan, pr.alergi, pr.penilaian, pr.rtl, pr.instruksi, pr.evaluasi, pr.nip, pg.nama, pg.jbtn, " +
                "'Ranap' as jns_rawat, v.nm_dokter as nm_dokter_verif, DATE_FORMAT(v.tgl_verifikasi,'%d-%m-%Y %H:%i:%s') as tgl_verif " +
                "from pemeriksaan_ranap pr " +
                "inner join reg_periksa rp on pr.no_rawat=rp.no_rawat " +
                "inner join pasien p on rp.no_rkm_medis=p.no_rkm_medis " +
                "inner join pegawai pg on pr.nip=pg.nik " +
                "inner join verifikasi_cppt_dpjp v on pr.no_rawat=v.no_rawat and pr.tgl_perawatan=v.tgl_perawatan and pr.jam_rawat=v.jam_rawat and v.jenis_rawat='Ranap' " +
                "where rp.no_rkm_medis='"+NoRM.getText().trim()+"' " +
                "order by tgl_perawatan, jam_rawat";

            System.out.println("=== DEBUG PRINT CPPT (Hanya Terverifikasi) ===");
            System.out.println("No.RM: " + NoRM.getText().trim());
            System.out.println("Query: " + querySQL);

            ps = koneksi.prepareStatement(querySQL);
            rs = ps.executeQuery();
            int no = 1;
            int totalData = 0;

            while(rs.next()){
                totalData++;
                // Set data pasien untuk header (ambil dari record pertama)
                if(no == 1) {
                    currentNoRawat = rs.getString(1);
                    currentNoRM = rs.getString(2);
                    currentNamaPasien = rs.getString(3);
                    System.out.println("Data pertama found - No.Rawat: " + currentNoRawat);
                }

                // Format data dengan SOAP dan vital sign (format sederhana)
                String soap = "S: "+rs.getString(15).replaceAll("(\r\n|\r|\n|\n\r)","<br>")+"<br>" +
                              "O: "+rs.getString(16).replaceAll("(\r\n|\r|\n|\n\r)","<br>")+"<br>" +
                              "A: "+rs.getString(18).replaceAll("(\r\n|\r|\n|\n\r)","<br>")+"<br>" +
                              "P: "+rs.getString(19).replaceAll("(\r\n|\r|\n|\n\r)","<br>")+"<br>" +
                              "Tensi: "+rs.getString(7)+"<br>" +
                              "Nadi: "+rs.getString(8)+"<br>" +
                              "Respirasi: "+rs.getString(9)+"<br>" +
                              "Suhu(C): "+rs.getString(6)+"<br>" +
                              (rs.getString(12).isEmpty()?"":"SpO2: "+rs.getString(12)+"%<br>") +
                              (rs.getString(13).isEmpty()?"":"GCS: "+rs.getString(13)+"<br>") +
                              "Kesadaran: "+rs.getString(14);

                String ppa = rs.getString(23)+"<br>"+rs.getString(24);

                String instruksi = rs.getString(20).replaceAll("(\r\n|\r|\n|\n\r)","<br>");

                // Verifikasi DPJP - Semua data pasti sudah terverifikasi
                String verifikasiDPJP = "";
                String qrCodeDokter = "";
                try {
                    // Query untuk mendapatkan kode dokter dari tabel verifikasi_cppt_dpjp
                    PreparedStatement psVerif = koneksi.prepareStatement(
                        "select kd_dokter from verifikasi_cppt_dpjp " +
                        "where no_rawat=? and tgl_perawatan=? and jam_rawat=? and jenis_rawat=?");
                    psVerif.setString(1, rs.getString(1));
                    psVerif.setString(2, rs.getString(4));
                    psVerif.setString(3, rs.getString(5));
                    psVerif.setString(4, rs.getString(25));
                    ResultSet rsVerif = psVerif.executeQuery();

                    if(rsVerif.next()) {
                        String kdDokter = rsVerif.getString("kd_dokter");
                        get = new GetMethod("http://"+koneksiDB.HOSTHYBRIDWEB()+":"+koneksiDB.PORTWEB()+"/"+koneksiDB.HYBRIDWEB()+"/penggajian/generateqrcode.php?kodedokter="+kdDokter.replace(" ","_"));
                        http.executeMethod(get);
                        qrCodeDokter = "<div style='text-align:center;'><img width='80' height='80' src='http://"+koneksiDB.HOSTHYBRIDWEB()+":"+koneksiDB.PORTWEB()+"/"+koneksiDB.HYBRIDWEB()+"/penggajian/temp/"+kdDokter+".png'/></div>";
                    }
                    rsVerif.close();
                    psVerif.close();
                } catch (Exception e) {
                    qrCodeDokter = "";
                    System.out.println("Error get QR: "+e);
                }
                verifikasiDPJP = qrCodeDokter+"<div style='text-align:center;'>"+rs.getString(26)+"<br>"+rs.getString(27)+"</div>";

                htmlContent += "            <tr>\n" +
                        "                <td class='nowrap center' style='vertical-align: top;'>"+rs.getString(4)+"<br>"+rs.getString(5)+"</td>\n" +
                        "                <td style='vertical-align: top;'>"+ppa+"</td>\n" +
                        "                <td style='vertical-align: top;'>"+soap+"</td>\n" +
                        "                <td style='vertical-align: top;'>"+instruksi+"</td>\n" +
                        "                <td style='vertical-align: top; text-align: center;'>"+verifikasiDPJP+"</td>\n" +
                        "            </tr>\n";
                no++;
            }

            System.out.println("Total data ditemukan: " + totalData);

            if(totalData == 0) {
                htmlContent += "            <tr>\n" +
                        "                <td colspan='5' style='text-align:center; padding:20px; color:#999;'><i>Tidak ada data pemeriksaan yang sudah terverifikasi untuk pasien ini</i></td>\n" +
                        "            </tr>\n";
            }

        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
            e.printStackTrace();
        }

        htmlContent += "        </tbody>\n" +
                "    </table>\n" +
                "    <div class='footer'>\n" +
                "        <p>Dicetak pada: "+Valid.SetTgl(Sequel.cariIsi("select current_date()"))+" | "+Sequel.cariIsi("select current_time()")+"</p>\n" +
                "    </div>\n" +
                "    <script>\n" +
                "        // Update data pasien di header\n" +
                "        document.getElementById('no_rawat').textContent = '"+currentNoRawat+"';\n" +
                "        document.getElementById('no_rkm_medis').textContent = '"+currentNoRM+"';\n" +
                "        document.getElementById('nm_pasien').textContent = '"+currentNamaPasien+"';\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>";

        // Simpan HTML ke file dan buka di browser
        try {
            // Update data pasien di JavaScript
            currentNoRM = NoRM.getText().trim();
            currentNamaPasien = NmPasien.getText().trim();

            java.io.File file = new java.io.File("report/Laporan_CPPT_"+currentNoRM.replace("/","_")+".html");
            java.io.FileWriter fw = new java.io.FileWriter(file);

            // Update JavaScript dengan data pasien yang benar
            String finalHtml = htmlContent.replace("'"+currentNoRawat+"'", "'"+currentNoRawat+"'")
                                         .replace("'"+currentNoRM+"'", "'"+currentNoRM+"'")
                                         .replace("'"+currentNamaPasien+"'", "'"+currentNamaPasien+"'");

            fw.write(finalHtml);
            fw.close();

            // Buka file HTML di browser default
            if (java.awt.Desktop.isDesktopSupported()) {
                java.awt.Desktop.getDesktop().browse(file.toURI());
            } else {
                JOptionPane.showMessageDialog(null, "File HTML berhasil dibuat di: " + file.getAbsolutePath());
            }
        } catch (Exception e) {
            System.out.println("Error membuat file HTML: " + e);
            JOptionPane.showMessageDialog(null, "Gagal membuat file HTML: " + e.getMessage());
        }

        this.setCursor(Cursor.getDefaultCursor());
    
    }//GEN-LAST:event_BtnPrintActionPerformed

    private void BtnPrintKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnPrintKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnPrintActionPerformed(null);
        }else{
            Valid.pindah(evt, Tgl2, BtnKeluar);
        }
    }//GEN-LAST:event_BtnPrintKeyPressed

    private void R1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_R1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_R1ActionPerformed

    private void TabRawatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabRawatMouseClicked
       
    }//GEN-LAST:event_TabRawatMouseClicked

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            RMVerifikasiCPPT dialog = new RMVerifikasiCPPT(new javax.swing.JFrame(), true);
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
    private widget.TextBox Agama;
    private widget.TextBox Alamat;
    private widget.TextBox Bahasa;
    private widget.Button BtnKeluar;
    private widget.Button BtnPasien;
    private widget.Button BtnPrint;
    private widget.TextBox CacatFisik;
    private widget.CekBox ChkInput;
    private widget.panelisi FormInput;
    private widget.TextBox GD;
    private widget.TextBox IbuKandung;
    private widget.TextBox Jk;
    private widget.editorpane LoadHTMLSOAPI;
    private widget.TextBox NmPasien;
    private widget.TextBox NoRM;
    private javax.swing.JPanel PanelInput;
    private widget.TextBox Pekerjaan;
    private widget.TextBox Pendidikan;
    private widget.RadioButton R1;
    private widget.RadioButton R2;
    private widget.RadioButton R3;
    private widget.ScrollPane Scroll2;
    private widget.TextBox StatusNikah;
    private javax.swing.JTabbedPane TabRawat;
    private widget.TextBox TanggalLahir;
    private widget.TextBox TempatLahir;
    private widget.Tanggal Tgl1;
    private widget.Tanggal Tgl2;
    private javax.swing.ButtonGroup buttonGroup1;
    private widget.InternalFrame internalFrame1;
    private widget.Label label17;
    private widget.Label label18;
    private widget.Label label19;
    private widget.Label label20;
    private widget.Label label21;
    private widget.Label label22;
    private widget.Label label23;
    private widget.Label label24;
    private widget.Label label25;
    private widget.Label label26;
    private widget.Label label27;
    private widget.Label label28;
    private widget.Label label29;
    private widget.panelisi panelGlass5;
    // End of variables declaration//GEN-END:variables

    public void setNoRm(String norm,String nama, String norawat) {
        NoRM.setText(norm);
        NmPasien.setText(nama);
        isPasien();
        tampilSOAPI();
    }

    private void isPasien() {
        try{
            ps=koneksi.prepareStatement(
                    "select pasien.no_rkm_medis,pasien.nm_pasien,pasien.jk,pasien.tmp_lahir,pasien.tgl_lahir,pasien.agama,"+
                    "bahasa_pasien.nama_bahasa,cacat_fisik.nama_cacat,pasien.gol_darah,pasien.nm_ibu,pasien.stts_nikah,pasien.pnd, "+
                    "concat(pasien.alamat,', ',kelurahan.nm_kel,', ',kecamatan.nm_kec,', ',kabupaten.nm_kab) as alamat,pasien.pekerjaan "+
                    "from pasien inner join bahasa_pasien on bahasa_pasien.id=pasien.bahasa_pasien "+
                    "inner join cacat_fisik on cacat_fisik.id=pasien.cacat_fisik "+
                    "inner join kelurahan on pasien.kd_kel=kelurahan.kd_kel "+
                    "inner join kecamatan on pasien.kd_kec=kecamatan.kd_kec "+
                    "inner join kabupaten on pasien.kd_kab=kabupaten.kd_kab "+
                    "where pasien.no_rkm_medis=?");
            try {
                ps.setString(1,NoRM.getText());
                rs=ps.executeQuery();
                if(rs.next()){
                    NoRM.setText(rs.getString("no_rkm_medis"));
                    NmPasien.setText(rs.getString("nm_pasien"));
                    Jk.setText(rs.getString("jk"));
                    TempatLahir.setText(rs.getString("tmp_lahir"));
                    TanggalLahir.setText(rs.getString("tgl_lahir"));
                    Alamat.setText(rs.getString("alamat"));
                    GD.setText(rs.getString("gol_darah"));
                    IbuKandung.setText(rs.getString("nm_ibu"));
                    Agama.setText(rs.getString("agama"));
                    StatusNikah.setText(rs.getString("stts_nikah"));
                    Pendidikan.setText(rs.getString("pnd"));
                    Bahasa.setText(rs.getString("nama_bahasa"));
                    CacatFisik.setText(rs.getString("nama_cacat"));
                    Pekerjaan.setText(rs.getString("pekerjaan"));
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notif : "+e);
        }
    }
   
    
    

    private void tampilPerawatan() {
        try{   
            htmlContent = new StringBuilder();
            if(R1.isSelected()==true){
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_reg,reg_periksa.no_rawat,reg_periksa.tgl_registrasi,reg_periksa.jam_reg,"+
                    "reg_periksa.kd_dokter,dokter.nm_dokter,poliklinik.nm_poli,reg_periksa.p_jawab,reg_periksa.almt_pj,"+
                    "reg_periksa.hubunganpj,reg_periksa.biaya_reg,reg_periksa.status_lanjut,penjab.png_jawab,"+
                    "reg_periksa.umurdaftar,reg_periksa.sttsumur "+
                    "from reg_periksa inner join dokter on reg_periksa.kd_dokter=dokter.kd_dokter "+
                    "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                    "where reg_periksa.stts<>'Batal' and reg_periksa.no_rkm_medis=? order by reg_periksa.tgl_registrasi desc limit 1");//limit 5
            }/*else if(R2.isSelected()==true){
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_reg,reg_periksa.no_rawat,reg_periksa.tgl_registrasi,reg_periksa.jam_reg,"+
                    "reg_periksa.kd_dokter,dokter.nm_dokter,poliklinik.nm_poli,reg_periksa.p_jawab,reg_periksa.almt_pj,"+
                    "reg_periksa.hubunganpj,reg_periksa.biaya_reg,reg_periksa.status_lanjut,penjab.png_jawab,"+
                    "reg_periksa.umurdaftar,reg_periksa.sttsumur "+
                    "from reg_periksa inner join dokter on reg_periksa.kd_dokter=dokter.kd_dokter "+
                    "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                    "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                    "where reg_periksa.stts<>'Batal' and reg_periksa.no_rkm_medis=? order by reg_periksa.tgl_registrasi");
            }else if(R3.isSelected()==true){
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_reg,reg_periksa.no_rawat,reg_periksa.tgl_registrasi,reg_periksa.jam_reg,"+
                    "reg_periksa.kd_dokter,dokter.nm_dokter,poliklinik.nm_poli,reg_periksa.p_jawab,reg_periksa.almt_pj,"+
                    "reg_periksa.hubunganpj,reg_periksa.biaya_reg,reg_periksa.status_lanjut,penjab.png_jawab,"+
                    "reg_periksa.umurdaftar,reg_periksa.sttsumur "+
                    "from reg_periksa inner join dokter on reg_periksa.kd_dokter=dokter.kd_dokter "+
                    "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                    "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                    "where reg_periksa.stts<>'Batal' and reg_periksa.no_rkm_medis=? and "+
                    "reg_periksa.tgl_registrasi between ? and ? order by reg_periksa.tgl_registrasi");
            }else if(R4.isSelected()==true){
                ps=koneksi.prepareStatement(
                    "select reg_periksa.no_reg,reg_periksa.no_rawat,reg_periksa.tgl_registrasi,reg_periksa.jam_reg,"+
                    "reg_periksa.kd_dokter,dokter.nm_dokter,poliklinik.nm_poli,reg_periksa.p_jawab,reg_periksa.almt_pj,"+
                    "reg_periksa.hubunganpj,reg_periksa.biaya_reg,reg_periksa.status_lanjut,penjab.png_jawab,"+
                    "reg_periksa.umurdaftar,reg_periksa.sttsumur "+
                    "from reg_periksa inner join dokter on reg_periksa.kd_dokter=dokter.kd_dokter "+
                    "inner join poliklinik on reg_periksa.kd_poli=poliklinik.kd_poli "+
                    "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                    "where reg_periksa.stts<>'Batal' and reg_periksa.no_rkm_medis=? and reg_periksa.no_rawat=?");
            }*/
            
            try {
                i=0;
                if(R1.isSelected()==true){
                    ps.setString(1,NoRM.getText().trim());
                }/*else if(R2.isSelected()==true){
                    ps.setString(1,NoRM.getText().trim());
                }else if(R3.isSelected()==true){
                    ps.setString(1,NoRM.getText().trim());
                    ps.setString(2,Valid.SetTgl(Tgl1.getSelectedItem()+""));
                    ps.setString(3,Valid.SetTgl(Tgl2.getSelectedItem()+""));
                }else if(R4.isSelected()==true){
                    ps.setString(1,NoRM.getText().trim());
                    ps.setString(2,NoRawat.getText().trim());
                }   */         
                urut=1;
                rs=ps.executeQuery();
                while(rs.next()){
                    try {
                        dokterrujukan="";
                        polirujukan="";
                        rs2=koneksi.prepareStatement(
                            "select poliklinik.nm_poli,dokter.nm_dokter from rujukan_internal_poli "+
                            "inner join poliklinik on rujukan_internal_poli.kd_poli=poliklinik.kd_poli "+
                            "inner join dokter on rujukan_internal_poli.kd_dokter=dokter.kd_dokter "+
                            "where no_rawat='"+rs.getString("no_rawat")+"'").executeQuery();
                        while(rs2.next()){
                            polirujukan=polirujukan+", "+rs2.getString("nm_poli");
                            dokterrujukan=dokterrujukan+", "+rs2.getString("nm_dokter");
                        }
                    } catch (Exception e) {
                        System.out.println("Notif : "+e);
                    } finally{
                        if(rs2!=null){
                            rs2.close();
                        }
                    }   

                    htmlContent.append(
                      "<tr class='isi'>"+ 
                        "<td valign='top' width='2%'>"+urut+"</td>"+
                        "<td valign='top' width='18%'>No.Rawat</td>"+
                        "<td valign='top' width='1%' align='center'>:</td>"+
                        "<td valign='top' width='79%'>"+rs.getString("no_rawat")+"</td>"+
                      "</tr>"+
                      "<tr class='isi'>"+ 
                        "<td valign='top' width='2%'></td>"+
                        "<td valign='top' width='18%'>No.Registrasi</td>"+
                        "<td valign='top' width='1%' align='center'>:</td>"+
                        "<td valign='top' width='79%'>"+rs.getString("no_reg")+"</td>"+
                      "</tr>"+
                      "<tr class='isi'>"+ 
                        "<td valign='top' width='2%'></td>"+
                        "<td valign='top' width='18%'>Tanggal Registrasi</td>"+
                        "<td valign='top' width='1%' align='center'>:</td>"+
                        "<td valign='top' width='79%'>"+rs.getString("tgl_registrasi")+" "+rs.getString("jam_reg")+"</td>"+
                      "</tr>"+
                      "<tr class='isi'>"+ 
                        "<td valign='top' width='2%'></td>"+
                        "<td valign='top' width='18%'>Umur Saat Daftar</td>"+
                        "<td valign='top' width='1%' align='center'>:</td>"+
                        "<td valign='top' width='79%'>"+rs.getString("umurdaftar")+" "+rs.getString("sttsumur")+"</td>"+
                      "</tr>"+
                      "<tr class='isi'>"+ 
                        "<td valign='top' width='2%'></td>"+
                        "<td valign='top' width='18%'>Unit/Poliklinik</td>"+
                        "<td valign='top' width='1%' align='center'>:</td>"+
                        "<td valign='top' width='79%'>"+rs.getString("nm_poli")+polirujukan+"</td>"+
                      "</tr>"+
                      "<tr class='isi'>"+ 
                        "<td valign='top' width='2%'></td>"+        
                        "<td valign='top' width='18%'>Dokter Poli</td>"+
                        "<td valign='top' width='1%' align='center'>:</td>"+
                        "<td valign='top' width='79%'>"+rs.getString("nm_dokter")+dokterrujukan+"</td>"+
                      "</tr>"
                    );
                    if(rs.getString("status_lanjut").equals("Ranap")){
                        try{
                            rs3=koneksi.prepareStatement(
                                "select dokter.nm_dokter from dpjp_ranap inner join dokter on dpjp_ranap.kd_dokter=dokter.kd_dokter where dpjp_ranap.no_rawat='"+rs.getString("no_rawat")+"'").executeQuery();
                            if(rs3.next()){
                                htmlContent.append(
                                  "<tr class='isi'>"+ 
                                    "<td valign='top' width='2%'></td>"+        
                                    "<td valign='top' width='18%'>DPJP Ranap</td>"+
                                    "<td valign='top' width='1%' align='center'>:</td>"+
                                    "<td valign='top' width='79%'>"
                                );
                                rs3.beforeFirst();
                                urutdpjp=1;
                                while(rs3.next()){
                                    htmlContent.append(urutdpjp+". "+rs3.getString("nm_dokter")+"&nbsp;&nbsp;");
                                    urutdpjp++;
                                }
                                htmlContent.append("</td>"+
                                  "</tr>"
                                );    
                            }
                        } catch (Exception e) {
                            System.out.println("Status Lanjut : "+e);
                        } finally{
                            if(rs3!=null){
                                rs3.close();
                            }
                        }
                    }
                    htmlContent.append( 
                      "<tr class='isi'>"+ 
                        "<td valign='top' width='2%'></td>"+
                        "<td valign='top' width='18%'>Cara Bayar</td>"+
                        "<td valign='top' width='1%' align='center'>:</td>"+
                        "<td valign='top' width='79%'>"+rs.getString("png_jawab")+"</td>"+
                      "</tr>"+
                      "<tr class='isi'>"+ 
                        "<td valign='top' width='2%'></td>"+        
                        "<td valign='top' width='18%'>Penanggung Jawab</td>"+
                        "<td valign='top' width='1%' align='center'>:</td>"+
                        "<td valign='top' width='79%'>"+rs.getString("p_jawab")+"</td>"+
                      "</tr>"+
                      "<tr class='isi'>"+ 
                        "<td valign='top' width='2%'></td>"+         
                        "<td valign='top' width='18%'>Alamat P.J.</td>"+
                        "<td valign='top' width='1%' align='center'>:</td>"+
                        "<td valign='top' width='79%'>"+rs.getString("almt_pj")+"</td>"+
                      "</tr>"+
                      "<tr class='isi'>"+ 
                        "<td valign='top' width='2%'></td>"+        
                        "<td valign='top' width='18%'>Hubungan P.J.</td>"+
                        "<td valign='top' width='1%' align='center'>:</td>"+
                        "<td valign='top' width='79%'>"+rs.getString("hubunganpj")+"</td>"+
                      "</tr>"+
                      "<tr class='isi'>"+ 
                        "<td valign='top' width='2%'></td>"+        
                        "<td valign='top' width='18%'>Status</td>"+
                        "<td valign='top' width='1%' align='center'>:</td>"+
                        "<td valign='top' width='79%'>"+rs.getString("status_lanjut")+"</td>"+
                      "</tr>"
                    );
                    urut++;

                    biayaperawatan=rs.getDouble("biaya_reg");
                    //biaya administrasi
                    htmlContent.append(
                       "<tr class='isi'>"+ 
                         "<td valign='top' width='2%'></td>"+        
                         "<td valign='top' width='18%'>Biaya & Perawatan</td>"+
                         "<td valign='top' width='1%' align='center'>:</td>"+
                         "<td valign='top' width='79%'>"+
                             "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                               "<tr>"+
                                 "<td valign='top' width='89%'>Administrasi</td>"+
                                 "<td valign='top' width='1%' align='right'>:</td>"+
                                 "<td valign='top' width='10%' align='right'>"+Valid.SetAngka(rs.getDouble("biaya_reg"))+"</td>"+
                               "</tr>"+
                             "</table>"
                    );
                    
  
                   
                  
                    htmlContent.append(
                        "<tr class='isi'><td></td><td colspan='3' align='right'>&nbsp;</tr>"
                    );
                    
                }
                
                
            } catch (Exception e) {
                System.out.println("Notifikasi : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }                
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
    }

    private void panggilLaporan(String teks) {
        try{
            File g = new File("file.css");            
            BufferedWriter bg = new BufferedWriter(new FileWriter(g));
            bg.write(".isi td{border-right: 1px solid #e2e7dd;font: 8.5px tahoma;height:12px;border-bottom: 1px solid #e2e7dd;background: #ffffff;color:#323232;}.isi a{text-decoration:none;color:#8b9b95;padding:0 0 0 0px;font-family: Tahoma;font-size: 8.5px;border: white;}");
            bg.close();

            File f = new File("riwayat.html");            
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            bw.write(
                 teks.replaceAll("<head>","<head><link href=\"file.css\" rel=\"stylesheet\" type=\"text/css\" />").
                      replaceAll("<body>",
                                 "<body>"+
                                    "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                                       "<tr class='isi'>"+ 
                                         "<td valign='top' width='20%'>No.RM</td>"+
                                         "<td valign='top' width='1%' align='center'>:</td>"+
                                         "<td valign='top' width='79%'>"+NoRM.getText().trim()+"</td>"+
                                       "</tr>"+
                                       "<tr class='isi'>"+ 
                                         "<td valign='top' width='20%'>Nama Pasien</td>"+
                                         "<td valign='top' width='1%' align='center'>:</td>"+
                                         "<td valign='top' width='79%'>"+NmPasien.getText()+"</td>"+
                                       "</tr>"+
                                       "<tr class='isi'>"+ 
                                         "<td valign='top' width='20%'>Alamat</td>"+
                                         "<td valign='top' width='1%' align='center'>:</td>"+
                                         "<td valign='top' width='79%'>"+Alamat.getText()+"</td>"+
                                       "</tr>"+
                                       "<tr class='isi'>"+ 
                                         "<td valign='top' width='20%'>Jenis Kelamin</td>"+
                                         "<td valign='top' width='1%' align='center'>:</td>"+
                                         "<td valign='top' width='79%'>"+Jk.getText().replaceAll("L","Laki-Laki").replaceAll("P","Perempuan")+"</td>"+
                                       "</tr>"+
                                       "<tr class='isi'>"+ 
                                         "<td valign='top' width='20%'>Tempat & Tanggal Lahir</td>"+
                                         "<td valign='top' width='1%' align='center'>:</td>"+
                                         "<td valign='top' width='79%'>"+TempatLahir.getText()+" "+TanggalLahir.getText()+"</td>"+
                                       "</tr>"+
                                       "<tr class='isi'>"+ 
                                         "<td valign='top' width='20%'>Ibu Kandung</td>"+
                                         "<td valign='top' width='1%' align='center'>:</td>"+
                                         "<td valign='top' width='79%'>"+IbuKandung.getText()+"</td>"+
                                       "</tr>"+
                                       "<tr class='isi'>"+ 
                                         "<td valign='top' width='20%'>Golongan Darah</td>"+
                                         "<td valign='top' width='1%' align='center'>:</td>"+
                                         "<td valign='top' width='79%'>"+GD.getText()+"</td>"+
                                       "</tr>"+
                                       "<tr class='isi'>"+ 
                                         "<td valign='top' width='20%'>Status Nikah</td>"+
                                         "<td valign='top' width='1%' align='center'>:</td>"+
                                         "<td valign='top' width='79%'>"+StatusNikah.getText()+"</td>"+
                                       "</tr>"+
                                       "<tr class='isi'>"+ 
                                         "<td valign='top' width='20%'>Agama</td>"+
                                         "<td valign='top' width='1%' align='center'>:</td>"+
                                         "<td valign='top' width='79%'>"+Agama.getText()+"</td>"+
                                       "</tr>"+
                                       "<tr class='isi'>"+ 
                                         "<td valign='top' width='20%'>Pendidikan Terakhir</td>"+
                                         "<td valign='top' width='1%' align='center'>:</td>"+
                                         "<td valign='top' width='79%'>"+Pendidikan.getText()+"</td>"+
                                       "</tr>"+
                                       "<tr class='isi'>"+ 
                                         "<td valign='top' width='20%'>Bahasa Dipakai</td>"+
                                         "<td valign='top' width='1%' align='center'>:</td>"+
                                         "<td valign='top' width='79%'>"+Bahasa.getText()+"</td>"+
                                       "</tr>"+
                                       "<tr class='isi'>"+ 
                                         "<td valign='top' width='20%'>Cacat Fisik</td>"+
                                         "<td valign='top' width='1%' align='center'>:</td>"+
                                         "<td valign='top' width='79%'>"+CacatFisik.getText()+"</td>"+
                                       "</tr>"+
                                    "</table>"            
                      ).
                      replaceAll((getClass().getResource("/picture/"))+"","./gambar/")
            );  
            bw.close();
            Desktop.getDesktop().browse(f.toURI());
        } catch (Exception e) {
            System.out.println("Notifikasi : "+e);
        }   
    }
    
    private void isForm(){
        if(ChkInput.isSelected()==true){
            ChkInput.setVisible(false);
            PanelInput.setPreferredSize(new Dimension(WIDTH,126));
            FormInput.setVisible(true);      
            ChkInput.setVisible(true);
        }else if(ChkInput.isSelected()==false){           
            ChkInput.setVisible(false);            
            PanelInput.setPreferredSize(new Dimension(WIDTH,20));
            FormInput.setVisible(false);      
            ChkInput.setVisible(true);
        }
    }

    private void tampilSOAPI() {
        try {
            htmlContent = new StringBuilder();
            // Header utama - hanya satu kali
            htmlContent.append("<tr class='isi'>").
                            append("<td valign='middle' bgcolor='#FFFAF8' align='center' width='5%'>Tgl.Reg</td>").
                            append("<td valign='middle' bgcolor='#FFFAF8' align='center' width='7%'>No.Rawat</td>").
                            append("<td valign='middle' bgcolor='#FFFAF8' align='center' width='6%'>Status</td>").
                            append("<td valign='middle' bgcolor='#FFFAF8' align='center' width='8%'>Tgl/Jam</td>").
                            append("<td valign='middle' bgcolor='#FFFAF8' align='center' width='12%'>Profesional Pemberi Asuhan</td>").
                            append("<td valign='middle' bgcolor='#FFFAF8' align='center' width='35%'>Hasil Asesmen Penatalaksanaan Pasien<br>(Tulis dengan format SOAP, disertai sasaran, Tulis nama, beri paraf/tanda akhir catatan)</td>").
                            append("<td valign='middle' bgcolor='#FFFAF8' align='center' width='15%'>Instruksi PPA<br>(Intruksi ditulis dengan rinci dan jelas)<br>termasuk pasca bedah</td>").
                            append("<td valign='middle' bgcolor='#FFFAF8' align='center' width='12%'>Verifikasi DPJP<br>(Tulis Nama, Beri paraf, Tgl, Jam)<br>(DPJP harus membaca/menreview seluruh rencana asuhan)</td>").
                        append("</tr>");

            ps=koneksi.prepareStatement(
                "select reg_periksa.no_reg,reg_periksa.no_rawat,reg_periksa.tgl_registrasi,reg_periksa.status_lanjut "+
                "from reg_periksa where reg_periksa.stts<>'Batal' and reg_periksa.no_rkm_medis=? order by reg_periksa.tgl_registrasi desc limit 20");

            try {
                ps.setString(1,NoRM.getText().trim());
                rs=ps.executeQuery();

                boolean adaData = false;

                while(rs.next()){
                    adaData = true;
                    String noRawatCurrent = rs.getString("no_rawat");
                    String tglRegistrasi = rs.getString("tgl_registrasi");
                    System.out.println("Processing No.Rawat: " + noRawatCurrent);

                    // Hitung total baris SOAP untuk registrasi ini (untuk rowspan)
                    int totalRowsSOAP = 0;
                    try {
                        ResultSet rsCount = koneksi.prepareStatement(
                                "select count(*) as total from pemeriksaan_ralan where no_rawat='"+noRawatCurrent+"'").executeQuery();
                        if(rsCount.next()) {
                            totalRowsSOAP = rsCount.getInt("total");
                        }
                        rsCount.close();

                        rsCount = koneksi.prepareStatement(
                                "select count(*) as total from pemeriksaan_ranap where no_rawat='"+noRawatCurrent+"'").executeQuery();
                        if(rsCount.next()) {
                            totalRowsSOAP += rsCount.getInt("total");
                        }
                        rsCount.close();
                    } catch (Exception e) {
                        System.out.println("Error counting SOAP: "+e);
                    }

                    System.out.println("Total SOAP rows for " + noRawatCurrent + ": " + totalRowsSOAP);

                    // Query pemeriksaan rawat jalan
                    int countRalan = 0;
                    boolean isFirstRow = true;
                    try {
                        rs2=koneksi.prepareStatement(
                                "select pemeriksaan_ralan.no_rawat,pemeriksaan_ralan.tgl_perawatan,pemeriksaan_ralan.jam_rawat,pemeriksaan_ralan.suhu_tubuh,pemeriksaan_ralan.tensi,pemeriksaan_ralan.nadi,pemeriksaan_ralan.respirasi,"+
                                "pemeriksaan_ralan.tinggi,pemeriksaan_ralan.berat,pemeriksaan_ralan.gcs,pemeriksaan_ralan.spo2,pemeriksaan_ralan.kesadaran,pemeriksaan_ralan.keluhan, "+
                                "pemeriksaan_ralan.pemeriksaan,pemeriksaan_ralan.alergi,pemeriksaan_ralan.lingkar_perut,pemeriksaan_ralan.rtl,pemeriksaan_ralan.penilaian,"+
                                "pemeriksaan_ralan.instruksi,pemeriksaan_ralan.evaluasi,pemeriksaan_ralan.nip,pegawai.nama,pegawai.jbtn from pemeriksaan_ralan inner join pegawai on pemeriksaan_ralan.nip=pegawai.nik where "+
                                "pemeriksaan_ralan.no_rawat='"+noRawatCurrent+"' "+
                                "order by pemeriksaan_ralan.tgl_perawatan,pemeriksaan_ralan.jam_rawat").executeQuery();
                        while(rs2.next()){
                            countRalan++;
                            // Format SOAP dengan vital signs
                            String soapContent = "S: "+rs2.getString("keluhan").replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("(\r\n|\r|\n|\n\r)","<br>")+"<br>"+
                                               "O: "+rs2.getString("pemeriksaan").replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("(\r\n|\r|\n|\n\r)","<br>")+
                                               "<br>A: "+rs2.getString("penilaian").replaceAll("(\r\n|\r|\n|\n\r)","<br>")+
                                               "<br>P: "+rs2.getString("rtl").replaceAll("(\r\n|\r|\n|\n\r)","<br>")+
                                               "<br>Tensi: "+rs2.getString("tensi")+
                                               "<br>Nadi: "+rs2.getString("nadi")+
                                               "<br>Respirasi: "+rs2.getString("respirasi")+
                                               "<br>Suhu(C): "+rs2.getString("suhu_tubuh")+
                                               (rs2.getString("tinggi").equals("")?"":"<br>Tinggi: "+rs2.getString("tinggi"))+
                                               (rs2.getString("berat").equals("")?"":"<br>Berat: "+rs2.getString("berat"))+
                                               (rs2.getString("spo2").equals("")?"":"<br>SpO2: "+rs2.getString("spo2")+"%")+
                                               (rs2.getString("gcs").equals("")?"":"<br>GCS(E,V,M): "+rs2.getString("gcs"))+
                                               (rs2.getString("kesadaran").equals("")?"":"<br>Kesadaran: "+rs2.getString("kesadaran"))+
                                               (rs2.getString("alergi").equals("")?"":"<br>Alergi: "+rs2.getString("alergi"));

                            // Cek apakah sudah diverifikasi DPJP
                            String verifikasiContent = "";
                            try {
                                rs3=koneksi.prepareStatement(
                                        "select kd_dokter,nm_dokter,DATE_FORMAT(tgl_verifikasi,'%d-%m-%Y %H:%i:%s') as tgl_verif "+
                                        "from verifikasi_cppt_dpjp where no_rawat='"+rs2.getString("no_rawat")+"' and "+
                                        "tgl_perawatan='"+rs2.getString("tgl_perawatan")+"' and jam_rawat='"+rs2.getString("jam_rawat")+"' and jenis_rawat='Ralan'").executeQuery();
                                if(rs3.next()){
                                    // Sudah diverifikasi, tampilkan QR code
                                    String qrCodeDokter = "";
                                    try {
                                        get = new GetMethod("http://"+koneksiDB.HOSTHYBRIDWEB()+":"+koneksiDB.PORTWEB()+"/"+koneksiDB.HYBRIDWEB()+"/penggajian/generateqrcode.php?kodedokter="+rs3.getString("kd_dokter").replace(" ","_"));
                                        http.executeMethod(get);
                                        qrCodeDokter = "<img width='60' height='60' src='http://"+koneksiDB.HOSTHYBRIDWEB()+":"+koneksiDB.PORTWEB()+"/"+koneksiDB.HYBRIDWEB()+"/penggajian/temp/"+rs3.getString("kd_dokter")+".png'/>";
                                    } catch (Exception e) {
                                        qrCodeDokter = "";
                                    }
                                    verifikasiContent = qrCodeDokter+"<br>"+rs3.getString("nm_dokter")+"<br><small>"+rs3.getString("tgl_verif")+"</small>";
                                } else {
                                    // Belum diverifikasi, tampilkan link yang terlihat seperti tombol
                                    verifikasiContent = "<a href='verifikasi://"+rs2.getString("no_rawat")+"|"+rs2.getString("tgl_perawatan")+"|"+rs2.getString("jam_rawat")+"|Ralan' "+
                                                       "style='display:inline-block;padding:8px 15px;background-color:#4CAF50;color:white;text-decoration:none;border-radius:4px;font-weight:bold;'>Verifikasi</a>";
                                }
                            } catch (Exception e) {
                                System.out.println("Error cek verifikasi: "+e);
                                verifikasiContent = "<a href='verifikasi://"+rs2.getString("no_rawat")+"|"+rs2.getString("tgl_perawatan")+"|"+rs2.getString("jam_rawat")+"|Ralan' "+
                                                   "style='display:inline-block;padding:8px 15px;background-color:#4CAF50;color:white;text-decoration:none;border-radius:4px;font-weight:bold;'>Verifikasi</a>";
                            } finally {
                                if(rs3!=null){
                                    rs3.close();
                                }
                            }

                            htmlContent.append("<tr class='isi'>");

                            // Hanya tampilkan Tgl.Reg dan No.Rawat di baris pertama dengan rowspan
                            if(isFirstRow && totalRowsSOAP > 0) {
                                htmlContent.append("<td align='center' valign='top' rowspan='").append(totalRowsSOAP).append("'>").append(tglRegistrasi).append("</td>").
                                            append("<td align='center' valign='top' rowspan='").append(totalRowsSOAP).append("'>").append(noRawatCurrent).append("</td>");
                                isFirstRow = false;
                            }

                            htmlContent.append("<td align='center' valign='top'>Ralan</td>").
                                            append("<td align='center' valign='top'>").append(rs2.getString("tgl_perawatan")).append("<br>").append(rs2.getString("jam_rawat")).append("</td>").
                                            append("<td align='left' valign='top'>").append(rs2.getString("nama")).append("<br>").append(rs2.getString("jbtn")).append("</td>").
                                            append("<td align='left' valign='top'>").append(soapContent).append("</td>").
                                            append("<td align='left' valign='top'>").append(rs2.getString("instruksi").replaceAll("(\r\n|\r|\n|\n\r)","<br>")).append("</td>").
                                            append("<td align='center' valign='top'>").append(verifikasiContent).append("</td>").
                                        append("</tr>");
                        }
                        System.out.println("Found " + countRalan + " SOAP Ralan records for No.Rawat: " + noRawatCurrent);
                    } catch (Exception e) {
                        System.out.println("Error query Ralan: "+e);
                        e.printStackTrace();
                    } finally{
                        if(rs2!=null){
                            rs2.close();
                        }
                    }

                    // Query pemeriksaan rawat inap
                    int countRanap = 0;
                    try {
                        rs2=koneksi.prepareStatement(
                                "select pemeriksaan_ranap.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,"+
                                "pemeriksaan_ranap.tgl_perawatan,pemeriksaan_ranap.jam_rawat,pemeriksaan_ranap.suhu_tubuh,pemeriksaan_ranap.tensi, " +
                                "pemeriksaan_ranap.nadi,pemeriksaan_ranap.respirasi,pemeriksaan_ranap.tinggi, " +
                                "pemeriksaan_ranap.berat,pemeriksaan_ranap.spo2,pemeriksaan_ranap.gcs,pemeriksaan_ranap.kesadaran,pemeriksaan_ranap.keluhan, " +
                                "pemeriksaan_ranap.pemeriksaan,pemeriksaan_ranap.alergi,pemeriksaan_ranap.penilaian,pemeriksaan_ranap.rtl,"+
                                "pemeriksaan_ranap.instruksi,pemeriksaan_ranap.evaluasi,pemeriksaan_ranap.nip,pegawai.nama,pegawai.jbtn "+
                                "from pasien inner join reg_periksa on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                                "inner join pemeriksaan_ranap on pemeriksaan_ranap.no_rawat=reg_periksa.no_rawat "+
                                "inner join pegawai on pemeriksaan_ranap.nip=pegawai.nik where pemeriksaan_ranap.no_rawat='"+noRawatCurrent+"' "+
                                "order by pemeriksaan_ranap.tgl_perawatan,pemeriksaan_ranap.jam_rawat").executeQuery();
                        while(rs2.next()){
                            countRanap++;
                            // Format SOAP dengan vital signs untuk rawat inap
                            String soapContent = "S: "+rs2.getString("keluhan").replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("(\r\n|\r|\n|\n\r)","<br>")+"<br>"+
                                               "O: "+rs2.getString("pemeriksaan").replaceAll("<","&lt;").replaceAll(">","&gt;").replaceAll("(\r\n|\r|\n|\n\r)","<br>")+
                                               "<br>A: "+rs2.getString("penilaian").replaceAll("(\r\n|\r|\n|\n\r)","<br>")+
                                               "<br>P: "+rs2.getString("rtl").replaceAll("(\r\n|\r|\n|\n\r)","<br>")+
                                               "<br>Tensi: "+rs2.getString("tensi")+
                                               "<br>Nadi: "+rs2.getString("nadi")+
                                               "<br>Respirasi: "+rs2.getString("respirasi")+
                                               "<br>Suhu(C): "+rs2.getString("suhu_tubuh")+
                                               (rs2.getString("tinggi").equals("")?"":"<br>Tinggi: "+rs2.getString("tinggi"))+
                                               (rs2.getString("berat").equals("")?"":"<br>Berat: "+rs2.getString("berat"))+
                                               (rs2.getString("spo2").equals("")?"":"<br>SpO2: "+rs2.getString("spo2")+"%")+
                                               (rs2.getString("gcs").equals("")?"":"<br>GCS(E,V,M): "+rs2.getString("gcs"))+
                                               (rs2.getString("kesadaran").equals("")?"":"<br>Kesadaran: "+rs2.getString("kesadaran"))+
                                               (rs2.getString("alergi").equals("")?"":"<br>Alergi: "+rs2.getString("alergi"));

                            // Cek apakah sudah diverifikasi DPJP
                            String verifikasiContent = "";
                            try {
                                rs3=koneksi.prepareStatement(
                                        "select kd_dokter,nm_dokter,DATE_FORMAT(tgl_verifikasi,'%d-%m-%Y %H:%i:%s') as tgl_verif "+
                                        "from verifikasi_cppt_dpjp where no_rawat='"+rs2.getString("no_rawat")+"' and "+
                                        "tgl_perawatan='"+rs2.getString("tgl_perawatan")+"' and jam_rawat='"+rs2.getString("jam_rawat")+"' and jenis_rawat='Ranap'").executeQuery();
                                if(rs3.next()){
                                    // Sudah diverifikasi, tampilkan QR code
                                    String qrCodeDokter = "";
                                    try {
                                        get = new GetMethod("http://"+koneksiDB.HOSTHYBRIDWEB()+":"+koneksiDB.PORTWEB()+"/"+koneksiDB.HYBRIDWEB()+"/penggajian/generateqrcode.php?kodedokter="+rs3.getString("kd_dokter").replace(" ","_"));
                                        http.executeMethod(get);
                                        qrCodeDokter = "<img width='60' height='60' src='http://"+koneksiDB.HOSTHYBRIDWEB()+":"+koneksiDB.PORTWEB()+"/"+koneksiDB.HYBRIDWEB()+"/penggajian/temp/"+rs3.getString("kd_dokter")+".png'/>";
                                    } catch (Exception e) {
                                        qrCodeDokter = "";
                                    }
                                    verifikasiContent = qrCodeDokter+"<br>"+rs3.getString("nm_dokter")+"<br><small>"+rs3.getString("tgl_verif")+"</small>";
                                } else {
                                    // Belum diverifikasi, tampilkan link yang terlihat seperti tombol
                                    verifikasiContent = "<a href='verifikasi://"+rs2.getString("no_rawat")+"|"+rs2.getString("tgl_perawatan")+"|"+rs2.getString("jam_rawat")+"|Ranap' "+
                                                       "style='display:inline-block;padding:8px 15px;background-color:#4CAF50;color:white;text-decoration:none;border-radius:4px;font-weight:bold;'>Verifikasi</a>";
                                }
                            } catch (Exception e) {
                                System.out.println("Error cek verifikasi: "+e);
                                verifikasiContent = "<a href='verifikasi://"+rs2.getString("no_rawat")+"|"+rs2.getString("tgl_perawatan")+"|"+rs2.getString("jam_rawat")+"|Ranap' "+
                                                   "style='display:inline-block;padding:8px 15px;background-color:#4CAF50;color:white;text-decoration:none;border-radius:4px;font-weight:bold;'>Verifikasi</a>";
                            } finally {
                                if(rs3!=null){
                                    rs3.close();
                                }
                            }

                            htmlContent.append("<tr class='isi'>");

                            // Hanya tampilkan Tgl.Reg dan No.Rawat di baris pertama dengan rowspan
                            if(isFirstRow && totalRowsSOAP > 0) {
                                htmlContent.append("<td align='center' valign='top' rowspan='").append(totalRowsSOAP).append("'>").append(tglRegistrasi).append("</td>").
                                            append("<td align='center' valign='top' rowspan='").append(totalRowsSOAP).append("'>").append(noRawatCurrent).append("</td>");
                                isFirstRow = false;
                            }

                            htmlContent.append("<td align='center' valign='top'>Ranap</td>").
                                            append("<td align='center' valign='top'>").append(rs2.getString("tgl_perawatan")).append("<br>").append(rs2.getString("jam_rawat")).append("</td>").
                                            append("<td align='left' valign='top'>").append(rs2.getString("nama")).append("<br>").append(rs2.getString("jbtn")).append("</td>").
                                            append("<td align='left' valign='top'>").append(soapContent).append("</td>").
                                            append("<td align='left' valign='top'>").append(rs2.getString("instruksi").replaceAll("(\r\n|\r|\n|\n\r)","<br>")).append("</td>").
                                            append("<td align='center' valign='top'>").append(verifikasiContent).append("</td>").
                                        append("</tr>");
                        }
                        System.out.println("Found " + countRanap + " SOAP Ranap records for No.Rawat: " + noRawatCurrent);
                    } catch (Exception e) {
                        System.out.println("Error query Ranap: "+e);
                        e.printStackTrace();
                    } finally{
                        if(rs2!=null){
                            rs2.close();
                        }
                    }

                    System.out.println("Total SOAP for No.Rawat " + noRawatCurrent + ": " + (countRalan + countRanap) + " records");
                }

                // Jika tidak ada data registrasi
                if(!adaData) {
                    htmlContent.append("<tr class='isi'>").
                               append("<td colspan='8' align='center' style='padding:20px;'>").
                               append("<b>Tidak ada data registrasi untuk pasien ini</b>").
                               append("</td>").
                            append("</tr>");
                    System.out.println("Tidak ada data reg_periksa untuk No.RM: " + NoRM.getText().trim());
                }

            } catch (Exception e) {
                System.out.println("Notif : "+e);
                e.printStackTrace();
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }

            // Debug: cek isi htmlContent
            System.out.println("=== DEBUG tampilSOAPI ===");
            System.out.println("No.RM: " + NoRM.getText().trim());
            System.out.println("HTML Length: " + htmlContent.length());
            System.out.println("HTML Preview: " + htmlContent.substring(0, Math.min(200, htmlContent.length())));

            LoadHTMLSOAPI.setText(
                    "<html>"+
                      "<table width='100%' border='0' align='center' cellpadding='3px' cellspacing='0' class='tbl_form'>"+
                       htmlContent.toString()+
                      "</table>"+
                    "</html>");
        } catch (Exception e) {
            System.out.println("Notif tampilSOAPI : "+e);
            e.printStackTrace();
        }
    }

    private void prosesVerifikasi(String noRawat, String tglPerawatan, String jamRawat, String jenisRawat) {
        // Cek apakah user yang login adalah petugas yang bersangkutan
        String nipLogin = akses.getkode();
        String nipPetugas = "";
        String namaPetugas = "";

        try {
            // Cek petugas dari pemeriksaan
            String queryPetugas = "";
            if(jenisRawat.equals("Ralan")) {
                queryPetugas = "select pemeriksaan_ralan.nip, pegawai.nama from pemeriksaan_ralan " +
                              "inner join pegawai on pemeriksaan_ralan.nip=pegawai.nik " +
                              "where pemeriksaan_ralan.no_rawat=? and pemeriksaan_ralan.tgl_perawatan=? and pemeriksaan_ralan.jam_rawat=?";
            } else {
                queryPetugas = "select pemeriksaan_ranap.nip, pegawai.nama from pemeriksaan_ranap " +
                              "inner join pegawai on pemeriksaan_ranap.nip=pegawai.nik " +
                              "where pemeriksaan_ranap.no_rawat=? and pemeriksaan_ranap.tgl_perawatan=? and pemeriksaan_ranap.jam_rawat=?";
            }

            PreparedStatement psPetugas = koneksi.prepareStatement(queryPetugas);
            psPetugas.setString(1, noRawat);
            psPetugas.setString(2, tglPerawatan);
            psPetugas.setString(3, jamRawat);
            ResultSet rsPetugas = psPetugas.executeQuery();

            if(rsPetugas.next()) {
                nipPetugas = rsPetugas.getString("nip");
                namaPetugas = rsPetugas.getString("nama");
            }
            rsPetugas.close();
            psPetugas.close();

            // Cek apakah login sebagai petugas yang bersangkutan
            if(nipLogin.equals(nipPetugas)) {
                // Langsung verifikasi tanpa dialog
                try {
                    PreparedStatement psVerif = koneksi.prepareStatement(
                        "insert into verifikasi_cppt_dpjp (no_rawat, tgl_perawatan, jam_rawat, jenis_rawat, kd_dokter, nm_dokter, tgl_verifikasi) " +
                        "values (?, ?, ?, ?, ?, ?, now())");
                    psVerif.setString(1, noRawat);
                    psVerif.setString(2, tglPerawatan);
                    psVerif.setString(3, jamRawat);
                    psVerif.setString(4, jenisRawat);
                    psVerif.setString(5, nipPetugas);
                    psVerif.setString(6, namaPetugas);
                    psVerif.executeUpdate();
                    psVerif.close();

                    JOptionPane.showMessageDialog(null, "Verifikasi berhasil disimpan!");

                    // Reload tampilan
                    tampilSOAPI();

                } catch (Exception ex) {
                    System.out.println("Error simpan verifikasi: " + ex);
                    JOptionPane.showMessageDialog(null, "Gagal menyimpan verifikasi: " + ex.getMessage());
                }
                return; // Keluar dari method
            } else {
                // Bukan petugas yang bersangkutan, tampilkan notifikasi
                JOptionPane.showMessageDialog(null, "Hanya bisa di verifikasi oleh dokter/petugas yang bersangkutan..!!");
                return; // Keluar dari method
            }

        } catch (Exception e) {
            System.out.println("Error cek petugas: " + e);
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat mengecek petugas!");
            return;
        }
    }
}