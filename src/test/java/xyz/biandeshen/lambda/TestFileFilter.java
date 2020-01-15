package xyz.biandeshen.lambda;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * FileName: TestFileFilter
 * Author:   admin
 * Date:     2020/1/13 21:34
 * Description: 测试自定义FileFilter函数式接口
 * History:
 * <author>          <time>          <version>
 * admin           2020/1/13           版本号
 */
public class TestFileFilter {
	public static String getFilterContent(String fileName, WordFilter wordFilter) throws IOException {
		StringBuilder sb = new StringBuilder();
		try (BufferedReader bf = new BufferedReader(new FileReader(new File(fileName)))) {
			String line;
			while ((line = bf.readLine()) != null) {
				if (!wordFilter.filter(line)) {
					sb.append(line);
				}
			}
		}
		return sb.toString();
	}
	
	
	public static void main(String[] args) {
		try {
			String filterContent = getFilterContent("E://test.txt", word -> word.contains("和谐") && word.contains("1"));
			System.out.println("filterContent = " + filterContent);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
