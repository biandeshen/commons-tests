package xyz.biandeshen.图灵学院.面试突击;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;

/**
 * @FileName: CustomerBeanFactoryPostProcessor
 * @Author: admin
 * @Date: 2020/5/13 22:48
 * @Description: 自定义BeanFactoryPostProcessor
 * History:
 * <author>          <time>          <version>
 * admin           2020/5/13           版本号
 */
public class CustomerBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
	
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		GenericBeanDefinition beanDefinition = (GenericBeanDefinition) beanFactory.getBeanDefinition();
		
		// 设置接口的类型为 类
		beanDefinition.setBeanClass();
		beanDefinition.getConstructorArgumentValues().addGenericArgumentValue();
		
	}
}