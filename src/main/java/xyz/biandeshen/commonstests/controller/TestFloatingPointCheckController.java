package xyz.biandeshen.commonstests.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import xyz.biandeshen.commonstests.anno.BeanValidator;
import xyz.biandeshen.commonstests.util.common.JSONUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @FileName: TestFloatingPointCheckController
 * @Author: fjp
 * @Date: 2020/6/11 9:20
 * @Description: 测试数值校验注解
 * History:
 * <author>          <time>          <version>
 * fjp           2020/6/11           版本号
 */
@RestController
public class TestFloatingPointCheckController {
	
	//@PostMapping(value = "/insurancePolicy"/*, produces = {MediaType.TEXT_PLAIN_VALUE, MediaType
	//.APPLICATION_JSON_VALUE,
	//		MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.ALL_VALUE}*/)
	//public String test(@RequestParam String insurancePolicy) {
	//	InsurancePolicy insurancePolicy1 = JSONUtils.parseJsonToBeanGsonImpl(insurancePolicy, InsurancePolicy.class);
	//	System.out.println("insurancePolicy.toString() = " + insurancePolicy1);
	//	BeanValidator.check(null, insurancePolicy1);
	//	return insurancePolicy1.toString();
	//}
	
	@PostMapping(value = "/insurancePolicy")
	public String test(@RequestParam String insurancePolicy) {
		return test2(insurancePolicy).toString();
	}
	
	@PostMapping(value = "/insurancePolicy2")
	public InsurancePolicy test2(@RequestParam String insurancePolicy) {
		HttpServletRequest request =
				((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		System.out.println("request.getContentType() = " + request.getContentType());
		InsurancePolicy insurancePolicy1 = JSONUtils.parseJsonToBeanGsonImpl(insurancePolicy, InsurancePolicy.class);
		System.out.println("insurancePolicy.toString() = " + insurancePolicy1);
		BeanValidator.check(null, insurancePolicy1);
		return insurancePolicy1;
	}
}

