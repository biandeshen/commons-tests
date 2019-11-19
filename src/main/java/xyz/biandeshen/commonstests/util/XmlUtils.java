package xyz.biandeshen.commonstests.util;

/**
 * @author fjp
 * @Title: XmlUtils
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/6/618:35
 */

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

public class XmlUtils {
	
	/**
	 * <P>map集合转化成xml字符串</P>
	 *
	 * @param map
	 * @param rootName
	 *
	 * @return
	 */
	public static String mapToXml(Map<String, Object> map, String rootName) {
		Element root = new Element(rootName);
		if (map == null) {
			return xmlToString(root);
		}
		for (String str : map.keySet()) {
			root.addContent(new Element(str).setText((map.get(str) == null ? "" : map.get(str) + "")));
		}
		return xmlToString(root);
	}
	
	/**
	 * <P>xml字符串转化成map集合</P>
	 *
	 * @param xmlStr
	 * 		字符串
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> xmlToMap(String xmlStr) {
		
		SAXBuilder builder = new SAXBuilder();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			xmlStr = URLDecoder.decode(xmlStr, "UTF-8");
			Reader in = new StringReader(xmlStr);
			Document doc = builder.build(in);
			Element root = doc.getRootElement();
			List<Element> list = root.getChildren();
			for (Element e : list)
				map.put(e.getName(), e.getText());
			return map;
		} catch (JDOMException e) {
		} catch (UnsupportedEncodingException e) {
		} catch (IOException e) {
		} catch (Exception e) {
		}
		return map;
		
	}
	
	
	/**
	 * <P>list集合转化成xml字符串</P>
	 *
	 * @param list
	 * @param rootName
	 * @param parentName
	 *
	 * @return
	 */
	public static String listToXml(List<Map<String, Object>> list, String rootName, String parentName) {
		Element root = new Element(rootName);
		boolean flag = false;
		Element parentElement = null;
		Element child = null;
		if (list == null)
			return xmlToString(root);
		for (Map<String, Object> map : list)
			if (flag) {
				flag = false;
				for (String str : map.keySet()) {
					child = new Element(str).setText(map.get(str) == null ? "" : (map.get(str) + ""));
					root.addContent(child);
				}
			} else {
				parentElement = new Element(parentName);
				root.addContent(parentElement);
				for (String str : map.keySet()) {
					String value = map.get(str) == null ? "" : (map.get(str) + "");
					value = wrapXmlContent(value);
					child = new Element(str).setText(value);
					parentElement.addContent(child);
				}
			}
		return xmlToString(root);
	}
	
	
	/**
	 * <P>xml字符串转化成list集合</P>
	 *
	 * @param xmlStr
	 *
	 * @return
	 */
	public static List<Map<String, Object>> xmlToList(String xmlStr) {
		SAXBuilder builder = new SAXBuilder();
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		boolean flag = true;
		try {
			xmlStr = URLDecoder.decode(xmlStr, "UTF-8");
			Reader in = new StringReader(xmlStr);
			Document doc = builder.build(in);
			Element root = doc.getRootElement();
			List<Element> list = root.getChildren();
			for (Element e : list) {
				if (e.getChildren().size() == 0) {
					if (flag) {
						flag = false;
						map = new HashMap<String, Object>();
						resultList.add(map);
					}
					map.put(e.getName(), e.getText());
				} else {
					map = new HashMap<String, Object>();
					List<Element> childrenList = e.getChildren();
					resultList.add(map);
					for (Element element : childrenList) {
						map.put(element.getName(), element.getText());
					}
				}
			}
			return resultList;
		} catch (JDOMException e) {
		} catch (UnsupportedEncodingException e) {
		} catch (IOException e) {
		} catch (Exception e) {
		}
		return resultList;
	}
	
	/**
	 * @author joed
	 * 描述：
	 * 字符转意
	 */
	public static String wrapXmlContent(String content) {
		StringBuffer appender = new StringBuffer("");
		
		if ((content != null) && (!content.trim().isEmpty())) {
			appender = new StringBuffer(content.length());
			
			for (int i = 0; i < content.length(); i++) {
				char ch = content.charAt(i);
				if ((ch == '\t') || (ch == '\n') || (ch == '\r') || ((ch >= ' ') && (ch <= 55295)) || ((ch >= 57344) && (ch <= 65533)) || ((ch >= 65536) && (ch <= 1114111))) {
					appender.append(ch);
				}
			}
		}
		String result = appender.toString();
		
		return result;
	}
	
	/**
	 * 将Element对象转化成字串
	 *
	 * @param element
	 *
	 * @return
	 */
	public static String xmlToString(Element element) {
		XMLOutputter output = new XMLOutputter();
		output.setFormat(Format.getPrettyFormat().setEncoding("UTF-8"));
		Document doc = new Document(element);
		String str = output.outputString(doc);
		return str;
	}
	
	
	/**
	 * 解析xml字符串获取节点内容
	 *
	 * @param xmlStr
	 * 		xml字符串
	 * @param nodeStrArr
	 * 		需要获取的节点
	 *
	 * @return 解析信息
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> readXml(String xmlStr, String[] nodeStrArr) {
		Map<String, String> map = new HashMap<String, String>();
		SAXBuilder builder = new SAXBuilder();
		try {
			xmlStr = URLDecoder.decode(xmlStr, "UTF-8");
			Reader in = new StringReader(xmlStr);
			Document doc = builder.build(in);
			Element root = doc.getRootElement();
			List<Element> l = null;
			String str = "";
			for (String nodeStr : nodeStrArr) {
				l = root.getChildren(nodeStr);
				str = l.get(0).getText();
				if ("".equals(str) || str == null) {
					map.put("msg", "01: 传入参数为空或格式错误，请检查各节点是否为有效XML节点");
					return map;
				} else {
					map.put("msg", "00: 解析xml成功");
					map.put(nodeStr, str);
				}
			}
		} catch (JDOMException e) {
			map.put("msg", "01: 传入参数为空或格式错误，请检查各节点是否为有效XML节点");
			return map;
		} catch (UnsupportedEncodingException e) {
			map.put("msg", "05：编码格式错误,不支持的字符编码");
			return map;
		} catch (IOException e) {
			map.put("msg", "02: 读取失败");
			return map;
		} catch (Exception e) {
			map.put("msg", "02: 读取失败");
			return map;
		}
		return map;
	}
	
	
	/**
	 * 生成带内容的节点
	 *
	 * @param parentElement
	 * 		父节点
	 * @param map
	 * 		数据集
	 *
	 * @return
	 */
	public static Element createNodes(Element parentElement, Map<String, Object> map) {
		String msg = "";
		Iterator<String> it = map.keySet().iterator();
		String tempStr = "";
		Element sonElement = null;
		while (it.hasNext()) {
			tempStr = it.next();
			msg = (map.get(tempStr)) == null ? "" : (map.get(tempStr) + "");
			sonElement = new Element(tempStr);
			parentElement.addContent(sonElement.setText(msg));
		}
		return parentElement;
	}
	
	/**
	 * 生成不带内容的节点
	 *
	 * @param root
	 * 		根节点
	 * @param strArr
	 * 		节点字符
	 */
	public static void createNodes(Element root, String[] strArr) {
		Element e = null;
		for (String str : strArr) {
			e = new Element(str);
			root.addContent(e);
		}
		
	}
}