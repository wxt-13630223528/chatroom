package myeverthingg1.config;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 处理的目录
 */
public class HandlerPath {
    /**
     * 包含的目录
     */
    private Set<String> includePath = new HashSet<>();

    /**
     * 排除的目录
     */
    private Set<String> excludePath = new HashSet<>();

    private HandlerPath() {

    }

    public void addIncludePath(String path) {
        this.includePath.add(path);
    }

    public void addExcludePath(String path) {
        this.excludePath.add(path);
    }
    //----------getter and setter


    public Set<String> getIncludePath() {
        return includePath;
    }


    public Set<String> getExcludePath() {
        return excludePath;
    }

    @Override
    public String toString() {
        return "HandlerPath{" +
                "includePath=" + includePath +
                ", excludePath=" + excludePath +
                '}';
    }

    //-------------------
    public static HandlerPath getDefaultHandlerPath() {
        //排除windows program files
        HandlerPath handlerPath = new HandlerPath();
        Iterable<Path> paths = FileSystems.getDefault().getRootDirectories();
        //默认要包含的目录，即构建索引是需要处理的路径
        Iterator<Path> iterator = paths.iterator();
        while (iterator.hasNext()) {
            handlerPath.addIncludePath(iterator.next().toString());
        }

        //默认要排除的目录，即构建索引时不需要处理的路径
        //windows Linux
        String systemName = System.getProperty("os.name");
        if (systemName.contains("Windows")) {
            //windows
            handlerPath.addExcludePath("C:\\Windows");
            handlerPath.addExcludePath("C:\\ProgramData");
            handlerPath.addExcludePath("C:\\Program Files");
            handlerPath.addExcludePath("C:\\Program Files(x86)");
        } else {
            //linux
            handlerPath.addExcludePath("/root");
            handlerPath.addExcludePath("/temp");
        }
        return handlerPath;
    }
}
