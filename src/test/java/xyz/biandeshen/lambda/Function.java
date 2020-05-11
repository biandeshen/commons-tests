package xyz.biandeshen.lambda;

/**
 * @FileName: ddd
 * @Author: admin
 * @Date: 2020/1/13 23:44
 * @Description: Function工具类
 * History:
 * <author>          <time>          <version>
 * admin           2020/1/13           版本号
 */
public interface Function<T, U> {
	//作为抽象函数接口的基础方法，接受T类型返回U类型
	U apply(T arg);
	
	// 传入Function<V,T>
	// 即传入V，返回T 则其方法为 T apply(V arg)，
	// 故 f.apply 返回 T
	// 正常类型为是Function<T,U>
	// 即 外围 apply 方法中传入是T 返回的将是U,
	// 即 传入一个 V，最终返回一个 U
	// 而实际对外围方法，参数 V 为 Function<V,T>，即参数类型为 Function 类型
	// 因此 返回的应为 一个 Function ，
	// 泛型的泛型参数为 <V,U>
	// 对应 Function 则为 Function<V,U>
	default <V> Function<V, U> compose(Function<V, T> f) {
		// 此形式 标识 传入 是参数附加，返回是原有
		return x -> apply(f.apply(x));
	}
	
	//<T,U> -> 做参数 应用于 <U,V> ，最终为 <T,V> ，参数类型为 Function
	default <V> Function<T, V> andThen(Function<U, V> f) {
		// 此形式 标识 传入 是原有，返回是参数新增
		return x -> f.apply(apply(x));
	}
	
	static <T> Function<T, T> identity() {
		return t -> t;
	}
	
	// 根据传入参数类型 TU VT 得到结果 传入V 返回U
	static <T, U, V> Function<V, U> compose(Function<T, U> f, Function<V, T> g) {
		return x -> f.apply(g.apply(x));
	}
	
	static <T, U, V> Function<T, V> andThen(Function<T, U> f, Function<U, V> g) {
		return x -> g.apply(f.apply(x));
	}
	
	// 前面为传入的参数，后面为返回的参数，即最后的参数的类型为整个返回的类型？
	// 由此可得知以下此compose为错误 compose 返回应为 类型U
	//static <T, U, V> Function<Function<T, U>, Function<Function<U, V>, Function<T, V>>> compose() {
	//	//return x -> y -> y.compose(x);
	//	//   T U   U V  ——  y.apply(x)  —— V(U)(U)T
	//	//return x -> y -> compose(y, x);
	//	return  f->g->x->g.apply(f.apply(x));
	//}
	static <T, U, V> Function<Function<T, U>, Function<Function<V, T>, Function<V, U>>> compose() {
		//return x -> y -> y.compose(x);
		//   T U   U V  ——  y.apply(x)  —— V(U)(U)T
		//return x -> y -> compose(y, x);
		return f -> g -> x -> f.apply(g.apply(x));
	}
	
	// TU , Fun<VU>
	// f      g
	// f.apply(g.apply(x))
	//static <T, U, V> Function<Function<T, U>, Function<Function<V, T>, Function<V, U>>> andThen() {
	//	//return x -> y -> y.andThen(x);
	//	return x -> y -> andThen(y, x);
	//
	//}
	
	// T int
	// U long
	// V double
	// T->U            U->V                 T->V
	// int->long      long -> double       int -> double
	//static <T, U, V> Function<Function<U, V>, Function<Function<T, U>, Function<T, V>>> andThen3() {
	//	//return x -> y -> y.andThen(x);
	//	//return x -> y -> andThen(y, x);
	//	return f -> g -> x -> f.apply(g.apply(x));
	//}
	
	static <T, U, V> Function<Function<T, U>, Function<Function<U, V>, Function<T, V>>> andThen() {
		//return x -> y -> y.andThen(x);
		//return x -> y -> andThen(y, x);
		//return f -> g -> x -> g.apply(f.apply(x));
		return f -> f::andThen;
	}
	
	// *** 实际 Function<U,V> 最外层函数   Function<Function<T,U> 为第二层函数   传入参数的为 Function<T,V> 即此方法完成
	// 的行为为 T->V
	//         Function<Double,Integer>   Function<Function<Long,Double            Function<Long,Integer>
	
	//	Function<Double, Integer> f = a -> (int) (a * 3);
	//	Function<Long, Double> g = x -> x + 2.0;
	// 很显然我们要组合成f.apply(g.apply(x))
	// 可见多次柯里化，不管传实际值还是传函数对象，泛型从最开始的类型到最终返回类型按顺序排好，
	// 而其中的函数则从最外层排到最内层，最后返回的对象是整个函数的映射，所以泛型是从最初类型到最终返回类型。这就是高阶函数泛型的关键。
	static <T, U, V> Function<Function<U, V>, Function<Function<T, U>, Function<T, V>>> higherCompose() {
		return f -> g -> x -> f.apply(g.apply(x));
	}
	
	static <T, U, V> Function<Function<T, U>, Function<Function<U, V>, Function<T, V>>> higherAndThen() {
		return f -> g -> z -> g.apply(f.apply(z));
	}
	
	
	public static void main(String[] args) {
		Function<Integer, Long> f = x -> x * 10L;
		Function<Long, Double> g = x -> x * 0.3D;
		Double apply = g.apply(f.apply(4));
		System.out.println("apply = " + apply);
		Double apply1 = andThen(f, g).apply(4);
		System.out.println("apply1 = " + apply1);
		
		
		//=======================================
		Function<Function<Object, Object>, Function<Function<Object, Object>, Function<Object, Object>>> f3 =
				higherAndThen();
		Function<Function<Object, Object>, Function<Object, Object>> f2 = f3.apply(x -> x);
		Function<Object, Object> f1 = f2.apply(x -> 2);
		Object fv = f1.apply(4);
		System.out.println("fv = " + fv);
		
		Object fv2 = f3.apply(x -> x).apply(x -> 2).apply(4);
		System.out.println("fv2 = " + fv2);
	}
	
}