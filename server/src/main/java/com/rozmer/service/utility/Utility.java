package com.rozmer.service.utility;

import javax.servlet.http.HttpServletRequest;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import org.apache.tomcat.util.codec.binary.Base64;


public class Utility {

    private static Cipher cipher;

    static SecretKey key;

    public static String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    public static String decrypt(String encryptedString) {
        String decryptedText=null;
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] encryptedText = Base64.decodeBase64(encryptedString);
            byte[] plainText = cipher.doFinal(encryptedText);
            decryptedText= new String(plainText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedText;
    }
}