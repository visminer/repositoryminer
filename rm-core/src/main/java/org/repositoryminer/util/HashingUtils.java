package org.repositoryminer.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

/**
 * This class handles string hashing operations.
 */
public class HashingUtils {

	/**
	 * Hashes the input using SHA1 algorithm.
	 * 
	 * @param input
	 *            the input string.
	 * @return the hashed input string.
	 */
	public static String encodeToSHA1(final String input) {
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

	/**
	 * Hashes the input using CRC32 algorithm.
	 * 
	 * @param input
	 *            the input string.
	 * @return the hashed input string.
	 */
	public static long encodeToCRC32(final String input) {
		byte bytes[] = input.getBytes();
		Checksum checksum = new CRC32();
		checksum.update(bytes, 0, bytes.length);
		return checksum.getValue();
	}

}