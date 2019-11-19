package xyz.biandeshen;

/**
 * @author fjp
 * @Title: s
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/6/410:18
 */

import org.junit.Test;
import xyz.biandeshen.thread.StateSafeThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * 状态安全线程测试类
 *
 * @author pengjunlin
 * @package com.boonya.thread.StateSafeThreadTest
 * @date 2017年3月22日  下午4:41:37
 * @comment
 * @update
 */
public class StateSafeThreadTest {
	
	/**
	 * 线程安全的
	 *
	 * @throws
	 * @MethodName: testByOneThread
	 * @Description:
	 */
	@Test
	public void testByOneThread() {
		long start = System.currentTimeMillis();
		for (int i = 0; i < 5; i++) {
			Thread thread = new StateSafeThread();
			thread.start();
		}
		
		try {
			Thread.sleep(30000);
			System.out.println(System.currentTimeMillis() - start + "ms");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 线程安全的
	 *
	 * @throws
	 * @MethodName: testByThreadPool
	 * @Description:
	 */
	@Test
	public void testByThreadPool() {
		long start = System.currentTimeMillis();
		Thread thread;
		ExecutorService executorService = Executors.newFixedThreadPool(5);// 线程个数大于或等于线程个数时线程安全
		
		for (int i = 0; i < 5; i++) {
			thread = new StateSafeThread();
			executorService.execute(thread);
		}
		/*Future future=executorService.submit(thread);
		
		try {
			System.out.println(future.get());
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
		try {
			executorService.awaitTermination(20000, TimeUnit.MILLISECONDS);
			System.out.println(System.currentTimeMillis() - start + "ms");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			executorService.shutdown();
		}
		
	}
	
	/**
	 * 非线程安全的
	 *
	 * @throws
	 * @MethodName: testByThreadPool2
	 * @Description:
	 */
	@Test
	public void testByThreadPool2() {
		long start = System.currentTimeMillis();
		Thread thread;
		ExecutorService executorService = Executors.newFixedThreadPool(3);// 小于线程个数导致线程不安全
		
		for (int i = 0; i < 5; i++) {
			thread = new StateSafeThread();
			executorService.execute(thread);
		}
		/*Future future=executorService.submit(thread);
		
		try {
			System.out.println(future.get());
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
		try {
			executorService.awaitTermination(30000, TimeUnit.MILLISECONDS);
			System.out.println(System.currentTimeMillis() - start + "ms");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			executorService.shutdown();
		}
		
	}
	
	/**
	 * 线程安全的
	 *
	 * @throws
	 * @MethodName: testByThreadPool3
	 * @Description:
	 */
	@Test
	public void testByThreadPool3() {
		long start = System.currentTimeMillis();
		Thread thread;
		ExecutorService executorService = Executors.newCachedThreadPool();
		
		
		for (int i = 0; i < 5; i++) {
			thread = new StateSafeThread();
			executorService.execute(thread);
		}
		/*Future future=executorService.submit(thread);
		
		try {
			System.out.println(future.get());
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
		try {
			executorService.awaitTermination(30000, TimeUnit.MILLISECONDS);
			System.out.println(System.currentTimeMillis() - start + "ms");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			executorService.shutdown();
		}
		
	}
	
}
