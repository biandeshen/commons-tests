package xyz.biandeshen.图灵学院.第三期第一节.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class Person {
	//普通方式
	@Value("注解")
	String name;
	//SPEL方式
	@Value("#{20-2}")
	String age;
	//读取外部配置
	@Value("${sex}")
	String sex;
	
	private void init() {
		System.out.println("this.getClass() = " + this.getClass() + " init!");
	}
	
	private void destory() {
		System.out.println("this.getClass() = " + this.getClass() + " destory!");
	}
}