package xyz.biandeshen.代码;

import com.zjs.customer.danniao.service.common.StandardPostService;
import com.zjs.customer.danniao.utils.common.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import spp.model.common.CommonMsgModel;
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
public abstract class AbstractOrderStatusService {
	private static final Logger logger = LoggerFactory.getLogger(AbstractOrderStatusService.class);
	
	/**
	 * 标准post请求服务
	 */
	@Autowired
	private StandardPostService standardPostService;
	
	
	static Edi_Log edilogtrue(String req, String resp) {
		return new Edi_Log(true, req, resp);
	}
	
	static Edi_Log edilogfalse(String req, String resp, String ex) {
		return new Edi_Log(false, req, resp, ex);
	}
	
	static Edi_Log edilogother(String wc, String cc, String cf, boolean status, Date rt, String req,
	                           String resp) {
		return new Edi_Log(wc, cc, cf, status, rt, req, resp);
	}
	
	
	/**
	 * 状态推送具体处理模块
	 *
	 * @param uuid
	 * 		uuid
	 * @param cmm
	 * 		传入订单信息
	 * @param updateTrackUrl
	 * 		更新状态url(客户提供的地址)
	 * @param stringObjectMultiValueMap
	 * 		参数名及参数map
	 *
	 * @return 处理后记录Edi_Log结果
	 */
	public Edi_Log orderStatusHandler(String uuid, CommonMsgModel cmm, String updateTrackUrl, MultiValueMap<String,
			                                                                                                       Object> stringObjectMultiValueMap) {
		return orderStatusHandler(uuid, cmm, updateTrackUrl, stringObjectMultiValueMap, null);
	}
	
	/**
	 * 状态推送具体处理模块
	 *
	 * @param uuid
	 * 		uuid
	 * @param cmm
	 * 		传入订单信息
	 * @param updateTrackUrl
	 * 		更新状态url(客户提供的地址)
	 * @param stringObjectMultiValueMap
	 * 		参数名及参数map
	 * @param httpHeaders
	 * 		HTTP请求头,可为空
	 *
	 * @return 处理后记录Edi_Log结果
	 */
	protected Edi_Log orderStatusHandler(String uuid, CommonMsgModel cmm, String updateTrackUrl,
	                                     MultiValueMap<String,
			                                                  Object> stringObjectMultiValueMap,
	                                     HttpHeaders httpHeaders) {
		if (stringObjectMultiValueMap == null) {
			stringObjectMultiValueMap = getStringObjectMultiValueMap();
		}
		return getEdiLogByObtainAndCheckUpdateTraceRespResult(uuid,
		                                                      cmm.getNd(),
		                                                      updateTrackUrl,
		                                                      stringObjectMultiValueMap,
		                                                      httpHeaders);
	}
	
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
	 * 根据传入参数和地址,调用结果并返回接口返回结果
	 *
	 * @param uuid
	 * 		uuid,记录日志用
	 * @param linkedMultiValueMap
	 * 		map,post请求的参数
	 * @param updateTrackUrl
	 * 		url,客户接受请求的url地址
	 * @param httpHeaders
	 * 		HTTP请求头,可为空
	 *
	 * @return 调用结果
	 */
	private String getUpdateTrackResponseResult(String uuid,
	                                            MultiValueMap<String, Object> linkedMultiValueMap,
	                                            String updateTrackUrl, HttpHeaders httpHeaders) throws
	                                                                                            RuntimeException {
		String updateTrackResponseResult = "";
		try {
			updateTrackResponseResult = standardPostService.sendPostFormURLencodedMsg(uuid, updateTrackUrl,
			                                                                          linkedMultiValueMap, "状态推送接口",
			                                                                          httpHeaders);
			//先判空
			if (StringUtils.isEmpty(updateTrackResponseResult)) {
				logger.warn("{} 调用状态推送接口成功,接口返回结果为空!", uuid);
				//跳出循环
				throw new RuntimeException("调用状态推送接口结果为空! ");
			}
			logger.info("{} 调用状态推送接口成功,接口返回结果为: {}", uuid, updateTrackResponseResult);
		} catch (RuntimeException e) {
			logger.warn("{} 调用状态推送接口异常,接口返回结果为: {},异常原因: {}", uuid, updateTrackResponseResult, e);
			//跳出循环
			throw new RuntimeException("调用状态推送接口异常! ", e);
		}
		return updateTrackResponseResult;
	}
	
	/**
	 * 通过获取和检查更新跟踪结果获得Edi日志
	 *
	 * @param uuid
	 * 		uuid
	 * @param node
	 * 		当前推送节点
	 * @param updateTrackUrl
	 * 		更新路由地址url
	 * @param stringObjectMultiValueMap
	 * 		请求参数值
	 * @param httpHeaders
	 * 		请求头,若无则为空
	 *
	 * @return eid_log 日志,是否需要重推数据
	 */
	public Edi_Log getEdiLogByObtainAndCheckUpdateTraceRespResult(String uuid, String node,
	                                                              String updateTrackUrl,
	                                                              MultiValueMap<String, Object> stringObjectMultiValueMap, HttpHeaders httpHeaders) {
		if (stringObjectMultiValueMap != null) {
			try {
				/*通过MultiValueMap,createOrderUrl获取调用平台揽件信息接收接口的返回结果*/
				String updateTrackResponseResult = getUpdateTrackResponseResult(uuid, stringObjectMultiValueMap,
				                                                                updateTrackUrl, httpHeaders);
				if (StringUtils.isNotEmpty(updateTrackResponseResult) && judgeResult(uuid,
				                                                                     updateTrackResponseResult)) {
					logger.info("{} 调用状态回传接口返回成功结果= {}", uuid, updateTrackResponseResult);
					return edilogtrue("当前正常状态: nd= " + node + "请求数据为=" + StringEscapeUtils.unescapeJava(JsonUtils.beanToJsonGsonImpl(stringObjectMultiValueMap)), "接口返回成功结果:" + updateTrackResponseResult);
				} else {
					logger.warn("{} 调用状态回传接口返回失败结果= {}", uuid, updateTrackResponseResult);
					return edilogfalse("当前失败状态: nd= " + node + "请求数据为=" + StringEscapeUtils.unescapeJava(JsonUtils.beanToJsonGsonImpl(stringObjectMultiValueMap)), "接口返回失败结果:" + updateTrackResponseResult, null);
				}
			} catch (Exception e) {
				//异常
				logger.warn("{} 调用状态回传接口返回异常结果! 异常原因: {}", uuid, e);
				return edilogfalse("当前异常状态: nd= " + node + "请求数据为=" + StringEscapeUtils.unescapeJava(JsonUtils.beanToJsonGsonImpl(stringObjectMultiValueMap)), "接口返回异常结果:", e.toString());
			}
		}
		//	 失败 or 错误
		logger.warn("{} 调用状态回传接口参数为空! ", uuid);
		return edilogfalse("当前错误状态: nd= " + node + "请求数据为空! ", "未调用客户状态接收接口! ", null);
	}
	
	
	/**
	 * 扩展的结果处理程序,自定义以实现具体的判断逻辑
	 *
	 * @param uuid
	 * 		uuid
	 * @param updateTrackResponseResult
	 * 		调用客户状态接口返回的结果
	 *
	 * @return 结果处理器
	 *
	 * @throws Exception
	 * 		抛出异常
	 */
	protected abstract boolean judgeResult(String uuid, String updateTrackResponseResult) throws Exception;
}
