package com.gvart.amazonrater;

import com.gvart.amazonrater.config.AmazonRateProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AmazonRateProperties.class)
public class AmazonRaterApplication {

  public static void main(String[] args) {
    SpringApplication.run(AmazonRaterApplication.class, args);
  }
}
