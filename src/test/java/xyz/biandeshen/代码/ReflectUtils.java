package xyz.biandeshen.代码;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author fjp
 * @Title: ReflectUtils
 * @ProjectName icp
 * @Description: 反射工具类
 * @date 2018/11/29:55
 */
@Component
public class ReflectUtils {
	private static final Logger logger = LoggerFactory.getLogger(ReflectUtils.class);
	
	/**
	 * 得到属性值
	 *
	 * @param obj
	 */
	public <T> Map<String, Object> readAttributeValue(T obj) {
		Map<String, Object> objectMap = new Hashtable<>();
		//得到class
		Class cls = obj.getClass();
		//得到所有属性
		Field[] fields = cls.getDeclaredFields();
		//遍历
		for (Field field1 : fields) {
			try {
				//打开私有访问
				field1.setAccessible(true);
				//获取属性
				String name = field1.getName();
				//todo 获取属性类型 并 对通过类型对属性值进行转换 如时间 -》 时间戳
				//获取属性值
				if (!StringUtils.isEmpty(field1.get(obj))) {
					Object value = field1.get(obj);
					//一个个赋值
					objectMap.put(name, value);
				}
			} catch (IllegalAccessException e) {
				logger.error("设置{}对象属性失败！ 错误原因：{}", obj + field1.getName(), e);
			}
		}
		return objectMap;
	}
}
