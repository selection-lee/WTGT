package com.ssafy.wattagatta.domain.product.controller;

import static com.ssafy.wattagatta.global.utils.ApiUtils.success;

import com.ssafy.wattagatta.domain.product.response.GetAllProductResponse;
import com.ssafy.wattagatta.domain.product.service.ProductService;
import com.ssafy.wattagatta.global.utils.ApiUtils.ApiResult;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products")
    public ApiResult<List<GetAllProductResponse>> getAllProducts() {
        return success(productService.getAllProducts());
    }
}
