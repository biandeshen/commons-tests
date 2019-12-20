package xyz.biandeshen.thread;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * @author fjp
 * @Title: TestPhaser
 * @ProjectName commons-tests
 * @Description: TODO 重要 需理解
 * @date 2019/11/1514:04
 */
public class TestPhaser {
	public static void main(String[] args) {
		Phaser phaser = new Phaser(3);
		FileSearch system = new FileSearch("C:\\Windows", "log", phaser);
		FileSearch apps = new FileSearch("C:\\Program Files", "log", phaser);
		FileSearch documents = new FileSearch("C:\\ProgramData", "log", phaser);
		
		Thread systemThread = new Thread(system, "System");
		systemThread.start();
		Thread appsThread = new Thread(apps, "Apps");
		appsThread.start();
		Thread documedntsThread = new Thread(documents, "Documents");
		documedntsThread.start();
		
		try {
			systemThread.join();
			appsThread.join();
			documedntsThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Terminated: " + phaser.isTerminated());
		//System.out.println("phaser.getPhase() = " + phaser.getPhase());
		//System.out.println("phaser.getArrivedParties() = " + phaser.getArrivedParties());
		//System.out.println("phaser.getRegisteredParties() = " + phaser.getRegisteredParties());
		//System.out.println("phaser.getUnarrivedParties() = " + phaser.getUnarrivedParties());
	}
}

class FileSearch implements Runnable {
	// 存储查找的文件夹
	private String initPath;
	// 存储要查找的文件的扩展名
	private String end;
	// 存储查找到的文件的完整路径
	private List<String> resultList;
	// 控制任务不同阶段的同步
	private Phaser phaser;
	
	public FileSearch(String initPath, String end, Phaser phaser) {
		this.initPath = initPath;
		this.end = end;
		this.phaser = phaser;
		this.resultList = new ArrayList<>();
	}
	
	@Override
	public void run() {
		// 放在此处,即表示到达并等待 在其它线程创建后同时执行,所有线程同时起步
		phaser.arriveAndAwaitAdvance();
		System.out.printf("%s: Starting. \n", Thread.currentThread().getName());
		File file = new File(initPath);
		if (file.isDirectory()) {
			directoryProcess(file);
		}
		if (!checkResults()) {
			return;
		}
		filterResults();
		if (!checkResults()) {
			return;
		}
		showInfo();
		phaser.arriveAndDeregister();
		System.out.printf("%s: Work completed. \n", Thread.currentThread().getName());
	}
	
	// 目录过滤处理
	private void directoryProcess(File file) {
		File[] list = file.listFiles();
		if (list != null) {
			for (File file1 : list) {
				if (file1.isDirectory()) {
					directoryProcess(file1);
				} else {
					fileProcess(file1);
				}
			}
		}
	}
	
	// 文件过滤并置入列表
	private void fileProcess(File file) {
		if (file.getName().endsWith(end)) {
			resultList.add(file.getAbsolutePath());
		}
	}
	
	// 一阶段已查到文件列表过滤
	private void filterResults() {
		List<String> newResultList = new ArrayList<>();
		long actualDate = System.currentTimeMillis();
		for (String s : resultList) {
			File file = new File(s);
			long fileDate = file.lastModified();
			if (actualDate - fileDate < TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)) {
				newResultList.add(s);
			}
		}
		resultList = newResultList;
	}
	
	// 一阶段及二阶段文件过渡,文件列表处理
	private boolean checkResults() {
		// 如果结果为空,则注销,通知Phaser当前线程已结束此阶段,并不再参与接下来的阶段操作
		if (resultList.isEmpty()) {
			System.out.printf("%s: Phase %d: 0 result. \n", Thread.currentThread().getName(), phaser.getPhase());
			System.out.printf("%s: Phase %d: 0 End. \n", Thread.currentThread().getName(), phaser.getPhase());
			phaser.arriveAndDeregister();
			return false;
		} else {
			// 结果集不为空 查找的文件数打印到控制台 调用到达及等待方法
			// 通知 Phaser对象当前线程已经完成了当前阶段,需要阻塞指定其他线程也都完成当前阶段
			System.out.printf("%s: Phase %d: %d result. \n", Thread.currentThread().getName(), phaser.getPhase(),
			                  resultList.size());
			phaser.arriveAndAwaitAdvance();
			return true;
		}
	}
	
	private void showInfo() {
		for (String s : resultList) {
			File file = new File(s);
			System.out.printf("%s: %s \n", Thread.currentThread().getName(), file.getAbsolutePath());
		}
		phaser.arriveAndAwaitAdvance();
	}
}
