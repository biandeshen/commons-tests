package xyz.biandeshen.generic;

import java.io.File;
import java.util.Scanner;

/**
 * @author fjp
 * @Title: ClearExceptionCheck
 * @ProjectName commons-tests
 * @Description: 消除对受查异常的检查
 * @date 2019/8/1610:20
 */
public class ClearExceptionCheck {
	public static void main(String[] args) {
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
		return new Thread(new Runnable() {
			@Override
			public void run() {
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
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	private static <T extends Throwable> void throwAs(Throwable e) throws T {
		throw (T) e;
	}
	
	private void testThrow(Throwable throwable) throws RuntimeException {
		//throw new RuntimeException("文件找不到!", throwable);
		throw new RuntimeException(throwable);
	}
}


