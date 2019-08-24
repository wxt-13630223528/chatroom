package com.bittech.java.client.entity;

import lombok.Data;

import java.io.InputStream;
import java.util.Set;

@Data
public class User {
    private Integer id;
    private String UserName;
    private String Password;
    private String brief;
    private Set<String> usernames;
}
