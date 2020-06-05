package com.zjs.edistorage.utils.common;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author fjp
 * @Title: VerifyData
 * @ProjectName dj-fjp-benlaishenghuo
 * @Description: 生成verifyData
 * @date 2019/6/2023:49
 */
@SuppressWarnings("all")
public class VerifyDataUtils {
	
	/**
	 * 根据订单信息列表,生成对应顺序的verifydata列表
	 *
	 * @param uuid
	 * 		uuid
	 * @param orderMsgs
	 * 		订单信息列表
	 *
	 * @return verifyData列表
	 */
	public static List<String> getVerifyDatas(String uuid, List<String> orderMsgs, String clientFlag, String strSeed) {
		List<String> verifyDatas = new LinkedList<>();
		if (Objects.nonNull(orderMsgs) && orderMsgs.size() > 0) {
			for (String orderMsg : orderMsgs) {
				String verifyData = getVerifyDataByRdm(orderMsg, RandomStringUtils.random(4, uuid),
				                                       RandomStringUtils.random(4, uuid), clientFlag, strSeed);
				verifyDatas.add(verifyData);
			}
		}
		return verifyDatas;
	}
	
	/**
	 * 根据订单信息,生成对应的verifydata
	 *
	 * @param uuid
	 * 		uuid
	 * @param datajson
	 * 		订单信息
	 *
	 * @return verifyData
	 */
	public static String getVerifyData(String uuid, String datajson, String clientFlag, String strSeed) {
		if (Objects.isNull(uuid) || uuid.isEmpty()) {
			uuid = UUID.randomUUID().toString().replace("-", "");
		}
		return getVerifyDataByRdm(datajson, RandomStringUtils.random(4, uuid), RandomStringUtils.random(4, uuid),
		                          clientFlag, strSeed);
	}
	
	
	/**
	 * 验证客户verifyData是否正确
	 *
	 * @param jsonData
	 * 		客户json报文
	 * @param customerVerifyData
	 * 		客户生成的verifyData
	 * @param custerClientFlag
	 * 		客户传入的clientFlag
	 * @param customerStrSeed
	 * 		客户本身的密钥
	 *
	 * @return 校验结果TRUE | FALSE
	 */
	public static boolean checkAndReVerifyData(String jsonData, String customerVerifyData, String custerClientFlag,
	                                           String customerStrSeed) {
		String myReVerifyData = getVerifyDataByRdm(jsonData, customerVerifyData.substring(0, 4),
		                                           customerVerifyData.substring(customerVerifyData.length() - 4),
		                                           custerClientFlag, customerStrSeed);
		return StringUtils.equals(customerVerifyData, myReVerifyData);
	}
	
	/**
	 * 根据订单信息报文及随机数,生成VerifyData
	 *
	 * @param datajson
	 * 		json格式订单报文
	 * @param rmd1
	 * 		随机数,长度为4,数字字母
	 * @param rmd2
	 * 		随机数,长度为4,数字字母
	 * @param clientFlag
	 * 		客户标识
	 * @param strSeed
	 * 		客户密钥
	 *
	 * @return verifyData
	 */
	public static String getVerifyDataByRdm(String datajson, String rmd1, String rmd2, String clientFlag,
	                                        String strSeed) {
		String strConst = "z宅J急S送g";
		String str = rmd1 + clientFlag + datajson + strSeed + strConst + rmd2;
		String strMd5 = EncryptUtils.encryptMD5(str, "UTF-8");
		if (StringUtils.isNotEmpty(strMd5)) {
			strMd5 = strMd5.replace("-", " ");
			strMd5 = strMd5.toLowerCase();
			return rmd1 + strMd5.substring(7, 28) + rmd2;
		}
		return "";
	}
	
	//public static void main(String[] args) {
	//	String verifyData = "6q4u7b80bd69a57d88667f24f6q4u";
	//	String jsonData = "{\"clientFlag\":\"test\",\"mailNo\":\"A001705874494\",\"orderNo\":\"518061054080517801\",
	//	\"time\":\"2018-06-12 12:01:01\",\"desc\":\"客户已签收\",\"city\":\"鄂尔多斯市\",\"facilityType\":\"1\",
	//	\"facilityNo\":\"8884\",\"facilityName\":\"内蒙古_鄂尔多斯分拨站_伊金霍洛旗A点\",\"action\":\"SIGNED\",\"tz\":\"8\",
	//	\"country\":\"China\",\"singer\":\"李慧芳\"}";
	//	String clientFlagCustomer = "test";
	//	String strSeed = "aafc04a1bacb487fa8d03f2a7bfdb555";
	//	String myVerifyData = ReVerifyData.getVerifyDataByRdm(jsonData, verifyData.substring(0, 4), verifyData
	//	.substring(verifyData.length() - 4, verifyData.length()), clientFlagCustomer, strSeed);
	//	//String myVerifyData = ReVerifyData.getVerifyDataByRdm(jsonData, "1111","1111", clientFlagCustomer, strSeed);
	//	System.out.println(myVerifyData);
	//}
}
