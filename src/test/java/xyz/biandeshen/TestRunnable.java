package xyz.biandeshen;

import org.apache.poi.ss.formula.functions.T;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author fjp
 * @Title: TestRunnable
 * @ProjectName commons-tests
 * @Description: 队列测试
 * @date 2019/9/910:54
 */
public class TestRunnable {
	private static final int FILE_QUEUE_SIZE = 10;
	private static final int SEARCH_THREADS = 100;
	private static final File DUMMY = new File("");
	private static BlockingQueue<File> queue = new LinkedBlockingQueue<>();
	
	public static void main(String[] args) {
		TestRunnable testRunnable = new TestRunnable();
		testRunnable.server();
		TestRunnable testRunnable2 = new TestRunnable();
		testRunnable2.search(queue);
	}
	
	public void server() {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter base directory ( e.g. /opt/jdkl.8.0/src): ");
		String directory = in.nextLine();
		//while (true) {
		Runnable enumerator = new Runnable() {
			@Override
			public void run() {
				try {
					enumerate(new File(directory));
					//设置终止条件,即读取到队列第一条时所有已查询出
					queue.put(DUMMY);
				} catch (InterruptedException e) {
					
				}
			}
		};
		new Thread(enumerator).start();
		//}
	}
	
	public void search(BlockingQueue blockingQueue) {
		Scanner in = new Scanner(System.in);
		System.out.print(" Enter keyword (e.g.volatile):");
		String keyword = in.nextLine();
		for (int i = 0; i < SEARCH_THREADS; i++) {
			Runnable searcher = () -> {
				try {
					boolean done = false;
					while (true) {
						File file = queue.take();
						//终止条件,遍历完成
						if (file == DUMMY) {
							queue.put(file);
							//TODO 不停止,一直阻塞
							//done = true;
						} else {
							search(file, keyword);
						}
					}
				} catch (InterruptedException | IOException e) {
					e.printStackTrace();
				}
			};
			Thread thread = new Thread(searcher);
			thread.setName("searcher---" + i);
			thread.start();
		}
	}
	
	public static void enumerate(File directory) throws InterruptedException {
		File[] files = directory.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) enumerate(file);
				else queue.put(file);
			}
		}
	}
	
	public static void search(File file, String keyword) throws IOException {
		try (Scanner in = new Scanner(file, "UTF-8")) {
			int lineNumber = 0;
			while (in.hasNextLine()) {
				lineNumber++;
				String line = in.nextLine();
				if (line.contains(keyword))
					System.out.printf("%s\t:%s:%d:%s%n", Thread.currentThread().getName(), file.getPath(), lineNumber, line);
			}
		}
	}
	
}