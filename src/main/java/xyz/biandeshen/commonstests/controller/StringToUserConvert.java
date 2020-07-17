package xyz.biandeshen.commonstests.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * @FileName: StringToUserConvert
 * @Author: fjp
 * @Date: 2020/6/16 15:30
 * @Description: 自定义字符串转User对象转换器
 * History:
 * <author>          <time>          <version>
 * fjp           2020/6/16           版本号
 */
//@EnableWebMvc
//@Configuration
public class StringToUserConvert extends WebMvcConfigurationSupport {
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		List<HttpMessageConverter<?>> converterList = converters;
		converterList.add((HttpMessageConverter<?>) new CustomerStringToUserConverter());
		super.configureMessageConverters(converterList);
		
	}
}
// 自定义字符串转User对象转换类
class CustomerStringToUserConverter implements Converter<String,User>{
	@SneakyThrows
	@Override
	public User convert(String source) {
		// 反序列化为一个对象
		return new ObjectMapper().readValue(source,User.class);
	}
}