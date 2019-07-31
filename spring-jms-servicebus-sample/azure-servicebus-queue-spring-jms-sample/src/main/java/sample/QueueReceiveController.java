/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */

package sample;

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
public class QueueReceiveController {

    @Value("${spring.jms.servicebus.connection-string}")
    private String connectionString;

    @Value("${spring.jms.servicebus.topic-client-id}")
    private String clientId = "";

    @Value("${spring.jms.servicebus.idle-timeout}")
    private int idleTimeout;

    private static final String AMQP_URI_FORMAT = "amqps://%s?amqp.idleTimeout=%d";

    private static final String QUEUE_NAME = "que001";

    private final Logger logger = LoggerFactory.getLogger(QueueReceiveController.class);

    @Bean
    public ConnectionFactory myConnectionFactory() {
        ServiceBusKey serviceBusKey = ConnectionStringResolver.getServiceBusKey(connectionString);
        String host = serviceBusKey.getHost();
        String sasKeyName = serviceBusKey.getSharedAccessKeyName();
        String sasKey = serviceBusKey.getSharedAccessKey();

        String remoteUri = String.format(AMQP_URI_FORMAT, host, idleTimeout);
        JmsConnectionFactory jmsConnectionFactory = new JmsConnectionFactory();
        jmsConnectionFactory.setRemoteURI(remoteUri);
        jmsConnectionFactory.setClientID(clientId);
        jmsConnectionFactory.setUsername(sasKeyName);
        jmsConnectionFactory.setPassword(sasKey);
        return new CachingConnectionFactory(jmsConnectionFactory);
    }

    @Bean
    public JmsListenerContainerFactory<?> myQueueFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory queueFactory = new DefaultJmsListenerContainerFactory();
        queueFactory.setConnectionFactory(connectionFactory);
        return queueFactory;
    }

    @JmsListener(destination = QUEUE_NAME, containerFactory = "myQueueFactory")
    public void receiveQueueMessage(User user) {
        logger.info("Receiving message from queue: {}", user.getName());
    }

}
