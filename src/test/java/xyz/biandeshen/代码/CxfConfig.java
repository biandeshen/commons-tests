package xyz.biandeshen.代码;

/**
 * @author fjp
 * @Title: CxfConfig
 * @ProjectName guangda
 * @Description: TODO
 * @date 2019/3/2117:06
 */

import com.cn.zjs.guangda.service.TransService;
import com.cn.zjs.guangda.service.impl.TransServiceImpl;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.xml.ws.Endpoint;

/**
 * @author Jerry
 * @ClassName:CxfConfig
 * @Description:cxf发布webservice配置
 * @date:2018年4月10日下午4:12:24
 */
@Configuration
public class CxfConfig {
	
	@Bean(name = Bus.DEFAULT_BUS_ID)
	public SpringBus springBus() {
		return new SpringBus();
	}
	
	@Bean
	public TransService wmsService() {
		return new TransServiceImpl();
	}
	
	/**
	 * 此方法作用是改变项目中服务名的前缀名，此处127.0.0.1或者localhost不能访问时，请使用ipconfig查看本机ip来访问
	 * 此方法被注释后:wsdl访问地址为http://127.0.0.1:8080/services/user?wsdl
	 * 去掉注释后：wsdl访问地址为：http://127.0.0.1:8080/soap/user?wsdl
	 *
	 * 与endpoint()配合使用
	 */
	@Bean
	public ServletRegistrationBean dispatcherServlet() {
		return new ServletRegistrationBean(new CXFServlet(), "/services/*");
	}
	
	/**
	 * 添加普通的controller处理,支持rest
	 *
	 * @return
	 */
	@Bean
	public ServletRegistrationBean dispatcherRestServlet() {
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		//替换成自己想买的controller包路径
		context.scan("com.cn.zjs.guangda.controller");
		DispatcherServlet disp = new DispatcherServlet(context);
		ServletRegistrationBean registrationBean = new ServletRegistrationBean(disp);
		registrationBean.setLoadOnStartup(1);
		//映射路径自定义,必须设置一个不重复的名称
		registrationBean.addUrlMappings("/rest/*");
		registrationBean.setName("rest");
		return registrationBean;
	}
	
	
	/**
	 * JAX-WS
	 * 站点服务
	 **/
	@Bean
	public Endpoint endpoint() {
		EndpointImpl endpoint = new EndpointImpl(springBus(), wmsService());
		endpoint.publish("/webService");
		return endpoint;
	}
	
}