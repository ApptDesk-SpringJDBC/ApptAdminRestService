package com.telappoint.admin.appt.common.core;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.log4j.Logger;

/**
 * 
 * @author Balaji Nandarapu
 * 
 */
public class AESCryptoUtility {
	private static final Logger logger = Logger.getLogger(AESCryptoUtility.class.getName());

	private static final String CHARSET = "UTF-8";

	private String algorithm; // AES, RSA
	private String cipherMode;// ECB, CBC
	private String paddingScheme; // PKCS5Padding, NoPadding
	private byte[] key;

	private StringBuilder cipherParams;

	public AESCryptoUtility(String algorithm, String cipherMode, String paddingScheme, byte[] key) {
		this.algorithm = algorithm;
		this.cipherMode = cipherMode;
		this.paddingScheme = paddingScheme;
		this.key = key;
	}

	public AESCryptoUtility() {
		this.algorithm = "AES";
		this.cipherMode = "ECB";
		this.paddingScheme = "PKCS5Padding";
	}

	public byte[] encrypt(String plainText, byte[] key) {
		byte[] cipherText = null;
		try {
			cipherText = encrypt(plainText.getBytes(CHARSET), key);
			return cipherText;
		} catch (Exception e) {
			logger.error("Error:" + e, e);
		}
		return null;
	}

	public byte[] encrypt(byte[] plainText, byte[] key) {
		SecretKeySpec skeySpec = new SecretKeySpec(key, algorithm);
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(getCipherParams());
		} catch (Exception e) {
			logger.error("Error:" + e, e);
			return null;
		}
		byte[] cipherText = null;
		try {
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			cipherText = cipher.doFinal(plainText);
		} catch (Exception e) {
			logger.error("Error:" + e, e);
			return null;
		}
		return cipherText;
	}

	public byte[] encrypt(String plainText) {
		return encrypt(plainText, key);
	}

	public byte[] decrypt(byte[] cipheredText, byte[] key) {
		SecretKeySpec skeySpec = new SecretKeySpec(key, algorithm);
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(getCipherParams());
		} catch (Exception e) {
			logger.error("Error:" + e, e);
			return null;
		}
		byte[] original = null;
		try {
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			original = cipher.doFinal(cipheredText);
		} catch (Exception e) {
			logger.error("Error:" + e, e);
			return null;
		}
		return original;
	}

	public byte[] decrypt(byte[] cipheredText) {
		return decrypt(cipheredText, key);
	}

	private String getCipherParams() {
		if (cipherParams != null)
			return cipherParams.toString();

		StringBuilder _cipherParams = new StringBuilder();
		if (algorithm != null) {
			_cipherParams.append(algorithm);
		}
		if (cipherMode != null) {
			_cipherParams.append("/").append(cipherMode);
		}
		if (paddingScheme != null) {
			_cipherParams.append("/").append(paddingScheme);
		}
		cipherParams = _cipherParams;

		return cipherParams.toString();
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public String getCipherMode() {
		return cipherMode;
	}

	public void setCipherMode(String cipherMode) {
		this.cipherMode = cipherMode;
	}

	public String getPaddingScheme() {
		return paddingScheme;
	}

	public void setPaddingScheme(String paddingScheme) {
		this.paddingScheme = paddingScheme;
	}

	public byte[] getKey() {
		return key;
	}

	public void setKey(byte[] key) {
		this.key = key;
	}
}
