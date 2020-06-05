package com.cn.zjs.guangda.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author fjp
 * @Title: RestTemplateConfig
 * @ProjectName common
 * @Description: RestTemplage注册类, 实现支持Http, Https请求接口
 * @date 2019/5/2314:54
 */
@Configuration
@EnableWebMvc
@SuppressWarnings("all")
public class RestTemplateConfig2 extends WebMvcConfigurerAdapter {
	/**
	 * 连接池的最大连接数默认为200
	 */
	@Value("${remote.maxTotalConnect:50}")
	private int maxTotalConnect;
	
	/**
	 * 单个主机的最大连接数
	 */
	@Value("${remote.maxConnectPerRoute:200}")
	private int maxConnectPerRoute;
	
	/**
	 * 连接超时默认1s
	 */
	@Value("${remote.connectTimeout:1000}")
	private int connectTimeout;
	
	/**
	 * 读取超时默认2.5s
	 */
	@Value("${remote.readTimeout:2500}")
	private int readTimeout;
	
	/**
	 * 连接池获取连接超时默认0.5s
	 */
	@Value("${remote.connectionRequestTimeout:500}")
	private int connectionRequestTimeout;
	
	// 启动的时候要注意，由于我们在服务中注入了RestTemplate，所以启动的时候需要实例化该类的一个实例
	@Autowired
	private RestTemplateBuilder builder;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	/**
	 * 获取RestTemplate实例
	 *
	 * @return RestTemplate
	 * <li>默认编码名称 UTF-8</li>
	 * <li>不开启自定义 的 FastJson Convert 处理返回的 Json</li>
	 */
	@Bean
	public RestTemplate restTemplate() {
		return restTemplate(StandardCharsets.UTF_8.toString(), false);
	}
	
	/**
	 * 获取 RestTemplate实例 根据传入编码名称 以及 是否开启自定义 的 FastJson Convert 处理返回的 Json
	 *
	 * @param charset
	 * 		编码名称
	 * @param isOpenCustomerFastJsonConverter
	 * 		是否开启自定义 的 FastJson Convert 处理返回的 Json
	 *
	 * @return RestTemplate
	 */
	public RestTemplate restTemplate(String charset, boolean isOpenCustomerFastJsonConverter) {
		return getInstance(charset, isOpenCustomerFastJsonConverter);
	}
	
	/**
	 * 获取RestTemplate实例 根据传入编码名称 以及 是否开启自定义 的 FastJson Convert 处理返回的 Json
	 *
	 * @param charset
	 * 		编码名称
	 * @param isOpenCustomerFastJsonConverter
	 * 		是否开启自定义 的 FastJson Convert 处理返回的 Json
	 *
	 * @return RestTemplate
	 */
	private RestTemplate getInstance(String charset, boolean isOpenCustomerFastJsonConverter) {
		// 使用RestTemplateBuilder来实例化RestTemplate对象，spring默认已经注入了RestTemplateBuilder实例
		RestTemplate restTemplate = builder.requestFactory(createFactory()).build();
		List<HttpMessageConverter<?>> converterList = restTemplate.getMessageConverters();
		//重新设置StringHttpMessageConverter字符集为UTF-8，解决中文乱码问题
		removeAndResetStringHttpMessageConverter(charset, converterList);
		//加入FastJson转换器
		if (isOpenCustomerFastJsonConverter) {
			converterList.add(customerFastJsonHttpMessageConverter());
		}
		//MappingJackson2HttpMessageConverter 修改支持的MediaType，并设置 ObjectMapper
		supportMultiMediaTypeAndSetObjectMapper(restTemplate, converterList);
		//添加默认的List<HttpMessageConverter<?>> 至restTemplate
		restTemplate.setMessageConverters(converterList);
		return restTemplate;
	}
	
	/**
	 * 引入Fastjson解析json，不使用默认的jackson
	 * 必须在pom.xml引入fastjson的jar包，并且版必须大于 1.2.10
	 *
	 * @return HttpMessageConverters
	 */
	private HttpMessageConverter customerFastJsonHttpMessageConverter() {
		//1、定义一个convert转换消息的对象
		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
		//2、添加fastjson的配置信息
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		// 自定义默认序列化内容
		SerializerFeature[] serializerFeatures = new SerializerFeature[]{
				//  输出key是包含双引号
				SerializerFeature.QuoteFieldNames,
				//  是否输出为null的字段,若为null 则显示该字段
				SerializerFeature.WriteMapNullValue,
				//  数值字段如果为null，则输出为0
				SerializerFeature.WriteNullNumberAsZero,
				//  List字段如果为null,输出为[],而非null
				SerializerFeature.WriteNullListAsEmpty,
				//  字符类型字段如果为null,输出为"",而非null
				SerializerFeature.WriteNullStringAsEmpty,
				//  Boolean字段如果为null,输出为false,而非null
				SerializerFeature.WriteNullBooleanAsFalse,
				//  Date的日期转换器
				SerializerFeature.WriteDateUseDateFormat,
				//  循环引用
				SerializerFeature.DisableCircularReferenceDetect,};
		fastJsonConfig.setSerializerFeatures(serializerFeatures);
		fastJsonConfig.setCharset(Charset.forName(StandardCharsets.UTF_8.toString()));
		//3、在convert中添加配置信息
		fastConverter.setFastJsonConfig(fastJsonConfig);
		//4、将convert添加到converters中
		fastConverter.setSupportedMediaTypes(new ArrayList() {{
			//add(MediaType.TEXT_PLAIN);
			//add(MediaType.APPLICATION_JSON);
			add(MediaType.APPLICATION_JSON_UTF8);
			add(MediaType.ALL);
		}});
		return fastConverter;
	}
	
