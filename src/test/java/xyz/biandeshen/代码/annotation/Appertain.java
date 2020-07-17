package com.zjs.edistorage.anno;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @FileName: 属于
 * @Author: admin
 * @Date: 2020/4/13 21:09
 * @Description: JSR303自定义格式校验注解, 只允许使用在数值类型或字符类型的字段上
 * History:
 * <author>          <time>          <version>
 * admin           2020/4/13           版本号
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR,
		ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {AppertainValidator.class})
public @interface Appertain {
	// true: 参数需要存在，并进行格式校验 false: 允许为空
	boolean required() default true;
	
	// 参数集合,将要校验参数是否属于此集合
	String[] values() default {};
	
	// 提示信息
	String message() default "参数不在允许的范围内";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
}

//errors.put("错误序号: " + Instant.now().getNano() + " 错误内容: " + violation.getPropertyPath().toString(),
//		violation.getMessage());