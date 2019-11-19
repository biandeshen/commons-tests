package xyz.biandeshen.innerclass;

import xyz.biandeshen.innerclass.TalkingClock.TimePrinter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.Date;

/**
 * @author fjp
 * @Title: TalkingClock
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/7/1913:49
 */
public class TalkingClock {
	
	private int interval;
	private boolean beep;
	
	private static String innerText;
	
	public TalkingClock(int interval, boolean beep) {
		this.interval = interval;
		this.beep = beep;
	}
	
	public void start() {
		ActionListener actionListener = new TimePrinter();
		Timer timer = new Timer(interval, actionListener);
		timer.start();
	}
	
	public static void run(int interval, boolean beep) {
		new TalkingClock(interval, beep).start();
	}
	
	//inner class 内部类
	public class TimePrinter implements ActionListener {
		// 内部类中声明的所有静态域都必须是final
		private final static String name = "test";
		
		//内部类不能有static方法
		//public final static String getName() {
		//	return innerText;
		//}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.println("At the tone,the time is  = " + new Date());
			//boolean beep = TalkingClock.this.beep;
			if (TalkingClock.this.beep) {
				Toolkit.getDefaultToolkit().beep();
				System.out.println("name:" + name.hashCode());
			}
		}
	}
}


class TalkingClock1 {
	public TalkingClock1() {
	
	}
	
	public void start(int interval, boolean beep) {
		//class TimePrinter implements ActionListener {
		//	// 内部类中声明的所有静态域都必须是final
		//	private final static String name = "test";
		//
		//	//内部类不能有static方法
		//	//public final static String getName() {
		//	//	return innerText;
		//	//}
		//	@Override
		//	public void actionPerformed(ActionEvent e) {
		//		System.out.println("At the tone,the time is  = " + new Date());
		//		//boolean beep = TalkingClock.this.beep;
		//		if (beep) {
		//			Toolkit.getDefaultToolkit().beep();
		//			System.out.println("name:" + name.hashCode());
		//		}
		//	}
		//}
		ActionListener actionListener = new ActionListener() {
			// 内部类中声明的所有静态域都必须是final
			private final static String name = "test";
			
			//内部类不能有static方法
			//public final static String getName() {
			//	return innerText;
			//}
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("At the tone,the time is  = " + new Date());
				//boolean beep = TalkingClock.this.beep;
				if (beep) {
					Toolkit.getDefaultToolkit().beep();
					System.out.println("name:" + name.hashCode());
				}
			}
		};
		Timer timer = new Timer(interval, actionListener);
		timer.start();
	}
	
	public static void run(int interval, boolean beep) {
		new TalkingClock(interval, beep).start();
	}
	
	//inner class 内部类
	
}


class TalkingClock2 {
	public TalkingClock2() {
	
	}
	
	//1.0 simplifier 简化
	public void start(int interval, boolean beep) {
		Timer timer = new Timer(interval, actionListener -> new ActionListener() {
			// 内部类中声明的所有静态域都必须是final
			private final static String name = "test";
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("At the tone,the time is  = " + new Date());
				//boolean beep = TalkingClock.this.beep;
				if (beep) {
					Toolkit.getDefaultToolkit().beep();
					System.out.println("name:" + name.hashCode());
				}
			}
		});
		timer.start();
		
		//2.0 simplifier 简化
		Timer timer2 = new Timer(interval, actionListener -> {
			// 内部类中声明的所有静态域都必须是final
			final String name = "test";
			System.out.println("At the tone,the time is  = " + new Date());
			//boolean beep = TalkingClock.this.beep;
			if (beep) {
				Toolkit.getDefaultToolkit().beep();
				System.out.println("name:" + name.hashCode());
			}
		});
		timer2.start();
	}
	
	
	public static void run(int interval, boolean beep) {
		new TalkingClock(interval, beep).start();
	}
	
	//inner class 内部类
	
}
