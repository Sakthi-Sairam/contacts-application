package com.QueryLayer;

import java.util.*;

public class Query {
    private String queryString;
    private int numberOfCols;
    private List<Object> parameters;
    private QueryType queryType;

    public Query() {
        parameters = new ArrayList<>();
    }

    public int getNumberOfCols() {
        return numberOfCols;
    }

    public void setNumberOfCols(int numberOfCols) {
        this.numberOfCols = numberOfCols;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getQueryString() {
        return queryString;
    }
    public void setQueryType(QueryType queryType){
        this.queryType = queryType;
    }
    public QueryType getQueryType(){
        return this.queryType;
    }

    public void addParameter(Object param) {
        this.parameters.add(param);
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public Object[] getParametersArray() {
        return parameters.toArray();
    }
}