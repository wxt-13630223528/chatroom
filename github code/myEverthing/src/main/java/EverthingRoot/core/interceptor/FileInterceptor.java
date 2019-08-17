package EverthingRoot.core.interceptor;
import java.io.*;
public interface FileInterceptor {
    /**
     * 文件拦截器，处理指定文件
     */

    void apply(File file);
}
