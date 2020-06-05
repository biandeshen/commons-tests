package xyz.biandeshen.commonstests;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Random;


/**
 * @author fjp
 * @Title: VerifyDataGenerator
 * @ProjectName commons-tools
 * @Description: 对接组签名生成工具
 * @date 2019/3/116:55
 */
public class VerifyDataGenerator {
	private static final String STRCONST = "z宅J急S送g";
	private static final String STRSEED = "aafc04a1bacb487fa8d03f2a7bfdb555";
	private static final String CLIENTFLAG = "test";
	
	public static void main(String[] args) {
		String jsondata = "{\"ownerCode\": \"015\", \"warehouseCode\": \"CSYC200\", \"goodsInfos\": [{\"actionType\": " +
				                  "\"update\", \"goodsCode\": \"zhtest-000000001\", \"goodsName\": \"米9\", " +
				                  "\"goodsShortName\": \"mi9\", \"goodsType\": \"FL10337\", \"brandName\": \"小米手机\", " +
				                  "\"stockUnit\": \"个\", \"styleNo\": \"HW343543543\",\"categoryType\": \"101001\"}]}";
		String verifyData = GetVerifyDataByrdm(jsondata, randomString(4),
		                                       randomString(4), CLIENTFLAG);
		System.err.println("verifyData:" + verifyData);
	}

	private static String GetVerifyDataByrdm(String datajson, String rmd1, String rmd2, String clientFlag) {
		String str = rmd1 + clientFlag + datajson + VerifyDataGenerator.STRSEED + STRCONST + rmd2;
		String strMd5 = toMD5(str);
		strMd5 = strMd5.replace("-", " ");
		strMd5 = strMd5.toLowerCase();
		return rmd1 + strMd5.substring(7, 28) + rmd2;
	}
	
	
	private static String toMD5(String plainText) {
		String md5Str = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes(StandardCharsets.UTF_8));
			byte[] b = md.digest();
			int i;
			StringBuilder buf = new StringBuilder("");
			for (byte b1 : b) {
				i = b1;
				if (i < 0) i += 256;
				if (i < 16) buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			md5Str = buf.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return md5Str;
	}
	// 根据length产生随机数
	private static String randomString(int length) {
		if (length < 1) {
			return null;
		}
		Random rd = new Random();
		String basicchars = "0123456789abcdefghijklmnopqrstuvwxy";
		StringBuilder rdmString = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int rdmvalue = rd.nextInt(basicchars.length());
			rdmString.append(basicchars, rdmvalue, rdmvalue + 1);
		}
		return rdmString.toString();
	}
}

