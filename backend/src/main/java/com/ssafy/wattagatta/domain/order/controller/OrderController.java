package com.ssafy.wattagatta.domain.order.controller;

import static com.ssafy.wattagatta.global.utils.ApiUtils.success;

import com.ssafy.wattagatta.domain.order.request.AddOrderRequest;
import com.ssafy.wattagatta.domain.order.service.OrderService;
import com.ssafy.wattagatta.global.utils.ApiUtils.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders")
    public ApiResult<Boolean> addOrder(@RequestBody AddOrderRequest request) {
        orderService.addOrder(request);
        return success(true);
    }
}
