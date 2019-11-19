package xyz.biandeshen.thread;

/**
 * @author fjp
 * @Title: TestInterrupt
 * @ProjectName commons-tests
 * @Description: 使用thread.interrupt()中断阻塞状态线程
 * @date 2019/11/119:45
 */
public class TestInterrupt2 extends Thread {
	public static void main(String[] args) throws InterruptedException {
		TestInterrupt2 testInterrupt = new TestInterrupt2();
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
			try {
				/*
				 * 如果线程阻塞，将不会去检查中断信号量stop变量，所 以thread.interrupt()
				 * 会使阻塞线程从阻塞的地方抛出异常，让阻塞线程从阻塞状态逃离出来，并
				 * 进行异常块进行 相应的处理
				 */
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.out.println("Thread interrupted...");
				/*
				 * 如果线程在调用 Object.wait()方法，或者该类的 join() 、sleep()方法
				 * 过程中受阻，则其中断状态将被清除
				 */
				System.out.println(this.isInterrupted());// false
				Thread.currentThread().interrupt();
			}
		}
		System.out.println("Thread exiting under request...");
	}
}
