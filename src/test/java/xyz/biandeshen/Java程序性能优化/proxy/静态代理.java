package xyz.biandeshen.Java程序性能优化.proxy;

import java.util.concurrent.TimeUnit;

/**
 * @author fjp
 * @Title: 静态代理
 * @ProjectName commons-tests
 * @Description: 静态代理
 * @date 2019/12/2011:02
 */
public class 静态代理 {
	public static void main(String[] args) {
		IDBQuery idbQuery = new DBQueryProxy();
		// 判断是否初始化 初始化时间 初始化顺序
		System.out.println("开始执行代理类！");
		System.out.println("idbQuery.request() = " + idbQuery.request());
		// 执行结果：
		// 开始执行代理类！
		// 初始化开始。。。
		// 初始化已完成！
		// idbQuery.request() = TheRealTheme
	}
	
}

interface IDBQuery {
	String request();
}

class DBQuery implements IDBQuery {
	
	public DBQuery() {
		//try {
		//	// 模拟耗时操作
		//	System.out.println("初始化开始。。。");
		//	TimeUnit.SECONDS.sleep(3);
		//	System.out.println("初始化已完成！");
		//} catch (InterruptedException e) {
		//	e.printStackTrace();
		//}
	}
	
	@Override
	public String request() {
		return "TheRealTheme";
	}
}

class DBQueryProxy implements IDBQuery {
	// 真实主题 关联关系： 代理类知道真实主题，真实主题不知道代理  聚合关系：代理不存在也不影响真实主题
	private DBQuery real = null;
	
	@Override
	public String request() {
		// 真正需要的时候，才创建真实对象，创建速度一句
		if (real == null) {
			real = new DBQuery();
		}
		// 多线程环境下，此处返回一个虚假类，类似于 Future 模式
		return real.request();
	}
}