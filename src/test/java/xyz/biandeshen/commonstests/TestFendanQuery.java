package xyz.biandeshen.commonstests;

import org.junit.Test;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;

//import com.cn.zjs.interfaceWrapper.model.status.StatusReponse;
//import com.cn.zjs.interfaceWrapper.util.jsontool;

public class TestFendanQuery {
	private static final String strConst = "z宅J急S送g";
	
	@Test
	public void t1() throws UnsupportedEncodingException {
		//String url="http://10.10.12.132:8110/querystatus";
		//String url = "http://businesstest.zjs.com.cn:9200/interface/iwc/querystatustest";
		//String url = "http://businesstest.zjs.com.cn:9200/interface/iwc/nctfendantest";
		String url = "http://cntm.zjs.com.cn/interface/iwc/nctfendan";
		//String url="http://cntm.zjs.com.cn/interface/iwc/querystatus";
		//String data="{\"clientFlag\": \"客户标识\",\"orders\": [{\"mailNo\": \"A602912931905\"}]}";
		//String data = "{\"clientFlag\": \"test\",\"orders\": [{\"mailNo\": \"A602908107966\"}]}";
		//String data = "[{\"orderNo\":\"121508\",\"address\":\"黑龙江省鹤岗市向阳区二马路龙江省煤营销分公司鹤岗公司\" }]";
		String data = "[{\"orderNo\":\"DH322019041817021620271\",\"address\":" + "\"上海上海市长宁区新泾镇北虹路1185弄\"}]";
		//String data="{\"clientFlag\": \"DianXin_HN\",\"orders\": [{\"mailNo\": \"A005548987905\"}]}";
		
		//data="{\"clientFlag\": \"客户标识\",\"orders\": [{\"mailNo\": \"A001417704024\"},{\"mailNo\":
		// \"A602914589233\"},{\"mailNo\": \"A001417630620\"},{\"mailNo\": \"A001417650253\"},{\"mailNo\":
		// \"A001417660753\"}]}";
		System.out.println("data = " + data);
		String verifyData = GetVerifyDataByrdm(data, "1111", "1111", "test", "aafc04a1bacb487fa8d03f2a7bfdb555");
		System.out.println("verifyData = " + verifyData);
		String str = "";
		System.out.println(verifyData);
		data = URLEncoder.encode(data, "UTF-8");
		long st = System.currentTimeMillis();
		try {
			str = sendPost(url, "data=" + data + "&clientFlag=test&verifyData=" + verifyData);
			
			//生成verifyData的规则:
			// 1. 拼接: 四位随机数 + 客户标识(测试: test) + 报文( {"clientFlag": "test","orders": [{"mailNo": "A602908107966"}]} ) +
			// 密钥(测试密钥: aafc04a1bacb487fa8d03f2a7bfdb555) + 常量值(z宅J急S送g) +四位随机数
			// 2. md5加密上面拼接的字符串,获取 长度 32位 的 校验码
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(System.currentTimeMillis() - st + "----" + str);
		
		
		//String  ss="{\"description\":\"成功!\",\"clientFlag\":\"test\"}";
		//try {
		//	StatusReponse w = jsontool.json2obj(ss, StatusReponse.class);
		//	System.out.println(w.getDescription());
		//} catch (IOException e) {
		//	// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
		
		
	}
	
	
	@Test
	public void t2() {
		//生成verifyData的规则:
		// 1. 拼接: 四位随机数 + 客户标识(测试: test) + 报文( {"clientFlag": "test","orders": [{"mailNo": "A602908107966"}]} ) + 密钥
		// (测试密钥: aafc04a1bacb487fa8d03f2a7bfdb555) + 常量值(z宅J急S送g) +四位随机数
		// 2. md5加密上面拼接的字符串,获取 长度 32位 的 校验码
		String url = "http://businesstest.zjs.com.cn:9200/interface/iwc/querystatustest";
		String data = "{\"clientFlag\": \"test\",\"orders\": [{\"mailNo\": \"A602908107966\"}]}";
		System.out.println("data = " + data);
		String verifyData = GetVerifyDataByrdm(data, "1111", "1111", "test", "aafc04a1bacb487fa8d03f2a7bfdb555");
		System.out.println("verifyData = " + verifyData);
		String str = "";
		System.out.println(verifyData);
		long st = System.currentTimeMillis();
		try {
			str = sendPost(url, "data=" + data + "&clientFlag=test&verifyData=" + verifyData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(System.currentTimeMillis() - st + "----" + str);
	}
	
	
	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
	
	
	public static String GetVerifyDataByrdm(String datajson, String rmd1, String rmd2, String clientFlag,
	                                        String strSeed) {
		
		// 四位随机数 + 客户标识(测试为test) + 报文 + 密钥(测试密钥) + 常量值() +四位随机数
		String str = rmd1 + clientFlag + datajson + strSeed + strConst + rmd2;
		
		String strMd5 = toMD5(str, "UTF-8");
		strMd5 = strMd5.replace("-", " ");
		
		strMd5 = strMd5.toLowerCase();
		
		return rmd1 + strMd5.substring(7, 28) + rmd2;
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
