/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See LICENSE in the project root for
 * license information.
 */

package sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;
import org.apache.qpid.jms.JmsConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import javax.jms.ConnectionFactory;

@SpringBootApplication
@EnableJms
public class ServiceBusJMSTopicApplication {

    @Value("${spring.jms.servicebus.connection-string}")
    private String connectionString;

    @Value("${spring.jms.servicebus.topic-client-id}")
    private String clientId = "";

    @Value("${spring.jms.servicebus.idle-timeout}")
    private int idleTimeout;

    private static final String AMQP_URI_FORMAT = "amqps://%s?amqp.idleTimeout=%d";

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
    public JmsListenerContainerFactory<?> myTopicFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory topicFactory = new DefaultJmsListenerContainerFactory();
        topicFactory.setConnectionFactory(connectionFactory);
        topicFactory.setSubscriptionDurable(Boolean.TRUE);
        return topicFactory;
    }

    public static void main(String[] args) {
        SpringApplication.run(ServiceBusJMSTopicApplication.class, args);
    }

}
