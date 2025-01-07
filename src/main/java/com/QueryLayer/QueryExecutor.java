package com.QueryLayer;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.*;
import com.models.Column;
import com.utils.DBConnection;
import com.exceptions.QueryExecutorException;
import com.exceptions.ErrorCode;

public class QueryExecutor {
    private Connection connection;
    private boolean isTransaction = false;

    /**
     * Establishes and returns a database connection.
     * Throws QueryExecutorException if connection fails.
     */
    private Connection getConnection() throws QueryExecutorException {
        try {
            connection = DBConnection.getConnection();
            return connection;
        } catch (QueryExecutorException e) {
            throw new QueryExecutorException(ErrorCode.DATABASE_CONNECTION_ERROR, "Failed to establish database connection", e);
        }
    }

    /**
     * Starts a new database transaction.
     * Throws QueryExecutorException if transaction initialization fails.
     */
    public void transactionStart() throws QueryExecutorException {
        if (isTransaction) {
            throw new QueryExecutorException(ErrorCode.TRANSACTION_ERROR, "A transaction is already active.");
        }
        this.connection = getConnection();
        this.isTransaction = true;
        try {
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new QueryExecutorException(ErrorCode.TRANSACTION_ERROR, "Failed to start transaction", e);
        }
    }
    
    /**
     * Commits and ends the current transaction.
     * @throws QueryExecutorException if commit fails or connection cannot be closed
     */
    public void transactionEnd() throws QueryExecutorException {
        if (!isTransaction) {
            throw new QueryExecutorException(ErrorCode.TRANSACTION_ERROR, "No active transaction to end.");
        }
        try {
            connection.commit();
            isTransaction = false;
        } catch (SQLException e) {
            throw new QueryExecutorException(ErrorCode.TRANSACTION_ERROR, "Failed to commit transaction", e);
        } finally {
            closeConnection();
        }
    }
    
    /**
     * Executes an INSERT, UPDATE, or DELETE query.
     * @param sql QueryBuilder object containing the query to execute
     * @return number of rows affected by the query
     * @throws QueryExecutorException if query execution fails
     */
    public int executeUpdate(QueryBuilder query) throws QueryExecutorException {
        String sql = query.build();
        if (!isTransaction) {
            connection = getConnection();
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new QueryExecutorException(ErrorCode.DATABASE_ERROR, "Rollback failed",rollbackEx);
            }
        	throw new QueryExecutorException(ErrorCode.QUERY_EXECUTION_FAILED, "Error executing update query: " + sql, e);
        } finally {
            if (!isTransaction) {
                closeConnection();
            }
        }
    }

    public Pair executeUpdateWithGeneratedKeys(QueryBuilder sql) throws QueryExecutorException {
        if (!isTransaction) {
            connection = getConnection();
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql.build(), Statement.RETURN_GENERATED_KEYS)) {
            int rowCount = preparedStatement.executeUpdate();

            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                if (rs.next()) {
                    return new Pair(rowCount, rs.getInt(1));
                }
                return new Pair(rowCount, -1);
            }
        } catch (SQLException e) {
            if (isTransaction) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    throw new QueryExecutorException(ErrorCode.DATABASE_ERROR, "Rollback error",rollbackEx);
                }
            }
            throw new QueryExecutorException(ErrorCode.QUERY_EXECUTION_FAILED,"Error executing the query" ,e);
        } finally {
            if (!isTransaction) {
                closeConnection();
            }
        }
    }

    public void executeTransaction(List<QueryBuilder> sqlQueries) throws QueryExecutorException {
        transactionStart();
        try {
            for (QueryBuilder query : sqlQueries) {
                executeUpdate(query);
            }
            transactionEnd();
        } finally {
            isTransaction = false;
            closeConnection();
        }
    }
    
    /**
     * Executes an Select query and returns the respective model (POJO).
     * @param query QueryBuilder object containing the INSERT query
     * @param clazz class of the model
     * @return model reference
     * @throws QueryExecutorException if query execution fails
     */
    public <T> List<T> executeQuery(QueryBuilder query, Class<T> clazz) throws QueryExecutorException {
        try {
            String sql = query.build();
            System.out.println("Executing SQL: " + sql);

            if (!isTransaction) {
                connection = getConnection();
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                return mapResultSetToClass(resultSet, clazz);
            }
        } catch (Exception e) {
        	if(isTransaction) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackEx) {
                    throw new QueryExecutorException(ErrorCode.DATABASE_ERROR, "Rollback failed",rollbackEx);
                }
        	}
        	throw new QueryExecutorException(ErrorCode.QUERY_EXECUTION_FAILED, "Execution failed",e);
        } finally {
            if (!isTransaction) {
                closeConnection();
            }
        }
    }

    private void closeConnection() throws QueryExecutorException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            throw new QueryExecutorException(ErrorCode.DATABASE_CONNECTION_ERROR, "Failed to close database connection", e);
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
    

}