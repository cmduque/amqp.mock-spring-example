package io.github.cmduque.amqp.mock.springexample.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookArrivedListener implements MessageListener {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void onMessage(Message message) {
        String body = new String(message.getBody());
        rabbitTemplate.convertAndSend("book", "received", "Received:" + body);
    }
}