package com.example.bssm_dev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableFeignClients(basePackages = "com.example.bssm_dev.global.feign")
public class BssmDevApplication {

    public static void main(String[] args) {
        SpringApplication.run(BssmDevApplication.class, args);
    }

}
