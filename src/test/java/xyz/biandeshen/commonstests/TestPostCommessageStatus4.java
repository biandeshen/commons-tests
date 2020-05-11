package xyz.biandeshen.commonstests;

import com.sun.istack.internal.Nullable;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import spp.model.common.CommonMsgModel;
import xyz.biandeshen.commonstests.util.common.JSONUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * @author fjp
 * @Title: TestPostStatus
 * @ProjectName StandardPostStatus
 * @Description: 状态推送
 * @date 2019/3/518:33
 */

public class TestPostCommessageStatus4 {
	
	String uuid = UUID.randomUUID().toString().replace("-", "");
	private static final String clientFlag = "\"test\"";
	private static final String mailNo = "\"ZJS024032437284\"";
	private static final String orderNo = "\"123456\"";
	private static String wt = "\"2201\"";
	//月结编码
	private static String ce = "\"100310\"";
	//收件地址
	private static String receiveAddress = "\"湖北省咸宁市赤壁市\"";
	//重量
	private static String weight = "null";
	
	//private static final String url = "http://10.10.12.109:23009/acceptstatedata";
	
	private static final String url = "http://119.145.30.196:8085/FGBP-FACE-UNIFIEDAPI/third/zjs/trajectory";
	
	//拿趣用
	//private static final String url = "https://dev0-api.nqyong.com/api/order/zhaijisong/stateNotify";
	//宽容
	//private static final String url = "http://ps.aries-far.com/test1/b2c/routeTrack/zjsOrderTrack";
	private static final String strSeed = "aafc04a1bacb487fa8d03f2a7bfdb555";
	
	
	@Test
	// TODO 揽收
	public void postGot() {
		System.err.println("揽收:" + postStatusToURL(GOT));
	}
	
	@Test
	// TODO 入库
	public void postArrival() {
		System.err.println("入库:" + postStatusToURL(ARRIVAL));
	}
	
	@Test
	// TODO 出库
	public void postDEPARTURE() {
		System.err.println("出库:" + postStatusToURL(DEPARTURE));
	}
	
	@Test
	// TODO 派送
	public void postSENTSCAN() {
		System.err.println("派送:" + postStatusToURL(SENT_SCAN));
	}
	
	@Test
	// TODO 签收
	public void postSIGNED() {
		System.err.println("签收:" + postStatusToURL(SIGNED));
	}
	
	
	//@Test
	//// TODO 返货入库
	//public void postRARRIVAL() {
	//	System.err.println("返货入库:" + postStatusToURL(RARRIVAL));
	//}
	//
	//@Test
	//// TODO 返货出库
	//public void postRSENTSCAN() {
	//	System.err.println("返货出库:" + postStatusToURL(RSENT_SCAN));
	//}
	//
	//@Test
	//// TODO 返货派送
	//public void postRDEPARTURE() {
	//	System.err.println("返货派送:" + postStatusToURL(RDEPARTURE));
	//}
	//
	//@Test
	//// TODO 返货签收
	//public void postRSIGNED() {
	//	System.err.println("返货签收:" + postStatusToURL(RSIGNED));
	//}
	
