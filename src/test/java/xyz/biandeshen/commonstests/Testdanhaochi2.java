package xyz.biandeshen.commonstests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;

//import com.cn.zjs.interfaceWrapper.util.PostUtil;
//import com.cn.zjs.interfaceWrapper.util.ReVerifyData;

public class Testdanhaochi2 {
	private static String strConst="z宅J急S送g";
	public static void main(String[] args) {
		
		
		String url="http://cntm.zjs.com.cn/interface/iwc/ctdanhaochitest";
		
		//String jsonData="{\"key\":\"zgpTest\",\"clientFlag\":\"test\",\"applynum\":5}";
		String jsonData="{\"key\":\"DH100580\",\"clientFlag\":\"test\",\"applynum\":5}";


		
		String verifySeed=GetVerifyDataByrdm(jsonData, "1111", "1111", "test", "aafc04a1bacb487fa8d03f2a7bfdb555");
		String str = sendPost(url, "data="+jsonData+"&clientFlag=test&verifyData="+verifySeed);
	    System.out.println(str);
		

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
