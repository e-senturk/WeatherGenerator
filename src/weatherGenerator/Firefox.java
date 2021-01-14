package weatherGenerator;

import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.Locale;

public class Firefox {
    // kullanılan webdriver
    private static final String webDriverLocationMac = System.getProperty("user.dir") + "/webdriver/geckodriver";
    private static final String webDriverLocation = "webdriver/geckodriver.exe";
    // kullanılan tarayıcının hangisi olduğunu göstermek istediğimiz (Safari seçildi)
    private static final String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Safari/605.1.15\")";
    // Driver nesnesi
    private static FirefoxDriver driver;

    //Singleton class ;
    private Firefox() {
    }

    // Bekleme süresine göre url yükleyen ve içindeki html verisini htmlInformation a kaydeden fonksiyon
    public static String updateUrl(String url, int timeMs) {
        try {
            //Driver içine link alındı.
            driver.get(url);

            try {
                Thread.sleep(timeMs);
            } catch (InterruptedException e) {
                System.out.println("Error on wait.");
                e.printStackTrace();
            }

            if (driver != null) {
                System.out.println(driver);
                return driver.getPageSource();
            } else {
                System.out.println("Link okunamadı.\n");
            }
        } catch (WebDriverException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }

    // Verilen özelliklere göre firefox driverı oluşturan fonksiyon
    public static void createFirefox(String url, boolean outScreenFirefox, boolean hiddenScreen, int timeMs) {
        System.out.println("Test2");
        System.out.println(System.getProperty("os.name"));
        if(System.getProperty("os.name").toLowerCase().contains("mac")) {
            System.out.println("This is a Mac Os X system");
            System.setProperty("webdriver.gecko.driver",webDriverLocationMac);
        }
        else{
            System.out.println("This is a Windows system");
            System.setProperty("webdriver.gecko.driver", webDriverLocation);
        }
        // Sistem yapısı webDriver incelenerek oluşturuldu.
        FirefoxOptions options = new FirefoxOptions();
        //Seçenekler belirlendi.
        options.addPreference("general.useragent.override", userAgent);
        options.addPreference("javascript.enabled", true);
        //Eğer seçiliyse headless mode aktifletirilerek arka planda çalıştırıldı.
        if (hiddenScreen)
            options.addArguments("--headless");

        //Oluşturulan seçeneklere göre web driver oluşturuldu.
        driver = new FirefoxDriver(options);

        //Eğer seçiliyse webdriver ekrandan çıkartıldı.
        if (outScreenFirefox) {
            driver.manage().window().setPosition(new Point(2000, 2000));
        }
        updateUrl(url, timeMs);
    }

    // Firefox driverını kapatan fonksiyon
    public static void destroyFirefox() {
        try {
            if (isOperates()) {
                driver.close();
                driver.quit();
            }
        } catch (WebDriverException e) {
            System.out.println(e.getMessage());
        }

    }

    // Firefoxun driverın çalışıp çalışmadığını döndüren fonksiyon
    public static boolean isOperates() {
        if (driver == null) {
            return false;
        }
        boolean operation = true;
        try {
            driver.getWindowHandles();
        } catch (WebDriverException | NullPointerException e) {
            operation = false;
            System.out.println("No driver found");
        }
        return operation;
    }
}
