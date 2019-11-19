package xyz.biandeshen.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Deque;
import java.util.concurrent.Exchanger;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * @author fjp
 * @Title: TestExchager
 * @ProjectName commons-tests
 * @Description: Two-Party Barrier ==== Exchanger 两方交换栅栏(代码中休眠为格式需要,实际无需休眠)
 * @date 2019/11/129:18
 */
@SuppressWarnings("all")
public class TestExchager {
	public static void main(String[] args) {
		Exchanger<Deque<String>> exchanger = new Exchanger<>();
		new Thread(new Producer(exchanger)).start();
		new Thread(new Consumer(exchanger)).start();
		try {
			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	static class Producer implements Runnable {
		private static final Logger logger = LoggerFactory.getLogger(Producer.class);
		private static Exchanger<Deque<String>> dequeExchanger;
		private static Deque<String> stringDeque = new LinkedBlockingDeque<>();
		
		public Producer(Exchanger<Deque<String>> dequeExchanger) {
			this.dequeExchanger = dequeExchanger;
		}
		
		@Override
		public void run() {
			Thread.currentThread().setName("Producer-");
			for (int i = 0; i < 5; i++) {
				try {
					stringDeque.addFirst(String.valueOf(i));
					logger.info(Thread.currentThread().getName() + "交换前size: = " + stringDeque.size());
					stringDeque = dequeExchanger.exchange(stringDeque);
					// 通过程序休眠来确定代码执行顺序
					// 由于 exchanger 会阻塞,直到另一个结果产生,才会执行交换,所以可在此处使用休眠
					TimeUnit.MILLISECONDS.sleep(10);
					logger.info(Thread.currentThread().getName() + "交换后size: = " + stringDeque.size());
					System.out.println();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	static class Consumer implements Runnable {
		private static final Logger logger = LoggerFactory.getLogger(Consumer.class);
		private static Exchanger<Deque<String>> dequeExchanger;
		private static Deque<String> stringDeque = new LinkedBlockingDeque<>();
		
		public Consumer(Exchanger<Deque<String>> dequeExchanger) {
			this.dequeExchanger = dequeExchanger;
		}
		
		@Override
		public void run() {
			Thread.currentThread().setName("Consumer-");
			for (int i = 0; i < 5; i++) {
				try {
					TimeUnit.MILLISECONDS.sleep(2);
					logger.info(Thread.currentThread().getName() + "交换前size: = " + stringDeque.size());
					stringDeque = dequeExchanger.exchange(stringDeque);
					logger.info(Thread.currentThread().getName() + "交换后size: = " + stringDeque.size());
					logger.info(Thread.currentThread().getName() + "stringDeque.getFirst() = " + stringDeque.peekFirst());
					TimeUnit.MILLISECONDS.sleep(8);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
