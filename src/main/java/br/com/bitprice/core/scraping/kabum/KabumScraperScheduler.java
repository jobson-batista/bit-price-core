package br.com.bitprice.core.scraping.kabum;

import br.com.bitprice.core.model.Product;
import br.com.bitprice.core.service.ProductService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KabumScraperScheduler {

    private final KabumScraperService kabumScraperService;
    private final ProductService productService;

    public KabumScraperScheduler(
            KabumScraperService kabumScraperService,
            ProductService productService
    ) {
        this.kabumScraperService = kabumScraperService;
        this.productService = productService;
    }

    @Scheduled(cron = "0 20 04 * * *") // sec min h dayMon mon weekday
    public void runAmazonBestSellersScraper() {
        List<Product> products = kabumScraperService.findBestSellers();
        if (!products.isEmpty()) {
            productService.saveAll(products);
        }
    }
}
