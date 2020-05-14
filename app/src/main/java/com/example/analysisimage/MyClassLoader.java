package com.example.analysisimage;

import com.example.base_module.util.Bean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyClassLoader extends ClassLoader {
    private String libPath;

    public MyClassLoader(String path) {
        libPath = path;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String fileName = name;
        File file = new File(libPath, fileName);
        try {
            FileInputStream is = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int len = 0;
            try {
                while ((len = is.read()) != -1) {
                    bos.write(len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] data = bos.toByteArray();
            is.close();
            bos.close();
            return defineClass(name, data, 0, data.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.findClass(name);
    } //获取要加载 的class文件名 private String getFileName(String name) { int index = name.lastIndexOf('.'); if(index == -1){ return name+".class"; }else{ return name.substring(index+1)+".class"; } }}
}