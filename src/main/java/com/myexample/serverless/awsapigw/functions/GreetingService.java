package com.myexample.serverless.awsapigw.functions;

import com.myexample.serverless.awsapigw.functions.models.Greet;
import org.springframework.stereotype.Service;

@Service
public class GreetingService {

    public Greet greeting(Greet greet) {
        var resultGreet = new Greet();
        resultGreet.setName("Spring Cloud Function Native");
        resultGreet.setMessage(String.format("%s,%s", greet.getMessage(), greet.getName()));
        return resultGreet;
    }

}
