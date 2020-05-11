package xyz.biandeshen.代码;


import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author fjp
 * @Title: Base64Utils
 * @ProjectName icp
 * @Description: Base64
 * @date 2018/11/19:08
 */
public class Base64Utils {
	private static final Base64.Decoder DECODER = Base64.getDecoder();
	private static final Base64.Encoder ENCODER = Base64.getEncoder();
	
	/**
	 * Gets the value of encodedText.
	 *
	 * @return the value of encodedText
	 */
	public static String getEncodedText(String text) {
		if (StringUtils.isEmpty(text)) {
			return null;
		}
		byte[] textByte;
		textByte = text.getBytes(StandardCharsets.UTF_8);
		//编码
		return ENCODER.encodeToString(textByte);
	}
	
	/**
	 * Gets the value of decodedText.
	 *
	 * @return the value of decodedText
	 */
	public static String getDecodedText(String text) {
		if (StringUtils.isEmpty(text)) {
			return null;
		}
		byte[] textByte;
		textByte = text.getBytes(StandardCharsets.UTF_8);
		//解码
		return new String(DECODER.decode(textByte), StandardCharsets.UTF_8);
	}
}