package xyz.biandeshen;

/**
 * @author fjp
 * @Title: a
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/9/2510:35
 */
public class OverrideTest extends Oparent{
	private static String z;
	
	public OverrideTest() {
	}
	
	public OverrideTest(String a) {
		z = a;
	}
	public static void main(String[] args) {
		OverrideTest ss = new OverrideTest();
		//OverrideTest ss = new OverrideTest("abc");
	}
	
	public void ABC(){
		super.ABC();
		//System.out.println(z +"子类");
	}
}

class Oparent{
	public Oparent() {
		ABC();
	}
	public void ABC(){
		System.out.println("父类");
	}
}
