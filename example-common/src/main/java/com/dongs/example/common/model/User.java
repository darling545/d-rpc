package com.dongs.example.common.model;

import java.io.Serializable;

/**
 * 用户
 *
 * @author dongs
 */
public class User implements Serializable {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
