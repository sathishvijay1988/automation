/**
 * this class test the functionality of finance.yahoo.com to get the values of different stocks
 *
 */

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import ui.YahooFinancePage;

import java.util.Map;

public class YahooFinanceTest {
    YahooFinancePage yahooFinancePage;
    private static final Logger log = Logger.getLogger(YahooFinanceTest.class);

    @BeforeTest
    public void beforeTest() {
        log.debug("before test");
        yahooFinancePage = new YahooFinancePage();
    }

    @Test
    @Parameters("stockName")
    public void testYahoo(String stockList) {
        String[] splitStock = stockList.split(",");
        for(String stock: splitStock) {
            log.debug("Test Scenario: "+stock);
            Map<String, Map<String, String>> stockValuesMap = yahooFinancePage.getStockValues(stock);

            //verifying the actual values
            //null check
            Assert.assertNotNull(stockValuesMap.get(stock).get("average"));
            Assert.assertNotNull(stockValuesMap.get(stock).get("low"));
            Assert.assertNotNull(stockValuesMap.get(stock).get("current"));
            Assert.assertNotNull(stockValuesMap.get(stock).get("high"));

        }
    }

    @AfterTest
    public void afterTest() {
        yahooFinancePage.closeBrowser();
        log.debug("Scenario execution Complete:");
    }
}


