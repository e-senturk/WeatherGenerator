package tools;

// Yıl seçici için kullandığım harici kütüphane
import com.toedter.calendar.JYearChooser;
public class YearChooser {
    // Verilen aralık ve varsayılan değerlere göre yıl seçici oluşturan fonksiyon
    public static JYearChooser generateYearChooser(int defaultYear, int minYear, int maxYear) {
        JYearChooser yearChooser = new JYearChooser();
        yearChooser.setStartYear(minYear);
        yearChooser.setEndYear(maxYear);
        if (maxYear > minYear)
            yearChooser.setValue(defaultYear);
        return yearChooser;
    }
}
