package ui;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class YahooFinancePage extends InitiUI {
    private static final Logger log = Logger.getLogger(YahooFinancePage.class);

    private WebDriver driver;
    private static final String URL = "https://www.finance.yahoo.com";

    @FindBy(id = "yfin-usr-qry")
    private WebElement searchType;

    @FindBy(xpath = "//*[@id='header-desktop-search-button']")
    private WebElement searchButton;

    @FindBy(xpath = "//*[@id='quote-nav']/ul/li[9]/a/span")
    private WebElement analysis;

    @FindBy(id = "Col2-4-QuoteModule-Proxy")
    private WebElement recommendation;

    @FindBy(xpath = "//*[@id='Col2-4-QuoteModule-Proxy']/div/section/div/div/div[1]")
    private WebElement recoomendationRatingLink;

    @FindBy(xpath = "//*[@id='Col2-5-QuoteModule-Proxy']/div/section/div/div[1]/div[4]/div[1]/span[2]")
    private WebElement averageElement;

    @FindBy(xpath = "//*[@id='Col2-5-QuoteModule-Proxy']/div/section/div/div[2]/div[1]/span[2]")
    private WebElement lowElement;

    @FindBy(xpath = "//*[@id='Col2-5-QuoteModule-Proxy']/div/section/div/div[2]/div[2]/span[2]")
    private WebElement highElement;

    @FindBy(xpath = "//*[@id='Col2-5-QuoteModule-Proxy']/div/section/div/div[1]/div[3]/div[3]/span[2]")
    private WebElement currentElement;

    public YahooFinancePage() {
        super();
        this.driver = getDriver();
        PageFactory.initElements(this.driver, this);
        driver.get(URL);
    }

    public Map<String, Map<String, String>> getStockValues(String stock) {
        Map<String, Map<String, String>> stockValuesMap = new HashMap<>();
        Map<String,String> valuesMap = new HashMap<>();

        driver.manage().window().maximize();

        //this button click can be removed if "try free" window doesnt show up
        //this window was shown during the coding, might not come up later
//        WebElement button = driver.findElement(By.xpath("//*[@id='myLightboxContainer']/button"));
//        if(button.isDisplayed()) {
//            button.click();
//        }
        driver.manage().timeouts().implicitlyWait(25, TimeUnit.SECONDS);
        searchType.sendKeys(stock);
        searchButton.click();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        analysis.click();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,1000);", "");
        js.executeScript("arguments[0].scrollIntoView(true);", recommendation);
        js.executeScript("arguments[0].click();", recommendation);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String ratingText = null;

        //ASM stock doesnt have Recommendation Rating,
        //hence searching for that text using hyperlinks
//        List<WebElement> searchHyperLinksList = driver.findElements(By.tagName("a"));
//        boolean isText = false;
//        for (WebElement element : searchHyperLinksList) {
//            if (element.getAttribute("href").contains("Recommendation Rating")) {
//                isText = true;
//                break;
//            }
//        }

        if (!stock.equalsIgnoreCase("asm")) {
            js.executeScript("arguments[0].scrollIntoView(true);", recoomendationRatingLink);
            js.executeScript("arguments[0].click();", recoomendationRatingLink);
            ratingText = recoomendationRatingLink.getText();
            valuesMap.putIfAbsent("rating", ratingText);
        }
        String averageValue = averageElement.getText();
        String lowValue = lowElement.getText();
        String highValue = highElement.getText();
        String currentValue = currentElement.getText();

        valuesMap.putIfAbsent("average", averageValue);
        valuesMap.putIfAbsent("low", lowValue);
        valuesMap.putIfAbsent("high", highValue);
        valuesMap.putIfAbsent("current", currentValue);

        stockValuesMap.put(stock, valuesMap);

        System.out.println("Rating value for stock: " + stock + " = " + ratingText);
        System.out.println("Average value for stock: " + stock + " = " + averageValue);
        System.out.println("Current value for stock: " + stock + " = " + currentValue);
        System.out.println("High value for stock: " + stock + " = " + highValue);
        System.out.println("Low value for stock: " + stock + " = " + lowValue);

        log.debug("Scenario execution Complete:");
        js.executeScript("scroll(0,-1000);", "");

        return stockValuesMap;
    }


        public void closeBrowser(){
            driver.close();
        }
}
