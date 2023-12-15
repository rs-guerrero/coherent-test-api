package com.coherent.test.task;

import com.coherent.test.task.infrastructure.config.ShutdownListener;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@OpenAPIDefinition(servers = {@Server(url = "/coherent-hotel", description = "Default Server URL")})
@SpringBootApplication
public class CoherentTestTaskApplication {

    public static void main(String[] args) {

        SpringApplication.run(CoherentTestTaskApplication.class, args);
    }

}
