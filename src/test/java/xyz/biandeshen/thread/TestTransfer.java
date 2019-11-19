package xyz.biandeshen.thread;

/**
 * @author fjp
 * @Title: Test传递
 * @ProjectName commons-tests
 * @Description: 值传递
 * @date 2019/11/1110:09
 */
public class TestTransfer {
	volatile String str = "111";
	
	public static void main(String[] args) {
		TestTransfer testTransfer = new TestTransfer();
		String str1 = testTransfer.str;
		for (int i = 0; i < 10; i++) {
			System.out.println("str1 = " + str1);
			testTransfer.str = testTransfer.str.concat(String.valueOf(i));
			System.out.println("testTransfer.str = " + testTransfer.str);
		}
		
	}
	
}
