package com.saiteja.ProductService.service;

import com.saiteja.ProductService.entity.Product;
import com.saiteja.ProductService.entity.ProductDocument;
import com.saiteja.ProductService.exception.ProductServiceCustomException;
import com.saiteja.ProductService.model.ProductRequest;
import com.saiteja.ProductService.model.ProductResponse;
import com.saiteja.ProductService.repository.ProductRepository;
import com.saiteja.ProductService.repository.ProductSearchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService{

    @Autowired
    ProductRepository productRepository;

    @Autowired
    private ProductSearchRepository productSearchRepository;

    @Override
    public long addProduct(ProductRequest productRequest) {
        log.info("Adding Product..");

        // Save product to MySQL
        Product product = Product.builder()
                .productName(productRequest.getName())
                .price(productRequest.getPrice())
                .quantity(productRequest.getQuantity())
                .build();
        log.info("Product Created..");
        productRepository.save(product);

        try {
            ProductDocument productDocument = ProductDocument.builder()
                    .productId(product.getProductId())
                    .productName(product.getProductName())
                    .price(product.getPrice())
                    .quantity(product.getQuantity())
                    .build();

            productSearchRepository.save(productDocument);
            log.info("Product indexed in Elasticsearch: {}", productDocument);
        } catch (Exception e) {
            log.error("Failed to index product in Elasticsearch", e);
        }
        return product.getProductId();
    }

    @Override
    public ProductResponse getProductById(long productId) {
        log.info("Get the product for productId: {}", productId);
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductServiceCustomException("Product with the give id not found", "PRODUCT_NOT_FOUND"));

        ProductResponse productResponse = new ProductResponse();
        BeanUtils.copyProperties(product, productResponse);
        return productResponse;
    }

    @Override
    public void reduceQuantity(long productId, long quantity) {
        log.info("Reduce Quantity {} for Id: {}", productId, quantity);

        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductServiceCustomException("Product with the give id not found", "PRODUCT_NOT_FOUND"));

        if(product.getQuantity() < quantity) {
            throw new ProductServiceCustomException("Product does not have sufficient Quantity", "INSUFFICIENT_QUANTITY");
        }

        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
        log.info("Product Quantity updated successfully..");

        // Update Elasticsearch
        try {
            ProductDocument productDocument = ProductDocument.builder()
                    .productId(product.getProductId())
                    .productName(product.getProductName())
                    .price(product.getPrice())
                    .quantity(product.getQuantity())
                    .build();

            productSearchRepository.save(productDocument);
            log.info("Product quantity updated in Elasticsearch: {}", productDocument);
        } catch (Exception e) {
            log.error("Failed to update product in Elasticsearch", e);
        }
    }
}
