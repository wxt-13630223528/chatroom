package EverthingRoot.core.interceptor.impl;

import EverthingRoot.core.interceptor.FileInterceptor;

import java.io.File;

public class FilePrintlnterceptor implements FileInterceptor {
    @Override
    public void apply(File file) {
        System.out.println(file.getAbsolutePath());
    }
}
