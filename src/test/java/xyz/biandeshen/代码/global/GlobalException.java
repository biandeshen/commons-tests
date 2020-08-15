package com.zjs.dj.warehouse.standardorder.config;

import javax.xml.bind.JAXBException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;

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
	private static final long serialVersionUID = 20200813161035413L;
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
	private final String message;
	/**
	 * 默认异常数据
	 */
	private final Object obj;
	
	public GlobalException() {
		super();
		this.uuid = null;
		this.code = null;
		this.message = null;
		this.obj = null;
	}
	
	public GlobalException(GlobalResult<Object> globalResult) {
		this(null, globalResult);
	}
	
	public GlobalException(String uuid, GlobalResult<Object> globalResult) {
		super();
		this.uuid = uuid;
		this.code = globalResult.getCode();
		this.message = globalResult.getMsg();
		this.obj = globalResult.getData();
	}
	
	public GlobalException(String uuid, String message) {
		super(message);
		this.uuid = uuid;
		this.code = null;
		this.message = message;
		this.obj = null;
	}
	
	public GlobalException(String uuid, String message, Throwable cause) {
		super(message, cause);
		this.uuid = uuid;
		this.code = null;
		this.message = message;
		this.obj = null;
	}
	
	public GlobalException(String uuid, String code, String message) {
		super(message);
		this.uuid = uuid;
		this.code = code;
		this.message = message;
		this.obj = null;
	}
	
	public GlobalException(String uuid, String code, String message, Throwable cause) {
		super(message, cause);
		this.uuid = uuid;
		this.code = code;
		this.message = message;
		this.obj = null;
	}
	
	public GlobalException(String uuid, String code, String message, Object obj) {
		super(message);
		this.uuid = uuid;
		this.code = code;
		this.message = message;
		this.obj = obj;
	}
	
	public GlobalException(String uuid, String code, String message, Object obj, Throwable cause) {
		super(message, cause);
		this.uuid = uuid;
		this.code = code;
		this.message = message;
		this.obj = obj;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getMsg() {
		return message;
	}
	
	public Object getObj() {
		return obj;
	}
	
	public static void main(String[] args) throws JAXBException, UnsupportedEncodingException {
		GlobalResult globalResult = new GlobalResult.Builder<String>("0", "test").flag("success").data("test").build();
		System.out.println("build.toString() = " + globalResult.toString());
		System.out.println("build.toXMLString() = " + globalResult.toXMLString());
	}
}

