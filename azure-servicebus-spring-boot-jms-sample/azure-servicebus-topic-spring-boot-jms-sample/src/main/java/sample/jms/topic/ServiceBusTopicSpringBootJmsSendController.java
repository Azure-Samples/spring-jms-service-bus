package sample.jms.topic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceBusTopicSpringBootJmsSendController {

    private static final String TOPIC_NAME = "mytopic";
    private static final Logger logger = LoggerFactory.getLogger(ServiceBusTopicSpringBootJmsSendController.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    @PostMapping("/topic")
    public String postMessage(@RequestParam String message) {
        logger.info("Sending message");
        jmsTemplate.convertAndSend(TOPIC_NAME, new User(message));
        return message;
    }
}
