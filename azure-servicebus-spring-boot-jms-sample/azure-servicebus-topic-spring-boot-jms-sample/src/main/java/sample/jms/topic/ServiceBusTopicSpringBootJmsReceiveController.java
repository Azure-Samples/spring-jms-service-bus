package sample.jms.topic;

import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.stereotype.Component;

import javax.jms.ConnectionFactory;

@Component
public class ServiceBusTopicSpringBootJmsReceiveController {

    @Value("${spring.jms.servicebus.connection-string}")
    private String connectionString;

    @Value("${spring.jms.servicebus.topic-client-id}")
    private String clientId;

    @Bean
    public ConnectionFactory myConnectionFactory() {
        ConnectionStringBuilder connectionStringBuilder = new ConnectionStringBuilder(connectionString);
        String remoteUri = "amqps://" + connectionStringBuilder.getEndpoint().getHost();
        JmsConnectionFactory connectionFactory = new JmsConnectionFactory(remoteUri);
        connectionFactory.setRemoteURI(remoteUri);
        connectionFactory.setClientID(clientId);
        connectionFactory.setUsername(connectionStringBuilder.getSasKeyName());
        connectionFactory.setPassword(connectionStringBuilder.getSasKey());
        return new CachingConnectionFactory(connectionFactory);
    }

    @Bean
    public JmsListenerContainerFactory<?> myTopicFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory topicFactory = new DefaultJmsListenerContainerFactory();
        topicFactory.setConnectionFactory(connectionFactory);
        topicFactory.setSubscriptionDurable(Boolean.TRUE);
        return topicFactory;
    }

    private static final String TOPIC_NAME = "mytopic";
    private static final String SUBSCRIPTION_NAME = "S1";
    private final Logger logger = LoggerFactory.getLogger(ServiceBusTopicSpringBootJmsReceiveController.class);

    @JmsListener(destination = TOPIC_NAME, containerFactory = "myTopicFactory", subscription = SUBSCRIPTION_NAME)
    public void receiveMessage(User user) {
        logger.info("Received message from topic: {}", user.getName());
    }
}
