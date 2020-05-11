package xyz.biandeshen.代码;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * @author fjp
 * @Title: GlobalReturnConfig
 * @Description: 全局返回值、全局异常统一封装
 * @date 2019/11/188:59
 */
//@EnableWebMvc
//@Configuration
@SuppressWarnings("all")
public class GlobalReturnConfig {
	/**
	 * 统一返回结果（默认json格式）
	 */
	@RestControllerAdvice
	static class GlobalReusltResponseAdvice implements ResponseBodyAdvice<Object> {
		@Override
		public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
			return true;
		}
		
		@Override
		public Object beforeBodyWrite(Object t, MethodParameter methodParameter, MediaType mediaType,
		                              Class<? extends HttpMessageConverter<?>> aClass,
		                              ServerHttpRequest serverHttpRequest,
		                              ServerHttpResponse serverHttpResponse) {
			if (t instanceof GlobalResult) {
				return t.toString();
			}
			return new GlobalResult.Builder<>(t).build().toString();
		}
	}
	
	/**
	 * 统一异常处理（默认json格式）
	 */
	@RestControllerAdvice
	@Slf4j
	static class GlobalExceptionResponseHandler {
		/**
		 * 全局异常处理（默认json格式）
		 */
		@ExceptionHandler(Exception.class)
		public String handleException(HttpServletRequest request, HttpServletResponse response, final Exception e) {
			log.error(e.getMessage(), e);
			GlobalResult globalResult;
			
			//异常类型
			if (e instanceof GlobalException) {
				//业务异常
				globalResult = new GlobalResult.Builder<>(((GlobalException) e).getCode(),
				                                          ((GlobalException) e).getMsg()).build();
			} else {
				//系统异常
				globalResult = new GlobalResult.Builder<String>(String.valueOf(HttpStatus.SC_INTERNAL_SERVER_ERROR),
				                                                "系统内部错误!").build();
			}
			return globalResult.toString();
		}
	}
	
	/**
	 * 自定义异常,可自定义覆盖
	 * 包括非受查异常(RuntimeException),受查异常(IOExcepton)
	 * 注: spring 对于 RuntimeException 异常才会进行事务回滚
	 */
	class GlobalException extends RuntimeException implements Serializable {
		private final long serialVersionUID = 20191118140712100L;
		// 默认 UUID
		private String uuid;
		// 默认异常响应 500
		private String code = String.valueOf(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		// 默认异常信息
		private String msg = "系统内部错误!";
		
		public GlobalException() {
			new GlobalException(null, getCode(), getMsg());
		}
		
		public GlobalException(String uuid, String message) {
			super(message);
			setUuid(uuid);
			setMsg(message);
		}
		
		public GlobalException(String uuid, String message, Throwable cause) {
			super(message, cause);
			setUuid(uuid);
			setMsg(message);
		}
		
		public GlobalException(String uuid, String code, String message) {
			super(message);
			setUuid(uuid);
			setCode(code);
		}
		
		public GlobalException(String uuid, String code, String message, Throwable cause) {
			super(message, cause);
			setUuid(uuid);
			setCode(code);
			setMsg(message);
		}
		
		/**
		 * 获取 默认异常响应 500
		 *
		 * @return code 默认异常响应 500
		 */
		public String getCode() {
			return this.code;
		}
		
		/**
		 * 设置 默认异常响应 500
		 *
		 * @param code
		 * 		默认异常响应 500
		 */
		public void setCode(String code) {
			this.code = code;
		}
		
		/**
		 * 获取 默认异常信息
		 *
		 * @return msg 默认异常信息
		 */
		public String getMsg() {
			return this.msg;
		}
		
		/**
		 * 设置 默认异常信息
		 *
		 * @param msg
		 * 		默认异常信息
		 */
		public void setMsg(String msg) {
			this.msg = msg;
		}
		
		/**
		 * 获取 默认 UUID
		 *
		 * @return uuid 默认 UUID
		 */
		public String getUuid() {
			return this.uuid;
		}
		
		/**
		 * 设置 默认 UUID
		 *
		 * @param uuid
		 * 		默认 UUID
		 */
		public void setUuid(String uuid) {
			this.uuid = uuid;
		}
	}
	
	
	/**
	 * 全局返回结果,可根据格式自定义返回结果
	 */
	static class GlobalResult<T> {
		//必填参数
		private final String code;
		private final String msg;
		//可选参数
		private T data;
		
		@SuppressWarnings("unchecked")
		private GlobalResult(Builder builder) {
			this.code = builder.code;
			this.msg = builder.msg;
			this.data = (T) builder.data;
		}
		
		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			sb.append('{');
			sb.append("\"code\":\"")
					.append(code).append('\"');
			sb.append(",\"msg\":\"")
					.append(msg).append('\"');
			sb.append(",\"data\":")
					.append(data);
			sb.append('}');
			return sb.toString();
		}
		
		public static class Builder<T> {
			//必填参数
			private final String code;
			private final String msg;
			//可选参数
			private T data;
			
			public Builder(T t) {
				this.code = null;
				this.msg = null;
				this.data = t;
			}
			
			public Builder(String code, String msg) {
				this.code = code;
				this.msg = msg;
			}
			
			//builder模式可选参数
			public Builder data(T data) {
				this.data = data;
				return this;
			}
			
			public GlobalResult build() {
				return new GlobalResult(this);
			}
		}
	}
}
