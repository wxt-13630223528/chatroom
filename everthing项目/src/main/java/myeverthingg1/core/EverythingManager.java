package myeverthingg1.core;

import myeverthingg1.config.EverythingConfig;
import myeverthingg1.config.HandlerPath;
import myeverthingg1.core.dao.DataSourceFactory;
import myeverthingg1.core.dao.FileIndexDao;
import myeverthingg1.core.dao.impl.FileIndexDaoImpl;
import myeverthingg1.core.index.FileScan;
import myeverthingg1.core.index.impl.FileScanImpl;
import myeverthingg1.core.interceptor.impl.FileIndexInterceptor;
import myeverthingg1.core.interceptor.impl.FilePrintInterceptor;
import myeverthingg1.core.model.Condition;
import myeverthingg1.core.model.Thing;
import myeverthingg1.core.monitor.FileMonitor;
import myeverthingg1.core.monitor.FileMonitorImpl;
import myeverthingg1.core.search.ThingSearch;
import myeverthingg1.core.search.impl.ThingSearchImpl;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 核心的统一调度器
 * 1.索引
 * 2.检索
 *
 * 配置，索引模块，检索模块，拦截器模块组合调度
 */
public class EverythingManager {
    private static volatile EverythingManager manager;
    /**
     * 业务层
     */
    private FileScan fileScan;
    private ThingSearch thingSearch;


    //线程池的执行器
    private final ExecutorService executorService = Executors.newFixedThreadPool
            (Runtime.getRuntime().availableProcessors()*2);

    private EverythingConfig config = EverythingConfig.getInstance();

    private FileMonitor fileMonitor;

    private EverythingManager() {
        /**
         * 数据库访问层
         */
        FileIndexDao fileIndexDao = new FileIndexDaoImpl(DataSourceFactory.getInstance());

        this.fileScan = new FileScanImpl();
        //打印索引信息的拦截器
        //this.fileScan.interceptor(new FilePrintInterceptor());
        //索引信息写数据库的拦截器
        this.fileScan.interceptor(new FileIndexInterceptor(fileIndexDao));

        this.thingSearch = new ThingSearchImpl(fileIndexDao);

        this.fileMonitor = new FileMonitorImpl(fileIndexDao);
    }

    public static EverythingManager getInstance() {
        //double - check
        if (manager == null) {
            synchronized (EverythingManager.class) {
                if (manager == null) {
                    manager = new EverythingManager();
                }
            }
        }
        return manager;
    }

    /**
     * 构建索引
     */
    public void buildIndex() {
        //建立索引
        DataSourceFactory.databaseInit(true);
        HandlerPath handlerPath = config.getHandlerPath();
        Set<String> includePaths = handlerPath.getIncludePath();
        new Thread(()-> {
                System.out.println("Build Index Started....");
                final CountDownLatch countDownLatch = new CountDownLatch(includePaths.size());
                for (String path: includePaths) {
                    executorService.submit(()-> {
                        fileScan.index(path);
                        countDownLatch.countDown();
                    });
                }

                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Build Index complete...");
        }).start();
    }
    /**
     * 检索功能
     */
    public List<Thing> search(Condition condition){
        //condition 的用户提供的是：name file_type
        //limit orderBy
        condition.setLimit(config.getMaxReturn());
        condition.setOrderByDepthAsc(!config.getOrderByDesc());
        return this.thingSearch.search(condition);
    }
    /**
     * 帮助功能
     */
     public void  help(){
         /**
          * 命令列表
          * 退出：quit
          * 帮助：help
          * 索引：index
          * 搜索：search<name> [<file - Type> img / doc / bin / archive / other ]
          */
         System.out.println("命令列表：");
         System.out.println("退出：quite");
         System.out.println("帮助：help");
         System.out.println("索引：index");
         System.out.println("搜索：search<name> [<file - Type> img / doc / bin / archive / other ]");
     }
    /**
     * 退出
     */
    public void quit(){
        System.out.println("欢迎使用  再见！");
        System.exit(0);
    }

    /**
     * 文件监控
     */
    public void monitor(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                fileMonitor.monitor(config.getHandlerPath());
                fileMonitor.start();

            }
        }).start();
    }
}
