package myeverthingg1.core.dao;

import myeverthingg1.core.model.Condition;
import myeverthingg1.core.model.Thing;

import java.util.List;

/**
 * 数据库访问的对象
 */
public interface FileIndexDao {
    // File ->Thing ->Database Table Record
    //CRUD ->c /R U D

    /**
     * 插入
     * @param thing
     */
    void insert(Thing thing);

    /**
     * 插入
     * @param thing
     */
    void delete(Thing thing);
    /**
     * 查询
     * @param condition
     */
    List<Thing> query(Condition condition);
}
