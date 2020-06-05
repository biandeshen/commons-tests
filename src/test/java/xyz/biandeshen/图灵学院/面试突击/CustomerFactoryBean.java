package xyz.biandeshen.图灵学院.面试突击;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.lang.Nullable;

/**
 * @FileName: CustomerFactoryBean
 * @Author: admin
 * @Date: 2020/5/13 22:45
 * @Description: 自定义的FactoryBean
 * History:
 * <author>          <time>          <version>
 * admin           2020/5/13           版本号
 */
public class CustomerFactoryBean<T> implements FactoryBean<T> {
	private Class<T> targetClass;
	
	public CustomerFactoryBean(Class<T> targetClass) {
		this.targetClass = targetClass;
	}
	
	@Override
	public T getObject() throws Exception {
		return null;
	}
	
	@Override
	@Nullable
	public Class<?> getObjectType() {
		return targetClass;
	}
	
	@Override
	@Nullable
	public boolean isSingleton() {
		return true;
	}
}