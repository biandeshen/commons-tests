package xyz.biandeshen.图灵学院.面试突击;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author fjp
 * @Title: EnableCustomerMybatis
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2020/5/1323:00
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Import({CustomerImportBeanDefinitionRegistrar.class})
public @interface EnableCustomerMybatis {

}
