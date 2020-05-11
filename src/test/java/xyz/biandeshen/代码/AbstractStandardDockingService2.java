package xyz.biandeshen.代码;

import com.zjs.edistorage.service.common.StandardGetService;
import com.zjs.edistorage.service.common.StandardPostService;
import com.zjs.edistorage.utils.common.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import spp.model.mongodb.Edi_Log;

import java.util.Date;

/**
 * @author fjp
 * @Title: AbstractOrderStatusController
 * @ProjectName dj-fjp-yl
 * @Description: 抽象状态推送
 * @date 2019/8/1911:05
 */
@Component
@SuppressWarnings("all")
public abstract class AbstractStandardDockingService2 {
	private static final Logger logger = LoggerFactory.getLogger(AbstractStandardDockingService2.class);
	
	/**
	 * 接口名称 枚举
	 */
	enum InterfaceNameEnum {/**
	 * 默认的接口名称
	 */
	Order("下单"), Status("状态推送"), Fendan("分单"), Danhao("获取单号");
		
		private String interfaceName;
		
		InterfaceNameEnum(String interfaceName) {
			this.interfaceName = interfaceName;
		}}
	
	/**
	 * 标准post请求服务
	 */
	@Autowired
	private StandardPostService standardPostService;
	
	/**
	 * 标准get请求服务
	 */
	@Autowired
	private StandardGetService standardGetService;
	
	static Edi_Log edilogtrue(String req, String resp) {
		return new Edi_Log(true, req, resp);
	}
	
	static Edi_Log edilogfalse(String req, String resp, String ex) {
		return new Edi_Log(false, req, resp, ex);
	}
	
	static Edi_Log edilogother(String wc, String cc, String cf, boolean status, Date rt, String req, String resp) {
		return new Edi_Log(wc, cc, cf, status, rt, req, resp);
	}
	
	///**
	// * 具体处理模块
	// *
	// * @return 处理后记录Edi_Log结果
	// */
	//public abstract Edi_Log orderStatusHandler();
	
	/**
	 * 根据传入args,生成对应的post请求参数
	 * <br>(根据文档,自己覆盖重写)</br>
	 *
	 * @param args
	 * 		args,传入参数,即map的value
	 *
	 * @return 自定义的post请求的map
	 *
	 * @apiNote <p>
	 * <br>MultiValueMap<String, Object> getStringObjectMultiValueMap(Object... args) {</br>
	 * <br>MultiValueMap<String, Object> linkedMultiValueMap = new LinkedMultiValueMap<>();</br>
	 * <br>linkedMultiValueMap.add("content", args[0]);</br>
	 * <br>return linkedMultiValueMap;</br>
	 * <br>}</br>
	 * </p>
	 */
	protected abstract MultiValueMap<String, Object> getStringObjectMultiValueMap(Object... args);
	
	
	/**
	 * 外部接口调用服务
	 */
	public abstract class ExternalInterfaceInvocationService {
		/**
		 * 具体处理模块
		 *
		 * @param uuid
		 * 		uuid
		 * @param cmm
		 * 		传入订单信息
		 * @param invokedInterfaceUrl
		 * 		接口调用的Url
		 * @param stringObjectMultiValueMap
		 * 		参数名及参数map  若实现了getStringObjectMultiValueMap，则可为null
		 * @param httpHeaders
		 * 		HTTP请求头,可为空
		 *
		 * @return 处理后记录String结果
		 */
		protected String orderStatusHandler(String uuid, String invokedInterfaceUrl, String interfaceName,
		                                    MultiValueMap<String, Object> stringObjectMultiValueMap,
		                                    HttpHeaders httpHeaders, boolean requestTypeIsGet) {
			if (stringObjectMultiValueMap == null) {
				stringObjectMultiValueMap = getStringObjectMultiValueMap();
			}
			return getInvocationInterfaceResult(uuid, invokedInterfaceUrl, interfaceName, stringObjectMultiValueMap,
			                                    httpHeaders, requestTypeIsGet);
		}
		
		
		/**
		 * 根据传入参数和地址,调用结果并返回接口返回结果
		 *
		 * @param uuid
		 * 		uuid,记录日志用
		 * @param linkedMultiValueMap
		 * 		map,post请求的参数
		 * @param invokedInterfaceUrl
		 * 		url,客户接受请求的url地址
		 * @param interfaceName
		 * 		接口名称,方便记录日志
		 * @param httpHeaders
		 * 		HTTP请求头,可为空
		 * @param requestTypeIsGet
		 * 		请求类型(get,post)
		 * 		true : false
		 * 		->
		 * 		get : post
		 *
		 * @return 调用结果
		 */
		protected String getInvocationInterfaceResult(String uuid, String invokedInterfaceUrl, String interfaceName,
		                                              MultiValueMap<String, Object> linkedMultiValueMap,
		                                              HttpHeaders httpHeaders, boolean requestTypeIsGet) throws RuntimeException {
			String responseResult = "";
			try {
				if (requestTypeIsGet) {
					responseResult = getMethodResponseResult(uuid, linkedMultiValueMap, invokedInterfaceUrl,
					                                         interfaceName);
				} else {
					responseResult = postMethodResponseResult(uuid, linkedMultiValueMap, invokedInterfaceUrl,
					                                          interfaceName, httpHeaders);
				}
				//先判空
				if (StringUtils.isEmpty(responseResult)) {
					logger.warn("{} 调用{}接口成功,接口返回结果为空!", uuid, interfaceName);
					//跳出循环
					throw new RuntimeException("调用" + interfaceName + "接口结果为空!");
				}
				logger.info("{} 调用{}接口成功,接口返回结果为: {}", uuid, responseResult, interfaceName);
			} catch (RuntimeException e) {
				logger.warn("{} 调用{}接口异常,接口返回结果为: {},异常原因: {}", uuid, interfaceName, responseResult, e);
				//跳出循环
				throw new RuntimeException("调用" + interfaceName + "接口异常! ", e);
			}
			return responseResult;
		}
		
