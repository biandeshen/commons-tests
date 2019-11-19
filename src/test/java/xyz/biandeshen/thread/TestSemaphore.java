package xyz.biandeshen.thread;

import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;


/**
 * @author fjp
 * @Title: TestSemaphore
 * @ProjectName commons-tests
 * @Description: 信号量测试类
 * @date 2019/10/1610:38
 */
@Slf4j
public class TestSemaphore implements Runnable {
	
	private static final Semaphore semaphore = new Semaphore(5, true);
	private static final SecureRandom secureRandom = new SecureRandom();
	
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
		log.info("Acquire permit");
		try {
			semaphore.acquire(2);
			try {
				log.info("Got permit and {} remain", semaphore.availablePermits());
				Thread.sleep(1000L + secureRandom.nextInt(200));
			} finally {
				semaphore.release(2);
				log.info("Release permit and {} remain", semaphore.availablePermits());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ExecutorService executorService = Executors.newFixedThreadPool(100);
		for (int i = 0; i < 100; i++) {
			TestSemaphore testSemaphore = new TestSemaphore();
			executorService.execute(testSemaphore);
		}
		executorService.shutdown();
	}
}
