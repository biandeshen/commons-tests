package xyz.biandeshen.commonstests;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author fjp
 * @Title: testTime
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/3/2618:14
 */
public class testTime {
	
	@Test
	public void test4() throws ParseException {
		
		DateTimeFormatter dateTimeFormatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("HH:mm:ss");
		String requestTime = "2019-3-26 18:13:46";
		String[] times = requestTime.split(" ");
		System.out.println(times[0] + "  ddddd " + times[1]);
		
		//LocalDate.parse(times[0], dateTimeFormatter1);
		//dateTimeFormatter1.parse(times[0]);
		//dateTimeFormatter2.parse(times[1]);
		
		LocalDateTime localDateTime = LocalDateTime.parse(requestTime, DateTimeFormatter.ofPattern("yyyy-M-dd HH:ss:mm"));
		System.out.println(localDateTime.toString());
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
		
		
		
		//LocalTime.parse(times[1], dateTimeFormatter2);
		
		
		
	}
	
}
