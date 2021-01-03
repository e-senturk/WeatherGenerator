package weatherCalculator;
import main.MainPanel;
import tools.GradientPanel;
import tools.Settings;

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
    // Hava durumu yazdırılan UI bölümlerini saklayan dizi
    private final JLabel []area1= new JLabel[]{tempLabel1,mxTempLabel1,mnTempLabel1,humLabel1,visibleLabel1,avgWindLabel1,maxWindLabel1};
    private final JLabel []area2= new JLabel[]{tempLabel2,mxTempLabel2,mnTempLabel2,humLabel2,visibleLabel2,avgWindLabel2,maxWindLabel2};
    // Ayların türkçe hallerini saklayan dizi
    public static final String [] months= {"Ocak","Şubat","Mart","Nisan","Mayıs","Haziran","Temmuz","Ağustos","Eylül","Ekim","Kasım","Aralık"};
    // Frame yapısını oluşturur
    public static void init() {
        mainFrame = new JFrame("Hava Durumu Tahmini");
        mainFrame.setContentPane(new Weather().mainPanel);
        mainFrame.setPreferredSize(new Dimension(420, 450));
        mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ImageIcon img = new ImageIcon("res/ytulogo.png");
        mainFrame.setIconImage(img.getImage());
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
        // Çıkış yapılınca ana ekrana döner
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent ev) {
                MainPanel.init();
            }
        });
    }

    public Weather(){
        // Ayarları yükler
        // Şehir değerlerini input klasöründen okur.
        initCities();
        // Gerekli alanları doldurur.
        initMonth();
        // Gün alanını doldurur
        initDay(31,true);
        // Ay değiştirildiğinde gün değerini otomatik olarak ayın gün sayısına ayarlar.
        month.addActionListener(e -> {
            new Settings();
            int outputYear = Settings.getPredicateSettings()[5];
            int i = month.getSelectedIndex();
            if(i==1){
                if(outputYear%4==0)
                    initDay(29,false);
                else
                    initDay(28,false);
            }
            else if(i==3||i==5||i==8||i==10)
                initDay(30,false);
            else
                initDay(31,false);
        });
        // Hesaplama işlemini başlatır.
        calculateButton.addActionListener(e -> {
            DayWeather[] answer =calculateWeather(month.getSelectedIndex()+1,day.getSelectedIndex()+1,Objects.requireNonNull(cities.getSelectedItem()).toString());
            // Hesaplanan sonuçlar 1. alana Gerçek sonuçlar 2. alana yazdırılır.
            initWeather(answer[0],area1);
            initWeather(answer[1],area2);
        });
        // Ayarları açar ve bu frame'i kapatır.
        settingsButton.addActionListener(e -> {
            Settings.init();
            mainFrame.dispose();
        });
    }
    // Havadurumu hesabı yapan Fonksiyon
    public static DayWeather[] calculateWeather(int m,int d,String city){
        // Seçilen şehre göre input ve output için hash tablosu oluşturur.
        HashMap<Integer,DayWeather> hashInput,hashOutput;
        try{
            hashInput = Predictor.readData(city,"input");
            hashOutput = Predictor.readData(city,"output");
        }
        catch(NullPointerException ex){
            //      Eğer dosyalar eksikse hash tablolarını null değerine atar.
            hashInput = null;
            hashOutput = null;
            System.out.println("Şehir bulunamadı");
        }
        //Ayarları settings classından alır.
        new Settings();
        int[] settings = Settings.getPredicateSettings();
        int ratio = settings[0];
        int missingData = settings[1];
        int step = settings[2];
        int inputStartYear = settings[3];
        int inputEndYear = settings[4];
        int outputYear = settings[5];
        // Sonuçları hesaplamak için boş bir havadurumu nesnesi oluşturur.
        DayWeather result = new DayWeather();
        // Katsayılar matrisi 0 olacak şekilde oluşturulur.
        int [] coefficient= new int[]{0,0,0,0,0,0,0};
        // Tüm yıllar için predictor metodu ile hesaplama yapılır.
        // Sonuç null değilse oran miktarınca her yıl giderek az oranda etkileyecek şekilde sonuçlar nesnesine eklenir.
        for(int i=inputEndYear,j=100;i>=inputStartYear;i--,j-=ratio){
            DayWeather weatherYear = Predictor.generateYearRatio(Predictor.convertDate(d,m,i),hashInput,step,missingData);
            if(weatherYear!=null){
                result.addMul(weatherYear,j,coefficient);
            }
        }
        // Hesaplanan sonuç coefficent dizisine bölünerek hesaplama tamamlanır.
        result.div(coefficient);
        // Sonuç hava durumu output verilerinden okunur. Eğer output verisi yoksa null döndürülür.
        DayWeather expected;
        if(hashInput == null)
            expected =null;
        else
            expected= hashOutput.get(Predictor.convertDate(d,m,outputYear));
        return new DayWeather[]{result,expected};
    }
    public static Object[] getCityNames(){
        Object[] fileNames= null;
        try{
            File folder = new File("input");
            File[] listOfFiles = folder.listFiles();
            fileNames = new Object[Objects.requireNonNull(listOfFiles).length];
            for (int i = 0; i < Objects.requireNonNull(listOfFiles).length; i++) {
                if (listOfFiles[i].isFile())
                    fileNames[i] = listOfFiles[i].getName().replace(".txt","");
            }
        }
        catch(NullPointerException e){
            System.out.println("No city found");
        }
        return fileNames;
    }

    // Şehir comboboxunu input klasöründeki dosyaların adlarına göre ayarlayan fonksiyon.
    private void initCities(){
        try{
            Object[] fileNames = getCityNames();
            DefaultComboBoxModel<Object> model = new DefaultComboBoxModel<>(fileNames);
            cities.setModel(model);
        }
        catch(NullPointerException e){
            System.out.println("No city found");
        }
    }
    // Ayları türkçe olarak belirtiğimiz diziden alıp comboboxa yazdırır.
    private void initMonth(){
        DefaultComboBoxModel<Object> model = new DefaultComboBoxModel<>(months);
        month.setModel(model);
    }
    // Günleri gün sayısına göre yazdırır.
    // Her ay değiştirildiğinde son seçili gün tekrar seçilir.
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
    // Area 1 ve Area 2 için gradiyent arkaplan oluşturulur.
    private void createUIComponents() {
        Color purple = new Color(129,15,99);
        Color orange = new Color(252,80,57);
        Color midnight = new Color(17,33,88);
        Color purpleNight = new Color(150,98,162);
        resultPanel = new GradientPanel(purple,orange);
        expectedPanel = new GradientPanel(midnight,purpleNight);
    }
    //eğer verilen gün null ise boş değerleri, diğer durumlarda hava durumu değerlerini verilen alana yazdıran fonksiyon
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
