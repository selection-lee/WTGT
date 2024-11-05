package com.ssafy.wattagatta.domain.product.service;

import com.ssafy.wattagatta.domain.product.entity.ProductEntity;
import com.ssafy.wattagatta.domain.product.repository.ProductRepository;
import com.ssafy.wattagatta.domain.product.response.ProductResponse;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductEntity::toResponse)
                .toList();
    }

    public List<ProductResponse> getTodayProducts(LocalDate date) {
        return productRepository.findByExpectedArrivalDate(date)
                .stream()
                .map(ProductEntity::toResponse)
                .toList();
    }
}
