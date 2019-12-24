package xyz.biandeshen.Java程序性能优化.designpatterns;

/**
 * @author fjp
 * @Title: Decorator
 * @ProjectName commons-tests
 * @Description: 装饰者
 * @date 2019/12/2410:02
 */
public class Decorator {
	public static void main(String[] args) {
		IPacketCreator packetCreator =
				new PacketHTTPHeaderCreator(
						new PacketHTMLHeaderCreator(
								new PacketBodyCreator()));
		System.out.println(packetCreator.handleContent());
	}
}

interface IPacketCreator {
	public String handleContent();
}

class PacketBodyCreator implements IPacketCreator {
	
	@Override
	public String handleContent() {
		return "Content of Packet!";
	}
}

// 维护核心组件对象,负责告知子类，核心业务逻辑应全权委托 component 完成,自己仅做增强处理
abstract class PacketDecorator implements IPacketCreator {
	IPacketCreator component;
	
	public PacketDecorator(IPacketCreator component) {
		this.component = component;
	}
}


// 将给定数据封装成HTML
class PacketHTMLHeaderCreator extends PacketDecorator {
	
	public PacketHTMLHeaderCreator(IPacketCreator component) {
		super(component);
	}
	
	@Override
	public String handleContent() {
		StringBuilder sb;
		sb = new StringBuilder();
		sb.append("<html>");
		sb.append("<body>");
		sb.append(component.handleContent());
		sb.append("</body>");
		sb.append("</html>\n");
		return sb.toString();
	}
}

class PacketHTTPHeaderCreator extends PacketDecorator {
	
	public PacketHTTPHeaderCreator(IPacketCreator component) {
		super(component);
	}
	
	@Override
	public String handleContent() {
		StringBuffer sb;
		sb = new StringBuffer();
		sb.append("Cache-Control:no-cache\n");
		sb.append("Date:Mon,31Dec201204:25:57GMT\n");
		sb.append(component.handleContent());
		return sb.toString();
	}
}