	@Test
	// TODO 拒收
	public void postFAILED() {
		System.err.println("拒收:" + postStatusToURL(FAILED));
	}
	
	
	private String postStatusToURL(String nowStr) {
		//将字符串转换成UTF-8格式进行md5加密,截取
		//String verifyData = getVerifyDataByRdm(nowStr, "1111", "1111", clientFlag, strSeed);
		//TODO 输出verifyData
		System.err.println(nowStr);
		
		// 1. 当headers设置为MediaType.APPLICATION_FORM_URLENCODED(或不设置)时，
		// 此写法等效于postman中 Body -》 x-www-form-urlencoded  -》 Key,Value
		// 通常以RequestBody的参数接收形式接收时，请求类型支持 get/post , 参数值为 interface?var1=val1&var2=val2&....
		// 参数值中参数名称为 LinkedMultiValueMap 中的 key, 参数值为 LinkedMultiValueMap 中 key 对应的 value
		// 2. 当headers设置为MediaType.APPLICATION_JSON_UTF8时，
		// 此写法等效于postman中 Body -》raw -》application/json -》json
		// 通常以RequestBody的参数接收形式接收时，请求类型支持 get/post , 参数值为 标准json
		// 参数值json中参数键值对中的键为 LinkedMultiValueMap 中的 key, 参数键值对中的值为 LinkedMultiValueMap 中 key 对应的 value
		//LinkedMultiValueMap postParameters = new LinkedMultiValueMap<>();
		////postParameters.add("clientFlag", clientFlag);
		////postParameters.add("verifyData", verifyData);
		//postParameters.add("cmm", nowStr);
		CommonMsgModel cmm = new CommonMsgModel();
		cmm = JSONUtils.parseJsonToBeanGsonImpl(nowStr, CommonMsgModel.class);
		
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<CommonMsgModel> result = restTemplate.postForEntity(url, cmm, CommonMsgModel.class);
		//TODO 此方法不支持get请求，在 application/json 或 x-www-form-urlencoded 两种状态下
		//ResponseEntity<String> result = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
		//TODO 输出返回结果
		String resStr = "";
		try {
			resStr = URLDecoder.decode(JSONUtils.objectToJsonStrImpl(result.getBody()), StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return resStr;
	}
	
	private LocalDateTime localDateTime = LocalDateTime.now();
	private String nowTime = "\"" + localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\"";
	
	
	private String GOT = "{\n" + "\t\"id\": \"AC12000900016B884D5767881CEAF140\",\n" + "\t\"mid\": \"dc5c43943312e6e7b89a8c7ae3d7ffb8\",\n" + "\t\"wc\": " + mailNo + ",\n" + "\t\"pm\": null,\n" + "\t\"puc\": \"282633\",\n" + "\t\"pun\": \"贾天龙\",\n" + "\t\"cc\": " + orderNo + ",\n" + "\t\"ce\": " + ce + ",\n" + "\t\"wt\": " + wt + ",\n" + "\t\"sp\": null,\n" + "\t\"sc\": null,\n" + "\t\"sa\": null,\n" + "\t\"rp\": null,\n" + "\t\"rc\": null,\n" + "\t\"ra\": " + receiveAddress + ",\n" + "\t\"ou\": \"1001\",\n" + "\t\"ouc\": \"A\",\n" + "\t\"oun\": \"(ZJS)总公司\",\n" + "\t\"oup\": null,\n" + "\t\"nd\": \"1\",\n" + "\t\"on\": \"282633\",\n" + "\t\"one\": \"贾天龙\",\n" + "\t\"onp\": \"15839162779\",\n" + "\t\"ot\": " + nowTime + ",\n" + "\t\"op\": \"北京市\",\n" + "\t\"oc\": \"北京市\",\n" + "\t\"oa\": null,\n" + "\t\"ost\": \"BOSCORP00000000A0008\",\n" + "\t\"np\": null,\n" + "\t\"nc\": null,\n" + "\t\"na\": null,\n" + "\t\"ne\": null,\n" + "\t\"nec\": null,\n" + "\t\"net\": null,\n" + "\t\"nn\": \"北京_朝阳营业所_崇文门营业厅\",\n" + "\t\"nep\": null,\n" + "\t\"dc\": null,\n" + "\t\"dn\": null,\n" + "\t\"dm\": null,\n" + "\t\"st\": \"2232\",\n" + "\t\"sn\": \"宋总敏\",\n" + "\t\"ec\": null,\n" + "\t\"et\": null,\n" + "\t\"ee\": null,\n" + "\t\"ew\": null,\n" + "\t\"tu\": null,\n" + "\t\"tun\": null,\n" + "\t\"twc\": null,\n" + "\t\"trc\": null,\n" + "\t\"trn\": null,\n" + "\t\"cf\": " + clientFlag + ",\n" + "\t\"iswb\": \"0\",\n" + "\t\"spa\": null,\n" + "\t\"ts\": null,\n" + "\t\"weight\": " + weight + ",\n" + "\t\"packageNum\": null}";
	private String ARRIVAL = "{\n" + "\t\"id\": \"AC12000900016B884D5767881CEAF140\",\n" + "\t\"mid\": \"dc5c43943312e6e7b89a8c7ae3d7ffb8\",\n" + "\t\"wc\": " + mailNo + ",\n" + "\t\"pm\": null,\n" + "\t\"puc\": \"282633\",\n" + "\t\"pun\": \"贾天龙\",\n" + "\t\"cc\": " + orderNo + ",\n" + "\t\"ce\": " + ce + ",\n" + "\t\"wt\": " + wt + ",\n" + "\t\"sp\": null,\n" + "\t\"sc\": null,\n" + "\t\"sa\": null,\n" + "\t\"rp\": null,\n" + "\t\"rc\": null,\n" + "\t\"ra\": " + receiveAddress + ",\n" + "\t\"ou\": \"1001\",\n" + "\t\"ouc\": \"A\",\n" + "\t\"oun\": \"(ZJS)总公司\",\n" + "\t\"oup\": null,\n" + "\t\"nd\": \"2\",\n" + "\t\"on\": \"282633\",\n" + "\t\"one\": \"贾天龙\",\n" + "\t\"onp\": \"15839162779\",\n" + "\t\"ot\": " + nowTime + ",\n" + "\t\"op\": \"北京市\",\n" + "\t\"oc\": \"北京市\",\n" + "\t\"oa\": null,\n" + "\t\"ost\": \"BOSCORP00000000A0008\",\n" + "\t\"np\": null,\n" + "\t\"nc\": null,\n" + "\t\"na\": null,\n" + "\t\"ne\": null,\n" + "\t\"nec\": null,\n" + "\t\"net\": null,\n" + "\t\"nn\": \"北京_朝阳营业所_崇文门营业厅\",\n" + "\t\"nep\": null,\n" + "\t\"dc\": null,\n" + "\t\"dn\": null,\n" + "\t\"dm\": null,\n" + "\t\"st\": \"2232\",\n" + "\t\"sn\": \"宋总敏\",\n" + "\t\"ec\": null,\n" + "\t\"et\": null,\n" + "\t\"ee\": null,\n" + "\t\"ew\": null,\n" + "\t\"tu\": null,\n" + "\t\"tun\": null,\n" + "\t\"twc\": null,\n" + "\t\"trc\": null,\n" + "\t\"trn\": null,\n" + "\t\"cf\": " + clientFlag + ",\n" + "\t\"iswb\": \"0\",\n" + "\t\"spa\": null,\n" + "\t\"ts\": null,\n" + "\t\"weight\": " + weight + ",\n" + "\t\"packageNum\": null}";
	private String DEPARTURE = "{\n" + "\t\"id\": \"AC12000900016B884D5767881CEAF140\",\n" + "\t\"mid\": \"dc5c43943312e6e7b89a8c7ae3d7ffb8\",\n" + "\t\"wc\": " + mailNo + ",\n" + "\t\"pm\": null,\n" + "\t\"puc\": \"282633\",\n" + "\t\"pun\": \"贾天龙\",\n" + "\t\"cc\": " + orderNo + ",\n" + "\t\"ce\": " + ce + ",\n" + "\t\"wt\": " + wt + ",\n" + "\t\"sp\": null,\n" + "\t\"sc\": null,\n" + "\t\"sa\": null,\n" + "\t\"rp\": null,\n" + "\t\"rc\": null,\n" + "\t\"ra\": " + receiveAddress + ",\n" + "\t\"ou\": \"1001\",\n" + "\t\"ouc\": \"A\",\n" + "\t\"oun\": \"(ZJS)总公司\",\n" + "\t\"oup\": null,\n" + "\t\"nd\": \"3\",\n" + "\t\"on\": \"282633\",\n" + "\t\"one\": \"贾天龙\",\n" + "\t\"onp\": \"15839162779\",\n" + "\t\"ot\": " + nowTime + ",\n" + "\t\"op\": \"北京市\",\n" + "\t\"oc\": \"北京市\",\n" + "\t\"oa\": null,\n" + "\t\"ost\": \"BOSCORP00000000A0008\",\n" + "\t\"np\": null,\n" + "\t\"nc\": null,\n" + "\t\"na\": null,\n" + "\t\"ne\": null,\n" + "\t\"nec\": null,\n" + "\t\"net\": null,\n" + "\t\"nn\": \"北京_朝阳营业所_崇文门营业厅\",\n" + "\t\"nep\": null,\n" + "\t\"dc\": null,\n" + "\t\"dn\": null,\n" + "\t\"dm\": null,\n" + "\t\"st\": \"2232\",\n" + "\t\"sn\": \"宋总敏\",\n" + "\t\"ec\": null,\n" + "\t\"et\": null,\n" + "\t\"ee\": null,\n" + "\t\"ew\": null,\n" + "\t\"tu\": null,\n" + "\t\"tun\": null,\n" + "\t\"twc\": null,\n" + "\t\"trc\": null,\n" + "\t\"trn\": null,\n" + "\t\"cf\": " + clientFlag + ",\n" + "\t\"iswb\": \"0\",\n" + "\t\"spa\": null,\n" + "\t\"ts\": null,\n" + "\t\"weight\": " + weight + ",\n" + "\t\"packageNum\": null}";
	private String SENT_SCAN = "{\n" + "\t\"id\": \"AC12000900016B884D5767881CEAF140\",\n" + "\t\"mid\": \"dc5c43943312e6e7b89a8c7ae3d7ffb8\",\n" + "\t\"wc\": " + mailNo + ",\n" + "\t\"pm\": null,\n" + "\t\"puc\": \"282633\",\n" + "\t\"pun\": \"贾天龙\",\n" + "\t\"cc\": " + orderNo + ",\n" + "\t\"ce\": " + ce + ",\n" + "\t\"wt\": " + wt + ",\n" + "\t\"sp\": null,\n" + "\t\"sc\": null,\n" + "\t\"sa\": null,\n" + "\t\"rp\": null,\n" + "\t\"rc\": null,\n" + "\t\"ra\": " + receiveAddress + ",\n" + "\t\"ou\": \"1001\",\n" + "\t\"ouc\": \"A\",\n" + "\t\"oun\": \"北京_朝阳营业所_崇文门营业厅\",\n" + "\t\"oup\": null,\n" + "\t\"nd\": \"4\",\n" + "\t\"on\": \"282633\",\n" + "\t\"one\": \"贾天龙\",\n" + "\t\"onp\": \"15839162779\",\n" + "\t\"ot\": " + nowTime + ",\n" + "\t\"op\": \"北京市\",\n" + "\t\"oc\": \"北京市\",\n" + "\t\"oa\": null,\n" + "\t\"ost\": \"BOSCORP00000000A0008\",\n" + "\t\"np\": null,\n" + "\t\"nc\": null,\n" + "\t\"na\": null,\n" + "\t\"ne\": null,\n" + "\t\"nec\": null,\n" + "\t\"net\": null,\n" + "\t\"nn\": \"北京_朝阳营业所_崇文门营业厅\",\n" + "\t\"nep\": null,\n" + "\t\"dc\": null,\n" + "\t\"dn\": \"杨成名\",\n" + "\t\"dm\": \"13779977777\",\n" + "\t\"st\": \"2232\",\n" + "\t\"sn\": \"宋总敏\",\n" + "\t\"ec\": null,\n" + "\t\"et\": null,\n" + "\t\"ee\": null,\n" + "\t\"ew\": null,\n" + "\t\"tu\": null,\n" + "\t\"tun\": null,\n" + "\t\"twc\": null,\n" + "\t\"trc\": null,\n" + "\t\"trn\": null,\n" + "\t\"cf\": " + clientFlag + ",\n" + "\t\"iswb\": \"0\",\n" + "\t\"spa\": null,\n" + "\t\"ts\": null,\n" + "\t\"weight\": " + weight + ",\n" + "\t\"packageNum\": null}";
	private String SIGNED = "{\n" + "\t\"id\": \"AC12000900016B884D5767881CEAF140\",\n" + "\t\"mid\": \"dc5c43943312e6e7b89a8c7ae3d7ffb8\",\n" + "\t\"wc\": " + mailNo + ",\n" + "\t\"pm\": null,\n" + "\t\"puc\": \"282633\",\n" + "\t\"pun\": \"贾天龙\",\n" + "\t\"cc\": " + orderNo + ",\n" + "\t\"ce\": " + ce + ",\n" + "\t\"wt\": " + wt + ",\n" + "\t\"sp\": null,\n" + "\t\"sc\": null,\n" + "\t\"sa\": null,\n" + "\t\"rp\": null,\n" + "\t\"rc\": null,\n" + "\t\"ra\": " + receiveAddress + ",\n" + "\t\"ou\": \"1001\",\n" + "\t\"ouc\": \"A\",\n" + "\t\"oun\": \"(ZJS)总公司\",\n" + "\t\"oup\": null,\n" + "\t\"nd\": \"6\",\n" + "\t\"on\": \"282633\",\n" + "\t\"one\": \"贾天龙\",\n" + "\t\"onp\": \"15839162779\",\n" + "\t\"ot\": " + nowTime + ",\n" + "\t\"op\": \"北京市\",\n" + "\t\"oc\": \"北京市\",\n" + "\t\"oa\": null,\n" + "\t\"ost\": \"BOSCORP00000000A0008\",\n" + "\t\"np\": null,\n" + "\t\"nc\": null,\n" + "\t\"na\": null,\n" + "\t\"ne\": null,\n" + "\t\"nec\": null,\n" + "\t\"net\": null,\n" + "\t\"nn\": \"北京_朝阳营业所_崇文门营业厅\",\n" + "\t\"nep\": null,\n" + "\t\"dc\": null,\n" + "\t\"dn\": null,\n" + "\t\"dm\": null,\n" + "\t\"st\": \"2232\",\n" + "\t\"sn\": \"宋总敏\",\n" + "\t\"ec\": null,\n" + "\t\"et\": null,\n" + "\t\"ee\": null,\n" + "\t\"ew\": null,\n" + "\t\"tu\": null,\n" + "\t\"tun\": null,\n" + "\t\"twc\": null,\n" + "\t\"trc\": null,\n" + "\t\"trn\": null,\n" + "\t\"cf\": " + clientFlag + ",\n" + "\t\"iswb\": \"0\",\n" + "\t\"spa\": null,\n" + "\t\"ts\": null,\n" + "\t\"weight\": " + weight + ",\n" + "\t\"packageNum\": null}";
	private String FAILED = "{\n" + "\t\"id\": \"AC12000900016B884D5767881CEAF140\",\n" + "\t\"mid\": \"dc5c43943312e6e7b89a8c7ae3d7ffb8\",\n" + "\t\"wc\": " + mailNo + ",\n" + "\t\"pm\": null,\n" + "\t\"puc\": \"282633\",\n" + "\t\"pun\": \"贾天龙\",\n" + "\t\"cc\": " + orderNo + ",\n" + "\t\"ce\": " + ce + ",\n" + "\t\"wt\": " + wt + ",\n" + "\t\"sp\": null,\n" + "\t\"sc\": null,\n" + "\t\"sa\": null,\n" + "\t\"rp\": null,\n" + "\t\"rc\": null,\n" + "\t\"ra\": " + receiveAddress + ",\n" + "\t\"ou\": \"1001\",\n" + "\t\"ouc\": \"A\",\n" + "\t\"oun\": \"(ZJS)总公司\",\n" + "\t\"oup\": null,\n" + "\t\"nd\": \"1\",\n" + "\t\"on\": \"282633\",\n" + "\t\"one\": \"贾天龙\",\n" + "\t\"onp\": \"15839162779\",\n" + "\t\"ot\": " + nowTime + ",\n" + "\t\"op\": \"北京市\",\n" + "\t\"oc\": \"北京市\",\n" + "\t\"oa\": null,\n" + "\t\"ost\": \"BOSCORP00000000A0008\",\n" + "\t\"np\": null,\n" + "\t\"nc\": null,\n" + "\t\"na\": null,\n" + "\t\"ne\": null,\n" + "\t\"nec\": null,\n" + "\t\"net\": null,\n" + "\t\"nn\": \"北京_朝阳营业所_崇文门营业厅\",\n" + "\t\"nep\": null,\n" + "\t\"dc\": null,\n" + "\t\"dn\": null,\n" + "\t\"dm\": null,\n" + "\t\"st\": \"2232\",\n" + "\t\"sn\": \"宋总敏\",\n" + "\t\"ec\": null,\n" + "\t\"et\": null,\n" + "\t\"ee\": null,\n" + "\t\"ew\": null,\n" + "\t\"tu\": null,\n" + "\t\"tun\": null,\n" + "\t\"twc\": null,\n" + "\t\"trc\": null,\n" + "\t\"trn\": null,\n" + "\t\"cf\": " + clientFlag + ",\n" + "\t\"iswb\": \"0\",\n" + "\t\"spa\": null,\n" + "\t\"ts\": null,\n" + "\t\"weight\": " + weight + ",\n" + "\t\"packageNum\": null}";
	//private String RARRIVAL = "{\"clientFlag\":\"" + clientFlag + "\",\"mailNo\":\"1-" + mailNo + "\",\"orderNo\":\"" + orderNo + "\",\"time\":\"" + nowTime + "\",\"city\":\"北京市\",\"facilityType\":\"1\",\"facilityNo\":\"1202\",\"facilityName\":\"北京_朝阳营业所_崇文门营业厅\",\"action\":\"RARRIVAL\",\"tz\":\"8\",\"nextCity\":\"北京市\",\"country\":\"China\"}";
	//private String RSENT_SCAN = "{\"clientFlag\":\"" + clientFlag + "\",\"mailNo\":\"1-" + mailNo + "\",\"orderNo\":\"" + orderNo + "\",\"time\":\"" + nowTime + "\",\"city\":\"北京市\",\"facilityType\":\"1\",\"facilityNo\":\"1202\",\"facilityName\":\"北京_朝阳营业所_崇文门营业厅\",\"action\":\"RDEPARTURE\",\"tz\":\"8\",\"nextCity\":\"北京市\",\"country\":\"China\"}";
	//private String RDEPARTURE = "{\"clientFlag\":\"" + clientFlag + "\",\"mailNo\":\"1-" + mailNo + "\",\"orderNo\":\"" + orderNo + "\",\"time\":\"" + nowTime + "\",\"city\":\"北京市\",\"facilityType\":\"1\",\"facilityNo\":\"1202\",\"facilityName\":\"北京_朝阳营业所_崇文门营业厅\",\"action\":\"RSENT_SCAN\",\"tz\":\"8\",\"nextCity\":\"北京市\",\"country\":\"China\",\"contacter\":\"张辉1\",\"contactPhone\":\"18001395384\"}";
	//private String RSIGNED = "{\"clientFlag\":\"" + clientFlag + "\",\"mailNo\":\"1-" + mailNo + "\",\"orderNo\":\"" + orderNo + "\",\"time\":\"" + nowTime + "\",\"city\":\"北京市\",\"facilityType\":\"1\",\"facilityNo\":\"1202\",\"facilityName\":\"北京_朝阳营业所_崇文门营业厅\",\"action\":\"RSIGNED\",\"tz\":\"8\",\"nextCity\":\"北京市\",\"country\":\"China\",\"signer\":\"test\"}";
	
	
	@Test
	public void toverify() {
		String data = "{\"clientFlag\":\"test\",\"mailNo\":\"A602941984778\",\"time\":\"2019-03-18 18:50:02\",\"desc\":\"客户已签收\",\"city\":\"北京市\",\"facilityType\":\"1\",\"facilityNo\":\"1076\",\"facilityName\":\"北京_朝阳营业所_崇文门营业厅\",\"action\":\"SIGNED\",\"tz\":\"8\",\"country\":\"China\",\"signer\":\"test\"}";
		String verifyData = getVerifyDataByRdm(data, "1111", "1111", clientFlag, strSeed);
		//TODO 输出verifyData
		System.err.println(verifyData);
		
	}
	
	
	public String getVerifyDataByRdm(String datajson, String rmd1, String rmd2, String clientFlag, String strSeed) {
		String strConst = "z宅J急S送g";
		String str = rmd1 + clientFlag + datajson + strSeed + strConst + rmd2;
		
		String strMd5 = encryptMD5(str, "UTF-8");
		if (strMd5 != null) {
			strMd5 = strMd5.replace("-", " ");
			strMd5 = strMd5.toLowerCase();
			return rmd1 + strMd5.substring(7, 28) + rmd2;
		}
		return "";
	}
	
	public String encryptMD5(String data, String charsetName) {
		return encryptMD5(stringToBytes(data, charsetName));
	}
	
	public String encryptMD5(byte[] data) {
		return getMessageDigestEncryptResult(data, "MD5");
	}
	
	private String getMessageDigestEncryptResult(byte[] data, String keyAlg) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(keyAlg);
			messageDigest.update(data);
			return bytesToString(messageDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return keyAlg;
	}
	
	/**
	 * byte[] 转 字符串
	 *
	 * @param bytes
	 * 		字符数组
	 *
	 * @return 转换结果
	 */
	public static String bytesToString(byte[] bytes) {
		int i;
		StringBuilder buf = new StringBuilder();
		for (byte aB : bytes) {
			i = aB;
			if (i < 0) {
				i += 256;
			}
			if (i < 16) {
				buf.append("0");
			}
			buf.append(Integer.toHexString(i));
		}
		return buf.toString();
	}
	
	/**
	 * String 转 byte[]
	 *
	 * @param str
	 * 		传入字符串
	 * @param charsetName
	 * 		编码格式字符串
	 *
	 * @return 转换结果
	 */
	public static byte[] stringToBytes(String str, @Nullable String charsetName) {
		Charset charset = Charset.forName("Utf-8");
		if (!StringUtils.isEmpty(charsetName)) {
			charset = Charset.forName(charsetName);
		}
		return str.getBytes(charset);
	}
}
