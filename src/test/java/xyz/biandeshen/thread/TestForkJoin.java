package xyz.biandeshen.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author fjp
 * @Title: TestForkJoin
 * @ProjectName commons-tests
 * @Description: fork/join
 * @date 2019/10/2811:22
 */
public class TestForkJoin extends RecursiveTask<Integer> {
	private static final long serialVersionUID = -1314141414141L;
	
	private static final Logger logger = LoggerFactory.getLogger(TestForkJoin.class);
	
	protected int start;
	
	protected int end;
	
	public TestForkJoin(int start, int end) {
		this.start = start;
		this.end = end;
	}
	
	/**
	 * The main computation performed by this task.
	 *
	 * @return the result of the computation
	 */
	@Override
	protected Integer compute() {
		//声明计算用的变量
		int m = 2, s = start, n = end, r = 0;
		//创建子任务,每个子任务处理m个数字
		List<ForkJoinTask<Integer>> lt = new ArrayList<>();
		do {
			if (n - s < m * 1.5f) {
				for (int i = s; i <= n; i++) {
					r += i;
				}
				logger.info("Sum {}~{} = {}", s, n, r);
			} else {
				n = Math.min(s + m - 1, n);
				lt.add(new TestForkJoin(s, n).fork());
			}
			s = n + 1;
			n = end;
		} while (s <= end);
		for (ForkJoinTask<Integer> integerForkJoinTask : lt) {
			r += integerForkJoinTask.join();
		}
		return r;
	}
	
	public static void main(String[] args) {
		ForkJoinPool fjp = new ForkJoinPool();
		//	计算 ss 到 nn 的累加
		int ss = 1, nn = 11;
		try {
			long start = System.currentTimeMillis();
			Future<Integer> result = fjp.submit(new TestForkJoin(ss, nn));
			while (!result.isDone()) {
				Thread.sleep(10);
			}
			System.out.println("result.get() = " + result.get());
			logger.info(String.valueOf((System.currentTimeMillis()) - start));
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
	}
}