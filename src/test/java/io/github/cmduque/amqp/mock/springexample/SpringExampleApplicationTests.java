package io.github.cmduque.amqp.mock.springexample;

import com.rabbitmq.client.AMQP;
import io.github.cmduque.amqp.mock.AMQPServerMock;
import io.github.cmduque.amqp.mock.dto.Message;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static io.github.cmduque.amqp.mock.dto.ServerConfig.defaultConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SpringExampleApplicationTests {

    AMQPServerMock server;

    @BeforeEach
    public void beforeMethod() {
        server = new AMQPServerMock(defaultConfig());
        server.start();
    }

    @Test
    void serviceMustPublishAMessageAfterWithReceivedPayload() throws Exception {
        String key = "book.received";
        CountDownLatch lockForMessages = server.getLockForMessages(key, 1);
        String queue = "arrived";
        String payload = "Spring Boot in Action";
        server.publish(new Message("", queue, new AMQP.BasicProperties.Builder().build(), payload.getBytes()));

        lockForMessages.await(10, TimeUnit.SECONDS);

        List<Message> receivedMessages = server.getAllReceivedMessages(key);
        assertEquals(1, receivedMessages.size());
        assertEquals("Received:" + payload, new String(receivedMessages.get(0).getBody()));
    }

    @AfterEach
    public void afterMethod() {
        server.stop();
    }
}