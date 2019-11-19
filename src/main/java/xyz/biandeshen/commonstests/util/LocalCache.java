package xyz.biandeshen.commonstests.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author fjp
 * @Title: LiteCache
 * @ProjectName commons-tests
 * @Description: 带有过期时间的缓存
 * @date 2019/11/1816:24
 */
@SuppressWarnings("all")
public class LocalCache {
	/**
	 * 缓存最大个数
	 */
	private final Integer CACHE_MAX_NUMBER = 2 << 31;
	/**
	 * 过期队列大小
	 */
	private Integer CURRENT_SIZE = CACHE_MAX_NUMBER + 1;
	/**
	 * 过期时间,默认一分钟
	 */
	private Long EXPIRATION_TIME = TimeUnit.MINUTES.toMillis(1);
	
	/**
	 * 缓存大小
	 */
	private ConcurrentMap<String, Node> cache = new ConcurrentHashMap<>(CACHE_MAX_NUMBER);
	/**
	 * 到期队列大小
	 */
	private PriorityBlockingQueue<Node> expireQueue = new PriorityBlockingQueue<>(CURRENT_SIZE);
	
	/**
	 * 锁
	 */
	private ReentrantLock reentrantLock = new ReentrantLock();
	
	public LocalCache() {
		Runnable cacheCleaner = new SwapExpiredNodeWork();
		//执行定时清理任务
		new CacheAndExpirationQueueManagementThreadPool.Builder().build().scheduleAtFixedRate(cacheCleaner);
	}
	
	/**
	 * 设置缓存
	 */
	public Object set(String key, Object value, long cacheTime) {
		//判断缓存中是否存在
		Node node = new Node(key, value, (System.currentTimeMillis() + TimeUnit.MILLISECONDS.toMillis(cacheTime)));
		//加锁判断,锁定过期队列,缓存则使用原子操作
		reentrantLock.lock();
		try {
			Node putIfAbsent = cache.putIfAbsent(key, node);
			// 为空,则代表不存在,需在过期队列添加,过期队列的大小可能大于缓存
			expireQueue.put(node);
			if (putIfAbsent != null) {
				// 否则,代表缓存中已存在（已更新）过期队列中需要更新时间
				// 移除已有的
				expireQueue.remove(node);
			}
		} finally {
			//释放锁
			reentrantLock.unlock();
		}
		return node.value;
	}
	
	/**
	 * 设置缓存
	 */
	public Object set(String key, Object value) {
		return set(key, value, EXPIRATION_TIME);
	}
	
	/**
	 * 获取缓存
	 */
	public Object get(String key) {
		Node node = cache.get(key);
		// 加锁
		reentrantLock.lock();
		try {
			// 不为空,则更新
			if (node != null) {
				// 更新过期时间
				expireQueue.remove(node);
				// 延长过期时间
				node.expireTime = (System.currentTimeMillis() + TimeUnit.MILLISECONDS.toMillis(EXPIRATION_TIME));
				expireQueue.put(node);
			}
		} finally {
			reentrantLock.unlock();
		}
		return node == null ? null : node.value;
	}
	
	/**
	 * 移除缓存,并返回对应key的值
	 */
	public Object remove(String key) {
		Node node = cache.remove(key);
		//	加锁
		reentrantLock.lock();
		try {
			//	不为空,则移除
			if (node != null) {
				expireQueue.remove(node);
			}
		} finally {
			reentrantLock.unlock();
		}
		return node == null ? null : node.value;
	}
	
	/**
	 * 缓存及过期队列管理线程池
	 */
	@SuppressWarnings("all")
	static class CacheAndExpirationQueueManagementThreadPool {
		/**
		 * 缓存清理线程池名称
		 */
		private final String THREAD_POOL_NAME;
		/**
		 * 缓存清理线程池最小线程数
		 */
		private final int THREAD_POOL_CORESIZE;
		/**
		 * 缓存清理线程池初始延迟时间
		 * 指定时延后开始执行任务，以后每隔period的时长再次执行该任务
		 */
		private final Long THREAD_RATE_TIME;
		/**
		 * 每隔period的时长再次执行该任务
		 */
		private final Long THREAD_PERIOD_TIME;
		/**
		 * 线程工厂,为线程命名
		 */
		private final ThreadFactory namedThreadFactory;
		/**
		 * 计划执行者服务-过期管理线程池
		 */
		private final ScheduledExecutorService expirationManagementThreadPool;
		
		/**
		 * 清理线程是否设置为守护线程（默认为true）
		 */
		private final boolean THREAD_DEAMON;
		
		private CacheAndExpirationQueueManagementThreadPool(Builder builder) {
			this.THREAD_POOL_NAME = builder.THREAD_POOL_NAME;
			this.THREAD_POOL_CORESIZE = builder.THREAD_POOL_CORESIZE;
			this.THREAD_RATE_TIME = builder.THREAD_RATE_TIME;
			this.THREAD_PERIOD_TIME = builder.THREAD_PERIOD_TIME;
			this.THREAD_DEAMON = builder.THREAD_DEAMON;
			namedThreadFactory = new ThreadFactoryBuilder()
					                     .setNameFormat(THREAD_POOL_NAME).setDaemon(THREAD_DEAMON).build();
			expirationManagementThreadPool = new ScheduledThreadPoolExecutor(THREAD_POOL_CORESIZE, namedThreadFactory);
		}
		
		private void scheduleAtFixedRate(Runnable runnable) {
			expirationManagementThreadPool.scheduleAtFixedRate(runnable, THREAD_RATE_TIME, THREAD_PERIOD_TIME,
			                                                   TimeUnit.MILLISECONDS);
		}
		
