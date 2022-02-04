import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class YahooFinanceTest {

    private WebDriver driver;
    private static final String URL = "https://www.finance.yahoo.com";

    @BeforeTest
    public void beforeTest() {
        System.setProperty("webdriver.chrome.driver", "/Users/abiramirajendran/drivers/chromedriver");
        driver = new ChromeDriver();
    }

    @Test
    @Parameters("stockName")
    public void testYahoo(String stockList) {
        String splitStock[] = stockList.split(",");
        driver.get(URL);
        driver.manage().window().maximize();

        //this button click can be removed if "try free" window doesnt show up
        //this window was shown during the coding, might not come up later
//        WebElement button = driver.findElement(By.xpath("//*[@id='myLightboxContainer']/button"));
//        if(button.isDisplayed()) {
//            button.click();
//        }

        for(String stock: splitStock) {
            driver.manage().timeouts().implicitlyWait(25, TimeUnit.SECONDS);
            driver.findElement(By.id("yfin-usr-qry")).sendKeys(stock);
            driver.findElement(By.xpath("//*[@id='header-desktop-search-button']")).click();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            driver.findElement(By.xpath("//*[@id='quote-nav']/ul/li[9]/a/span")).click();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            driver.manage().timeouts().pageLoadTimeout(20,TimeUnit.SECONDS);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0,1000);", "");
            WebElement recommendation = driver.findElement(By.id("Col2-4-QuoteModule-Proxy"));
            js.executeScript("arguments[0].scrollIntoView(true);",recommendation);
            js.executeScript("arguments[0].click();",recommendation);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String ratingText = null;

            //ASM stock doesnt have Recommendation Rating,
            //hence searching for that text using hyperlinks
            List<WebElement> searchHyperLinksList = driver.findElements(By.tagName("a"));
            boolean isText = false;
            for(WebElement element: searchHyperLinksList) {
                if(element.getAttribute("href").contains("Recommendation Rating")){
                    isText = true;
                    break;
                }
            }

            if(isText) {
                WebElement rating = driver.findElement(By.xpath("//*[@id='Col2-4-QuoteModule-Proxy']/div/section/div/div/div[1]"));
                js.executeScript("arguments[0].scrollIntoView(true);", rating);
                js.executeScript("arguments[0].click();", rating);
                ratingText = rating.getText();
                System.out.println(ratingText);
            }
            String average = driver.findElement(By.xpath("//*[@id='Col2-5-QuoteModule-Proxy']/div/section/div/div[1]/div[4]/div[1]/span[2]")).getText();
            String low = driver.findElement(By.xpath("//*[@id='Col2-5-QuoteModule-Proxy']/div/section/div/div[2]/div[1]/span[2]")).getText();
            String high = driver.findElement(By.xpath("//*[@id='Col2-5-QuoteModule-Proxy']/div/section/div/div[2]/div[2]/span[2]")).getText();
            String current = driver.findElement(By.xpath("//*[@id='Col2-5-QuoteModule-Proxy']/div/section/div/div[1]/div[3]/div[3]/span[2]")).getText();
            System.out.println("Rating value for stock: "+stock+" = "+ratingText);
            System.out.println("Average value for stock: "+stock+" = "+average);
            System.out.println("Current value for stock: "+stock+" = "+current);
            System.out.println("High value for stock: "+stock+" = "+high);
            System.out.println("Low value for stock: "+stock+" = "+low);

            js.executeScript("scroll(0,-1000);", "");
        }
    }


    @AfterTest
    public void afterTest() {
        driver.close();
    }
}

