package com.xsy.sqlSession;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public interface SqlSession {
    /**
     * 查询所有的方法 select * from user where username like '%aaa%' and sex = ''
     * 参数一：唯一表示
     * 参数二：入参
     */
    public <E> List<E> selectList(String statementId, Object parameter) throws Exception;

    /**
     * 查询单个的方法
     */
    public <T> T selectOne(String statementId, Object parameter) throws Exception;
}
