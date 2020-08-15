package com.zjs.dj.warehouse.standardorder.config;

import com.zjs.dj.warehouse.standardorder.dict.CommonResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

/**
 * @author fjp
 * @Title: GlobalReturnConfig
 * @Description: 全局返回值、全局异常统一封装
 * @date 2019/11/18 8:59
 */
@Configuration
@SuppressWarnings("all")
public class GlobalReturnConfig {
	/**
	 * 统一异常处理（默认json格式）
	 */
	@RestControllerAdvice
	// 方法上应添加 ResponseBody
	static class GlobalExceptionResponseHandler {
		private static Logger logger = LoggerFactory.getLogger(GlobalExceptionResponseHandler.class);
		
		/**
		 * 全局异常处理（默认json格式）
		 */
		@ExceptionHandler(Exception.class)
		// 添加此注解 以返回正常的json字符串
		//@ResponseBody
		public void handleException(HttpServletRequest request, HttpServletResponse response, Exception e) throws IOException, JAXBException {
			//todo 测试时使用 打印异常信息 正式注释，则不会打印异常
			//log.error(e.getMessage(), e);
			GlobalResult<Object> globalResult;
			//异常类型
			if (e instanceof GlobalException) {
				//业务异常
				GlobalException globalException = (GlobalException) e;
				// 获取异常信息，若异常信息为空，则提示系统内部错误
				String message = globalException.getMessage() != null ? globalException.getMessage() : "系统内部错误！";
				// 使用Json字符串规则转义中的字符
				// 将找到的任何值转义成Json字符串形式
				// 正确处理引号和控制字符(制表符、反斜杠、cr、ff等)
				globalResult = new GlobalResult.Builder(globalException.getCode() == null ?
						                                        CommonResponseEntity.S0006.getCode() :
						                                        globalException.getCode(),
				                                        "调用接口" + request.getRequestURI().toString() + "失败，" + message).data(globalException.getObj()).flag("failure").build();
				logger.warn("{} 宅急送接口异常响应结果:  {}，异常原因: {}", globalException.getUuid(), globalResult.toString(), e);
			} else {
				//系统异常
				globalResult = new GlobalResult.Builder(CommonResponseEntity.S0006.getCode(), "系统内部错误!").flag("failure"
				).build();
				logger.error("{} 宅急送接口错误响应结果:  {}，错误原因: {} 调用ip: {}", Instant.now().getEpochSecond(),
				             globalResult.toString(), e,
				             request.getRemoteAddr() + " " + request.getRemoteHost() + " " + request.getRemotePort() + " " + request.getMethod() + " " + request.getRequestURI() + " " + request.getContentType() + " " + request.getHeader("Accept"));
			}
			
			response.setCharacterEncoding(StandardCharsets.UTF_8.name());
			// 从给定内容类型设置响应的字符编码
			response.setContentType(MediaType.TEXT_PLAIN_VALUE);
			String str = globalResult.toXMLString();
			//response.getWriter().println(UnicodeBackslashUtils.unicodeToCn(str));
			response.getWriter().println(str);
		}
	}
}

