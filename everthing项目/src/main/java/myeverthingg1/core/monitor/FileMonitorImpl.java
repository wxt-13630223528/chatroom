package myeverthingg1.core.monitor;

import myeverthingg1.config.EverythingConfig;
import myeverthingg1.config.HandlerPath;
import myeverthingg1.core.common.FileConvertThing;
import myeverthingg1.core.dao.FileIndexDao;
import org.apache.commons.io.monitor.*;

import java.io.File;
import java.io.FileFilter;
import java.util.Set;

public class FileMonitorImpl extends FileAlterationListenerAdaptor implements FileMonitor {

    private FileAlterationMonitor monitor;

    private final FileIndexDao fileIndexDao;

    public FileMonitorImpl(FileIndexDao fileIndexDao){
        this.monitor = new FileAlterationMonitor(
                EverythingConfig.getInstance().getInterval()
        );
        this.fileIndexDao = fileIndexDao;

    }

    @Override
    public void start() {
        //启动
        try {
            this.monitor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void monitor(HandlerPath handlerPath) {
        //监控的目录
        Set<String> includePath = handlerPath.getIncludePath();
        for(String path :includePath){
            FileAlterationObserver observer =
                    new FileAlterationObserver(new File(path),pathname ->{
                            for(String exclude : handlerPath.getExcludePath()) {
                                if (pathname.getAbsolutePath().startsWith(exclude)) {
                                    return false;
                                }
                            }
                            return true;
                    });
            observer.addListener(this);
            this.monitor.addObserver(observer);
        }
    }

    @Override
    public void onDirectoryCreate(File directory) {
        System.out.println("onDirectoryCreate:"+directory.getAbsolutePath());
        this.fileIndexDao.insert(FileConvertThing.convert(directory));
    }

    @Override
    public void onDirectoryDelete(File directory) {
        System.out.println("onDirectoryDelete:"+directory.getAbsolutePath());
        this.fileIndexDao.insert(FileConvertThing.convert(directory));
    }

    @Override
    public void onFileCreate(File file) {
        System.out.println("onFileChange:"+file.getAbsolutePath());
        this.fileIndexDao.insert(FileConvertThing.convert(file));
    }

    @Override
    public void onFileDelete(File file) {
        System.out.println("onFileDelete:"+file.getAbsolutePath());
        this.fileIndexDao.insert(FileConvertThing.convert(file));
    }

    @Override
    public void stop() {
        //停止
        try {
            this.monitor.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
