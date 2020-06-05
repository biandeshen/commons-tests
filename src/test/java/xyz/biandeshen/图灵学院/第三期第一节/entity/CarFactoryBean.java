package xyz.biandeshen.图灵学院.第三期第一节.entity;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * @FileName: CarFactoryBean
 * @Author: admin
 * @Date: 2020/5/13 15:34
 * @Description: 通过实现 FactoryBean 接口来实现注册组件
 * History:
 * <author>          <time>          <version>
 * admin           2020/5/13           版本号
 */
public class CarFactoryBean implements FactoryBean<Car> {
	
	@Override
	public Car getObject() throws Exception {
		return new Car();
	}
	
	@Override
	public Class<?> getObjectType() {
		return Car.class;
	}
	
	@Override
	public boolean isSingleton() {
		return true;
	}
}

class Car {

}