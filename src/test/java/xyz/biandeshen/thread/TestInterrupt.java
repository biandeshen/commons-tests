package xyz.biandeshen.thread;

/**
 * @author fjp
 * @Title: TestInterrupt
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/11/119:45
 */
public class TestInterrupt extends Thread {
	public static void main(String[] args) throws InterruptedException {
		TestInterrupt testInterrupt = new TestInterrupt();
		System.out.println("Starting thread...");
		testInterrupt.start();
		testInterrupt.sleep(3000);
		System.out.println("Asking thread to stop...");
		// 发出中断请求
		testInterrupt.interrupt();
		testInterrupt.sleep(3000);
		System.out.println("Stoping application...");
	}
	
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
	 */
	@Override
	public void run() {
		// 每隔一秒检查是否设置了中断标识
		while (!Thread.currentThread().isInterrupted()) {
			System.out.println("Thread is running...");
			long time = System.currentTimeMillis();
			while ((System.currentTimeMillis() - time < 1000)) {
			
			}
		}
		System.out.println("Thread exiting under request...");
	}
}
