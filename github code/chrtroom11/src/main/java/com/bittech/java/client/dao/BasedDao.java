package com.bittech.java.client.dao;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.bittech.java.util.CommUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.concurrent.locks.Condition;

/**
 * 封装基础的dao操作，获取数据源。连接、关闭资源等
 */
public class BasedDao {
    private static DruidDataSource dataSource;
    //数据源类加载时执行，并且只执行一次
    static {
        Properties properties = CommUtils.loadProperties("datasource.properties");
        try {
            dataSource = (DruidDataSource)DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            System.out.println("数据源加载失败");
            e.printStackTrace();
        }
    }
    //只有继承了BasedDao的所有子类，才有能力获取连接
    protected DruidPooledConnection getConnection(){
        try {
            return (DruidPooledConnection) dataSource.getPooledConnection();
        } catch (SQLException e) {
            System.out.println("数据库连接获取失败");
            e.printStackTrace();
        }
        return null;
    }
    protected void closeResources(Connection connection, Statement statement){
        if(connection!=null){
            try {
               connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(statement!=null){
            try {
                statement.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    protected  void closeResources(Connection connection, Statement statement, ResultSet resultSet){
        closeResources(connection,statement);
        if(resultSet!=null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
