package com.bittech.java.server;

import com.bittech.java.util.CommUtils;
import com.bittech.java.vo.MessageVo;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 聊天室服务端
 */
public class MultiThreadServer {
    private static final String IP;
    private static final int PORT;
    //缓存当前服务器所有在线的客户端信息
    private static Map<String,Socket> clients = new ConcurrentHashMap<>();
    //缓存当前服务器注册的所有群名称以及群好友
    private static Map<String,Set<String>> groups;
    //类加载时就初始化
    static {
        //获取配置文件
        Properties pros = CommUtils.loadProperties("socket.properties");
        IP = pros.getProperty("address");
        PORT = Integer.parseInt(pros.getProperty("port"));
    }
    private static class ExecuteClient implements Runnable{
        private Socket client; //客户端是Socket类
        private Scanner in;
        private PrintStream out;
        public ExecuteClient(Socket client){
            this.client = client;
            try {
                this.in = new Scanner(client.getInputStream());
                this.out = new PrintStream(client.getOutputStream(),true,"UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //接受客户端发来信息
        @Override
        public void run() {
            while (true) if (in.hasNextLine()) {
                //将客户端发来的信息，反序列化为messageVO类的对象
                String josnStrFromClient = in.nextLine();
                MessageVo msgFromClient = (MessageVo) CommUtils.json2Object(josnStrFromClient, MessageVo.class);
                //根据type服务器直到需要干什么
                if (msgFromClient.getType().equals("1")) {
                    //新用户注册到服务端
                    String userName = msgFromClient.getContent();
                    //将当前在线的所有用户用户名，发回客户端，就是加载用户列表
                    MessageVo msg2Client = new MessageVo();
                    msg2Client.setType("1");
                    //所有在线用户名在client 的key中，将它变成Json对象扔到里面
                    msg2Client.setContent(CommUtils.object2Json(clients.keySet()));
                    out.println(CommUtils.object2Json(msg2Client)); //发回客户端
                    //将新上线的用户信息发回给当前已在线的所有用户
                    sendUserLogin("newLogin:" + userName);
                    //将当前新用户注册到服务器缓存
                    clients.put(userName, client);
                    System.out.println(userName + "上线了！");
                    System.out.println("当前聊天室共有" + clients.size() + "人");

                }
                //用户私聊
                //type：2
                //content：myName - msg
                //to：friendName
                else if (msgFromClient.getType().equals("2")) {
                    String senderName = msgFromClient.getContent().split("-")[0];
                    String msg = msgFromClient.getContent().split("-")[1];
                    String friendName = msgFromClient.getTo();
                    Socket clientSocket = clients.get(friendName);
                    try {
                        //调用输出流发送
                        PrintStream out = new PrintStream(clientSocket.getOutputStream(), true, "UTF-8");
                        MessageVo msg2Client = new MessageVo();
                        msg2Client.setType("2");
                        msg2Client.setContent(msg2Client.getContent());
                        System.out.println("收到私聊信息，内容为" + msgFromClient.getContent());
                        out.println(CommUtils.object2Json(msg2Client));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else if (msgFromClient.getType().equals("3")) {
                    //注册群
                    String groupName = msgFromClient.getContent();
                    //该群成员,字符串变成set集合
                    Set<String> friends = (Set<String>)CommUtils.json2Object(msgFromClient.getTo(), Set.class);
                    groups.put(groupName,friends);
                    System.out.println("有新的群注册成功，群名称为"+groupName+",一共有"+groups.size()+"个群");
                }else if (msgFromClient.getType().equals("4")) {  //客户端发来的消息
                    //群聊信息
                    String groupName = msgFromClient.getTo();
                    //group Map缓存包含群名，和群成员
                    Set<String> names = groups.get(groupName);
                    //遍历name，取value，即好友列表
                    Iterator<String> iterator = names.iterator();
                    while (iterator.hasNext()){
                        String socketName = iterator.next(); // 取出相应name的Socket输出
                        Socket client = clients.get(socketName);
                        try {
                            PrintStream out = new PrintStream(client.getOutputStream(),true,"UTF-8");
                            //取出客户端发来的群信息进行转发
                            MessageVo messageVo = new MessageVo();
                            messageVo.setType("4");
                            messageVo.setContent(msgFromClient.getContent());

                            //群名 -[好友]
                            messageVo.setTo(groupName+"-"+CommUtils.object2Json(names));
                            out.println(CommUtils.object2Json(messageVo));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        /**
         * 向所有在线用户发送新用户上线信息
         * @param msg
         */
        private void sendUserLogin(String msg){
            for(Map.Entry<String,Socket> entry :clients.entrySet()){
                Socket socket = entry.getValue();
                try {
                    PrintStream out = new PrintStream(socket.getOutputStream(),true,"UTF-8");
                    out.println(msg); //将信息发给所有在线用户
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }
    }
    public static void main(String[] args)throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        //线程池创建线程，目前写的最大支持50个并发
        ExecutorService executors = Executors.newFixedThreadPool(50);
        for(int i = 0;i<50 ;i++){
            System.out.println("等待客户连接...");
            //具体连接交给线程处理，下一次循环等新的客户
            Socket client = serverSocket.accept();
            //当accept继续往下执行，说明已经有一个客户端连接
            System.out.println("有新的连接，端口号为"+client.getPort());
            //提交任务，交给子线程去处理，服务器侦听新的客户端连接。
            executors.submit(new ExecuteClient(client));

        }
    }
}
