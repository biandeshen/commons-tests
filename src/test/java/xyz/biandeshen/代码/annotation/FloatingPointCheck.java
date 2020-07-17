package xyz.biandeshen.commonstests.anno;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @FileName: FloatingPointCheck
 * @Author: fjp
 * @Date: 2020/6/10 11:05
 * @Description: JSR303自定义格式校验注解, 只允许使用在数值类型或字符类型的字段上 检验非负浮点数
 * History:
 * <author>          <time>          <version>
 * admin            2020/6/10          版本号
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {BigDecimalValidator.class})
public @interface FloatingPointCheck {
	// true: 成员变量是否必填，并进行格式校验 false: 成员变量允许为空
	boolean required() default true;
	
	// true: 默认  成员变量允许含 0， false: 成员变量不包含 0
	boolean includeZero() default true;
	
	// 小数值位数 默认保留两位小数
	int scale() default 2;
	
	// 默认最大值 空 不限制
	String max() default "";
	
	// 默认最小值 空 不限制
	String min() default "";
	
	// 提示信息
	String message() default "数值不在允许的取值范围内";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
}