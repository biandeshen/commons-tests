package xyz.biandeshen.代码;

import java.io.IOException;


import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class jsontool {

  static ObjectMapper objMapper = new ObjectMapper();

  public static String obj2json(Object obj) {
    // ObjectMapper objMapper = new ObjectMapper();
    objMapper.setSerializationInclusion(Include.NON_EMPTY);
    // 允许出现特殊字符和转义符
    objMapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);

    // 允许出现单引号
    objMapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);
    String rtn;
    try {
      rtn = objMapper.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("该对象无法转换成json格式");
    }
    return rtn;
  }

  public static <T> T json2obj(String jsonStr, Class<T> clazz)
      throws JsonParseException, JsonMappingException, IOException {

    objMapper.setSerializationInclusion(Include.NON_EMPTY);
    // 允许出现特殊字符和转义符
    objMapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
    // 允许出现单引号
    objMapper.configure(Feature.ALLOW_SINGLE_QUOTES, true);

    T rtn = objMapper.readValue(jsonStr, clazz);

    return rtn;
  }

 
}
