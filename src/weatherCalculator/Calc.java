package weatherCalculator;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Scanner;

public class Calc {
    public static HashMap<Integer,DayWeather> readData(String city,String path) {
        File input = new File(path+"/"+city+".txt");
        HashMap<Integer, DayWeather> hash = new HashMap<>();
        try{
            Scanner reader = new Scanner(input);
            while(reader.hasNextLine()){
                String data = reader.nextLine();
                String [] split = data.split(" ");
                DayWeather weather = new DayWeather(split[1],split[2],split[3],split[5],split[7],split[8],split[9]);
                hash.put(convertDate(split[0]),weather);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Dosya bulunamadı");
        }
        return hash;
    }
    public static int convertDate(int d,int m,int y){
        StringBuilder result = new StringBuilder();
        if(d<10)
            result.append("0");
        result.append(d).append("-");
        if(m<10)
            result.append("0");
        result.append(m).append("-").append(y);
        return convertDate(result.toString());
    }
    public static int convertDate(String date){
        try{
            return (int) (new SimpleDateFormat("dd-MM-yyyy").parse(date).getTime() / 86400000);
        }
        catch (ParseException e) {
            System.err.println("InvalidDate" + date);
            return -1;
        }
    }
    public static Integer[] generateCloseYears(int date,int range){
        Integer[] years= new Integer[range*2+1];
        for(int i=date-range,j=0;i<=date+range;i++,j++)
            years[j]=i;
        return years;
    }
    public static DayWeather generateYearRatio(int date,HashMap<Integer,DayWeather> hash,int step,int missingData){
        int start = 100%step;
        Integer[] years = generateCloseYears(date,100/step);
        DayWeather result = new DayWeather();
        int i=start;
        int j=0;
        int [] coefficient = new int[]{0,0,0,0,0,0,0};
        for(;i<100;i+=step,j++){
            if(hash.containsKey(years[j]))
                result.addMul(hash.get(years[j]),i,coefficient);
            else
                missingData--;
        }
        if(hash.containsKey(date))
            result.addMul(hash.get(date),100,coefficient);
        else
            missingData--;
        j++;
        i-=step;
        for(;i>0;i-=step,j++){
            if(hash.containsKey(years[j]))
                result.addMul(hash.get(years[j]),i,coefficient);
            else
                missingData--;
        }
        if(missingData>0){
            result.div(coefficient);
            return result;
        }
        else
            return null;

    }
}
