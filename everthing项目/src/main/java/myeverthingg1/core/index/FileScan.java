package myeverthingg1.core.index;

import myeverthingg1.core.dao.DataSourceFactory;
import myeverthingg1.core.dao.FileIndexDao;
import myeverthingg1.core.dao.impl.FileIndexDaoImpl;
import myeverthingg1.core.index.impl.FileScanImpl;
import myeverthingg1.core.interceptor.FileInterceptor;
import myeverthingg1.core.interceptor.impl.FileIndexInterceptor;
import myeverthingg1.core.interceptor.impl.FilePrintInterceptor;

import javax.sql.DataSource;

public interface FileScan {
    //path -> File -> Thing -> Database Record

    /**
     * 将指定path路径下的所有目录和文件以及子目录和文件递归扫描索引到数据库
     *
     * @param path
     */
    void index(String path);

    /**
     * 拦截器对象
     *
     * @param interceptor
     */
    void interceptor(FileInterceptor interceptor);
    /*
    public static void main(String[] args) {
        FileScan fileScan = new FileScanImpl();

        //第一个: 打印输出拦截器
        fileScan.interceptor(new FilePrintInterceptor());
        //第二个: 索引文件到数据库的拦截器
        DataSource dataSource = DataSourceFactory.getInstance();
        FileIndexDao fileIndexDao = new FileIndexDaoImpl(dataSource);
        fileScan.interceptor(new FileIndexInterceptor(fileIndexDao));
        fileScan.index("D:\\test");
    }
    */
}


