package xyz.biandeshen.lambda;

/**
 * @FileName: TestLiquid
 * @Author: admin
 * @Date: 2020/1/17 17:09
 * @Description: 测试限定通配符
 * History:
 * <author>          <time>          <version>
 * admin           2020/1/17           版本号
 */
public class TestLiquid {
	
	public static void main(String[] args) {
		Bottle<? super Drink> superA = new Bottle<>(new Liquid());
		Bottle<? super Drink> superB = new Bottle<>(new Drink());
		
		//superA.set(new Liquid()); 不允许
		Object object = superA.get();
		superA.set(new Drink());
		
		superB.set(new Drink());
		superB.set(new Milk());
		Object object1 = superB.get();
		
		Bottle<? extends Drink> extendB1 = new Bottle<>(new Drink());
		Bottle<? extends Milk> extendB2 = new Bottle<>(new Milk());
		extendB1.get();
		extendB2.get();
		
		Bottle<?> another = new Bottle<>(new Drink());
		//another.set(new Drink());
		Object o = another.get();
		Drink drink1 = (Drink) o;
	}
}

// 液体
class Liquid {}

// 饮料
class Drink extends Liquid {}

// 牛奶
class Milk extends Drink {}

// 可乐
class Cola extends Drink {}

// 瓶子
class Bottle<T> {
	private T liquid;
	
	public Bottle(T t) {liquid = t;}
	
	public void set(T t) {liquid = t;}
	
	public T get() {return liquid;}
}