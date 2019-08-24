package com.bittech.java;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.bittech.java.util.CommUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Properties;

public class JDBCTest {
    private static DruidDataSource dataSource;
    //静态代码快当类被加载时执行，执行一次
    static {
        Properties props = CommUtils.loadProperties("datasource.properties");
        try {
            dataSource = (DruidDataSource)DruidDataSourceFactory.createDataSource(props);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    //测试查询操作
    public void testQuery(){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            //获取连接
            connection = (Connection) dataSource.getPooledConnection();
            String sql = "SELECT * FROM user WHERE username =? AND password =?";
            //执行sql语句
            statement = connection.prepareStatement(sql);
            String user = "root";
            //String user = "test’--";
            String pass = "123456";
            // String pass = "666666";失败
            statement.setString(1,user);
            statement.setString(2,pass);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                System.out.println("登陆成功！");
            }else{
                System.out.println("登陆失败！");
            }
            //判断是否有返回值
            while(resultSet.next()) {
                //取出列名为id的属性
                int id = resultSet.getInt("id");
                String userName = resultSet.getString("username");
                String password = resultSet.getString("password");
                String brief = resultSet.getString("brief");
                System.out.println("id "+id+",用户名："+userName+",密码："+password+",简介："+brief);
            }
        }catch (SQLException e){

        }finally{
            closeResource(connection,statement,resultSet);
        }
    }
   @Test
    public void testInsert() {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = (Connection) dataSource.getPooledConnection();
            //DigestUtils加密的一个类
            String password = DigestUtils.md5Hex("123");
            String sql = "INSERT INTO user(username, password,brief) " +
                    " VALUES (?,?,?)";
            statement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);//更改行数
            statement.setString(1,"test1");//传入的第一个参数
            statement.setString(2,password);
            statement.setString(3,"还是帅!");
            int rows = statement.executeUpdate();
            Assert.assertEquals(1,rows); //期望插入一行
        }catch (SQLException e) {
        }finally {
            closeResource(connection,statement);
        }
    }
    @Test
    public void testLogin(){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = (Connection) dataSource.getPooledConnection();
            String sql = "select * from user where username = ? and password = ?";
            statement = connection.prepareStatement(sql);
             String user = "test";
             String pass = "123";
             statement.setString(1,user);
             statement.setString(2,pass);
             resultSet = statement.executeQuery();
             if(resultSet.next()){
                 System.out.println("登陆成功");
             }else {
                 System.out.println("登陆失败");
             }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResource(connection,statement,resultSet);
        }

    }

    //关闭资源（更新，删除，插入关闭这两个资源）
    public void closeResource(Connection connection, Statement statement){
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(statement != null){
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //关闭资源（查找关闭多了一个resultSet资源）
    public void closeResource(Connection connection,
                              Statement statement,
                              ResultSet resultSet){
        closeResource(connection,statement);
        if(resultSet!=null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}






