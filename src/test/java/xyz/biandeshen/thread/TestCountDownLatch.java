package xyz.biandeshen.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author fjp
 * @Title: TestCountDownLatch
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/9/3014:36
 */
public class TestCountDownLatch {
	private static final Logger logger = LoggerFactory.getLogger(TestCountDownLatch.class);
	
	public static void main(String[] args) {
		logger.info("Thread.currentThread().getName() = {}  Start!", Thread.currentThread().getName());
		
		int count = 100;
		final CountDownLatch cdl = new CountDownLatch(count);
		AtomicInteger sum = new AtomicInteger();
		//ExecutorService executorService = Executors.newFixedThreadPool(5);
		ExecutorService executorService = Executors.newCachedThreadPool();
		for (int i = 0; i < count; i++) {
			executorService.execute(() -> {
				try {
					synchronized (cdl) {
						//synchronized (sum) {
						System.out.println("cdl.getCount() = " + cdl.getCount());
						sum.getAndIncrement();
					}
				} finally {
					cdl.countDown();
				}
			});
		}
		try {
			cdl.await(10L, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		executorService.shutdown();
		System.err.println("cdl.getCount() = " + cdl.getCount());
		System.out.println("sum.get() = " + sum.get());
		
		logger.info("Thread.currentThread().getName() = {}  Start!", Thread.currentThread().getName());
	}
}

