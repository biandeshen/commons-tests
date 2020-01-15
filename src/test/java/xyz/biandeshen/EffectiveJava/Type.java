/**
 * FileName: Type
 * Author:   admin
 * Date:     2020/1/8 16:20
 * Description: 博客 https://www.ctolib.com/topics-1449.html
 * History:
 * <author>          <time>          <version>
 * admin           2020/1/8           版本号
 */
package xyz.biandeshen.EffectiveJava;   /**
 * @Title: Type
 * @ProjectName commons-tests
 * @Description: TODO
 * @author fjp
 * @date 2020/1/816:20
 */

/**
 * 〈博客 https://www.ctolib.com/topics-1449.html〉
 *
 * @author admin
 * @since 1.0.0
 */
public interface Type<T> {}

class C implements Type<Type<? super C>> {}

class D<P> implements Type<Type<? super D<D<P>>>> {}

class Test2 {
	//Type<? super C> c = new C();
	//Type<? super D<Byte>> d = new D<Byte>();
}