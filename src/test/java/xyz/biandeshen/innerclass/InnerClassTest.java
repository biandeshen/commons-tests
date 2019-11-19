package xyz.biandeshen.innerclass;

import com.google.common.collect.Lists;
import org.junit.Test;
import xyz.biandeshen.innerclass.TalkingClock.TimePrinter;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.*;

/**
 * @author fjp
 * @Title: InnerClassTest
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/7/1914:18
 */
public class InnerClassTest {
	public static void main(String[] args) {
		TalkingClock talkingClock = new TalkingClock(1000, true);
		talkingClock.start();
		
		TalkingClock talkingClock2 = new TalkingClock(1001, true);
		talkingClock2.start();
		
		JOptionPane.showMessageDialog(null, "Quit program?");
		System.exit(0);
	}
	
	@Test
	public void testTalkingClock1() {
		TalkingClock1 talkingClock1 = new TalkingClock1();
		talkingClock1.start(1000, true);
		
		//while (true){
		JOptionPane.showMessageDialog(null, "Quit program?");
		System.exit(0);
		//}
		
	}
	
	@Test
	public void TestFinalInstead() {
		final int[] counter = {0};
		Date[] dates = new Date[100];
		for (int i = 0; i < dates.length; i++) {
			dates[i] = new Date() {
				@Override
				public int compareTo(Date anotherDate) {
					counter[0]++;
					return super.compareTo(anotherDate);
				}
			};
		}
		Arrays.sort(dates);
		Arrays.sort(dates, Collections.reverseOrder());
		System.out.println("counter = " + counter[0]);
	}
	
	
	@Test
	//测试 双括号初始化
	public void testDoubleBraceInitialization() {
		ArrayList<String> strings = new ArrayList<>();
		strings.add("first");
		strings.add("second");
		invite(strings);
		
		// 双括号初始化
		invite(new ArrayList<String>(){{add("third");add("fourth");}});
		
		// 双括号初始化扩展
		ArrayList<String> strings2 = new ArrayList<String>(){
			{add("s2first");}
			{add("s2second");}
			{add("s2third");add("s2fourth");}
		};
		invite(strings2);
	}
	
	public void invite(ArrayList<String> arrayList) {
		////1.0 simplifier 简化
		//for (String x : arrayList) {
		//	System.out.println(x);
		//}
		////2.0 simplifier 简化
		//arrayList.forEach(action -> System.out.println(action));
		//3.0 simplifier 简化
		arrayList.forEach(System.out::println);
		
	}
}
