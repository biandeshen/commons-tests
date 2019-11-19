package xyz.biandeshen;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author fjp
 * @Title: TestContains
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/10/813:59
 */
public class TestContains {
	
	static final String str = "<responses><logisticProviderID>ZJS</logisticProviderID><responseItems><response><success>false</success><reason>_S01_ϵͳ�쳣</reason></response></responseItems></responses>";
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		//byte[] containsAny = str.getBytes("gbk");
		System.out.println("containsAny = " + StringUtils.toEncodedString(str.getBytes(StandardCharsets.UTF_8),
				Charset.forName("gbk")));
	}
}
