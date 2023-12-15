package com.coherent.test.task.infrastructure.config;

import com.coherent.test.task.domain.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ShutdownListener implements DisposableBean {

    private final ReservationService service;

    @Override
    public void destroy() throws Exception {
        log.info("just before to stop...");
        service.saveDataInDB();

    }
}
