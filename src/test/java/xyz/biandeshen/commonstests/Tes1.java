package xyz.biandeshen.commonstests;

import org.apache.commons.lang3.StringEscapeUtils;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fjp
 * @Title: Tes
 * @ProjectName commons-tools
 * @Description: TODO
 * @date 2019/3/116:55
 */
public class Tes1 {
	private static final String strConst="z宅J急S送g";
	private static final String strSeed="79B0E6D5B338102C4F572E646E3CD37C";
	
	
	public static void main(String[] args) {
		//String ss = "{\"clientFlag\":\"DianXin_HN\",\"orders\":[{\"mailNo\":\"A005548987905\"}]}";
		String ss = "{\"clientFlag\":\"cebBank\",\"orders\":[{\"orderNo\":\"12201905095941460101\"}]}";
		String sss = StringEscapeUtils.unescapeJava(ss);
		System.out.println("将data数据反转义:" + sss);
		Map<String, String> map = new HashMap<String, String>();
		map.put("data", sss);
		String verifyData=GetVerifyDataByrdm(sss, "8vt2", "8vt3", "cebBank", strSeed);
		map.put("verifyData", verifyData);
		map.put("clientFlag", "cebBank");
		String jieguo = ZJSHttpUtils2.post("http://cntm.zjs.com.cn/interface/iwc/querystatus", map);
		System.out.println("返回结果为:" + jieguo);
	}
	
	public static String GetVerifyDataByrdm(String datajson,String rmd1,String rmd2,String clientFlag,String strSeed){
		String str=rmd1+clientFlag+datajson+strSeed+strConst+rmd2;
		
		String strMd5 = toMD5(str, "UTF-8");
		strMd5=strMd5.replace("-"," ");
		
		strMd5=strMd5.toLowerCase();
		
		return rmd1+strMd5.substring(7, 28)+rmd2;
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
