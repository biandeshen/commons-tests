package xyz.biandeshen.groovy;

import groovy.text.StreamingTemplateEngine;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fjp
 * @Title: JavaGroovyTest
 * @ProjectName interfaceDivideOrder
 * @Description: TODO
 * @date 2018/8/2213:58
 */
public class JavaGroovyTest {
	private static StreamingTemplateEngine templateEngine = new StreamingTemplateEngine();
	
	/**
	 * Json 字段解析 事例
	 *
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@Test
	public void testJsonSlurper() throws IOException, ClassNotFoundException {
		String templete = "<% def root = new groovy.json.JsonSlurper().parseText(data); %>" + "  <logisticProviderID>${root.self_service_tel.clientFlag}</logisticProviderID>  \n";
		String xmlStr = "{\"rcv_name\":\"李老师\",\"should_receive_payment\":\"1995.0\",\"rcv_address\":\"中国山东济南市市中区,王官庄东区工业园北院13号\",\"self_service_tel\":{ \"clientFlag\": \"Test\", \"dataFlag\": \"01\", \"serviceAgent\": \" \", \"mailNo\": \" \", \"orderNo\": \"Test0001\",\"cancelTime\": \"2017-12-21 00:01:00\", \"remarks\": \"客户取消订单\" },\"self_service_name\":null,\"cust_message\":\"\",\"send_date\":null,\"sender_city\":null,\"sender_town\":null,\"product_items\":[{\"product_num\":1,\"product_id\":\"20384532\",\"product_price\":19.4,\"product_total\":19.4,\"product_name\":\"线性代数 第四版 学习参考（经济应用数学基础（二））(换)\"}],\"sender_country\":null,\"sender_name\":null,\"warehouse\":\"天津出版仓\",\"order_id\":\"A001432474304\",\"express_id\":\"17404\",\"sender_address\":null,\"goods_weight\":\"0\",\"customer_type\":\"0\",\"best_arrive_date\":\"周一至周五送货\",\"malice_status\":null,\"express_name\":\"宅急送-山东\",\"client_type\":\"1\",\"rcv_zip\":\"250021\",\"rcv_city\":\"济南市\",\"pay_way\":\"3\",\"origin_order_id\":null,\"is_multi_package\":\"1\",\"rcv_town\":\"市中区\",\"refund_reason\":\"\",\"order_date\":\"2017-11-12 10:00:34\",\"sender_province\":null,\"mapping_tracking_number\":\"A001432474304\",\"sender_mobile_tel\":null,\"rcv_province\":\"山东\",\"rcv_fix_tel\":\"\",\"client_order_type\":\"1\",\"goods_payment\":\"1995.0\",\"cover_code\":null,\"rcv_mobile_tel\":\"18615591688\",\"package_size\":null,\"self_service_address\":null,\"package_num\":\"2\",\"should_refund_payment\":null,\"expensive_items\":null,\"shipment_type\":\"1\",\"rcv_country\":null,\"sub_express_name\":\"宅急送-山东\",\"sender_zip\":null,\"sender_fix_tel\":null}";
		System.out.println("templete = " + templete);
		System.out.println("xmlStr = " + xmlStr);
		Map<String, Object> map = new HashMap<>(16);
		map.put("data", xmlStr);
		String out = templateEngine.createTemplate(templete).make(map).toString();
		System.out.println();
		System.out.println(out);
	}
}
