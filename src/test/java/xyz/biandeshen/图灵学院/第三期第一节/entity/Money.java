package xyz.biandeshen.图灵学院.第三期第一节.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @FileName: Money
 * @Author: admin
 * @Date: 2020/5/13 16:58
 * @Description: 测试通过注解对bean进行初始化
 * History:
 * <author>          <time>          <version>
 * admin           2020/5/13           版本号
 */
@Data
@Component
public class Money {
	@PreDestroy
	public void destroy() throws Exception {
		System.out.println("this.getClass() = " + this.getClass() + " destory!");
	}
	
	@PostConstruct
	public void init() throws Exception {
		System.out.println("this.getClass() = " + this.getClass() + " init!");
	}
}