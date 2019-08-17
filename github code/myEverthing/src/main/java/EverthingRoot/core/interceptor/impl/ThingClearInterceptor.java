package EverthingRoot.core.interceptor.impl;

import EverthingRoot.core.dao.FileIndexDao;
import EverthingRoot.core.interceptor.ThingInterceptor;
import EverthingRoot.core.model.Thing;

import java.util.Queue;

public class ThingClearInterceptor  implements ThingInterceptor,Runnable {
    private final FileIndexDao fileIndexDao;
    private final Queue<Thing> queue;

    public ThingClearInterceptor(FileIndexDao fileIndexDao, Queue<Thing> queue) {
        this.fileIndexDao = fileIndexDao;
        this.queue = queue;
    }

    @Override
    public void apply(Thing thing) {
        this.fileIndexDao.delete(thing);
    }

    @Override
    public void run() {   //循环取出清除队列中的 需要清除的thing  然后调用delete删除数据库中的信息
        while(true){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Thing thing = this.queue.poll();
            if(thing!=null){
                this.apply(thing);
            }
        }
    }
}
