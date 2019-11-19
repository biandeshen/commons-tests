package xyz.biandeshen.dict;

/**
 * @author fjp
 * @Title: TestEnumFactory
 * @ProjectName commons-tests
 * @Description: car的原始工厂与枚举工厂的两种工厂实现方式
 * @date 2019/11/1116:14
 */
@SuppressWarnings("all")
public class TestEnumFactory {
	public static void main(String[] args) {
		//最常用的工厂模式，实例化class
		Car car = OriginCarFactory.createCar(BuickCar.class);
		if (car != null) {
			car.myName();
		}
		
		//用枚举模式实例化class
		try {
			// 1.
			car = EnumCarFactory.valueOf("FordCar").create();
			// 2.
			car = EnumCarFactory.FordCar.create();
			// 3.
			car = EnumCarFactory.createCar(FordCar.class);
			car.myName();
		} catch (Exception e) {
			System.out.println("无效参数,无法初始化");
		}
		
	}
}

@SuppressWarnings("all")
interface Car {
	public void myName();
}

class FordCar implements Car {
	public void myName() {
		System.out.println("it's ford");
	}
}

class BuickCar implements Car {
	public void myName() {
		System.out.println("it's buick");
	}
}

/**
 * 原始的工厂模式，新增一个Car子类，完全不管
 */
@SuppressWarnings("all")
class OriginCarFactory {
	public static Car createCar(Class<? extends Car> c) {
		try {
			return c.newInstance();
		} catch (Exception e) {
			System.out.println("无效参数,无法初始化");
		}
		return null;
	}
}

/**
 * 枚举实现工厂模式，一旦新加一个类，还需要在enum中新增对应实例化方法
 */
enum EnumCarFactory {
	FordCar {
		@Override
		public Car create() {
			return new FordCar();
		}
		
	},
	BuickCar {
		@Override
		public Car create() {
			return new BuickCar();
		}
	};
	
	//abstract修饰方法，强制每个枚举实现该方法
	public abstract Car create();
	
	//根据类型创建,即原始工厂的创建对象方式,放此处实际此代码并未与枚举类相关,二者实际无耦合
	public static Car createCar(Class<? extends Car> c) {
		try {
			return c.newInstance();
		} catch (Exception e) {
			System.out.println("无效参数,无法初始化");
		}
		return null;
	}
}