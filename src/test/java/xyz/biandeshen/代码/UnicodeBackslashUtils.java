package xyz.biandeshen.代码;

import java.util.regex.Pattern;

/**
 * @FileName: UnicodeBackslashUtils
 * @author: 张超
 * @Date: 2020/5/12 9:16
 * @Description: 字符串中存在 反斜杠+u 开头 的Unicode字符。本类用于把那些Unicode字符串转换成汉字
 * @Url: https://blog.csdn.net/zhangchao19890805/article/details/84227981
 * History:
 * <author>          <time>          <version>
 * admin           2020/5/12           版本号
 */
@SuppressWarnings("ALL")
public final class UnicodeBackslashUtils {
	
	private UnicodeBackslashUtils() {
	}
	
	/**
	 * 单个字符的正则表达式
	 */
	private static final String SINGLE_PATTERN = "[0-9|a-f|A-F]";
	/**
	 * 4个字符的正则表达式
	 */
	private static final String MULTI_PATTERN = SINGLE_PATTERN + SINGLE_PATTERN + SINGLE_PATTERN + SINGLE_PATTERN;
	
	
	/**
	 * 把 \\u 开头的单字转成汉字，如 \\u6B65 ->　步
	 *
	 * @param str
	 * 		待转换的unicode开头的字符串
	 *
	 * @return 汉字
	 */
	private static String ustartToCn(final String str) {
		StringBuilder sb = new StringBuilder().append("0x").append(str, 2, 6);
		int code = Integer.decode(sb.toString());
		char c = (char) code;
		return String.valueOf(c);
	}
	
	/**
	 * 字符串是否以Unicode字符开头。约定Unicode字符以 \\u开头。
	 *
	 * @param str
	 * 		字符串
	 *
	 * @return true表示以Unicode字符开头.
	 */
	private static boolean isStartWithUnicode(final String str) {
		if (null == str || str.length() == 0) {
			return false;
		}
		if (!str.startsWith("\\u")) {
			return false;
		}
		// \u6B65
		if (str.length() < 6) {
			return false;
		}
		String content = str.substring(2, 6);
		
		return Pattern.matches(MULTI_PATTERN, content);
	}
	
	/**
	 * 字符串中，所有以 \\u 开头的UNICODE字符串，全部替换成汉字
	 *
	 * @param str
	 * 		待转换的Unicode字符串
	 *
	 * @return 汉字
	 */
	public static String unicodeToCn(final String str) {
		// 用于构建新的字符串
		StringBuilder sb = new StringBuilder();
		// 从左向右扫描字符串。tmpStr是还没有被扫描的剩余字符串。
		// 下面有两个判断分支：
		// 1. 如果剩余字符串是Unicode字符开头，就把Unicode转换成汉字，加到StringBuilder中。然后跳过这个Unicode字符。
		// 2.反之， 如果剩余字符串不是Unicode字符开头，把普通字符加入StringBuilder，向右跳过1.
		int length = str.length();
		int i;
		for (i = 0; i < length; ) {
			String tmpStr = str.substring(i);
			if (isStartWithUnicode(tmpStr)) {
				// 分支1
				sb.append(ustartToCn(tmpStr));
				i += 6;
			} else {
				// 分支2
				sb.append(str, i, i + 1);
				++i;
			}
		}
		return sb.toString();
	}
}