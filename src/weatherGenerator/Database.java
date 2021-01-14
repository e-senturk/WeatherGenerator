package weatherGenerator;

import tools.CityList;
import tools.FileMaker;

import javax.swing.*;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.LinkedList;

//Veri tabanı oluşturan fonksiyonların bulunduğu class yapısı
public class Database {
    private static boolean isWorking;
    // Seçilen özelliklere göre veri tabanı oluşturur
    public static void generateDatabase(String type, boolean outScreenFirefox, boolean hiddenFirefox, int inputStartYear,
                                        int inputEndYear, int outputYear,JLabel infoInput, JLabel infoOutput) {
        isWorking = true;
        // Başlangıç sayfası google olacak şekilde firefoxu başlatır
        Firefox.createFirefox("https://www.google.com", outScreenFirefox, hiddenFirefox, 400);
        // Eğer input alınacaksa tüm şehir kodları,ve input yılları için iç içe loop oluşturulur
        if (type.equals("input")) {
            for (String[] city : CityList.cityCodes) {
                // işlem yapılan tarihi ay ve yıl olarak tutar.
                int[] date = {1, inputStartYear};
                while (date[0] != 1 || date[1] != inputEndYear) {
                    // tarih için bir string oluşturulur
                    String dateText = generateStringDate(date);
                    // O şehir için o aya ait veriler dosyaya yazdırılır
                    generateMonth(type, dateText, city, infoInput);
                    // Sonraki aya geçilir
                    nextMonth(date);
                    if(!isWorking)
                        return;
                }
            }
        }
        // Eğer output alınacaksa tüm şehir kodları,ve seçilen output yılı için iç içe loop oluşturulur
        else if (type.equals("output")) {
            for (String[] city : CityList.cityCodes) {
                // işlem yapılan tarihi ay ve yıl olarak tutar.
                int[] date = {1, outputYear};
                while (date[0] != 1 || date[1] != outputYear + 1) {
                    // tarih için bir string oluşturulur
                    String dateText = generateStringDate(date);
                    // O şehir için o aya ait veriler dosyaya yazdırılır
                    generateMonth(type, dateText, city, infoOutput);
                    // Sonraki aya geçilir
                    nextMonth(date);
                    if(!isWorking)
                        return;
                }
            }
        }
        // İşlem bitince firefox kapatılır.
        Firefox.destroyFirefox();
    }
    public static void setIsWorking(boolean isWorking) {
        Database.isWorking = isWorking;
    }

    // Siteden alınan html verisi üzerindeki bi takım kodlama metodları kullanılmıştı o nedenle veriyi çekerken bu fonksiyonlara
    // ihtiyaç duydum
    public static void generateMonth(String type, String date, String[] city, JLabel info) {
        // Verilen verilere göre linki okur ve html verisini alır
        String pageContent = Firefox.updateUrl("https://en.tutiempo.net/climate/" + date + "/" + city[1] + ".html", 400);
        // Alınan verideki kodlu kısımları çözmek için anahtar değerleri ile bir hash tablosu oluşturulur.
        String pageHash = findBetween(pageContent, "<style>.tablancpy", "</table>", 0);
        HashMap<String, String> numberCodes = generateNumberHash(findBetweenList(pageHash, "span", "numspan"));
        // İşlem uzun sürdüğünden hangi ilde olduğumu takip etmek için çıktı aldım
        info.setText(city[0]+ " Oluşturuluyor.");
        // Tablo bilgilerini aldım
        String pageInfo = findBetween(pageContent, "In the monthly average, Total days with fog)\">FG", "</table>", 0);
        // Tablodaki 1 aylık bilgiyi günlere böldüm.
        LinkedList<String> monthTemperature = findBetweenList(pageInfo, "<tr><td>", ";</td></tr>");
        for (String dayTemperature : monthTemperature) {
            // Tüm günler için alınan kısmı dosyaya yazdıracağım formata çevirdim
            dayTemperature = updateWithHash(dayTemperature, numberCodes, "<span class=\"", "\"").
                    replace("<span class=\"", "").replace("\"></span>", "").
                    replace("&nbsp", "-").replace(";", " ").replace("</td><td>", " ").
                    replace("</strong>", "").replace("<strong>", "").replace("</td></tr><tr><td>", "\n");
            // Sonucun başıdaki tarih bilgisini düzenledim
            dayTemperature = addDate(dayTemperature, date);
            // Sonucu dosyaya yazdırdım.
            FileMaker.addFile(type, city[0], dayTemperature);
        }
    }

