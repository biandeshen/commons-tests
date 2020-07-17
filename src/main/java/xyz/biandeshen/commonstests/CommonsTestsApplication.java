package xyz.biandeshen.commonstests;

import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.DocsConfig;
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
		apiGenerate();
	}
	
	
	public static void apiGenerate() {
		DocsConfig config = new DocsConfig();
		config.setProjectPath("D:\\WorkSpace\\commons-tests"); // root project path
		config.setProjectName("commons-tests"); // project name
		config.setApiVersion("V1.0");       // api version
		config.setDocsPath("D:\\WorkSpace\\commons-tests"); // api docs target path
		config.setAutoGenerate(Boolean.TRUE);  // auto generate
		Docs.buildHtmlDocs(config); // execute to generate
	}
	
	public static void destory() {
	
	}
}
