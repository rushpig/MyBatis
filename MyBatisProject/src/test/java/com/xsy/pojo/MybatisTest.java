package com.xsy.pojo;

import com.xsy.io.Resources;
import com.xsy.sqlSession.SqlSession;
import com.xsy.sqlSession.SqlSessionFactory;
import com.xsy.sqlSession.SqlSessionFactoryBuilder;
import org.dom4j.DocumentException;
import org.junit.Test;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;


public class MybatisTest {

    @Test
    public void test1() throws IOException, DocumentException, SQLException, IntrospectionException, NoSuchFieldException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException {
        InputStream resourceAsStream = Resources.getResourceAsSteam("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        User user = new User();
        user.setUsername("xsr");
        user.setId(232);
        User user1 = sqlSession.selectOne(String.valueOf(user.getId()), user);
        System.out.println(user1);
        System.out.println("MyBatis源码环境搭建成功...");
        //sqlSession.close();
        }
}