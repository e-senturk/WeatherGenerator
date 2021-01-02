package tools;

import com.toedter.calendar.JYearChooser;
import weatherCalculator.Weather;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.prefs.Preferences;

// Ayarları dinamik olarak sistem üzerinde kaydeden class
public class Settings {
    private static final String ID1 = "ratio";
    private static final String ID2 = "missingData";
    private static final String ID3 = "step";
    private static final String ID4 = "inputStartYear";
    private static final String ID5 = "inputEndYear";
    private static final String ID6 = "outputYear";
    private static final String ID7 = "firefoxMode";
    private static int ratio;
    private static int missingData;
    private static int step;
    private static int inputStartYear;
    private static int inputEndYear;
    private static int outputYear;
    private static int firefoxMode;
    private JButton saveButton;
    private JPanel inputStartYearPanel;
    private JPanel mainPanel;
    private JPanel inputEndYearPanel;
    private JPanel outputYearPanel;
    private JPanel ratioPanel;
    private JPanel missingDataPanel;
    private JPanel stepPanel;
    private JYearChooser ratioChooser;
    private JYearChooser missingDataChooser;
    private JYearChooser stepChooser;
    private JYearChooser inputStartChooser;
    private JYearChooser inputEndChooser;
    private JYearChooser outputYearChooser;

    // Frame yapısını oluşturur.
    public static void init() {
        JFrame mainFrame = new JFrame("Ayarlar");
        mainFrame.setContentPane(new Settings().mainPanel);
        mainFrame.setPreferredSize(new Dimension(500, 300));
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ImageIcon img = new ImageIcon("res/ytulogo.png");
        mainFrame.setIconImage(img.getImage());
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                Weather.init();
            }
        });
    }
    // Eski değerleri okur
    public Settings(){
        initFields();
        saveButton.addActionListener(e -> setDataPredicate(ratioChooser.getYear(),missingDataChooser.getYear(),stepChooser.getYear(), inputStartChooser.getYear(),
                inputEndChooser.getYear(),outputYearChooser.getYear()));
    }
    // Ayarları sisteme kaydeder.
    public void setDataPredicate(int ratio, int missingData, int step, int inputStartYear, int inputEndYear, int outputYear) {
        Preferences prefs = Preferences.userRoot().node(this.getClass().getName());
        prefs.putInt(ID1, ratio);
        prefs.putInt(ID2, missingData);
        prefs.putInt(ID3, step);
        prefs.putInt(ID4, inputStartYear);
        prefs.putInt(ID5, inputEndYear);
        prefs.putInt(ID6, outputYear);
    }
    // Ayarları sisteme kaydeder.
    public void setDataGenerate(int inputStartYear,int inputEndYear,int outputYear,int ffMode) {
        Preferences prefs = Preferences.userRoot().node(this.getClass().getName());
        prefs.putInt(ID4, inputStartYear);
        prefs.putInt(ID5, inputEndYear);
        prefs.putInt(ID6, outputYear);
        prefs.putInt(ID7, ffMode);
    }
    // Ayarları sistemden okur
    private void getData() {
        Preferences prefs = Preferences.userRoot().node(this.getClass().getName());
        ratio = prefs.getInt(ID1, 10);
        missingData = prefs.getInt(ID2, 10);
        step = prefs.getInt(ID3, 3);
        inputStartYear = prefs.getInt(ID4, 2010);
        inputEndYear = prefs.getInt(ID5, 2019);
        outputYear = prefs.getInt(ID6, 2020);
        firefoxMode = prefs.getInt(ID7,2);
    }
    // Verilen alanların arayayüz bağlantılarının yapıldığı fonksiyon
    private void initFields() {
        getData();
        ratioChooser = YearChooser.generateYearChooser(ratio, 0, 100);
        ratioPanel.add(ratioChooser);
        missingDataChooser = YearChooser.generateYearChooser(missingData, 0, 100);
        missingDataPanel.add(missingDataChooser);
        stepChooser = YearChooser.generateYearChooser(step, 0, 100);
        stepPanel.add(stepChooser);
        inputStartChooser = YearChooser.generateYearChooser(inputStartYear, 1960, Calendar.getInstance().get(Calendar.YEAR));
        inputStartYearPanel.add(inputStartChooser);
        inputEndChooser = YearChooser.generateYearChooser(inputEndYear, 1960, Calendar.getInstance().get(Calendar.YEAR));
        inputEndYearPanel.add(inputEndChooser);
        outputYearChooser = YearChooser.generateYearChooser(outputYear, 1960, Calendar.getInstance().get(Calendar.YEAR));
        outputYearPanel.add(outputYearChooser);
    }

    public static int[] getPredicateSettings(){
        return new int[]{ratio,missingData,step,inputStartYear,inputEndYear,outputYear};
    }
    public static int[] getGenerateSettings(){
        return new int[]{inputStartYear,inputEndYear,outputYear,firefoxMode};
    }
}