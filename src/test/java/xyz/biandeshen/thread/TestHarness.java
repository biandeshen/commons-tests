package xyz.biandeshen.thread;

import java.util.concurrent.CountDownLatch;

/**
 * @author fjp
 * @Title: TestHarness
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/11/1115:07
 */
public class TestHarness {
	public long timetasks(int nThreads, final Runnable task) throws InterruptedException {
		// init start from 1.
		final CountDownLatch startGate = new CountDownLatch(1);
		final CountDownLatch endGate = new CountDownLatch(nThreads);
		for (int i = 0; i < nThreads; i++) {
			Thread t = new Thread(() -> {
				try {
					startGate.await();//wait the startgate count down to zero, then run.
					try {
						task.run();
					} finally {
						endGate.countDown();
					}
				} catch (InterruptedException ignored) { }
			});
			t.start();
		}
		long start = System.nanoTime(); //单位纳秒
		startGate.countDown(); // 因为本身值为1, 故无需用await等待
		endGate.await();//wait the endGate count down to zero, then run.
		long end = System.nanoTime();
		return end - start;
	}
	
	public static void main(String[] args) throws InterruptedException {
		final Thread tt = new Thread() {
			@Override
			public void run() {
				System.out.println("Thread Id: " + this.getId());  //具体的方法，比如DOTA2中点击准备就绪的按键
			}
		};
		TestHarness bisuo = new TestHarness();
		long threadtime = bisuo.timetasks(100, tt);
		double ans = (double) threadtime / 1000000000;
		System.out.println("Thread time:" + ans + "s");
	}
}
