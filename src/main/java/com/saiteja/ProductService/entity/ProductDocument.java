package com.saiteja.ProductService.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDocument {

    @Id
    private long productId;
    private String productName;
    private long price;
    private long quantity;
}
