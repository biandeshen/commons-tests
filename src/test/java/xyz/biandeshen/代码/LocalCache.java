package xyz.biandeshen.代码;


import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author fjp
 * @Title: LiteCache
 * @ProjectName commons-tests
 * @Description: 带有过期时间的缓存, 过期队列以延时队列实现
 * @date 2019/11/1816:24
 */
@SuppressWarnings("all")
public class LocalCache<K, V> {
	/**
	 * 缓存最大个数
	 */
	private final Integer CACHE_MAX_SIZE;
	/**
	 * 过期时间,默认一分钟
	 */
	private final Long EXPIRATION_TIME;
	
	/**
	 * 过期管理线程池
	 */
	private final CacheAndExpirationQueueManagementThreadPool cacheAndExpirationQueueManagementThreadPool;
	/**
	 * 缓存
	 */
	private final ConcurrentMap<K, Node> cache;
	/**
	 * 到期队列
	 */
	private final DelayQueue<Node> expireQueue;
	/**
	 * 锁
	 */
	private final ReentrantLock reentrantLock;
	
	
	private LocalCache(Builder builder) {
		//缓存及队列等参数赋值
		this.CACHE_MAX_SIZE = builder.CACHE_MAX_SIZE;
		this.EXPIRATION_TIME = builder.EXPIRATION_TIME;
		//缓存及过期队列初始化
		cache = new ConcurrentHashMap<>(CACHE_MAX_SIZE);
		expireQueue = new DelayQueue<Node>();
		reentrantLock = new ReentrantLock();
		
		//创建缓存及过期队列管理线程池
		this.cacheAndExpirationQueueManagementThreadPool =
				builder.cacheAndExpirationQueueManagementThreadPool;
	}
	
	/**
	 * 执行清理线程
	 */
	public void scheduleAtFixedRate() {
		//清理线程
		CleanExpiredNodeWorker cacheCleaner = new CleanExpiredNodeWorker();
		//执行定时清理任务
		cacheAndExpirationQueueManagementThreadPool.scheduleAtFixedRate(cacheCleaner);
	}
	
	public static class Builder {
		/**
		 * 缓存最大个数
		 */
		private Integer CACHE_MAX_SIZE = 2 << 16;
		/**
		 * 过期时间,默认一分钟
		 */
		private Long EXPIRATION_TIME = TimeUnit.MINUTES.toMillis(1);
		/**
		 * 过期管理线程池
		 */
		private CacheAndExpirationQueueManagementThreadPool cacheAndExpirationQueueManagementThreadPool =
				new CacheAndExpirationQueueManagementThreadPool.Builder().build();
		
		public Builder() {
		
		}
		
		/**
		 * LocalCache参数设置
		 *
		 * @param cacheMaxSize
		 * 		缓存大小
		 * @param expiredQueueSize
		 * 		过期队列大小
		 * @param expriationTime
		 * 		过期时间(即保存缓存时长)
		 * @param threadPool
		 * 		过期管理线程池
		 */
		public Builder(Integer cacheMaxSize, Long expriationTime,
		               CacheAndExpirationQueueManagementThreadPool threadPool) {
			this.CACHE_MAX_SIZE = cacheMaxSize;
			this.EXPIRATION_TIME = expriationTime;
			this.cacheAndExpirationQueueManagementThreadPool = threadPool;
		}
		
		public Builder cacheMaxSize(Integer cacheMaxSize) {
			this.CACHE_MAX_SIZE = cacheMaxSize;
			return this;
		}
		
		public Builder expriationTime(Long expriationTime) {
			this.EXPIRATION_TIME = expriationTime;
			return this;
		}
		
		public Builder threadPool(CacheAndExpirationQueueManagementThreadPool threadPool) {
			this.cacheAndExpirationQueueManagementThreadPool = threadPool;
			return this;
		}
		
		public LocalCache build() {
			return new LocalCache(this);
		}
		
	}
	
