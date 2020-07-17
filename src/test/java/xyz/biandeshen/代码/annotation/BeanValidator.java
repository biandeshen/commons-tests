package xyz.biandeshen.commonstests.anno;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.MapUtils;
import xyz.biandeshen.commonstests.config.GlobalException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
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
				errors.put("错误序号: " + Instant.now().getNano() + " 错误内容: " + violation.getPropertyPath().toString() +
						           "=" + violation.getInvalidValue(), violation.getMessage());
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
			sb.append('{');
			for (; ; ) {
				Entry<K, V> e = i.next();
				K key = e.getKey();
				V value = e.getValue();
				sb.append(key.toString());
				sb.append("->");
				sb.append(value.toString()).append(" ");
				if (!i.hasNext()) {
					return sb.append('}').toString();
				}
				sb.append(',').append(' ');
			}
		}
	}
}