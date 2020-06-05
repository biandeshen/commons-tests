package xyz.biandeshen.图灵学院.第三期第一节.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import xyz.biandeshen.图灵学院.第三期第一节.anno.CustomerConditon;
import xyz.biandeshen.图灵学院.第三期第一节.entity.*;

/**
 * @FileName: BeanConfig
 * @Author: admin
 * @Date: 2020/5/13 8:52
 * @Description: 注册bean
 * History:
 * <author>          <time>          <version>
 * admin           2020/5/13           版本号
 */
@Configuration
//@ComponentScan(value = {"com.example.demo.entity"})
//@ComponentScan(value = {"com.example.demo.entity"}, excludeFilters = {
//		@ComponentScan.Filter(type =FilterType.ANNOTATION, value = {Component.class})
//})
@ComponentScan(value = {"xyz.biandeshen.图灵学院.第三期第一节.entity", "xyz.biandeshen.图灵学院.第三期第一节.config"}
		//		includeFilters = {
		//		//@ComponentScan.Filter(type = FilterType.ANNOTATION, value = {Component.class}),
		//		// 自定义过滤 扫描添加类名结尾为Bean的类
		//		@ComponentScan.Filter(type = FilterType.CUSTOM, value = {CustomerFilterType.class})},
		//		useDefaultFilters = false
		//
)
@Import({CustomerFilterType.class, CustomerImportSelector.class, CustomerImprotBeanDefinitionRegistrar.class})
@PropertySource(value = {"classpath:person.yml"})//指定外部文件的配置
public class BeanConfig {
	
	//@Bean
	@Bean(value = {"person", "person1", "person2"}, initMethod = "init", destroyMethod = "destory")
	//@Lazy
	//@Scope("prototype")
	public Person person() {
		return new Person();
	}
	
	@Bean
	@Conditional(value = CustomerConditon.class)
	public TestBean testBean() {
		return new TestBean();
	}
	
	@Bean
	//@Primary
	//@Qualifier("")
	public CarFactoryBean carFactoryBean() {
		return new CarFactoryBean();
	}
}

