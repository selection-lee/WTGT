package com.ssafy.wattagatta.domain.product.service;

import com.ssafy.wattagatta.domain.product.entity.ProductEntity;
import com.ssafy.wattagatta.domain.product.repository.ProductRepository;
import com.ssafy.wattagatta.domain.product.response.GetAllProductResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<GetAllProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductEntity::toResponse)
                .toList();
    }
}
