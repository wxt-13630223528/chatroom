package myeverthingg1.core.common;

import myeverthingg1.core.model.FileType;
import myeverthingg1.core.model.Thing;

import java.io.File;

/**
 * 文件对象转换Thing对象的辅助类
 */
public class FileConvertThing {
    public static Thing convert(File file){
        Thing thing = new Thing();
        String name = file.getName();

        thing.setName(name);
        thing.setPath(file.getAbsolutePath());
        //目录 -> *
        //文件 -> 有扩展名，通过扩展名获取FileType
        //         无扩展名，*
        int index = name.lastIndexOf(".");
        String extend = "*";
        if(index != -1 && (index+1)<name.length()){
            extend = name.substring(index+1);
        }
       thing.setFileType(FileType.lookupByExtend(extend));
        thing.setDepth(computePathDepth(file.getAbsolutePath()));
        return thing;
    }

    private static int computePathDepth(String path){
        //path => D:\a\b =>2
        //path => D:\a\b\hello.java =>3
        //windows:\
        //Linux : /
        int depth = 0;
        for(char c:path.toCharArray()){
            if(c == File.separatorChar){
                depth += 1;
            }
        }
        return depth;
    }
    /**
     * 测试代码2
     *  打印结果：Thing{name='test1.txt', path='D:\test\a\test1.txt', depth=3, fileType=DOC}
     */
    /*
    public static void main(String[] args) {
        File file = new File("D:\\test\\a\\test1.txt");
        Thing thing = FileConvertThing.convert(file);
        System.out.println(thing);
    }
    */






    /**
     * 测试代码1
     * 作用：将文件对象变为thing
     */
    /*
    public static void main(String[] args) {
        System.out.println(computePathDepth("D:\\a\\b"));
        System.out.println(computePathDepth("D:\\a\\b\\hello.java"));
    }
    */

}

