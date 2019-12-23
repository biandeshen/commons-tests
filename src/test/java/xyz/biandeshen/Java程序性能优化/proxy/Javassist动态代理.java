package xyz.biandeshen.Java程序性能优化.proxy;

import javassist.*;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import javassist.util.proxy.ProxyObject;

import java.lang.reflect.Method;

/**
 * @author fjp
 * @Title: JavassistDynDbQueryHandler
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/12/239:47
 */
@SuppressWarnings("ALL")
public class Javassist动态代理 {
	public static void main(String[] args) {
		IDBQuery idbQuery = null;
		try {
			idbQuery = JavassistDynDbQueryHandler.createJavassistDynProxy();
			idbQuery.request();
			// 判断是否初始化 初始化时间 初始化顺序
			System.out.println("开始执行代理类！");
			System.out.println("idbQuery.request() = " + idbQuery.request());
		} catch (IllegalAccessException | InstantiationException e) {
			e.printStackTrace();
		}
		
		System.out.println();
		IDBQuery idbQuery2 = null;
		try {
			idbQuery2 = JavassistDynDbQueryHandler.createJavassistBytecodeDynmicProxy();
			idbQuery2.request();
			// 判断是否初始化 初始化时间 初始化顺序
			System.out.println("开始执行代理类！");
			System.out.println("idbQuery2.request() = " + idbQuery2.request());
		} catch (IllegalAccessException | InstantiationException | CannotCompileException | NotFoundException e) {
			e.printStackTrace();
		}
		
	}
}

@SuppressWarnings("ALL")
class JavassistDynDbQueryHandler implements MethodHandler {
	private IDBQuery real = null;
	
	@Override
	public Object invoke(Object o, Method method, Method method1, Object[] objects) throws Throwable {
		if (real == null) {
			real = new DBQuery();
		}
		return real.request();
	}
	
	// 使用代理工厂创建 生成动态代理
	public static IDBQuery createJavassistDynProxy() throws IllegalAccessException, InstantiationException {
		ProxyFactory proxyFactory = new ProxyFactory();
		// 指定接口
		proxyFactory.setInterfaces(new Class[]{IDBQuery.class});
		//
		Class proxyClass = proxyFactory.createClass();
		IDBQuery javassistProxy = (IDBQuery) proxyClass.newInstance();
		// 设置handler处理器
		((ProxyObject) javassistProxy).setHandler(new JavassistDynDbQueryHandler());
		return javassistProxy;
	}
	
	
	// 使用动态代码创建 生成动态代理
	// 参考链接  https://blog.csdn.net/ShuSheng0007/article/details/81269295
	public static IDBQuery createJavassistBytecodeDynmicProxy() throws
	                                                            NotFoundException,
	                                                            CannotCompileException,
	                                                            IllegalAccessException,
	                                                            InstantiationException {
		// 基于hashMap实现的CtClass对象容器,其中键是类名称,值时表示该类的CtClass对象
		// 默认的 ClassPool 使用与底层JVM相同的类路径，因此在某些情况下，可能需要向ClassPool添加类路径或类字节
		ClassPool mPool = new ClassPool(true);
		// 定义类名
		CtClass mCtc = mPool.makeClass(IDBQuery.class.getName() + "JavassistBytcodeProxy");
		// 需要实现的接口
		mCtc.addInterface(mPool.get(IDBQuery.class.getName()));
		// 添加构造函数
		mCtc.addConstructor(CtNewConstructor.defaultConstructor(mCtc));
		// 添加类的字段信息，使用动态Java代码
		mCtc.addField(CtField.make("public " + IDBQuery.class.getName() + " real;", mCtc));
		String dbqueryname = DBQuery.class.getName();
		// 添加方法，这里使用动态Java指定内部逻辑
		mCtc.addMethod(CtNewMethod.make("public String request() {if(real == null){real = new " + dbqueryname + "();" +
				                                "}return real.request();}", mCtc));
		// 基于以上信息，生成动态类
		Class pc = mCtc.toClass();
		// 生成动态类的实例
		return (IDBQuery) pc.newInstance();
	}
}