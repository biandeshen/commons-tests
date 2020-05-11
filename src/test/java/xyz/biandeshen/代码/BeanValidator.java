package xyz.biandeshen.代码;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zjs.edistorage.config.GlobalReturnConfig;
import org.apache.commons.collections.MapUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.*;

/**
 * @FileName: BeanValidator
 * @Author: admin
 * @Date: 2020/3/31 12:07
 * @Description: jsr校验工具
 * History:
 * <author>          <time>          <version>
 * admin           2020/3/31           版本号
 */
public class BeanValidator {
	
	private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
	
	/**
	 * 校验对象
	 * @param t 对象的泛型
	 * @param groups
	 * @param <T>
	 * @return
	 */
	public static <T> Map<String,String> validate(T t, Class... groups){
		Validator validator = validatorFactory.getValidator();
		Set validateResult = validator.validate(t, groups);
		if(validateResult.isEmpty()){
			return Collections.emptyMap();
		}else {
			LinkedHashMap errors = Maps.newLinkedHashMap();
			Iterator iterator = validateResult.iterator();
			while (iterator.hasNext()){
				ConstraintViolation violation = (ConstraintViolation) iterator.next();
				errors.put(violation.getPropertyPath().toString(),violation.getMessage());
			}
			return errors;
		}
	}
	
	/**
	 * 校验集合
	 * @param collection 集合
	 * @return
	 */
	public static Map<String,String> validateList(Collection<?> collection){
		Preconditions.checkNotNull(collection);
		Iterator iterator = collection.iterator();
		Map errors;
		do{
			if (!iterator.hasNext()) {
				return Collections.emptyMap();
			}
			Object object = iterator.next();
			errors = validate(object,new Class[0]);
		}while (errors.isEmpty()); // 如果errors为空则循环校验,一旦不为空   跳出循环
		return errors;
	}
	
	/**
	 * 通用方法，至少传入一个Object参数
	 * <p>校验参数之后不会处理只会将错误信息存储到map中
	 * @param first
	 * @param objects
	 */
	public static Map<String,String> validateObject(Object first,Object... objects){
		if(objects == null || objects.length == 0){
			return validate(first,new Class[0]);
		}else {
			return validateList(Lists.asList(first, objects));
		}
	}
	
	/**
	 * 校验参数是否合法
	 *  <p>当校验参数不合法时抛出异常让程序不能继续执行
	 * @param param
	 * @throws ParamException
	 */
	public static void check(Object param) throws RuntimeException {
		Map<String, String> map = validateObject(param);
		if(MapUtils.isNotEmpty(map)){
			throw new RuntimeException(map.toString());
		}
	}
}