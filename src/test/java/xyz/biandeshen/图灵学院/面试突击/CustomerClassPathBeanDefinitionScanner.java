package xyz.biandeshen.图灵学院.面试突击;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import java.util.Set;

/**
 * @FileName: CustomerClassPathBeanDefinitionScanner
 * @Author: admin
 * @Date: 2020/5/13 22:55
 * @Description: 自定义的ClassPathBeanDefinitionScanner
 * History:
 * <author>          <time>          <version>
 * admin           2020/5/13           版本号
 */
public class CustomerClassPathBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {
	
	public CustomerClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
		super(registry);
	}
	
	@Override
	protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
		Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
		if (beanDefinitions.isEmpty()) {
			//
			//logger.error();
		} else {
			processBeanDefinitions(beanDefinitions);
		}
		
	}
	
	private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
		GenericBeanDefinition definition;
		for (BeanDefinitionHolder beanDefinition : beanDefinitions) {
			// 获取bean定义
			definition = (GenericBeanDefinition) beanDefinition.getBeanDefinition();
			// 获取bean定义的名称
			String beanClassName = definition.getBeanClassName();
			
			definition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName);
			definition.setBeanClass(this.);
			
		}
	}
	
	@Override
	protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
		return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
	}
}