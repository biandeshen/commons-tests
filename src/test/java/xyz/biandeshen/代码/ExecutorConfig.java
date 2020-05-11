package xyz.biandeshen.代码;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author fjp
 * @Title: ScheduleConfig
 * @ProjectName common
 * @Description: Schedule注册类, 实现支持定时任务的并行及异步
 * @date 2019/5/2314:54
 */
@Configuration
public class ExecutorConfig implements SchedulingConfigurer, AsyncConfigurer {
	private static final Logger logger = LoggerFactory.getLogger(ExecutorConfig.class);
	
	/**
	 * 设置ThreadPoolExecutor的核心池大小
	 */
	private static final int CORE_POOL_SIZE = 10;
	/**
	 * 设置ThreadPoolExecutor的最大池大小
	 */
	private static final int MAX_POOL_SIZE = 100;
	/**
	 * 为ThreadPoolExecutor的BlockingQueue设置容量
	 */
	private static final int QUEUE_CAPACITY = 50;
	/**
	 * 为ThreadPoolExecutor设置线程活跃时间（秒）
	 */
	private static final int KEEP_ALIVE_SECONDS = 120;
	/**
	 * 为ThreadPoolExecutor设置线程等待终止秒（秒）
	 */
	private static final int AWAIT_TERMINATION_SECONDS = 60;
	
	/**
	 * 异步任务 执行线程池
	 *
	 * @return 异步任务 执行器
	 * <p>
	 * (主要,若有其他要求,也可新增异步线程池进行使用与修改)
	 *
	 * @see ExecutorConfig#getAsyncExecutor()
	 */
	@Bean("asyncServiceExecutor")
	public Executor asyncServiceExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		//配置核心线程数
		executor.setCorePoolSize(CORE_POOL_SIZE);
		//配置最大线程数
		executor.setMaxPoolSize(MAX_POOL_SIZE);
		//配置阻塞任务队列大小
		executor.setQueueCapacity(QUEUE_CAPACITY);
		//设置线程活跃时间（秒）
		executor.setKeepAliveSeconds(KEEP_ALIVE_SECONDS);
		//配置线程池中的线程的名称前缀
		executor.setThreadNamePrefix("asyncExecutor-");
		// rejection-policy：当pool已经达到max size的时候，如何处理新任务
		// CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		//调度器shutdown被调用时等待当前被调度的任务完成
		executor.setWaitForTasksToCompleteOnShutdown(true);
		//等待时长
		executor.setAwaitTerminationSeconds(AWAIT_TERMINATION_SECONDS);
		//执行初始化
		executor.initialize();
		return executor;
	}
	
	
	/**
	 * 定时任务 线程池
	 *
	 * @return ThreadPoolTaskScheduler 线程池
	 */
	@Bean(destroyMethod = "shutdown", name = "taskScheduler")
	public ThreadPoolTaskScheduler taskScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		//线程池大小,默认 异步任务核心线程数*2 即定时任务线程池大小为异步任务线程池核心数*2
		scheduler.setPoolSize(CORE_POOL_SIZE >> 1);
		
		/*设置线程名开头*/
		scheduler.setThreadNamePrefix("scheduleTask-");
		//设置定时任务线程等待终止秒
		scheduler.setAwaitTerminationSeconds(AWAIT_TERMINATION_SECONDS << 2);
		scheduler.setWaitForTasksToCompleteOnShutdown(true);
		return scheduler;
	}
	
	/**
	 * 并行任务 -- 设置定时线程池
	 */
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		TaskScheduler taskScheduler = taskScheduler();
		taskRegistrar.setTaskScheduler(taskScheduler);
	}
	
	/**
	 * 异步任务 -- 设置异步线程池
	 */
	@Override
	public Executor getAsyncExecutor() {
		return asyncServiceExecutor();
	}
	
	/**
	 * 异步任务 异常处理
	 */
	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new SpringAsyncExceptionHandler();
	}
	
	static final class SpringAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
		@Override
		public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
			logger.error("异步方法执行异常, message {}, method {}, params {}", throwable, method, obj);
		}
	}
}

