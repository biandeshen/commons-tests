package xyz.biandeshen.Java程序性能优化.designpatterns;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

/**
 * @author fjp
 * @Title: EHCache
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/12/2614:22
 */
// EhCache
public class EhCache {
	public static void main(String[] args) {
		init();
	}
	
	public static void init() {
		CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
				                            .withCache("preConfigured",
				                                       CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
				                                                                                              ResourcePoolsBuilder.heap(100))
						                                       .build())
				                            .build(true);
		
		Cache<Long, String> preConfigured
				= cacheManager.getCache("preConfigured", Long.class, String.class);
		
		Cache<Long, String> myCache = cacheManager.createCache("myCache",
		                                                       CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
		                                                                                                              ResourcePoolsBuilder.heap(100)).build());
		
		myCache.put(1L, "da one!");
		String value = myCache.get(1L);
		System.out.println("value = " + value);
		cacheManager.close();
		
	}
}
