package xyz.biandeshen.proxy;

/**
 * @author fjp
 * @Title: PrintTest
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/8/816:25
 */
public class PrintTest implements PrintInterface {
	
	private int x, y;
	
	private PrintTest(int[] ints) {
		this.x = ints[0];
		this.y = ints[1];
	}
	
	public PrintTest(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public PrintTest(Object obj) {
		int[] ints = new int[2];
		ints[0] = ((PrintTest) obj).getX();
		ints[1] = ((PrintTest) obj).getY();
		new PrintTest(ints);
	}
	
	@Override
	public <T> void output(T t) {
		System.err.print("t.getClass().getName() = " + t.getClass().getName());
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("{");
		sb.append("\"x\":").append(x);
		sb.append(",\"y\":").append(y);
		sb.append('}');
		return sb.toString();
	}
	
	/**
	 * Gets the value of x.
	 *
	 * @return the value of x
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Gets the value of y.
	 *
	 * @return the value of y
	 */
	public int getY() {
		return y;
	}
}
