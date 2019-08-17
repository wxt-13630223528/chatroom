package EverthingRoot.core.index;


import EverthingRoot.core.dao.DataSourceFactory;
import EverthingRoot.core.dao.FileIndexDao;
import EverthingRoot.core.dao.impl.FileIndexDaoImpl;
import EverthingRoot.core.index.imlp.FileScanImpl;
import EverthingRoot.core.interceptor.FileInterceptor;
import EverthingRoot.core.interceptor.impl.FileIndexInterceptor;

import javax.sql.DataSource;

public interface FileScan {
    //将路径中的文件 编程一个文件对象  再把文件对象编程Thing对象  通过数据库的Dao层 放进数据库中


    /**
     * 将指定path路径下的所有目录和文件以及子目录和文件 递归扫描
     * 索引到数据库
     * @param path
     */
    void index(String path);
    //索引  ：  后台线程执行
    //检索  ：  立即执行返回结果
    //配置  ：  1，索引目录：包含的目录，排除的目录  2.通过参数设置是否开启索引线程   3.查询是按照depth的升序or降序。
    //          4.查询的结果数量是多少（最大返回的数量）
    /**
     * 拦截器对象
     * @param interceptor
     */
    void interceptor(FileInterceptor interceptor);



}
