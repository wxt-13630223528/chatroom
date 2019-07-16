package myeverthingg1.core.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *FileType模型类表示文件的扩展名归类
 *
 */



public enum FileType {
    IMG("jpg","jpeg","png","bmp","gif"),
    DOC("doc","docx","pdf","pptx","xls","txt"),
    BIN("exe","jar","sh","msi"),
    ARCHIVE("zip","rar"),
    OTHER("*");

    private Set<String> extend = new HashSet<>();
    FileType(String...extend){
        this.extend.addAll(Arrays.asList(extend));
    }

    //  根据扩展名寻找FileType
    public static FileType lookupByExtend(String extend){
        for(FileType fileType:FileType.values()){
            if(fileType.extend.contains(extend)){
                return fileType;
            }
        }
        return FileType.OTHER;
    }

    public static FileType lookupByName(String name){
        for(FileType fileType:FileType.values()){
            if(fileType.name().equals(name)){
                return fileType;
            }
        }
        return FileType.OTHER;
    }

    public static void main(String[] args) {
        System.out.println(FileType.lookupByExtend("exe"));//bin
        System.out.println(FileType.lookupByExtend("md"));//other
        System.out.println(FileType.lookupByName("BIN"));//bin
        System.out.println(FileType.lookupByName("SHELL"));//other
    }
}
