package xyz.biandeshen.thread;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author fjp
 * @Title: TestSemaphore2
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/11/1115:17
 */
public class TestSemaphore2 {
	public static void main(String[] args) {
		BoundedHashSet<String> boundedHashSet = new BoundedHashSet<>(10);
		for (int i = 0; i < 100; i++) {
			new Thread(() -> {
				int i1 = new SecureRandom().nextInt(10);
				System.out.println("add i = " + i1);
				try {
					System.out.println("boundedHashSet.add(String.valueOf(i)) = " + boundedHashSet.add(String.valueOf(i1)));
					TimeUnit.MILLISECONDS.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}).start();
			new Thread(() -> {
				int i12 = new SecureRandom().nextInt(10);
				//System.out.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());
				System.out.println("remove i = " + i12);
				System.out.println("boundedHashSet.remove(String.valueOf(i)) = " + boundedHashSet.remove(String.valueOf(i12)));
				try {
					TimeUnit.MILLISECONDS.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}).start();
		}
	}
	
}

class BoundedHashSet<T> {
	private final Set<T> set;
	private final Semaphore sem;
	
	public BoundedHashSet(int bound) {
		this.set = Collections.synchronizedSet(new HashSet<T>());
		sem = new Semaphore(bound);
	}
	
	public boolean add(T o) throws InterruptedException {
		sem.acquire();
		boolean wasAdded = false;
		try {
			wasAdded = set.add(o);
			return wasAdded;
		} finally {
			if (!wasAdded) {sem.release();}
			System.out.println("sem = " + sem);
		}
	}
	
	public boolean remove(Object o) {
		boolean wasRemoved = set.remove(o);
		if (wasRemoved) {sem.release();}
		System.out.println("sem = " + sem);
		return wasRemoved;
	}
}
