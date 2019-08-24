package com.bittech.java.client.server;

import com.bittech.java.util.CommUtils;
import com.bittech.java.vo.MessageVo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CreateGroupGUI {
    private JPanel createGroupPanel;
    private JPanel friendLablePanel;
    private JCheckBox checkBox1;
    private JTextField groupNameText;
    private JButton confromButton;

    private String myName;
    //在线好友
    private Set<String> friends;
    private Connect2Server connect2Server;
    private  FriendsList friendsList;
    public CreateGroupGUI(String myName, Set<String> friends,Connect2Server connect2Server
                               ,FriendsList friendsList) {

        this.myName = myName;
        this.friends = friends;
        this.connect2Server = connect2Server;
        this.friendsList = friendsList;
        JFrame frame = new JFrame("创建群组");
        frame.setContentPane(createGroupPanel);
        frame.setSize(400,300);
        frame.setLocationRelativeTo(null);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        //将在线好友以及checkBox展示到界面中
        friendLablePanel.setLayout(new BoxLayout(friendLablePanel,BoxLayout.Y_AXIS));//纵向展示
        //盒子大小
        JCheckBox[] checkBoxes = new JCheckBox[friends.size()];
        //遍历Set
        Iterator<String> iterator = friends.iterator();
        int i = 0;
        while (iterator.hasNext()){
            String lableName = iterator.next();
            checkBoxes[i] = new JCheckBox(lableName);
            friendLablePanel.add(checkBoxes[i]);
            i++;
        }
         friendLablePanel.revalidate();
        //点击提交按钮提交信息到服务端
        confromButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //1） 判断哪些好友选中加入群聊,选中的放在Set中，并且要添加自己
                Set<String> selectedFriends = new HashSet<>(); //集合放选中的好友
                //checkBox 父类JComponent, 获取他的所有组建
               Component[] comps =  friendLablePanel.getComponents();
                //遍历组建找到哪些被选中，Component 包含所有组建 ，所以需要强转，只找checkbox
                for(Component comp:comps){
                    JCheckBox checkBox = (JCheckBox) comp;
                    if(checkBox.isSelected()){
                        String lableName = checkBox.getText();
                        selectedFriends.add(lableName);
                    }
                }
                selectedFriends.add(myName);
               //2） 获取群名输入框输入的群名称
                String groupName = groupNameText.getText();
               // 3） 将群名+选中好友信息发送到服务器（序列化发送）
                //type：3
               // content：groupName
                //to：[user1， user2，user3...]
                MessageVo messageVo = new MessageVo();
                messageVo.setType("3");
                messageVo.setContent(groupName);
                //set集合中的好友 序列化成json字符串
                messageVo.setTo(CommUtils.object2Json(selectedFriends));
                try {
                    PrintStream out = new PrintStream(connect2Server.getOut(),true,"UTF-8");
                    out.println(CommUtils.object2Json(messageVo));
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                //将当前创建群的界面隐藏，刷新好友列表界面的群列表
                frame.setVisible(false);
                //addGroupINfo
                //loadGrou
                friendsList.addGroup(groupName,selectedFriends);
                friendsList.loadGroupList();
            }
        });
    }


}
