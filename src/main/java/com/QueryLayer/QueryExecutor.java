package com.QueryLayer;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.*;

import com.models.Column;
import com.utils.DBConnection;

import java.util.*;


public class QueryExecutor {
    private Connection connection;
    private boolean isTransaction = false;
    
    /**
     * Establishes and returns a database connection.
     * @return Connection object representing the database connection
     * @throws RuntimeException if connection fails
     */
    private Connection getConnection() {
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
        try {
            String sql = query.build();
            System.out.println("Executing SQL: " + sql);

            if (!isTransaction) {
                connection = getConnection();
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

//                ResultSetMetaData rsMetaData = resultSet.getMetaData();
//                int columnCount = rsMetaData.getColumnCount();
//
//                // Debug: Print column names
//                Set<String> columnNames = new HashSet<>();
//                for (int i = 1; i <= columnCount; i++) {
//                    String columnName = rsMetaData.getColumnLabel(i);
//                    columnNames.add(columnName);
//                    System.out.println("Column " + i + ": " + columnName);
//                }

                List<T> results = mapResultSetToClass(resultSet, clazz);
                return results;
            } finally {
                if (!isTransaction) {
                    closeConnection();
                }
            }
        } catch (Exception e) {
            System.err.println("Error executing query: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Maps ResultSet to class instances, handling grouping and nested fields.
     */
    private <T> List<T> mapResultSetToClass(ResultSet resultSet, Class<T> clazz) throws Exception {
        List<T> results = new ArrayList<>();
        ResultSetMetaData rsMetaData = resultSet.getMetaData();
        Map<Object, T> groupedResults = new HashMap<>();
        Set<String> columnNames = extractColumnNames(rsMetaData);

        while (resultSet.next()) {
            Object groupingKey = determineGroupingKey(resultSet);
            T instance = groupedResults.get(groupingKey);

            if (instance == null) {
                instance = clazz.getDeclaredConstructor().newInstance();
                populateMainFields(resultSet, instance, columnNames);
                groupedResults.put(groupingKey, instance);
            }

            if (hasNestedFields(instance)) {
                populateNestedFields(resultSet, instance, columnNames);
            }
        }

        results.addAll(groupedResults.values());
        return results;
    }

    /**
     * Extracts column names from ResultSetMetaData.
     */
    private Set<String> extractColumnNames(ResultSetMetaData rsMetaData) throws SQLException {
        Set<String> columnNames = new HashSet<>();
        int columnCount = rsMetaData.getColumnCount();
        
        for (int i = 1; i <= columnCount; i++) {
        	String tableName = rsMetaData.getTableName(i);
            String columnName = rsMetaData.getColumnLabel(i);

            columnNames.add((tableName + "." + columnName).toLowerCase());
        }
        
        System.out.println("Extracted column names: " + columnNames);
        return columnNames;
    }

    /**
     * Populates main (non-list) fields of the object.
     */
    private <T> void populateMainFields(ResultSet resultSet, T instance, Set<String> columnNames) 
            throws SQLException, IllegalAccessException {
        for (Field field : instance.getClass().getDeclaredFields()) {
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                field.setAccessible(true);
                String columnName = column.name().toLowerCase();

                if (columnNames.contains(columnName)) {
                    Object value = resultSet.getObject(columnName);

                    if (value != null) {
                        // Type conversion
                        value = convertValue(value, field.getType());
                        field.set(instance, value);
                    }
                }
            }
        }
    }

    /**
     * Converts values to match the target field type.
     */
    private Object convertValue(Object value, Class<?> targetType) {
        if (value == null) return null;

        // Boolean to Integer conversion
        if (targetType == int.class || targetType == Integer.class) {
            if (value instanceof Boolean) {
                return ((Boolean) value) ? 1 : 0;
            } else if (value instanceof Number) {
                return ((Number) value).intValue();
            }
        }

        // Integer/Number to Boolean conversion
        if (targetType == boolean.class || targetType == Boolean.class) {
            if (value instanceof Number) {
                return ((Number) value).intValue() == 1;
            }
        }

        return value;
    }

 
    private <T> void populateNestedFields(ResultSet resultSet, T instance, Set<String> columnNames) 
            throws Exception {
        for (Field field : instance.getClass().getDeclaredFields()) {
            if (List.class.isAssignableFrom(field.getType())) {
                field.setAccessible(true);

                // Get the type of list elements
                ParameterizedType listType = (ParameterizedType) field.getGenericType();
                Class<?> nestedClass = (Class<?>) listType.getActualTypeArguments()[0];

                // Create a new nested instance
                Object nestedInstance = nestedClass.getDeclaredConstructor().newInstance();
                boolean populated = false;

                // Check and populate nested fields
                for (Field nestedField : nestedClass.getDeclaredFields()) {
                    Column column = nestedField.getAnnotation(Column.class);
                    if (column != null) {
                        nestedField.setAccessible(true);
                        String columnName = column.name().toLowerCase();

                        if (columnNames.contains(columnName)) {
                            Object value = resultSet.getObject(columnName);
                            if (value != null) {
                                value = convertValue(value, nestedField.getType());
                                nestedField.set(nestedInstance, value);
                                populated = true;
                            }
                        }
                    }
                }

                // Add to list if populated
                if (populated) {
                    List<Object> nestedList = (List<Object>) field.get(instance);
                    if (nestedList == null) {
                        nestedList = new ArrayList<>();
                        field.set(instance, nestedList);
                    }
                    
                    // Prevent duplicates
                    if (!nestedList.contains(nestedInstance)) {
                        nestedList.add(nestedInstance);
                    }
                }
            }
        }
    }

    /**
     * Checks if the instance has any list/nested fields.
     */
    private <T> boolean hasNestedFields(T instance) {
        for (Field field : instance.getClass().getDeclaredFields()) {
            if (List.class.isAssignableFrom(field.getType())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines a unique key for grouping results.
     */
    private Object determineGroupingKey(ResultSet resultSet) {
        try {
            return resultSet.getObject(1);
        } catch (SQLException e) {
            System.err.println("Error determining grouping key, defaulting to UUID: " + e.getMessage());
            return UUID.randomUUID().toString();
        }
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