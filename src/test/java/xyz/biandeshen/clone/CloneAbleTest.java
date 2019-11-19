package xyz.biandeshen.clone;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.Date;

/**
 * @author fjp
 * @Title: CloneAbleTest
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/7/1810:56
 */
public class CloneAbleTest {

}

class A implements Cloneable, Comparable<A> {
	private String name;
	
	private Date date;
	
	A() {
	
	}
	
	public A clone() throws CloneNotSupportedException {
		//	call object.clone()
		A cloned = (A) super.clone();
		//	clone mutable fields
		cloned.date = (Date) date.clone();
		
		return cloned;
	}
	
	
	@Override
	public int compareTo(A o) {
		return 0;
	}
	
	int testA(String a, String b) {
		return a.length() + b.length();
	}
}

