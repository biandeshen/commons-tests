package xyz.biandeshen.Java程序性能优化.designpatterns;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

/**
 * @author fjp
 * @Title: EhcacheUtil
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/12/2614:26
 */
public class EhcacheUtil {
	private static CacheManager cacheManager;
	//private static Cache<String, String> myCache;
	
	static {
		cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
				               .withCache("preConfigured",
				                          CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class,
				                                                                                 String.class,
				                                                                                 ResourcePoolsBuilder.heap(100))
						                          .build())
				               .build(true);
		
		Cache<Long, String> preConfigured
				= cacheManager.getCache("preConfigured", Long.class, String.class);
		
		/*myCache = */
		cacheManager.createCache("myCache",
		                         CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class,
		                                                                                String.class,
		                                                                                ResourcePoolsBuilder.heap(100)).build());
	}
	
	@SuppressWarnings("unchecked")
	public static void put(String cacheName, String key, String value) {
		Cache cache = cacheManager.getCache(cacheName, key.getClass(), String.class);
		cache.putIfAbsent(key, value);
	}
	
	@SuppressWarnings("unchecked")
	public static Object get(String cacheName, String key) {
		Cache cache = cacheManager.getCache(cacheName, key.getClass(), String.class);
		return cache.get(key);
	}
	
	public static void close() {
		cacheManager.close();
	}
}
