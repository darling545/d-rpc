package com.dongs.example.common.service;

import com.dongs.example.common.model.User;

/**
 * 用户服务
 *
 * @author dongs
 */
public interface UserService {

    /**
     * 获取用户
     *
     * @param user
     * @return
     */
    User getUser(User user);


    /**
     * 获取默认值
     * @return
     */
    default short getNumber(){
        return 1;
    }

}
