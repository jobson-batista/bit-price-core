package br.com.bitprice.core.controller;

import br.com.bitprice.core.dto.ProductDTO;
import br.com.bitprice.core.model.Product;
import br.com.bitprice.core.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductDTO> save(@RequestBody  ProductDTO product) {
        return ResponseEntity.ok(productService.save(product));
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> findAll() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody Product product) {
        return ResponseEntity.ok(productService.update(id, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/amazon/bestsellers")
    public ResponseEntity<List<ProductDTO>> findAllBestSellersAmazon() {
        return ResponseEntity.ok(productService.listBestSellersAmazon());
    }

    @GetMapping("/kabum/bestsellers")
    public ResponseEntity<List<ProductDTO>> findAllBestSellersKabum() {
        return ResponseEntity.ok(productService.listBestSellersKabum());
    }
}
