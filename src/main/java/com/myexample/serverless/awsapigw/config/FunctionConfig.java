package com.myexample.serverless.awsapigw.config;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myexample.serverless.awsapigw.functions.GreetingService;
import com.myexample.serverless.awsapigw.functions.models.Greet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.function.Function;

@Configuration
public class FunctionConfig {

    final GreetingService greetingService;

    public FunctionConfig(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @Bean
    public Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> greeting() {
        return requestEvent -> {
            APIGatewayProxyResponseEvent responseEvent = null;
            try {
                ObjectMapper mapper = new ObjectMapper();

                var input = mapper.readValue(requestEvent.getBody(), Greet.class);
                var output = mapper.writeValueAsString(greetingService.greeting(input));

                responseEvent = new APIGatewayProxyResponseEvent()
                        .withStatusCode(HttpStatus.OK.value())
                        .withHeaders(new HashMap<>())
                        .withBody(output);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
            return responseEvent;
        };
    }


}
