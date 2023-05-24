package com.xsy.sqlSession;

import com.xsy.executor.Executor;
import com.xsy.executor.SimpleExecutor;
import com.xsy.pojo.Configuration;

public class DefaultSqlSessionFactory implements SqlSessionFactory{

    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration){
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        //执行器创造出来
        Executor executor = new SimpleExecutor();
        DefaultSqlSession defaultSqlSession = new DefaultSqlSession(configuration, executor);
        return  defaultSqlSession;

    }
}
