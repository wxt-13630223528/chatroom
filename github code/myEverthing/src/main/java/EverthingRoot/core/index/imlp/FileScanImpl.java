package EverthingRoot.core.index.imlp;

import EverthingRoot.config.EverythingConfig;

import EverthingRoot.core.dao.DataSourceFactory;
import EverthingRoot.core.dao.impl.FileIndexDaoImpl;
import EverthingRoot.core.index.FileScan;
import EverthingRoot.core.interceptor.FileInterceptor;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FileScanImpl implements FileScan {

    private  final List<FileInterceptor> interceptors = new ArrayList<>();

    private EverythingConfig config = EverythingConfig.geInstance();
    @Override
    public void index(String path) {

        Set<String> excludePaths = config.getHandlePath().getExcludePath();
        //B目录 C：Windows   A目录 C:Windows//System32
        //判断A目录  是否在B目录 中
        for(String excludePath : excludePaths){
            if(path.startsWith(excludePath)){  //如果在排除的目录集合中。
                return;
            }
        }


        File file = new File(path);
        if(file.isDirectory()){
            File[] files = file.listFiles();  //取出当前目录下的所有文件
            if(files != null){
                for(File f : files){
                    index(f.getAbsolutePath());
                }
            }
        }

        for(FileInterceptor interceptor : this.interceptors){  //这个interceptors为什么要是一个类集？
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
}
