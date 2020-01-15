/**
 * FileName: Pizza
 * Author:   admin
 * Date:     2020/1/8 13:57
 * Description: Builder模式类层次结果
 * History:
 * <author>          <time>          <version>
 * admin           2020/1/8           版本号
 */
package xyz.biandeshen.EffectiveJava;

import xyz.biandeshen.EffectiveJava.MyPizza.Size;

import java.lang.reflect.Field;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

/**
 * 〈Builder模式类层次结构〉
 *
 * @author admin
 * @since 1.0.0
 */
public abstract class Pizza {
	public enum Topping {HAM, MUSHROOM, ONION, PEPPER, SAUSAGE}
	
	final Set<Topping> toppings;
	
	abstract static class Builder<T extends Builder<T>> {
		EnumSet<Topping> toppings = EnumSet.noneOf(Topping.class);
		
		public T addTopping(Topping topping) {
			toppings.add(Objects.requireNonNull(topping));
			return self();
		}
		
		abstract Pizza build();
		
		protected abstract T self();
	}
	
	Pizza(Builder<?> builder) {
		toppings = builder.toppings.clone();
	}
	
	public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException,
	                                              IllegalAccessException {
		Pizza build = new MyPizza.Builder(Size.LARGE).self().addTopping(Topping.HAM).self().build();
		System.out.println("build = " + build);
		
		//Integer i = 1;
		//if (i.equals(1)) i = null;
		//Double d = 2.0;
		//Object o = true ? i : d; // NullPointerException!
		//System.out.println(o);
		
		
		// Extract the IntegerCache through reflection
		Class<?> clazz = Class.forName("java.lang.Integer$IntegerCache");
		Field field = clazz.getDeclaredField("cache");
		field.setAccessible(true);
		Integer[] cache = (Integer[]) field.get(clazz);
		
		// Rewrite the Integer cache
		for (int i = 0; i < cache.length; i++) {
			cache[i] = new Integer(new Random().nextInt(cache.length));
		}
		
		// Prove randomness
		for (int i = 0; i < 10; i++) {
			System.out.println((Integer) i);
		}
		
		//
		//Test test = new Test();
		//Field field1 = test.getClass().getDeclaredField("n");
		//field1.setAccessible(true);
		//Integer cache1 = (Integer) field1.get(test);
		//field1.set(test,1);
		//
		//test.x(1,2L);
		//System.out.println("test.n = " + test.n);
		
	}
}

class MyPizza extends Pizza {
	public enum Size {SMALL, MEDMEDIUM, LARGE}
	
	private final Size size;
	
	public static class Builder extends Pizza.Builder {
		private final Size size;
		
		public Builder(Size size) {
			this.size = Objects.requireNonNull(size);
		}
		
		@Override
		public MyPizza build() {
			return new MyPizza(this);
		}
		
		@Override
		protected Pizza.Builder self() {
			return this;
		}
	}
	
	private MyPizza(Builder builder) {
		super(builder);
		size = builder.size;
	}
}

class Calzone extends Pizza {
	private final boolean sauceInside;
	
	public static class Builder extends Pizza.Builder<Builder> {
		private boolean sauceInside = false;
		
		public Builder sauceInside() {
			sauceInside = true;
			return this;
		}
		
		@Override
		public Calzone build() {
			return new Calzone(this);
		}
		
		@Override
		protected Builder self() {
			return this;
		}
	}
	
	private Calzone(Builder builder) {
		super(builder);
		sauceInside = builder.sauceInside;
	}
}

//ta zhou ran jin bi le meng fei ——
//dang wo zhan shi chu dui si wang de qing mu hou
//

