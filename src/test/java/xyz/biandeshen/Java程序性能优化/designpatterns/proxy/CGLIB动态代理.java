package xyz.biandeshen.Java程序性能优化.designpatterns.proxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author fjp
 * @Title: CGLIB动态代理
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/12/239:33
 */
public class CGLIB动态代理 {
	public static void main(String[] args) {
		IDBQuery idbQuery = CglibDbQueryInterceptor.createCglibProxy();
		idbQuery.request();
		// 判断是否初始化 初始化时间 初始化顺序
		System.out.println("开始执行代理类！");
		System.out.println("idbQuery.request() = " + idbQuery.request());
	}
}

class CglibDbQueryInterceptor implements MethodInterceptor {
	IDBQuery real = null;
	
	@Override
	public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
		if (real == null) {
			real = new DBQuery();
		}
		return real.request();
	}
	
	public static IDBQuery createCglibProxy() {
		Enhancer enhancer = new Enhancer();
		// 指定切入，定义代理类逻辑
		enhancer.setCallback(new CglibDbQueryInterceptor());
		// 指定实现的接口
		enhancer.setInterfaces(new Class[]{IDBQuery.class});
		//	生成代理类的实例
		IDBQuery cglibProxy = (IDBQuery) enhancer.create();
		return cglibProxy;
	}
}