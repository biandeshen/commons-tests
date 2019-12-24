package xyz.biandeshen.Java程序性能优化.designpatterns;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @author fjp
 * @Title: singleton
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/12/1715:38
 */
public class Singleton implements Serializable {
	private static final Logger logger = LoggerFactory.getLogger(Singleton.class);
	
	private Singleton() {
		System.out.println("Singleton is create ...");
	}
	
	private static final long serialVersionUID = 20191217154110869L;
	
	private static Singleton singleton = new Singleton();
	
	private static Singleton instance = null;
	
	public static Singleton getInstance() {
		return singleton;
	}
	
	public static synchronized Singleton getInstance2() {
		if (instance == null) {
			instance = new Singleton();
		}
		return instance;
	}
	
	private static class SingletonHolder {
		public SingletonHolder() {
			System.out.println("\"inner class was created!\" = " + "inner class was created!");
		}
		
		private static Singleton instance = new Singleton();
	}
	
	public static Singleton getInstance3() {
		return SingletonHolder.instance;
	}
	
	private Object readResolve() {
		return singleton;
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Singleton singleton = Singleton.getInstance3();
		System.out.println("singleton = " + singleton);
		
		//Phaser phaser = new Phaser(10) {
		//	@Override
		//	protected boolean onAdvance(int phase, int registeredParties) {
		//		//当所有线程都完成了一个任务的时候，会回调。
		//		System.out.println("完成了第" + phase + "个屏障");
		//		//true:后面的屏障无效。false:保持屏障的有效性
		//		return super.onAdvance(phase, registeredParties);
		//	}
		//};
		//ExecutorService executorService = Executors.newFixedThreadPool(10);
		//
		////phaser.awaitAdvance(2);
		//for (int i = 0; i < 5; i++) {
		//	long startTime = Instant.now().toEpochMilli();
		//	executorService.execute(() -> {
		//		phaser.arriveAndAwaitAdvance();
		//		for (int j = 0; j < 100000; j++) {
		//			Singleton.getInstance();
		//		}
		//		//System.out.println("System.currentTimeMillis() - startTime = " + (System.currentTimeMillis() -
		//		// startTime));
		//		logger.info("System.currentTimeMillis() - startTime = {}", (System.currentTimeMillis() - startTime));
		//	});
		//}
		//
		//
		//for (int i = 0; i < 5; i++) {
		//	long startTime = Instant.now().toEpochMilli();
		//	executorService.execute(() -> {
		//		phaser.arriveAndAwaitAdvance();
		//		for (int j = 0; j < 100000; j++) {
		//			Singleton.getInstance3();
		//		}
		//		//System.out.println("System.currentTimeMillis() - startTime3 = " + (System.currentTimeMillis() -
		//		// startTime));
		//		logger.info("System.currentTimeMillis() - startTime3 = {}", (System.currentTimeMillis() - startTime));
		//	});
		//}
		//executorService.shutdown();
		//try {
		//	executorService.awaitTermination(5, TimeUnit.SECONDS);
		//} catch (InterruptedException e) {
		//	e.printStackTrace();
		//}
		
		
		//Singleton singleton;
		//Singleton singleton1 = Singleton.getInstance();
		////	将实例串行化到文件
		//FileOutputStream fos = new FileOutputStream("Singleton.txt");
		//ObjectOutputStream oos = new ObjectOutputStream(fos);
		//oos.writeObject(singleton1);
		//oos.flush();
		//oos.close();
		////	文件中读取原有单例类
		//FileInputStream fis = new FileInputStream("Singleton.txt");
		//ObjectInputStream ois = new ObjectInputStream(fis);
		//singleton = (Singleton) ois.readObject();
		//Assert.assertEquals(singleton, singleton1);
	}
	
	@Test
	public void test() throws IOException, ClassNotFoundException {
		Singleton singleton;
		Singleton singleton1 = Singleton.getInstance();
		//	将实例串行化到文件
		FileOutputStream fos = new FileOutputStream("Singleton.txt");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(singleton1);
		oos.flush();
		oos.close();
		//	文件中读取原有单例类
		FileInputStream fis = new FileInputStream("Singleton.txt");
		ObjectInputStream ois = new ObjectInputStream(fis);
		singleton = (Singleton) ois.readObject();
		Assert.assertEquals(singleton, singleton1);
	}
}
