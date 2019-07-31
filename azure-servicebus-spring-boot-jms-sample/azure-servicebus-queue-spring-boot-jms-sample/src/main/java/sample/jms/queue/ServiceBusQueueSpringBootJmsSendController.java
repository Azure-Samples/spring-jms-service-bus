package sample.jms.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceBusQueueSpringBootJmsSendController {

    private static final String QUEUE_NAME = "testqueue";
    private static final Logger logger = LoggerFactory.getLogger(ServiceBusQueueSpringBootJmsSendController.class);


    @Autowired
    private JmsTemplate jmsTemplate;

    @PostMapping("/queue")
    public String postMessage(@RequestParam String message) {
        logger.info("Sending message");
        jmsTemplate.convertAndSend(QUEUE_NAME, new User(message));
        return message;
    }
}
