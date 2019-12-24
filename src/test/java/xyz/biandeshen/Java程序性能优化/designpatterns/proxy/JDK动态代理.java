package xyz.biandeshen.Java程序性能优化.designpatterns.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author fjp
 * @Title: JDK动态代理
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/12/2011:31
 */
public class JDK动态代理 {
	public static void main(String[] args) {
		IDBQuery idbQuery = JdkDbQueryHandler.createJdkProxy();
		// 判断是否初始化 初始化时间 初始化顺序
		System.out.println("开始执行代理类！");
		System.out.println("idbQuery.request() = " + idbQuery.request());
		
		// 执行结果：
		// 开始执行代理类！
		// 初始化开始。。。
		// 初始化已完成！
		// idbQuery.request() = TheRealTheme
		
		
		// 测试proxy
		//System.out.println();
		//IBase iBase = JdkDbQueryHandler.createJdkProxy2();
		//// 判断是否初始化 初始化时间 初始化顺序
		//System.out.println("开始执行代理类！");
		//iBase.test();
	}
}

// 对比静态代理类 DBQueryProxy
// 实现了一个 IDBQuery接口的代理类，内部逻辑由此实现
// 使用这个handler先尝试生成真实主题对象，再生成动态代理对象,即 类中的 createJDKProxy方法
// 生成代理类后,由newProxyInstance()方法返回该代理类的一个实例
class JdkDbQueryHandler implements InvocationHandler {
	
	private IDBQuery real = null;
	// proxy 即动态代理对象，可以返回，用来进行操作，还可以据此继续对动态代理对象进行代理
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (real == null) {
			real = new DBQuery();
		}
		if (method.getName().equals("request")) {
			//System.out.println("proxy.getClass().toString() = " + proxy.getClass().toString());
			//System.out.println("proxy.getClass().getName() = " + proxy.getClass().getName());
			//System.out.println("method.getName() = " + method.getName());
			return real.request();
		}
		//if (proxy instanceof IBase && method.getName().equals("test")) {
		//	System.out.println("proxy = ");
		//	method.invoke(new Base(), args);
		//	return proxy;
		//}
		return null;
	}
	
	public static IDBQuery createJdkProxy() {
		return (IDBQuery) Proxy.newProxyInstance(
				ClassLoader.getSystemClassLoader(),
				new Class[]{IDBQuery.class},
				new JdkDbQueryHandler()
		);
	}
	
	public static IBase createJdkProxy2() {
		return (IBase) Proxy.newProxyInstance(
				ClassLoader.getSystemClassLoader(),
				new Class[]{IBase.class},
				new JdkDbQueryHandler()
		);
	}
	
}

interface IBase {
	IBase test();
}

class Base implements IBase {
	
	@Override
	public IBase test() {
		System.out.println("test...");
		return this;
	}
}
