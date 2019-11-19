package xyz.biandeshen.thread;

import java.util.concurrent.*;

/**
 * @author fjp
 * @Title: TestCompletableFuture
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/10/2822:35
 */
public class TestCompletableFuture {
	
	public static void main(String[] args) {
		try {
			String s1 = CompletableFuture.supplyAsync(() -> "Hello").thenApply(i -> i + 1).thenApply(s -> s + " World").thenApply(String::toUpperCase).get(10, TimeUnit.MILLISECONDS);
			System.out.println("s1 = " + s1);
			CompletableFuture.supplyAsync(() -> 1).thenApply(i -> i + 1).thenApply(i -> i + i).whenComplete((r, e) -> System.out.println(r));
			CompletableFuture.supplyAsync(() -> 1).thenApply(i -> i + 1).thenApply(i -> i + i).thenApply(s -> s + " " + "World").whenComplete((r, e) -> System.out.println(r));
			//ExecutorService executor = Executors.newCachedThreadPool();
			CompletableFuture.supplyAsync(() -> 1).thenApply(i -> i + 1).thenApply(i -> i + i).thenApply(s -> s + " " + "World").whenCompleteAsync((r, throwable) -> {System.out.println(r);}).exceptionally(Throwable::toString).join();
			
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			e.printStackTrace();
		}
	}
	
	
}
