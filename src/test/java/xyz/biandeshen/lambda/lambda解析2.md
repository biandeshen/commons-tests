### Java 中 lambda 表达式解析（2）
>  Java中lambda表达式的本质就是函数式接口的具体实现类的实例对象。

>以简单实例演示
```java
@FunctionalInterface // 函数式接口注解，可写可不写
public interface TestFunction<T, R> {
	R operate(T num);
}
// 负数运算实现类
class NegativeNumberOP implements TestFunction<Integer, Integer> {
	@Override
	public Integer operate(Integer num) {
		return -1 * num;
	}
}
// 浮点运算实现类
class FloatingNumberOP implements TestFunction<Integer, Double> {
	@Override
	public Double operate(Integer num) {
		return 0.7 * num;
	}
}
// 返回TestFunction的TestFunction实现类
class FuncationOp implements TestFunction<Integer, TestFunction> {
	@Override
	public TestFunction<Integer, Integer> operate(Integer num) {
		return new NegativeNumberOP();
	}
}
// 返回TestFunction<Integer,Integer>的TestFunction实现类
class FunctionNegativeOp implements TestFunction<Integer, TestFunction<Integer, Integer>> {
	@Override
	public TestFunction<Integer, Integer> operate(Integer num) {
		return new NegativeNumberOP();
	}
}
// 返回TestFunction<Integer, Double>的TestFunction实现类
class FunctionFloatingOp implements TestFunction<Integer, TestFunction<Integer, Double>> {
	@Override
	public TestFunction<Integer, Double> operate(Integer num) {
		return new FloatingNumberOP();
	}
}
```

> **可知，匿名类与具体实现类只是形式不同，但它们都是接口的实现类，且方法相同**

//  不过，在有了泛型后，仍旧使用原生态类型，就失掉了泛型在安全性和描述性方面的所有优势。
//  譬如，private final List list = new ArrayList(); 使用原生态类型声明一个list,
//  如果我们使用这个 list 添加任何一种对象（String，int...）它都是允许的，可以正常编译和允许，
//  但是当我们使用时获取，则需要注意到每个添加的对象的类型，否则将抛出类型转换的异常，
//  这对一个可能被大量操作的list来说是大概率会发生的。显而易见，程序出错应该尽早被发现，
//  在编译期间就发现与运行时发现完全是两种情况，运行时抛出的异常并不会提示你，
//  告诉你在具体某一行你增加了某个类型不匹配的值，导致你此时转换出错，而只会提示你类型转换的代码，
//  这里类型转换异常。因此，使用原生态类型虽然是合法的，但不应这么做。

//  因此，使用 List<String> 这样的参数化类型，能够避免我们在列表中插入不同类型的值，
//  在编译期间就能发现错误，我们应该将程序进行改造。
//  通过形式类型参数 T、R，我们约定程序的输入类型为T，返回类型为R
//  public interface TestFunction<T, R> {
//	  R operate(T num);
//  }
//  这样，在实现一个具体的实现类时，就可以以如下的形式来进行，
//  将输入类型与结果类型替换成你想要的结果，然后实现接口，重写方法，使参数类型与返回类型与约定匹配，
//  class NegativeNumberOP implements TestFunction<Integer, Integer> {
//	  @Override
//	  public Integer operate(Integer num) {
//	    return -1 * num;
//	  }
//  }
//  class FloatingNumberOP implements TestFunction<Integer, Double> {
//  	@Override
//  	public Double operate(Integer num) {
//  		return 0.7 * num;
//  	}
//  }
//  这样，我们就有了两个实现类，一个是负数运算操作，输入一个 Integer，返回一个 Integer，
//  另外一个是浮点数运算操作，输入一个 Integer，返回一个 Double，
//  从之前的分析我们将具体的实现类转化成匿名类操作，同时再根据类型推断，将匿名类转换为lambda表达式，
//  简化如下
//  public static void main(String[] args) {
//    TestFunction<Integer, Integer> negativeOp = x -> -1 * x;
//    TestFunction<Integer, Double> floatOp = x -> 0.7 * x;
//  }
//  我们就可以通过 negativeOp 将输入的 Integer 类型 进行负数运算输出一个 Integer 类型，
//  通过 floatOp 将输入的 Integer 类型 进行负数运算输出一个 Double 类型，
//  这些操作的含义我们在声明中就知道了(约定的T、R的含义)，因为我们将 T 设置为了 Integer， R 设置为了 Integer(Double)，
//  因此，我们就可以将一个 negativeOp 操作的结果，当成一个 floatOp 操作的输入，并返回一个Double类型，
//  即将 Integer intNum = negativeOp.operate(10); Double doubleNum = floatOp.operate(intNum); 两行，
//	简化成 Double doubleNum2 = floatOp.operate(negativeOp.operate(10)); 一行代码。
//  doubleNum 与 doubleNum2 结果相同。

