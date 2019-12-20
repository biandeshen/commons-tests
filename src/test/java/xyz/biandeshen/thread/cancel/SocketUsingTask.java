package xyz.biandeshen.thread.cancel;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @author fjp
 * @Title: SocketUsingTask
 * @ProjectName commons-tests
 * @Description: 通过newTaskFor将非标准的取消操作封装在一个任务中
 * TODO 定制任务的 Future 改变 Future.cancel() 方法的行为
 * @date 2019/11/2814:09
 */
@SuppressWarnings("ALL")
public abstract class SocketUsingTask<T> implements CancellableTask<T> {
	private Socket socket;
	
	protected synchronized void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	// TODO 定制取消时的功能 记录取消日志或收集统计信息等
	@Override
	public synchronized void cancel() {
		try {
			if (socket != null) {
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// 替代Executor中默认的FutureTask实现
	@Override
	public RunnableFuture<T> newTask() {
		return new FutureTask<T>(this) {
			@Override
			public boolean cancel(boolean mayInterruptIfRunning) {
				try {
					// *先调用自己的非标准中断实现
					SocketUsingTask.this.cancel();
				} finally {
					// finally 中存在 return 时,try中的结果将会被忽略,如果
					// 存在需要抛出的异常,那么异常也会被吃掉
					// ps: 在此处使用是因为,在上面重写cancel方法时,异常已被我们
					// 捕获处理,不会再有超出我们掌控范围外的可能性
					// *然后再调用父类的标准中断实现
					return super.cancel(mayInterruptIfRunning);
				}
			}
		};
	}
}

@SuppressWarnings("ALL")
interface CancellableTask<T> extends Callable<T> {
	void cancel();
	
	RunnableFuture<T> newTask();
}


@SuppressWarnings("ALL")
class CancellingExecutor extends ThreadPoolExecutor {
	
	
	public CancellingExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
	                          BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}
	
	
	public CancellingExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
	                          BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
	}
	
	
	public CancellingExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
	                          BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
	}
	
	
	public CancellingExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
	                          BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
	                          RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
		if (callable instanceof CancellableTask) {
			return ((CancellableTask<T>) callable).newTask();
		}
		return super.newTaskFor(callable);
	}
}
