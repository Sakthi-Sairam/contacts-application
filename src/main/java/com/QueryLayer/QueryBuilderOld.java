package com.QueryLayer;

import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.utils.DBConnection;

public class QueryBuilderOld {

    private Query query;

    public QueryBuilderOld() {
        this.query = new Query();
    }
    
    //to get the number of columns received:
    public int getNumberOfCols() {
    	return query.getNumberOfCols();
    }
    //------------------------------------------------

    // for select statements
    public QueryBuilderOld select(String... columns) {
        query.setQueryType(QueryType.SELECT);
        query.setNumberOfCols(columns.length);
        String selectQuery = "SELECT " + String.join(", ", columns);
        query.setQueryString(selectQuery);
        return this;
    }

    public QueryBuilderOld from(String table) {
        String queryString = query.getQueryString() + " FROM " + table;
        query.setQueryString(queryString);
        return this;
    }

    public QueryBuilderOld limit(int limit) {
        String queryString = query.getQueryString();
        queryString += " LIMIT " + limit;
        query.setQueryString(queryString);
        return this;
    }

    public QueryBuilderOld where(String col, String condition, Object value,boolean isWhere){
        if(!isWhere) return where(col, condition, value);

        String queryString = query.getQueryString();
        queryString += (" where " + col + condition + formatValue(value));
        query.setQueryString(queryString);
        return this;
    }

    public QueryBuilderOld where(String col, String condition, Object value){
        String queryString = query.getQueryString();
        queryString += (" "+col + condition + formatValue(value));
        query.setQueryString(queryString);
        return this;
    }

    public QueryBuilderOld and(){
        query.setQueryString(query.getQueryString()+" and ");
        return this;
    }
    public QueryBuilderOld join(String tableName, String foreignKey1, String foreignKey2){
        String queryString = query.getQueryString();
        queryString += String.format(" JOIN %s ON %s = %s",tableName,foreignKey1,foreignKey2);
        query.setQueryString(queryString);
        return this;
    }

    //for insert statements
    public QueryBuilderOld insert(String tableName){
        query.setQueryType(QueryType.INSERT);
        String insertQuery = "INSERT INTO "+tableName;
        query.setQueryString(insertQuery);
        return this;
    }
    public QueryBuilderOld columns(String... columns){
        String queryString   =  query.getQueryString()+" ("+String.join(", ", columns)+")";
        query.setQueryString(queryString);
        return this;
    }
    public QueryBuilderOld values(Object... values) {
        StringBuilder queryString = new StringBuilder(query.getQueryString() + " VALUES (");
        String prefix = "";
        for (Object val : values) {
            queryString.append(prefix).append(formatValue(val));
            prefix = ", ";
        }
        queryString.append(")");
        query.setQueryString(queryString.toString());
        return this;
    }

    //for delete statements
    public QueryBuilderOld delete(String tableName) {
        query.setQueryType(QueryType.DELETE);
        String deleteQuery = "DELETE FROM " + tableName;
        query.setQueryString(deleteQuery);
        return this;
    }

    // returning the value
    public String build() {
    	System.out.println(query.getQueryString());
        return query.getQueryString() + ";";
    }

	@SuppressWarnings("unchecked")
	public <T> T execute() throws SQLException {
        String sql = build();
        try(Connection connection = DBConnection.getConnection()){ 
	        if(query.getQueryType()==QueryType.SELECT) {
	            return (T) executeSelect(connection, sql);
	        }else if(query.getQueryType()==QueryType.INSERT || query.getQueryType()==QueryType.UPDATE || query.getQueryType()==QueryType.DELETE) {
	            return (T) Integer.valueOf(executeUpdate(connection, sql));
	        }else {
	            throw new UnsupportedOperationException("Query type not supported");
	        }
        }catch(Exception e){
        	System.out.println(e);
        	return null;
        }
    }
    private List<Object> executeSelect(Connection connection, String sql) throws SQLException {
        List<Object> results = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                for(int i = 1; i < query.getNumberOfCols()+1; i++){
                    results.add(resultSet.getObject(i));
                }
            }
        }
        return results;
    }

    private int executeUpdate(Connection connection, String sql) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            return preparedStatement.executeUpdate();
        }
    }




    // helper method
    private String formatValue(Object value) {
        if (value instanceof String) {
            return "'" + value + "'";
        } else if (value instanceof Timestamp) {
            return "'" + value.toString() + "'"; // Enclose Timestamp in quotes
        } 
        else {
            return value.toString();
        }
    }
}
