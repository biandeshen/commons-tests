package xyz.biandeshen.代码;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author fjp
 * @version 0.0.1
 * @Title: JsonUtils
 * @Description: JsonUTILS
 * @date 2018/12/12 17:26
 */
@SuppressWarnings(value = {"all"})
public class JsonUtils {
	/**
	 * jackson
	 */
	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	public static ObjectMapper getInstance() {
		return OBJECT_MAPPER;
	}
	
	/**
	 * <big><i>String字符串转换为json格式</i></big>
	 *
	 * @param str
	 * 		<strong>StringStr</strong>
	 *
	 * @return <strong>java.lang.String</strong>
	 *
	 * @throws
	 * @Description: 字符串实现
	 * @author fjp
	 * @date 2018/12/13 14:47
	 */
	public static String stringToJsonStrImpl(String str) {
		if (StringUtils.isEmpty(str) || "".equals(str)) {
			return nullToJsonStrImpl();
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			switch (ch) {
				case '"':
					sb.append("\\\"");
					break;
				case '\\':
					sb.append("\\\\");
					break;
				case '\b':
					sb.append("\\b");
					break;
				case '\f':
					sb.append("\\f");
					break;
				case '\n':
					sb.append("\\n");
					break;
				case '\r':
					sb.append("\\r");
					break;
				case '\t':
					sb.append("\\t");
					break;
				case '/':
					sb.append("\\/");
					break;
				default:
					if (ch <= '\u001F') {
						String ss = Integer.toHexString(ch);
						sb.append("\\u");
						for (int k = 0; k < 4 - ss.length(); k++) {
							sb.append('0');
						}
						sb.append(ss.toUpperCase());
					} else {
						sb.append(ch);
					}
			}
		}
		return sb.toString();
	}
	
