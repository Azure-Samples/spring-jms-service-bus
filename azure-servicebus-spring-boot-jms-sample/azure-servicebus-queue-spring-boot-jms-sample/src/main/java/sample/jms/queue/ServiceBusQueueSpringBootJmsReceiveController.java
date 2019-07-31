package sample.jms.queue;

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
public class ServiceBusQueueSpringBootJmsReceiveController {

    @Value("${spring.jms.servicebus.connection-string}")
    private String connectionString;

    @Bean
    public ConnectionFactory myConnectionFactory() {
        ConnectionStringBuilder connectionStringBuilder = new ConnectionStringBuilder(connectionString);
        String remoteUri = "amqps://" + connectionStringBuilder.getEndpoint().getHost();
        JmsConnectionFactory connectionFactory = new JmsConnectionFactory(remoteUri);
        connectionFactory.setRemoteURI(remoteUri);
        connectionFactory.setUsername(connectionStringBuilder.getSasKeyName());
        connectionFactory.setPassword(connectionStringBuilder.getSasKey());
        return new CachingConnectionFactory(connectionFactory);
    }

    @Bean
    public JmsListenerContainerFactory<?> myQueueFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory queueFactory = new DefaultJmsListenerContainerFactory();
        queueFactory.setConnectionFactory(connectionFactory);
        return queueFactory;
    }

    private static final String QUEUE_NAME = "testqueue";
    private final Logger logger = LoggerFactory.getLogger(ServiceBusQueueSpringBootJmsReceiveController.class);

    // Queue receiver
    @JmsListener(destination = QUEUE_NAME, containerFactory = "myQueueFactory")
    public void receiveQueueMessage(User user) {
        logger.info("Receiving message from queue: {}", user.getName());
    }

}
