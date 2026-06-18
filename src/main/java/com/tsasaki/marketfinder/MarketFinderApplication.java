package com.tsasaki.marketfinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.tsasaki.marketfinder.config.OpenAiProperties;

@SpringBootApplication
@EnableConfigurationProperties(OpenAiProperties.class)
public class MarketFinderApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarketFinderApplication.class, args);
    }

}
