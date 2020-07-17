package xyz.biandeshen.commonstests;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @author: WangRui
 * @create: 2018/12/22 14:34
 * @description:
 **/

public class ZjsStandardPostUtil {
	
	public static String zjsStandardPost(String jsonData, String url, String clientFlag, String verifyData) {
		
		MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
		//postParameters.add("clientFlag", clientFlag);
		//postParameters.add("verifyData", verifyData);
		//postParameters.add("data", jsonData);
		postParameters.add("age", 12);
		postParameters.add("name", "test");
		postParameters.add("info", "infoclass");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/x-www-form-urlencoded");
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(postParameters, headers);
		RestTemplate restTemplate = new RestTemplate();
		
		return restTemplate.postForObject(url, request, String.class);
	}
	
	//	public static void main(String[] args) {
	//		String jsonData = " [{  " + " \"orderNo\": \"DD12345678911\", " +
	//
	//				" \"address\": \"山东省聊城莘县十八里铺\" " + " }, " + " { " + " \"orderNo\": \"DD12345678912\", " +
	//
	//				" \"address\": \"江苏省淮安市盱眙县官滩镇和谐家园(龙宝西街北50米)和谐家园1\" }]";
	//		//      String url="http://10.10.6.189:8109/fendan";
	//		String url = "http://cntm.zjs.com.cn/interface/iwc/nctfendantest";
	//		String mm = zjsStandardPost(jsonData, url, "test", "aafc04a1bacb487fa8d03f2a7bfdb555");
	//		System.out.println(mm);
	//	}
		public static void main(String[] args) {
			String url = "http://10.10.12.109:8081/user7";
			String mm = zjsStandardPost(null, url, "test", "aafc04a1bacb487fa8d03f2a7bfdb555");
			System.out.println(mm);
		}
	
}
