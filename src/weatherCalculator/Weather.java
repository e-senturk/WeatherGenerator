package weatherCalculator;
import main.MainPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Objects;

public class Weather {
    private static JFrame mainFrame;
    private JPanel mainPanel;
    private JComboBox<Object> cities;
    private JComboBox<Object> day;
    private JComboBox<Object> month;
    private JButton calculateButton;
    private JButton settingsButton;
    private JLabel tempLabel1;
    private JLabel mxTempLabel1;
    private JLabel mnTempLabel1;
    private JLabel humLabel1;
    private JLabel visibleLabel1;
    private JLabel avgWindLabel1;
    private JLabel maxWindLabel1;
    private JLabel humLabel2;
    private JLabel tempLabel2;
    private JLabel mxTempLabel2;
    private JLabel mnTempLabel2;
    private JLabel visibleLabel2;
    private JLabel avgWindLabel2;
    private JLabel maxWindLabel2;
    JPanel expectedPanel;
    JPanel resultPanel;

    private final JLabel []area1= new JLabel[]{tempLabel1,mxTempLabel1,mnTempLabel1,humLabel1,visibleLabel1,avgWindLabel1,maxWindLabel1};
    private final JLabel []area2= new JLabel[]{tempLabel2,mxTempLabel2,mnTempLabel2,humLabel2,visibleLabel2,avgWindLabel2,maxWindLabel2};
    public static final String [] months= {"Ocak","Şubat","Mart","Nisan","Mayıs","Haziran","Temmuz","Ağustos","Eylül","Ekim","Kasım","Aralık"};
    public static void init() {
        mainFrame = new JFrame("Hava Durumu Tahmini");
        mainFrame.setContentPane(new Weather().mainPanel);
        mainFrame.setPreferredSize(new Dimension(420, 450));
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
    public Weather(){
        initCities();
        initMonth();
        initDay(31,true);
        month.addActionListener(e -> {
            int i = month.getSelectedIndex();
            if(i==1)
                initDay(29,false);
            else if(i==3||i==5||i==8||i==10)
                initDay(30,false);
            else
                initDay(31,false);
        });
        calculateButton.addActionListener(e -> calculateWeather());
        settingsButton.addActionListener(e -> {
            Settings.init();
            mainFrame.dispose();
        });
    }
    private void calculateWeather(){
        int m = month.getSelectedIndex()+1;
        int d = day.getSelectedIndex()+1;
        HashMap<Integer,DayWeather> hashInput,hashOutput;
        try{
            hashInput = Calc.readData(Objects.requireNonNull(cities.getSelectedItem()).toString(),"input");
            hashOutput = Calc.readData(Objects.requireNonNull(cities.getSelectedItem()).toString(),"output");
        }
        catch(NullPointerException ex){
            hashInput = null;
            hashOutput = null;
            System.out.println("Şehir bulunamadı");
        }
        ////////////////////////////////
        new Settings();
        int[] settings = Settings.getSettings();
        int ratio = settings[0];
        int missingData = settings[1];
        int step = settings[2];
        int inputStartYear = settings[3];
        int inputEndYear = settings[4];
        int outputYear = settings[5];
        ///////////////////////////////
        DayWeather result = new DayWeather();
        int [] coefficient= new int[]{0,0,0,0,0,0,0};
        for(int i=inputEndYear,j=100;i>=inputStartYear;i--,j-=ratio){
            DayWeather weatherYear = Calc.generateYearRatio(Calc.convertDate(d,m,i),hashInput,step,missingData);
            if(weatherYear!=null){
                result.addMul(weatherYear,j,coefficient);
            }
        }
        assert hashOutput != null;
        DayWeather expected = hashOutput.get(Calc.convertDate(d,m,outputYear));
        result.div(coefficient);
        System.out.println(expected);
        initWeather(result,area1);
        initWeather(expected,area2);
    }
    private void initCities(){
        try{
            File folder = new File("input");
            File[] listOfFiles = folder.listFiles();
            Object[] fileNames = new Object[Objects.requireNonNull(listOfFiles).length];
            for (int i = 0; i < Objects.requireNonNull(listOfFiles).length; i++) {
                if (listOfFiles[i].isFile())
                    fileNames[i] = listOfFiles[i].getName().replace(".txt","");
            }
            DefaultComboBoxModel<Object> model = new DefaultComboBoxModel<>(fileNames);
            cities.setModel(model);
        }
        catch(NullPointerException e){
            System.out.println("No city found");
        }
    }
    private void initMonth(){
        DefaultComboBoxModel<Object> model = new DefaultComboBoxModel<>(months);
        month.setModel(model);
    }
    private void initDay(int dayCount,boolean start){
        int index=0;
        if(!start){
            index = day.getSelectedIndex();
        }
        Object [] days = new Object[dayCount];
        for(int i=0;i<dayCount;i++){
            days[i]=i+1;
        }
        DefaultComboBoxModel<Object> model = new DefaultComboBoxModel<>(days);
        day.setModel(model);
        if(!start){
            if(index<dayCount)
                day.setSelectedIndex(index);
            else
                day.setSelectedIndex(dayCount-1);
        }
    }
    private void createUIComponents() {
        Color purple = new Color(129,15,99);
        Color orange = new Color(252,80,57);
        Color midnight = new Color(17,33,88);
        Color purpleNight = new Color(150,98,162);
        resultPanel = new GradientPanel(purple,orange);
        expectedPanel = new GradientPanel(midnight,purpleNight);
    }
    private void initWeather(DayWeather day,JLabel[] weather){
        if(day == null){
            weather[0].setText("--°C");
            weather[1].setText("--°C");
            weather[2].setText("--°C");
            weather[3].setText("<html><div style='text-align: center;'>Nem<br/>%--</html>");
            weather[4].setText("<html><div style='text-align: center;'>Görüş<br/>-- km</html>");
            weather[5].setText("<html><div style='text-align: center;'>Rüzgar(ort)<br/>-- km</html>");
            weather[6].setText("<html><div style='text-align: center;'>Rüzgar(max)<br/>-- km</html>");
        }
        else{
            weather[0].setText((int)day.getAverageTemp()+"°C");
            weather[1].setText((int)day.getMaxTemp()+"°C");
            weather[2].setText((int)day.getMinTemp()+"°C");
            weather[3].setText("<html><div style='text-align: center;'>Nem<br/>%"+round(day.getHumidity(),1)+"</html>");
            weather[4].setText("<html><div style='text-align: center;'>Görüş<br/>"+round(day.getVisibility(),1)+" km</html>");
            weather[5].setText("<html><div style='text-align: center;'>Rüzgar(ort)<br/>"+round(day.getAverageWind(),1)+" km</html>");
            weather[6].setText("<html><div style='text-align: center;'>Rüzgar(max)<br/>"+round(day.getMaxWind(),1)+" km</html>");
        }

    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
