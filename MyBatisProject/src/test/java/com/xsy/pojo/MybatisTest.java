package com.xsy.pojo;

import com.xsy.dao.IUserDao;
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
import java.util.List;


public class MybatisTest {


    /**
     * 传统方式（不使用mapper代理）测试
     */
    @Test
    public void test1() throws Exception {

        // 1.根据配置文件的路径，加载成字节输入流，存到内存中 注意：配置文件还未解析
        InputStream resourceAsSteam = Resources.getResourceAsSteam("sqlMapConfig.xml");

        // 2.解析了配置文件，封装了Configuration对象  2.创建sqlSessionFactory工厂对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsSteam);

        // 3.生产sqlSession 创建了执行器对象
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 4.调用sqlSession方法
        User user = new User();
        user.setId(1);
        user.setUsername("tom");

       // User user2 = sqlSession.selectOne("com.xsy.dao.IUserDao.selectList", user);

       // System.out.println(user2);

        IUserDao iUserDao = sqlSession.getMapper(IUserDao.class);
        List<User> list = iUserDao.selectList();
        System.out.println(list.toString());

    }

}
