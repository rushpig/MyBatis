package com.xsy.sqlSession;

import com.xsy.executor.Executor;
import com.xsy.pojo.Configuration;
import com.xsy.pojo.MappedStatement;

import java.beans.IntrospectionException;
import java.lang.reflect.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    private Executor executor;

    public DefaultSqlSession(Configuration configuration, Executor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }

    @Override
    public <E> List<E> selectList(String statementId, Object parameter) throws Exception {
        Map<String, MappedStatement> map = configuration.getMappedStatementMap();
        MappedStatement mappedStatement = map.get(statementId);
        //将查询操作委派给底层的执行器
        List<E> list = executor.query(configuration, mappedStatement, parameter);
        return list;
    }

    @Override
    public <T> T selectOne(String statementId, Object parameter) throws Exception {
        List<Object> list = this.selectList(statementId, parameter);
        if (list.size() == 1)
            return (T) list.get(0);
        else if (list.size() > 1)
            throw new RuntimeException("返回结果过多");
        else
            return null;

    }

    @Override
    public <T> T getMapper(Class<?> c) {

        // 基于JDK动态代理产生接口的代理对象
        Object proxy = Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[]{c}, new InvocationHandler() {

            /*
             o : 代理对象：很少用到
             method ：正在执行的方法
             objects ：方法的参数
             */
            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                // findByCondition
                String methodName = method.getName();

                // com.itheima.dao.IUserDao
                String className = method.getDeclaringClass().getName();

                // 唯一标识：namespace.id  com.itheima.dao.IUserDao.findByCondition
                String statementId = className + "." + methodName;

                MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
                String sql = mappedStatement.getSql();

                // sqlcommandType select  insert update  delete
                String sqlcommandType = mappedStatement.getSqlcommandType();
                switch (sqlcommandType) {

                    case "select":
                        // 查询操作 问题来了：selectList 还是selectOne?
                        Type genericReturnType = method.getGenericReturnType();
                        // 判断是否实现泛型类型参数化
                        if (genericReturnType instanceof ParameterizedType) {
                            return selectList(statementId, objects);
                        }

                        return selectOne(statementId, objects);

                    case "update":
                        break;
                    // 更新操作
                    case "delete":
                        break;
                    // 删除操作
                    case "insert":
                        break;
                    // 添加操作
                }

                return null;
            }
        });


        return (T) proxy;
    }
}
