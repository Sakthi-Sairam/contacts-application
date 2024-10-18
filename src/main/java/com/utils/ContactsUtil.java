package com.utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.Dao.ContactDao;
import com.Dao.userDao;
import com.models.Contact;
import com.models.Email;
import com.models.User;

public class ContactsUtil {
	public static void getContacts(User user) {
	      try {
	    	  List<Contact> contacts = ContactDao.getContactsByUserId(user.getUserId());
	    	  List<Contact> favourites = getFavourites(contacts);
	    	  List<Contact> archieves = getArchieves(contacts);
	    	  
	    	  List<Email> emails = userDao.getEmailsByUserId(user.getUserId());
	    	  
	    	  user.setMyContacts(contacts); 
	    	  user.setFavourites(favourites);
	    	  user.setArchieves(archieves);
	    	  
	    	  user.setEmails(emails);
	    	  user.setPrimaryEmailId(user.findPrimaryEmail());     	  
	    	  
	      } catch (SQLException e) {

	    	  e.printStackTrace();
	      }
	}
	
    private static List<Contact> getFavourites(List<Contact> contacts) {
    	List<Contact> favourites = new ArrayList<>();
    	for(Contact c: contacts) {
    		if(c.getIsFavorite()==1) favourites.add(c);
    	}
    	return favourites;	
    }
    private static List<Contact> getArchieves(List<Contact> contacts) {
    	List<Contact> archieves = new ArrayList<>();
    	for(Contact c: contacts) {
    		if(c.getIsArchived()==1) archieves.add(c);
    	}
    	return archieves;	
    }

}
