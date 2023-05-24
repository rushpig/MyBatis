package com.xsy.sqlSession;

public interface SqlSessionFactory {
    /*
        生产SqlSession:封装着与数据库交互的方法
     */
    public SqlSession openSession();

}
