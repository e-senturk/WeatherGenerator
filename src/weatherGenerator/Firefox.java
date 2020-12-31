package weatherGenerator;

import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class Firefox {
    private static final String webDriverLocation = "webdriver/geckodriver.exe";
    private static final String webDriverLocationMac = "webdriver/geckodriver";
    private static final String userAgent="Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Safari/605.1.15\")";
    private static FirefoxDriver driver;
    private static String htmlInformation;
    private static String osName = null;

    //Singleton class ;
    private Firefox() {
    }



    public static String getHtmlInformation() {
        return htmlInformation;
    }




    public static void updateUrl(String url, int timeMs) {
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
                htmlInformation = driver.getPageSource();
            } else {
                System.out.println("Link okunamadı.\n");
            }
        } catch (WebDriverException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createFirefox(String url, boolean outScreenFirefox, boolean hiddenScreen,int timeMs) {
        if (isWindows()) {
            System.setProperty("webdriver.gecko.driver", webDriverLocation);
        } else {
            System.setProperty("webdriver.gecko.driver", webDriverLocationMac);
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

    private static String getOsName() {
        if (osName == null) {
            osName = System.getProperty("os.name");
        }
        return osName;
    }

    private static boolean isWindows() {
        return getOsName().startsWith("Windows");
    }

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