		/**
		 * get 请求方式调用结果 (可自定义扩展实现)
		 *
		 * @param uuid
		 * 		uuid
		 * @param invokedInterfaceUrl
		 * 		更新路由地址url
		 * @param interfaceName
		 * 		接口名称
		 * @param stringObjectMultiValueMap
		 * 		请求参数值
		 *
		 * @return 接口调用结果
		 */
		protected String getMethodResponseResult(String uuid, MultiValueMap<String, Object> stringObjectMultiValueMap,
		                                         String invokedInterfaceUrl, String interfaceName) {
			return standardGetService.sendGetRequestWithMultiValueMap(uuid, invokedInterfaceUrl,
			                                                          stringObjectMultiValueMap, interfaceName);
		}
		
		/**
		 * post 请求方式请求接口 (可自定义扩展实现)
		 *
		 * @param uuid
		 * 		uuid
		 * @param invokedInterfaceUrl
		 * 		更新路由地址url
		 * @param interfaceName
		 * 		接口名称
		 * @param stringObjectMultiValueMap
		 * 		请求参数值
		 * @param httpHeaders
		 * 		请求头,若无则为空
		 *
		 * @return 接口调用结果
		 */
		protected String postMethodResponseResult(String uuid, MultiValueMap<String, Object> stringObjectMultiValueMap
				, String invokedInterfaceUrl, String interfaceName, HttpHeaders httpHeaders) {
			return standardPostService.sendPostFormURLencodedMsg(uuid, invokedInterfaceUrl, stringObjectMultiValueMap,
			                                                     interfaceName, httpHeaders);
		}
	}
	
	
	/**
	 * 内部接口调用服务
	 */
	public abstract class InternalInterfaceInvocationService {
		
		/**
		 * 具体处理模块
		 *
		 * @param uuid
		 * 		uuid
		 * @param invokedInterfaceUrl
		 * 		接口调用的Url
		 * @param interfaceName
		 * 		接口名称
		 * @param jsonData
		 * 		json报文
		 * @param clientFlag
		 * 		客户标识
		 * @param verifyData
		 * 		签名
		 *
		 * @return 处理后记录Edi_Log结果
		 */
		protected Edi_Log orderStatusHandler(String uuid, String invokedInterfaceUrl, String interfaceName,
		                                     String jsonData, String clientFlag, String verifyData) {
			if (StringUtils.isEmpty(jsonData)) {
				jsonData = "{}";
			}
			return getEdiLogRespResultByJson(uuid, invokedInterfaceUrl, interfaceName, jsonData, clientFlag,
			                                 verifyData);
		}
		
