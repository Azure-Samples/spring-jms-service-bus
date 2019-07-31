package sample.jms.topic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class ServiceBusTopicSpringBootJmsSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceBusTopicSpringBootJmsSampleApplication.class, args);
    }

}