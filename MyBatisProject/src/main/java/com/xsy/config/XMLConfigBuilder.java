package com.xsy.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.xsy.io.Resources;
import com.xsy.pojo.Configuration;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class XMLConfigBuilder {

    private Configuration configuration;

    public XMLConfigBuilder(){
        configuration = new Configuration();
    }

    /*
        使用dom4j解析xml文件，封装成configuration对象
     */
    public Configuration parse(InputStream inputStream) throws DocumentException {
        Document document = new SAXReader().read(inputStream);
        Element rootElement = document.getRootElement();

        //解析核心配置文件中数据源部分
        List<Element> list = rootElement.elements("//preperty"); //这里跟pdf的不一样
        Properties properties = new Properties();
        for (Element element : list){
            String name = element.attributeValue("name");
            String value = element.attributeValue("value");
            properties.setProperty(name, value);
        }

        //创建数据源对象（连接池）
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(properties.getProperty("url"));
        druidDataSource.setUsername(properties.getProperty("username"));
        druidDataSource.setPassword(properties.getProperty("password"));

        //创建好的数据源对象封装到configuration中
        configuration.setDataSource(druidDataSource);

        //解析映射配置文件
        //1.获取映射文件的路径 2.解析 3.封装好mappedStatement
        List<Element> mapperList = rootElement.elements("//mapper");
        for (Element element : mapperList){
            String mapperPath = element.attributeValue("resource");
            InputStream resourceAsSteam = Resources.getResourceAsSteam(mapperPath);
            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(configuration);
            xmlMapperBuilder.parse(resourceAsSteam);

        }
        return configuration;


    }
}
