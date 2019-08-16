package EverthingRoot.core.dao;

import EverthingRoot.core.model.Condition;
import EverthingRoot.core.model.Thing;

import java.util.List;

public interface FileIndexDao {
    //插入
    void insert(Thing thing);

    //查询
    List<Thing> query(Condition condition);

    //删除
    void delete(Thing thing);
}
