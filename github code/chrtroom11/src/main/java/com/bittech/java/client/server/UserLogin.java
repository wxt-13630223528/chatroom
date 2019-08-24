package com.bittech.java.client.server;

import com.bittech.java.client.dao.AccountDao;
import com.bittech.java.client.entity.User;
import com.bittech.java.util.CommUtils;
import com.bittech.java.vo.MessageVo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.util.Scanner;
import java.util.Set;

public class UserLogin {
    private JPanel UserLogin;
    private JPanel userPanel;
    private JTextField userNameText;
    private JPanel btnPanel;
    private JButton reButton;
    private JButton loginButton;
    private JPasswordField passwordText;
    AccountDao accountDao = new AccountDao();
// 登陆页面绑定类
    public UserLogin() {
        JFrame frame = new JFrame("用户登录");
        frame.setContentPane(UserLogin);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); //界面居中
        frame.pack();
        frame.setVisible(true);
        //注册按钮
        reButton.addActionListener(new ActionListener() {
            @Override
            //当动作事件产生，
            public void actionPerformed(ActionEvent e) {
                //弹出注册页面
                new UserReg();
            }
        });

        //登陆按钮
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //校验用户信息
                String userName = userNameText.getText();
                String password = String.valueOf(passwordText.getPassword());
                User user = accountDao.userLogin(userName,password);
                if(user!=null){
                    //成功，加载用户列表
                    JOptionPane.showMessageDialog(frame,"登陆成功","提示信息",JOptionPane.INFORMATION_MESSAGE);
                    frame.setVisible(false); //登陆成功，登陆页面不可见

                    //与服务器建立连接，将当前用户的用户名与密码发送到服务端
                    Connect2Server connect2Server = new Connect2Server();
                    MessageVo msg2Server = new MessageVo();
                    msg2Server.setType("1");  // 1表示注册到服务器缓存
                    msg2Server.setContent(userName);
                    //把对象变成json字符串发到服务器
                    Object json2Server = CommUtils.object2Json(msg2Server);
                    try {
                        PrintStream out = new PrintStream(connect2Server.getOut(),true,"UTF-8");
                        out.println(json2Server);//将当前用户的用户名以及它的信息发到了服务端
                        //读取服务端发回的所有在线用户信息，
                        Scanner in = new Scanner(connect2Server.getIn()); //获取输入流
                        if(in.hasNextLine()){
                            String msgFromServerstr = in.nextLine();
                            MessageVo msgFromServer =(MessageVo) CommUtils.json2Object(msgFromServerstr,MessageVo.class);
                            //将当前信息解成set集合
                            Set<String> users =(Set<String>) CommUtils.json2Object(msgFromServer.getContent(),Set.class);
                            System.out.println("所有在线用户为:"+users);

                            //加载用户列表界面
                            //将当前用户名，所有用户名、与服务器建立的链接
                            new FriendsList(userName,users,connect2Server);
                        }

                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();
                    }

                }else {
                    //失败，停留在当前的登陆页面，提示用户信息错误,
                    //提示框
                    JOptionPane.showMessageDialog(frame,"登陆失败！",
                            "错误信息",JOptionPane.ERROR_MESSAGE);

                }
            }
        });

    }

    public static void main(String[] args) {
        UserLogin userLogin = new UserLogin();
    }
}
