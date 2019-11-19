package xyz.biandeshen.generic;

import java.time.LocalDate;

/**
 * @author fjp
 * @Title: GenericTypeTest
 * @ProjectName commons-tests
 * @Description: 通配符捕获
 * @date 2019/8/1615:53
 */
public class GenericTypeTest {
	
	public static void main(String[] args) {
		Manager ceo = new Manager("zhao", 800000, 2013, 12, 15);
		Manager cfo = new Manager("qian", 600000, 2013, 12, 15);
		Pair<Manager> buddies = new Pair<>(ceo, cfo);
		printBuddies(buddies);
		
		ceo.setBonus(1000000);
		cfo.setBonus(500000);
		
		Manager[] managers = {ceo, cfo};
		
		Pair<Employee> result = new Pair<>();
		minmaxBonus(managers, result);
		System.out.println("\"first: \"+result.getFirst().getName()+\" ,second: \" + result.getSecond().getName() = " + "first: " + result.getFirst().getName() + " ,second: " + result.getSecond().getName());
		maxminBonus(managers, result);
		System.out.println("\"first: \"+result.getFirst().getName()+\" ,second: \" + result.getSecond().getName() = " + "first: " + result.getFirst().getName() + " ,second: " + result.getSecond().getName());
	}
	
	public static void printBuddies(Pair<? extends Employee> pair) {
		Employee first = pair.getFirst();
		Employee second = pair.getSecond();
		System.out.println("first.getName() +\"and\" + second.getName() = " + first.getName() + " and " + second.getName() + " are buddies.");
		
	}
	
	public static void minmaxBonus(Manager[] a, Pair<? super Manager> result) {
		if (a.length == 0)
			return;
		Manager min = a[0];
		Manager max = a[0];
		for (Manager manager : a) {
			if (min.getBonus() > manager.getBonus())
				min = manager;
			if (max.getBonus() < manager.getBonus())
				max = manager;
		}
		result.setFirst(min);
		result.setSecond(max);
	}
	
	public static void maxminBonus(Manager[] a, Pair<? super Manager> result) {
		minmaxBonus(a, result);
		PairAlg.swap(result);
	}
}

class Employee {
	
	private String name;
	
	private double salary;
	
	private LocalDate hireDay;
	
	public Employee(String name, double salary, LocalDate hireDay) {
		this.name = name;
		this.salary = salary;
		this.hireDay = hireDay;
	}
	
	public Employee(String name, double salary, int year, int month, int day) {
		this.name = name;
		this.salary = salary;
		this.hireDay = LocalDate.of(year, month, day);
	}
	
	
	@Override
	public String toString() {
		final StringBuilder sb;
		sb = new StringBuilder(this.getClass().getName() + "={");
		sb.append("\"name\":\"").append(name).append('\"');
		sb.append(",\"salary\":").append(salary);
		sb.append(",\"hireDay\":\"").append(hireDay).append('\"');
		sb.append('}');
		return sb.toString();
	}
	
	/**
	 * 获取
	 *
	 * @return name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * 设置
	 *
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 获取
	 *
	 * @return salary
	 */
	public double getSalary() {
		return this.salary;
	}
	
	/**
	 * 设置
	 *
	 * @param salary
	 */
	public void setSalary(double salary) {
		this.salary = salary;
	}
	
	/**
	 * 获取
	 *
	 * @return hireDay
	 */
	public LocalDate getHireDay() {
		return this.hireDay;
	}
	
	/**
	 * 设置
	 *
	 * @param hireDay
	 */
	public void setHireDay(LocalDate hireDay) {
		this.hireDay = hireDay;
	}
}

class Manager extends Employee {
	private int bonus;
	
	public Manager(String name, double salary, LocalDate hireDay) {
		super(name, salary, hireDay);
	}
	
	public Manager(String name, double salary, int year, int month, int day) {
		super(name, salary, year, month, day);
	}
	
	/**
	 * 获取
	 *
	 * @return bonus
	 */
	public int getBonus() {
		return this.bonus;
	}
	
	/**
	 * 设置
	 *
	 * @param bonus
	 */
	public void setBonus(int bonus) {
		this.bonus = bonus;
	}
}

class Pair<T> {
	private T first;
	private T second;
	
	public Pair() {
	}
	
	public Pair(T first, T second) {
		this.first = first;
		this.second = second;
	}
	
	
	/**
	 * 获取
	 *
	 * @return first
	 */
	public T getFirst() {
		return this.first;
	}
	
	/**
	 * 设置
	 *
	 * @param first
	 */
	public void setFirst(T first) {
		this.first = first;
	}
	
	/**
	 * 获取
	 *
	 * @return second
	 */
	public T getSecond() {
		return this.second;
	}
	
	/**
	 * 设置
	 *
	 * @param second
	 */
	public void setSecond(T second) {
		this.second = second;
	}
}

class PairAlg {
	public static boolean hasNulls(Pair<?> pair) {
		return pair.getFirst() == null || pair.getSecond() == null;
	}
	
	public static void swap(Pair<?> pair) {
		swapHelper(pair);
	}
	
	public static <T> void swapHelper(Pair<T> pair) {
		T t = pair.getFirst();
		pair.setFirst(pair.getSecond());
		pair.setSecond(t);
	}
}