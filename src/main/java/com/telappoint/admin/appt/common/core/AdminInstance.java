package com.telappoint.admin.appt.common.core;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;

import com.telappoint.admin.appt.common.constants.PropertiesConstants;
import com.telappoint.admin.appt.common.util.FileUtils;

/**
 *
 * @author Balaji Nandarpu
 */
public class AdminInstance {
	private static final Logger logger = Logger.getLogger(AdminInstance.class);
	
	private static final String CHARSET = "UTF-8";
	public static int AES_128 = 128;
	
	public String keyValue = "";
	
	private static final AESCryptoUtility aesCryptoUtility = new AESCryptoUtility();
	
	private static AdminInstance instance__ = new AdminInstance();

	public static AdminInstance getInstance()
	{
		return instance__;
	}
	
	private AdminInstance() {
		try { 
			keyValue = FileUtils.getValueFromProperties(logger, "PASSWORD_ENCRYPT_DECRYPT_KEY_VALUE", PropertiesConstants.APPT_ADMIN_REST_SERVICE_PROP.getPropertyFileName());
			if(keyValue == null) {
				keyValue = "417f3b92ba4549b2f22340e3107d869d";
			}
		}catch(Exception e){
			keyValue = "417f3b92ba4549b2f22340e3107d869d";
		}
	}
	
	private AdminInstance(String key) {
		this.keyValue = key;
	}
	
	public String getKey() {
		return keyValue;
	}

	public byte[] encrypt(byte[] rawArray) throws Exception {
		return encrypt(rawArray, true);
	}


	public byte[] encrypt(byte[] rawArray, String key) throws Exception {
		return encrypt(rawArray, key, true);
	}


	public String encrypt(String rawValue) throws Exception {
		return encrypt(rawValue, true);
	}


	public String encrypt(String rawValue, String key) throws Exception {
		byte[] res = encrypt(rawValue.getBytes(CHARSET), key, true);
		return new String(Base64.encode(res));
	}


	public String encrypt(String rawValue, boolean cache) throws Exception {
		byte[] res = null;
		try {
			res = encrypt(rawValue.getBytes(CHARSET), cache);
			if (res == null) return null;
			return new String(Base64.encode(res));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UnsupportedEncodingException : " + e);
		}
	}


	public byte[] encrypt(byte[] rawArray, boolean cache) throws Exception {
		String keyValue = getKey();
		//System.out.println("keyValue   =========Encrypt=========>  "+keyValue);
		return encrypt(rawArray, keyValue, cache);
	}

	
	public String encrypt(String rawValue, String key, boolean cache) throws Exception {
		byte[] res = encrypt(rawValue.getBytes(CHARSET), key, cache);
		return new String(Base64.encode(res));
	}
	
	 
	
	public byte[] encrypt(byte[] rawArray, String keyValue, boolean cache) throws Exception {
		byte[] decodedKey = Hex.decodeHex(keyValue.toCharArray());
		byte [] encryptedArray = aesCryptoUtility.encrypt(rawArray, decodedKey);		
		return encryptedArray;
	 
	}

	
	public byte[] decrypt(byte[] rawArray) throws Exception {
		return decrypt(rawArray, true);
	}

	
	public byte[] decrypt(byte[] rawArray, String key) throws Exception {
		return decrypt(rawArray, key, true);
	}

	
	public String decrypt(String rawValue) throws Exception {
		return decrypt(rawValue, true);
	}

	
	public String decrypt(String rawValue, String key) throws Exception {
		return decrypt(rawValue, key, true);
	}

	
	public String decrypt(String rawValue, boolean cache) throws Exception {
		byte[] res = null;
		try {			
			res = decrypt(Base64.decode(rawValue.toCharArray()), cache);
			if (res == null) return null;
			return new String(res, CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UnsupportedEncodingException : " + e);
		}
	}

	
	public byte[] decrypt(byte[] rawArray, boolean cache) throws Exception {
		String keyValue = getKey();
		//System.out.println("keyValue   =====DECRYPT=============>  "+keyValue);
		return decrypt(rawArray, keyValue, cache);
	}

	
	public String decrypt(String rawValue, String keyValue, boolean cache) throws Exception {
		byte[] res = null;
		try {
			res = decrypt(Base64.decode(rawValue.toCharArray()), keyValue, cache);
			if (res == null) return null;
			return new String(res, CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("UnsupportedEncodingException : " + e);
		} catch (Exception ex) {
			logger.warn("Error: " + ex,ex);
			throw ex;
		}
	}

	
	public byte[] decrypt(byte[] rawArray, String keyValue, boolean cache) throws Exception {
		
			byte[] decodedKey = Hex.decodeHex(keyValue.toCharArray());
			byte [] decryptedArray = aesCryptoUtility.decrypt(rawArray, decodedKey);			
			return decryptedArray;
		
	}
	
	public static void main(String[] args) {
		try {
			System.out.println(new AdminInstance().encrypt("h1d2f3c4",false));
			System.out.println(new AdminInstance().decrypt("fV8nqWqdQo9+ljEnTh5K7Q=="));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
