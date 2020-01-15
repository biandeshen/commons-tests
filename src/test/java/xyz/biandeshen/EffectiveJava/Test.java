/**
 * FileName: Test
 * Author:   admin
 * Date:     2020/1/8 16:12
 * Description: 博客： https://www.ctolib.com/topics-1449.html
 * History:
 * <author>          <time>          <version>
 * admin           2020/1/8           版本号
 */
package xyz.biandeshen.EffectiveJava;

public class Test<I extends Integer> {
	public I n;
	
	<L extends Long> void x(I i, L l) {
		System.out.println("i.intValue()+\",\"+l.intValue() = " + i.intValue() + "," + l.intValue());
	}
}