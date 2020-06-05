package xyz.biandeshen.commonstests;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

//@EnableSecurity
@SpringBootApplication
public class CommonsTestsApplication {
	
	public static void main(String[] args) {
		// 添加hook thread，重写其run方法
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.out.println("this is hook demo...");
		}));
		SpringApplication.run(CommonsTestsApplication.class, args);
	}
	
	
	public static void destory() {
	
	}
}
