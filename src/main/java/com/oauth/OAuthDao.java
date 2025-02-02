package com.oauth;

import java.util.List;

import com.exceptions.DaoException;
import com.exceptions.ErrorCode;
import com.exceptions.QueryExecutorException;
import com.models.OAuthToken;
import com.queryLayer.QueryBuilder;
import com.queryLayer.QueryExecutor;
import com.queryLayer.DatabaseSchemaEnums.OAuthTokensColumn;
import com.queryLayer.DatabaseSchemaEnums.Table;
import com.queryLayer.DatabaseSchemaEnums.UserDataColumn;

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
			  .where(UserDataColumn.USER_ID, "=", userId, true);

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
}
