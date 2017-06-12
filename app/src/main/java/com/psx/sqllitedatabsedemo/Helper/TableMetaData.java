package com.psx.sqllitedatabsedemo.Helper;

import java.util.List;

/**
 * Created by Pranav on 12-06-2017.
 */

public class TableMetaData {
    List<String> fieldNames;
    List<String> fieldtype;
    List<String> notNull;
    List<String> dfltValue;
    List<String> primaryKey;


    public List<String> getFieldNames() {
        return fieldNames;
    }

    public void setFieldName(List<String> tableName) {
        this.fieldNames = tableName;
    }

    public List<String> getFieldtype() {
        return fieldtype;
    }

    public void setFieldtype(List<String> fieldtype) {
        this.fieldtype = fieldtype;
    }

    public List<String> getNotNull() {
        return notNull;
    }

    public void setNotNull(List<String> notNull) {
        this.notNull = notNull;
    }

    public List<String> getDfltValue() {
        return dfltValue;
    }

    public void setDfltValue(List<String> dfltValue) {
        this.dfltValue = dfltValue;
    }

    public List<String> getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(List<String> primaryKey) {
        this.primaryKey = primaryKey;
    }
}
