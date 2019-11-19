package xyz.biandeshen.commonstests.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fjp
 * @Title: TestController2
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/4/813:49
 */

//@RestController
//@RequestMapping("test")
public class TestController2 {
	

		//@DecryptRSAParam(value = "hello,world!")
		@RequestMapping("/hello")
		public String testBusiness(){

			return "business";
		}
}
