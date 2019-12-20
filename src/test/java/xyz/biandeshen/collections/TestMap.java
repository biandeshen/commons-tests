package xyz.biandeshen.collections;

import org.junit.Test;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Consumer;

/**
 * @author fjp
 * @Title: TestMap
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/9/223:42
 */
public class TestMap {
	/**
	 * ddd
	 */
	/*dasdfa*/
	@Test
	public void testmap() {
		Map<String, String> map = new HashMap<>();
		map.put("n0", "0");
		//调用putIfAbsent方法,只有当键存在时才放入一个值
		map.putIfAbsent("n0", "1");
		System.out.println("map.get(\"n0\") = " + map.get("n0"));
		map.putIfAbsent("n1", "11");
		//使用getOrDefault方法,避免出现空指针
		String n1 = map.getOrDefault("n2", null);
		System.out.println("n1 = " + n1);
		//如果键原先不存在,将键与值关联,否则使用函数组合原值与value
		map.merge("n1", "12", String::concat);
		String n2 = map.getOrDefault("n1", null);
		System.out.println("n2 = " + n2);
		
		/*映射视图*/
		//键集
		map.keySet().forEach(System.out::println);
		//值集
		map.values().forEach(System.out::println);
		//键值对集
		map.entrySet().forEach(System.out::println);
		for (Entry<String, String> stringStringEntry : map.entrySet()) {
			stringStringEntry.getKey();
			stringStringEntry.getValue();
		}
		//lambda表达式遍历一个map
		map.forEach((k, v) -> System.out.println("key: " + k + " value: " + v));
		
		String[] values = {"one", "two", "three"};
		HashSet<String> staff = new HashSet<>(Arrays.asList(values));
		String[] strings = staff.toArray(new String[0]);
		
		Collection<String> values1 = map.values();
		List<Object> objects = new ArrayList<>(values1);
		
		System.out.println("objects.get(0) = " + objects.get(0));
	}
	
	@Test
	public void testCustomForEachImpl() {
		CustomForEachImpl<String> strings = new CustomForEachImpl<>();
		strings.add("1");
		strings.add("2");
		strings.add("3");
		strings.forEach(System.out::println);
	}
	
}


interface customForEach<T> {
	default void forEach(Consumer<? super T> action) {}
	/* {
		Objects.requireNonNull(action);
		//for (T t : this) {
		//	action.accept(t);
		//}
	}*/
}

class CustomForEachImpl<E> implements customForEach<E> {
	private Object[] objs;
	private int size = 0;
	
	public boolean add(E e) {
		objs[size++] = e;
		return true;
	}
	
	public CustomForEachImpl(int initialCapacity) {
		this.objs = new Object[initialCapacity];
	}
	
	public CustomForEachImpl() {
		this.objs = new Object[2 << 3];
	}
	
	@Override
	public void forEach(Consumer<? super E> action) {
		@SuppressWarnings("unchecked") final E[] elementData = (E[]) this.objs;
		for (E e : elementData) {
			action.accept(e);
		}
	}
}
