package br.com.bitprice.core.scraping.amazon;

import br.com.bitprice.core.enums.Category;
import br.com.bitprice.core.enums.SourcePlatform;
import br.com.bitprice.core.exception.NotFoundException;
import br.com.bitprice.core.model.Product;
import br.com.bitprice.core.scraping.ScraperFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class AmazonScraperService implements ScraperFactory {

    @Value("${amazon.tagname}")
    private String tagName;

    private final String URL_BEST_SELLERS = "https://www.amazon.com.br/gp/bestsellers/videogames?pg=";

    private static final Logger logger = LoggerFactory.getLogger(AmazonScraperService.class);

    public List<Product> findBestSellers() {
        List<Product> products = new ArrayList<>();
        WebDriver driver = new ChromeDriver();
        ((JavascriptExecutor) driver).executeScript(
                "Object.defineProperty(navigator, 'webdriver', {get: () => undefined})"
        );
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36");
        options.setExperimentalOption("excludeSwitches", List.of("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("--start-maximized");
        options.addArguments("--headless=new"); // "new" reduz detecção no Chrome mais novo

        try {
            int pages = 2;
            for (int i = 1; i <= pages; i++) {

                driver.get(URL_BEST_SELLERS + i);

                scroll(driver);

                List<WebElement> itens = driver.findElements(By.id("gridItemRoot"));

                for (WebElement item : itens) {
                    Product product = new Product();
                    String title = item.findElement(By.tagName("img")).getAttribute("alt");
                    String link = item.findElement(By.tagName("a")).getAttribute("href");
                    String imageUrl = item.findElement(By.tagName("img")).getAttribute("src");
                    String price = "";
                    for (WebElement span : item.findElements(By.tagName("span"))) {
                        if(span.getText().contains("R$")) {
                            price = span.getText().replace("R$","").replace(".", "").replace(",", ".").trim();
                        }
                    }
                    product.setTitle(title);
                    product.setLink(link + "&" + this.tagName);
                    product.setImageURL(imageUrl != null ? imageUrl.trim() : null);
                    product.setCategory(Category.GAMES);
                    product.setSourcePlatform(SourcePlatform.AMAZON);
                    product.setExternalProductId(getExternalProductId(item));

                    if(!price.isEmpty()) {
                        product.setPrice(new BigDecimal(price));
                    }
                    products.add(product);
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            driver.quit();
        }

        return products;
    }

    private String getExternalProductId(WebElement item) {
        String asin = "";
        List<WebElement> divs = item.findElements(By.cssSelector("div[data-asin]"));
        for (WebElement div : divs) {
            asin = div.getAttribute("data-asin");
            if (asin != null && !asin.isEmpty()) {
                logger.info("ASIN encontrado: " + asin);
            }
        }
        return asin;
    }

    private void scroll(WebDriver driver) throws InterruptedException {

        Thread.sleep(5000);

        JavascriptExecutor js = (JavascriptExecutor) driver;

        for (int k = 0; k < 10; k++) {
            js.executeScript("window.scrollBy(0,800)");
            Thread.sleep(1000 + new Random().nextInt(1000)); // 1000ms a 2000ms
        }

        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        Thread.sleep(5000);
    }
}
