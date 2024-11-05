package com.ssafy.wattagatta.domain.product.controller;

import static com.ssafy.wattagatta.global.utils.ApiUtils.success;

import com.ssafy.wattagatta.domain.product.request.GetSpecificProductRequest;
import com.ssafy.wattagatta.domain.product.response.ProductResponse;
import com.ssafy.wattagatta.domain.product.service.ProductService;
import com.ssafy.wattagatta.global.utils.ApiUtils.ApiResult;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products")
    public ApiResult<List<ProductResponse>> getAllProducts() {
        return success(productService.getAllProducts());
    }

    @PostMapping("/products/date")
    public ApiResult<List<ProductResponse>> getSpecificProducts(@RequestBody GetSpecificProductRequest request) {
        return success(productService.getTodayProducts(request.date()));
    }
}
