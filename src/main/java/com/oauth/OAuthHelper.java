package com.oauth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.dao.ContactDao;
import com.exceptions.DaoException;
import com.models.Contact;

public class OAuthHelper {
	public static void addOrUpdateTheContacts(String contactsJson, int userId) {
		JSONObject jsonObject = new JSONObject(contactsJson);
		JSONArray contactsArray = jsonObject.getJSONArray("contacts");

		Map<String, Map<String, String>> map = new HashMap<>();
		for (int i = 0; i < contactsArray.length(); i++) {
			JSONObject contact = contactsArray.getJSONObject(i);
			Map<String, String> innerMap = new HashMap<String, String>();

			innerMap.put("email", contact.optString("email", "null"));
			innerMap.put("name", contact.optString("name", ""));
			innerMap.put("phone", contact.optString("phone", ""));
			String resourceName = contact.optString("resourceName", "");
			innerMap.put("resourceName", resourceName);
			map.put(resourceName, innerMap);
		}

		try {
			List<Contact> contacts = ContactDao.getContactsByUserId(userId);
			for (Contact contact : contacts) {
				String resourceName = contact.getResourceName();
				Map<String, String> innerMap = null;
				if(resourceName!=null && map.containsKey(resourceName)) {
					innerMap = map.get(resourceName);
					map.remove(resourceName);
				}
				if (resourceName != null && innerMap!=null && !equals(innerMap, contact)) {
					contact.setAlias_name(innerMap.get("name"));
					contact.setFriend_email(innerMap.get("email"));
					contact.setPhone(innerMap.get("phone"));
					ContactDao.updateContact(contact);
				}
			}
			if (!map.isEmpty()) {
			    for (Map.Entry<String, Map<String, String>> entry : map.entrySet()) {
			        Map<String, String> innerMap = entry.getValue();
			        ContactDao.addContact(
			            innerMap.get("email"),
			            innerMap.get("name"),
			            innerMap.get("phone"),
			            "",
			            userId,
			            innerMap.get("resourceName"),
			            0,
			            0
			        );
			    }
			}
		} catch (DaoException e) {
			e.printStackTrace();
		}
	}

	private static boolean equals(Map<String, String> map, Contact contact) {

		if (!map.get("email").equals(contact.getFriend_email()) || !map.get("name").equals(contact.getAlias_name())
				|| !map.get("phone").equals(contact.getPhone())) {
			return false;
		}
		return true;
	}
}
