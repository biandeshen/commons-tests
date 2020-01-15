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
 * @Description: 文件搜索
 * todo 尝试 以 毒丸 及 CountDownLatch 或 semaphore 来终止线程的执行
 * 跳出 where(true) 的循环
 * @date 2019/11/611:06
 */
@SuppressWarnings("all")
public class ForkJoinBlockingQueue {
	//查询路径
	private static File root = new File("C:\\");
	// private File root2 = new File("D:\\");
	// private File root3 = new File("C:\\");
	
	////查询规则
	private static final FileFilter fileFilter = file -> {
		if (file.isDirectory()) {
			return true;
		}
		////查询规则
		return StringUtils.contains(file.getName(), ".pdf") &&
				       StringUtils.contains(file.getName(), "阿里");
		//查询规则
		//return StringUtils.contains(file.getName().toLowerCase(), ".pdf");
	};
	
	public static void main(String[] args) {
		File[] roots = new File[10];
		roots[0] = root;
		//roots[1] = root2;
		//roots[2] = root3;
		
		//开启索引
		new Thread(() -> {
			try {
				ProduceAndConsumer.startIndexing(roots, fileFilter);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}).start();
		Thread thread = new Thread(() -> {
			BlockingDeque<File> indexBlockingDeque = ProduceAndConsumer.getIndexBlockingDeque();
			while (!Thread.currentThread().isInterrupted()) {
					try {
					File take = indexBlockingDeque.take();
					if (take != null) {
						System.out.println("查詢结果为 = " + take.getAbsolutePath());
					}
					if (take == null) {
						throw new InterruptedException();
					}
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
			System.out.println("查詢结束!");
		});
		thread.setDaemon(true);
		thread.start();
	}
}

@SuppressWarnings("all")
class TestFileCrawler extends RecursiveAction {
	private static final long serialVersionUID = 8921400203791115241L;
	private final BlockingQueue<File> fileBlockingQueue;
	private final transient FileFilter fileFilter;
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
	public void compute() {
		List<ForkJoinTask> folderProcessors = new ArrayList<>();
		
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
						try {
							fileBlockingQueue.put(entry);
						} catch (InterruptedException e) {
							e.printStackTrace();
							Thread.currentThread().interrupt();
						}
					}
				}
			}
		}
		//recursiveTask
		for (ForkJoinTask folderProcessor : folderProcessors) {
			fileBlockingQueue.addAll((Collection<? extends File>) folderProcessor.join());
		}
	}
	
	/**
	 * 是否已被索引 todo
	 *
	 * @param entry
	 * 		文件实体
	 *
	 * @return 是否被索引
	 */
	private boolean alreadyIndexed(File entry) {
		//return fileBlockingQueue.contains(entry);
		//return false;
		//索引队列中是否已经存在
		return ProduceAndConsumer.getIndexBlockingDeque().contains(entry);
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
	
	
	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			try {
				//File take = fileBlockingQueue.poll(10, TimeUnit.SECONDS);
				File take = fileBlockingQueue.take();
				indexFile(take);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		
	}
	
	private void indexFile(File take) throws InterruptedException {
		if (take != null) {
			fileBlockingDeque.put(take);
		}
	}
}

@SuppressWarnings("ALL")
class ProduceAndConsumer {
	private static final int N_CONSUMERS = Runtime.getRuntime().availableProcessors();
	/**
	 * 索引队列
	 */
	private static final BlockingDeque<File> indexBlockingDeque = new LinkedBlockingDeque<>();
	
	//文件队列
	private static final BlockingQueue<File> blockingQueue = new LinkedBlockingQueue<>();
	
	static void startIndexing(File[] roots, FileFilter fileFilter) throws InterruptedException {
		FileFilter DefaultFileFilter = pathname -> false;
		fileFilter = fileFilter == null ? DefaultFileFilter : fileFilter;
		
		ExecutorService executorService = Executors.newCachedThreadPool();
		ForkJoinPool forkJoinPool = new ForkJoinPool(N_CONSUMERS - 1);
		for (File root : roots) {
			forkJoinPool.execute(new TestFileCrawler(blockingQueue, fileFilter, root));
		}
		Future<?> submit = executorService.submit(new Indexer(blockingQueue, indexBlockingDeque));
		forkJoinPool.shutdown();
		executorService.awaitTermination(10, TimeUnit.SECONDS);
		submit.cancel(true);
		executorService.shutdown();
	}
	
	static BlockingDeque<File> getIndexBlockingDeque() {
		return indexBlockingDeque;
	}
}