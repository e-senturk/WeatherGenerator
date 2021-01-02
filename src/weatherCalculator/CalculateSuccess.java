package weatherCalculator;

import com.toedter.calendar.JYearChooser;
import main.MainPanel;
import tools.YearChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Vector;

public class CalculateSuccess {
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
    private static boolean isWorking=true;

    public CalculateSuccess() {
        initFields();
        calculate.addActionListener(e -> {isWorking=true; startThread();});
        stop.addActionListener(e -> isWorking= false);
    }

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

    private void startThread() {
        SwingWorker<String, Object> sw1 = new SwingWorker<String, Object>() {
            @Override
            protected String doInBackground() {
                int[] success = new int[]{0, 0, 0, 0, 0, 0, 0};
                Object[] cits = Weather.getCityNames();
                int iteration = 0;
                for (Object city : cits) {
                    infoLabel.setText(city.toString() + " için tüm günler hesaplanıyor. İşlem adımı: " +iteration);
                    initializeTable(resultsTable,iteration,success);
                    for (int i = 1; i <= 12; i++) {
                        int max = 31;
                        if (i == 4 || i == 6 || i == 9 || i == 11)
                            max = 30;
                        else if (i == 2)
                            max = 29;
                        for (int j = 1; j <= max; j++) {
                            if (!isWorking) return "Interrupted";
                            DayWeather[] answer = Weather.calculateWeather(i, j, city.toString());
                            boolean[] results = DayWeather.isSuccessful(answer[0], answer[1], tempChooser.getYear(), humChooser.getYear(), vizChooser.getYear(), windChooser.getYear());
                            iteration++;
                            DayWeather.addValue(success, results);
                        }
                    }
                }
                System.out.println(iteration);
                return "Finished Execution";

            }
        };

        // executes the swingworker on worker thread
        sw1.execute();
    }
    public void initializeTable(JTable table, int trial,int[]success) {
        DefaultTableModel tableModel;
        tableModel = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int i, int i1) {
                    return false;
                }
            };
        table.setModel(tableModel);
        Vector<Object> header = new Vector<>(Arrays.asList("Değerler", "Başarı", "Oran"));
        Vector<Vector<Object>>values = new Vector<>();
        String[] starts = {"Ortalama Sıcaklık","Maksimum Sıcaklık","Minimum Sıcaklık","Nem Değeri","Görüş","Ortalama Rüzgar","Maksimum Rüzgar"};
        for(int i=0;i<7;i++){
            Vector<Object> temp = new Vector<>();
            temp.add(starts[i]);
            temp.add(String.valueOf(success[i]));
            if(trial==0)
                temp.add(0);
            else
                temp.add(String.valueOf(String.format("%.2g%n", (double) success[i]/trial)));
            values.add(temp);
        }
        tableModel.setDataVector(values,header);
    }
    public void initFields(){
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
