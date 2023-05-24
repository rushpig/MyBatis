package com.xsy.config;

import com.xsy.utils.ParameterMapping;
import lombok.Data;

import java.util.List;

@Data
public class BoundSql {
    private String finalSql;
    private List<ParameterMapping> parameterMappingList;

    public BoundSql(String finalSql, List<ParameterMapping> parameterMappingList){
        this.finalSql = finalSql;
        this.parameterMappingList = parameterMappingList;
    }
}
