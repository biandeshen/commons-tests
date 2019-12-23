package xyz.biandeshen.Java程序性能优化.proxy;

import javassist.CannotCompileException;
import javassist.NotFoundException;

/**
 * @author fjp
 * @Title: 动态代理性能检测
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/12/2310:44
 */
@SuppressWarnings("ALL")
// 实际使用过程中，调用频率往往高于生成频率，故生成频率应为重要指标
public class 动态代理性能比较 {
	public static final int CIRCLE = 30000000;
	
	public static void main(String[] args) throws
	                                       InstantiationException,
	                                       IllegalAccessException,
	                                       NotFoundException,
	                                       CannotCompileException {
		IDBQuery d = null;
		// 测试JDK动态代理
		long begin = System.currentTimeMillis();
		d = JdkDbQueryHandler.createJdkProxy();
		System.out.println("createJDKProxy: " + (System.currentTimeMillis() - begin) + "ms");
		System.out.println("JdkProxy Class: " + d.getClass().getName());
		begin = System.currentTimeMillis();
		for (int i = 0; i < CIRCLE; i++) {
			d.request();
		}
		System.out.println("callJdkProxy: " + (System.currentTimeMillis() - begin) + "ms");
		System.out.println();
		
		
		// 测试CGLIB动态代理
		begin = System.currentTimeMillis();
		d = CglibDbQueryInterceptor.createCglibProxy();
		System.out.println("createCglibProxy: " + (System.currentTimeMillis() - begin) + "ms");
		System.out.println("CglibProxy Class: " + d.getClass().getName());
		begin = System.currentTimeMillis();
		for (int i = 0; i < CIRCLE; i++) {
			d.request();
		}
		System.out.println("callCglibProxy: " + (System.currentTimeMillis() - begin) + "ms");
		System.out.println();
		
		// 测试Javassist动态代理
		begin = System.currentTimeMillis();
		d = JavassistDynDbQueryHandler.createJavassistDynProxy();
		System.out.println("createJavassistProxy: " + (System.currentTimeMillis() - begin) + "ms");
		System.out.println("JavassistProxy Class: " + d.getClass().getName());
		begin = System.currentTimeMillis();
		for (int i = 0; i < CIRCLE; i++) {
			d.request();
		}
		System.out.println("callJavassistProxy: " + (System.currentTimeMillis() - begin) + "ms");
		System.out.println();
		
		// 测试Javassist动态代理2
		begin = System.currentTimeMillis();
		d = JavassistDynDbQueryHandler.createJavassistBytecodeDynmicProxy();
		System.out.println("createJavassistBytecodeDynamicProxy: " + (System.currentTimeMillis() - begin) + "ms");
		System.out.println("JavassistBytecodeDynamicProxy Class: " + d.getClass().getName());
		begin = System.currentTimeMillis();
		for (int i = 0; i < CIRCLE; i++) {
			d.request();
		}
		System.out.println("callJavassistBytecodeDynamicProxy: " + (System.currentTimeMillis() - begin) + "ms");
	}
}
