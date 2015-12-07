package spring.cloud.netflix.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Cloud Eureka Launcher
 */
@Configuration
@EnableAutoConfiguration
@EnableEurekaClient
@EnableEurekaServer
public class EurekaLauncher {
  public static void main(String args[]) {
    SpringApplication.run(EurekaLauncher.class, args);
  }
}
