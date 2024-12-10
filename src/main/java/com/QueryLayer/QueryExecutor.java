package com.QueryLayer;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;

import com.models.Column;
import com.utils.DBConnection;

import java.util.*;

class Pair {
    int rowCount;
    int generatedKey;
    Pair(int rowCount, int generatedKey) {
        this.rowCount = rowCount;
        this.generatedKey = generatedKey;
    }
}

public class QueryExecutor {
    private Connection connection;
    private boolean isTransaction = false;
    
    /**
     * Establishes and returns a database connection.
     * @return Connection object representing the database connection
     * @throws RuntimeException if connection fails
     */
    public Connection getConnection() {
    	connection = DBConnection.getConnection();
        return connection;
    }

    /**
     * Starts a new database transaction.
     * @throws SQLException if transaction initialization fails
     */
    public void transactionStart() throws SQLException {
        if (isTransaction) {
            throw new SQLException("Transaction already in progress");
        }
        this.connection = getConnection();
        this.isTransaction = true;
        this.connection.setAutoCommit(false);
    }

    /**
     * Commits and ends the current transaction.
     * @throws SQLException if commit fails or connection cannot be closed
     */
    public void transactionEnd() throws SQLException {
        if (!isTransaction) {
            throw new SQLException("No active transaction to end");
        }
        try {
            connection.commit();
            isTransaction = false;
        } finally {
            closeConnection();
        }
    }

    /**
     * Executes a SELECT query and returns the results.
     * @param query QueryBuilder object containing the query to execute
     * @return List of Objects array containing the query results
     * @throws SQLException if query execution fails
     */
    public List<Object[]> executeQuery(QueryBuilder query) throws SQLException {
        List<Object[]> results = new ArrayList<>();
        String sql = query.build();
        System.out.println(sql);
        int numberOfCols = query.getNumberOfCols();

        if (!isTransaction) {
            connection = getConnection();
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Object tempArr[] = new Object[numberOfCols];
                for (int i = 1; i <= numberOfCols; i++) {
                    tempArr[i-1] = resultSet.getObject(i);
                }
                results.add(tempArr);
            }
            System.out.println(results);
            return results;
        } finally {
            if (!isTransaction) {
                closeConnection();
            }
        }
    }

    /**
     * Executes an INSERT, UPDATE, or DELETE query.
     * @param sql QueryBuilder object containing the query to execute
     * @return number of rows affected by the query
     * @throws SQLException if query execution fails
     */
    public int executeUpdate(QueryBuilder sql) throws SQLException {
        if (!isTransaction) {
            connection = getConnection();
        }
        System.out.println(sql.build());


        try (PreparedStatement preparedStatement = connection.prepareStatement(sql.build())) {
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            if (isTransaction) {
                connection.rollback();
            }
            throw e;
        } finally {
            if (!isTransaction) {
                closeConnection();
            }
        }
    }

    /**
     * Executes an INSERT query and returns the generated keys.
     * @param sql QueryBuilder object containing the INSERT query
     * @return Pair containing row count and generated key (-1 if no key generated)
     * @throws SQLException if query execution fails
     */
    public Pair executeUpdateWithGeneratedKeys(QueryBuilder sql) throws SQLException {
        if (!isTransaction) {
            connection = getConnection();
        }

        try (PreparedStatement preparedStatement =
                     connection.prepareStatement(sql.build(), Statement.RETURN_GENERATED_KEYS)) {

            int rowCount = preparedStatement.executeUpdate();

            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                if (rs.next()) {
                    return new
                            Pair(rowCount, rs.getInt(1));
                }
                return new Pair(rowCount, -1); // No keys generated
            }
        } catch (SQLException e) {
            if (isTransaction) {
                connection.rollback();
            }
            throw e;
        } finally {
            if (!isTransaction) {
                closeConnection();
            }
        }
    }

    /**
     * Executes multiple queries in a transaction.
     * @param sqlQueries List of QueryBuilder objects containing the queries
     * @throws SQLException if any query fails
     */
    public void executeTransaction(List<QueryBuilder> sqlQueries) throws SQLException {
        transactionStart();
        try {
            for (QueryBuilder query : sqlQueries) {
                executeUpdate(query);
            }
            transactionEnd();
        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            isTransaction = false;
            closeConnection();
        }
    }
    
    
    public <T> List<T> executeQuery(QueryBuilder query, Class<T> clazz) {
    	try{
        List<T> results = new ArrayList<>();
        String sql = query.build();
        System.out.println(sql);

        if (!isTransaction) {
            connection = getConnection();
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            // Get the number of columns
        	ResultSetMetaData rsMetaData = resultSet.getMetaData();
            int columnCount = rsMetaData.getColumnCount();
            
            // Loop through all the columns and print their names
            for (int i = 1; i <= columnCount; i++) {
                String columnName = rsMetaData.getColumnName(i);
                System.out.println("Column " + i + ": " + columnName);
            }

            while (resultSet.next()) {
                T instance = mapResultSetToClass(resultSet, clazz);
                results.add(instance);
            }
            return results;
        } finally {
            if (!isTransaction) {
                closeConnection();
            }
        }
    }catch(Exception e){
    	System.out.println(e);
    }
		return null;
    	}
   

    /**
     * Maps a single ResultSet row to an instance of the specified class.
     * @param resultSet ResultSet containing the data
     * @param clazz Target class type
     * @return Object instance populated with data from the ResultSet
     * @throws SQLException if ResultSet access fails
     */
