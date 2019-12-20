package xyz.biandeshen.thread;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newScheduledThreadPool;
import static xyz.biandeshen.thread.LaunderThrowable.launderThrowable;

/**
 * @author fjp
 * @Title: TimeRun2
 * @ProjectName commons-tests
 * @Description: 专门的线程中中断线程
 * @date 2019/11/2515:25
 */
public class TimedRun2 {
	
	private static final ScheduledExecutorService cancelExec = newScheduledThreadPool(1);
	
	public static void timedRun(final Runnable r,
	                            long timeout, TimeUnit unit)
	throws InterruptedException {
		class RethrowableTask implements Runnable {
			private volatile Throwable t;
			
			public void run() {
				try {
					r.run();
				} catch (Throwable t) {
					this.t = t;
				}
			}
			
			void rethrow() throws InterruptedException {
				if (t != null)
					throw launderThrowable(t);
			}
		}
		
		RethrowableTask task = new RethrowableTask();
		final Thread taskThread = new Thread(task);
		taskThread.start();
		cancelExec.schedule(taskThread::interrupt, timeout, unit);
		taskThread.join(unit.toMillis(timeout));
		task.rethrow();
	}
}

class LaunderThrowable extends Throwable {
	
	public LaunderThrowable() {
	}
	
	public LaunderThrowable(String message) {
		super(message);
	}
	
	public LaunderThrowable(Throwable cause) {
		super(cause);
	}
	
	public static InterruptedException launderThrowable(Throwable cause) {
		return (InterruptedException) cause;
	}
	
	private static final long serialVersionUID = 6109133060804112088L;
}