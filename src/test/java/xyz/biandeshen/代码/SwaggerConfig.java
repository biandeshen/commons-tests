package com.zjs.edistorage.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author fjp
 * @Title: SwaggerConfig
 * @ProjectName dj-fjp-edi-storage
 * @Description: TODO
 * @date 2019/11/2616:50
 */
//标记配置类
@Configuration
//测试环境开启在线接口文档
@Profile("dev")
@EnableSwagger2
public class SwaggerConfig {
	/**
	 * 添加摘要信息(Docket)
	 * * 创建API应用
	 * * apiInfo() 增加API相关信息
	 * * 通过select()函数返回一个ApiSelectorBuilder实例,用来控制哪些接口暴露给Swagger来展现，
	 * * 本例采用指定扫描的包路径来定义指定要建立API的目录。
	 * <p>
	 * 作者：LittleJessy
	 * 链接：https://www.jianshu.com/p/4fdac2a10c79
	 * 来源：简书
	 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
	 */
	@Bean
	public Docket controllerApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select().apis(RequestHandlerSelectors.basePackage("com.zjs.edistorage.controller")).paths(PathSelectors.any()).build();
	}
	
	/**
	 * 创建该API的基本信息（这些基本信息会展现在文档页面中）
	 * 访问地址：http://项目实际地址/swagger-ui.html
	 *
	 * @return
	 */
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("宅急送对接仓储接口文档").description("对接仓储接口的接口地址及参数").contact(new Contact("fjp", "",
		                                                                                                   "fanjiangpan@gmail.com")).version("版本号: 1.0.0").build();
	}
	
	@Configuration
	@EnableWebMvc
	@Profile("dev")
			//WebMvcConfigurerAdapter || WebMvcConfigurationSupport 异常时切换实现
	class WebMVCConfig extends WebMvcConfigurerAdapter implements WebMvcConfigurer {
		
		private CorsConfiguration buildConfig() {
			CorsConfiguration corsConfiguration = new CorsConfiguration();
			corsConfiguration.addAllowedOrigin("*");
			corsConfiguration.setAllowCredentials(true);
			corsConfiguration.addAllowedHeader("*");
			corsConfiguration.addAllowedMethod("*");
			
			return corsConfiguration;
		}
		
		@Bean
		public CorsFilter corsFilter() {
			UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
			source.registerCorsConfiguration("/**", buildConfig());
			return new CorsFilter(source);
		}
		
		@Override
		public void addResourceHandlers(ResourceHandlerRegistry registry) {
			registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
			registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
			registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/META-INF/resources/favicon" +
					                                                                 ".ico");
		}
		
	}
}
