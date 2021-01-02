package weatherGenerator;

import com.toedter.calendar.JYearChooser;
import main.MainPanel;
import tools.FileMaker;
import tools.YearChooser;
import tools.Settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;

public class Generate {
    private JPanel mainPanel;
    private JPanel inputStartYear;
    private JPanel inputEndYear;
    private JPanel outputYear;
    private JButton generateInput;
    private JButton generateOutput;
    private JRadioButton firefoxBackground;
    private JRadioButton firefoxOutScreen;
    private JRadioButton visibleFirefox;
    private JButton saveSettings;
    private JYearChooser inputStartChooser;
    private JYearChooser inputEndChooser;
    private JYearChooser outputYearChooser;
    private final String[] yesNoOption = {"Evet", "Hayır"};


    //Seçilen özelliklere göre input veya outputu firefox üzerinden oluşturan fonksiyon
    public void generator(String type){
        int confirmed =0;
        // Seçilen input yılının doğru olup olmadığının kontrolü
        if(inputStartChooser.getYear()>=inputEndChooser.getYear())
            JOptionPane.showMessageDialog(null, "İnput başlangıç yılı bitiş yılından küçük olmalıdır.", "Uyarı", JOptionPane.INFORMATION_MESSAGE);
        else{
            // Eğer klasör zaten varsa
            if(!FileMaker.generateFolders(type)){
                //Eskisini silmek istediğini sor
                confirmed = JOptionPane.showOptionDialog(null,
                        "Eski "+type+" bulundu silmek istiyor musunuz?", type,
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, yesNoOption, null);
                if(confirmed ==0){
                    //Eski verileri sil ve yeni boş kasör oluştur
                    FileMaker.deleteDirectory(type);
                    FileMaker.generateFolders(type);
                }
            }
            if(confirmed==0){
                Database.generateDatabase(type, firefoxOutScreen.isSelected(), firefoxBackground.isSelected(), inputStartChooser.getYear(), inputEndChooser.getYear(), outputYearChooser.getYear());
            }
        }
    }
    // Alanların başlangıç değerlerini oluştur ve button listenerları ata
    public Generate() {
        initFields();
        generateInput.addActionListener(e -> generator("input"));
        generateOutput.addActionListener(e -> generator("output"));
        saveSettings.addActionListener(e -> {
            Settings x =new Settings();
            int ffMode = 0;
            if(firefoxOutScreen.isSelected())
                ffMode = 1;
            else if(visibleFirefox.isSelected())
                ffMode = 2;
            x.setDataGenerate(inputStartChooser.getYear(), inputEndChooser.getYear(),outputYearChooser.getYear(),ffMode);
        });
    }
    // Frame oluştur
    public static void init() {
        JFrame mainFrame = new JFrame("Veritabanı Oluştur");
        mainFrame.setContentPane(new Generate().mainPanel);
        mainFrame.setPreferredSize(new Dimension(420, 230));
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ImageIcon img = new ImageIcon("res/ytulogo.png");
        mainFrame.setIconImage(img.getImage());
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
        // Pencere kapanınca ana panele dön
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                MainPanel.init();
            }
        });
    }
    public void initFields(){
        new Settings();
        int [] settings = Settings.getGenerateSettings();
        // Default yılları ata
        inputStartChooser = YearChooser.generateYearChooser(settings[0], 1960, Calendar.getInstance().get(Calendar.YEAR));
        inputStartYear.add(inputStartChooser);
        inputEndChooser = YearChooser.generateYearChooser(settings[1], 1960, Calendar.getInstance().get(Calendar.YEAR));
        inputEndYear.add(inputEndChooser);
        outputYearChooser = YearChooser.generateYearChooser(settings[2], 1960, Calendar.getInstance().get(Calendar.YEAR));
        outputYear.add(outputYearChooser);
        if(settings[3]==0)
            firefoxBackground.setSelected(true);
        else if(settings[3]==1)
            firefoxOutScreen.setSelected(true);
        else
            visibleFirefox.setSelected(true);
    }


}
