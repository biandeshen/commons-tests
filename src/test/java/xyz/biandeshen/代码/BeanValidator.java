package com.zjs.dj.warehouse.standardorder.utils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zjs.dj.warehouse.standardorder.config.GlobalException;
import org.apache.commons.collections.MapUtils;

import javax.validation.*;
import java.lang.reflect.Field;
import java.time.Instant;
import java.util.*;

/**
 * @FileName: BeanValidator
 * @Author: admin
 * @Date: 2020/3/31 12:07
 * @Description: jsr303校验工具 https://www.cnblogs.com/bbgs-xc/p/10723630.html
 * History:
 * <author>          <time>          <version>
 * admin           2020/3/31           版本号
 */
@SuppressWarnings("ALL")
public class BeanValidator {
	private BeanValidator() {
	}
	
	private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
	
	/**
	 * 校验对象
	 *
	 * @param t
	 * 		对象的泛型
	 * @param groups
	 * @param <T>
	 *
	 * @return
	 */
	public static <T> Map<String, String> validate(T t, Class... groups) {
		Validator validator = validatorFactory.getValidator();
		Set validateResult = validator.validate(t, groups);
		if (Objects.isNull(validateResult) || validateResult.isEmpty()) {
			return Collections.emptyMap();
		} else {
			LinkedHashMap errors = Maps.newLinkedHashMap();
			Iterator iterator = validateResult.iterator();
			while (iterator.hasNext()) {
				ConstraintViolation violation = (ConstraintViolation) iterator.next();
				//errors.put(violation.getPropertyPath().toString(), violation.getMessage());
				//errors.put("错误序号: " + Instant.now().getNano() + " 错误内容: " + violation.getPropertyPath().toString() +
				//		           "=" + violation.getInvalidValue(), violation.getMessage());
				errors.put("\"错误内容\":" + violation.getPropertyPath().toString() + "=" + violation.getInvalidValue(),
				           violation.getMessage());
			}
			return errors;
		}
	}
	
	/**
	 * 校验集合
	 *
	 * @param collection
	 * 		集合
	 *
	 * @return
	 */
	public static Map<String, String> validateList(Collection<?> collection) {
		Preconditions.checkNotNull(collection);
		Iterator iterator = collection.iterator();
		Map errors = new HashMap(2);
		if (!iterator.hasNext()) {
			return Collections.emptyMap();
		}
		// 如果errors为空则循环校验,一旦不为空   跳出循环
		//while (errors.isEmpty()) {
		//	if (!iterator.hasNext()) {
		//		return Collections.emptyMap();
		//	}
		//	Object object = iterator.next();
		//	errors.putAll(validate(object, new Class[0]));
		//}
		while (iterator.hasNext()) {
			Object object = iterator.next();
			errors.putAll(validate(object, new Class[0]));
		}
		if (errors.isEmpty()) {
			return Collections.emptyMap();
		}
		return errors;
	}
	
	/**
	 * 通用方法，至少传入一个Object参数
	 * <p>校验参数之后不会处理只会将错误信息存储到map中
	 *
	 * @param first
	 * @param objects
	 */
	public static Map<String, String> validateObject(Object first, Object... objects) {
		if (objects == null || objects.length == 0) {
			return validate(first, new Class[0]);
		} else {
			return validateList(Lists.asList(first, objects));
		}
	}
	
	/**
	 * 得到属性值
	 *
	 * @param obj
	 */
	public static <T> Map<String, String> validateRecursion(T obj) {
		if (obj == null) {
			return Collections.emptyMap();
		}
		Map<String, String> errors = new HashMap<>(8);
		if (obj instanceof List) {
			errors.putAll(validateList((List) obj));
		} else {
			errors.putAll(validateObject(obj));
		}
		errors.putAll(BeanValidator.validate(obj));
		//得到class
		Class<?> cls = obj.getClass();
		//得到所有属性
		Field[] fields = cls.getDeclaredFields();
		//遍历
		for (Field field1 : fields) {
			boolean annotationPresent = field1.isAnnotationPresent(Valid.class);
			if (annotationPresent) {
				try {
					// 打开私有访问
					field1.setAccessible(true);
					// 获取对象
					Object o = field1.get(obj);
					// 递归调用此方法
					errors.putAll(validateRecursion(o));
					field1.setAccessible(false);
				} catch (IllegalAccessException e) {
					// do nothing
				}
			}
		}
		return errors;
	}
	
	/**
	 * 校验参数是否合法
	 * <p>当校验参数不合法时抛出异常让程序不能继续执行
	 *
	 * @param param
	 *
	 * @throws
	 */
	public static void check(String uuid, Object param) {
		Map<String, String> map = new MyHashMap<>();
		if (param instanceof List) {
			Map<String, String> stringStringMap = validateList((List) param);
			map.putAll(stringStringMap);
		} else {
			Map<String, String> stringStringMap = validateObject(param);
			map.putAll(stringStringMap);
		}
		if (MapUtils.isNotEmpty(map)) {
			// 抛出检验的异常信息
			throw new GlobalException(uuid, map.toString());
		}
	}
	
	public static void checkRecursion(String uuid, Object param) {
		Map<String, String> map = new MyHashMap<>();
		map.putAll(validateRecursion(param));
		if (MapUtils.isNotEmpty(map)) {
			// 抛出检验的异常信息
			throw new GlobalException(uuid, map.toString());
		}
	}
	
	/**
	 * 重写toString()方法的hashmap，重写校验输出格式
	 *
	 * @param <K>
	 * @param <V>
	 */
	static class MyHashMap<K, V> extends HashMap<K, V> {
		@Override
		public String toString() {
			Iterator<Entry<K, V>> i = entrySet().iterator();
			if (!i.hasNext()) {
				return "{}";
			}
			StringBuilder sb = new StringBuilder();
			sb.append(" ");
			sb.append("{");
			sb.append("\r\n");
			for (; ; ) {
				Entry<K, V> e = i.next();
				K key = e.getKey();
				V value = e.getValue();
				sb.append(key.toString());
				sb.append("--");
				sb.append(value.toString());
				sb.append("");
				if (!i.hasNext()) {
					return sb.append("}").toString();
				}
				sb.append(",").append("\r\n");
			}
		}
	}
}