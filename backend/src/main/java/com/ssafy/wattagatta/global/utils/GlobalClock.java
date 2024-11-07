package com.ssafy.wattagatta.global.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Getter
@Configuration
@EnableScheduling
public class GlobalClock {
    private int globalTime = 0;

    @Scheduled(fixedRate = 1000)
    public void incrementGlobalTime() {
        globalTime++;
        log.info("Global Time : {}", globalTime);
    }
}

