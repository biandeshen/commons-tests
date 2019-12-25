package xyz.biandeshen.Java程序性能优化.designpatterns;

import javafx.event.ActionEvent;
import javafx.event.Event;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Vector;

/**
 * @author fjp
 * @Title: ObserverPattern
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/12/2510:25
 */
// 观察者模式
public class ObserverPattern {
	public static void main(String[] args) {
		ISubject iSubject = new ConcreteSubject();
		IObserver iObserver = new ConcreteObserver();
		IObserver iObserver2 = new ConcreteObserver();
		iSubject.attach(iObserver);
		iSubject.attach(iObserver2);
		iSubject.inform();
	}
}

// 主题接口 被观察者接口
interface ISubject {
	// 添加观察者
	void attach(IObserver iObserver);
	
	// 删除观察者
	void detach(IObserver iObserver);
	
	// 通知所有观察者
	void inform();
	
	// Q : 为什么没有通知部分观察者的情况呢?
	// T : 依据六大原则的单一职责来看,如果已注册的观察者并不都会被通知,那么它应该属于另一个主题接口
	// 如果真的有相关的逻辑需要针对不同的观察者进行通知,那么依据开闭原则在主题的子类中自行定义(或者就修改
	// 观察者子类具体的处理操作,达到相同的效果)
	
}

// 观察者接口
interface IObserver {
	// 更新观察者
	void update(Event event);
}

class ConcreteSubject implements ISubject {
	private Vector<IObserver> observers = new Vector<>();
	
	@Override
	public void attach(IObserver iObserver) {
		observers.addElement(iObserver);
	}
	
	@Override
	public void detach(IObserver iObserver) {
		observers.removeElement(iObserver);
	}
	
	@Override
	public void inform() {
		Event evt = new ActionEvent();
		for (IObserver observer : observers) {
			// 在此通知所有观察者 添加判断可以根据条件针对不同观察者进行通知
			if (observer instanceof ConcreteObserver) {
				observer.update(evt);
			}
		}
	}
}

class ConcreteObserver implements IObserver {
	private String observerName = RandomStringUtils.randomAscii(1);
	
	@Override
	public void update(Event event) {
		// 此处判断可以根据条件做不同处理
		if (event instanceof ActionEvent) {
			System.out.println("observer receives infomation!" + " " + observerName);
		}
	}
}