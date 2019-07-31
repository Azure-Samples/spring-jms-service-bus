package sample.jms.queue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class ServiceBusQueueSpringBootJmsSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceBusQueueSpringBootJmsSampleApplication.class, args);
    }

}
