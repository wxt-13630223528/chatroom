package EverthingRoot.core.monitor.Impl;

import EverthingRoot.config.EverythingConfig;
import EverthingRoot.config.HandlePath;
import EverthingRoot.core.common.FileConvertThing;
import EverthingRoot.core.dao.FileIndexDao;
import EverthingRoot.core.monitor.FileMonitor;
import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.io.File;
import java.io.FileFilter;
import java.util.Set;

public class FileMonitorImpl extends FileAlterationListenerAdaptor implements FileMonitor, FileAlterationListener {

    private FileAlterationMonitor monitor;

    private final FileIndexDao fileIndexDao;

    public FileMonitorImpl(FileIndexDao fileIndexDao){
        this.monitor = new FileAlterationMonitor(
                EverythingConfig.geInstance().getInterval()
        );
        this.fileIndexDao = fileIndexDao;
    }
    @Override
    public void start() {
        //启动
        try{
            this.monitor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void monitor(HandlePath handlePath) {
        //监控的目录
        Set<String> includePath = handlePath.getIncludePath();
        for(String path : includePath){

            FileAlterationObserver observer = new FileAlterationObserver(new File(path),pathname -> {
                for(String exclude : handlePath.getExcludePath()){
                    if(pathname.getAbsolutePath().startsWith(exclude)){
                        return false;
                    }
                }
                return true;
            });
            observer.addListener(this);      // 创建文件变化监听器
            this.monitor.addObserver(observer);
        }
    }

    @Override
    public void stop() {
        //停止
        try{
            this.monitor.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart(FileAlterationObserver fileAlterationObserver) {

    }

    @Override
    public void onDirectoryCreate(File file) {
        System.out.println("onDirectoryCreate :"+file.getAbsolutePath());
        this.fileIndexDao.insert(FileConvertThing.convert(file));
    }

    @Override
    public void onDirectoryChange(File file) {

    }


    @Override
    public void onDirectoryDelete(File file) {
        System.out.println("onDirectoryDelete :"+file.getAbsolutePath());
        this.fileIndexDao.delete(FileConvertThing.convert(file));
    }

    @Override
    public void onFileCreate(File file) {
        System.out.println("onFileCreate :"+file.getAbsolutePath());
        this.fileIndexDao.insert(FileConvertThing.convert(file));
    }

    @Override
    public void onFileChange(File file) {

    }


    @Override
    public void onFileDelete(File file) {
        System.out.println("onFileDelete :"+file.getAbsolutePath());
        this.fileIndexDao.delete(FileConvertThing.convert(file));
    }

    @Override
    public void onStop(FileAlterationObserver fileAlterationObserver) {

    }


}
