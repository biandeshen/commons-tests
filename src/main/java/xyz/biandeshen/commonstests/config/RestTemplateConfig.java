package xyz.biandeshen.commonstests.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.google.common.base.Throwables;
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
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author fjp
 * @Title: RestTemplateConfig
 * @ProjectName common
 * @Description: RestTemplage注册类, 实现支持Http, Https请求接口
 * @date 2019/5/2314:54
 */
@Configuration
@SuppressWarnings("all")
public class RestTemplateConfig {
	/**
	 * 连接池的最大连接数默认为200
	 */
	@Value("${remote.maxTotalConnect:200}")
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
	 * 读取超时默认3s
	 */
	@Value("${remote.readTimeout:3000}")
	private int readTimeout;
	
	/**
	 * 连接池获取连接超时默认1s
	 */
	@Value("${remote.connectionRequestTimeout:1000}")
	private int connectionRequestTimeout;
	
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
		return getInstance(Charset.forName(charset).toString(), isOpenCustomerFastJsonConverter);
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
		RestTemplate restTemplate = new RestTemplate(this.createFactory());
		List<HttpMessageConverter<?>> converterList = restTemplate.getMessageConverters();
		//重新设置StringHttpMessageConverter字符集为UTF-8，解决中文乱码问题
		HttpMessageConverter<?> converterTarget = null;
		for (HttpMessageConverter<?> item : converterList) {
			if (StringHttpMessageConverter.class == item.getClass()) {
				converterTarget = item;
				break;
			}
		}
		if (null != converterTarget) {
			converterList.remove(converterTarget);
		}
		//默认使用UTF-8编码
		if (StringUtils.isEmpty(charset)) {
			converterList.add(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
		} else {
			converterList.add(1, new StringHttpMessageConverter(Charset.forName(charset)));
		}
		//加入FastJson转换器
		if (isOpenCustomerFastJsonConverter) {
			converterList.add(customerFastJsonHttpMessageConverter().getConverters().get(0));
		}
		return restTemplate;
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
				                              .setConnectionRequestTimeout(1000).build();
		return HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).setConnectionManager(connectionManager).build();
	}
	
	
	/**
	 * 引入Fastjson解析json，不使用默认的jackson
	 * 必须在pom.xml引入fastjson的jar包，并且版必须大于 1.2.10
	 *
	 * @return HttpMessageConverters
	 */
	private HttpMessageConverters customerFastJsonHttpMessageConverter() {
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
		fastJsonConfig.setCharset(Charset.forName("UTF-8"));
		//3、在convert中添加配置信息
		fastConverter.setFastJsonConfig(fastJsonConfig);
		//4、将convert添加到converters中
		return new HttpMessageConverters((HttpMessageConverter<?>) fastConverter);
	}
	
	
	// ASYNC
	@Description("AsyncRestTemplate 已过时，将用WebClient替代")
	@Deprecated
	public AsyncClientHttpRequestFactory asyncHttpRequestFactory() {
		return new HttpComponentsAsyncClientHttpRequestFactory(asyncHttpClient());
	}
	
	@Bean
	@Description("AsyncRestTemplate 已过时，将用WebClient替代")
	@Deprecated
	public AsyncRestTemplate asyncRestTemplate() {
		return new AsyncRestTemplate(asyncHttpRequestFactory(), restTemplate());
	}
	
	
	@Bean
	@Description("AsyncRestTemplate 已过时，将用WebClient替代")
	@Deprecated
	public CloseableHttpAsyncClient asyncHttpClient() {
		try {
			PoolingNHttpClientConnectionManager connectionManager =
					new PoolingNHttpClientConnectionManager(new DefaultConnectingIOReactor(IOReactorConfig.DEFAULT));
			connectionManager.setMaxTotal(maxTotalConnect);
			connectionManager.setDefaultMaxPerRoute(maxConnectPerRoute);
			RequestConfig config =
					RequestConfig.custom().setConnectTimeout(connectTimeout).setConnectionRequestTimeout(connectionRequestTimeout).setSocketTimeout(readTimeout).build();
			return HttpAsyncClientBuilder.create().setConnectionManager(connectionManager).setDefaultRequestConfig(config).build();
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}
	
	
	/**
	 * 自定义Factory扩展,重写以支持GET请求的带body支持
	 */
	public static final class HttpComponentsClientRestfulHttpRequestFactory extends
	                                                                        HttpComponentsClientHttpRequestFactory {
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


