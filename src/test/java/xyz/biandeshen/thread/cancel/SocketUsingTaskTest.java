package xyz.biandeshen.thread.cancel;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author fjp
 * @Title: SocketUsingTaskTest
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/11/2815:29
 */
public class SocketUsingTaskTest {
	private static final String SUCCESS = "success";
	
	
	public static void main(String[] args) {
		ExecutorService executorService = new CancellingExecutor(1, 10, 10, TimeUnit.SECONDS,
		                                                         new LinkedBlockingQueue<>(100));
		
		Future<Object> submit = executorService.submit(new SocketUsingTask<Object>() {
			@Override
			public Object call() throws Exception {
				Thread.sleep(5000);
				System.out.println("submit方法执行任务完成" + "   thread name: " + Thread.currentThread().getName());
				return SUCCESS;
			}
		});
		
		try {
			String str = (String) submit.get();
			submit. cancel(true);
			if (SUCCESS.equals(str)) {
				String name = Thread.currentThread().getName();
				System.out.println("经过返回值比较，submit方法执行任务成功    thread name: " + name);
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		try {
			executorService.awaitTermination(1, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<Runnable> runnables = executorService.shutdownNow();
		if (runnables.size() > 0) {
			runnables.get(0).run();
		}
	}
}
