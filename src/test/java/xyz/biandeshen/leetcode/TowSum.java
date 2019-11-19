package xyz.biandeshen.leetcode;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

import static java.lang.Math.subtractExact;

/**
 * @author fjp
 * @Title: TowSum
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/7/1723:17
 */
public class TowSum {
	//有序数组的towSum
	@Test
	public void testTowSum() {
		int[] numbers = new int[]{2, 7, 10, 15};
		// target: two sum   total 9
		int total = 9;
		int start = 0;
		int end = numbers.length - 1;
		int[] target = new int[2];
		
		while (start < end) {
			int sum = numbers[start] + numbers[end];
			if (sum == total) {
				//对应numbers中两个数位置,从1开始
				target = new int[]{start + 1, end + 1};
				break;
			}
			//有序数组,若大于total,则end左移
			if (sum > total) {
				end--;
			}
			//有序数组,若小于total,则start左移
			if (sum < total) {
				start++;
			}
		}
		System.out.println("Arrays.toString(target) = " + Arrays.toString(target));
	}
	
	//无序数组的towSum
	@Test
	public void testTowSum2() {
		int[] numbers = new int[]{2, 3, 10, 5, 19, 7, 0, 1, 4, 15};
		// target: two sum   total 9
		int total = 9;
		int[] target = new int[0];
		
		int sum;
		for (int i = 0; i < numbers.length; i++) {
			for (int j = numbers.length - 1; j > 0; j--) {
				sum = numbers[i] + numbers[j];
				if (sum == total) {
					target = new int[]{i + 1, j + 1};
					break;
				}
			}
			if (target.length > 1) {
				break;
			}
		}
		System.out.println("Arrays.toString(target) = " + Arrays.toString(target));
	}
	
	//无序数组的towSum
	@Test
	public void testTowSum3() {
		int[] numbers = {2, 3, 10, 5, 19, 7, 0, 1, 4, 15};
		//无序数组的towSum 转有序数组
		// target: two sum   total 9
		// 1. 升序重排
		Arrays.sort(numbers);
		System.out.println("numbers = " + Arrays.toString(numbers));
		// 2. 降序重排
		// int[] 转 List<Integer>
		List<Integer> numbersList = Arrays.stream(numbers).boxed().collect(Collectors.toList());
		
		// Arrays.stream(arr) 可以替换成IntStream.of(arr)。
		// 2.1.使用Arrays.stream将int[]转换成IntStream。
		// 2.2.使用IntStream中的boxed()装箱。将IntStream转换成Stream<Integer>。
		// 2.3.使用Stream的collect()，将Stream<T>转换成List<T>，因此正是List<Integer>。
		
		// int[] 转 Integer[]
		Integer[] integers = Arrays.stream(numbers).boxed().toArray(Integer[]::new);
		//Integer[] 转 int[]
		//int[] ints = Arrays.stream(integers).mapToInt(Integer::new).toArray();
		//int[] numbersList1 = Arrays.stream(integers).mapToInt(Integer::new).toArray();
		// 前两步同上，此时是Stream<Integer>。
		// 然后使用Stream的toArray，传入IntFunction<A[]> generator。
		// 这样就可以返回Integer数组。
		// 不然默认是Object[]。
		Arrays.sort(integers, Collections.reverseOrder());
		
		//3. lambda 表达式
		Comparator<Integer> comparator = Comparator.comparingInt(o -> o);
		Arrays.sort(integers, comparator);
		
		
		System.out.println("numbers = " + Arrays.toString(integers));
		int total = 9;
		int start = 0;
		int end = numbers.length - 1;
		int[] target = new int[2];
		
		while (start < end) {
			int sum = numbers[start] + numbers[end];
			if (sum == total) {
				//对应numbers中两个数位置,从1开始
				target = new int[]{start + 1, end + 1};
				break;
			}
			//有序数组,若大于total,则end左移
			if (sum > total) {
				end--;
			}
			//有序数组,若小于total,则start左移
			if (sum < total) {
				start++;
			}
		}
		System.out.println("Arrays.toString(target) = " + Arrays.toString(target));
	}
}