	/**
	 * 设置缓存
	 */
	public V set(K key, V value, long cacheTime) {
		//判断缓存中是否存在
		Node node = new Node(key, value, (System.currentTimeMillis() + TimeUnit.MILLISECONDS.toMillis(cacheTime)));
		//加锁判断,锁定过期队列,缓存则使用原子操作
		reentrantLock.lock();
		Node putIfAbsent;
		try {
			putIfAbsent = cache.putIfAbsent(key, node);
			// 为空,则代表不存在,需在过期队列添加,过期队列的大小可能大于缓存
			expireQueue.put(node);
			if (putIfAbsent != null) {
				// 否则,代表缓存中已存在（已更新）过期队列中需要更新时间
				// 移除已有的
				expireQueue.remove(node);
				if (putIfAbsent != node) {
					// 上面putifabsent判断的是key是否相同
					// 此处表示将 key 的值进行替换 (若旧值与新值不同)
					cache.replace(key, putIfAbsent, node);
				}
			}
		} finally {
			//释放锁
			reentrantLock.unlock();
		}
		return (V) cache.get(key).value == null ? null : (V) cache.get(key).value;
	}
	
	/**
	 * 设置缓存
	 */
	public V set(K key, V value) {
		return set(key, value, EXPIRATION_TIME);
	}
	
	/**
	 * 获取缓存
	 */
	public V getAndDelay(String key) {
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
		return node == null ? null : (V) node.value;
	}
	
	/**
	 * 获取缓存
	 */
	public V get(String key) {
		if (key == null) {
			return null;
		}
		return cache.get(key) == null ? null : (V) cache.get(key).value;
	}
	
	/**
	 * 获取缓存
	 */
	public synchronized List<V> getAll() {
		Collection<Node> values = cache.values();
		ArrayList<Node> nodes = new ArrayList<>(values);
		ArrayList<V> valueList = new ArrayList<>(nodes.size());
		for (Node node : nodes) {
			valueList.add((V) node.value);
		}
		return valueList;
	}
	
	/**
	 * 移除缓存,并返回对应key的值
	 */
	public V remove(String key) {
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
		return node == null ? null : (V) node.value;
	}
	
	public int size() {
		return cache.size();
	}
	
	/**
	 * 缓存及过期队列管理线程池
	 */
	@SuppressWarnings("all")
	public static class CacheAndExpirationQueueManagementThreadPool {
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
		
		public static class Builder {
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
			
			/**
			 * 缓存清理线程池参数设置
			 *
			 * @param threadPoolName
			 * 		线程池名
			 * @param threadPoolCoreSize
			 * 		线程池大小
			 * @param threadRateTime
			 * 		缓存清理线程池初始延迟时间（默认一分钟）
			 * @param threadPeriodTime
			 * 		每隔period的时长再次执行该任务（默认一分钟）
			 * @param deamon
			 * 		清理线程是否设置为守护线程（默认为true）
			 */
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
	 * 删除已过期的数据(默认实现)
	 */
	private class CleanExpiredNodeWorker implements Runnable {
		@Override
		public void run() {
			long now = System.currentTimeMillis();
			while (true) {
				//reentrantLock.lock();
				try {
					Node node = expireQueue.take();
					//	否则,移除
					if (node != null) {
						cache.remove(node.key);
					}
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				} /*finally {
					reentrantLock.unlock();
				}*/
			}
		}
		
	}
	
	/**
	 * 数据存储节点
	 */
	private static class Node<K, V> implements Delayed {
		// key
		private final K key;
		// value
		private final V value;
		// 预计过期时间 : 当前时间 + 过期时间
		private long expireTime;
		
		Node(K key, V value, long expireTime) {
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
		public long getDelay(TimeUnit unit) {
			return expireTime - System.currentTimeMillis();
		}
		
		@Override
		public int compareTo(Delayed delayed) {
			Node node = (Node) delayed;
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
}
