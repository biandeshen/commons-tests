package xyz.biandeshen.thread;

import java.util.concurrent.*;

/**
 * @author fjp
 * @Title: ExecutorServiceTest
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/9/2614:29
 */
public class ExecutorServiceTest {
	
	public static void main(String[] args) throws InterruptedException {
		ExecutorService executorService = Executors.newCachedThreadPool();
		long start = System.currentTimeMillis();
		for (int i = 0; i < 5; i++) {
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					int i = 0;
					//while (!Thread.interrupted() && i < Integer.MAX_VALUE) {
					//System.out.println(Thread.currentThread().getName()+"Thread.interrupted2() = " + Thread.interrupted());
					while (!Thread.interrupted() && i < Integer.MAX_VALUE) {
						System.out.println(Thread.currentThread().getName()+"Thread.interrupted() = " + Thread.interrupted());
						i++;
					}
					System.out.println("i = " + i);
				}
			});
		}
		//executorService.shutdown();
		executorService.shutdownNow();
		System.out.println("executorService.isTerminated() = " + executorService.isTerminated());
		//executorService.execute(new Runnable() {
		//	@Override
		//	public void run() {
		//		int i = 0;
		//		//while (!Thread.interrupted() && i < Integer.MAX_VALUE) {
		//		System.err.println("Thread.interrupted() = " + Thread.interrupted());
		//		while (!Thread.interrupted() && i < Integer.MAX_VALUE) {
		//			i++;
		//		}
		//		System.out.println("i = " + i);
		//	}
		//});
		executorService.awaitTermination(10L, TimeUnit.SECONDS);
		System.out.println("System.currentTimeMillis()-start = " + (System.currentTimeMillis() - start) + "ms");
		System.err.println("executorService.isTerminated() = " + executorService.isTerminated());
	}
	
	public static void main2(String[] args) throws InterruptedException {
		ExecutorService executorService = Executors.newCachedThreadPool();
		long start = System.currentTimeMillis();
		Future<String> stringFuture = executorService.submit(new Callable<String>() {
			/**
			 * Computes a result, or throws an exception if unable to do so.
			 *
			 * @return computed result
			 *
			 * @throws Exception
			 * 		if unable to compute a result
			 */
			@Override
			public String call() throws Exception {
				return "";
			}
		});
		//executorService.shutdown();
		executorService.shutdownNow();
		System.out.println("executorService.isTerminated() = " + executorService.isTerminated());
		//executorService.execute(new Runnable() {
		//	@Override
		//	public void run() {
		//		int i = 0;
		//		//while (!Thread.interrupted() && i < Integer.MAX_VALUE) {
		//		System.err.println("Thread.interrupted() = " + Thread.interrupted());
		//		while (!Thread.interrupted() && i < Integer.MAX_VALUE) {
		//			i++;
		//		}
		//		System.out.println("i = " + i);
		//	}
		//});
		executorService.awaitTermination(10L, TimeUnit.SECONDS);
		System.out.println("System.currentTimeMillis()-start = " + (System.currentTimeMillis() - start) + "ms");
		System.err.println("executorService.isTerminated() = " + executorService.isTerminated());
	}
}
