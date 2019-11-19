package xyz.biandeshen.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * @author fjp
 * @Title: TestCyclicBarrier
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/10/1215:37
 */
public class TestCyclicBarrier implements Runnable {
	public static final Logger logger = LoggerFactory.getLogger(TestCyclicBarrier.class);
	
	/**
	 * cyclicBarrier 栅栏
	 * 构造函数中的数量表示 要阻塞的线程数
	 */
	public static final CyclicBarrier cycliBarrier = new CyclicBarrier(5);
	
	private int page;
	
	public TestCyclicBarrier(int page) {
		this.page = page;
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
		for (int i = 0; i < 2; i++) {
			try {
				Thread.sleep(1000L + page * 1000L);
				logger.info("{} waiting threads", cycliBarrier.getNumberWaiting());
				// 执行到此处时,执行栅栏的wait方法,表示当前线程已经完成了自身的任务,开始等待其他线程达到栅栏的条件
				cycliBarrier.await(10L, TimeUnit.SECONDS);
				logger.info("continue...");
			} catch (InterruptedException | BrokenBarrierException | TimeoutException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		for (int i = 0; i < 5; i++) {
			TestCyclicBarrier testCyclicBarrier = new TestCyclicBarrier(i);
			executorService.execute(testCyclicBarrier);
		}
		//executorService.awaitTermination()
		executorService.shutdown();
	}
}
