package xyz.biandeshen;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author fjp
 * @Title: CopyOfTest
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/7/1422:20
 */
public class CopyOfTest {
	
	public static void main(String[] args) {
		int[] a = {1, 2, 3};
		a = (int[]) goodCopyOf(a, 10);
		System.out.println(Arrays.toString(a));
		
		String[] b = {"Tom", "Dick", "Harry"};
		b = (String[]) goodCopyOf(b, 10);
		System.out.println(Arrays.toString(b));
		
		//System.out.println("下面的调用将生成异常。");
		//b = (String[]) badCopyOf(b,10);
		//System.out.println(Arrays.toString(b));
		
		try {
			Method method = CopyOfTest.class.getMethod("goodCopyOf", Object.class, int.class);
			b = (String[]) method.invoke(CopyOfTest.class, b, 10);
			System.out.println("b = " + Arrays.toString(b));
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public static Object[] badCopyOf(Object[] a, int newLength) {
		Object[] newArray = new Object[newLength];
		System.arraycopy(a, 0, newArray, 0, Math.min(a.length, newLength));
		return newArray;
	}
	
	public static Object goodCopyOf(Object a, int newLength) {
		Class c1 = a.getClass();
		if (!c1.isArray()) {
			return null;
		}
		Class componentType = c1.getComponentType();
		int length = Array.getLength(a);
		Object newArray = Array.newInstance(componentType, length);
		//noinspection SuspiciousSystemArraycopy
		System.arraycopy(a, 0, newArray, 0, Math.min(length, newLength));
		return newArray;
	}
}
