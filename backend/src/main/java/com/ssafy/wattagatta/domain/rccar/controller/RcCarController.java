package com.ssafy.wattagatta.domain.rccar.controller;

import com.ssafy.wattagatta.domain.rccar.response.GridInfoResponse;
import com.ssafy.wattagatta.domain.rccar.response.RcCarResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RcCarController {

    @GetMapping("/api/rccar/test")
    public GridInfoResponse testUnity() {
        RcCarResponse rcCar = new RcCarResponse(21.49, 3.77, 11.94);
        RcCarResponse rcCar2 = new RcCarResponse(2.0, 3.77, 3.0);
        GridInfoResponse coordinates = new GridInfoResponse(rcCar, rcCar2);
        return coordinates;
    }
}
