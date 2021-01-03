package weatherCalculator;

import com.toedter.calendar.JYearChooser;
import main.MainPanel;
import tools.Settings;
import tools.YearChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Vector;

public class CalculateSuccess {
    private static boolean isWorking = true;
    private JButton calculate;
    private JPanel mainPanel;
    private JButton stop;
    private JTable resultsTable;
    private JLabel infoLabel;
    private JPanel tempPanel;
    private JPanel humPanel;
    private JPanel vizPanel;
    private JPanel windPanel;
    private JYearChooser tempChooser;
    private JYearChooser humChooser;
    private JYearChooser vizChooser;
    private JYearChooser windChooser;

    public CalculateSuccess() {
        initFields();
        calculate.addActionListener(e -> {
            isWorking = true;
            swingWorker();
        });
        stop.addActionListener(e -> isWorking = false);
    }

    // Tablo framei oluşturan fonksiyon
    public static void init() {
        JFrame mainFrame = new JFrame("Başarı Hesabı");
        mainFrame.setContentPane(new CalculateSuccess().mainPanel);
        mainFrame.setPreferredSize(new Dimension(600, 350));
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ImageIcon img = new ImageIcon("res/ytulogo.png");
        mainFrame.setIconImage(img.getImage());
        mainFrame.pack();
        mainFrame.setVisible(true);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                isWorking = false;
                MainPanel.init();
            }
        });
    }

    // İşlem yapılırken arayüz güncelleneceğinden swing worker kullandım
    private void swingWorker() {
        SwingWorker<String, Object> sw1 = new SwingWorker<String, Object>() {
            @Override
            protected String doInBackground() {
                // başarı değerleri için başlangıç dizisi oluşturulduç
                int[] success = new int[]{0, 0, 0, 0, 0, 0, 0};
                Object[] cits = Weather.getCityNames();
                // kaç günlük deneme yapıldığı seçildi.
                int iteration = 0;
                // Kayıtlı ayarlardan output Yılı okundu.
                new Settings();
                int outputYear = Settings.getPredicateSettings()[5];
                // Tüm şehirler için output yılındaki tüm günler loopa alındı.
                for (Object city : cits) {
                    infoLabel.setText(city.toString() + " için tüm günler hesaplanıyor. İşlem adımı: " + iteration);
                    // tablo şu ana kadar toplanan değerlere göre yazdırıldı.
                    initializeTable(resultsTable, iteration, success);
                    for (int i = 1; i <= 12; i++) {
                        int max = 31;
                        if (i == 4 || i == 6 || i == 9 || i == 11)
                            max = 30;
                        else if (i == 2) {
                            // Output yılına göre şubat ayı ayarlandı.
                            if (outputYear % 4 == 0)
                                max = 29;
                            else
                                max = 28;
                        }
                        for (int j = 1; j <= max; j++) {
                            // İşlemi durdurma kontrolü durdurma buttonu için
                            if (!isWorking) return "Interrupt";
                            // O gün ve şehir için tahmin yapıldı. Tahmin sonucuna göre başarı dizisi değiştirildi.
                            DayWeather[] answer = Weather.calculateWeather(i, j, city.toString());
                            boolean[] results = DayWeather.isSuccessful(answer[0], answer[1], tempChooser.getYear(), humChooser.getYear(), vizChooser.getYear(), windChooser.getYear());
                            iteration++;
                            DayWeather.addValue(success, results);
                        }
                    }
                }
                infoLabel.setText("Hesaplama" + iteration + "adımda tamamlandı.");
                initializeTable(resultsTable, iteration, success);
                System.out.println(iteration);
                return "Success";
            }
        };

        // executes the swingworker on worker thread
        sw1.execute();
    }

    //verilen tabloya başarı değerlerini yazdıran fonksiyon
    public void initializeTable(JTable table, int trial, int[] success) {
        DefaultTableModel tableModel;
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };
        table.setModel(tableModel);
        Vector<Object> header = new Vector<>(Arrays.asList("Değerler", "Başarı", "Oran"));
        Vector<Vector<Object>> values = new Vector<>();
        String[] starts = {"Ortalama Sıcaklık", "Maksimum Sıcaklık", "Minimum Sıcaklık", "Nem Değeri", "Görüş", "Ortalama Rüzgar", "Maksimum Rüzgar"};
        for (int i = 0; i < 7; i++) {
            Vector<Object> temp = new Vector<>();
            temp.add(starts[i]);
            temp.add(String.valueOf(success[i]));
            if (trial == 0)
                temp.add(0);
            else
                temp.add(String.valueOf(String.format("%.2g%n", (double) success[i] / trial)));
            values.add(temp);
        }
        tableModel.setDataVector(values, header);
    }

    // arayüzü oluşturur.
    public void initFields() {
        tempChooser = YearChooser.generateYearChooser(2, 0, 10);
        tempPanel.add(tempChooser);
        humChooser = YearChooser.generateYearChooser(5, 0, 20);
        humPanel.add(humChooser);
        vizChooser = YearChooser.generateYearChooser(5, 0, 20);
        vizPanel.add(vizChooser);
        windChooser = YearChooser.generateYearChooser(5, 0, 20);
        windPanel.add(windChooser);
    }
}
