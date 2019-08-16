package EverthingRoot.core.dao;

import EverthingRoot.core.model.Condition;
import com.alibaba.druid.pool.DruidDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.*;

public class DataSourceFactory {
    /**
     * 数据库的数据源
     */
    private static volatile DruidDataSource instance;

    private DataSourceFactory(){

    }

    public static DataSource getInstance(){
        if(instance == null){
            synchronized (DataSource.class){
                if(instance == null){
                    instance = new DruidDataSource();
                    //链接数据库需要url host port databaseName
                    //username
                    //password
                   /**这是链接mysql的配置
                    *  instance.setUrl("jdbc:mysql://127.0.0.1:3306/everything_g2?useUnicode=true&characterEncoding=utf-8&useSSL=false");
                    instance.setUsername("root");
                    instance.setPassword("1052602391");
                    instance.setDriverClassName("com.mysql.jdbc.Driver");*/
                   //这是链接H2的配置
                    instance.setTestWhileIdle(false);
                    instance.setDriverClassName("org.h2.Driver");
                    String path = System.getProperty("user.dir")+File.separator+"lib";
                    instance.setUrl("jdbc:h2:"+path);

                    //数据库创建完成之后，初始化表结构
                    databaseInit(false);
                }
            }
        }
        return instance;
    }

    public static void databaseInit(boolean buildIndex){
        //classpath:database.sql => String
        StringBuilder sb = new StringBuilder();
        try(InputStream in = DataSourceFactory.class.getClassLoader().getResourceAsStream("database.sql");){
            if(in != null) {
                try(BufferedReader reader = new BufferedReader(
                        new InputStreamReader(in)
                )){
                    String line = null;
                    while((line = reader.readLine())!=null){   //将database.sql文件中的命令保存到sb中
                        sb.append(line);
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                throw new RuntimeException("database.sql script can't load please check it.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String sql = sb.toString();
        try(Connection connection = getInstance().getConnection();
        ) {
            if(buildIndex){  //如果为真，就把旧表删除  创建新的表
                try(PreparedStatement statement1 = connection.prepareStatement("drop table if exists thing")){
                    statement1.executeUpdate();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            try(PreparedStatement statement1 = connection.prepareStatement(sql)){
                statement1.executeUpdate();
            }catch (SQLException e){
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

