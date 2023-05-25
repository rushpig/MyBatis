package com.xsy.executor;

import com.xsy.pojo.Configuration;
import com.xsy.pojo.MappedStatement;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public interface Executor {

    <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object param) throws Exception;

    void close();
}

