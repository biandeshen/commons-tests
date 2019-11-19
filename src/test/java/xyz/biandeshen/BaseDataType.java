package xyz.biandeshen;

import org.junit.Test;
import org.omg.CORBA.IntHolder;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * @author fjp
 * @Title: BaseDataType
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/7/99:51
 */
public class BaseDataType {
	
	@Test
	public void testInt_$() {
		int[] ints1 = {1, 2, 3, 4, 5, 6};
		int[] ints2;
		ints2 = ints1;
		ints2[0] = 0;
		
		
		System.out.println("ints1[0] = " + ints1[0]);
		System.out.println("ints2[0] = " + ints2[0]);
		//ints2 = mvInts(ints1);
		System.out.println("ints1[0] = " + ints1[0]);
		System.out.println("ints2[0] = " + ints2[0]);
		//System.out.println("Math.PI = " + Math.PI);
	}
	
	@Test
	public void systemSetOut() throws FileNotFoundException {
		OutputStream out = new FileOutputStream(new File("E://日志//slog.log"));
		PrintStream ps = new PrintStream(out);
		System.setOut(ps);
		System.out.println("test System.setOut()...Start: ");
	}
	
	@Test
	public void systemSetIn() throws FileNotFoundException {
		InputStream in = new FileInputStream(new File("E://日志//slog.log"));
		System.setIn(in);
		InputStream inputStream = System.in;
		Scanner scanner = new Scanner(inputStream);
		String line;
		while (scanner.hasNext()) {
			line = scanner.nextLine();
			System.out.println(line);
		}
		//System.out.println("test System.setIn()...Start: ");
	}
	
	/**
	 * @see <a href="www.baidu.com">百度一下,你就知道!</a>
	 */
	@Test
	public void testConstract() {
		B b = new B();
		System.out.println();
		A b2 = new B();
		System.out.println();
	}
	
	@Test
	public void testSAndSB() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException {
		String s = "Ok";
		StringBuilder sb = new StringBuilder(s);
		System.out.println(s.hashCode() + " " + sb.hashCode());
		String t = new String("Ok");
		StringBuilder tb = new StringBuilder(t);
		System.out.println(t.hashCode() + " " + tb.hashCode());
		
		int i = 3;
		Integer j = i;
		IntHolder intHolder = new IntHolder(4);
		triple(intHolder);
		System.out.println("intHolder.valuie = " + intHolder.value);
		triple1(j);
		System.out.println("j = " + j);
		System.out.println("i = " + i);
		Integer[] integers = new Integer[]{1, 2, 3, 4, 5};
		System.out.println("integers[0] = " + integers[0].toString());
		triple2(integers);
		System.out.println("integers[0] = " + integers[0]);
		
		System.out.println("Math.PI = " + Math.PI);
		System.out.printf("%24s", Math.PI);
		
		System.out.println();
		Class c1 = B.class;
		System.out.println("c1.getName() = " + c1.getName());
		
		System.out.println("integers = " + integers.getClass().getName());
		
		//try {
		//	Object newInstance = c1.newInstance();
		//} catch (InstantiationException e) {
		//	e.printStackTrace();
		//} catch (IllegalAccessException e) {
		//	e.printStackTrace();
		//}
		B b = new B();
		System.out.println("Modifier.toString(c1.getModifiers()) = " + Modifier.toString(c1.getConstructor().getModifiers()));
		
		//c1.getComponentType().isPrimitive();
		System.out.println("b.getClass().isPrimitive() = " + b.getClass().isPrimitive());
	}
	
	
	public void reviewNewInstanceByConstructMethodTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException {
		Class c2 = B.class;
		@SuppressWarnings("all") Constructor c = c2.getConstructor(String.class);
		Constructor[] constructors = c2.getConstructors();
		for (Constructor constructor : constructors) {
			System.out.println("start ==============================================");
			//构造方法名称
			System.out.println("constructor.getName() = " + constructor.getName());
			//构造方法名称及构造参数类型
			System.out.println("Arrays.toString(constructor.getParameters()) = " + Arrays.toString(constructor.getParameters()));
			//有参构造方法newInstance
			if (constructor.getParameters().length > 0) {
				for (Parameter parameter : constructor.getParameters()) {
					//判断构造参数为String
					if (parameter.getType().equals(String.class)) {
						B b = (B) c.newInstance("ceshi");
						System.err.println("b.getDesc() = " + b.getDesc());
					}
				}
			}
			//默认无参构造方法newInstance
			else {
				B b = (B) Class.forName(c2.getName()).newInstance();
				System.err.println("b.getDesc() = " + b.getDesc());
			}
			System.out.println("end ================================================");
		}
	}
	
	public static void triple(IntHolder intHolder) {
		intHolder.value = intHolder.value * 3;
	}
	
	public static void triple1(Integer integer) {
		integer = integer * 3;
	}
	
	public static void triple2(Integer[] integer) {
		integer[0] = 7;
	}
}


class ReviewConstruct {
	public void reviewNewInstanceByConstructMethodTest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException {
		Class c2 = B.class;
		@SuppressWarnings("all") Constructor c = c2.getConstructor(String.class);
		Constructor[] constructors = c2.getConstructors();
		for (Constructor constructor : constructors) {
			System.out.println("start ==============================================");
			//构造方法名称
			System.out.println("constructor.getName() = " + constructor.getName());
			//构造方法名称及构造参数类型
			System.out.println("Arrays.toString(constructor.getParameters()) = " + Arrays.toString(constructor.getParameters()));
			//有参构造方法newInstance
			if (constructor.getParameters().length > 0) {
				for (Parameter parameter : constructor.getParameters()) {
					//判断构造参数为String
					if (parameter.getType().equals(String.class)) {
						B b = (B) c.newInstance("ceshi");
						System.err.println("b.getDesc() = " + b.getDesc());
					}
				}
			}
			//默认无参构造方法newInstance
			else {
				B b = (B) Class.forName(c2.getName()).newInstance();
				System.err.println("b.getDesc() = " + b.getDesc());
			}
			System.out.println("end ================================================");
		}
	}
	
	
}

class A {
	static {
		System.out.println("I am A");
	}
	
	{
		System.out.println("I am A1");
	}
	
	A() {
	
	}
	
}

class B extends A {
	private String name;
	
	static {
		System.out.println("I am B");
	}
	
	public B() {
		{
			System.out.println("I am B1");
		}
	}
	
	public B(String name) {
		this.name = name;
		System.out.println("name = " + name);
	}
	
	public String getDesc() {
		return "测试构造器newInstance" + " \t " + name;
	}
}


