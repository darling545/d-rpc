package com.dongs.examplespringbootprovider.impl;

import com.dongs.dongsrpcspringbootstarter.annotation.RpcService;
import com.dongs.example.common.model.User;
import com.dongs.example.common.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RpcService
public class UserServiceImpl implements UserService {
    /**
     * @param user
     * @return
     */
    public User getUser(User user) {
        System.out.println("UserServiceImpl.getUser：" + user.getName());
        return user;
    }
}
