package br.com.bitprice.core.scraping;

import br.com.bitprice.core.model.Product;

import java.util.List;

public interface ScraperFactory {

    List<Product> findBestSellers();
}
