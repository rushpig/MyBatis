package com.xsy.sqlSession;

import com.xsy.config.XMLConfigBuilder;
import com.xsy.pojo.Configuration;
import org.dom4j.DocumentException;

import java.io.InputStream;

public class SqlSessionFactoryBuilder {

    /**
     * 1.解析配置文件，封装configuration
     * 2.创建sqlSessionFactory工厂对象
     */

    public SqlSessionFactory build(InputStream inputStream) throws DocumentException {

        //1.解析配置文件，封装Configuration
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder();
        Configuration configuration = xmlConfigBuilder.parse(inputStream);

        SqlSessionFactory defaultSqlSessionFactory = new DefaultSqlSessionFactory(configuration);
        return defaultSqlSessionFactory;
    }
}
