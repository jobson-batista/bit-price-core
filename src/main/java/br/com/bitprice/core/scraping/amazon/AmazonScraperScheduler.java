package br.com.bitprice.core.scraping.amazon;

import br.com.bitprice.core.model.Product;
import br.com.bitprice.core.service.ProductService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AmazonScraperScheduler {

    private final AmazonScraperService amazonScraperService;
    private final ProductService productService;

    public AmazonScraperScheduler(
            AmazonScraperService amazonScraperService,
            ProductService productService
    ) {
        this.amazonScraperService = amazonScraperService;
        this.productService = productService;
    }

    @Scheduled(cron = "0 0 04 * * *") // sec min h dayMon mon weekday
    public void runAmazonBestSellersScraper() {
        List<Product> products = amazonScraperService.findBestSellers();
        if (!products.isEmpty()) {
            productService.saveAll(products);
        }
    }
}
