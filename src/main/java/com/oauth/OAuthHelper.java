package com.oauth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dao.ContactDao;
import com.exceptions.DaoException;
import com.models.Contact;
import com.models.ContactEmail;
import com.models.ContactPhoneNumber;

public class OAuthHelper {
    private static final Logger LOGGER = Logger.getLogger(OAuthHelper.class.getName());

    public static void addOrUpdateTheContacts(String contactsJson, int userId) {
        JSONObject jsonObject = new JSONObject(contactsJson);
        JSONArray contactsArray = jsonObject.getJSONArray("contacts");

        Map<String, Map<String, String>> map = new HashMap<>();
        for (int i = 0; i < contactsArray.length(); i++) {
            JSONObject contact = contactsArray.getJSONObject(i);
            Map<String, String> innerMap = new HashMap<>();

            String email = contact.optString("email", "").strip();
            String name = contact.optString("name", "").strip();
            String phone = contact.optString("phone", "").strip();
            String resourceName = contact.optString("resourceName", "");

            if ((!name.isBlank() || !phone.isBlank()) && !resourceName.isBlank()) {
                innerMap.put("email", email);
                innerMap.put("name", name);
                innerMap.put("phone", phone);
                innerMap.put("resourceName", resourceName);
                map.put(resourceName, innerMap);
            }
        }

        try {
            List<Contact> contacts = ContactDao.getContactsByUserId(userId);
            for (Contact contact : contacts) {
                String resourceName = contact.getResourceName();
                if (resourceName != null && map.containsKey(resourceName)) {
                    Map<String, String> innerMap = map.remove(resourceName);
                    updateContactIfNeeded(contact, innerMap);
                }
            }

            for (Map.Entry<String, Map<String, String>> entry : map.entrySet()) {
                Map<String, String> innerMap = entry.getValue();
                if (!innerMap.get("name").isBlank() || !innerMap.get("phone").isBlank()) {
                    ContactDao.addContact(innerMap.get("email"), innerMap.get("name"), innerMap.get("phone"), "",
                            userId, innerMap.get("resourceName"), 0, 0);
                }
            }
        } catch (DaoException e) {
            LOGGER.log(Level.SEVERE, "Error processing contacts", e);
        }
    }

    private static void updateContactIfNeeded(Contact contact, Map<String, String> innerMap) throws DaoException {
        int contactId = contact.getMyContactsID();
        String newName = innerMap.get("name");
        String newEmail = innerMap.get("email");
        String newPhone = innerMap.get("phone");

        if (!newName.isBlank() && !newName.equalsIgnoreCase(contact.getAlias_name())) {
            ContactDao.updateContact(contactId, newName, contact.getAddress(), contact.getIsArchived(), contact.getIsFavorite());
        }

        if (!newPhone.isBlank() && !phoneNumberExists(newPhone, contact.getPhoneNumbers())) {
            ContactDao.addContactPhoneNumber(contactId,null, newPhone);
        }

        if (!newEmail.isBlank() && !emailExists(newEmail, contact.getEmails())) {
            ContactDao.addContactEmail(contactId,null, newEmail);
        }
    }

    private static boolean emailExists(String email, List<ContactEmail> emails) {
        return emails.stream().anyMatch(e -> email.equalsIgnoreCase(e.getEmail()));
    }

    private static boolean phoneNumberExists(String phone, List<ContactPhoneNumber> phoneNumbers) {
        return phoneNumbers.stream().anyMatch(p -> phone.equals(p.getPhoneNumber()));
    }
}
