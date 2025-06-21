package br.com.bitprice.core.service;

import br.com.bitprice.core.enums.Category;
import br.com.bitprice.core.exeption.NotFoundException;
import br.com.bitprice.core.model.Product;
import br.com.bitprice.core.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final NotFoundException notFoundException = new NotFoundException("Product Not Found","Check Product the ID entered");

    ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product save(Product product) {
        product.setDeleted(false);
        return productRepository.save(product);
    }

    public Product findById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if(product.isEmpty()) {
            throw notFoundException;
        }
        return product.orElse(null);
    }

    public Product update(Long id, Product newProduct) {
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
        return productRepository.save(product.get());
    }

    public void delete(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            productRepository.delete(product.get());
        } else {
            throw notFoundException;
        }
    }
}
