package com.ssafy.wattagatta.domain.order.service;

import com.ssafy.wattagatta.domain.member.entity.MemberEntity;
import com.ssafy.wattagatta.domain.member.repository.MemberRepository;
import com.ssafy.wattagatta.domain.order.entity.OrderEntity;
import com.ssafy.wattagatta.domain.order.repository.OrderRepository;
import com.ssafy.wattagatta.domain.order.request.AddOrderRequest;
import com.ssafy.wattagatta.global.exception.CustomException;
import com.ssafy.wattagatta.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    
    public void addOrder(AddOrderRequest request) {
        MemberEntity member = memberRepository.findById(1)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        orderRepository.save(OrderEntity.from(request, member));
    }
}
