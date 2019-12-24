## 装饰者模式与OutputStream

---

> 装饰者模式通过委托机制，复用系统中的各个组件，运行时，将功能组件进行叠加，从而构建一个    
> 增强的对象，拥有所有这些组件的功能。同时，各子功能模块，被维护在各个组件的相关类中，拥有    
> 整洁的系统结构。




**_装饰者模式的这种结构，可以很好地将功能组件和性能组件进行分离，彼此互不影响，并在需要的时候，   
有机地结合起来。_**

---
> 装饰者模式UML图

![装饰者模式UML](http://assets.processon.com/chart_image/5e017b8fe4b0aef94ca81384.png)
---

### 代码举例 《Java程序性能优化》
```java
public class Decorator {
	public static void main(String[] args) {
		Component packetCreator =
				new ConcreteDecoratorB(
						new ConcreteDecoratorA(
								new ConcreteComponent()));
		System.out.println(packetCreator.operation());
	}
}

interface Component {
	public String operation();
}

class ConcreteComponent implements Component {
	
	@Override
	public String operation() {
		return "Content of Packet!";
	}
}

// 维护核心组件对象,负责告知子类，核心业务逻辑应全权委托 component 完成,自己仅做增强处理
abstract class Decorator implements Component {
	Component component;
	
	public Decorator(Component component) {
		this.component = component;
	}
}


// 将给定数据封装成HTML
class ConcreteDecoratorA extends Decorator {
	
	public ConcreteDecoratorA(Component component) {
		super(component);
	}
	
	@Override
	public String operation() {
		StringBuilder sb;
		sb = new StringBuilder();
		sb.append("<html>");
		sb.append("<body>");
		sb.append(component.operation());
		sb.append("</body>");
		sb.append("</html>\n");
		return sb.toString();
	}
}

class ConcreteDecoratorB extends Decorator {
	
	public ConcreteDecoratorB(Component component) {
		super(component);
	}
	
	@Override
	public String operation() {
		StringBuffer sb;
		sb = new StringBuffer();
		sb.append("Cache-Control:no-cache\n");
		sb.append("Date:Mon,31Dec201204:25:57GMT\n");
		sb.append(component.operation());
		return sb.toString();
	}
}
```
---

### JDK相关组件的案例
> OutputStream 与 InputStream 类族的实现。以 OutputStream 为例, 将OutputStream作为Component,  
> 则 FileOutPutStream 则为其字节流形式的ConcreteComponent,实现了向文件写, 而 FilterOutputStream  
> 则是 OutputStream 的装饰器（Decorator）,用以增强 OutputStream Decorator（FIlterOutputStream）  
> 的子类则是具体的装饰类, 如: BufferedOutputStream 实现缓冲输出,可以向底层输出流写入字节,而不必写入  
> 的每个字节调用系统底层,优化了I/O性能; DataOutputStream 则在 FileOutputStream 的基础上,增加了对  
> 多种数据类型的写操作支持; 此外还有 DeflaterOutputStream、CheckedOutputStream等具体的装饰类，分  
> 别对输出流做了各样的增强。

```java
// 流对象示例
public class Test {
	public static void main(String[] args) throws IOException{
		// 带缓冲功能的流对象 	
		DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("D:\\test.txt")));
		// 无缓冲功能的流对象
		DataOutputStream dout2 = new DataOutputStream(new FileOutputStream("D:\\test.txt"));
		long begin = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {
			dout.writeLong(i);
		}
		System.out.println((System.currentTimeMillis() - begin) + "ms");
		long begin2 = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {
			dout2.writeLong(i);
		}
		System.out.println((System.currentTimeMillis() - begin2) + "ms");
	}
}
// 以上两种建立OutputStream的方法,第一种加入了性能组件BufferedOutputStream,第二种没有。
// 执行结果：
// 45ms
// 2356ms
// 第一种方法产生的OutputStream将拥有更好的I/O性能

```

---

### 总结
> OutputStream 通过性能组件增强性能,即如果创建对象是new DataOutputStream(new BufferedOutputStream(new FileOutputStream("D:\\test.txt")))  
> 则创建对象实际是由内到外依次初始化FileOutputStream、BufferedOutputStream、DataOutputStream,并将前一个作为后一个的成员变量,在调用DataOutputStream  
> 的writeLong方法时,方法内部调用了out.write方法,此时具体装饰类DataOutputStream中的out对象实际为具体装饰类BufferedOutputStream,故调用的是它的write方法,  
> 而具体装饰类BufferedOutputStream中的write方法中调用了out.write方法,此时out对象为写出字节流到文件的输出流组件FileOutputStream,调用的是它的write方法,  
> 层层相扣,实现了对输出流的增强,完成了对性能组件与功能组件的分离。
>> 具体的增强,如BufferedOutputStream是在调用FileOutputStream的write前,将数据进行了缓存相关的操作,这就是具体的装饰类做的增强工作。由于每个具体实现的装饰类  
>> 各不相同,通过灵活组合选择装饰类,能满足我们的各种需求,同时,明确的职责与功能划分,也降低了系统的耦合。


## 参考
1. [装饰模式](https://design-patterns.readthedocs.io/zh_CN/latest/structural_patterns/decorator.htm)
2. 《Java程序性能优化》