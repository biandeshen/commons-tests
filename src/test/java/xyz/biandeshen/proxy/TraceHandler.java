package xyz.biandeshen.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author fjp
 * @Title: TraceHandler
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/8/811:01
 */
public class TraceHandler implements InvocationHandler {
	
	private Object target;
	
	public TraceHandler(Object target) {
		this.target = target;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		//System.err.println(method.getName());
		if ("output".equals(method.getName())) {
			System.out.print(target);
			System.out.print("." + method.getName() + "(");
			if (args != null) {
				for (int i = 0; i < args.length; i++) {
					System.out.print(args[i]);
					if (i < args.length - 1) {
						System.out.print(",");
					}
				}
			}
			System.out.println(")");
		}
		return method.invoke(target, args);
	}
	
}
