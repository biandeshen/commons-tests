package xyz.biandeshen.lambda;

import java.util.function.Consumer;

/**
 * @FileName: TestFunction
 * @Author: admin
 * @Date: 2020/1/14 14:15
 * @Description: 简单的Function实例
 * History:
 * <author>          <time>          <version>
 * admin           2020/1/14           版本号
 */
@FunctionalInterface
public interface TestFunction {
	Integer operate(Integer num);
	
	public static void main(String[] args) {
		Consumer<String> consumer = System.out::println;
		consumer.accept("aaa");
		Consumer<String> stringConsumer = consumer.andThen(s -> System.out.println(s+"sss"));
		stringConsumer.accept("bbb");
	}
}

class NegativeNumberOP implements TestFunction {
	@Override
	public Integer operate(Integer obj) {
		return -1 * obj;
	}
}
// 创建一个实现类并重写operate方法，
// 将方法实现为传入参数为Integer时设置为它的负数
//
// 此时即可在main方法中声明接口，创建具体实现类，运用重写的操作
//	public static void main(String[] args) {
//		TestFunction testFunction = new NegativeNumberOP();
//		Object operate = testFunction.operate(666);
//		System.out.println("operate = " + operate);
//	}
//
//  若此操作不需要多次复用，那可以用嵌套类的形式，创建一个匿名类
//	public static void main(String[] args) {
//		TestFunction testFunction = new TestFunction() {
//			@Override
//			public Integer operate(Integer obj) {
//				return -1 * obj;
//			}
//		};
//		Object operate = testFunction.operate(666);
//		System.out.println("operate = " + operate);
//	}
//  此时，匿名类与具体实现类只是类的形式不同，但它们都是接口的一个实现类，它们的方法相同
//  我们此时可以简化匿名类的写法
//  一般来说，我们通过 TestFunction testFunction = new TestFunction(){} 来 new 一个接口的内部类,
//  因此，只要我们有了声明 TestFunction testFunction，后面的 new TestFunction 就能依据声明被自动推断出来，
//  我们就可以简化 new TestFunction，无需写
//  同时，当接口中只有一个方法需要我们重写时，
//  @Override
//	public Integer operate(Integer obj) {
//  这个重写的注解以及方法的名称、参数类型、返回类型也能被自动推断
//  因此，有返回值时将会有return，无返回值时将没有return也能被推断
//  此时，方法的具体实现简化写成
//  (Integer obj) -> {return -1 * obj;};
//  括号中的是方法的参数，箭头指向方法，大括号内是具体的实现
//  我们知道，参数类型与返回类型是可以被推断出来的，所以参数类型 Integer 与 return 就可以被忽略
//  (obj) -> {-1 * obj;};
//  此时这种形态将会报错，因为 -1 * obj; 在{}中并不是一条完整的语句，但此时只有这一条语句，
//  方法体{}可以被推断出来，所以可以省略方法体；而多条语句时则方法体与return都不可省略
//  因此 可以简化写成 (obj) -> -1 * obj;
//  我们知道，箭头左侧的括号内都是方法的参数，所以箭头左边方法的括号也可以省略，只保留参数
//  此时可以简化写成 obj -> -1 * obj;
//  同时，我们也可以知道 方法中的参数名称都是 形式参数，所以可以自由命名
//  因此 参数名称可以随意填写，即 o -> -1 * o; 与 obj -> -1 * obj; 相同。
//  Java中，同名方法的参数类型是不能相同的，当我们确定一个方法时，只要知道方法名称与方法参数的个数、类型及顺序，
//  因此，当方法简化成函数式时，箭头左边的参数类型，个数，顺序确定，就能表示对应的方法，
//  而对应的参数的名称，则是形式参数，可以随意命名，分隔方式仍旧以逗号
//  因此 多个参数的表达式 就可以写为 参数名1，参数名2，参数名3 -> 的形式
//
//  结论： Java中lambda表达式的本质就是接口的实现类的实例（匿名类对象）。

