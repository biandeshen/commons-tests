package xyz.biandeshen.Java程序性能优化.designpatterns;


import lombok.Data;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author fjp
 * @Title: PoolTest
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/12/2611:02
 */
// 对象池 动态代理 EHCache
public class TestPool {
	private static final Logger logger = LoggerFactory.getLogger(TestPool.class);
	
	// 消除受查异常
	@SuppressWarnings("all")
	private static <T extends Throwable> void throwAs(Throwable e) throws T {
		throw (T) e;
	}
	
	public static void main(String[] args) throws InterruptedException {
		// 创建池对象工厂
		//PooledObjectFactory<Resource> factory = new MyPoolableObjectFactory<Resource>() {
		//	@Override
		//	protected Resource init() {
		//		return CGLIBResourceProxy.createCglibProxy();
		//	}
		//};
		PooledObjectFactory<Resource> factory = new MyPoolableObjectFactory2();
		// 对象池配置
		GenericObjectPoolConfig<Resource> poolConfig = new GenericObjectPoolConfig<>();
		// 最大空闲数
		poolConfig.setMaxIdle(5);
		// 最小空闲数, 池中只有一个空闲对象的时候，池会在创建一个对象，并借出一个对象，从而保证池中最小空闲数为1
		poolConfig.setMinIdle(1);
		// 最大池对象总数
		poolConfig.setMaxTotal(20);
		// 逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
		poolConfig.setMinEvictableIdleTimeMillis(1800000);
		// 逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
		poolConfig.setTimeBetweenEvictionRunsMillis(1800000 * 2L);
		// 在获取对象的时候检查有效性, 默认false
		poolConfig.setTestOnBorrow(true);
		// 在归还对象的时候检查有效性, 默认false
		poolConfig.setTestOnReturn(false);
		// 在空闲时检查有效性, 默认false
		poolConfig.setTestWhileIdle(false);
		// 最大等待时间， 默认的值为-1，表示无限等待。
		poolConfig.setMaxWaitMillis(5000);
		// 是否启用后进先出, 默认true
		poolConfig.setLifo(true);
		// 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
		poolConfig.setBlockWhenExhausted(true);
		// 每次逐出检查时 逐出的最大数目 默认3
		poolConfig.setNumTestsPerEvictionRun(3);
		
		// 创建对象池
		GenericObjectPool<Resource> objectPool = new GenericObjectPool<>(factory, poolConfig);
		
		// 线程池
		//ExecutorService executor = Executors.newFixedThreadPool(20);
		//ExecutorService executor = new ThreadPoolExecutor(0, 20, 0, TimeUnit.MILLISECONDS,
		//                                                  new LinkedBlockingQueue<>(16 << 2),
		//                                                  new ThreadFactoryBuilder().setNameFormat("Executor")
		//                                                  .setDaemon(false).build());
		ExecutorService executor = new ThreadPoolExecutor(20, 20, 0, TimeUnit.MILLISECONDS,
		                                                  new LinkedBlockingQueue<>(16 << 2));
		try {
			for (int i = 0; i < 20; i++) {
				executor.execute(() -> {
					long begin = System.currentTimeMillis();
					Resource resource = null;
					try {
						resource = objectPool.borrowObject();
						//System.out.println("resource = " + System.identityHashCode(resource));
					} catch (Exception e) {
						TestPool.throwAs(e);
					}
					for (int j = 0; j < 1000000; j++) {
						resource.factor(2147483646);
						//System.out.println("factor = " + factor);
					}
					logger.info("spend = " + (System.currentTimeMillis() - begin));
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		executor.awaitTermination(0, TimeUnit.SECONDS);
		executor.shutdown();
		
		
	}
}


// 任意对象
@Data
class Resource {
	private int id;
	private String name;
	private String desc;
	
	// 需要缓存结果的方法
	public String factor(int num) {
		StringBuilder sb = new StringBuilder();
		for (int i = 2; i < Math.sqrt(num); i++) {
			if (num % i == 0) {
				sb.append(i).append("*");
				num /= i;
				i--;
			}
		}
		sb.append(num);
		return sb.toString();
	}
	
	//@Override
	//public String toString() {
	//	final StringBuilder sb;
	//	sb = new StringBuilder();
	//	sb.append('{');
	//	sb.append("\"id\":")
	//			.append(id);
	//	sb.append(",\"name\":\"")
	//			.append(name).append('\"');
	//	sb.append(",\"desc\":\"")
	//			.append(desc).append('\"');
	//	sb.append('}');
	//	return sb.toString();
	//}
}

// 池对象 工厂类
//class MyPoolableObjectFactory<T> extends BasePooledObjectFactory<T> {
//	protected T init() {
//		return null;
//	}
//
//	// 创建一个对象实例
//	@Override
//	public T create() throws Exception {
//		//return new Resource();
//		//return (T) CGLIBResourceProxy.createCglibProxy();
//		return init();
//	}
//
//	@Override
//	public PooledObject<T> wrap(T t) {
//		return new DefaultPooledObject<>(t);
//	}
//}


class MyPoolableObjectFactory2 extends BasePooledObjectFactory<Resource> {
	
	// 创建一个对象实例
	@Override
	public Resource create() throws Exception {
		//return new Resource();
		return CGLIBResourceProxy.createCglibProxy();
		//return init();
	}
	
	@Override
	public PooledObject<Resource> wrap(Resource t) {
		return new DefaultPooledObject<>(t);
	}
	
	// 对象销毁前调用
	@Override
	public void destroyObject(PooledObject<Resource> p) throws Exception {
		super.destroyObject(p);
	}
	
	// 对象校验时调用
	@Override
	public boolean validateObject(PooledObject<Resource> p) {
		return super.validateObject(p);
	}
	
	// 对象返回池中被调用
	@Override
	public void passivateObject(PooledObject<Resource> p) throws Exception {
		super.passivateObject(p);
	}
	
	// 对象取出前被调用
	@Override
	public void activateObject(PooledObject<Resource> p) throws Exception {
		super.activateObject(p);
	}
}

class CGLIBResourceProxy implements MethodInterceptor {
	private Resource resource = new Resource();
	
	@Override
	// 动态代理拦截器中增加 缓存
	public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
		if (o instanceof Resource) {
			if (method.getName().equals("factor")) {
				// 查询缓存
				String value = (String) EhcacheUtil.get("myCache", String.valueOf(objects[0]));
				if (value == null) {
					// 查询未找到
					value = resource.factor((Integer) objects[0]);
					// 保存计算结果
					EhcacheUtil.put("myCache", String.valueOf(objects[0]), value);
				}
				return value;
			}
		}
		
		return null;
	}
	
	// 返回代理类
	public static Resource createCglibProxy() {
		Enhancer enhancer = new Enhancer();
		// 指定切入，定义代理类逻辑
		enhancer.setCallback(new CGLIBResourceProxy());
		// 指定实现的接口
		//enhancer.setInterfaces(new Class[]{IDBQuery.class});
		enhancer.setSuperclass(Resource.class);
		//	生成代理类的实例
		return (Resource) enhancer.create();
	}
}