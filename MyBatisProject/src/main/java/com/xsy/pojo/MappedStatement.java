package com.xsy.pojo;

import lombok.Data;

/**
 * 存放解析配置文件的内容
 * <select id="selectOne" resultType="com.oo.ds" parameterType="ds.dsd">
 *     select *from user where id = #{id} and username = #{username}
 *     </select>
 */
@Data
public class MappedStatement {

    //1.唯一标识
    private String statementId;

    //2.返回结果类型
    private String resultType;

    //3.参数类型
    private String parameterType;

    //4.要执行的sql语句
    private String sql;

    //5.mapper代理:sqlcommandType
    private String sqlcommandType;
}
