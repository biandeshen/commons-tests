package xyz.biandeshen.proxy;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author fjp
 * @Title: TraceHandlerTest
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/8/811:05
 */
public class TraceHandlerTest {
	@Mock
	Object target;
	@InjectMocks
	TraceHandler traceHandler;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testInvoke() throws Exception {
		Class<?> proxyClass = Proxy.getProxyClass(null, Comparable.class, Cloneable.class);
		System.out.println("proxyClass = " + proxyClass);
		Constructor<?> constructor = proxyClass.getConstructor(InvocationHandler.class);
		Object aaa = constructor.newInstance(new TraceHandler("AAA"));
		System.out.println("aaa = " + aaa);
		Arrays.sort(new Object[]{"aaa", "BBB", aaa});
		//System.out.println();
		
		Object[] elments = new Object[1000];
		for (int i = 0; i < elments.length; i++) {
			Integer val = i + 1;
			InvocationHandler handler1 = new TraceHandler(val);
			Object proxy = Proxy.newProxyInstance(null, new Class[]{Comparable.class}, handler1);
			elments[i] = proxy;
		}
		Integer key = new Random().nextInt(elments.length) + 1;
		int result = Arrays.binarySearch(elments, key);
		if (result >= 0) {
			System.out.println("elments[result] = " + elments[result]);
		}
	}
	
	@Test
	public void testArray() {
		List<Object> list = new ArrayList<>();
		list.add(0);
		list.add(1);
		list.add(2);
		list.forEach(System.out::println);
		changeList(list);
		list.forEach(System.out::println);
	}
	
	private static void changeList(List<Object> list) {
		list.set(0, 9);
	}
	
	
	@Test
	public void testPrint() throws Exception {
		//添加这一句是生成代理类的class文件，前提是你需要在工程根目录下创建com/sun/proxy目录
		System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
		InvocationHandler handler = new TraceHandler(new PrintTest(0, 0));
		Object proxy0 = Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{PrintInterface.class}, handler);
		Object[] printTests = new Object[1];
		printTests[0] = proxy0;
		PrintInterface printInterface = (PrintInterface) printTests[0];
		printInterface.output(proxy0);
	}
	
	public static void main(String[] args) {
		//添加这一句是生成代理类的class文件，前提是你需要在工程根目录下创建com/sun/proxy目录
		System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
		InvocationHandler handler = new TraceHandler(new PrintTest(0, 0));
		Object proxy0 = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{PrintInterface.class}, handler);
		Object[] printTests = new Object[1];
		printTests[0] = proxy0;
		PrintInterface printInterface = (PrintInterface) printTests[0];
		printInterface.output(proxy0);
	}
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme