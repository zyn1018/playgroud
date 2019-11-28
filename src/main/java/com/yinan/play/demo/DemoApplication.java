package com.yinan.play.demo;

import com.yinan.play.demo.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

/**
 * @author yinan
 */
@SpringBootApplication
@EnableRetry
public class DemoApplication {
    @Autowired
    private Config config;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        System.out.println(System.getProperty("qc-test-env"));
        System.out.println(Config.getTestEnv());
    }
}
