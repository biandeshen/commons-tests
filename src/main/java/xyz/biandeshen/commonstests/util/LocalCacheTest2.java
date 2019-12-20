package xyz.biandeshen.commonstests.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.biandeshen.commonstests.util.LocalCache2.CacheAndExpirationQueueManagementThreadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author fjp
 * @Title: LocalCacheTest
 * @ProjectName commons-tests
 * @Description: LocalCacheTest
 * @date 2019/11/209:42
 */
@SuppressWarnings("all")
public class LocalCacheTest2 {
	public static void main(String[] args) throws InterruptedException {
		Logger logger = LoggerFactory.getLogger(LocalCacheTest2.class);
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		//LocalCache localCache =
		//		new LocalCache.Builder().expriationTime(TimeUnit.MINUTES.toMillis(1)).threadPool(
		//				new CacheAndExpirationQueueManagementThreadPool.Builder().deamon(true).build()).build();
		/*
		 LocalCache 使用示例
		 */
		LocalCache2 localCache = new LocalCache2.Builder(1024, TimeUnit.MINUTES.toMillis(1),
		                                                 new CacheAndExpirationQueueManagementThreadPool.Builder(
				                                                 "clean-cache-pool",
				                                                 10,
				                                                 TimeUnit.MINUTES.toMillis(1),
				                                                 TimeUnit.MINUTES.toMillis(1),
				                                                 true)
				                                                 .build()).build();
		//执行定时清理线程
		localCache.scheduleAtFixedRate();
		
		executorService.execute(() -> {
			for (int i = 0; i < 10000; i++) {
				Object set = localCache.set(String.valueOf(i), RandomStringUtils.random(10));
				logger.info("{} {}", Thread.currentThread().getName(), set);
			}
		});
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		executorService.execute(() -> {
			for (int i = 0; i < 10000; i++) {
				Object key = localCache.get(String.valueOf(i));
				logger.error("{} {}", Thread.currentThread().getName(), key);
			}
		});
		executorService.execute(() -> {
			for (int i = 0; i < 10000; i++) {
				Object key = localCache.get(String.valueOf(i));
				logger.warn("{} {}", Thread.currentThread().getName(), key);
			}
		});
		executorService.awaitTermination(10, TimeUnit.SECONDS);
		executorService.shutdownNow();
	}
}
