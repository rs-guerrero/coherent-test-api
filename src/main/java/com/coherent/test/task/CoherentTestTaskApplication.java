package com.coherent.test.task;

import com.coherent.test.task.domain.service.ReservationService;
import com.coherent.test.task.infrastructure.config.ShutdownListener;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@OpenAPIDefinition(servers = {@Server(url = "/coherent-hotel", description = "Default Server URL")})
@SpringBootApplication
public class CoherentTestTaskApplication {

    public static void main(String[] args) {

        SpringApplication.run(CoherentTestTaskApplication.class, args);
    }

//    @Autowired
//    private ReservationService service;
//
//    @PreDestroy
//    public void onExit() {
//        log.info("###STOPing###");
//
//        log.info("###STOP FROM THE LIFECYCLE###");
//    }

}
