package xyz.biandeshen.图灵学院.第三期第一节.entity;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @FileName: CustomerBeanPostProcessor
 * @Author: admin
 * @Date: 2020/5/13 17:11
 * @Description: 自定义的BeanPostProcessor后置处理器，进行前置后置增强（或初始化及销毁）
 * History:
 * <author>          <time>          <version>
 * admin           2020/5/13           版本号
 */
@Component
public class CustomerBeanPostProcessor implements BeanPostProcessor {
	
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("CustomerBeanPostProcessor is working...beforeInitialization:" + beanName);
		return bean;
	}
	
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("CustomerBeanPostProcessor is working...afterInitialization:" + beanName);
		return bean;
	}
}