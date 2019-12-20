package xyz.biandeshen.thread;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.LongStream;

/**
 * @author fjp
 * @Title: TestCalculator
 * @ProjectName commons-tests
 * @Description: 多种形式的累加性能比较 （串行、并行）
 * @date 2019/10/299:05
 */
public interface Calculator {
	long sumUp(long[] numbers);
	
	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			
			
			long[] numbers = LongStream.rangeClosed(1, 23333333).toArray();
			
			ForLoopCalculator for1 = new ForLoopCalculator();
			long start1 = System.currentTimeMillis();
			long l1 = for1.sumUp(numbers);
			long end1 = System.currentTimeMillis();
			System.out.println("(end1-start1) = " + (end1 - start1));
			System.out.println("l1 = " + l1);
			
			
			ForLoopCalculator1 for2 = new ForLoopCalculator1();
			long start2 = System.currentTimeMillis();
			long l2 = for2.sumUp(numbers);
			long end2 = System.currentTimeMillis();
			System.out.println("(end2-start2) = " + (end2 - start2));
			System.out.println("l2 = " + l2);
			
			ExecutorServiceCalculator for3 = new ExecutorServiceCalculator();
			long start3 = System.currentTimeMillis();
			long l3 = for3.sumUp(numbers);
			long end3 = System.currentTimeMillis();
			System.out.println("(end3-start3) = " + (end3 - start3));
			System.out.println("l3 = " + l3);
			
			ForkJoinCalculator for4 =
					new ForkJoinCalculator(new ForkJoinPool(Runtime.getRuntime().availableProcessors() - 1));
			long start4 = System.currentTimeMillis();
			long l4 = for4.sumUp(numbers);
			long end4 = System.currentTimeMillis();
			System.out.println("(end4-start4) = " + (end4 - start4));
			System.out.println("l4 = " + l4);
			
			/*
			 * 并行流
			 */
			Instant start5 = Instant.now();
			long result = LongStream.rangeClosed(1, 23333333L).parallel().reduce(0, Long::sum);
			Instant end5 = Instant.now();
			System.out.println("Duration.between(start5,end5).toMillis() = " + Duration.between(start5, end5).toMillis());
			System.out.println("result = " + result);
		}
	}
}

/**
 * 利用CompletableFuture.supplyAsync执行的for循环累加
 */
class ForLoopCalculator implements Calculator {
	@Override
	public long sumUp(long[] numbers) {
		return CompletableFuture.supplyAsync(() -> {
			long sum = 0;
			for (long number : numbers) {
				sum += number;
			}
			return sum;
		}).join();
	}
}

/**
 * 简单的for循环累加
 */
class ForLoopCalculator1 implements Calculator {
	
	@Override
	public long sumUp(long[] numbers) {
		long total = 0;
		for (long i : numbers) {
			total += i;
		}
		return total;
	}
}

/**
 * ExecutorService多线程方式累加
 */
class ExecutorServiceCalculator implements Calculator {
	private int parallism;
	private ExecutorService pool;
	
	
	public ExecutorServiceCalculator() {
		//cpu 核心数
		parallism = Runtime.getRuntime().availableProcessors();
		pool = Executors.newFixedThreadPool(parallism);
	}
	
	private static class SumTask implements Callable<Long> {
		private long[] numbers;
		private int from;
		private int to;
		
		public SumTask(long[] numbers, int from, int to) {
			this.numbers = numbers;
			this.from = from;
			this.to = to;
		}
		
		/**
		 * Computes a result, or throws an exception if unable to do so.
		 *
		 * @return computed result
		 *
		 * @throws Exception
		 * 		if unable to compute a result
		 */
		@Override
		public Long call() throws Exception {
			long total = 0;
			for (int i = from; i < to; i++) {
				total += numbers[i];
			}
			return total;
		}
	}
	
	@Override
	public long sumUp(long[] numbers) {
		List<Future<Long>> results = new ArrayList<>();
		
		int part = numbers.length / parallism;
		for (int i = 0; i < parallism; i++) {
			int from = i * part;
			int to = (i == parallism - 1) ? numbers.length : (i + 1) * part;
			
			results.add(pool.submit(new SumTask(numbers, from, to)));
		}
		long total = 0L;
		for (Future<Long> f : results) {
			try {
				total += f.get();
			} catch (Exception ignore) {
			}
		}
		pool.shutdown();
		return total;
	}
}

/**
 * ForkJoinPool 方式累加
 */
class ForkJoinCalculator implements Calculator {
	private ForkJoinPool pool;
	
	public ForkJoinCalculator(ForkJoinPool pool) {
		this.pool = pool;
	}
	
	private static class SumTask extends RecursiveTask<Long> {
		private long[] numbers;
		private int from;
		private int to;
		
		SumTask(long[] numbers, int from, int to) {
			this.numbers = numbers;
			this.from = from;
			this.to = to;
		}
		
		/**
		 * The main computation performed by this task.
		 *
		 * @return the result of the computation
		 */
		@Override
		protected Long compute() {
			List<ForkJoinTask<Long>> list = new ArrayList<>();
			long total = 0;
			if (to - from < 10000) {
				for (int i = from; i < to; i++) {
					total += numbers[i];
				}
				return total;
			} else {
				int mid = (from + to) / 2;
				SumTask leftTask = new SumTask(numbers, from, mid);
				SumTask rightTask = new SumTask(numbers, mid, to);
				list.add(leftTask.fork());
				list.add(rightTask.fork());
			}
			
			for (ForkJoinTask<Long> longForkJoinTask : list) {
				total += longForkJoinTask.join();
			}
			return total;
		}
	}
	
	
	@Override
	public long sumUp(long[] numbers) {
		Long result = pool.invoke(new SumTask(numbers, 0, numbers.length));
		pool.shutdown();
		return result;
	}
}