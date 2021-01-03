package weatherCalculator;

public class DayWeather {
    private double averageTemp;
    private double maxTemp;
    private double minTemp;
    private double humidity;
    private double visibility;
    private double averageWind;
    private double maxWind;

    // Hava durumu verilerinin saklandığı nesne
    public DayWeather(String averageTemp, String maxTemp, String minTemp, String humidity, String visibility, String averageWind, String maxWind) {
        this.averageTemp = fixValue(averageTemp);
        this.maxTemp = fixValue(maxTemp);
        this.minTemp = fixValue(minTemp);
        this.humidity = fixValue(humidity);
        this.visibility = fixValue(visibility);
        this.averageWind = fixValue(averageWind);
        this.maxWind = fixValue(maxWind);
    }

    // Eğer değer verilmemişse tüm değerleri 0 olan bir nesne oluşturan constructor
    public DayWeather() {
        this.averageTemp = 0;
        this.maxTemp = 0;
        this.minTemp = 0;
        this.humidity = 0;
        this.visibility = 0;
        this.averageWind = 0;
        this.maxWind = 0;
    }

    //Verilen 2 günün fark değerlerlerine göre yakınlığını hesaplar başarı oranını hesaplarken kullanıldı.
    public static boolean[] isSuccessful(DayWeather d1, DayWeather d2, int tempDiff, int humDiff, int visDiff, int windDiff) {
        // Başarı dizisi
        boolean[] success = new boolean[]{false, false, false, false, false, false, false};
        // iki günden biri null ise başarısız kabul edildi
        if (d1 == null || d2 == null)
            return success;
        // fark değerlerine göre başarı dizisi ayarlandı
        if (Math.abs(d1.getAverageTemp() - d2.getAverageTemp()) <= tempDiff) {
            success[0] = true;
        }
        if (Math.abs(d1.getMaxTemp() - d2.getMaxTemp()) <= tempDiff) {
            success[1] = true;
        }
        if (Math.abs(d1.getMinTemp() - d2.getMinTemp()) <= tempDiff) {
            success[2] = true;
        }
        if (Math.abs(d1.getHumidity() - d2.getHumidity()) <= humDiff) {
            success[3] = true;
        }
        if (Math.abs(d1.getVisibility() - d2.getVisibility()) <= visDiff) {
            success[4] = true;
        }
        if (Math.abs(d1.getAverageWind() - d2.getAverageWind()) <= windDiff) {
            success[5] = true;
        }
        if (Math.abs(d1.getMaxWind() - d2.getMaxWind()) <= windDiff) {
            success[6] = true;
        }
        return success;
    }

    //integer bir diziye boolean değerleri eklemek için kullanılan fonksiyon
    public static void addValue(int[] sum, boolean[] value) {
        for (int i = 0; i < 7; i++) {
            if (value[i])
                sum[i]++;
        }
    }

    // String double a çevirlebiliyorsa sayısı çevrilemiyosa minimum double değerini döndüren fonksiyon
    private double fixValue(String text) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return Double.MIN_VALUE;
        }
    }

    // Getter Metotları
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

    // Yazdırmak için kulalndığım metot
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

    /* Bir hava durumu verisine başka bir hava durumu verisindeki tüm değerleri istenilen kat sayı ile çarpıp ekler
     Eğer geçersiz veri ise o değeri çarpmaz.
     Coefficent her geçerli veri için arttırılır.
     Bu sayede oran hesaplanırken geçerli sayıdaki veri katsayılarının çarpımına bölme işlemi yapılabilmiş olur.
     */
    public void addMul(DayWeather day, int value, int[] coefficient) {
        // Veriler tek tek geçerli mi değil mi diye değerlendirilir geçerli ise çarpılıp eklenir.
        if (day.getAverageTemp() != Double.MIN_VALUE) {
            this.averageTemp += day.getAverageTemp() * value;
            coefficient[0] += value;
        }
        if (day.getMaxTemp() != Double.MIN_VALUE) {
            this.maxTemp += day.getMaxTemp() * value;
            coefficient[1] += value;
        }
        if (day.getMinTemp() != Double.MIN_VALUE) {
            this.minTemp += day.getMinTemp() * value;
            coefficient[2] += value;
        }
        if (day.getHumidity() != Double.MIN_VALUE) {
            this.humidity += day.getHumidity() * value;
            coefficient[3] += value;
        }
        if (day.getVisibility() != Double.MIN_VALUE) {
            this.visibility += day.getVisibility() * value;
            coefficient[4] += value;
        }
        if (day.getAverageWind() != Double.MIN_VALUE) {
            this.averageWind += day.getAverageWind() * value;
            coefficient[5] += value;
        }
        if (day.getMaxWind() != Double.MIN_VALUE) {
            this.maxWind += day.getMaxWind() * value;
            coefficient[6] += value;
        }
    }

    // Önceki fonksiyonda oluşturulan coefficent matrisine veriler bölünür bu sayede istenilen ortalama değeri alınmış olur.
    public void div(int[] coefficient) {
        this.averageTemp /= coefficient[0];
        this.maxTemp /= coefficient[1];
        this.minTemp /= coefficient[2];
        this.humidity /= coefficient[3];
        this.visibility /= coefficient[4];
        this.averageWind /= coefficient[5];
        this.maxWind /= coefficient[6];
    }

}
