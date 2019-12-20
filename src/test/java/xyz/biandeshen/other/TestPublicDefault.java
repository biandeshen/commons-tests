package xyz.biandeshen.other;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author fjp
 * @Title: TestPublicDefault
 * @ProjectName commons-tests
 * @Description: 1.8 特性 接口中 定义方法 可以是 public default(static)的形式,然后带方法体;
 * https://www.jianshu.com/p/17ca28bf8f15
 * https://www.oschina.net/question/12_65077
 * @date 2019/11/2116:19
 */
public class TestPublicDefault implements A, B {
	@Override
	public void a() {
	}
	
	static D makeD() { return () -> { System.out.println("X"); }; }
	
	static D d1 = makeD();
	static D d2 = makeD();
	
	
	public static void main(String[] args) {
		//TestPublicDefault test = new TestPublicDefault();
		//test.a();
		//A.b();
		//System.out.println("A.a = " + A.a);
		//B.b();
		
		
		d1.setName("d1");
		d2.setName("d2");
		System.out.println("d1.getName() = " + d1.getName());
		System.out.println("d2.getName() = " + d2.getName());
		
	}
	
	
}


interface A {
	public int a = 0;
	
	public default void a() {
		System.out.println("\"a\" = " + "a");
	}
	
	public static void b() {
		System.out.println("\"ab\" = " + "ab");
	}
	
}

interface B {
	public default void a() {
		System.out.println("\"b\" = " + "b");
	}
	
	public static void b() {
		System.out.println("\"bb\" = " + "bb");
	}
}

interface C {
	public default void c() {
		System.out.println("\"c\" = " + "c");
	}
	
	public static void cc() {
		System.out.println("\"cc\" = " + "cc");
	}
}

interface FakeBrokenMixin {
	Map<FakeBrokenMixin, String> backingMap
			= Collections.synchronizedMap(new WeakHashMap<>());
	
	default String getName() { return backingMap.get(this); }
	
	default void setName(String name) { backingMap.put(this, name); }
}

interface D extends Runnable, FakeBrokenMixin {
}
