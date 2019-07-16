package myeverthingg1.core.index.impl;

import myeverthingg1.config.EverythingConfig;
import myeverthingg1.core.dao.DataSourceFactory;
import myeverthingg1.core.dao.FileIndexDao;
import myeverthingg1.core.dao.impl.FileIndexDaoImpl;
import myeverthingg1.core.index.FileScan;
import myeverthingg1.core.interceptor.FileInterceptor;
import myeverthingg1.core.interceptor.impl.FileIndexInterceptor;
import myeverthingg1.core.interceptor.impl.FilePrintInterceptor;

import javax.sql.DataSource;
import java.io.File;
import java.util.LinkedList;
import java.util.Set;

public class FileScanImpl implements FileScan {

    private final LinkedList<FileInterceptor> interceptors = new LinkedList<>();

    private EverythingConfig config = EverythingConfig.getInstance();

    @Override
    public void index(String path) {

        Set<String> excludePaths = config.getHandlerPath().getExcludePath();
        //判断A path 是否在B path中
        for(String excludePath : excludePaths){
            if(path.startsWith(excludePath)){
                return;
            }
        }
        File file = new File(path);
        if(file.isDirectory()){
            File[] files = file.listFiles();
            if(files!=null){
                for(File f : files) {
                    index(f.getAbsolutePath());
                }
            }
        }
        for(FileInterceptor interceptor:this.interceptors){
            try {
                interceptor.apply(file);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    @Override
    public void interceptor(FileInterceptor interceptor) {
        this.interceptors.add(interceptor);
    }

    /**
     * 测试代码
     * 文件递归
     */
    /*
    public static void main(String[] args) {
        FileScan scan = new FileScanImpl();
        scan.index("D:\\test");
    }
    */
}
