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

    private static final String URL = System.getenv("REMOTE_URL");;
    private WebDriver driver;

    public WebDriver getDriver() {

        String browser = System.getenv("BROWSER");
        String location = System.getenv("LOCATION");
        browser = (browser == null) ? "CHROME" : browser;

        if (location.equals("LOCAL")) {
            return setUpLocal(browser);
        } else
            return setUpRemote(browser);
    }

    public WebDriver setUpLocal(String browser) {
        switch (browser) {
            case "IE":
                InternetExplorerDriverManager.getInstance().setup();
                return new InternetExplorerDriver();
            case "Firefox":
                FirefoxDriverManager.getInstance().setup();
                return new FirefoxDriver();
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

    public WebDriver setUpRemote(String browser) {
        try {
            DesiredCapabilities desiredCapabilities;
            switch (browser) {
                case "IE":
                    InternetExplorerDriverManager.getInstance().setup();
                    this.driver = new InternetExplorerDriver();
                case "FIREFOX":
                    desiredCapabilities = DesiredCapabilities.firefox();
                    this.driver = new RemoteWebDriver(new URL(URL), desiredCapabilities);
                case "CHROME":
                default:
                    desiredCapabilities = DesiredCapabilities.chrome();
                    this.driver = new RemoteWebDriver(new URL(URL), desiredCapabilities);
            }

            this.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        } catch (MalformedURLException e) {
            System.out.println(e.toString());
        }
        return this.driver;
    }
}
