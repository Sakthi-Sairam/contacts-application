package com.handlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dao.ContactDao;
import com.dao.MergeContactsDao;
import com.exceptions.DaoException;
import com.filters.AuthFilter;
import com.models.Contact;
import com.utils.ExceptionHandlerUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MergeHandler {

	public static void getDuplicateContacts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			int userId = AuthFilter.getCurrentUser().getUserId();
			List<Contact> contacts = ContactDao.getContactsByUserId(userId);
			
			Map<String, List<Contact>> mergedContactsMap = mergeContacts(contacts);
			List<List<Contact>> mergedContacts = groupMergedContacts(mergedContactsMap);
			
			request.setAttribute("mergedContacts", mergedContacts);
			request.getRequestDispatcher("/contactsMerge.jsp").forward(request, response);
			
		} catch (DaoException e) {
			ExceptionHandlerUtil.logAndForwardServerException(request, response, e, MergeHandler.class);
		}

	}
	
	private static  Map<String, List<Contact>> mergeContacts(List<Contact> contacts) {
        Map<String, List<Contact>> map = new HashMap<>();
        Contact prevContact = contacts.get(0);
        int size = contacts.size();
        for (int i=1; i<size; i++){
            Contact currContact = contacts.get(i);
            if(prevContact.getAlias_name().equals(currContact.getAlias_name())){
                List<Contact> list = map.get(prevContact.getAlias_name());
                if(list == null){
                    list = new ArrayList<>();
                    map.put(prevContact.getAlias_name(), list);
                }
                list.add(prevContact);
            }else {
                List<Contact> list = map.get(prevContact.getAlias_name());
                if(list != null){
                    list.add(prevContact);
                }
            }
            prevContact = currContact;
        }
        return map;
    }
	
    private static List<List<Contact>> groupMergedContacts(Map<String, List<Contact>> mergedContactsMap) {
    	List<List<Contact>> mergedContacts = new ArrayList<>();
    	for(List<Contact> groupedContacts: mergedContactsMap.values()) {
    		mergedContacts.add(groupedContacts);
    	}
    	return mergedContacts;
	}

	public static void mergeContacts(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
        String[] selectedContactIds = request.getParameterValues("selectedContacts");
        int userId = AuthFilter.getCurrentUser().getUserId();
        if (selectedContactIds == null || selectedContactIds.length < 2) {
        	ExceptionHandlerUtil.logAndForwardClientException(request, response, "At least two contacts are required for merging", null, "/contactsMerge.jsp", MergeHandler.class);
            return;
        }
        
        int primaryContactId = -1;
        List<Integer> secondaryContactIds = new ArrayList<>();
        int size = selectedContactIds.length;
        
        for (int i=0; i<size ; i++) {
        	int contactId = Integer.parseInt(selectedContactIds[i]);
            if(i==0) {
            	primaryContactId = contactId;
            	continue;
            }
            secondaryContactIds.add(contactId);
        }
        
        try {
			MergeContactsDao.updateContactPhoneNumbersAndEmails(primaryContactId, secondaryContactIds);
			MergeContactsDao.deleteDuplicateContactData(secondaryContactIds);
			response.sendRedirect("/merge-contacts");
		} catch (DaoException e) {
			ExceptionHandlerUtil.logAndForwardServerException(request, response, e, MergeHandler.class);
		}

		
	}

}
