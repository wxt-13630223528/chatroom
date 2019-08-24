package com.bittech.java.client.dao;

import com.bittech.java.client.entity.User;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.*;

@Data
public class AccountDao extends BasedDao {
    //用户注册
   public boolean userReg(User user){
       Connection connection = null;
       PreparedStatement statement = null;
       try {
           connection  = getConnection();
           String sql = "Insert into user(username,password,brief)"+"values(?,?,?)";
           statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
           statement.setString(1,user.getUserName());
           statement.setString(2,DigestUtils.md5Hex(user.getPassword()));
           statement.setString(3,user.getBrief());
           int rows = statement.executeUpdate();
           if(rows==1){
               return true;
           }
       } catch (SQLException e) {
           System.out.println("用户注册失败");
           e.printStackTrace();
       }finally {
           closeResources(connection,statement);
       }
       return false;
   }
   public User userLogin(String userName,String password){
       Connection connection = null;
       PreparedStatement statement = null;
       ResultSet resultSet = null;
       try {
           connection = getConnection();
           String sql = "select * from user where username = ? and password = ?";
           statement = connection.prepareStatement(sql);
           statement.setString(1,userName);
           statement.setString(2,DigestUtils.md5Hex(password));
           resultSet = statement.executeQuery();
           //上面的resultSet返回的是结果集，我们需要返回User，所以用getUser（）方法获取
           if(resultSet.next()){
               User user = getUser(resultSet);
               return user;
           }
       } catch (SQLException e) {
           System.out.println("用户登陆失败");
           e.printStackTrace();
       }finally {
          closeResources(connection,statement,resultSet);
       }
       return null;
   }
    private User getUser(ResultSet resultSet) throws SQLException{
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setUserName(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        user.setBrief(resultSet.getString("brief"));
        return user;
    }
}
