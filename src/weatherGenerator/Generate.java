package weatherGenerator;

import com.toedter.calendar.JYearChooser;
import main.MainPanel;
import main.YearChooser;

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
    private final JYearChooser inputStartChooser;
    private final JYearChooser inputEndChooser;
    private final JYearChooser outputYearChooser;
    private final String[] yesNoOption = {"Evet", "Hayır"};

    public Generate() {
        visibleFirefox.setSelected(true);
        inputStartChooser = YearChooser.generateYearChooser(2010, 1960, Calendar.getInstance().get(Calendar.YEAR));
        inputStartYear.add(inputStartChooser);
        inputEndChooser = YearChooser.generateYearChooser(2019, 1960, Calendar.getInstance().get(Calendar.YEAR));
        inputEndYear.add(inputEndChooser);
        outputYearChooser = YearChooser.generateYearChooser(2020, 1960, Calendar.getInstance().get(Calendar.YEAR));
        outputYear.add(outputYearChooser);
        generateInput.addActionListener(e -> generator("input"));
        generateOutput.addActionListener(e -> generator("output"));

    }

    public void generator(String type){
        int confirmed =0;
        if(inputStartChooser.getYear()>=inputEndChooser.getYear())
            JOptionPane.showMessageDialog(null, "İnput başlangıç yılı bitiş yılından küçük olmalıdır.", "Uyarı", JOptionPane.INFORMATION_MESSAGE);

        else{
            if(!FileMaker.generateFolders(type)){
                confirmed = JOptionPane.showOptionDialog(null,
                        "Eski "+type+" bulundu silmek istiyor musunuz?", type,
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, yesNoOption, null);
                if(confirmed ==0){
                    FileMaker.deleteDirectory(type);
                    FileMaker.generateFolders(type);
                }
            }
            if(confirmed==0){
                Database.generateDatabase(type, firefoxOutScreen.isSelected(), firefoxBackground.isSelected(), inputStartChooser.getYear(), inputEndChooser.getYear(), outputYearChooser.getYear());
            }
        }
    }

    public static void init() {
        JFrame mainFrame = new JFrame("Veritabanı Oluştur");
        mainFrame.setContentPane(new Generate().mainPanel);
        mainFrame.setPreferredSize(new Dimension(420, 230));
        mainFrame.setLocation(300, 100);
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ImageIcon img = new ImageIcon("res/ytulogo.png");
        mainFrame.setIconImage(img.getImage());
        mainFrame.pack();
        mainFrame.setVisible(true);
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                MainPanel.init();
            }
        });
    }


}
