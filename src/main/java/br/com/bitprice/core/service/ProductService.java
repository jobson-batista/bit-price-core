package br.com.bitprice.core.service;

import br.com.bitprice.core.dto.ProductDTO;
import br.com.bitprice.core.exception.NotFoundException;
import br.com.bitprice.core.exception.WebScrapingException;
import br.com.bitprice.core.model.Product;
import br.com.bitprice.core.repository.ProductRepository;
import br.com.bitprice.core.scraping.amazon.AmazonScraperService;
import br.com.bitprice.core.scraping.kabum.KabumScraperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final AmazonScraperService amazonScraperService;
    private final KabumScraperService kabumScraperService;

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final NotFoundException notFoundException = new NotFoundException("Product Not Found","Check Product the ID entered");

    ProductService(
            ProductRepository productRepository,
            AmazonScraperService amazonScraperService,
            KabumScraperService kabumScraperService
    ) {
        this.productRepository = productRepository;
        this.amazonScraperService = amazonScraperService;
        this.kabumScraperService = kabumScraperService;
    }

    public List<ProductDTO> findAll() {
        List<Product> products = this.productRepository.findByDeletedFalse();
        return fromProductListToProductDTOList(products);
//        return fromProductListToProductDTOList(productRepository.findAll());
    }

    public ProductDTO save(ProductDTO dto) {
        return productRepository.save(dto.toProduct()).toDTO();
    }

    public void saveAll(List<Product> productList) {
        productRepository.saveAll(productList);
    }

    public ProductDTO findById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isEmpty()) {
            throw notFoundException;
        }
        return product.orElse(null).toDTO();
    }

    public ProductDTO update(Long id, Product newProduct) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            if(newProduct.getTitle() != null && !newProduct.getTitle().isEmpty()) {
                product.get().setTitle(newProduct.getTitle());
            } else if(newProduct.getDescription() != null && !newProduct.getDescription().isEmpty()) {
                product.get().setDescription(newProduct.getDescription());
            } else if(newProduct.getPrice() != null && newProduct.getPrice().intValue() > 0) {
                product.get().setPrice(newProduct.getPrice());
            } else if(newProduct.getLink() != null && !newProduct.getLink().isEmpty()) {
                product.get().setLink(newProduct.getLink());
            }
        } else {
            throw notFoundException;
        }
        return productRepository.save(product.get()).toDTO();
    }

    public void delete(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            productRepository.delete(product.get());
        } else {
            throw notFoundException;
        }
    }

    public List<ProductDTO> listBestSellersAmazon() {
        try {
            List<Product> products = this.amazonScraperService.findBestSellers();
            products = productRepository.saveAll(products);
            logger.info("Amazon Best Sellers Products saved!");
            return fromProductListToProductDTOList(products);
        } catch (Exception e) {
            e.printStackTrace();
            throw new WebScrapingException();
        }
    }

    public List<ProductDTO> listBestSellersKabum() {
        try {
            List<Product> products = this.kabumScraperService.findBestSellers();
            products = productRepository.saveAll(products);
            logger.info("Kabum Best Sellers Products saved!");
            return fromProductListToProductDTOList(products);
        } catch (Exception e) {
            e.printStackTrace();
            throw new WebScrapingException();
        }
    }

    private List<Product> fromProductDTOListToProductList(List<ProductDTO> productDTOList) {
        List<Product> products = new ArrayList<>();
        for (ProductDTO productDTO : productDTOList) {
            products.add(productDTO.toProduct());
        }
        return products;
    }

    private List<ProductDTO> fromProductListToProductDTOList(List<Product> productList) {
        List<ProductDTO> products = new ArrayList<>();
        for (Product product : productList) {
            products.add(product.toDTO());
        }
        return products;
    }
}
