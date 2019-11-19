package xyz.biandeshen.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author fjp
 * @Title: WaitAndNotifyTest
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/9/2714:03
 */
public class WaitAndNotifyTest implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(WaitAndNotifyTest.class);
	
	private static final byte[] flag = new byte[0];
	
	
	/**
	 * When an object implementing interface <code>Runnable</code> is used
	 * to create a thread, starting the thread causes the object's
	 * <code>run</code> method to be called in that separately executing
	 * thread.
	 * <p>
	 * The general contract of the method <code>run</code> is that it may
	 * take any action whatsoever.
	 *
	 * @see Thread#run()
	 */
	@Override
	public void run() {
		synchronized (flag) {
			try {
				logger.info(Thread.currentThread().getName() + " Before flag.wait()");
				flag.wait();
				logger.info(Thread.currentThread().getName() + " After flag.wait()");
				//flag.wait(1000L);
				Thread.sleep(1000L);
				logger.info(Thread.currentThread().getName() + " After Thread.sleep()");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(3);
		executorService.execute(new WaitAndNotifyTest());
		executorService.execute(new WaitAndNotifyTest());
		long start = System.currentTimeMillis();
		executorService.execute(() -> {
			try {
				logger.info(Thread.currentThread().getName() + " Before Thread.sleep()");
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			synchronized (flag) {
				logger.info(Thread.currentThread().getName() + " flag.notifyAll()");
				//flag.notify();
				flag.notifyAll();
			}
		});
		executorService.shutdown();
		//executorService.shutdownNow();
		executorService.awaitTermination(10L, TimeUnit.SECONDS);
		System.out.println("executorService.isTerminated() = " + executorService.isTerminated());
		System.out.println("System.currentTimeMillis()-start = " + (System.currentTimeMillis() - start) + "ms");
		System.err.println("executorService.isTerminated() = " + executorService.isTerminated());
	}
}


