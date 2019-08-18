package EverthingRoot.core.search;

import EverthingRoot.core.model.Condition;
import EverthingRoot.core.model.Thing;

import java.util.List;

/**
 * 文件检索业务
 */


//根据Condition条件检索数据
public interface ThingSearch  {
    List<Thing> search(Condition condition);
}
