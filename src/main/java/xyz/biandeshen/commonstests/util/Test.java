package xyz.biandeshen.commonstests.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fjp
 * @Title: Test
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/11/2214:57
 */
public class Test {
	private List<String>[] stringLists;
	
	public Test(List<String>[] stringList) {
		this.stringLists = stringList;
	}
	
	public int size() {
		final List<String>[] stringsLists = this.stringLists;
		this.stringLists = new ArrayList[0];
		int sum = 0;
		for (List<String> stringsList : stringsLists) {
			sum += stringsList.size();
		}
		return sum;
	}
	
	public static void main(String[] args) {
		List<String> list1 = new ArrayList<>();
		list1.add("0");
		List<String>[] stringList = new ArrayList[1];
		stringList[0] = list1;
		Test test = new Test(stringList);
		System.out.println("test.size() = " + test.size());
		
	}
}