//  既然我们可以将约定中的 T、R 设置为 Integer与Double，同样的我们也能设置为 String，List等其他类型，
//  当然也包括我们声明的接口 TestFunction。
//  现在举例，输入一个 Integer， 返回一个 TestFunction，即 TestFunction<T,R> 中 T 设置 为 Integer，R 为 TestFunction
//	class FuncationOp implements TestFunction<Integer, TestFunction> {
//		@Override
//		public TestFunction<Integer,Integer> operate(Integer num) {
//			return new NegativeNumberOP();
//		}
//	}
//  我们在重写的方法中定义了具体的返回，返回一个 TestFunction<Integer,Integer>，也就是 NegativeNumberOP 实现类，
//  当然，我们也可以返回一个 TestFunction<Integer, Double> ，即 FloatingNumberOP 实现类，
//  或者我们重新在此处编写一个 TestFunction 的匿名实现类，只要方法的返回类型匹配即可。
//  这是因为我们在 implements TestFunction<Integer, TestFunction> 时，尖括号中的 TestFunction 我们并没有约束，
//  所以它默认的是TestFunction<T,R>，我们就可以自由定义方法的返回类型中的T、R的类型。
//  我们也可以在尖括号中进行约束，那么在方法中就只能返回我们约束的值的类型。如下所示：
//	class FunctionNegativeOp implements TestFunction<Integer, TestFunction<Integer, Integer>> {
//		@Override
//		public TestFunction<Integer, Integer> operate(Integer num) {
//			return new NegativeNumberOP();
//		}
//	}
//  这里面我们一开始就在尖括号限制了返回结果的TestFunction为输入Integer，返回Integer，
//  因此我们能返回的要么是一个 NegativeNumberOP 实现类，要么是重写一个匿名内部类，必须满足输入返回都是Integer的要求。


//  我们以匿名类的方式来实现一个 TestFunction<Integer, TestFunction<Integer, Integer>>， 代码如下：
//	TestFunction<Integer, TestFunction<Integer, Integer>> functionNegativeOp = new TestFunction<Integer,
//			                                                                                           TestFunction<Integer, Integer>>() {
//		@Override
//		public TestFunction<Integer, Integer> operate(Integer num) {
//			System.out.println("传入参数为 = " + num);
//			return new TestFunction<Integer, Integer>() {
//				@Override
//				public Integer operate(Integer num) {
//					return -1 * num;
//				}
//			};
//		}
//	};
//  简化匿名类为 lambda 如下：
//	TestFunction<Integer, TestFunction<Integer, Integer>> functionNegativeOp = num -> {
//		System.out.println("传入参数为 = " + num);
//		return num1 -> -1 * num1;
//	};
//  尝试调用此实现，即如下所示
//  Integer operate = functionNegativeOp2.operate(2).operate(4);
//	System.out.println("operate = " + operate);
//  最终返回的结果也是第二个 operate 的结果，
//  第一个 operate 表示的是 TestFunction<Integer, TestFunction<Integer, Integer>> 实例的 operate 方法，
//  第二个 operate 表示的是第一个 operate 方法 return 的 TestFunction<Integer, Integer> 实例的 operate 方法。
//  根据我们的T、R的值，这样的结果显而易见。


//  上面我们尝试将R，也就是返回结果替换成了TestFunction<Integer,Integer>,现在我们尝试同时替换T和R，示例如下：
//	TestFunction<TestFunction<Integer, Integer>, TestFunction<Integer, Double>> intToDoubleOp =
//		new TestFunction<TestFunction<Integer, Integer>, TestFunction<Integer, Double>>() {
//			@Override
//			public TestFunction<Integer, Double> operate(TestFunction<Integer, Integer> num) {
//				int sum = 1;
//				Integer negativeOpValue = num.operate(sum);
//				System.out.println("negativeOpValue = " + negativeOpValue);
//				return new TestFunction<Integer, Double>() {
//					@Override
//					public Double operate(Integer num12) {
//						return 0.7 * sum * negativeOpValue;
//					}
//				};
//			}
//	};
//  此时我们将传入一个TestFunction<Integer,Integer>，返回一个TestFunction<Integer,Double>，
//  由代码可知，我们执行了传入对象的 operate 方法，返回了一个 Integer 操作结果，然后利用这个返回的 Integer 操作结果，参与了
//  内部的 operate 运算。也就是说，我们动态的传入一个TestFunction<Integer,Integer>实现（即匿名类/lambda）去参与运算
//  1. 按步骤运算：
//  TestFunction<Integer, Integer> negativeOp = x -> -1 * x;
//	TestFunction<Integer, Double> operate = intToDoubleOp2.operate(negativeOp);
//  Double operateValue = operate.operate(10);
//  2. 单号运算
//  Double operateValue2 = intToDoubleOp2.operate(x -> -1 * x).operate(10);
//  System.out.println("operateValue = " + operateValue);
//  System.out.println("operateValue2 = " + operateValue2);
//  可以看到，通过匿名类化简成lambda表达式，能够有效的简化代码，并提高编码效率。
//  上面的代码中，我们输入一个 TestFunction ，输出一个 TestFunction，可以执行两次 operate 操作，
//  同时，在执行两次 operate 中的第一次可以动态传入一个TestFunction接口的实现，执行它的 operate 实现，
//  使我们的程序更灵活。

