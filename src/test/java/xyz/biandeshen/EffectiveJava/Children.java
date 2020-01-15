/**
 * FileName: Test2
 * Author:   admin
 * Date:     2020/1/8 16:37
 * Description: 返回类型不同的重载方法
 * History:
 * <author>          <time>          <version>
 * admin           2020/1/8           版本号
 */
package xyz.biandeshen.EffectiveJava;

/**
 * 〈返回类型不同的重载方法〉
 *
 * @author admin
 * @since 1.0.0
 */
public class Children extends Parent<String> {
	
	@Override
	String x() {
		return "abc";
	}
	
	public static void main(String[] args) {
		Children children = new Children();
		System.out.println("children.x() = " + children.x());
	}
}

abstract class Parent<T> {
	abstract T x();
}