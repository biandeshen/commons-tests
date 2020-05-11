
### Java 中 lambda 表达式解析（1）
>  Java中lambda表达式的本质就是函数式接口的具体实现类的实例对象。

>以简单实例演示
```java
@FunctionalInterface//注解用于标识接口为函数式接口，它不是必须的，可加可不加
public interface TestFunction {
	Integer operate(Integer num);
}

/**
 * 创建一个实现类并重写operate方法，
   将方法实现为传入参数为Integer时设置为它的负数
 */
class NegativeNumberOP implements TestFunction {
	@Override
	public Integer operate(Integer obj) {
		return -1 * obj;
	}
}
```
> **此时即可在main方法中声明接口，创建具体实现类，运用重写的操作**
```java
public class Test{
	public static void main(String[] args) {
		TestFunction testFunction = new NegativeNumberOP();
		Object operate = testFunction.operate(666);
		System.out.println("operate = " + operate);
	}
}
```

>  **若此操作不需要多次复用，那可以用嵌套类的形式，创建一个匿名类**

```java
public class Test{
	public static void main(String[] args) {
		TestFunction testFunction = new TestFunction() {
			@Override
			public Integer operate(Integer obj) {
				return -1 * obj;
			}
		};
		Object operate = testFunction.operate(666);
		System.out.println("operate = " + operate);
	}
}
```
> **可知，匿名类与具体实现类只是形式不同，但它们都是接口的实现类，且方法相同**

>***简化匿名类至lambda步骤及详细说明：***
>>1. 我们还可以通过简化匿名类的写法，将匿名类转换为lambda表达式的写法。
  一般来说，我们通过 TestFunction testFunction = new TestFunction(){} 来创建一个接口的内部类实例，
  因此，若声明 TestFunction testFunction，则后面的 new TestFunction 完全可以依据声明被自动推断出来，
 我们可以简化，无需写 new TestFunction，程序自动推断 TestFunction testFunction = 右侧是一个匿名类，
  当接口中只有一个方法需要我们重写时，
  @Override
  public Integer operate(Integer obj) {};
  这个重写的注解以及方法的名称、参数类型、个数、返回类型也能被自动识别
  所以，方法的具体实现可以简化写成
  (Integer obj) -> {return -1 * obj;};
  其中，箭头左边括号中的是方法的参数，箭头指向方法，花括号内是方法具体的实现
>>2. 我们知道，参数类型与返回类型是可以被推断出来的
  而假若方法有返回值时将会有return，无返回值时将没有return，
  因此，是否 return 也能被推断出来，类型推断能自动对有返回值的方法体（单条语句）进行return，
  所以参数类型 Integer 与 return 也就可以被忽略（当方法的具体实现能用单条语句实现的）
  简化写成 (obj) -> {-1 * obj;};
  而此时这种形态将会报错，因为 -1 * obj; 因为在{}中这并不是一条完整的语句，
  由于方法只有这一条语句，所以方法体{}可以被推断出来，就可以省略方法体（而多条语句时则方法体与return都不可省略）
  因此 可以简化写成 (obj) -> -1 * obj;
>>3.  我们知道，箭头左侧的括号内都是方法的参数，所以箭头左边方法的括号也可以省略，只保留参数
  因此可以简化写成 obj -> -1 * obj;
  同时，我们也可以知道 方法中的参数名称都是 形式参数，所以可以自由命名
  因此 参数名称可以随意填写，即 o -> -1 * o; 与 obj -> -1 * obj; 相同。
>>4. Java中，当我们知道方法名称与方法参数的个数、类型及顺序时，就能确定一个方法，
  所以，当方法简化成函数式时，箭头左边的参数类型，个数，顺序确定，就能表示对应的方法，
  由于方法中参数的名称，是形式参数，因此可以随意命名，且分隔方式仍旧以逗号
  所以多个参数的表达式 就可以写为 参数名1，参数名2，参数名3 -> 的形式


---

> <h6> 结论： Java中lambda表达式的本质就是接口的实现类的实例（匿名类对象）。

