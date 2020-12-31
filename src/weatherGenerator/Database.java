package weatherGenerator;

import java.util.HashMap;
import java.util.LinkedList;

public class Database {
    public static void generateDatabase(String type,boolean outScreenFirefox,boolean hiddenFirefox,int inputStartYear,
                                        int inputEndYear,int outputYear){
        Firefox.createFirefox("https://www.google.com",outScreenFirefox,hiddenFirefox,400);
        if(type.equals("input")){
            for(String[] city:CityList.cityCodes){
                int [] date = {1,inputStartYear};
                while(date[0]!=1 || date[1] != inputEndYear){
                    String dateText = generateStringDate(date);
                    generateMonth(type,dateText,city);
                    nextMonth(date);
                }
            }
        }
        else if(type.equals("output")){
            for(String[] city:CityList.cityCodes){
                int [] date = {1,outputYear};
                while(date[0]!=1 || date[1] != outputYear+1){
                    String dateText = generateStringDate(date);
                    generateMonth(type,dateText,city);
                    nextMonth(date);
                }
            }
        }
        Firefox.destroyFirefox();
    }
    public static void generateMonth(String type,String date,String[] city){

        Firefox.updateUrl("https://en.tutiempo.net/climate/"+date+"/"+city[1]+".html",400);
        String pageContent = Firefox.getHtmlInformation();
        String pageHash = findBetween(pageContent,"<style>.tablancpy","</table>",0);
        System.err.println(city[0]);
        HashMap<String,String> numberCodes = generateNumberHash(findBetweenList(pageHash,"span","numspan"));
        String pageInfo = findBetween(pageContent,"In the monthly average, Total days with fog)\">FG","</table>",0);
        LinkedList<String> monthTemperature = findBetweenList(pageInfo,"<tr><td>",";</td></tr>");
        for(String dayTemperature:monthTemperature){
            dayTemperature = updateWithHash(dayTemperature,numberCodes,"<span class=\"","\"").
                    replace("<span class=\"","").replace("\"></span>","").
                    replace("&nbsp","-").replace(";"," ").replace("</td><td>"," ").
                    replace("</strong>","").replace("<strong>","").replace("</td></tr><tr><td>","\n");
            dayTemperature = addDate(dayTemperature,date);
            FileMaker.addFile(type,city[0],dayTemperature);
        }
    }

    //İki kelime arasındaki ilk bulunan bölümü bulan fonksiyon.
    public static String findBetween(String split, String begin, String end, int startIndex) {
        int start = split.indexOf(begin, startIndex);
        int stop = split.indexOf(end, start + begin.length());
        if (start == -1 || stop == -1) {
            return "";
        } else {
            return split.substring(start + begin.length(), stop).trim();
        }
    }
    //İki kelime arasındakileri bulup bir liste oluşturan fonksiyon
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
    public static HashMap<String, String> generateNumberHash(LinkedList<String> hashList){
        HashMap<String, String> numbers = new HashMap<>();
        for(String text: hashList){
            String key = findBetween(text,".","::",0);
            String value = findBetween(text,"content:\"","\"",0);
            numbers.put(key,value);
        }
        return numbers;
    }
    //İki kelime arasındaki ilk bulunan bölümü bulan ve araya hash değerini yerleştiren fonksiyonfonksiyon.
    public static String insertBetween(String split,HashMap<String, String> hash, String begin, String end, int [] startIndex) {
        int start = split.indexOf(begin, startIndex[0]);
        int stop = split.indexOf(end, start + begin.length());
        if (start == -1 || stop == -1) {
            startIndex[0] = -1;
            return split;
        } else {
            start+=begin.length();
            startIndex[0]=stop;
            return split.substring(0, start) +
                    hash.get(split.substring(start, stop).trim()) +
                    split.substring(stop);
        }
    }
    public static String updateWithHash(String update,HashMap<String,String> hash, String begin,String end){
        int [] index = {0};
        while (index[0]!=-1)
            update = Database.insertBetween(update, hash, begin, end, index);
        return update;
    }
    public static String addDate(String info,String date){
        int i = 0;
        do{
            int space = info.indexOf(" ",i);
            if(space==i+1&&i==0)
                info = "0"+info.substring(0,space) +"-"+ date+ info.substring(space);
            else if (space==i+2&&i!=0){
                info = info.substring(0,space-1)+"0"+info.charAt(space-1) +"-"+ date+ info.substring(space);
            }
            else
                info = info.substring(0,space) +"-"+ date+ info.substring(space);
            i = info.indexOf("\n",i+1);

        }while (i!=-1);
        return info;
    }
    public static void nextMonth(int[] date){
        if(date.length!=2)
            return;
        if(date[0] <12)
            date[0] = date[0]+1;
        else {
            date[0] = 1;
            date[1] = date[1] + 1;
        }
    }
    public static String generateStringDate(int[] date){
        if(date[0]<10)
            return "0"+ date[0] +"-"+ date[1];
        return date[0] +"-"+ date[1];
    }
}
