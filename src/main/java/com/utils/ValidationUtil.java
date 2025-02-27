package com.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtil {
    public static boolean validateEmail(String email) {
    	Pattern validEmailRegex = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9_.+]+@[a-zA-Z]+\\.[a-zA-Z]{2,}$");
    	Matcher matcher = validEmailRegex.matcher(email);
    	return matcher.matches();
    }
    
    public static boolean validatePhone(String phone) {
    	Pattern validPhoneRegex = Pattern.compile("^[1-9][0-9]{1,14}$");
    	Matcher matcher = validPhoneRegex.matcher(phone);
    	return matcher.matches();
    }
}
