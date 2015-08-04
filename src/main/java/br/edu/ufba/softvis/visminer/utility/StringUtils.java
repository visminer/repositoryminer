package br.edu.ufba.softvis.visminer.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @version 0.9
 * Utility class to work with strings.
 */
public class StringUtils {
	
	/**
	 * @param input
	 * @return SHA1 hash
	 * Calculates SHA1 hash.
	 */
	public static String sha1(String input){
		
		MessageDigest mDigest;
		try {
			mDigest = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
         
        return sb.toString();
		
	}
	
}
