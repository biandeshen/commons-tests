package xyz.biandeshen.thread;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author fjp
 * @Title: TestFileCrawler
 * @ProjectName commons-tests`
 * @Description: TODO
 * @date 2019/11/611:06
 */
@SuppressWarnings("all")
public class ForkJoinBlockingQueue {
	public static void main(String[] args) {
		//查询路径
		File root = new File("E:\\");
		FileFilter fileFilter = file -> {
			if (file.isDirectory()) {
				return true;
			}
			//查询关键字
			return StringUtils.contains(file.getName(), ".pdf") && StringUtils.contains(file.getName(), "并发");
		};
		File[] roots = new File[10];
		roots[0] = root;
		ProduceAndConsumer.startIndexing(roots, fileFilter);
		new Thread(() -> {
			BlockingDeque<File> indexBlockingDeque = ProduceAndConsumer.getIndexBlockingDeque();
			try {
				while (true) {
					File take = indexBlockingDeque.takeFirst();
					System.out.println("查詢结果为 = " + take.getAbsolutePath());
				}
			} catch (InterruptedException e) {
				System.out.println("2 " + e);
				try {
					Thread.currentThread().wait(10);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}).start();
	}
}

@SuppressWarnings("all")
class TestFileCrawler extends RecursiveTask {
	private static final long serialVersionUID = 8921400203791115241L;
	private final BlockingQueue<File> fileBlockingQueue;
	private final FileFilter fileFilter;
	private final File root;
	
	public TestFileCrawler(BlockingQueue<File> fileBlockingQueue, FileFilter fileFilter, File root) {
		this.fileBlockingQueue = fileBlockingQueue;
		this.fileFilter = fileFilter;
		this.root = root;
	}
	
	/**
	 * The main computation performed by this task.
	 *
	 * @return the result of the computation
	 */
	@Override
	protected Object compute() {
		List<ForkJoinTask> folderProcessors = new ArrayList<>();
		try {
			
			if (root.isDirectory()) {
				File[] entries = root.listFiles(fileFilter);
				if (entries != null) {
					for (File entry : entries) {
						if (entry.isDirectory()) {
							//recursiveTask 方式
							ForkJoinTask testFileCrawler = new TestFileCrawler(fileBlockingQueue, fileFilter, entry);
							testFileCrawler.fork();
							folderProcessors.add(testFileCrawler);
						} else if (!alreadyIndexed(entry)) {
							fileBlockingQueue.put(entry);
						}
					}
				}
			}
			
			//recursiveTask
			for (ForkJoinTask folderProcessor : folderProcessors) {
				fileBlockingQueue.addAll((Collection<? extends File>) folderProcessor.join());
			}
			
		} catch (Exception e) {
			try {
				Thread.currentThread().wait(10);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
				Thread.currentThread().interrupt();
			}
		}
		return fileBlockingQueue;
	}
	
	
	private boolean alreadyIndexed(File entry) {
		return fileBlockingQueue.contains(entry);
	}
}

@SuppressWarnings("all")
class Indexer implements Runnable {
	private final BlockingQueue<File> fileBlockingQueue;
	private final BlockingDeque<File> fileBlockingDeque;
	
	public Indexer(BlockingQueue<File> fileBlockingQueue, BlockingDeque<File> fileBlockingDeque) {
		this.fileBlockingQueue = fileBlockingQueue;
		this.fileBlockingDeque = fileBlockingDeque;
	}
	
	/**
	 * When an object implementing interface <code>Runnable</code> is used
	 * to create a thread, starting the thread causes the object's
	 * <code>run</code> method to be called in that separately executing
	 * thread.
	 * <p>
	 * The general contract of the method <code>run</code> is that it may
	 * take any action whatsoever.
	 *
	 * @see Thread#run()
	 */
	@Override
	public void run() {
		try {
			while (true) {
				indexFile(fileBlockingQueue.take());
			}
		} catch (InterruptedException e) {
			System.out.println("3 " + e);
			try {
				Thread.currentThread().wait(10);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	private void indexFile(File take) {
		try {
			//System.err.println("添加索引路径为: " + take.getAbsolutePath());
			fileBlockingDeque.putLast(take);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

class ProduceAndConsumer {
	private static final int N_CONSUMERS = Runtime.getRuntime().availableProcessors();
	/**
	 * 索引队列
	 */
	private static final BlockingDeque<File> indexBlockingDeque = new LinkedBlockingDeque<>();
	
	static void startIndexing(File[] roots, FileFilter fileFilter) {
		BlockingQueue<File> blockingQueue = new LinkedBlockingQueue<>();
		FileFilter DefaultFileFilter = pathname -> false;
		fileFilter = fileFilter == null ? DefaultFileFilter : fileFilter;
		ForkJoinPool forkJoinPool = new ForkJoinPool(N_CONSUMERS - 1);
		for (File root : roots) {
			forkJoinPool.execute(new TestFileCrawler(blockingQueue, fileFilter, root));
		}
		for (int i = 0; i < forkJoinPool.getPoolSize(); i++) {
			forkJoinPool.execute(new Indexer(blockingQueue, indexBlockingDeque));
		}
	}
	
	static BlockingDeque<File> getIndexBlockingDeque() {
		return indexBlockingDeque;
	}
}