package driver;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import io.github.bonigarcia.wdm.InternetExplorerDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class DriverFactory {

//    private static final String USERNAME = System.getenv("BROWSERSTACK_USERNAME");
//    private static final String AUTOMATE_KEY = System.getenv("BROWSERSTACK_ACCESS_KEY");
//    private static final String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";

    private static final String URL = System.getenv("REMOTE_URL");
    private static WebDriver driver;

    public static WebDriver getDriver() {

        String browser = System.getenv("BROWSER");
        String location = System.getenv("LOCATION");
        browser = (browser == null) ? "CHROME" : browser;

        if (location.equals("LOCAL")) {
            return setUpLocal(browser);
        } else
            return setUpRemote(browser);
    }

    public static WebDriver setUpLocal(String browser) {
        switch (browser) {
            case "IE":
                InternetExplorerDriverManager.getInstance().setup();
                return new InternetExplorerDriver();
            case "Firefox":
                FirefoxDriverManager.getInstance().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if ("Y".equalsIgnoreCase(System.getenv("HEADLESS"))) {
                    firefoxOptions.addArguments("--headless");
                }

                return new FirefoxDriver(firefoxOptions);
            case "Chrome":
            default:
                ChromeDriverManager.getInstance().setup();

                ChromeOptions options = new ChromeOptions();
                if ("Y".equalsIgnoreCase(System.getenv("HEADLESS"))) {
                    options.addArguments("--headless");
                    options.addArguments("--disable-gpu");
                }

                return new ChromeDriver(options);
        }
    }

    public static WebDriver setUpRemote(String browser) {
        try {
            DesiredCapabilities desiredCapabilities;
            switch (browser) {
                case "IE":
                    InternetExplorerDriverManager.getInstance().setup();
                    driver = new InternetExplorerDriver();
                case "FIREFOX":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    if ("Y".equalsIgnoreCase(System.getenv("HEADLESS"))) {
                        firefoxOptions.addArguments("--headless");
                    }
                    driver = new RemoteWebDriver(new URL(URL), firefoxOptions);
                case "CHROME":
                default:
                    ChromeOptions options = new ChromeOptions();
                    if ("Y".equalsIgnoreCase(System.getenv("HEADLESS"))) {
                        options.addArguments("--headless");
                        options.addArguments("--disable-gpu");
                    }
//                    desiredCapabilities = DesiredCapabilities.chrome();
//                    desiredCapabilities.setVersion(System.getenv("BROWSER_VERSION"));
                    driver = new RemoteWebDriver(new URL(URL), options);
            }
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        } catch (MalformedURLException e) {
            System.out.println(e.toString());
        }
        return driver;
    }
}
