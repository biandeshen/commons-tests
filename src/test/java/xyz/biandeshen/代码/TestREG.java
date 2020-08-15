package com.zjs.dj.warehouse.standardorder;

import org.junit.Test;

import java.util.regex.Pattern;

/**
 * @FileName: TestREG
 * @Author: fjp
 * @Date: 2020/8/14 16:02
 * @Description: 测试日期时间正则表达式
 * History:
 * <author>          <time>          <version>
 * fjp           2020/8/14           版本号
 */
public class TestREG {
	//正则年份2001-2999，格式yyyy-MM-dd
	@Test
	public void testDate() {
		
		String content = "2001-8-14";
		String patten = "^(?:(?!2000)2[0-9]{3}-(?:(?:0?[1-9]|1[0-2])-(?:0?[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-" +
				                "(?:29|30)|(?:0?[13578]|1[02])-31)|(?:[0-9]{2}(?:0?[48]|[2468][048]|[13579][26])|" +
				                "(?:0?[48]|[2468][048]|[13579][26])00)-0?2-29)$";
		boolean isMatch = Pattern.matches(patten, content);
		System.out.println("patten = " + patten);
		System.out.println("isMatch = " + isMatch);
	}
	
	//正则年份2001-2999，格式yyyy-MM-dd或yyyy-M-d 连字符可以没有或是“-”、“/”、“.”之一。
	@Test
	public void testDate1() {
		
		String content = "2001/8/14";
		String patten =
				"^(?:(?!2000)2[0-9]{3}([-/.]?)(?:(?:0?[1-9]|1[0-2])\\1(?:0?[1-9]|1[0-9]|2[0-8])|" + "(?:0?[13" + "-9" + "]|1[0-2])\\1" + "(?:29|30)|(?:0?[13578]|1[02])\\1(?:31))|(?:[0-9]{2}" + "(?:0[48" + "]|[2468" + "][048]|[13579][26])|" + "(?:0[48]|[2468][048]|[13579][26])00)([-/.]?)" + "0?2" + "\\2(?:29)" + ")$";
		boolean isMatch = Pattern.matches(patten, content);
		System.out.println("patten = " + patten);
		System.out.println("isMatch = " + isMatch);
	}
	
	//正则年份2001-2999，格式yyyy-MM-dd HH:mm:ss
	@Test
	public void testDate2() {
		
		String content = "2020-08-14 16:30:31";
		String patten =
				"^(?:(?!2000)2[0-9]{3}-(?:(?:0?[1-9]|1[0-2])-(?:0?[1-9]|1[0-9]|2[0-8])|(?:0?[13-9]|1?[0-2])-" + "(?:29" +
						"|30)|(?:0?[13578]|1[02])-31)|(?:[0-9]{2}(?:0?[48]|[2468][048]|[13579][26])|" + "(?:0?[48" +
						"]|[2468][048]|[13579][26])00)-02-29)\\s+([01][0-9]|2[0-3])" + ":[0-5][0-9]:[0" + "-5][0-9]$";
		boolean isMatch = Pattern.matches(patten, content);
		System.out.println("patten = " + patten);
		System.out.println("isMatch = " + isMatch);
	}
	
}