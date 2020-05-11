package xyz.biandeshen.代码.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.net.SocketTimeoutException;

/**
 * @author fjp
 * @Title: StandardPostService
 * @ProjectName dj-fjp-customer
 * @Description: SpringBoot 调用接口工具类
 * (如未说明,则默认以Get进行请求)
 * (带重试机制,支持https,http,配合RestTemplateConfig用)
 * (JsonUtils可用其他工具类替换)
 * (StringEscapeUtils需引入 Apache Commons lang 包)
 * (参考地址:https://www.cnblogs.com/EasonJim/p/7684649.html)
 * @date 2019/5/1314:04
 */
@Service
@SuppressWarnings("all")
public class StandardGetService {
	private static Logger logger = LoggerFactory.getLogger(StandardGetService.class);
	
	@Autowired
	private StandardHttpRequestHandler standardHttpRequestHandler;
	
	/**
	 * <p><h2>调用接口(get 带参数 形式)</h2></p>
	 *
	 * @param uuid
	 * 		uuid(记录日志用)
	 * @param url
	 * 		调用地址
	 * @param MultiValueMap
	 * 		MultiValueMap
	 *
	 * @return 调用接口结果
	 *
	 * @throws RuntimeException
	 * 		调用异常
	 */
	@Retryable(value = RuntimeException.class, maxAttempts = 3, backoff = @Backoff(delay = 10L,
			maxDelay = 100L), exclude = {SocketTimeoutException.class})
	public String sendGetRequestWithMultiValueMap(
			String uuid, String url,
			MultiValueMap multiValueMap
	) throws
	  RuntimeException {
		return sendGetRequestWithMultiValueMap(uuid, url, multiValueMap, null);
	}
	
	/**
	 * <p><h2>调用接口(get 带参数 形式)</h2></p>
	 *
	 * @param uuid
	 * 		uuid(记录日志用)
	 * @param url
	 * 		调用地址
	 * @param MultiValueMap
	 * 		MultiValueMap
	 * @param interfaceName
	 * 		调用接口的名称(记录日志用)
	 *
	 * @return 调用接口结果
	 *
	 * @throws RuntimeException
	 * 		调用异常
	 */
	@Retryable(value = RuntimeException.class, maxAttempts = 3, backoff = @Backoff(delay = 10L,
			maxDelay = 100L), exclude = {SocketTimeoutException.class})
	public String sendGetRequestWithMultiValueMap(
			String uuid, String url,
			MultiValueMap multiValueMap,
			String interfaceName
	) throws
	  RuntimeException {
		String responseJson;
		try {
			logger.info("{} 调用{}接口参数为: data={}", uuid, interfaceName, multiValueMap);
			responseJson = standardHttpRequestHandler.standardGet(url, multiValueMap);
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
