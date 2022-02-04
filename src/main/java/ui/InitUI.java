package ui;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class InitUI {
    private WebDriver driver;
    //change browser driver
    private static final  String DRIVER_PATH = "/Users/abiramirajendran/drivers/chromedriver";

    public InitUI() {
        System.setProperty("webdriver.chrome.driver", DRIVER_PATH);
        this.driver = new ChromeDriver();
    }

    public WebDriver getDriver() {
        return  driver;
    }

}
