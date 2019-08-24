package com.bittech.java.util;

import com.bittech.java.client.entity.User;
import com.bittech.java.client.server.UserReg;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.*;

public class CommUtilsTest {

    @Test
    public void loadProperties() {
        String fileName = "datasource.properties";
        Properties properties = CommUtils.loadProperties(fileName);
        Assert.assertNotNull(properties); //断言，认为它不为空
    }

    @Test
    public void object2Json() {
        User user = new User();
        user.setId(1);
        user.setUserName("zhang ");
        user.setPassword("123");
        user.setBrief("帅");
        Set<String> strings = new HashSet<>();
        strings.add("test1");
        strings.add("test2");
        strings.add("test3");
        user.setUsernames(strings);
        String str = CommUtils.object2Json(user);
        System.out.println(str);
    }

@Test
    public void json2Object() {
       String jsonstr = "{\"id\":1,\"UserName\":\"zhang \",\"Password\":\"123\",\"brief\":\"帅\",\"usernames\":[\"test2\",\"test3\",\"test1\"]}";
       User user =(User) CommUtils.json2Object(jsonstr,User.class);
       System.out.println(user.getUsernames());
    }
}