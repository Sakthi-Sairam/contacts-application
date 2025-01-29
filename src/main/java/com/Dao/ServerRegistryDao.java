package com.Dao;

import com.QueryLayer.QueryBuilder;
import com.QueryLayer.QueryExecutor;
import com.QueryLayer.DatabaseSchemaEnums.ServerRegistryColumn;
import com.QueryLayer.DatabaseSchemaEnums.Table;
import com.exceptions.DaoException;
import com.exceptions.ErrorCode;
import com.exceptions.QueryExecutorException;
import com.models.ServerRegistry;

import java.sql.SQLException;
import java.util.List;

public class ServerRegistryDao {

	/**
	 * Registers a new server in the server_registry table.
	 *
	 * @param ipAddress  the IP address of the server
	 * @param portNumber the port number of the server
	 * @return true if the server was successfully registered, false otherwise
	 * @throws SQLException if a database error occurs
	 * @throws DaoException
	 */
	public static boolean registerServer(String ipAddress, int portNumber) throws DaoException {
		QueryExecutor executor = new QueryExecutor();
		QueryBuilder qb = new QueryBuilder();

		try {
			qb.insert(Table.SERVER_REGISTRY).columns(ServerRegistryColumn.IP_ADDRESS, ServerRegistryColumn.PORT_NUMBER)
					.values(ipAddress, portNumber);

			int rowCount = executor.executeUpdate(qb);
			return rowCount > 0;
		} catch (QueryExecutorException e) {
			throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to register server " + e.getMessage(), e);
		}
	}

	/**
	 * Retrieves all servers currently registered in the server_registry table.
	 *
	 * @return a list of ServerRegistry objects representing the servers
	 * @throws SQLException if a database error occurs
	 * @throws DaoException
	 */
	public static List<ServerRegistry> getAllServers() throws DaoException {
		QueryBuilder qb = new QueryBuilder();
		QueryExecutor executor = new QueryExecutor();

		try {
			qb.select(ServerRegistryColumn.ID, ServerRegistryColumn.IP_ADDRESS, ServerRegistryColumn.PORT_NUMBER,
					ServerRegistryColumn.REGISTERED_AT, ServerRegistryColumn.LAST_HEARTBEAT)
					.from(Table.SERVER_REGISTRY);

			return executor.executeQuery(qb, ServerRegistry.class);
		} catch (QueryExecutorException e) {
			throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to get all servers: " + e.getMessage(), e);
		}
	}

	/**
	 * Removes a server from the server_registry table by its IP address and port
	 * number.
	 *
	 * @param ipAddress  the IP address of the server to remove
	 * @param portNumber the port number of the server to remove
	 * @return true if the server was successfully removed, false otherwise
	 * @throws DaoException
	 * @throws SQLException if a database error occurs
	 */
	public static boolean deregisterServer(String ipAddress, int portNumber) throws DaoException {
		QueryBuilder qb = new QueryBuilder();
		QueryExecutor executor = new QueryExecutor();

		try {
			qb.delete(Table.SERVER_REGISTRY).where(ServerRegistryColumn.IP_ADDRESS, "=", ipAddress, true).and()
					.where(ServerRegistryColumn.PORT_NUMBER, "=", portNumber);

			int rowCount = executor.executeUpdate(qb);
			return rowCount > 0;
		} catch (QueryExecutorException e) {
			throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED,
					"Failed to deregister the server " + e.getMessage(), e);
		}
	}

	/**
	 * Updates the last_heartbeat timestamp for a specific server.
	 *
	 * @param ipAddress  the IP address of the server to update
	 * @param portNumber the port number of the server to update
	 * @return true if the update was successful, false otherwise
	 * @throws DaoException
	 * @throws SQLException if a database error occurs
	 */
	public static boolean updateLastHeartbeat(String ipAddress, int portNumber) throws DaoException {
		QueryBuilder qb = new QueryBuilder();
		QueryExecutor executor = new QueryExecutor();

		try {
			qb.update(Table.SERVER_REGISTRY).set(ServerRegistryColumn.LAST_HEARTBEAT, "CURRENT_TIMESTAMP")
					.where(ServerRegistryColumn.IP_ADDRESS, "=", ipAddress, true).and()
					.where(ServerRegistryColumn.PORT_NUMBER, "=", portNumber);

			int rowCount = executor.executeUpdate(qb);
			return rowCount > 0;
		} catch (QueryExecutorException e) {
			throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED,
					"Failed to update last heartbeat " + e.getMessage(), e);
		}
	}
}
