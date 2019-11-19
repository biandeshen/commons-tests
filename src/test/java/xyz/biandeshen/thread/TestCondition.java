package xyz.biandeshen.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author fjp
 * @Title: TestCondition
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/9/2911:18
 */
public class TestCondition implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(TestCondition.class);
	
	private static final Lock lock = new ReentrantLock();
	
	public static final Condition condition1 = lock.newCondition();
	public static final Condition condition2 = lock.newCondition();
	
	private boolean flag;
	
	public TestCondition(boolean flag) {
		this.flag = flag;
	}
	
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
		lock.lock();
		try {
			if (flag) {
				logger.info("Before condition1.await()");
				condition1.await();
				logger.info("After condition1.await()");
			} else {
				logger.info("Before condition2.await()");
				condition2.await();
				logger.info("After condition2.await()");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
	
	public static void main(String[] args) {
		ExecutorService executorService = Executors.newCachedThreadPool();
		executorService.execute(new TestCondition(true));
		executorService.execute(new TestCondition(false));
		executorService.execute(() -> {
			boolean b = false;
			lock.lock();
			try {
				Thread.sleep(1000L);
				logger.info("condition1.signalAll()");
				condition1.signalAll();
				if (b) {
					Thread.sleep(2000L);
					logger.info("condition2.signalAll()");
					condition2.signalAll();
					return;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
			//	condition2
			lock.lock();
			try {
				Thread.sleep(1000L);
				logger.info("condition2.signalAll()");
				condition2.signalAll();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		});
		
	}
}
