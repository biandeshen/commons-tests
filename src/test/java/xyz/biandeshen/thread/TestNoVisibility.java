package xyz.biandeshen.thread;

/**
 * @author fjp
 * @Title: TestNoVisibility
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/11/610:52
 */
public class TestNoVisibility {
	private static boolean ready;
	
	private static int number;
	
	private static class ReaderThead extends Thread {
		/**
		 * If this thread was constructed using a separate
		 * <code>Runnable</code> run object, then that
		 * <code>Runnable</code> object's <code>run</code> method is called;
		 * otherwise, this method does nothing and returns.
		 * <p>
		 * Subclasses of <code>Thread</code> should override this method.
		 *
		 * @see #start()
		 * @see #stop()
		 * @see #Thread(ThreadGroup, Runnable, String)
		 */
		@Override
		public void run() {
			int i = 0;
			while (!ready) {
				i++;
			}
			System.out.println("number = " + number);
		}
	}
	
	
	public static void main(String[] args) throws InterruptedException {
		new ReaderThead().start();
		Thread.sleep(1000);
		number = 42;
		ready = true;
	}
}
