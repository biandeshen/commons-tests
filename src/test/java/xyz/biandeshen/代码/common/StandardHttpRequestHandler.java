package xyz.biandeshen.代码.common;

import cn.com.zjs.cainiaostatus.util.JsonUtils;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author fjp
 * @Title: StandardPostHandler
 * @ProjectName dj-fjp-customer
 * @Description: SpringBoot 调用接口处理器
 **/


@SuppressWarnings("all")
@Component
public class StandardHttpRequestHandler {
	private final RestTemplate restTemplate;
	
	@Autowired
	public StandardHttpRequestHandler(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	public String zjsStandardPost(
			String jsonData, String url, String clientFlag,
			String verifyData
	) {
		MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
		postParameters.add("clientFlag", clientFlag);
		postParameters.add("verifyData", verifyData);
		postParameters.add("data", jsonData);
		return commonFormURLENCODEDStandardPost(postParameters, url);
	}
	
	public String standardPost(String jsonData, String url) {
		MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
		return commonJsonStandardPost(jsonData, url, type);
	}
	
	
	public String commonJsonStandardPost(String jsonData, String url, MediaType mediaType) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(mediaType);
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		JsonObject jsonObject = JsonUtils.parseJsonToBeanGsonImpl(jsonData, JsonObject.class);
		HttpEntity<String> formEntity = new HttpEntity<>(jsonObject.toString(), headers);
		return restTemplate.postForObject(url, formEntity, String.class);
	}
	
	public String commonFormURLENCODEDStandardPost(MultiValueMap linkedMultiValueMap, String url) {
		return commonFormURLENCODEDStandardPost(linkedMultiValueMap, url, null);
	}
	
	public String commonFormURLENCODEDStandardPost(
			MultiValueMap linkedMultiValueMap, String url,
			HttpHeaders httpHeaders
	) {
		if (httpHeaders == null) {
			httpHeaders = new HttpHeaders();
		}
		//HttpHeaders headers = new HttpHeaders();
		if (httpHeaders.getContentType() == null) {
			httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		}
		/*if (httpHeaders != null) {
			if (httpHeaders.keySet() != null) {
				for (String headerKey : httpHeaders.keySet()) {
					headers.add(headerKey, String.valueOf(httpHeaders.get(headerKey).get(0)));

				}
			}
		}*/
		//原因: LinkedMultiValueMap实际就是Key-LinkedList的map。
		//RestTemplate在postForObject时，不可使用HashMap。而应该是MultiValueMap。
		//当接口参数没有被@RequestBody修饰的时候，使用LinkedMultiValueMap传递参数(MediaType
		// .APPLICATION_FORM_URLENCODED)
		//当接口参数被@RequestBody修饰的时候，可以使用HashMap，实体类等传递参数(MediaType.APPLICATION_JSON)
		//地址1: https://www.cnblogs.com/shoren/p/RestTemplate-problem.html
		//地址2: https://my.oschina.net/haokevin/blog/2254206
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(
				linkedMultiValueMap,
				httpHeaders
		);
		return restTemplate.postForObject(url, request, String.class);
	}
	
	public String standardGet(String url, MultiValueMap linkedMultiValueMap) {
		StringBuilder sbUrl = new StringBuilder(url);
		Set set = linkedMultiValueMap.keySet();
		if (set.size() > 0) {
			sbUrl.append("?");
			set.forEach(x -> sbUrl.append(x.toString()).append("={").append(x.toString()).append(
					"}").append("&"));
			//lambda 与 此等效
			//for (Object obj : set) {
			//	sbUrl.append(obj.toString()).append("={").append(obj.toString()).append("}")
			//	.append("&");
			//}
			sbUrl.delete(sbUrl.lastIndexOf("&"), sbUrl.length());
		}
		//需转换为普通的map
		//原因: LinkedMultiValueMap实际就是Key-LinkedList的map。
		//RestTemplate在postForObject时，不可使用HashMap。而应该是MultiValueMap。
		//当接口参数没有被@RequestBody修饰的时候，使用LinkedMultiValueMap传递参数(MediaType
		// .APPLICATION_FORM_URLENCODED)
		//当接口参数被@RequestBody修饰的时候，可以使用HashMap，实体类等传递参数(MediaType.APPLICATION_JSON)
		//地址1: https://www.cnblogs.com/shoren/p/RestTemplate-problem.html
		//地址2: https://my.oschina.net/haokevin/blog/2254206
		Map paramsMap = linkedMultiValueMap.toSingleValueMap();
		return restTemplate.getForObject(sbUrl.toString(), String.class, paramsMap);
	}
	
	public String commonEntityStandardPost(Object obj, String url, MediaType mediaType) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(mediaType);
		HttpEntity<Object> formEntity = new HttpEntity<>(obj, headers);
		return restTemplate.postForObject(url, formEntity, String.class);
	}
	
	public <T> T commonEntityStandardRequest(
			String url, Object obj, MediaType mediaType,
			Class<T> clazz, HttpMethod httpMethod,
			Object... uriVariables
	) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(mediaType);
		headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
		HttpEntity<Object> formEntity = new HttpEntity<>(obj, headers);
		ResponseEntity<T> responseEntity = restTemplate.exchange(url, httpMethod, formEntity,
		                                                         clazz, uriVariables
		);
		return responseEntity.getBody();
	}
	
	
	//	public static void main(String[] args) {
	//		String jsonData = " [{  " + " \"orderNo\": \"DD12345678911\", " +
	//
	//				" \"address\": \"山东省聊城莘县十八里铺\" " + " }, " + " { " + " \"orderNo\":
	//				\"DD12345678912\", " +
	//
	//				" \"address\": \"江苏省淮安市盱眙县官滩镇和谐家园(龙宝西街北50米)和谐家园1\" }]";
	//		//      String url="http://10.10.6.189:8109/fendan";
	//		String url = "http://cntm.zjs.com.cn/interface/iwc/nctfendantest";
	//		String mm = zjsStandardPost(jsonData, url, "test", "aafc04a1bacb487fa8d03f2a7bfdb555");
	//		System.out.println(mm);
	//	}
	//
	//public static void main(String[] args) {
	//	MultiValueMap multiValueMap = new LinkedMultiValueMap();
	//	multiValueMap.add("vwocode", "A602936776646");
	//	multiValueMap.add("sysType", "CaiNiao_sign_service");
	//	String url = "http://10.10.6.93:30200/zjs-athena-remote/restful/queryVrecvaddr
	//	/queryVrecvaddrByVwocode";
	//	RestTemplate restTemplate = new RestTemplate();
	//	restTemplate.setRequestFactory(new RestTemplateConfig
	//	.HttpComponentsClientRestfulHttpRequestFactory());
	//	StandardPostHandler standardPostHandler = new StandardPostHandler(restTemplate);
	//	String mm = standardPostHandler.standardGet(multiValueMap, url);
	//	System.out.println(mm);
	//}
}