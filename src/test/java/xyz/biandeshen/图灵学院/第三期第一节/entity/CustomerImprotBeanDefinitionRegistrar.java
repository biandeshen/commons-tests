package xyz.biandeshen.图灵学院.第三期第一节.entity;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @FileName: CustomerImprotBeanDefinitionRegistrar
 * @Author: admin
 * @Date: 2020/5/13 14:39
 * @Description: 测试 通过 ImportBeanDefinitionRegistrar
 * History:
 * <author>          <time>          <version>
 * admin           2020/5/13           版本号
 */
public class CustomerImprotBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
	
	@Override
	public void registerBeanDefinitions(AnnotationMetadata annotationMetadata,
	                                    BeanDefinitionRegistry beanDefinitionRegistry) {
		
		BeanDefinition beanDefinition = new RootBeanDefinition(CustomerImportSelector.class);
		beanDefinitionRegistry.registerBeanDefinition("test", beanDefinition);
	}
}