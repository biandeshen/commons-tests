package xyz.biandeshen.代码.common;

import com.zjs.edistorage.utils.common.JsonUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.http.conn.ConnectTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.net.BindException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * @author fjp
 * @Title: StandardPostService
 * @ProjectName dj-fjp-customer
 * @Description: SpringBoot 调用接口工具类
 * (如未说明,则默认以Post进行请求)
 * (带重试机制,支持https,http,配合RestTemplateConfig用)
 * (JsonUtils可用其他工具类替换)
 * (StringEscapeUtils需引入 Apache Commons lang 包)
 * (参考地址:https://www.cnblogs.com/EasonJim/p/7684649.html)
 * @date 2019/5/1314:04
 */
@Service
@SuppressWarnings("all")
public class StandardPostService {
	private static Logger logger = LoggerFactory.getLogger(StandardPostService.class);
	
	@Autowired
	private StandardHttpRequestHandler standardHttpRequestHandler;
	
	/**
	 * <p><h2>调用宅急送接口(需验证)</h2></p>
	 *
	 * @param uuid
	 * 		uuid(记录日志用)
	 * @param jsonData
	 * 		json数据报文
	 * @param url
	 * 		调用地址
	 * @param clientFlag
	 * 		客户标识
	 * @param verifyData
	 * 		数据校验码
	 *
	 * @return 调用接口结果
	 *
	 * @throws RuntimeException
	 * 		调用异常
	 */
	@Retryable(include = {ConnectTimeoutException.class, BindException.class, ConnectException.class,
			SocketException.class}, maxAttempts = 3, backoff = @Backoff(delay = 10L, maxDelay = 100L), exclude = {SocketTimeoutException.class})
	public String sendSingleOrderInfoMsg(String uuid, String jsonData, String url, String clientFlag,
	                                     String verifyData) throws RuntimeException {
		return sendSingleOrderInfoMsg(uuid, jsonData, url, clientFlag, verifyData, null);
	}
	
	/**
	 * <p><h2>调用接口(json形式)</h2></p>
	 *
	 * @param uuid
	 * 		uuid(记录日志用)
	 * @param jsonData
	 * 		json数据报文
	 * @param url
	 * 		调用地址
	 *
	 * @return 调用接口结果
	 *
	 * @throws RuntimeException
	 * 		调用异常
	 */
	@Retryable(include = {ConnectTimeoutException.class, BindException.class, ConnectException.class,
			SocketException.class}, maxAttempts = 3, backoff = @Backoff(delay = 10L, maxDelay = 100L), exclude = {SocketTimeoutException.class})
	public String sendSingleOrderInfoMsg(String uuid, String jsonData, String url) throws RuntimeException {
		return sendSingleOrderInfoMsg(uuid, jsonData, url, null);
	}
	
	/**
	 * <p><h2>调用接口(Object形式)</h2></p>
	 *
	 * @param uuid
	 * 		uuid(记录日志用)
	 * @param url
	 * 		调用地址
	 * @param obj
	 * 		调用实体类
	 *
	 * @return 调用接口结果
	 *
	 * @throws RuntimeException
	 * 		调用异常
	 */
	@Retryable(include = {ConnectTimeoutException.class, BindException.class, ConnectException.class,
			SocketException.class}, maxAttempts = 3, backoff = @Backoff(delay = 10L, maxDelay = 100L), exclude = {SocketTimeoutException.class})
	public String sendPostEntityXMLMsg(String uuid, String url, Object obj) throws RuntimeException {
		return sendPostEntityXMLMsg(uuid, url, obj, null);
	}
	
	/**
	 * <p><h2>调用接口(带参数)(Object形式)</h2></p>
	 *
	 * @param uuid
	 * 		uuid(记录日志用)
	 * @param url
	 * 		调用地址
	 * @param obj
	 * 		调用实体类
	 * @param clazz
	 * 		返回实体类类型(Class<T>)
	 * @param mediaType
	 * 		MediaType(请求头设置)
	 * @param uriVariables
	 * 		参数数组(Object...)
	 *
	 * @return 调用接口结果
	 *
	 * @throws RuntimeException
	 * 		调用异常
	 */
	@Retryable(include = {ConnectTimeoutException.class, BindException.class, ConnectException.class,
			SocketException.class}, maxAttempts = 3, backoff = @Backoff(delay = 10L, maxDelay = 100L), exclude = {SocketTimeoutException.class})
	public <T> T sendPostEntityMsgWithMediaType(String uuid, String url, Object obj, Class<T> clazz,
	                                            MediaType mediaType, Object... uriVariables) throws RuntimeException {
		return sendPostEntityMsgWithMediaType(uuid, url, obj, clazz, mediaType, null, uriVariables);
	}
	
	/**
	 * <p><h2>调用接口(linkedMultiValueMap形式)</h2></p>
	 * 请求头(application/x-www-form-urlencoded)
	 *
	 * @param uuid
	 * 		uuid(记录日志用)
	 * @param url
	 * 		调用地址
	 * @param linkedMultiValueMap
	 * 		调用实体类
	 *
	 * @return 调用接口结果
	 *
	 * @throws RuntimeException
	 * 		调用异常
	 */
	@Retryable(include = {ConnectTimeoutException.class, BindException.class, ConnectException.class,
			SocketException.class}, maxAttempts = 3, backoff = @Backoff(delay = 10L, maxDelay = 100L), exclude = {SocketTimeoutException.class})
	public String sendPostFormURLencodedMsg(String uuid, String url, MultiValueMap linkedMultiValueMap) throws RuntimeException {
		return sendPostFormURLencodedMsg(uuid, url, linkedMultiValueMap, null, null);
	}
	
	/**
	 * <p><h2>调用接口(linkedMultiValueMap形式)</h2></p>
	 * 请求头(application/x-www-form-urlencoded)
	 *
	 * @param uuid
	 * 		uuid(记录日志用)
	 * @param url
	 * 		调用地址
	 * @param linkedMultiValueMap
	 * 		调用实体类
	 * @param interfaceName
	 * 		调用接口的名称(记录日志用)
	 *
	 * @return 调用接口结果
	 *
	 * @throws RuntimeException
	 * 		调用异常
	 */
	@Retryable(include = {ConnectTimeoutException.class, BindException.class, ConnectException.class,
			SocketException.class}, maxAttempts = 3, backoff = @Backoff(delay = 10L, maxDelay = 100L), exclude = {SocketTimeoutException.class})
	public String sendPostFormURLencodedMsg(String uuid, String url, MultiValueMap linkedMultiValueMap,
	                                        String interfaceName) throws RuntimeException {
		return sendPostFormURLencodedMsg(uuid, url, linkedMultiValueMap, interfaceName, null);
	}
	
	/**
	 * <p><h2>调用宅急送接口(需验证)</h2></p>
	 *
	 * @param uuid
	 * 		uuid(记录日志用)
	 * @param jsonData
	 * 		json数据报文
	 * @param url
	 * 		调用地址
	 * @param clientFlag
	 * 		客户标识
	 * @param verifyData
	 * 		数据校验码
	 * @param interfaceName
	 * 		调用接口的名称(记录日志用)
	 *
	 * @return 调用接口结果
	 *
	 * @throws RuntimeException
	 * 		调用异常
	 */
	@Retryable(include = {ConnectTimeoutException.class, BindException.class, ConnectException.class,
			SocketException.class}, maxAttempts = 3, backoff = @Backoff(delay = 10L, maxDelay = 100L), exclude = {SocketTimeoutException.class})
	public String sendSingleOrderInfoMsg(String uuid, String jsonData, String url, String clientFlag,
	                                     String verifyData, String interfaceName) throws RuntimeException {
		String responseJson;
		try {
			logger.info("{} 调用{}接口参数为: clientFlag={}&verifyData={}&data={}", uuid, interfaceName, clientFlag,
			            verifyData, jsonData);
			responseJson = standardHttpRequestHandler.zjsStandardPost(jsonData, url, clientFlag, verifyData);
		} catch (Exception e) {
			logger.warn("{} 调用{}接口异常! 异常原因: {}", uuid, interfaceName, e);
			throw new RuntimeException("调用" + interfaceName + "接口发生异常!", e);
		}
		return responseJson;
	}
	
	/**
	 * <p><h2>调用接口(json形式)</h2></p>
	 *
	 * @param uuid
	 * 		uuid(记录日志用)
	 * @param jsonData
	 * 		json数据报文
	 * @param url
	 * 		调用地址
	 * @param interfaceName
	 * 		调用接口的名称(记录日志用)
	 *
	 * @return 调用接口结果
	 *
	 * @throws RuntimeException
	 * 		调用异常
	 */
	@Retryable(include = {ConnectTimeoutException.class, BindException.class, ConnectException.class,
			SocketException.class}, maxAttempts = 3, backoff = @Backoff(delay = 10L, maxDelay = 100L), exclude = {SocketTimeoutException.class})
	public String sendSingleOrderInfoMsg(String uuid, String jsonData, String url, String interfaceName) throws RuntimeException {
		String responseJson;
		try {
			logger.info("{} 调用{}接口参数为: data={}", uuid, interfaceName, jsonData);
			responseJson = standardHttpRequestHandler.standardPost(jsonData, url);
		} catch (Exception e) {
			logger.warn("{} 调用{}接口异常! 异常原因: {}", uuid, interfaceName, e);
			throw new RuntimeException("调用" + interfaceName + "接口发生异常!", e);
		}
		return responseJson;
	}
	
	/**
	 * <p><h2>调用接口(Object形式)</h2></p>
	 *
	 * @param uuid
	 * 		uuid(记录日志用)
	 * @param url
	 * 		调用地址
	 * @param obj
	 * 		调用实体类
	 * @param interfaceName
	 * 		调用接口的名称(记录日志用)
	 *
	 * @return 调用接口结果
	 *
	 * @throws RuntimeException
	 * 		调用异常
	 */
	@Retryable(include = {ConnectTimeoutException.class, BindException.class, ConnectException.class,
			SocketException.class}, maxAttempts = 3, backoff = @Backoff(delay = 10L, maxDelay = 100L), exclude = {SocketTimeoutException.class})
	public String sendPostEntityXMLMsg(String uuid, String url, Object obj, String interfaceName) throws RuntimeException {
		String responseJson;
		try {
			logger.info("{} 调用{}接口参数为: data={}", uuid, interfaceName, obj.toString());
			responseJson = standardHttpRequestHandler.commonEntityStandardRequest(url, obj, MediaType.TEXT_XML,
			                                                                      String.class, HttpMethod.POST,
			                                                                      (Object[]) null);
		} catch (Exception e) {
			logger.warn("{} 调用{}接口异常! 异常原因: {}", uuid, interfaceName, e);
			throw new RuntimeException("调用" + interfaceName + "接口发生异常!", e);
		}
		return responseJson;
	}
	
	/**
	 * <p><h2>调用接口(带参数)(Object形式)</h2></p>
	 *
	 * @param uuid
	 * 		uuid(记录日志用)
	 * @param url
	 * 		调用地址
	 * @param obj
	 * 		调用实体类
	 * @param clazz
	 * 		返回实体类类型(Class<T>)
	 * @param mediaType
	 * 		MediaType(请求头设置)
	 * @param interfaceName
	 * 		调用接口的名称(记录日志用)
	 * @param uriVariables
	 * 		参数数组(Object...)
	 *
	 * @return 调用接口结果
	 *
	 * @throws RuntimeException
	 * 		调用异常
	 */
	@Retryable(include = {ConnectTimeoutException.class, BindException.class, ConnectException.class,
			SocketException.class}, maxAttempts = 3, backoff = @Backoff(delay = 10L, maxDelay = 100L), exclude = {SocketTimeoutException.class})
	public <T> T sendPostEntityMsgWithMediaType(String uuid, String url, Object obj, Class<T> clazz,
	                                            MediaType mediaType, String interfaceName, Object... uriVariables) throws RuntimeException {
		T responseEntity;
		try {
			logger.info("{} 调用{}接口参数为: data={}", uuid, interfaceName, obj.toString());
			responseEntity = standardHttpRequestHandler.commonEntityStandardRequest(url, obj, mediaType, clazz,
			                                                                        HttpMethod.POST, uriVariables);
		} catch (Exception e) {
			logger.warn("{} 调用{}接口异常! 异常原因: {}", uuid, interfaceName, e);
			throw new RuntimeException("调用" + interfaceName + "接口发生异常!", e);
		}
		return responseEntity;
	}
	
	/**
	 * <p><h2>调用接口(linkedMultiValueMap形式)</h2></p>
	 * 请求头(application/x-www-form-urlencoded)
	 *
	 * @param uuid
	 * 		uuid(记录日志用)
	 * @param url
	 * 		调用地址
	 * @param linkedMultiValueMap
	 * 		调用实体类
	 * @param interfaceName
	 * 		调用接口的名称(记录日志用)
	 *
	 * @return 调用接口结果
	 *
	 * @throws RuntimeException
	 * 		调用异常
	 */
	@Retryable(include = {ConnectTimeoutException.class, BindException.class, ConnectException.class,
			SocketException.class}, maxAttempts = 3, backoff = @Backoff(delay = 10L, maxDelay = 100L), exclude = {SocketTimeoutException.class})
	public String sendPostFormURLencodedMsg(String uuid, String url, MultiValueMap linkedMultiValueMap,
	                                        String interfaceName, HttpHeaders httpHeaders) throws RuntimeException {
		String responseJson;
		try {
			logger.info("{} 调用{}接口参数为: data={}, headers={}", uuid, interfaceName,
			            StringEscapeUtils.unescapeJava(JsonUtils.beanToJsonGsonImpl(linkedMultiValueMap)),
			            StringEscapeUtils.unescapeJava(JsonUtils.beanToJsonGsonImpl(httpHeaders)));
			responseJson = standardHttpRequestHandler.commonFormURLENCODEDStandardPost(linkedMultiValueMap, url,
			                                                                           httpHeaders != null ?
					                                                                           httpHeaders : null);
		} catch (Exception e) {
			logger.warn("{} 调用{}接口异常! 异常原因: {}", uuid, interfaceName, e);
			throw new RuntimeException("调用" + interfaceName + "接口发生异常!", e);
		}
		return responseJson;
	}
	
	/**
	 * 重试失败回掉方法,支持自定义处理(需加Recover注解)
	 *
	 * @param e
	 *
	 * @return
	 */
	@Recover
	public String recover(RuntimeException e) {
		throw new RuntimeException("多次重试调用接口仍旧异常", e);
	}
}

