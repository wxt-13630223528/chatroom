package EverthingRoot.core.dao.impl;


import EverthingRoot.core.dao.FileIndexDao;
import EverthingRoot.core.model.Condition;
import EverthingRoot.core.model.FileType;
import EverthingRoot.core.model.Thing;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FileIndexDaoImpl implements FileIndexDao {

    //DriverManager.getConnection
    //DataSource.getConnection  通过数据源工程获取DataSource实例化对象

    private  final DataSource dataSource;

    public FileIndexDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void insert(Thing thing) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = this.dataSource.getConnection();
            String sql = "insert into thing(name,path,depth,filetype) values(?,?,?,?)";
            statement = connection.prepareStatement(sql);
            //预编译命令中SQL的占位符赋值
            statement.setString(1,thing.getName());
            statement.setString(2,thing.getPath());
            statement.setInt(3,thing.getDepth());
            statement.setString(4, String.valueOf(thing.getFileType()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            releaseResource(null,statement,connection);
        }
    }

    @Override
    public List<Thing> query(Condition condition) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Thing> list = new ArrayList<>();
        try {
            connection = this.dataSource.getConnection();
            StringBuilder sb = new StringBuilder();
            sb.append("select name,path,depth,filetype from thing ");
            sb.append("where ");
            sb.append("name like '").append(condition.getName()).append("%'");
            //采用模糊匹配
            //前模糊       搜索：简历  出现XXXX简历
            //后模糊
            //前后模糊
            //
            if(condition.getFileType() != null){
                FileType fileType = FileType.lookupByName(condition.getFileType());  //查询的时候 输入的是大写枚举  返回的也是大写枚举
                System.out.println(fileType);
                sb.append(" and filetype='"+fileType.name()+"'");
            }
            sb.append(" order by depth ").append(condition.getOrderByDepthAsc() ? "asc" : "desc");
            sb.append(" limit ").append(condition.getLimit());

            
            statement = connection.prepareStatement(sb.toString());
            //预编译命令中SQL的占位符赋值


            resultSet = statement.executeQuery();

            //处理结果
            while(resultSet.next()){
                Thing thing = new Thing();
                thing.setName(resultSet.getString("name"));
                thing.setPath(resultSet.getString("path"));
                thing.setDepth(resultSet.getInt("depth"));
                thing.setFileType((FileType.lookupByName(resultSet.getString("filetype"))));//查出来之后返回的也是大写的枚举，但是这个好像没什么用 因为输出的是路径
                list.add(thing);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            releaseResource(resultSet,statement,connection);
        }

        return list;
    }

    @Override
    public void delete(Thing thing) {
        //thing -> path : D:\a\b\hello.java
        //thing -> path : D:\a\b
        //等号意味着只能删除一个文件。

        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = this.dataSource.getConnection();
            String sql = "delete from thing where path = ?";
            statement = connection.prepareStatement(sql);
            //预编译命令中SQL的占位符赋值
            statement.setString(1,thing.getPath());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            releaseResource(null,statement,connection);
        }

    }

    //代码的重构
    //在不改变程序的功能和业务的前提下，对代码进行优化，使得代码更易阅读和扩展

    private void releaseResource(ResultSet resultSet, PreparedStatement statement,Connection connection){
        if(resultSet!=null){
            try {
                resultSet.close();
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
        if(connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


}
