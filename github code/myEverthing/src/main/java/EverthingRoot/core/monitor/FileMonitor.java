package EverthingRoot.core.monitor;

import EverthingRoot.config.HandlePath;
/**
 * 文件监控接口
 */
public interface FileMonitor {
    void start();


    void monitor(HandlePath handlePath);


    void stop();
}
