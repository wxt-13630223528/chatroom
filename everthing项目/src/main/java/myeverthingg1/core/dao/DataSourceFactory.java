package myeverthingg1.core.dao;

import myeverthingg1.core.model.Thing;


import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.alibaba.druid.pool.DruidDataSource;

import javax.sql.DataSource;


public class DataSourceFactory {
    /**
     * 数据库的数据源
     */

    public static volatile DruidDataSource instance;

    private DataSourceFactory() {
    }

    public static DataSource getInstance() {
        if (instance == null) {
            synchronized (DataSource.class) {
                if (instance == null) {
                    instance = new DruidDataSource();
                    //url -> host,port,databaseName
                    //username
                    //password
                    //这是链接MySQL的配置
                    //                   instance.setUrl("jdbc:mysql://127.0.0.1:3306/everything_g1" +
                    //  "?useUnicode=true&characterEncoding=utf-8&useSSL=false");
//                    instance.setUsername("root");
//                    instance.setPassword("123456");
//                    instance.setDriverClassName("com.mysql.jdbc.Driver");

                    //这是链接H2数据库的配置
                    instance.setTestWhileIdle(false);
                    instance.setDriverClassName("org.h2.Driver");
                    String path = System.getProperty("user.dir") + File.separator + "lib";
                    instance.setUrl("jdbc:h2:" + path);

                    //数据库创建完成之后，初始化表结构
                    databaseInit(false);
                }
            }
        }
        return instance;
    }

    public static void databaseInit(boolean buildIndex) {
        StringBuilder sb = new StringBuilder();
        try (InputStream in = DataSourceFactory.class.getClassLoader().getResourceAsStream("database.sql");)
         {
            if (in!=null) {
                try (
                       BufferedReader reader = new BufferedReader(new InputStreamReader(in))
                ){
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                throw new RuntimeException("database.sql script can't load please check it.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String sql = sb.toString();

        try (Connection connection = getInstance().getConnection();) {
            if (buildIndex) {
                try (PreparedStatement statement = connection.prepareStatement("drop table if exists thing;");) {
                    statement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            try (PreparedStatement statement = connection.prepareStatement(sql);) {
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DataSourceFactory.databaseInit(true);
    }
}



//    public static void main(String[] args){
//        Thing thing = new Thing();
//        DataSource dataSource = DataSourceFactory.getInstance();
//
//        try (Connection connection = dataSource.getConnection();
//             PreparedStatement statement = connection.
//                     prepareStatement("insert into thing(name,path,depth,file_type) value(?,?,?,?)")
//        ) {
//            statement.setString(1, "1.png");
//            statement.setString(2, "C:\\打地鼠\\1.png");
//            statement.setInt(3, 2);
//            statement.setString(4,"DOC");
//            statement.executeUpdate();
//        } catch (SQLException e) {
//
//        }


