package com.zjs.edistorage.config;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjs.edistorage.utils.common.JsonUtils;

import java.io.Serializable;

/**
 * @author fjp
 * @Title: GlobalException
 * @Description: 自定义异常, 可自定义覆盖
 * 包括非受查异常(RuntimeException),受查异常(IOExcepton)
 * 注: spring 对于 RuntimeException 异常才会进行事务回滚
 * @date 2020/5/27 10:50
 */
@SuppressWarnings("ALL")
public class GlobalException extends RuntimeException implements Serializable {
	private static final long serialVersionUID = 20191118140712100L;
	/**
	 * 默认 UUID
	 */
	private final String uuid;
	/**
	 * 默认异常响应 500
	 */
	private final String code;
	/**
	 * 默认异常信息
	 */
	private final String msg;
	/**
	 * 默认异常数据
	 */
	private final Object obj;
	
	public GlobalException() {
		super();
		this.uuid = null;
		this.code = null;
		this.msg = null;
		this.obj = null;
	}
	
	public GlobalException(String uuid, String message) {
		super(message);
		this.uuid = uuid;
		this.code = null;
		this.msg = message;
		this.obj = null;
	}
	
	public GlobalException(String uuid, String message, Throwable cause) {
		super(message, cause);
		this.uuid = uuid;
		this.code = null;
		this.msg = message;
		this.obj = null;
	}
	
	public GlobalException(String uuid, String code, String message) {
		super(message);
		this.uuid = uuid;
		this.code = code;
		this.msg = message;
		this.obj = null;
	}
	
	public GlobalException(String uuid, String code, String message, Throwable cause) {
		super(message, cause);
		this.uuid = uuid;
		this.code = code;
		this.msg = message;
		this.obj = null;
	}
	
	public GlobalException(String uuid, String code, String message, Object obj) {
		super(message);
		this.uuid = uuid;
		this.code = code;
		this.msg = message;
		this.obj = obj;
	}
	
	public GlobalException(String uuid, String code, String message, Object obj, Throwable cause) {
		super(message, cause);
		this.uuid = uuid;
		this.code = code;
		this.msg = message;
		this.obj = obj;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public Object getObj() {
		return obj;
	}
	
	/**
	 * 全局返回结果,可根据格式自定义返回结果
	 */
	@JsonSerialize
	public static class GlobalResult<T> implements Serializable {
		//必填参数
		private final String code;
		private final String message;
		//可选参数
		private T data;
		
		@SuppressWarnings("unchecked")
		private GlobalResult(Builder<T> builder) {
			this.code = builder.code;
			this.message = builder.msg;
			this.data = builder.data;
		}
		
		@Override
		public String toString() {
			return JsonUtils.beanToJsonGsonImpl(this);
		}
		
		/**
		 * 获取 可选参数
		 *
		 * @return data 可选参数
		 */
		public T getData() {
			return this.data;
		}
		
		/**
		 * Gets the value of code.
		 *
		 * @return the value of code
		 */
		public String getCode() {
			return code;
		}
		
		/**
		 * Gets the value of msg.
		 *
		 * @return the value of msg
		 */
		public String getMsg() {
			return message;
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