		static class Builder {
			/**
			 * 缓存清理线程池名称
			 */
			private String THREAD_POOL_NAME = "cache-pool-%d";
			/**
			 * 缓存清理线程池最小线程数
			 */
			private int THREAD_POOL_CORESIZE = 10;
			/**
			 * 缓存清理线程池初始延迟时间（默认一分钟）
			 * 指定时延后开始执行任务，以后每隔period的时长再次执行该任务
			 */
			private Long THREAD_RATE_TIME = TimeUnit.MINUTES.toMillis(1);
			/**
			 * 每隔period的时长再次执行该任务（默认一分钟）
			 */
			private Long THREAD_PERIOD_TIME = TimeUnit.MINUTES.toMillis(1);
			/**
			 * 清理线程是否设置为守护线程（默认为true）
			 */
			private boolean THREAD_DEAMON = true;
			
			public Builder(String threadPoolName, int threadPoolCoreSize, Long threadRateTime,
			               Long threadPeriodTime, boolean deamon) {
				this.THREAD_POOL_NAME = threadPoolName;
				this.THREAD_POOL_CORESIZE = threadPoolCoreSize;
				this.THREAD_RATE_TIME = threadRateTime;
				this.THREAD_PERIOD_TIME = threadPeriodTime;
				this.THREAD_DEAMON = deamon;
			}
			
			public Builder() {
			}
			
			public Builder threadPoolName(String threadPoolName) {
				this.THREAD_POOL_NAME = threadPoolName;
				return this;
			}
			
			public Builder threadPoolCoreSize(int threadPoolCoreSize) {
				this.THREAD_POOL_CORESIZE = threadPoolCoreSize;
				return this;
			}
			
			public Builder threadRateTime(Long threadRateTime) {
				this.THREAD_RATE_TIME = threadRateTime;
				return this;
			}
			
			public Builder threadPeriodTime(Long threadPeriodTime) {
				this.THREAD_PERIOD_TIME = threadPeriodTime;
				return this;
			}
			
			public Builder deamon(boolean deamon) {
				this.THREAD_DEAMON = deamon;
				return this;
			}
			
			public CacheAndExpirationQueueManagementThreadPool build() {
				return new CacheAndExpirationQueueManagementThreadPool(this);
			}
		}
	}
	
	/**
	 * 删除已过期的数据
	 */
	class SwapExpiredNodeWork implements Runnable {
		@Override
		public void run() {
			long now = System.currentTimeMillis();
			while (true) {
				reentrantLock.lock();
				try {
					Node node = expireQueue.peek();
					//	数据为空或过期时间大于当前时间
					if (node == null || node.expireTime > now) {
						//直接退出循环
						return;
					}
					//	否则,移除
					cache.remove(node.key);
					expireQueue.poll();
					System.err.println(Thread.currentThread().getName() + "缓存清理!" + node.key);
				} finally {
					reentrantLock.unlock();
				}
			}
		}
		
	}
	
	/**
	 * 数据存储节点
	 */
	static class Node implements Comparable<Node> {
		// key
		private final String key;
		// value
		private final Object value;
		// 预计过期时间 : 当前时间 + 过期时间
		private long expireTime;
		
		Node(String key, Object value, long expireTime) {
			this.key = key;
			this.value = value;
			this.expireTime = expireTime;
		}
		
		
		@Override
		public boolean equals(Object object) {
			if (this == object) {
				return true;
			}
			if (object == null || getClass() != object.getClass()) {
				return false;
			}
			Node node = (Node) object;
			return expireTime == node.expireTime &&
					       key.equals(node.key) &&
					       value.equals(node.value);
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(key, value, expireTime);
		}
		
		@Override
		public int compareTo(Node node) {
			if (node != null) {
				return Math.toIntExact(this.expireTime - node.expireTime);
			}
			return -1;
		}
	}
	////先进先出实体
	//class FIFOEntry<E extends Comparable<? super E>> implements Comparable<FIFOEntry<E>> {
	//	final AtomicLong seq = new AtomicLong(0);
	//	final long seqNum;
	//	final E entry;
	//
	//	public FIFOEntry(E entry) {
	//		seqNum = seq.getAndIncrement();
	//		this.entry = entry;
	//	}
	//
	//	public E getEntry() {
	//		return entry;
	//	}
	//
	//	@Override
	//	public int compareTo(FIFOEntry<E> other) {
	//		int res = entry.compareTo(other.entry);
	//		if (res == 0 && other.entry != this.entry) {
	//			res = (seqNum < other.seqNum ? -1 : 1);
	//		}
	//		return res;
	//	}
	//}
	
	public static void main(String[] args) {
		Logger logger = LoggerFactory.getLogger(LocalCache.class);
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		LocalCache localCache = new LocalCache();
		localCache.EXPIRATION_TIME = TimeUnit.SECONDS.toMillis(60);
		executorService.execute(() -> {
			for (int i = 0; i < 1000000; i++) {
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
			for (int i = 0; i < 1000000; i++) {
				Object key = localCache.get(String.valueOf(i));
				logger.error("{} {}", Thread.currentThread().getName(), key);
			}
		});
		executorService.execute(() -> {
			for (int i = 0; i < 1000000; i++) {
				Object key = localCache.get(String.valueOf(i));
				logger.warn("{} {}", Thread.currentThread().getName(), key);
			}
		});
		executorService.shutdownNow();
	}
}
