package xyz.biandeshen.commonstests.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Function;
import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.validation.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

/**
 * @FileName: TestUserController
 * @Author: fjp
 * @Date: 2020/6/16 14:56
 * @Description: 测试一个User对象的RequestParam注解
 * History:
 * <author>          <time>          <version>
 * fjp           2020/6/16           版本号
 */

@RestController
public class TestUserController {
	
	//@PostMapping("/user")
	//public String testUser(String name, String age) {
	//	return new User(name, age).toString();
	//}
	//
	//@PostMapping("/user1")
	//public String testUser1(@RequestParam("name") String name, @RequestParam("name") String age) {
	//	return new User(name, age).toString();
	//}
	//
	//@PostMapping("/user2")
	//public String testUser2(@RequestParam("name1") String name, @RequestParam("age1") String age) {
	//	return new User(name, age).toString();
	//}
	////@PostMapping("/user1")
	////public String testUser1(@RequestParam("user1") String user){
	////	HttpServletRequest request =
	////			((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	////	for (String[] value : request.getParameterMap().values()) {
	////		try {
	////			System.out.println("value = " + new ObjectMapper().writeValueAsString(value));
	////		} catch (JsonProcessingException e) {
	////			e.printStackTrace();
	////		}
	////	}
	////	return user;
	////}
	//
	////@PostMapping("/user2")
	////public User testUser2(String user) {
	////	// 反序列化为一个对象
	////	try {
	////		return new ObjectMapper().readValue(user, User.class);
	////	} catch (IOException e) {
	////		throw new IllegalArgumentException("不是一个合法的请求参数！");
	////	}
	////}
	//
	////@PostMapping("/user3")
	////@Deprecated
	////public User testUser3(User user) {
	////	return user;
	////}
	//
	//@PostMapping("/user4")
	//public String testUser4(User user) {
	//	return user.toString();
	//}
	//
	////@PostMapping("/user5")
	////public String testUser5(@RequestParam("user") User user) {
	////	return user.toString();
	////}
	//
	////@PostMapping("/user6")
	////@Deprecated
	////public User testUser6(@RequestParam("user") User user) {
	////	return user;
	////}
	//
	//@PostMapping("/user7")
	//public String testUser7(Info info) {
	//	System.out.println("info.toString() = " + info.toString());
	//	return info.toString();
	//}
	//
	//@PostMapping("/user8")
	//public String testUser8(User user, Info info) {
	//	System.out.println("user.toString() = " + user.toString());
	//	return info.toString();
	//}
	//
	////@PostMapping("/user8")
	////@Deprecated
	////public User testUser8(@ModelAttribute User user){
	////	return user;
	////}
	//
	//@PostMapping("/user9")
	//public String testUser9(UserInfo userInfo) {
	//	return userInfo.toString();
	//}
	
	
	@PostMapping("/user")
	public String testUser(String name, String age) {
		return new User(name, age).toString();
	}
	
	@PostMapping("/user1")
	public String testUser1(@RequestParam("name") String name, @RequestParam("name") String age) {
		return new User(name, age).toString();
	}
	
	@PostMapping("/user2")
	public String testUser2(@RequestParam("name1") String name, @RequestParam("age1") String age) {
		return new User(name, age).toString();
	}
	
	@PostMapping("/user3")
	public String testUser3(User user) {
		return user.toString();
	}
	
	@PostMapping("/user4")
	public String testUser4(@Valid Info info) {
		return info.toString();
	}
	
	@PostMapping("/user5")
	public String testUser5(User user, Info info) {
		return new UserInfo(user, info).toString();
	}
	
	//@PostMapping("/user51")
	//public String testUser51(@RequestParam("name") String name, @RequestParam("name") String age, @RequestParam(
	//		"profile") String profile) {
	//	return new UserInfo(new User(name, age), new Info(profile)).toString();
	//}
	
	@PostMapping("/user6")
	public String testUser6(UserInfo userInfo) {
		return userInfo.toString();
	}
	
	//@PostMapping("/user61")
	//public String testUser62(UserInfo userInfo, User user, Info info) {
	//	System.out.println("user = " + user);
	//	System.out.println("info = " + info);
	//	System.out.println("userInfo = " + userInfo);
	//	return userInfo.toString();
	//}
	
	@PostMapping("/user7")
	public String testUser7(@RequestParam("userInfo") @Valid UserInfo userInfo) {
		return userInfo.toString();
	}
}

class User {
	public User() {
	}
	
	public User(String name, String age) {
		this.name = name;
		this.age = age;
	}
	
	private String name;
	
	private String age;
	
	@Override
	public String toString() {
		final StringBuilder sb;
		sb = new StringBuilder();
		sb.append('{');
		sb.append("\"name\":\"").append(name).append('\"');
		sb.append(",\"age\":\"").append(age).append('\"');
		sb.append('}');
		return sb.toString();
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAge() {
		return this.age;
	}
	
	public void setAge(String age) {
		this.age = age;
	}
}

class Info {
	public Info() {
	}
	
	public Info(String profile) {
		this.profile = profile;
	}
	
	@NotBlank
	private String profile;
	
	
	public String getProfile() {
		return profile;
	}
	
	public void setProfile(String profile) {
		this.profile = profile;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb;
		sb = new StringBuilder();
		sb.append('{');
		sb.append("\"profile\":\"").append(profile).append('\"');
		sb.append('}');
		return sb.toString();
	}
}

class UserInfo {
	private User user;
	
	@NotNull
	private Info info;
	
	public UserInfo() {
	}
	
	public UserInfo(User user) {
		this.user = user;
	}
	
	public UserInfo(Info info) {
		this.info = info;
	}
	
	public UserInfo(User user, Info info) {
		this.user = user;
		this.info = info;
	}
	
	public User getUser() {
		return this.user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public Info getInfo() {
		return this.info;
	}
	
	public void setInfo(Info info) {
		this.info = info;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb;
		sb = new StringBuilder();
		sb.append('{');
		sb.append("\"user\":").append(user);
		sb.append(",\"info\":").append(info);
		sb.append('}');
		return sb.toString();
	}
}

class StringToUserInfoConverter implements Converter<String, UserInfo> {
	
	@Override
	public UserInfo convert(String from) {
		//System.out.println("from = " + from);
		UserInfo userInfo;
		try {
			userInfo = new ObjectMapper().readValue(from, UserInfo.class);
			Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
			Set<ConstraintViolation<UserInfo>> violationSet = validator.validate(userInfo, new Class[0]);
			if (violationSet.isEmpty()) {
				return userInfo;
			} else {
				final StringBuilder sb;
				sb = new StringBuilder();
				sb.append('{');
				for (ConstraintViolation<UserInfo> userInfoConstraintViolation : violationSet) {
					sb.append("\"").append(userInfoConstraintViolation.getPropertyPath()).append("\":").append("\"").append(userInfoConstraintViolation.getMessage()).append("\",");
				}
				sb.deleteCharAt(sb.lastIndexOf(","));
				sb.append('}');
				//throw new ValidationException(Maps.uniqueIndex(violationSet.iterator(),
				//                                               input -> input.getPropertyPath().toString()).toString
				//                                               ());
				throw new ValidationException(sb.toString());
			}
		} catch (IOException e) {
			throw new ClassCastException(e.getMessage());
		}
	}
}

@Configuration
class WebConfig implements WebMvcConfigurer {
	
	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new StringToUserInfoConverter());
	}
}

//@ControllerAdvice
//class BindingControllerAdvice {
//
//	@InitBinder
//	public void initBinder(WebDataBinder binder) {
//		binder.initDirectFieldAccess();
//	}
//}
