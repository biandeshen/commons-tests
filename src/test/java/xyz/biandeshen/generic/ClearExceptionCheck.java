package xyz.biandeshen.generic;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author fjp
 * @Title: ClearExceptionCheck
 * @ProjectName commons-tests
 * @Description: 消除对受查异常的检查 && https://www.ctolib.com/topics-1449.html
 * @date 2019/8/1610:20
 */
public class ClearExceptionCheck {
	public static void main(String[] args) {
		//抛出受检查的异常但却并不想在方法上加入throws XXException的signature
		new Block() {
			@Override
			public void body() throws Exception {
				Scanner in = new Scanner(new File("nofile"), "UTF-8");
				while (in.hasNext()) {
					System.out.println("in.nextInt() = " + in.nextInt());
				}
			}
		}.toThread().start();
	}
}

abstract class Block {
	public abstract void body() throws Exception;
	
	Thread toThread() {
		return new Thread(() -> {
			try {
				body();
			} catch (Exception e) {
				//throwAs(e);
				Block.throwAs(e);
				//testThrow(e);
			}
			//try {
			//	body();
			//} catch (Exception e) {
			//	e.printStackTrace();
			//}
		});
	}
	
	@SuppressWarnings("unchecked")
	private static <E extends Throwable> void throwAs(Throwable e) throws E {
		throw (E) e;
	}
	
	private void testThrow(Throwable throwable) throws RuntimeException {
		//throw new RuntimeException("文件找不到!", throwable);
		throw new RuntimeException(throwable);
	}
}


