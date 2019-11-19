package xyz.biandeshen.commonstests.util.common;

import com.sun.istack.internal.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUtils {
	private static final String KEY_MAC = "HmacMD5";
	private static final String KEY_SHA_256 = "SHA-256";
	private static final String KEY_SHA_512 = "SHA-512";
	private static final String KEY_MD5 = "MD5";
	
	private static Charset charset = Charset.forName("Utf-8");
	
	private static String encryptResult;
	
	private static final Logger logger = LoggerFactory.getLogger(EncryptUtils.class);
	
	/**
	 * MD5加密
	 *
	 * @param data
	 *
	 * @return
	 *
	 * @throws Exception
	 */
	public static String encryptMD5(byte[] data) {
		getMessageDigestEncryptResult(data, KEY_MD5);
		return encryptResult;
	}
	
	public static String encryptMD5(String data, String charsetName) {
		return encryptMD5(stringToBytes(data, charsetName));
	}
	
	public static String encryptMD5(String data) {
		return encryptMD5(stringToBytes(data));
	}
	
	/**
	 * SHA256加密
	 *
	 * @param data
	 *
	 * @return
	 *
	 * @throws Exception
	 */
	public static String encryptSHA256(byte[] data) {
		getMessageDigestEncryptResult(data, KEY_SHA_256);
		return encryptResult;
	}
	
	public static String encryptSHA256(String data, String charsetName) {
		return encryptSHA256(stringToBytes(data, charsetName));
	}
	
	public static String encryptSHA256(String data) {
		return encryptSHA256(stringToBytes(data));
	}
	
	/**
	 * SHA512加密
	 *
	 * @param data
	 *
	 * @return
	 *
	 * @throws Exception
	 */
	public static String encryptSHA512(byte[] data) {
		getMessageDigestEncryptResult(data, KEY_SHA_512);
		return encryptResult;
	}
	
	public static String encryptSHA512(String data, String charsetName) {
		return encryptSHA512(stringToBytes(data, charsetName));
	}
	
	public static String encryptSHA512(String data) {
		return encryptSHA512(stringToBytes(data));
	}
	
	/**
	 * HMAC加密
	 *
	 * @param data
	 * @param key
	 *
	 * @return
	 *
	 * @throws Exception
	 */
	public static String encryptHMAC(byte[] data, String key) {
		SecretKey secretKey = new SecretKeySpec(stringToBytes(encryptMD5(data)), KEY_MAC);
		Mac mac = null;
		try {
			mac = Mac.getInstance(secretKey.getAlgorithm());
			mac.init(secretKey);
			encryptResult = bytesToString(mac.doFinal(data));
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			logger.info("{}加密失败，错误原因: {}", KEY_MAC, e);
		}
		return encryptResult;
	}
	
	public static String encryptHMAC(String data, String key, String charsetName) {
		return encryptHMAC(stringToBytes(data, charsetName), key);
	}
	
	public static String encryptHMAC(String data, String key) {
		return encryptHMAC(stringToBytes(data), key);
	}
	
	/**
	 * String 转 byte[]
	 *
	 * @param str
	 * 		传入字符串
	 * @param charsetName
	 * 		编码格式字符串
	 *
	 * @return 转换结果
	 */
	public static byte[] stringToBytes(String str, @Nullable String charsetName) {
		Charset charset = Charset.forName("Utf-8");
		if (!StringUtils.isEmpty(charsetName)) {
			charset = Charset.forName(charsetName);
		}
		return str.getBytes(charset);
	}
	
	public static byte[] stringToBytes(String str) {
		return stringToBytes(str, null);
	}
	
	/**
	 * byte[] 转 字符串
	 *
	 * @param bytes
	 * 		字符数组
	 *
	 * @return 转换结果
	 */
	public static String bytesToString(byte[] bytes) {
		int i;
		StringBuilder buf = new StringBuilder();
		for (byte aB : bytes) {
			i = aB;
			if (i < 0) {
				i += 256;
			}
			if (i < 16) {
				buf.append("0");
			}
			buf.append(Integer.toHexString(i));
		}
		return buf.toString();
	}
	
	private static void getMessageDigestEncryptResult(byte[] data, String keyAlg) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(keyAlg);
			messageDigest.update(data);
			encryptResult = bytesToString(messageDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			logger.info("{}加密失败，错误原因： {}", keyAlg, e);
		}
	}
	
}
