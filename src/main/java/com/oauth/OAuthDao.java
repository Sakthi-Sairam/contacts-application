package com.oauth;

import java.util.List;

import com.exceptions.DaoException;
import com.exceptions.ErrorCode;
import com.exceptions.QueryExecutorException;
import com.models.OAuthToken;
import com.queryLayer.QueryBuilder;
import com.queryLayer.QueryExecutor;
import com.queryLayer.databaseSchemaEnums.OAuthTokensColumn;
import com.queryLayer.databaseSchemaEnums.Table;
import com.queryLayer.databaseSchemaEnums.UserDataColumn;

public class OAuthDao {
	public static boolean insertOAuthRecord(int userId, String refreshToken, String email) throws DaoException {
	    QueryBuilder qb = new QueryBuilder();
	    QueryExecutor executor = new QueryExecutor();
		long currTime = System.currentTimeMillis();
		
		try {
			qb.insert(Table.OAUTH_TOKENS).columns(OAuthTokensColumn.USER_ID,OAuthTokensColumn.REFRESH_TOKEN,OAuthTokensColumn.EMAIL,OAuthTokensColumn.CREATED_AT,OAuthTokensColumn.UPDATED_AT).values(userId,refreshToken,email,currTime,currTime);
			int rowsAffected = executor.executeUpdate(qb);
			return rowsAffected>0;
		}catch (QueryExecutorException e) {
            throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to add the tokens in the db" + e.getMessage(), e);
		}
	}
	
    public static List<OAuthToken> getOAuthTokensByUserId(int userId) throws DaoException {
        QueryBuilder qb = new QueryBuilder();
        QueryExecutor executor = new QueryExecutor();
        try {
			qb.select(OAuthTokensColumn.ID.getAllColumns())
			  .from(Table.OAUTH_TOKENS)
			  .where(OAuthTokensColumn.USER_ID, "=", userId, true);

			List<OAuthToken> oauthAccounts = executor.executeQuery(qb, OAuthToken.class);

			return oauthAccounts;
		} catch (QueryExecutorException e) {
            throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to get the oauth accounts" + e.getMessage(), e);

		}
    }
    
    public static List<OAuthToken> getAllOAuthTokens() throws DaoException {
        QueryBuilder qb = new QueryBuilder();
        QueryExecutor executor = new QueryExecutor();
        try {
			qb.select(OAuthTokensColumn.ID.getAllColumns())
			  .from(Table.OAUTH_TOKENS);

			List<OAuthToken> oauthAccounts = executor.executeQuery(qb, OAuthToken.class);

			return oauthAccounts;
		} catch (QueryExecutorException e) {
            throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to get the oauth accounts" + e.getMessage(), e);

		}
    }

	public static void updateSyncInterval(int tokenId, long syncInterval) throws DaoException {
		QueryBuilder qb = new QueryBuilder();
	    QueryExecutor executor = new QueryExecutor();
		long currTime = System.currentTimeMillis();
	    try {
	        qb.update(Table.OAUTH_TOKENS)
	          .set(OAuthTokensColumn.SYNC_INTERVAL, syncInterval)
	          .set(OAuthTokensColumn.UPDATED_AT, currTime)
	          .where(OAuthTokensColumn.ID, "=", tokenId, true);
	        executor.executeUpdate(qb);
	    } catch (QueryExecutorException e) {
	        throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to update sync interval", e);
	    }	
	}
	public static void updateLastSync(int tokenId) throws DaoException {
	    QueryBuilder qb = new QueryBuilder();
	    QueryExecutor executor = new QueryExecutor();
		long currTime = System.currentTimeMillis();
	    try {
	        qb.update(Table.OAUTH_TOKENS)
	          .set(OAuthTokensColumn.LAST_SYNC, currTime)
	          .set(OAuthTokensColumn.UPDATED_AT, currTime)
	          .where(OAuthTokensColumn.ID, "=", tokenId, true);
	        executor.executeUpdate(qb);
	    } catch (QueryExecutorException e) {
	        throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to update last sync", e);
	    }
	}

	public static boolean isSyncEmailPresent(String email, int userId) throws DaoException {
		QueryBuilder qb = new QueryBuilder();
		QueryExecutor executor = new QueryExecutor();
		List<OAuthToken> result = null;
		try {
			qb.select(OAuthTokensColumn.ID.getAllColumns())
			.from(Table.OAUTH_TOKENS)
			.where(OAuthTokensColumn.EMAIL, "=", email, true).and().where(OAuthTokensColumn.USER_ID, "=", userId);
			
			result = executor.executeQuery(qb, OAuthToken.class);
			
		}catch (QueryExecutorException e) {
	        throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to select the token", e);
		}
		return result.size()>0;
	}
	
	
}
