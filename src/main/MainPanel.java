package main;

import tools.GradientPanel;
import weatherCalculator.CalculateSuccess;
import weatherCalculator.Weather;
import weatherGenerator.Firefox;
import weatherGenerator.Generate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainPanel {
    private static JFrame mainFrame;
    private JPanel mainPanel;
    private JButton predicateButton;
    private JButton generateButton;
    private JButton calculateSuccess;

    // Giriş paneli
    public MainPanel() {
        // Diğer panelleri açan ve bu paneli kapatan listenerlar
        predicateButton.addActionListener(e -> {
            Weather.init();
            mainFrame.dispose();
        });
        generateButton.addActionListener(e -> {
            Generate.init();
            mainFrame.dispose();
        });
        calculateSuccess.addActionListener(e -> {
            CalculateSuccess.init();
            mainFrame.dispose();
        });
    }

    // Programı çalıştıran fonksiyon
    public static void main(String[] args) {
        theming("com.formdev.flatlaf.FlatIntelliJLaf");
        MainPanel.init();
    }

    // Arayüz için kullandığım temayı aktifleştirdiğim fonksiyon
    public static void theming(String theme) {
        try {
            UIManager.setLookAndFeel(theme);
        } catch (IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException | ClassNotFoundException e) {
            System.out.println("Theme Error\n");
            e.printStackTrace();
        }
    }

    // Ana ekranı başlatan fonksiyon
    public static void init() {
        mainFrame = new JFrame("Hava Tahmin Programı");
        mainFrame.setContentPane(new MainPanel().mainPanel);
        mainFrame.setPreferredSize(new Dimension(420, 220));
        Toolkit it = Toolkit.getDefaultToolkit();
        Dimension d = it.getScreenSize();
        int w = mainFrame.getWidth(), h = mainFrame.getHeight();
        mainFrame.setLocation(d.width / 2 - w / 2, d.height / 2 - h / 2);
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ImageIcon img = new ImageIcon("res/ytulogo.png");
        mainFrame.setIconImage(img.getImage());
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
        // Program kapatıldığında firefoxu da kapatan fonksiyon
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                Firefox.destroyFirefox();
            }
        });
    }

    // Gradiyent arka plan oluşturduğum kısım
    private void createUIComponents() {
        mainPanel = new GradientPanel(new Color(204, 22, 73), new Color(244, 141, 46));
    }
}
