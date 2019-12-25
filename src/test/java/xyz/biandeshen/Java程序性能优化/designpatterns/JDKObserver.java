package xyz.biandeshen.Java程序性能优化.designpatterns;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Observable;
import java.util.Observer;

/**
 * @author fjp
 * @Title: JDKObserver
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/12/2511:01
 */
// JDK 自带Observer实现
public class JDKObserver {
	public static void main(String[] args) {
		Observable iSubject = new JDKConcreteObservable();
		Observer iObserver = new JDKConcreteObserver();
		Observer iObserver2 = new JDKConcreteObserver();
		iSubject.addObserver(iObserver);
		iSubject.addObserver(iObserver2);
		((JDKConcreteObservable) iSubject).setChanged();
		iSubject.notifyObservers();
	}
}


class JDKConcreteObservable extends Observable {
	
	@Override
	public synchronized void addObserver(Observer o) {
		super.addObserver(o);
	}
	
	@Override
	public synchronized void deleteObserver(Observer o) {
		super.deleteObserver(o);
	}
	
	@Override
	public void notifyObservers() {
		super.notifyObservers();
	}
	
	@Override
	protected synchronized void setChanged() {
		super.setChanged();
	}
}

class JDKConcreteObserver implements Observer {
	private String observerName = RandomStringUtils.randomAscii(1);
	
	@Override
	public void update(Observable o, Object arg) {
		System.out.println("observer receives infomation!" + " " + observerName);
	}
}