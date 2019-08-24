package com.bittech.java.client.server;

import com.bittech.java.client.dao.AccountDao;
import com.bittech.java.client.entity.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserReg {
    private JPanel UserRegPanel;
    private JTextField UserNameText;
    private JPasswordField passwordText;
    private JTextField briefText;
    private JButton regBtn;
    AccountDao accountDao = new AccountDao();

    // 通过其他操作调用弹出来的注册窗口，放在构造方法，如果在主方法加载时就会出来，
    //不能单独启动注册界面，必须通过其他调用产生对象
   public UserReg(){
       JFrame frame = new JFrame("用户的注册");
       frame.setContentPane(UserRegPanel);
       //窗口关了主方法停止
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       //设置框架位置
       frame.setLocationRelativeTo(null);
       frame.pack();
       frame.setVisible(true);

       //点击注册按钮将信息持久化到数据库中，成功弹出提示框
       regBtn.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               //获取用户输入的注册信息
               String userName = UserNameText.getText();
               String password = String.valueOf(passwordText.getPassword());
               String brief = briefText.getText();
               //将输入信息包装为User类，保存到数据库中
               User user = new User();
               user.setUserName(userName);
               user.setPassword(password);
               user.setBrief(brief);
               //调用dao对象
               if(accountDao.userReg(user)){
                   //
                   //弹出提示框
                   //JOptionPane.INFORMATION_MESSAGE 成功提示信息图标
                   JOptionPane.showMessageDialog(frame,"注册成功！",
                           "提示信息",JOptionPane.INFORMATION_MESSAGE);
                   frame.setVisible(false);//注册成功，返回主页面，当前页面不保留，不可见
               }else {
                   JOptionPane.showMessageDialog(frame,"注册失败！",
                           "提示信息",JOptionPane.ERROR_MESSAGE);
               }

           }
       });
   }
}
