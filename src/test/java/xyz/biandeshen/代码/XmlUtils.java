package xyz.biandeshen.代码;


import org.apache.commons.lang3.exception.CloneFailedException;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fjp
 * @Title: XmlUtils
 * @ProjectName icp
 * @Description: XmlUtils 工具类
 * @date 2018/10/3116:23
 * 2019.7.30  xml工具类并发情况下执行缓慢JAXBContext context = JAXBContext.newInstance()，所以改成类变量
 */
@SuppressWarnings({"unchecked", "unused"})
public final class XmlUtils {
	private static final SchemaFactory SCH_FACTORY = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	
	/**
	 * 若基于package创建JAXBContext，如下所示：
	 * JAXBContext jaxbContext = JAXBContext.newInstance("com.xxx.jaxb");
	 * 并不是指定的包中所有的Class都会用来创建JAXBContext
	 * 按照JAXB的规范，我们需要在对应的包中创建一个jaxb.index文件，
	 * 然后在其中指定创建JAXBContext时需要用到的Class，每个Class名称占一行，只需要写Class名称即可
	 * jaxb.index 如下所示：
	 * entity1
	 * entity2
	 * entity3
	 *
	 */
	/**
	 * Class 与 JAXBContext 的 映射关系, 避免重复创建
	 * 将保存映射的对象作为不可变对象来使用,即对象值放入map后就不再改变,
	 * sychorionizedMap中的同步机制就足以使映射值被安全发布,
	 * 并在访问值时不需要额外的同步
	 */
	private static Map<List<Class<?>>, JAXBContext> classJAXBContextMap =
			Collections.synchronizedMap(new HashMap<>(2 << 3));
	
	/**
	 * 从xml文件读取
	 */
	public static <T> T readFromFile(Class<T>[] clazz, String filePath) throws JAXBException {
		JAXBContext jaxbContext = getClassJAXBContextMap(clazz);
		Unmarshaller unmarshaller = jaxbContext != null ? jaxbContext.createUnmarshaller() : null;
		return (T) (unmarshaller != null ? unmarshaller.unmarshal(new File(filePath)) : null);
	}
	
	/**
	 * 从xml字符串读取
	 */
	public static <T> T readFromString(Class<T>[] clazz, String xmlStr) throws JAXBException {
		JAXBContext jaxbContext = getClassJAXBContextMap(clazz);
		Unmarshaller unmarshaller = jaxbContext != null ? jaxbContext.createUnmarshaller() : null;
		return (T) (unmarshaller != null ? unmarshaller.unmarshal(new StreamSource(new StringReader(xmlStr))) : null);
	}
	
	/**
	 * 把对象转化成xml输出到控制台
	 */
	public static <T> void writeToConsole(Class<T>[] clazz, T t) throws JAXBException, SAXException {
		writeToConsole(clazz, t, null);
	}
	
	/**
	 * 把对象转化成xml输出到控制台
	 */
	@SuppressWarnings("all")
	public static <T> void writeToConsole(Class<T>[] clazz, T t, String xsdSchemaRelativePath) throws
	                                                                                           JAXBException,
	                                                                                           SAXException {
		Marshaller marshaller = object2xml(clazz, xsdSchemaRelativePath);
		marshaller.marshal(t, System.out);
	}
	
