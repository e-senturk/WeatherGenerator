package weatherGenerator;

import com.toedter.calendar.JYearChooser;
import main.MainPanel;
import tools.FileMaker;
import tools.Settings;
import tools.YearChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;

public class Generate {
    private final String[] yesNoOption = {"Evet", "Hayır"};
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
    private JLabel inputInfo;
    private JLabel outputInfo;
    private JYearChooser inputStartChooser;
    private JYearChooser inputEndChooser;
    private JYearChooser outputYearChooser;


    // Alanların başlangıç değerlerini oluştur ve button listenerları ata
    public Generate() {
        initFields();
        generateInput.addActionListener(e -> swingWorker("input"));
        generateOutput.addActionListener(e -> swingWorker("output"));
        saveSettings.addActionListener(e -> {
            Settings x = new Settings();
            int ffMode = 0;
            if (firefoxOutScreen.isSelected())
                ffMode = 1;
            else if (visibleFirefox.isSelected())
                ffMode = 2;
            x.setDataGenerate(inputStartChooser.getYear(), inputEndChooser.getYear(), outputYearChooser.getYear(), ffMode);
        });
    }

    // Frame oluştur
    public static void init() {
        JFrame mainFrame = new JFrame("Veritabanı Oluştur");
        mainFrame.setContentPane(new Generate().mainPanel);
        mainFrame.setPreferredSize(new Dimension(460, 260));
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
                Database.setIsWorking(false);
            }
        });
    }
    private void swingWorker(String type) {
        SwingWorker<String, Object> sw1 = new SwingWorker<String, Object>() {
            @Override
            protected String doInBackground() {
                int confirmed = 0;
                // Seçilen input yılının doğru olup olmadığının kontrolü
                if (inputStartChooser.getYear() >= inputEndChooser.getYear())
                    JOptionPane.showMessageDialog(null, "İnput başlangıç yılı bitiş yılından küçük olmalıdır.", "Uyarı", JOptionPane.INFORMATION_MESSAGE);
                else {
                    // Eğer klasör zaten varsa
                    if (!FileMaker.generateFolders(type)) {
                        //Eskisini silmek istediğini sor
                        confirmed = JOptionPane.showOptionDialog(null,
                                "Eski " + type + " bulundu silmek istiyor musunuz?", type,
                                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, yesNoOption, null);
                        if (confirmed == 0) {
                            //Eski verileri sil ve yeni boş kasör oluştur
                            FileMaker.deleteDirectory(type);
                            FileMaker.generateFolders(type);
                        }
                    }
                    if (confirmed == 0) {
                        Database.generateDatabase(type, firefoxOutScreen.isSelected(), firefoxBackground.isSelected(), inputStartChooser.getYear(), inputEndChooser.getYear(), outputYearChooser.getYear(), inputInfo, outputInfo);
                    }
                }
                return "Success";
            }
        };

        // executes the swingworker on worker thread
        sw1.execute();
    }

    public void initFields() {
        new Settings();
        int[] settings = Settings.getGenerateSettings();
        // Default yılları ata
        inputStartChooser = YearChooser.generateYearChooser(settings[0], 1960, Calendar.getInstance().get(Calendar.YEAR));
        inputStartYear.add(inputStartChooser);
        inputEndChooser = YearChooser.generateYearChooser(settings[1], 1960, Calendar.getInstance().get(Calendar.YEAR));
        inputEndYear.add(inputEndChooser);
        outputYearChooser = YearChooser.generateYearChooser(settings[2], 1960, Calendar.getInstance().get(Calendar.YEAR));
        outputYear.add(outputYearChooser);
        if (settings[3] == 0)
            firefoxBackground.setSelected(true);
        else if (settings[3] == 1)
            firefoxOutScreen.setSelected(true);
        else
            visibleFirefox.setSelected(true);
    }


}
