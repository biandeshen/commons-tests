package xyz.biandeshen.commonstests.util.common;

/**
 * @author fjp
 * @Title: XmlUtils
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/6/618:47
 */

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import com.thoughtworks.xstream.XStream;

/**
 * XML工具类
 */
public class XmlUtils {
	
	private final static String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
	
	private XmlUtils() {
	}
	
	/**
	 * 序列化XML
	 *
	 * @param obj
	 * @param clazz
	 *
	 * @return
	 */
	public static <T> String toXML(Object obj) {
		XStream stream = getXStream();
		stream.processAnnotations(obj.getClass());
		return new StringBuffer(XML_DECLARATION).append(stream.toXML(obj)).toString();
	}
	
	/**
	 * 反序列化XML
	 *
	 * @param xmlStr
	 * @param clazz
	 *
	 * @return
	 */
	public static <T> T fromXML(String xmlStr, Class<T> clazz) {
		XStream stream = getXStream();
		stream.processAnnotations(clazz);
		Object obj = stream.fromXML(xmlStr);
		try {
			return clazz.cast(obj);
		} catch (ClassCastException e) {
			return null;
		}
	}
	
	/**
	 * 获取指定节点的值
	 *
	 * @param xpath
	 * @param params
	 *
	 * @return
	 */
	public static String getNodeValue(String xpath, String dataStr) {
		try {
			// 将字符串转为xml
			Document document = DocumentHelper.parseText(dataStr);
			// 查找节点
			Element element = (Element) document.selectSingleNode(xpath);
			if (element != null) {
				return element.getStringValue();
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 获取Xstream实例
	 *
	 * @return
	 */
	public static XStream getXStream() {
		return new XStream();
	}
}