	/**
	 * 设置 支持更多 MediaType 以及 ObjectMapper
	 *
	 * @param restTemplate
	 * @param converterList
	 *
	 * @return
	 */
	private void supportMultiMediaTypeAndSetObjectMapper(RestTemplate restTemplate,
	                                                     List<HttpMessageConverter<?>> converterList) {
		//MappingJackson2HttpMessageConverter 修改支持的MediaType
		HttpMessageConverter<?> converterTarget = null;
		for (HttpMessageConverter<?> item : converterList) {
			if (MappingJackson2HttpMessageConverter.class == item.getClass()) {
				converterTarget = item;
				break;
			}
		}
		if (converterTarget != null) {
			// 不为空先清除
			converterList.remove(converterTarget);
		}
		// 重新添加
		converterTarget = new MappingJackson2HttpMessageConverter();
		// 设置 objectMapper
		((MappingJackson2HttpMessageConverter) converterTarget).setObjectMapper(objectMapper);
		// 更多的MediaType
		MediaType[] mediaTypes = new MediaType[]{MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM,
				
				MediaType.TEXT_HTML, MediaType.TEXT_PLAIN, MediaType.TEXT_XML,
				//MediaType.APPLICATION_STREAM_JSON,
				MediaType.APPLICATION_ATOM_XML, MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON_UTF8
				, MediaType.APPLICATION_PDF,};
		// 支持更多的MediaType
		((MappingJackson2HttpMessageConverter) converterTarget).setSupportedMediaTypes(Arrays.asList(mediaTypes));
		//添加MappingJackson2HttpMessageConverter
		converterList.add(converterTarget);
	}
	
	/**
	 * 移除默认的StringHttpMessageConverter,设置编码格式 charset （默认utf-8）
	 *
	 * @param charset
	 * 		将设置的编码格式
	 * @param converterList
	 * 		restemplate中的所有转换类
	 */
	private void removeAndResetStringHttpMessageConverter(String charset,
	                                                      List<HttpMessageConverter<?>> converterList) {
		HttpMessageConverter<?> converterTarget = null;
		for (HttpMessageConverter<?> item : converterList) {
			if (StringHttpMessageConverter.class == item.getClass()) {
				converterTarget = item;
				break;
			}
		}
		// 移除默认编码格式
		if (null == converterTarget) {
			// 为空时默认添加 utf-8 编码格式
			converterList.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
		} else {
			converterList.remove(converterTarget);
			//设置默认使用UTF-8编码
			if (StringUtils.isEmpty(charset)) {
				converterList.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
			} else {
				converterList.add(new StringHttpMessageConverter(Charset.forName(charset)));
			}
		}
	}
	
	/**
	 * 创建HTTP客户端工厂
	 *
	 * @return http客户端工厂
	 */
	@Bean
	public ClientHttpRequestFactory createFactory() {
		if (this.maxTotalConnect <= 0) {
			SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
			factory.setConnectTimeout(this.connectTimeout);
			factory.setReadTimeout(this.readTimeout);
			return factory;
		}
		HttpComponentsClientRestfulHttpRequestFactory factory =
				new HttpComponentsClientRestfulHttpRequestFactory(this.createHttpClient());
		factory.setConnectTimeout(this.connectTimeout);
		factory.setReadTimeout(this.readTimeout);
		return factory;
	}
	
	/**
	 * Apache HttpClient
	 *
	 * @return httpClient
	 *
	 * @see [类、类#方法、类#成员]
	 */
	@Bean
	public HttpClient createHttpClient() {
		// 支持HTTP、HTTPS
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create().register("http"
				, PlainConnectionSocketFactory.getSocketFactory()).register("https",
		                                                                    SSLConnectionSocketFactory.getSocketFactory()).build();
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
		connectionManager.setMaxTotal(this.maxTotalConnect);
		connectionManager.setDefaultMaxPerRoute(this.maxConnectPerRoute);
		connectionManager.setValidateAfterInactivity(2000);
		// 服务器返回数据(response)的时间，超时抛出read timeout
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(this.readTimeout)
				                              // 连接上服务器(握手成功)的时间，超时抛出connect timeout
				                              .setConnectTimeout(this.connectTimeout)
				                              // 从连接池中获取连接的超时时间，超时抛出ConnectionPoolTimeoutException
				                              .setConnectionRequestTimeout(connectionRequestTimeout).build();
		return HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).setConnectionManager(connectionManager).build();
	}
	
	
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		// 响应结果乱码， 改为 默认 utf8 编码格式返回
		converters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
		//converters.add(customerFastJsonHttpMessageConverter());
		super.configureMessageConverters(converters);
	}
	
	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.favorPathExtension(false); //支持后缀匹配
	}
	
	
	/**
	 * 自定义Factory扩展,重写以支持GET请求的带body支持
	 */
	public static final class HttpComponentsClientRestfulHttpRequestFactory extends HttpComponentsClientHttpRequestFactory {
		@Override
		protected HttpUriRequest createHttpUriRequest(HttpMethod httpMethod, URI uri) {
			if (httpMethod == HttpMethod.GET) {
				return new HttpGetRequestWithEntity(uri);
			}
			return super.createHttpUriRequest(httpMethod, uri);
		}
		
		public HttpComponentsClientRestfulHttpRequestFactory() {
		}
		
		public HttpComponentsClientRestfulHttpRequestFactory(HttpClient httpClient) {
			super(httpClient);
		}
	}
	
	/**
	 * 继承并重写定义Http请求的基础抽象类,扩展以支持GET请求传递body
	 */
	public static final class HttpGetRequestWithEntity extends HttpEntityEnclosingRequestBase {
		HttpGetRequestWithEntity(final URI uri) {
			super.setURI(uri);
		}
		
		@Override
		public String getMethod() {
			return HttpMethod.GET.name();
		}
	}
	
}


