package com.example.guliMall.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author cc
 * @date 2023年02月18日 13:05
 */
@SpringBootApplication
@EnableFeignClients(basePackages = "com.example.guliMall.product.feign")
@EnableDiscoveryClient
@MapperScan("com.example.guliMall.product.dao")
public class ProduceApplication {
     public static void main(String[] args) {
           SpringApplication.run(ProduceApplication.class, args);
      }

}
