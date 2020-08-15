package com.zjs.dj.warehouse.standardorder.config;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zjs.dj.warehouse.standardorder.utils.common.JsonUtils;
import com.zjs.dj.warehouse.standardorder.utils.common.XmlUtils;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.UnsupportedEncodingException;

/**
 * @author fjp
 * @Title: GlobalResult
 * @Description: 统一响应结果 全局返回结果,可根据格式自定义返回结果
 * @date 2020/8/14 17:54
 */
@JsonSerialize
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response")
public class GlobalResult<T> {
	/**
	 * 必填参数
	 */
	private final String code;
	private final String message;
	/**
	 * 可选参数
	 */
	private String flag;
	private T data;
	
	private GlobalResult(Builder<T> builder) {
		this.code = builder.code;
		this.message = builder.msg;
		this.flag = builder.flag;
		this.data = builder.data;
	}
	
	/**
	 * 用以XML的序列化反序列化
	 */
	private GlobalResult() {
		this.code = null;
		this.message = null;
	}
	
	@Override
	public String toString() {
		return JsonUtils.beanToJsonGsonImpl(this);
	}
	
	public String toXMLString() throws JAXBException, UnsupportedEncodingException {
		return XmlUtils.marshal(this);
	}
	
	public GlobalResult<Object> convert(GlobalException globalException) {
		return new Builder<>(globalException.getCode(), globalException.getMsg()).data(globalException.getObj()).build();
	}
	
	public T getData() {
		return this.data;
	}
	
	public void setData(T data) {
		this.data = data;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getMsg() {
		return message;
	}
	
	public String getFlag() {
		return flag;
	}
	
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	public static class Builder<T> {
		//必填参数
		private final String code;
		private final String msg;
		//可选参数
		private String flag;
		private T data;
		
		public Builder(String code, String msg) {
			this.code = code;
			this.msg = msg;
		}
		
		//builder模式可选参数
		public Builder flag(String flag) {
			this.flag = flag;
			return this;
		}
		
		public Builder data(T data) {
			this.data = data;
			return this;
		}
		
		public GlobalResult<T> build() {
			return new GlobalResult<>(this);
		}
	}
}