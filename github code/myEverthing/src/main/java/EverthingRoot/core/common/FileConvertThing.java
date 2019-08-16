package EverthingRoot.core.common;

import java.io.*;

import EverthingRoot.core.model.FileType;
import EverthingRoot.core.model.Thing;
import java.io.*;
/**
 * 文件对象转化Thing对象的辅助类
 */

public class FileConvertThing {
    public static Thing convert(File file){
        Thing thing = new Thing();
        String name = file.getName();
        thing.setName(name);
        thing.setPath(file.getAbsolutePath());

        //目录  *
        //文件 -》 有扩展名，通过扩展名获取FileType
        //  无扩展  *

        int index = name.lastIndexOf(".");
        String extend = "*";
        if(index!=-1 && (index+1)< name.length()){
            extend = file.getName().substring(index+1);
        }
        thing.setFileType(FileType.LookupByextend(extend));//插入的时候调用转化，转化将枚举的大写字母返回到这里
        thing.setDepth(computePathDepth(file.getAbsolutePath()));
        return thing;
    }
    private static int computePathDepth(String path){

        int depth = 0;
        for(char c : path.toCharArray()){
            if(c == File.separatorChar){
                depth += 1;
            }
        }
        return depth;
    }
}
