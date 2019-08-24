package com.bittech.java.client.server;




import com.bittech.java.util.CommUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 与服务端建立链接
 */
public class Connect2Server {
    private static final String IP;
    private static final int PORT;
    //缓存当前服务器所有在线的客户端信息
    private static Map<String,Socket> clients = new ConcurrentHashMap<>();
    //类加载时就初始化
    static {
        //获取配置文件
        Properties pros = CommUtils.loadProperties("socket.properties");
        IP = pros.getProperty("address");
        PORT = Integer.parseInt(pros.getProperty("port"));
    }
    private Socket client;
    private InputStream in;
    private OutputStream out;

    public Connect2Server() {
        try {
            client = new Socket(IP,PORT);
            in = client.getInputStream();
            out = client.getOutputStream();
        } catch (IOException e) {
            System.out.println("与服务器建立链接失败");
            e.printStackTrace();
        }
    }
    public InputStream getIn(){
        return in;
    }
    public OutputStream getOut(){
        return out;
    }
}
