package EverthingRoot.core.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * FileType 模型类表示文件的扩展名归类
 */

public enum  FileType {
    IMG("jpg","jpeg","png","bmp","gif"),
    DOC("doc","docx","pdf","ppt","pptx","xls"),
    BIN("exe","jar","sh","msi"),
    ARCHIVE("zip","rar"),
    OTHER("*");

    private Set<String> extend = new HashSet<>();

    FileType(String... extend){   //这个方法是枚举的特点，extend  就是枚举里面的字符串
        this.extend.addAll(Arrays.asList(extend));
    }

    public static FileType LookupByextend(String extend){  //这个方法就是将文件信息插入数据库之前，将文件的类型统一化 变成枚举
        for(FileType fileType : FileType.values()){
            if(fileType.extend.contains(extend)){ //如果在某个枚举的字符串中存在，就返回这个枚举
                return fileType;
            }
        }
        return FileType.OTHER;
    }

    public static FileType lookupByName(String name){
        for(FileType fileType : FileType.values()){
            if(fileType.name().equals(name)){
                return  fileType;
            }
        }
        return FileType.OTHER;
    }
}
