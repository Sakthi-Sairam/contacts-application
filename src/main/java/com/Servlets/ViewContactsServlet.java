package com.Servlets;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.Dao.ContactDao;
import com.Dao.userDao;
import com.models.Contact;
import com.models.Email;
import com.models.User;

@WebServlet("/viewcontacts")
public class ViewContactsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

//        if (session.getAttribute("contactsLoaded") == null) {
//            try {
//                List<Contact> contacts = ContactDao.getContactsByUserId(user.getUserId());
//                user.setMyContacts(contacts); 
//                session.setAttribute("contactsLoaded", true);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
      try {
    	  List<Contact> contacts = ContactDao.getContactsByUserId(user.getUserId());
    	  List<Contact> favourites = getFavourites(contacts);
    	  List<Contact> archieves = getArchieves(contacts);
    	  
    	  List<Email> emails = userDao.getEmailsByUserId(user.getUserId());
    	  
    	  user.setMyContacts(contacts); 
    	  user.setFavourites(favourites);
    	  user.setArchieves(archieves);
    	  
    	  user.setEmails(emails);
    	  
    	  session.setAttribute("contactsLoaded", true);
      } catch (SQLException e) {
    	  e.printStackTrace();
      }
        
        response.sendRedirect("dashboard.jsp");
    }
}
