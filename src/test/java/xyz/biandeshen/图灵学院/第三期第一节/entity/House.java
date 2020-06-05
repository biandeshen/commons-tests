package xyz.biandeshen.图灵学院.第三期第一节.entity;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @FileName: House
 * @Author: admin
 * @Date: 2020/5/13 16:42
 * @Description: 测试 通过接口实现bean的初始化以及销毁方法
 * History:
 * <author>          <time>          <version>
 * admin           2020/5/13           版本号
 */
@Component
public class House implements InitializingBean, DisposableBean {
	
	@Override
	public void destroy() throws Exception {
		System.out.println("this.getClass() = " + this.getClass() + " destory!");
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("this.getClass() = " + this.getClass() + " init!");
	}
}