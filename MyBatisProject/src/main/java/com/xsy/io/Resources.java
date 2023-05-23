package com.xsy.io;

import java.io.InputStream;


public class Resources {

    /**
     * 根据配置文件的路径，加载成字节输入流，存到内存中
     */
    public static InputStream getResourceAsSteam(String path){
        //这个方法已经用了很多次了，得到加载器，然后得到路径，前面的ioc容器，注意查一下
        InputStream resourceAsStream = Resources.class.getClassLoader().getResourceAsStream(path);
        return resourceAsStream;
    }
}
