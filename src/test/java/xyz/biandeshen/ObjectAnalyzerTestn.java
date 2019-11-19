package xyz.biandeshen;

import java.util.ArrayList;

public class ObjectAnalyzerTestn {
	public static void main(String[] args) {
		ArrayList<Integer> squares = new ArrayList<>();
		for (int i = 1; i <= 5; i ++) {
			squares.add(i * i);
		}
		//int randomNum = 555;
		System.out.println(new ObjectAnalyzer().toString(squares));
		//System.out.println(new ObjectAnalyzer().toString(randomNum));
	}
}
