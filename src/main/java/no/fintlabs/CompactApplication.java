package no.fintlabs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@ConfigurationPropertiesScan
//@ComponentScan(basePackages = {"no.fintlabs.kafka.entity", "no.fintlabs.kafka.common", "no.fintlabs.kafka"})
@SpringBootApplication
public class CompactApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompactApplication.class, args);
    }

}
