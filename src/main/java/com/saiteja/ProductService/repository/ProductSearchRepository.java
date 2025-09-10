package com.saiteja.ProductService.repository;

import com.saiteja.ProductService.entity.Product;
import com.saiteja.ProductService.entity.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSearchRepository extends ElasticsearchRepository<ProductDocument, Long> {
}
