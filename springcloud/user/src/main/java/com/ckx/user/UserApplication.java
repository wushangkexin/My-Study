package com.ckx.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaClient
@RestController
public class UserApplication {
    @Value("${password}")
    String username;

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
        System.out.println("启动用户微服务...");
    }

    @RequestMapping("/hi")
    public String hi(){
        return "hello world";
    }

    @RequestMapping("/getUsername")
    public String getUsername(){
        return username;
    }

}