    // Verilen span bilgisinden hash tablosu oluşturan fonksiyon
    public static HashMap<String, String> generateNumberHash(LinkedList<String> hashList) {
        HashMap<String, String> numbers = new HashMap<>();
        for (String text : hashList) {
            String key = findBetween(text, ".", "::", 0);
            String value = findBetween(text, "content:\"", "\"", 0);
            numbers.put(key, value);
        }
        return numbers;
    }

    // Alınan inputun başındaki tarih bilgisini düzenleyen fonksiyon
    public static String addDate(String info, String date) {
        int i = 0;
        do {
            int space = info.indexOf(" ", i);
            if (space == i + 1 && i == 0)
                info = "0" + info.substring(0, space) + "-" + date + info.substring(space);
            else if (space == i + 2 && i != 0) {
                info = info.substring(0, space - 1) + "0" + info.charAt(space - 1) + "-" + date + info.substring(space);
            } else
                info = info.substring(0, space) + "-" + date + info.substring(space);
            i = info.indexOf("\n", i + 1);

        } while (i != -1);
        return info;
    }

    //Verilen stringde iki kelime arasındaki ilk bulunan bölümü bulan fonksiyon.
    public static String findBetween(String split, String begin, String end, int startIndex) {
        int start = split.indexOf(begin, startIndex);
        int stop = split.indexOf(end, start + begin.length());
        if (start == -1 || stop == -1) {
            return "";
        } else {
            return split.substring(start + begin.length(), stop).trim();
        }
    }

    //Verilen stringde iki kelime arasındaki tüm tekrarları bulup bir liste oluşturan fonksiyon
    public static LinkedList<String> findBetweenList(String split, String begin, String end) {
        LinkedList<String> newList = new LinkedList<>();
        int index = 0;
        boolean find = true;
        while (find && index < split.length()) {
            int start = split.indexOf(begin, index);
            int stop = split.indexOf(end, start + begin.length());
            if (start == -1 || stop == -1) {
                find = false;
            } else {
                index = stop + end.length();
                newList.add(split.substring(start + begin.length(), stop).trim());
            }
        }
        return newList;
    }

    //İki kelime arasındaki ilk bulunan bölümü bulan ve araya hash değerini yerleştiren fonksiyon.
    public static String insertBetween(String split, HashMap<String, String> hash, String begin, String end, int[] startIndex) {
        int start = split.indexOf(begin, startIndex[0]);
        int stop = split.indexOf(end, start + begin.length());
        if (start == -1 || stop == -1) {
            startIndex[0] = -1;
            return split;
        } else {
            start += begin.length();
            startIndex[0] = stop;
            return split.substring(0, start) +
                    hash.get(split.substring(start, stop).trim()) +
                    split.substring(stop);
        }
    }

    // Span değerlerini hash tablosundaki değerlere göre düzenleyen fonksiyon
    public static String updateWithHash(String update, HashMap<String, String> hash, String begin, String end) {
        int[] index = {0};
        while (index[0] != -1)
            update = Database.insertBetween(update, hash, begin, end, index);
        return update;
    }

    // Ay ve yıl bilgisini sonraki aya çeviren fonksiyon
    public static void nextMonth(int[] date) {
        if (date.length != 2)
            return;
        if (date[0] < 12)
            date[0] = date[0] + 1;
        else {
            date[0] = 1;
            date[1] = date[1] + 1;
        }
    }

    // Verilen ay ve yıl bilgisinden string oluşturan fonksiyon
    public static String generateStringDate(int[] date) {
        if (date[0] < 10)
            return "0" + date[0] + "-" + date[1];
        return date[0] + "-" + date[1];
    }
}
