package xyz.biandeshen.图灵学院.第三期第一节;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import xyz.biandeshen.图灵学院.第三期第一节.config.BeanConfig;

/**
 * @FileName: BeanMain
 * @Author: admin
 * @Date: 2020/5/13 8:57
 * @Description: 测试spring的注解的位置
 * History:
 * <author>          <time>          <version>
 * admin           2020/5/13           版本号
 */
public class BeanMain {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext actx = new AnnotationConfigApplicationContext();
		actx.register(BeanConfig.class);
		actx.refresh();
		for (String beanDefinitionName : actx.getBeanDefinitionNames()) {
			System.out.println("beanDefinitionName = " + beanDefinitionName);
		}
		//Iterator<String> beanNamesIterator = actx.getBeanFactory().getBeanNamesIterator();
		//while (beanNamesIterator.hasNext()) {
		//	System.out.println("beanName = " + beanNamesIterator.next());
		//}
		
		System.out.println(actx.getBean("person"));
		System.out.println(actx.getBean("person1"));
		System.out.println(actx.getBean("person2"));
		System.out.println(actx.getBean("testBean"));
		System.out.println(actx.getBean("carFactoryBean"));
		System.out.println(actx.getBean("&carFactoryBean"));
		System.out.println(actx.getBean("&carFactoryBean"));
		actx.close();
	}
	
}