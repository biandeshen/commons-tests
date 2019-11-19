package xyz.biandeshen.commonstests.util.zjs;

import xyz.biandeshen.commonstests.util.common.EncryptUtils;

public class ReVerifyData {
	
	//验证verifyData是否正确
	//String myVerifyData = ReVerifyData.getVerifyDataByRdm(jsonData, verifyData.substring(0, 4), verifyData.substring(verifyData.length() - 4, verifyData.length()), clientFlagCustomer, strSeed);
	
	public static String getVerifyDataByRdm(String datajson, String rmd1, String rmd2, String clientFlag, String strSeed) {
		String strConst = "z宅J急S送g";
		String str = rmd1 + clientFlag + datajson + strSeed + strConst + rmd2;
		
		String strMd5 = EncryptUtils.encryptMD5(str, "UTF-8");
		if (strMd5 != null) {
			strMd5 = strMd5.replace("-", " ");
			strMd5 = strMd5.toLowerCase();
			return rmd1 + strMd5.substring(7, 28) + rmd2;
		}
		return "";
	}
	
	//public static void main(String[] args) {
	//	String verifyData = "6q4u7b80bd69a57d88667f24f6q4u";
	//	String jsonData = "{\"clientFlag\":\"test\",\"mailNo\":\"A001705874494\",\"orderNo\":\"518061054080517801\",\"time\":\"2018-06-12 12:01:01\",\"desc\":\"客户已签收\",\"city\":\"鄂尔多斯市\",\"facilityType\":\"1\",\"facilityNo\":\"8884\",\"facilityName\":\"内蒙古_鄂尔多斯分拨站_伊金霍洛旗A点\",\"action\":\"SIGNED\",\"tz\":\"8\",\"country\":\"China\",\"singer\":\"李慧芳\"}";
	//	String clientFlagCustomer = "test";
	//	String strSeed = "aafc04a1bacb487fa8d03f2a7bfdb555";
	//	String myVerifyData = ReVerifyData.getVerifyDataByRdm(jsonData, verifyData.substring(0, 4), verifyData.substring(verifyData.length() - 4, verifyData.length()), clientFlagCustomer, strSeed);
	//	//String myVerifyData = ReVerifyData.getVerifyDataByRdm(jsonData, "1111","1111", clientFlagCustomer, strSeed);
	//	System.out.println(myVerifyData);
	//}
}
