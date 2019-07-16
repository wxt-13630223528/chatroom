package myeverthingg1.core.interceptor.impl;

import myeverthingg1.core.interceptor.FileInterceptor;

import java.io.File;

public class FilePrintInterceptor implements FileInterceptor {
    /**
     * 打印文件
     * @param file
     */
    @Override
    public void apply(File file) {
        System.out.println(file.getAbsolutePath());
    }
}
