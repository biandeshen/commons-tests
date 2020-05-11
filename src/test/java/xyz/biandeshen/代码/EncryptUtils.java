package xyz.biandeshen.代码;

import org.springframework.util.StringUtils;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SuppressWarnings("all")
public class EncryptUtils {
	private static final String KEY_MAC = "HmacMD5";
	private static final String KEY_SHA_1 = "SHA-1";
	private static final String KEY_SHA_256 = "SHA-256";
	private static final String KEY_SHA_512 = "SHA-512";
	private static final String KEY_MD5 = "MD5";


	/**
	 * MD5加密
	 *
	 * @param data
	 *
	 * @return
	 *
	 * @throws Exception
	 */
	public static String encryptMD5(byte[] data) throws RuntimeException {
		return getMessageDigestEncryptResult(data, KEY_MD5);
	}

	public static String encryptMD5(String data, String charsetName) throws RuntimeException {
		return encryptMD5(stringToBytes(data, charsetName));
	}

	public static String encryptMD5(String data) throws RuntimeException {
		return encryptMD5(stringToBytes(data));
	}

	/**
	 * SHA1加密
	 *
	 * @param data
	 *
	 * @return
	 *
	 * @throws Exception
	 */
	public static String encryptSHA1(byte[] data) throws RuntimeException {
		return getMessageDigestEncryptResult(data, KEY_SHA_1);
	}

	public static String encryptSHA1(String data, String charsetName) throws RuntimeException {
		return encryptSHA1(stringToBytes(data, charsetName));
	}

	public static String encryptSHA1(String data) throws RuntimeException {
		return encryptSHA1(stringToBytes(data));
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
	public static String encryptSHA256(byte[] data) throws RuntimeException {
		return getMessageDigestEncryptResult(data, KEY_SHA_256);
	}

	public static String encryptSHA256(String data, String charsetName) throws RuntimeException {
		return encryptSHA256(stringToBytes(data, charsetName));
	}

	public static String encryptSHA256(String data) throws RuntimeException {
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
	public static String encryptSHA512(byte[] data) throws RuntimeException {
		return getMessageDigestEncryptResult(data, KEY_SHA_512);
	}

	public static String encryptSHA512(String data, String charsetName) throws RuntimeException {
		return encryptSHA512(stringToBytes(data, charsetName));
	}

	public static String encryptSHA512(String data) throws RuntimeException {
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
	public static String encryptHMAC(byte[] data, String key) throws RuntimeException {
		SecretKey secretKey = new SecretKeySpec(stringToBytes(encryptMD5(data)), KEY_MAC);
		String encryptResult;
		Mac mac = null;
		try {
			mac = Mac.getInstance(secretKey.getAlgorithm());
			mac.init(secretKey);
			encryptResult = bytesToString(mac.doFinal(data));
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			throw new RuntimeException(KEY_MAC + "加密异常!", e);
		}
		return encryptResult;
	}

	public static String encryptHMAC(String data, String key, String charsetName) throws RuntimeException {
		return encryptHMAC(stringToBytes(data, charsetName), key);
	}

	public static String encryptHMAC(String data, String key) throws RuntimeException {
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
	public static byte[] stringToBytes(String str, String charsetName) {
		Charset charset = Charset.forName(StandardCharsets.UTF_8.toString());
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
	
	public static String bytes2HexString(byte[] b) {
		StringBuilder result = new StringBuilder();
		String hex;
		for (byte b1 : b) {
			hex = Integer.toHexString(b1 & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			result.append(hex.toUpperCase());
		}
		return result.toString();
	}

	private static String getMessageDigestEncryptResult(byte[] data, String keyAlg) throws RuntimeException {
		String encryptResult = null;
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(keyAlg);
			messageDigest.update(data);
			encryptResult = bytesToString(messageDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(KEY_MAC + "加密异常!", e);
		}
		return encryptResult;
	}

}
