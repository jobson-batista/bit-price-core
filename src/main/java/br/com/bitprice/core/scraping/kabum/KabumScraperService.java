package br.com.bitprice.core.scraping.kabum;

import br.com.bitprice.core.enums.Category;
import br.com.bitprice.core.enums.SourcePlatform;
import br.com.bitprice.core.model.Product;
import br.com.bitprice.core.scraping.ScraperFactory;
import br.com.bitprice.core.scraping.config.JavascriptExecutorConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class KabumScraperService implements ScraperFactory {

    private static final Logger logger = LoggerFactory.getLogger(KabumScraperService.class);

    private final JavascriptExecutorConfig executor = new  JavascriptExecutorConfig();

    public List<Product> findBestSellers() {
        List<Product> products = new ArrayList<>();
        WebDriver driver = new ChromeDriver();
        setupDriver(driver);

        try {
            String URL_BEST_SELLERS = "https://www.kabum.com.br/gamer?page_number=1&page_size=100&facet_filters=&sort=most_searched";
            driver.get(URL_BEST_SELLERS);

            scroll(driver);

            List<WebElement> items = driver.findElement(By.tagName("main")).findElements(By.cssSelector("article.productCard"));

            for (WebElement item : items) {
                Product product = new Product();
                String URL_BASE = "https://www.kabum.com.br";
                String link = URL_BASE + item.findElement(By.tagName("a")).getDomAttribute("href");
                String imageUrl = item.findElement(By.tagName("img")).getDomAttribute("src");
                String title = item.findElement(By.cssSelector("span.nameCard")).getText();
                String priceString = item.findElement(By.cssSelector("span.priceCard")).getText()
                        .replace("R$","").replace(".", "").replace(",", ".").trim();
                BigDecimal price = BigDecimal.ZERO;
                if (!priceString.isEmpty()) {
                    price = new BigDecimal(priceString);
                }

                String externalProductId = item.findElement(By.tagName("a")).getDomAttribute("data-smarthintproductid");
                if (externalProductId != null && !externalProductId.isEmpty()) {
                    product.setExternalProductId(externalProductId.trim());
                }
                product.setTitle(title);
                product.setLink(link);
                product.setImageURL(imageUrl);
                product.setPrice(price);
                product.setCategory(Category.GAMES);
                product.setSourcePlatform(SourcePlatform.KABUM);

                logger.info("Kabum Product: {}", product.getLink());
                products.add(product);
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            driver.quit();
        }

        return products;
    }

    private void setupDriver(WebDriver driver){
        executor.JSExecutor((JavascriptExecutor) driver);
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
