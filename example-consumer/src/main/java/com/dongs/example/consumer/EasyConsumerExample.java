package com.dongs.example.consumer;

import com.dongs.drpc.proxy.ServiceProxyFactory;
import com.dongs.example.common.model.User;
import com.dongs.example.common.service.UserService;

public class EasyConsumerExample {
    public static void main(String[] args) {


        // 通过rpc获取到userService的实例对象
        // 静态代理
//        UserService userService = new UserServiceProxy();

        for (int i = 0; i < 5; i++) {
            // 动态代理
            UserService userService = ServiceProxyFactory.getProxy(UserService.class);

            User user = new User();

            user.setName("dongs");

            User newUser = userService.getUser(user);

            if (newUser != null) {
                System.out.println("============================================" + newUser.getName());
            } else {
                System.out.println("newUser == null");
            }
        }
    }
}
