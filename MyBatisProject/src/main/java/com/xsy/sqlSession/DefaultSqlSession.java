package com.xsy.sqlSession;

import com.xsy.executor.Executor;
import com.xsy.pojo.Configuration;
import com.xsy.pojo.MappedStatement;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DefaultSqlSession implements SqlSession{

    private Configuration configuration;

    private Executor executor;

    public DefaultSqlSession(Configuration configuration, Executor executor){
        this.configuration = configuration;
        this.executor = executor;
    }

    @Override
    public <E> List<E> selectList(String statementId, Object parameter) throws Exception {
        Map<String, MappedStatement> map =  configuration.getMappedStatementMap();
        MappedStatement mappedStatement = map.get(statementId);
        //将查询操作委派给底层的执行器
        List<E> list = executor.query(configuration, mappedStatement, parameter);
        return list;
    }

    @Override
    public <T> T selectOne(String statementId, Object parameter) throws Exception {
        List<Object> list = this.selectList(statementId, parameter);
        if(list.size() == 1)
            return (T)list.get(0);
        else if(list.size() > 1)
            throw new RuntimeException("返回结果过多");
        else
            return null;

    }
}
