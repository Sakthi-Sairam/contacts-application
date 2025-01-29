package com.oauth;

import org.json.JSONArray;
import org.json.JSONObject;

import com.QueryLayer.QueryBuilder;
import com.QueryLayer.QueryExecutor;
import com.QueryLayer.DatabaseSchemaEnums.MyContactsDataColumn;
import com.QueryLayer.DatabaseSchemaEnums.Table;
import com.exceptions.DaoException;
import com.exceptions.ErrorCode;
import com.exceptions.QueryExecutorException;
import com.filters.SessionFilter;
import com.models.User;

public class OAuthDao {

    public static void insertContacts(String contactsJson, int userId) throws DaoException {
        JSONObject jsonObject = new JSONObject(contactsJson);
        JSONArray contacts = jsonObject.getJSONArray("contacts");
        
        QueryExecutor executor = new QueryExecutor();
        long currentTime = System.currentTimeMillis();
        
        try {
            executor.transactionStart();
            
            for (int i = 0; i < contacts.length(); i++) {
                JSONObject contact = contacts.getJSONObject(i);
                
                String email = contact.optString("email", "");
                String name = contact.optString("name", "");
                String phone = contact.optString("phone", "");
                if(name.isEmpty() || phone.isEmpty()) continue;
                QueryBuilder qb = new QueryBuilder();
                qb.insert(Table.MY_CONTACTS_DATA)
                  .columns(
                      MyContactsDataColumn.USER_ID, 
                      MyContactsDataColumn.FRIEND_EMAIL, 
                      MyContactsDataColumn.ALIAS_FND_NAME,
                      MyContactsDataColumn.PHONE, 
                      MyContactsDataColumn.IS_ARCHIVED,
                      MyContactsDataColumn.IS_FAVORITE, 
                      MyContactsDataColumn.CREATED_AT, 
                      MyContactsDataColumn.MODIFIED_AT
                  )
                  .values(
                      userId,
                      email.isEmpty()?null:email,
                      name,
                      phone,
                      0,
                      0,
                      currentTime,
                      currentTime
                  );
                
                executor.executeUpdate(qb);
            }
            
            executor.transactionEnd();
        } catch (Exception e) {
            try {
                executor.transactionEnd();
            } catch (QueryExecutorException ignored) { }
            throw new DaoException(ErrorCode.QUERY_EXECUTION_FAILED, "Failed to insert contacts: " + e.getMessage(), e);
        }
    }

}
