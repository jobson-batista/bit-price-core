package br.com.bitprice.core.model;

import br.com.bitprice.core.dto.ProductDTO;
import br.com.bitprice.core.enums.Category;
import br.com.bitprice.core.enums.SourcePlatform;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    private BigDecimal price;

    @Column(nullable = false)
    private String link;

    private String imageURL;

    private Category category;

    private SourcePlatform sourcePlatform;

    private String externalProductId;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "deleted")
    private boolean deleted = false;

    public ProductDTO toDTO() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setTitle(title);
        productDTO.setDescription(description);
        productDTO.setPrice(price);
        productDTO.setLink(link);
        productDTO.setImageURL(imageURL);
        productDTO.setCategory(category);
        productDTO.setUpdatedAt(updatedAt);
        productDTO.setCreatedAt(createdAt);
        return productDTO;
    }
}
