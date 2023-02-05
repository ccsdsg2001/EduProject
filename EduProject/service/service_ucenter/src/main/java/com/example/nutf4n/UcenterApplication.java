package com.example.nutf4n;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author cc
 * @date 2023年01月28日 19:24
 */
@SpringBootApplication
@ComponentScan({"com.example"})
@MapperScan("com.example.nutf4n.mapper")
@EnableDiscoveryClient
public class UcenterApplication {
     public static void main(String[] args) {
           SpringApplication.run(UcenterApplication.class, args);
      }

}
