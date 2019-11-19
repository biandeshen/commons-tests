package xyz.biandeshen.thread;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * @author fjp
 * @Title: TestForkJoin2
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/10/2823:34
 */
public class TestForkJoin2 {
	
	public static void main(String[] args) {
		FolderProcessor system = new FolderProcessor("C:\\Windows", "log");
		FolderProcessor apps = new FolderProcessor("C:\\Program Files", "log");
		FolderProcessor documents = new FolderProcessor("C:\\Documents And Settings", "log");
		ForkJoinPool pool = new ForkJoinPool(3);
		pool.execute(system);
		pool.execute(apps);
		pool.execute(documents);
		
		
		do {
			System.out.printf("******************************************\n");
			System.out.printf("Main: Parallelism: %d\n", pool.getParallelism());
			System.out.printf("Main: Active Threads: %d\n", pool.getActiveThreadCount());
			System.out.printf("Main: Task Count: %d\n", pool.getQueuedTaskCount());
			System.out.printf("Main: Steal Count: %d\n", pool.getStealCount());
			System.out.printf("******************************************\n");
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while ((!system.isDone()) || (!apps.isDone()) || (!documents.
				isDone()));
		
		List<String> results;
		results = system.join();
		System.out.printf("System: %d files found.\n", results.size());
		results = apps.join();
		System.out.printf("Apps: %d files found.\n", results.size());
		results = documents.join();
		System.out.printf("Documents: %d files found.\n", results.
				size());
		
	}
	
}

class FolderProcessor extends RecursiveTask<List<String>> {
	
	private static final long serialVersionUID = -187537300687354387L;
	
	private String path;
	
	private String extension;
	
	
	public FolderProcessor(String path, String extension) {
		this.path = path;
		this.extension = extension;
	}
	
	/**
	 * The main computation performed by this task.
	 *
	 * @return the result of the computation
	 */
	@Override
	protected List<String> compute() {
		List<FolderProcessor> folderProcessors = new ArrayList<>();
		List<String> fileList = new ArrayList<>();
		
		File file = new File(path);
		File[] files = file.listFiles();
		if (files != null) {
			for (File file1 : files) {
				if (file1.isDirectory()) {
					folderProcessors.add((FolderProcessor) new FolderProcessor(file1.getAbsolutePath(), extension).fork());
				} else {
					if (checkFile(file1.getName())) {
						fileList.add(file1.getAbsolutePath());
					}
				}
				if (folderProcessors.size() > 50) {
					System.out.printf("%s: %d tasks ran.\n", file.getAbsolutePath(), folderProcessors.size());
				}
			}
		}
		addResultsFromTasks(fileList, folderProcessors);
		return fileList;
	}
	
	private void addResultsFromTasks(List<String> fileList, List<FolderProcessor> folderProcessors) {
		for (FolderProcessor folderProcessor : folderProcessors) {
			fileList.addAll(folderProcessor.join());
		}
	}
	
	private boolean checkFile(String name) {
		return name.endsWith(extension);
	}
}
