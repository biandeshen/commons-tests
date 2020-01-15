package xyz.biandeshen.lambda;

import javax.swing.Timer;
import java.awt.*;
import java.util.*;
import java.util.function.Supplier;

/**
 * @author fjp
 * @Title: lambda
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/7/1913:47
 */
@SuppressWarnings("all")
public class Lambda {
	public static void main(String[] args) {
		//1
		Comparator<String> stringComparator = (String first, String second) -> {
			if (first.length() < second.length())
				return -1;
			else if (first.length() > second.length())
				return 1;
			else
				return 0;
		};
		//2
		Comparator<String> stringComparator1 = (first, second) -> first.length() - second.length();
		//3
		Comparator<String> stringComparator3 = Comparator.comparingInt(String::length);
		//4
		Comparator<String> stringComparator2 = (first, second) -> {
			int i = first.length() - second.length();
			return i;
		};
		
		//runnable test
		Runnable runnable = () -> {
			for (int i = 100; i >= 0; i--) {
				System.out.println(i);
			}
		};
		
		
	}
	
	B test = (String first, String second) -> first.length() - second.length();
	B test01 = (first, second) -> first.length() - second.length();
	
	
	Timer t1 = new Timer(1000, event -> {
		System.out.println(" At the tone , the time is " + new Date());
		Toolkit.getDefaultToolkit().beep();
	});
}


interface B {
	int test01(String i, String j);
	
	//boolean equals(Object obj);
}

@SuppressWarnings("all")
class Car implements Comparable<Car> {
	private String name;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	//Supplier是jdk1.8的接口，这里和lamda一起使用了
	public static Car create(final Supplier<Car> supplier) {
		return supplier.get();
	}
	
	public void follow(final Car another) {
		System.out.println("Following the " + another.toString());
	}
	
	public static void collide(final Car car) {
		System.out.println("Collided " + car.toString());
	}
	
	public void repair() {
		System.out.println("Repaired " + this.toString());
	}
	
	public static void repair2(String other) {
		System.out.println("Repaired " + other);
	}
	
	public void repair3(String other) {
		System.out.println("Repaired " + this.toString() + other);
	}
	
	@Override
	public int compareTo(Car o) {
		return 0;
	}
}

@SuppressWarnings("all")
class CarTest {
	public static void main(String[] args) {
		java.util.List<Car> carList = new ArrayList<>();
		Car c1 = new Car();
		Car c2 = new Car();
		Car c3 = new Car();
		Car c4 = new Car();
		carList.add(c1);
		carList.add(c2);
		carList.add(c3);
		carList.add(c4);
		
		// 3. 实例对象方法引用 以::做分割,第一个参数做调用者,第二个参数做被调用方法
		// 即 Class::instanceMethod (instance::Method)
		carList.forEach(c2::follow);
		
		// 2. 静态方法引用 以::做分割,第一个参数为类本身(同时也作为静态方法的参数传入),第二个参数为类的静态方法
		// 即 Class::staticMethod
		// 此方法引用等价于提供方法参数的 lambda 表达式,即
		// System.out::println  等价于 x -> System.out.println(x);
		carList.forEach(Car::collide);
		
		// 1. 特定类任意对象方法引用 以::做分割,第一个参数为类本身,第二个参数为类的无参方法（非静态方法）
		// 即 object::instanceMethod (Class::Method)
		// 此方法引用等价于提供方法参数的 lambda 表达式,即
		// Math::pow 等价于 (x,y) -> Math.pow(x,y)
		carList.forEach(Car::repair);
		
		
		// 即forEach执行的是一个操作,即for循环遍历整个list执行action
		// 即以下三者等价
		// 1. 先写出需要执行的方法
		// 2. 根据执行的方法所需调用的对象来放对应类型的参数(类或实例)
		// 3. 由于类型会自动判断,所以第二步一般可以不用考虑
		// 4. 对表达式进行微调
		carList.forEach((car) -> Car.repair2("other"));
		carList.stream().map(car -> "other").forEach(Car::repair2);
		for (Car car : carList) {
			Car.repair2("other");
		}
		
		carList.forEach((carNew) -> Car.repair2("otherStr"));
		
		carList.forEach((car -> {
			car.repair3("otherStr");
		}));
		
		//carList.forEach(Car.collide(car));
		//carList.forEach(car -> Car.collide(car));
		//carList.forEach((Car car) -> Car.collide(car));
		
		carList.forEach(Car::repair);
		
		Car car = Car.create(Car::new);
		Car car2 = Car.create(new Supplier<Car>() {
			@Override
			public Car get() {
				return new Car();
			}
		});
		Car car4 = Car.create(() -> {
			return new Car();
		});
		Car car3 = Car.create(() -> new Car());
		
		
		Map<String, Object> map = new HashMap<>();
		map.put("1", Car.create(Car::new));
		
		Comparator.comparing(Car::getName, Comparator.nullsFirst(Comparator.naturalOrder()));
	}
}
