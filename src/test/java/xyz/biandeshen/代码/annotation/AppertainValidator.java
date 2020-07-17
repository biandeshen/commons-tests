package xyz.biandeshen.commonstests.anno;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @FileName: AppertainValidater
 * @Author: admin
 * @Date: 2020/4/13 21:26
 * @Description: 自定义注解的从属关系校验器
 * History:
 * <author>          <time>          <version>
 * admin           2020/4/13           版本号
 */
@SuppressWarnings("ALL")
public class AppertainValidator implements ConstraintValidator<Appertain, Object> {
	/*
	 * 第一个参数是自定义注解的名字
	 * 第二个参数是注解修饰字段的类型
	 */
	
	/**
	 * 默认不允许为空，需要校验
	 */
	private boolean required = true;
	
	/**
	 * 默认参数集合为空，直接通过校验
	 */
	private String[] values = {};
	
	@Override
	public void initialize(Appertain appertain) {
		required = appertain.required();
		values = appertain.values();
	}
	
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
		CharSequence charSequence;
		if (value == null) {
			return !required;
		}
		
		if (value instanceof CharSequence) {
			charSequence = (CharSequence) value;
		} else if (value instanceof Number) {
			charSequence = value.toString();
		} else {
			return false;
		}
		if (required) {
			if (StringUtils.isNotEmpty(charSequence)) {
				return StringUtils.containsAny(charSequence, values);
			} else {
				return false;
			}
		} else {
			if (charSequence == null || StringUtils.isEmpty(charSequence) || values.length == 0) {
				return true;
			} else {
				return StringUtils.containsAny(charSequence, values);
			}
		}
	}
}