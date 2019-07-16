package myeverthingg1.core.dao.impl;

import myeverthingg1.core.dao.DataSourceFactory;
import myeverthingg1.core.dao.FileIndexDao;
import myeverthingg1.core.model.Condition;
import myeverthingg1.core.model.FileType;
import myeverthingg1.core.model.Thing;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FileIndexDaoImpl implements FileIndexDao {

    //DriverManager.getConnection
    //DataSource.getConnection   通过数据源工厂获取DataSource实例化对象
    private final DataSource dataSource;

    public FileIndexDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void insert(Thing thing) {
        //JDBC操作
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = this.dataSource.getConnection();
            String sql = "insert into thing(name,path,depth,file_type) values(?,?,?,?)";
             statement = connection.prepareStatement(sql);
             //预编译命令中的SQL的占位符赋值
            statement.setString(1,thing.getName());
            statement.setString(2,thing.getPath());
            statement.setInt(3,thing.getDepth());
            statement.setString(4,thing.getFileType().name());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
           releaseResource(null,statement,connection);
        }
    }

    @Override
    public void delete(Thing thing) {

        //thing -> path => D:\a\b\hello.java
        //thing -> path => D:\a\b
        //                  D:\a\ba
        //like path%
        //=  最多只能删除一个，绝对匹配



        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = this.dataSource.getConnection();
            String sql = "delete from thing where path = ?";
            statement = connection.prepareStatement(sql);
            //预编译命令中的SQL的占位符赋值
            statement.setString(1,thing.getPath());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            releaseResource(null,statement,connection);
        }
    }

    /**
     * 查询
     * @param condition
     * @return
     */
    @Override
    public List<Thing> query(Condition condition) {
        List<Thing> things = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = this.dataSource.getConnection();
            StringBuilder sb = new StringBuilder();
            sb.append("select name,path,depth,file_type from thing");

            sb.append(" where ");
            //search <name> [file_type]

            //采用模糊匹配
            //前模糊：xxx简历
            //后模糊：简历xxx  ✔
            //前后模糊:xx简历xx
            sb.append(" name like '").append(condition.getName()).append("%'");
            if(condition.getFileType()!=null){
                FileType fileType = FileType.lookupByName(condition.getName());
                sb.append(" and file_type= '" + fileType.name()+"'");
            }
            sb.append(" order by depth ").append(condition.getOrderByDepthAsc()?"asc" : "desc");
            sb.append(" limit ").append(condition.getLimit());

            statement = connection.prepareStatement(sb.toString());
            resultSet = statement.executeQuery();
            //处理结果
            while(resultSet.next()){
                //Record ->Thing
                Thing thing = new Thing();
                thing.setName(resultSet.getString("name"));
                thing.setPath(resultSet.getString("path"));
                thing.setDepth(resultSet.getInt("depth"));
                thing.setFileType(FileType.lookupByName(resultSet.getString("file_type")));
                things.add(thing);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            releaseResource(resultSet,statement,connection);
        }
        return things;
    }

    //重构
    //在不改变程序的功能和业务的前提下，对代码进行优化，使得代码更易阅读和扩展。
    private void releaseResource(ResultSet resultSet,
                  PreparedStatement statement,Connection connection){
        if(resultSet !=null){
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(statement!=null){
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(connection!=null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    //测试代码
    /*
    public static void main(String[] args) {
        DataSource dataSource = DataSourceFactory.getInstance();
        FileIndexDao dao = new FileIndexDaoImpl(dataSource);

        Thing thing = new Thing();
        thing.setName("Java编程思想.pdf");
        thing.setDepth(2);
        thing.setPath("D:\\a\\Java编程思想.pdf");
        thing.setFileType(FileType.DOC);

     //3.   dao.insert(thing);
     //4.   dao.delete(thing);






//  2.      Condition condition = new Condition();
//        condition.setName("地鼠.png");
//        List<Thing> things = dao.query(condition);
//        for(Thing thing:things){
//            System.out.println(thing);
//        }
    }

// 1.   public static void main(String[] args) {
//        File file = new File("D:\\a\\b\\hello.java");
//        System.out.println(file.isFile());
//    }
*/
}
