package myeverthingg1.core.search;

import myeverthingg1.core.model.Condition;
import myeverthingg1.core.model.Thing;

import java.util.List;

/**
 * 文件检索业务
 */
public interface ThingSearch {
    /**
     * 根据condition条件检索数据
     * @param condition
     * @return
     */
    List<Thing> search(Condition condition);

}
