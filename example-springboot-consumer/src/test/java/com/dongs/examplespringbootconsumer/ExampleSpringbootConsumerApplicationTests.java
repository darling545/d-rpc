package com.dongs.examplespringbootconsumer;

import com.dongs.example.common.model.User;
import com.dongs.examplespringbootconsumer.impl.ExampleServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ExampleSpringbootConsumerApplicationTests {

    @Resource
    private ExampleServiceImpl exampleService;
    @Test
    void contextLoads() {
        exampleService.test();
    }

}
