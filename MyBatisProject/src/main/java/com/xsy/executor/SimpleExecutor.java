package com.xsy.executor;

import com.xsy.config.BoundSql;
import com.xsy.pojo.Configuration;
import com.xsy.pojo.MappedStatement;
import com.xsy.utils.GenericTokenParser;
import com.xsy.utils.ParameterMapping;
import com.xsy.utils.ParameterMappingTokenHandler;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SimpleExecutor implements Executor{

    /**
     * 执行JDBC操作
     * @param configuration
     * @param mappedStatement
     * @param params
     * @param <E>
     * @return
     */
    @Override
    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object params) throws SQLException, ClassNotFoundException, NoSuchFieldException, InstantiationException, IllegalAccessException, IntrospectionException, InvocationTargetException {
        //1.加载驱动，获取连接
        Connection connection = configuration.getDataSource().getConnection();

        //2.获取预编译对象 PrepareStatement
        //select * from user where id = #{id} and username = #{username}
        //select * from user where id = ? and username = ?
        //替换占位符:(1)将#{}替换成？ (2)将#{}里面的值解析保存
        String sql = mappedStatement.getSql(); //PDF是getSqlText
        BoundSql boundSql = getBoundSQL(sql);
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getFinalSql());

        //3.设置参数
        //问题1：Object param(类型不确定user/product/map/String)
        String parameterType = mappedStatement.getParameterType();
        if(parameterType != null){
            Class<?> parameterTypeClass = Class.forName(parameterType);
            //问题2：该将对象中的那个属性赋值给那个占位符呢？parameterMappings
            List<ParameterMapping> parameterMappings = boundSql.getParameterMappingList();
            for(int i = 0; i < parameterMappings.size(); i++){
                ParameterMapping parameterMapping = parameterMappings.get(i);
                //id or username
                String fieldName = parameterMapping.getContent();
                Field declaredField = parameterTypeClass.getDeclaredField(fieldName);
                declaredField.setAccessible(true);
                Object value = declaredField.get(params);
                preparedStatement.setObject(i + 1,value);
            }
        }
        // 4. 执行sql
        ResultSet resultSet = preparedStatement.executeQuery();
        // 5.处理返回结果集
        // 问题1：要封装到那个对象？
        List<E> list = new ArrayList<>();
        String resultType = mappedStatement.getResultType();
        Class<?> resultTypeClass = Class.forName(resultType);
        while (resultSet.next()){
            Object obj = resultTypeClass.newInstance();
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount() ; i++) {
                String columnName = metaData.getColumnName(i);
                Object columnValue = resultSet.getObject(columnName);
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultTypeClass);
                Method writeMethod = propertyDescriptor.getWriteMethod();
                writeMethod.invoke(obj,columnValue);
            }
            list.add((E) obj);
        }
        return list;
    }
    /**
     * 1.将sql中#{}替换成？ 2.将#{}里面的值保存
     * @param sql
     * @return
     */
    private BoundSql getBoundSQL(String sql) {
// 标记处理器：配合标记解析器完成标记的解析工作
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
// 标记解析器
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{","}", parameterMappingTokenHandler);
        String finalSql = genericTokenParser.parse(sql);
// #{}里面的值的集合
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();
        BoundSql boundSql = new BoundSql(finalSql, parameterMappings);
        return boundSql;
    }
}
