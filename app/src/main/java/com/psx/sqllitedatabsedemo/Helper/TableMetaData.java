package com.psx.sqllitedatabsedemo.Helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pranav on 12-06-2017.
 */

public class TableMetaData {
    List<String> columnNames = new ArrayList<>();
    List<String> coulumnDataTypes = new ArrayList<>();
    List<String> columnPrimaryKey = new ArrayList<>();
    List<String> columnDefaultValue = new ArrayList<>();
    List<String> columnNotNull = new ArrayList<>();

    public List<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    public List<String> getCoulumnDataTypes() {
        return coulumnDataTypes;
    }

    public void setCoulumnDataTypes(List<String> coulumnDataTypes) {
        this.coulumnDataTypes = coulumnDataTypes;
    }

    public List<String> getColumnPrimaryKey() {
        return columnPrimaryKey;
    }

    public void setColumnPrimaryKey(List<String> columnPrimaryKey) {
        this.columnPrimaryKey = columnPrimaryKey;
    }

    public List<String> getColumnDefaultValue() {
        return columnDefaultValue;
    }

    public void setColumnDefaultValue(List<String> columnDefaultValue) {
        this.columnDefaultValue = columnDefaultValue;
    }

    public List<String> getColumnNotNull() {
        return columnNotNull;
    }

    public void setColumnNotNull(List<String> columnNotNull) {
        this.columnNotNull = columnNotNull;
    }
}
