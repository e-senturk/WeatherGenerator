package weatherCalculator;

public class DayWeather {
    private double averageTemp;
    private double maxTemp;
    private double minTemp;
    private double humidity;
    private double visibility;
    private double averageWind;
    private double maxWind;

    public DayWeather(String averageTemp, String maxTemp, String minTemp, String humidity, String visibility, String averageWind, String maxWind) {
        this.averageTemp = fixValue(averageTemp);
        this.maxTemp = fixValue(maxTemp);
        this.minTemp = fixValue(minTemp);
        this.humidity = fixValue(humidity);
        this.visibility = fixValue(visibility);
        this.averageWind = fixValue(averageWind);
        this.maxWind = fixValue(maxWind);
    }
    public DayWeather() {
        this.averageTemp = 0;
        this.maxTemp = 0;
        this.minTemp = 0;
        this.humidity = 0;
        this.visibility = 0;
        this.averageWind = 0;
        this.maxWind = 0;
    }
    private double fixValue(String text){
        try{
            return Double.parseDouble(text);
        }
        catch (NumberFormatException e){
            return Integer.MIN_VALUE;
        }
    }

    public double getAverageTemp() {
        return averageTemp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getVisibility() {
        return visibility;
    }

    public double getAverageWind() {
        return averageWind;
    }

    public double getMaxWind() {
        return maxWind;
    }

    @Override
    public String toString() {
        return "DayWeather{" +
                "averageTemp=" + averageTemp +
                ", maxTemp=" + maxTemp +
                ", minTemp=" + minTemp +
                ", humidity=" + humidity +
                ", visibility=" + visibility +
                ", averageWind=" + averageWind +
                ", maxWind=" + maxWind +
                '}';
    }



    public void addMul(DayWeather day, int value,int [] coefficient){
        if(day.getAverageTemp()!=Integer.MIN_VALUE){
            this.averageTemp += day.getAverageTemp()*value;
            coefficient[0]+=value;
        }
        if(day.getMaxTemp()!=Integer.MIN_VALUE){
            this.maxTemp += day.getMaxTemp()*value;
            coefficient[1]+=value;
        }
        if(day.getMinTemp()!=Integer.MIN_VALUE){
            this.minTemp += day.getMinTemp()*value;
            coefficient[2]+=value;
        }
        if(day.getHumidity()!=Integer.MIN_VALUE){
            this.humidity += day.getHumidity()*value;
            coefficient[3]+=value;
        }
        if(day.getVisibility()!=Integer.MIN_VALUE){
            this.visibility += day.getVisibility()*value;
            coefficient[4]+=value;
        }
        if(day.getAverageWind()!=Integer.MIN_VALUE){
            this.averageWind += day.getAverageWind()*value;
            coefficient[5]+=value;
        }
        if(day.getMaxWind()!=Integer.MIN_VALUE){
            this.maxWind += day.getMaxWind()*value;
            coefficient[6]+=value;
        }
    }
    public void div(int [] coefficient){
        this.averageTemp/=coefficient[0];
        this.maxTemp/=coefficient[1];
        this.minTemp/=coefficient[2];
        this.humidity/=coefficient[3];
        this.visibility/=coefficient[4];
        this.averageWind/=coefficient[5];
        this.maxWind/=coefficient[6];
    }

}
