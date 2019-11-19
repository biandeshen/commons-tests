package xyz.biandeshen.collections;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fjp
 * @Title: TestClone
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/11/515:14
 */
public class TestClone {
	
	public static void main(String[] args) {
		Map<String, Object> stringObjectMap = new HashMap<>();
		stringObjectMap.put("1", 1);
		stringObjectMap.put("2", 1);
		stringObjectMap.put("3", 1);
		stringObjectMap.put("4", 1);
		
		Map mapclone = stringObjectMap;
		mapclone.remove("1");
		
		System.out.println("mapclone = " + mapclone);
		System.err.println("stringObjectMap = " + stringObjectMap);
		
	}
	
	
}
