package xyz.biandeshen.other;

/**
 * @author fjp
 * @Title: TestEffectivelyFinal
 * @ProjectName commons-tests
 * @Description: Effectively final 功能
 * @date 2019/11/2117:18
 */
public class TestEffectivelyFinal {
	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			//Integer finalI = i;
			//Effectively final 功能
			final int finalI = i;
			new Thread(() -> System.out.println("i = " + finalI)).start();
		}
	}
}
