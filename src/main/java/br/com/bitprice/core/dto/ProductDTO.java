package br.com.bitprice.core.dto;

import br.com.bitprice.core.enums.Category;
import br.com.bitprice.core.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    public ProductDTO(String title, String link) {
        this.title = title;
        this.link = link;
    }

    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private String link;
    private String imageURL;
    private Category category;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    public Product toProduct() {
        Product product = new Product();
        product.setId(this.id);
        product.setTitle(this.title);
        product.setDescription(this.description);
        product.setPrice(this.price);
        product.setLink(this.link);
        product.setImageURL(this.imageURL);
        product.setCategory(this.category);
        product.setUpdatedAt(this.updatedAt);
        product.setCreatedAt(this.createdAt);
        return product;
    }
}
