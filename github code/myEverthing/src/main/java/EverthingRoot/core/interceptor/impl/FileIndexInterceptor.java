package EverthingRoot.core.interceptor.impl;


import EverthingRoot.core.common.FileConvertThing;
import EverthingRoot.core.dao.FileIndexDao;
import EverthingRoot.core.interceptor.FileInterceptor;
import EverthingRoot.core.model.Thing;

import java.io.*;
public class FileIndexInterceptor implements FileInterceptor {

    private final FileIndexDao fileIndexDao;

    public FileIndexInterceptor(FileIndexDao fileIndexDao1){

        this.fileIndexDao = fileIndexDao1;
    }

    @Override
    public void apply(File file) {
        Thing thing = FileConvertThing.convert(file);
        this.fileIndexDao.insert(thing);
    }
}
