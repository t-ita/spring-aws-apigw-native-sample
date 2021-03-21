package com.myexample.serverless.awsapigw.functions;

import com.myexample.serverless.awsapigw.functions.models.Greet;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.function.Function;

@Component
public class Greeting implements Function<Message<Greet>, Message<Greet>> {
    @Override
    public Message<Greet> apply(Message<Greet> greetMessage) {
        var greet = greetMessage.getPayload();

        var resultGreet = new Greet();
        resultGreet.setName("Spring Cloud Function Native");
        resultGreet.setMessage(String.format("%s,%s", greet.getMessage(), greet.getName()));

        var resultHeader = new MessageHeaders(new HashMap<>());

        return new Message<>() {
            @Override
            public Greet getPayload() {
                return resultGreet;
            }

            @Override
            public MessageHeaders getHeaders() {
                return resultHeader;
            }
        };
    }
}