		/**
		 * 根据传入参数和地址,调用结果并返回接口返回结果
		 *
		 * @param uuid
		 * 		uuid
		 * @param invokedInterfaceUrl
		 * 		接口调用的Url
		 * @param interfaceName
		 * 		接口名称
		 * @param jsonData
		 * 		json报文
		 * @param clientFlag
		 * 		客户标识
		 * @param verifyData
		 * 		签名
		 *
		 * @return 调用结果
		 */
		protected String getInvocationInterfaceResult(String uuid, String invokedInterfaceUrl, String interfaceName,
		                                              String jsonData, String clientFlag, String verifyData) throws RuntimeException {
			String responseResult = "";
			try {
				responseResult = zjsMethodResponseResult(uuid, jsonData, invokedInterfaceUrl, clientFlag, verifyData,
				                                         interfaceName);
				//先判空
				if (StringUtils.isEmpty(responseResult)) {
					logger.warn("{} 调用{}接口成功,接口返回结果为空!", uuid, interfaceName);
					//跳出循环
					throw new RuntimeException("调用" + interfaceName + "接口结果为空!");
				}
				logger.info("{} 调用{}接口成功,接口返回结果为: {}", uuid, interfaceName, responseResult);
			} catch (RuntimeException e) {
				logger.warn("{} 调用{}接口异常,接口返回结果为: {},异常原因: {}", uuid, interfaceName, responseResult, e);
				//跳出循环
				throw new RuntimeException("调用" + interfaceName + "接口异常!", e);
			}
			return responseResult;
		}
		
		/**
		 * zjs 标准请求方式调用结果 (可自定义扩展实现)
		 *
		 * @param uuid
		 * 		uuid
		 * @param invokedInterfaceUrl
		 * 		接口调用的Url
		 * @param interfaceName
		 * 		接口名称
		 * @param jsonData
		 * 		json报文
		 * @param clientFlag
		 * 		客户标识
		 * @param verifyData
		 * 		签名
		 *
		 * @return 调用接口结果
		 */
		protected String zjsMethodResponseResult(String uuid, String jsonData, String invokedInterfaceUrl,
		                                         String clientFlag, String verifyData, String interfaceName) {
			return standardPostService.sendSingleOrderInfoMsg(uuid, jsonData, invokedInterfaceUrl, clientFlag,
			                                                  verifyData, interfaceName);
		}
		
		/**
		 * 通过获取和检查更新跟踪结果获得Edi日志
		 *
		 * @param uuid
		 * 		uuid
		 * @param invokedInterfaceUrl
		 * 		接口调用的Url
		 * @param interfaceName
		 * 		接口名称
		 * @param jsonData
		 * 		json报文
		 * @param clientFlag
		 * 		客户标识
		 * @param verifyData
		 * 		签名
		 *
		 * @return eid_log 日志,是否需要重推数据
		 */
		public Edi_Log getEdiLogRespResultByJson(String uuid, String invokedInterfaceUrl, String interfaceName,
		                                         String jsonData, String clientFlag, String verifyData) {
			if (jsonData != null) {
				try {
					/*通过MultiValueMap,createOrderUrl获取调用平台揽件信息接收接口的返回结果*/
					String invocationInterfaceResult = getInvocationInterfaceResult(uuid, invokedInterfaceUrl,
					                                                                interfaceName, jsonData,
					                                                                clientFlag, verifyData);
					if (StringUtils.isNotEmpty(invocationInterfaceResult) && judgeResult(uuid,
					                                                                     invocationInterfaceResult)) {
						logger.info("{} 调用{}接口返回成功结果= {}", uuid, interfaceName, invocationInterfaceResult);
						return edilogtrue("请求数据为=" + StringEscapeUtils.unescapeJava(JsonUtils.beanToJsonGsonImpl(jsonData)), "接口返回成功结果:" + invocationInterfaceResult);
					} else {
						logger.warn("{} 调用{}接口返回失败结果= {}", uuid, interfaceName, invocationInterfaceResult);
						return edilogfalse("请求数据为=" + StringEscapeUtils.unescapeJava(JsonUtils.beanToJsonGsonImpl(jsonData)), "接口返回失败结果:" + invocationInterfaceResult, null);
					}
				} catch (Exception e) {
					//异常
					logger.warn("{} 调用{}接口返回异常结果! 异常原因: {}", uuid, interfaceName, e);
					return edilogfalse("请求数据为=" + StringEscapeUtils.unescapeJava(JsonUtils.beanToJsonGsonImpl(jsonData)), "接口返回异常结果:", e.toString());
				}
			}
			//	 失败 or 错误
			logger.warn("{} 调用{}接口参数为空! ", uuid, interfaceName);
			return edilogfalse("当前错误状态: 请求数据为空! ", "未调用接口! ", null);
		}
		
	}
	
	/**
	 * 扩展的结果处理程序,自定义以实现具体的判断逻辑
	 *
	 * @param uuid
	 * 		uuid
	 * @param invocationInterfaceResult
	 * 		调用客户状态接口返回的结果
	 *
	 * @return 结果处理器
	 *
	 * @throws Exception
	 * 		抛出异常
	 */
	protected abstract boolean judgeResult(String uuid, String invocationInterfaceResult) throws Exception;
}
