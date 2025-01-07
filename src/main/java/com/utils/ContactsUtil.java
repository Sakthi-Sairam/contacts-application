package com.utils;

import java.util.ArrayList;
import java.util.List;

import com.models.Contact;

public class ContactsUtil {
	public static void getContacts(List<Contact> contacts, List<Contact> favourites, List<Contact> archieves) {
    	  getFavourites(contacts,favourites);
    	  getArchieves(contacts,archieves);
	}
	
    private static void getFavourites(List<Contact> contacts, List<Contact> favourites) {
    	for(Contact c: contacts) {
    		if(c.getIsFavorite()==1) favourites.add(c);
    	}
    	return;	
    }
    private static void getArchieves(List<Contact> contacts,  List<Contact> archieves) {
    	for(Contact c: contacts) {
    		if(c.getIsArchived()==1) archieves.add(c);
    	}
    	return;	
    }

}
