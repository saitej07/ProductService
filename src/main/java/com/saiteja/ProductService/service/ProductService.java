package com.saiteja.ProductService.service;

import com.saiteja.ProductService.model.ProductRequest;
import com.saiteja.ProductService.model.ProductResponse;

public interface ProductService {

    long addProduct(ProductRequest productRequest);

    ProductResponse getProductById(long productId);

    void reduceQuantity(long productId, long quantity);
}
