package xyz.biandeshen.图灵学院.面试突击;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @FileName: CustomerImportBeanDefinitionRegistrar
 * @Author: admin
 * @Date: 2020/5/13 22:35
 * @Description: 自定义的ImportBeanDefinitionRegistrar
 * History:
 * <author>          <time>          <version>
 * admin           2020/5/13           版本号
 */
public class CustomerImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
	
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
	
	}
}