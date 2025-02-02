package com.dao;

import com.exceptions.DaoException;
import com.exceptions.QueryExecutorException;
import com.models.Session;
import com.queryLayer.QueryBuilder;
import com.queryLayer.QueryExecutor;
import com.queryLayer.DatabaseSchemaEnums.SessionColumn;
import com.queryLayer.DatabaseSchemaEnums.Table;
import com.exceptions.ErrorCode;

import java.util.List;

public class SessionDao {

    /**
     * Batch updates sessions' last accessed time.
     */
    public static void batchUpdateSessions(List<Session> sessionsToUpdate) throws DaoException {
        QueryExecutor executor = new QueryExecutor();
        try {
            QueryBuilder qb = new QueryBuilder();
            executor.transactionStart();
            for (Session session : sessionsToUpdate) {
                qb.update(Table.SESSIONS)
                  .set(SessionColumn.LAST_ACCESSED_TIME, session.getLastAccessedTime())
                  .where(SessionColumn.SESSION_ID, "=", session.getSessionId(), true);
                executor.executeUpdate(qb);
            }
            executor.transactionEnd();
        } catch (QueryExecutorException e) {
            throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to batch update sessions: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes expired sessions.
     */
    public static void deleteExpiredSessions(long expirationTime) throws DaoException {
    	QueryBuilder qb = new QueryBuilder();
        QueryExecutor executor = new QueryExecutor();
        try {
                qb.delete(Table.SESSIONS)
                .where(SessionColumn.LAST_ACCESSED_TIME, "<", expirationTime, true);
                executor.executeUpdate(qb);
        } catch (QueryExecutorException e) {
            throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to delete expired sessions: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a session by its ID.
     */
    public static void deleteSessionById(String id) throws DaoException {
    	QueryBuilder qb = new QueryBuilder();
        QueryExecutor executor = new QueryExecutor();
        try {
                qb.delete(Table.SESSIONS)
                .where(SessionColumn.SESSION_ID, "=", id, true);
                executor.executeUpdate(qb);
        } catch (QueryExecutorException e) {
            throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to delete session: " + e.getMessage(), e);
        }
    }

    /**
     * Creates a new session.
     */
    public static void createSession(String sessionId, int userId, long lastAccessedTime, long createdAt) throws DaoException {
    	QueryBuilder qb = new QueryBuilder();
        QueryExecutor executor = new QueryExecutor();
    	try {
            qb.insert(Table.SESSIONS)
                .columns(SessionColumn.SESSION_ID, SessionColumn.USER_ID, SessionColumn.LAST_ACCESSED_TIME, SessionColumn.CREATED_AT)
                .values(sessionId, userId, lastAccessedTime, createdAt);
            executor.executeUpdate(qb);
        } catch (QueryExecutorException e) {
            throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to create session: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves a session by its ID.
     */
    public static Session getSession(String sessionId) throws DaoException {
        QueryBuilder qb = new QueryBuilder();
        QueryExecutor executor = new QueryExecutor();
        try {
            qb.select(SessionColumn.SESSION_ID, SessionColumn.USER_ID, SessionColumn.LAST_ACCESSED_TIME, SessionColumn.CREATED_AT)
              .from(Table.SESSIONS)
              .where(SessionColumn.SESSION_ID, "=", sessionId, true);

            List<Session> results = executor.executeQuery(qb, Session.class);
            Session result = results.isEmpty() ? null : results.get(0);
            return result;
        } catch (QueryExecutorException e) {
            throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to retrieve session: " + e.getMessage(), e);
        }
    }
}
