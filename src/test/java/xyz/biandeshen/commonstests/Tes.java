package xyz.biandeshen.commonstests;


import org.apache.commons.lang3.StringEscapeUtils;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * @author fjp
 * @Title: Tes
 * @ProjectName commons-tools
 * @Description: TODO
 * @date 2019/3/116:55
 */
public class Tes {
	private static final String STRCONST = "z宅J急S送g";
	private static final String STRSEED = "B151266B9749B6299BD3F04B11036CFE";
	private static String CLIENTFLAG = "GuangDaKuFang";
	
	public static void main(String[] args) {
		//String ss = "{\"clientFlag\":\"GuangDaKuFang\",\"orders\":[{\"mailNo\":\"A005548987905\"}]}";
		//String ss = "{\"clientFlag\":\"GuangDaKuFang\",\"orders\":[{\"orderNo\":\"12201905095941460101\"}]}";
		//String ss = "{\"sendAddress\":\"北京北京丰台区兆丰园小区兆丰园小区\",\"receiveAddress\":\"安徽合肥蜀山区大蜀山半边街D栋201号午言造美餐厅\",\"goodsWeight\":\"10\",\"goodsVolume\":\"10*10*10*2,20*30*30*3\"}";
		String ss = "{\"sendAddress\":\"北京北京丰台区兆丰园小区兆丰园小区\",\"receiveAddress\":\"安徽合肥蜀山区大蜀山半边街D栋201号午言造美餐厅\",\"goodsWeight\":\"10\",\"goodsVolume\":\"10*10*10*2\"}";
		String sss = StringEscapeUtils.unescapeJava(ss);
		System.out.println("将data数据反转义:" + sss);
		Map<String, String> map = new HashMap<String, String>();
		//map.put("data", sss);
		String verifyData = GetVerifyDataByrdm(sss, "", "");
		//map.put("verifyData", verifyData);
		//map.put("clientFlag", "GuangDaKuFang");
		//String jieguo = ZJSHttpUtils2.post("http://cntm.zjs.com.cn/interface/iwc/querystatus", map);
		String jieguo = ZjsStandardPostUtil.zjsStandardPost(sss, "http://10.10.12.109:24014/priceinquiry", CLIENTFLAG, verifyData);
		System.err.println("返回结果为:" + jieguo);
	}

  /*  public static String GetVerifyDataByrdm(String datajson,String rmd1,String rmd2,String clientFlag,String strSeed){
        String str=rmd1+clientFlag+datajson+strSeed+strConst+rmd2;
        String strMd5 = toMD5(str, "UTF-8");
        strMd5=strMd5.replace("-"," ");
                                                        宅急送    江攀老师提供
        strMd5=strMd5.toLowerCase();

        return rmd1+strMd5.substring(7, 28)+rmd2;
    }*/
	
	
	// 根据length产生随机数
	private static String randomString(int length) {
		if (length < 1) {
			return null;
		}
		Random rd = new Random();
		String basicchars = "0123456789abcdefghijklmnopqrstuvwxy";
		String rdmString = "";
		for (int i = 0; i < length; i++) {
			int rdmvalue = rd.nextInt(basicchars.length());
			rdmString += basicchars.substring(rdmvalue, rdmvalue + 1);
		}
		return rdmString;
	}
	
	/**
	 * 宅急送MD5加密
	 * 2019.3.1
	 * 宅急送提供MD5加密方法
	 */
	public static String GetVerifyDataByrdm(String datajson, String rmdnus1, String rmdnus2) {
		// 生成rmd1&rmd2 两个参数
		String rdm1 = null;
		String rdm2 = null;
		if (rmdnus1.equals("") && rmdnus2.equals("")) {
			rdm1 = randomString(4);
			rdm2 = randomString(4);
		} else if (!rmdnus1.equals("") && rmdnus2.equals("")) {
			rdm2 = randomString(4);
			rdm1 = rmdnus1;
		} else if (!rmdnus2.equals("") && rmdnus1.equals("")) {
			rdm1 = randomString(4);
			rdm2 = rmdnus2;
		} else {
			rdm1 = rmdnus1;
			rdm2 = rmdnus2;
		}
		String str = rdm1 + CLIENTFLAG + datajson + STRSEED + STRCONST + rdm2;
		String strMd5 = toMD5(str, "UTF-8");
		strMd5 = strMd5.replace("-", " ");
		
		strMd5 = strMd5.toLowerCase();
		
		return rdm1 + strMd5.substring(7, 28) + rdm2;
	}
	
	public static String toMD5(String plainText, String charset) {
		String md5Str = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes(charset));
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			md5Str = buf.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return md5Str;
	}
}

