package com.dongs.examplespringbootconsumer.impl;

import com.dongs.dongsrpcspringbootstarter.annotation.RpcReference;
import com.dongs.example.common.model.User;
import com.dongs.example.common.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class ExampleServiceImpl {

    @RpcReference
    private UserService userService;


    public void test(){
        User user = new User();
        user.setName("dongs");
        User resultUser = userService.getUser(user);
        System.out.println(resultUser.getName());
    }
}
