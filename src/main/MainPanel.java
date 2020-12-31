package main;

import weatherCalculator.Weather;
import weatherGenerator.Firefox;
import weatherGenerator.Generate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainPanel {
    private JPanel mainPanel;
    private JButton prediacateButton;
    private JButton generateButton;
    private static JFrame mainFrame;

    public MainPanel() {
        prediacateButton.addActionListener(e -> {
            Weather.init();
            mainFrame.dispose();
        });
        generateButton.addActionListener(e -> {
            Generate.init();
            mainFrame.dispose();
        });
    }

    public static void main(String[] args) {
        theming("com.formdev.flatlaf.FlatIntelliJLaf");
        MainPanel.init();
    }
    public static void theming(String theme) {
        try {
            UIManager.setLookAndFeel(theme);
        } catch (IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException | ClassNotFoundException e) {
            System.out.println("Theme Error\n");
            e.printStackTrace();
        }

    }
    public static void init() {
        mainFrame = new JFrame("Hava Tahmin Programı");
        mainFrame.setContentPane(new MainPanel().mainPanel);
        mainFrame.setPreferredSize(new Dimension(420, 200));
        mainFrame.setLocation(300, 100);
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ImageIcon img = new ImageIcon("res/ytulogo.png");
        mainFrame.setIconImage(img.getImage());
        mainFrame.pack();
        mainFrame.setVisible(true);
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                Firefox.destroyFirefox();
            }
        });
    }
}