//    private <T> T mapResultSetToClass(ResultSet resultSet, Class<T> clazz) throws SQLException {
//        try {
//            T instance = clazz.getDeclaredConstructor().newInstance();
//            
//            for (Field field : clazz.getDeclaredFields()) {
//                Column column = field.getAnnotation(Column.class);
//                if (column != null) {
//                    try {
//                        String columnName = column.name();
//                        Object value = resultSet.getObject(columnName);
//
//                        field.setAccessible(true);
//                        
//                        // Handle type conversion if needed
//                        if (value != null) {
//                            // If the field type doesn't match exactly, try to convert
//                            if (!field.getType().isAssignableFrom(value.getClass())) {
//                                if (field.getType() == int.class || field.getType() == Integer.class) {
//                                    value = ((Number) value).intValue();
//                                }
//                                // Add more type conversions as needed
//                            }
//                            field.set(instance, value);
//                        }
//                    } catch (Exception fieldSetException) {
//                        System.err.println("Error setting field: " + field.getName() + 
//                                           " - " + fieldSetException.getMessage());
//                    }
//                }
//            }
//            return instance;
//        } catch (Exception e) {
//            throw new SQLException("Error mapping ResultSet to class: " + clazz.getSimpleName(), e);
//        }
//    }
    
    private <T> T mapResultSetToClass(ResultSet resultSet, Class<T> clazz) {
    	System.out.println("\n coming here da venna \n");
        try {
            ResultSetMetaData rsMetaData = resultSet.getMetaData();
            
            // Get the number of columns
            int columnCount = rsMetaData.getColumnCount();
            
            // Loop through all the columns and print their names
            for (int i = 1; i <= columnCount; i++) {
                String columnName = rsMetaData.getColumnName(i);
                System.out.println("Column " + i + ": " + columnName);
            }
            T instance = clazz.getDeclaredConstructor().newInstance();
            
            // Get all declared fields;
            List<Field> fields = new ArrayList<>();
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            
            for (Field field : fields) {
                Column column = field.getAnnotation(Column.class);
                System.out.println(field.toString()+">>"+column);
                if (column != null) {
                    try {
                        String columnName = column.name();
                        System.out.println(columnName);
                        
                        // Check if column exists in ResultSet
                        try {
                            resultSet.findColumn(columnName);
                        } catch (SQLException e) {
                            System.err.println("Column not found: " + columnName);
                            continue;
                        }
                        Object value = resultSet.getObject(columnName);
                        field.setAccessible(true);
                        
                        // Handle type conversion
                        if (value != null) {
                            // Specific type conversions
                            if (field.getType() == int.class || field.getType() == Integer.class) {
                                value = ((Number) value).intValue();
                            } else if (field.getType() == long.class || field.getType() == Long.class) {
                                value = ((Number) value).longValue();
                            } else if (field.getType() == double.class || field.getType() == Double.class) {
                                value = ((Number) value).doubleValue();
                            } else if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                            	value = ((Number) value).intValue() == 0? false : true;
                            }
                            
                            field.set(instance, value);
                        }
                    } catch (Exception fieldSetException) {
                        System.err.println("Error setting field: " + field.getName() + 
                                           " - " + fieldSetException.getMessage());
                    }
                }
            }
            return instance;
        } catch (Exception e) {
//            throw new SQLException("Error mapping ResultSet to class: " + clazz.getSimpleName(), e);
        	System.out.println("error is here in executor "+e);
        }
		return null;
    }


    /**
     * Closes the database connection if it exists and is open.
     * @throws SQLException if connection cannot be closed
     */
    public void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            connection = null;
        }
    }
}