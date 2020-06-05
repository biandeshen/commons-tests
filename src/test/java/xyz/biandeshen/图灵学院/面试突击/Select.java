package xyz.biandeshen.图灵学院.面试突击;

import java.lang.annotation.*;

/**
 * @author fjp
 * @Title: Select
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2020/5/1323:28
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Select {
	String value();
}