	/**
	 * 追加文件：使用FileWriter
	 */
	public static void appendContentToFile(File file, String content) throws Throwable {
		try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file, true),
		                                                                    StandardCharsets.UTF_8)) {
			try (BufferedWriter bw = new BufferedWriter(outputStreamWriter)) {
				bw.write(content);
			}
		}
	}
	
	/**
	 * 把对象转化成xml字符串
	 */
	public static <T> String writeToString(Class<T>[] clazz, T t) throws JAXBException, SAXException, IOException {
		return writeToString(clazz, t, null);
	}
	
	/**
	 * 把对象转化成xml字符串
	 */
	@SuppressWarnings("all")
	public static <T> String writeToString(Class<T>[] clazz, T t, String xsdSchemaRelativePath) throws
	                                                                                            JAXBException,
	                                                                                            SAXException,
	                                                                                            IOException {
		try (StringWriter writer = new StringWriter();) {
			Marshaller marshaller = object2xml(clazz, xsdSchemaRelativePath);
			//writer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			//writer.append("\n");
			marshaller.marshal(t, writer);
			return writer.toString();
		}
	}
	
	/**
	 * 对象转xml
	 *
	 * @param clazz
	 * 		对象Class
	 * @param <T>
	 * 		对象类型
	 *
	 * @return Marshaller 调度程序
	 *
	 * @throws JAXBException
	 * 		JAXB异常
	 * @throws SAXException
	 * 		SAX异常
	 */
	private static <T> Marshaller object2xml(Class<T>[] clazz) throws JAXBException, SAXException {
		return object2xml(clazz, null);
	}
	
	/**
	 * 对象转xml
	 *
	 * @param clazz
	 * 		对象Class
	 * @param <T>
	 * 		对象类型
	 *
	 * @return Marshaller 调度程序
	 *
	 * @throws JAXBException
	 * 		JAXB异常
	 * @throws SAXException
	 * 		SAX异常
	 */
	private static <T> Marshaller object2xml(Class<T>[] clazz, String xsdSchemaRelativePath) throws JAXBException,
	                                                                                                SAXException {
		JAXBContext cxt = getClassJAXBContextMap(clazz);
		Marshaller marshaller = null;
		if (cxt != null) {
			marshaller = cxt.createMarshaller();
			if (xsdSchemaRelativePath != null) {
				/* Schema sch = SCH_FACTORY.newSchema(XmlUtils.class.getResource("/static/xsd/ICPSchema.xsd")); */
				Schema sch = SCH_FACTORY.newSchema(XmlUtils.class.getResource(xsdSchemaRelativePath));
				// 设置 schema
				marshaller.setSchema(sch);
			}
			// 编码格式
			marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			// 是否格式化生成的xml串
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			//// 默认false表示xml指令存在 true表示隐藏默认
			//marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			//// 解决默认显示standalone的问题(貌似只有在输出在控制台方法中与上句代码同时生效)
			//marshaller.setProperty("com.sun.xml.internal.bind.xmlHeaders", "<?xml version=\"1.0\"
			// encoding=\"UTF-8\"?>");
		}
		return marshaller;
	}
	
	
	/**
	 * 序列化XML
	 *
	 * @param object
	 * 		java 对象
	 *
	 * @return xml 字符串
	 *
	 * @throws JAXBException
	 * 		异常
	 * @throws UnsupportedEncodingException
	 * 		异常
	 */
	public static String marshal(Object object) throws JAXBException, IOException {
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();) {
			JAXBContext context = getClassJAXBContextMap(object.getClass());
			Marshaller marshaller;
			if (context != null) {
				marshaller = context.createMarshaller();
				//编码格式
				marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
				//格式化XML
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				marshaller.marshal(object, outputStream);
			}
			return outputStream.toString("UTF-8");
		}
	}
	
	/**
	 * 反序列化XML
	 *
	 * @param cls
	 * 		class
	 * @param xmlStr
	 * 		xml字符串
	 * @param <T>
	 * 		返回类型
	 *
	 * @return 传入类型的对象
	 *
	 * @throws JAXBException
	 * 		异常
	 */
	public static <T> T unMarshal(Class<T> cls, String xmlStr) throws JAXBException, IOException {
		try (ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlStr.getBytes(StandardCharsets.UTF_8));) {
			JAXBContext context = getClassJAXBContextMap(cls);
			Unmarshaller unMarshaller = null;
			if (context != null) {
				unMarshaller = context.createUnmarshaller();
			}
			return (T) (unMarshaller != null ? unMarshaller.unmarshal(inputStream) : null);
		}
	}
	
	/**
	 * 反序列化XML
	 *
	 * @param cls
	 * 		class
	 * @param xmlStr
	 * 		xml字符串
	 * @param <T>
	 * 		返回类型
	 *
	 * @return 传入类型的对象
	 *
	 * @throws JAXBException
	 * 		异常
	 */
	public static <T> T unMarshal2(Class<T> cls, String xmlStr) throws JAXBException {
		JAXBContext context = getClassJAXBContextMap(cls);
		Unmarshaller unmarshaller = null;
		if (context != null) {
			unmarshaller = context.createUnmarshaller();
		}
		return (T) (unmarshaller != null ? unmarshaller.unmarshal(new StringReader(xmlStr)) : null);
	}
	
	/**
	 * 获取 Class 与 JAXBContext 的 映射关系 避免重复创建
	 *
	 * @return classJAXBContextMap Class 与 JAXBContext 的 映射关系 避免重复创建
	 */
	private static JAXBContext getClassJAXBContextMap(Class<?>... clazz) {
		List<Class<?>> classes = new ArrayList<>(Arrays.asList(clazz));
		//判断指定class为关键字的context是否存在,不存在则创建并返回
		if (!classJAXBContextMap.containsKey(classes)) {
			try {
				JAXBContext context = JAXBContext.newInstance(clazz);
				//由于已使用 Collections.sychronizedMap(),故无需再加锁
				//synchronized (XmlUtils.class) {
				if (!classJAXBContextMap.containsKey(classes)) {
					classJAXBContextMap.put(classes, context);
				} else {
					return classJAXBContextMap.get(classes);
				}
				//}
			} catch (JAXBException e) {
				return null;
			}
		}
		return classJAXBContextMap.get(classes);
	}
	
	/**
	 * 获取 Class 与 JAXBContext 的 映射关系 避免重复创建
	 *
	 * @return classJAXBContextMap Class 与 JAXBContext 的 映射关系 避免重复创建
	 */
	public static Map<List<Class<?>>, JAXBContext> getClassJAXBContextMap() throws CloneFailedException {
		return deepCopyClassJAXBContextMap();
	}
	
	
	/**
	 * 获取 Class 与 JAXBContext 的 映射关系 避免重复创建
	 *
	 * @return classJAXBContextMap Class 与 JAXBContext 的 映射关系 避免重复创建
	 */
	private static Map<List<Class<?>>, JAXBContext> deepCopyClassJAXBContextMap() throws CloneFailedException {
		//return XmlUtils.classJAXBContextMap;
		// 采取深拷贝,同时,返回一个unmodifiableMap包装过的结果
		Map<List<Class<?>>, JAXBContext> result = new HashMap<>(classJAXBContextMap.size());
		for (List<Class<?>> classes : classJAXBContextMap.keySet()) {
			try {
				result.put(new ArrayList<>(classes), JAXBContext.newInstance(classes.toArray(new Class[0])));
			} catch (JAXBException e) {
				throw new CloneFailedException(e);
			}
		}
		return Collections.unmodifiableMap(result);
	}
}

