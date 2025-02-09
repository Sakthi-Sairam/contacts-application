package com.server;

import com.exceptions.DaoException;
import com.exceptions.ErrorCode;
import com.exceptions.QueryExecutorException;
import com.models.ServerRegistry;
import com.queryLayer.QueryBuilder;
import com.queryLayer.QueryExecutor;
import com.queryLayer.databaseSchemaEnums.ServerRegistryColumn;
import com.queryLayer.databaseSchemaEnums.Table;

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
		long curTime = System.currentTimeMillis();

		try {
			qb.insert(Table.SERVER_REGISTRY).columns(ServerRegistryColumn.IP_ADDRESS, ServerRegistryColumn.PORT_NUMBER, ServerRegistryColumn.CREATED_AT)
					.values(ipAddress, portNumber, curTime);

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
			qb.select(ServerRegistryColumn.ID.getAllColumns())
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
}
