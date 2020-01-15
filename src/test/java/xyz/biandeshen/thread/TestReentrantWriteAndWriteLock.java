package xyz.biandeshen.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author fjp
 * @Title: TestReentrantWriteAndWriteLock
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/9/2716:40
 */
public class TestReentrantWriteAndWriteLock {
	private static final Logger logger = LoggerFactory.getLogger(TestReentrantWriteAndWriteLock.class);
	
	private static final ReentrantReadWriteLock rrwl = new ReentrantReadWriteLock();
	public static final Deque<Integer> data = new LinkedList<>();
	public static ConcurrentHashMap<String, Integer> concurrentHashMap = new ConcurrentHashMap<>(1);
	private static int value;
	
	
	public static void main(String[] args) throws InterruptedException {
		long start = System.currentTimeMillis();
		ExecutorService executorService = Executors.newCachedThreadPool();
		for (int i = 0; i < 5; i++) {
			executorService.execute(() -> {
				for (int j = 0; j < 10; j++) {
					rrwl.readLock().lock();
					//rrwl.writeLock().lock();
					try {
						data.addFirst(++value);
						logger.info(Thread.currentThread().getName());
					} finally {
						rrwl.readLock().unlock();
						//rrwl.writeLock().unlock();
					}
				}
			});
			
			executorService.execute(() -> {
				for (int j = 0; j < 10; j++) {
					Integer v;
					do {
						rrwl.writeLock().lock();
						//rrwl.readLock().lock();
						try {
							v = data.pollLast();
							if (v == null) {
								Thread.yield();
							} else {
								logger.info(Thread.currentThread().getName() + " v = {}", v);
								break;
							}
						} finally {
							//rrwl.readLock().unlock();
							rrwl.writeLock().unlock();
						}
					} while (true);
				}
			});
		}
		//executorService.awaitTermination(10L, TimeUnit.SECONDS);
		executorService.shutdown();
		System.out.println("(System.currentTimeMillis() - start) = " + (System.currentTimeMillis() - start) + " ms");
	}
}