	/**
	 * <big><i>String字符串转换为json格式</i></big>
	 *
	 * @param str
	 * 		<strong>StringStr</strong>
	 *
	 * @return <strong>java.lang.String</strong>
	 *
	 * @throws
	 * @Description: Gson实现
	 * @author fjp
	 * @date 2018/12/13 14:47
	 */
	public static String stringToJsonGsonImpl(String str) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return gson.toJson(str);
	}
	
	/**
	 * <big><i>null转json</i></big>
	 *
	 * @param
	 *
	 * @return <strong>java.lang.String</strong>
	 *
	 * @throws <strong><i>
	 * 		</i></strong>
	 * @Description:
	 * @author fjp
	 * @date 2018/12/13 15:28
	 */
	public static String nullToJsonStrImpl() {
		return "\"\"";
	}
	
	
	/**
	 * <big><i>null转json</i></big>
	 *
	 * @param
	 *
	 * @return <strong>java.lang.String</strong>
	 *
	 * @throws <strong><i>
	 * 		</i></strong>
	 * @Description:
	 * @author fjp
	 * @date 2018/12/13 15:28
	 */
	public static String nullToJsonGsonImpl() {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return gson.toJson("");
	}
	
	/**
	 * <big><i>一个obj对象转换为json格式</i></big>
	 *
	 * @param obj
	 * 		<strong>object对象</strong>
	 *
	 * @return <strong>java.lang.String</strong>
	 *
	 * @throws <strong><i>
	 * 		</i></strong>
	 * @Description: 字符串实现
	 * @author fjp
	 * @date 2018/12/13 15:34
	 */
	public static String objectToJsonStrImpl(Object obj) throws IllegalAccessException,
	                                                            IntrospectionException,
	                                                            InvocationTargetException {
		StringBuilder json = new StringBuilder();
		if (obj == null) {
			//json.append("\"\"");
			json.append("null");
		} else if (obj instanceof Number) {
			json.append(numberToJsonStrImpl((Number) obj));
		} else if (obj instanceof Boolean) {
			json.append(booleanToJsonStrImpl((Boolean) obj));
		} else if (obj instanceof String) {
			json.append("\"").append(stringToJsonStrImpl(obj.toString())).append("\"");
		} else if (obj instanceof Object[]) {
			json.append(arrayToJsonStrImpl((Object[]) obj));
		} else if (obj instanceof List) {
			json.append(listToJsonStrImpl((List<?>) obj));
		} else if (obj instanceof Map) {
			json.append(mapToJsonStrImpl((Map<?, ?>) obj));
		} else if (obj instanceof Set) {
			json.append(setToJsonStrImpl((Set<?>) obj));
		} else {
			json.append(beanToJsonStrImpl(obj));
		}
		return json.toString();
	}
	
	/**
	 * <big><i>一个obj对象转换为json格式</i></big>
	 *
	 * @param obj
	 * 		<strong>object对象</strong>
	 *
	 * @return <strong>java.lang.String</strong>
	 *
	 * @throws <strong><i>
	 * 		</i></strong>
	 * @Description: Gson实现
	 * @author fjp
	 * @date 2018/12/13 15:34
	 */
	public static String objectToJsonGsonImpl(Object obj) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return gson.toJson(obj);
	}
	
	
	/**
	 * <big><i>一个obj对象转换为json格式</i></big>
	 *
	 * @param obj
	 * 		<strong>object对象</strong>
	 *
	 * @return <strong>java.lang.String</strong>
	 *
	 * @throws <strong><i>
	 * 		</i></strong>
	 * @Description: Jackson实现
	 * @author fjp
	 * @date 2018/12/13 15:34
	 */
	public static String objectToJsonJackSonImpl(Object obj) throws JsonProcessingException {
		return OBJECT_MAPPER.writeValueAsString(obj);
		
	}
	
	
	/**
	 * <big><i>一个obj对象转换为json格式</i></big>
	 * <big><i><p>忽略空值</p></i></big>
	 *
	 * @param obj
	 * 		<strong>object对象</strong>
	 *
	 * @return <strong>java.lang.String</strong>
	 *
	 * @throws <strong><i>
	 * 		</i></strong>
	 * @Description: Gson实现
	 * @author fjp
	 * @date 2018/12/13 15:34
	 */
	public static String objectToJsonIgnoreNullJackSonImpl(Object obj) throws
	                                                                   JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper.writeValueAsString(obj);
		
	}
	
	/**
	 * <big><i>一个number转换为json字符串</i></big>
	 *
	 * @param number
	 * 		<strong>Number</strong>
	 *
	 * @return <strong>java.lang.String</strong>
	 *
	 * @throws <strong><i>
	 * 		</i></strong>
	 * @Description:
	 * @author fjp
	 * @date 2018/12/13 15:56
	 */
	public static String numberToJsonStrImpl(Number number) {
		return number == null ? null : number.toString();
	}
	
	/**
	 * <big><i>一个number转换为json字符串</i></big>
	 *
	 * @param number
	 * 		<strong>Number</strong>
	 *
	 * @return <strong>java.lang.String</strong>
	 *
	 * @throws <strong><i>
	 * 		</i></strong>
	 * @Description:
	 * @author fjp
	 * @date 2018/12/13 15:56
	 */
	public static String numberToJsonGsonImpl(Number number) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return gson.toJson(number);
		
	}
	
	/**
	 * <big><i>一个Boolean转换为json字符串</i></big>
	 *
	 * @param bool
	 * 		<strong>Boolean</strong>
	 *
	 * @return <strong>java.lang.String</strong>
	 *
	 * @throws <strong><i>
	 * 		</i></strong>
	 * @Description:
	 * @author fjp
	 * @date 2018/12/13 15:56
	 */
	public static String booleanToJsonStrImpl(Boolean bool) {
		return bool == null ? null : bool.toString();
	}
	
	/**
	 * <big><i>一个Boolean转换为json字符串</i></big>
	 *
	 * @param bool
	 * 		<strong>Boolean</strong>
	 *
	 * @return <strong>java.lang.String</strong>
	 *
	 * @throws <strong><i>
	 * 		</i></strong>
	 * @Description:
	 * @author fjp
	 * @date 2018/12/13 15:56
	 */
	public static String booleanToJsonGsonImpl(Boolean bool) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return gson.toJson(bool);
	}
	
	/**
	 * <big><i>一个Object转换为json字符串</i></big>
	 *
	 * @param bean
	 * 		<strong>Boolean</strong>
	 *
	 * @return <strong>java.lang.String</strong>
	 *
	 * @throws <strong><i>
	 * 		</i></strong>
	 * @Description:
	 * @author fjp
	 * @date 2018/12/13 15:56
	 */
	public static String beanToJsonStrImpl(Object bean) throws
	                                                    IntrospectionException,
	                                                    InvocationTargetException,
	                                                    IllegalAccessException {
		StringBuilder json = new StringBuilder();
		json.append("{");
		PropertyDescriptor[] props = null;
		props = Introspector.getBeanInfo(bean.getClass(), Object.class).getPropertyDescriptors();
		if (props != null) {
			for (PropertyDescriptor prop : props) {
				String name = objectToJsonStrImpl(prop.getName());
				String value = objectToJsonStrImpl(prop.getReadMethod().invoke(bean));
				json.append(name);
				json.append(":");
				json.append(value);
				json.append(",");
			}
			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append("}");
		}
		return json.toString();
	}
	
	/**
	 * <big><i>一个Object转换为json字符串</i></big>
	 *
	 * @param bean
	 * 		<strong>Boolean</strong>
	 *
	 * @return <strong>java.lang.String</strong>
	 *
	 * @throws <strong><i>
	 * 		</i></strong>
	 * @Description:
	 * @author fjp
	 * @date 2018/12/13 15:56
	 */
	public static String beanToJsonGsonImpl(Object bean) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return gson.toJson(bean);
	}
	
	
	/**
	 * <big><i>一个list转换为json字符串</i></big>
	 *
	 * @param list
	 * 		<strong>list</strong>
	 *
	 * @return <strong>java.lang.String</strong>
	 *
	 * @throws <strong><i>
	 * 		</i></strong>
	 * @Description:
	 * @author fjp
	 * @date 2018/12/13 16:53
	 */
	public static String listToJsonStrImpl(List<?> list) throws
	                                                     IllegalAccessException,
	                                                     IntrospectionException,
	                                                     InvocationTargetException {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (list != null && list.size() > 0) {
			for (Object obj : list) {
				json.append(objectToJsonStrImpl(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}
	
	
	/**
	 * <big><i>一个list转换为json字符串</i></big>
	 *
	 * @param list
	 * 		<strong>list</strong>
	 *
	 * @return <strong>java.lang.String</strong>
	 *
	 * @throws <strong><i>
	 * 		</i></strong>
	 * @Description:
	 * @author fjp
	 * @date 2018/12/13 16:53
	 */
	public static String listToJsonGsonImpl(List<?> list) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return gson.toJson(list);
	}
	
	/**
	 * <big><i>一个array转换为json字符串</i></big>
	 *
	 * @param array
	 * 		<strong>object[]</strong>
	 *
	 * @return <strong>java.lang.String</strong>
	 *
	 * @throws <strong><i>
	 * 		</i></strong>
	 * @Description:
	 * @author fjp
	 * @date 2018/12/13 16:53
	 */
	public static String arrayToJsonStrImpl(Object[] array) throws
	                                                        IllegalAccessException,
	                                                        IntrospectionException,
	                                                        InvocationTargetException {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (array != null && array.length > 0) {
			for (Object obj : array) {
				json.append(objectToJsonStrImpl(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}
	
	/**
	 * <big><i>一个array转换为json字符串</i></big>
	 *
	 * @param array
	 * 		<strong>object[]</strong>
	 *
	 * @return <strong>java.lang.String</strong>
	 *
	 * @throws <strong><i>
	 * 		</i></strong>
	 * @Description:
	 * @author fjp
	 * @date 2018/12/13 16:53
	 */
	public static String arrayToJsonGsonImpl(Object[] array) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return gson.toJson(array);
	}
	
	/**
	 * <big><i>一个set转换为json字符串</i></big>
	 *
	 * @param set
	 * 		<strong>set</strong>
	 *
	 * @return <strong>java.lang.String</strong>
	 *
	 * @throws <strong><i>
	 * 		</i></strong>
	 * @Description:
	 * @author fjp
	 * @date 2018/12/13 16:53
	 */
	public static String setToJsonStrImpl(Set<?> set) throws IllegalAccessException,
	                                                         IntrospectionException,
	                                                         InvocationTargetException {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (set != null && set.size() > 0) {
			for (Object obj : set) {
				json.append(objectToJsonStrImpl(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}
	
	/**
	 * <big><i>一个set转换为json字符串</i></big>
	 *
	 * @param set
	 * 		<strong>set</strong>
	 *
	 * @return <strong>java.lang.String</strong>
	 *
	 * @throws <strong><i>
	 * 		</i></strong>
	 * @Description:
	 * @author fjp
	 * @date 2018/12/13 16:53
	 */
	public static String setToJsonGsonImpl(Set<?> set) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return gson.toJson(set);
	}
	
	/**
	 * <big><i>一个map转换为json字符串</i></big>
	 *
	 * @param map
	 * 		<strong>map</strong>
	 *
	 * @return <strong>java.lang.String</strong>
	 *
	 * @throws <strong><i>
	 * 		</i></strong>
	 * @Description:
	 * @author fjp
	 * @date 2018/12/13 16:53
	 */
	public static String mapToJsonStrImpl(Map<?, ?> map) throws IllegalAccessException,
	                                                            IntrospectionException,
	                                                            InvocationTargetException {
		StringBuilder json = new StringBuilder();
		json.append("{");
		if (map != null && map.size() > 0) {
			Iterator<? extends Entry<?, ?>> entryIterator = map.entrySet().iterator();
			while (entryIterator.hasNext()) {
				json.append(objectToJsonStrImpl(entryIterator.next().getKey()));
				json.append(":");
				json.append(objectToJsonStrImpl(entryIterator.next().getValue()));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append("}");
		}
		return json.toString();
	}
	
	/**
	 * <big><i>一个map转换为json字符串</i></big>
	 *
	 * @param map
	 * 		<strong>map</strong>
	 *
	 * @return <strong>java.lang.String</strong>
	 *
	 * @throws <strong><i>
	 * 		</i></strong>
	 * @Description:
	 * @author fjp
	 * @date 2018/12/13 16:53
	 */
	public static String mapToJsonGsonImpl(Map<?, ?> map) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return gson.toJson(map);
	}
	
	/**
	 * <big><i>一个map转换为json字符串</i></big>
	 *
	 * @param map
	 * 		<strong>map</strong>
	 *
	 * @return <strong>java.lang.String</strong>
	 *
	 * @throws <strong><i>
	 * 		</i></strong>
	 * @Description:
	 * @author fjp
	 * @date 2018/12/13 16:53
	 */
	public static String mapToJsonGJackSonImpl(Map<?, ?> map) {
		try {
			return OBJECT_MAPPER.writeValueAsString(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * <big><i>一个json转换为object对象</i></big>
	 *
	 * @param json
	 * 		<strong>String</strong>
	 * @param clazz
	 * 		<strong>Class</strong>
	 *
	 * @return <strong>T</strong>
	 *
	 * @throws <strong><i>
	 * 		</i></strong>
	 * @Description:
	 * @author fjp
	 * @date 2018/12/13 16:53
	 */
	public static <T> T parseJsonToBeanGsonImpl(String json, Class<T> clazz) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return gson.fromJson(json, clazz);
	}
	
	/**
	 * <big><i>一个json转换为Map</i></big>
	 *
	 * @param json
	 * 		<strong>String</strong>
	 *
	 * @return <strong>java.util.Map</strong>
	 *
	 * @throws <strong><i>
	 * 		</i></strong>
	 * @Description:
	 * @author fjp
	 * @date 2018/12/13 16:53
	 */
	public static <T> Map<String, T> parseJsonToMapGsonImpl(String json) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		Type type = new TypeToken<HashMap<String, T>>() {
		}.getType();
		return gson.<HashMap<String, T>>fromJson(json, type);
	}
	
	/**
	 * <big><i>一个json转换为Map</i></big>
	 *
	 * @param json
	 * 		<strong>String</strong>
	 *
	 * @return <strong>java.util.Map</strong>
	 *
	 * @throws <strong><i>
	 * 		</i></strong>
	 * @Description:
	 * @author fjp
	 * @date 2018/12/13 16:53
	 */
	@SuppressWarnings("unchecked")
	public static <T> Map<String, T> parseJsonToMapJackSonImpl(String json) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper.readValue(json, HashMap.class);
	}
	
	/**
	 * <big><i>一个json转换为Map</i></big>
	 *
	 * @param json
	 * 		<strong>String</strong>
	 * @param clazz
	 * 		<strong>Class</strong>
	 *
	 * @return <strong>java.util.Map</strong>
	 *
	 * @throws <strong><i>
	 * 		</i></strong>
	 * @Description:
	 * @author fjp
	 * @date 2018/12/13 16:53
	 */
	@SuppressWarnings("unchecked")
	public static <T> Map<String, T> parseJsonToMapJackSonImpl(String json, Class<T> clazz) throws
	                                                                                        Exception {
		Map<String, Map<String, T>> map = OBJECT_MAPPER.readValue(json,
		                                                          new TypeReference<Map<String, T>>() {
		});
		Map<String, T> result = new HashMap<>();
		for (Entry<String, Map<String, T>> entry : map.entrySet()) {
			result.put(entry.getKey(), object2BeanJackSonImpl(entry.getValue(), clazz));
		}
		return result;
	}
	
	/**
	 * <big><i>把json字符串变成map(深度转换)</i></big>
	 *
	 * @param json
	 * 		<strong>String</strong>
	 *
	 * @return <strong>java.util.Map<java.lang.String,T></strong>
	 *
	 * @throws <strong><i>
	 * 		</i></strong>
	 * @Description:
	 * @author fjp
	 * @date 2018/12/14 10:07
	 */
	@SuppressWarnings("unchecked")
	public static <T> Map<String, T> parseJsonToMapDeepleyJackSonImpl(String json) throws
	                                                                               Exception {
		return (Map<String, T>) parseJsonToMapRecursionJackSonImpl(json, OBJECT_MAPPER);
	}
	
	/**
	 * 把json解析成map，如果map内部的value存在jsonString，继续解析
	 *
	 * @param json
	 * @param mapper
	 *
	 * @return
	 *
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private static <T> Map<String, Object> parseJsonToMapRecursionJackSonImpl(
			String json,
			ObjectMapper mapper
	) throws Exception {
		if (json == null) {
			return null;
		}
		
		Map<String, Object> map = mapper.readValue(json, Map.class);
		
		for (Entry<String, Object> entry : map.entrySet()) {
			Object obj = entry.getValue();
			if (obj != null && obj instanceof String) {
				String str = ((String) obj);
				if (str.startsWith("[")) {
					List<T> list = parseJsonToListRecursionJackSonImpl(str, mapper);
					map.put(entry.getKey(), list);
				} else if (str.startsWith("{")) {
					Map<String, Object> mapRecursion = parseJsonToMapRecursionJackSonImpl(str,
					                                                                      mapper);
					map.put(entry.getKey(), mapRecursion);
				}
			}
		}
		return map;
	}
	
	/**
	 * 把json字符串变成list
	 *
	 * @return
	 */
	public static <T> List<T> parseJsonToListGsonImpl(String json) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		List<T> list = null;
		try {
			Type type = new TypeToken<List<T>>() {
			}.getType();
			list = gson.fromJson(json, type);
		} catch (Exception e) {
		
		}
		return list;
	}
	
	/**
	 * 与javaBean json数组字符串转换为list
	 */
	public static <T> List<T> parseJsonToListJackSonImpl(String jsonArrayStr, Class<T> clazz) throws
	                                                                                          Exception {
		JavaType javaType = getCollectionType(ArrayList.class, clazz);
		return OBJECT_MAPPER.readValue(jsonArrayStr, javaType);
	}
	
	/**
	 * 把json解析成list，如果list内部的元素存在jsonString，继续解析
	 *
	 * @param json
	 * @param mapper
	 * 		解析工具
	 *
	 * @return
	 *
	 * @throws Exception
	 */
	@SuppressWarnings("unchedked")
	private static <T> List<T> parseJsonToListRecursionJackSonImpl(String json,
	                                                               ObjectMapper mapper) throws
	                                                                                                 Exception {
		if (json == null) {
			return null;
		}
		
		List<T> list = mapper.readValue(json, List.class);
		
		for (Object obj : list) {
			if (obj != null && obj instanceof String) {
				String str = (String) obj;
				if (str.startsWith("[")) {
					obj = parseJsonToListRecursionJackSonImpl(str, mapper);
				} else if (obj.toString().startsWith("{")) {
					obj = parseJsonToMapRecursionJackSonImpl(str, mapper);
				}
			}
		}
		
		return list;
	}
	
	/**
	 * 获取泛型的Collection Type
	 *
	 * @param collectionClass(如ArrayList)
	 * 		泛型的Collection
	 * @param elementClasses(给定的类)
	 * 		元素类
	 *
	 * @return JavaType Java类型
	 */
	public static JavaType getCollectionType(Class<?> collectionClass,
	                                         Class<?>... elementClasses) {
		return OBJECT_MAPPER.getTypeFactory().constructParametricType(collectionClass,
		                                                              elementClasses);
	}
	
	/**
	 * 获取json串中某个字段的值，注意，只能获取同一层级的value
	 *
	 * @param json
	 * @param key
	 *
	 * @return
	 */
	public static String getFieldValue(String json, String key) {
		if (StringUtils.isEmpty(json)) {
			return null;
		}
		if (!json.contains(key)) {
			return "";
		}
		JSONObject jsonObject;
		String value = null;
		try {
			jsonObject = new JSONObject(json);
			value = jsonObject.getString(key);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	
	/**
	 * 根据一个url地址.获取json数据.转换为List
	 */
	public static <T> List<Map<String, T>> getListByUrlStrImpl(String url) {
		try {
			//通过HTTP获取JSON数据
			StringBuilder sb;
			try (InputStream in = new URL(url).openStream(); BufferedReader reader =
					                                                 new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
				sb = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
			}
			return parseJsonToListGsonImpl(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据一个url地址.获取json数据.转换为MAP
	 */
	public static <T> Map<String, T> getMapByUrlStrImpl(String url) {
		try {
			//通过HTTP获取JSON数据
			StringBuilder sb;
			try (InputStream in = new URL(url).openStream(); BufferedReader reader =
					                                                 new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
				sb = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
			}
			return parseJsonToMapGsonImpl(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * json字符串变成对象
	 *
	 * @param json
	 * @param clazz
	 *
	 * @return
	 */
	public static <T> T json2BeanJackSonImpl(String json, Class<T> clazz) throws IOException {
		OBJECT_MAPPER.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		return OBJECT_MAPPER.readValue(json, clazz);
	}
	
	/**
	 * object  转JavaBean
	 */
	public static <T> T object2BeanJackSonImpl(Object object, Class<T> clazz) {
		return OBJECT_MAPPER.convertValue(object, clazz);
	}
	
	/**
	 * <big><i>XML转JSONObject</i></big>
	 *
	 * @param xmlstr
	 * 		<strong>xmlstr</strong>
	 *
	 * @return <strong>String</strong>
	 *
	 * @throws <strong><i>
	 * 		</i></strong>
	 * @Description:
	 * @author fjp
	 * @date 2018/12/20 9:12
	 */
	public static String parseXml2Json(String xmlstr) {
		return XML.toJSONObject(xmlstr).toString();
	}
	
	/**
	 * <big><i>JSONObject转XML</i></big>
	 *
	 * @param jsonstr
	 * 		<strong>jsonstr</strong>
	 *
	 * @return <strong>String</strong>
	 *
	 * @throws <strong><i>
	 * 		</i></strong>
	 * @Description:
	 * @author fjp
	 * @date 2018/12/20 9:12
	 */
	public static String parseJson2Xml(String jsonstr) {
		return XML.toString(jsonstr);
	}
}
