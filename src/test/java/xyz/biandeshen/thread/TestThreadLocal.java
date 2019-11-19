package xyz.biandeshen.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * @author fjp
 * @Title: TestThreadLocal
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/9/3014:16
 */
public class TestThreadLocal implements Runnable {
	static final Logger logger = LoggerFactory.getLogger(TestThreadLocal.class);
	
	protected static final ThreadLocal<String> threadLocal = new ThreadLocal<>();
	
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
		logger.info("start = {}", threadLocal.get());
		threadLocal.set(Thread.currentThread().getName());
		try {
			test();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			// 若注释 则不会清除threadLocal的值,同时,
			// 由于线程池原因,线程有限,同一个线程再次执行时,
			// 会因为前次没有清除,从而threadLocal.get()有值
			threadLocal.remove();
		}
	}
	
	public void test() {
		logger.info("test={}", threadLocal.get());
	}
	
	public static void main(String[] args) throws InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		for (int i = 0; i < 20; i++) {
			executorService.execute(new TestThreadLocal());
		}
		executorService.shutdown();
		executorService.awaitTermination(10L, TimeUnit.SECONDS);
		
	}
}
