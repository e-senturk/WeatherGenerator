package main;

import com.toedter.calendar.JYearChooser;

public class YearChooser {
    public static JYearChooser generateYearChooser(int defaultYear, int minYear, int maxYear) {
        JYearChooser yearChooser = new JYearChooser();
        yearChooser.setStartYear(minYear);
        yearChooser.setEndYear(maxYear);
        if (maxYear > minYear)
            yearChooser.setValue(defaultYear);
        return yearChooser;
    }
}
