package com.iscp.utilities;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

/**
 * Base Test
 *
 * @author SaRa
 * @lastModifiedBy SaRa
 * @lastModifiedDate 20220310
 */
public class BaseTest {

    /**
     * Fucntion to instantiate WebDriver
     * @return
     * @throws Throwable
     * @author SaRa
     * @lastModifiedBy SaRa
     * @lastModifiedDate 20220310
     */
    public static WebDriver init() throws Throwable {

        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://oe3fpy.axshare.com/#id=tmcvbv&p=hf-_retailer_end&g=1&hi=1");
        /*driver.get(url);*/
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        return driver;
    }

    public static void main(String[] args) throws Throwable {
        init();
    }
}
