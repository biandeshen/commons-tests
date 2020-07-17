package com.cn.zjs.guangda.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.BusFactory;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.service.model.BindingInfo;
import org.apache.cxf.service.model.BindingOperationInfo;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

public final class WebServiceClient {
	
	private static final Logger log = LoggerFactory.getLogger(WebServiceClient.class);
	
	private WebServiceClient() {
	
	}
	
	/**
	 * WebServiceClient
	 *
	 * @param wsdlAddress
	 * 		wsdl地址
	 * @param method
	 * 		调用方法
	 * @param parameters
	 * 		参数列表，按照接口参数顺序依次放入数组
	 *
	 * @return
	 *
	 * @description 动态调用
	 * @author wangjing-5
	 * @date 2019/3/22 16:03
	 * @version V3.0.0
	 */
	public static String sendData(String wsdlAddress, String method, Object... parameters) throws Exception {
		if (StringUtils.isBlank(wsdlAddress)) {
			log.info("WSDL地址调用前检测是否可用：wsdl地址为空，直接退出");
			return null;
		}
		try {
			/** 如果wsdl地址不为空，先验证此地址是否可用 */
			HttpURLConnection connection = (HttpURLConnection) new URL(wsdlAddress).openConnection();
			connection.setConnectTimeout(3000);
			connection.setReadTimeout(30000);
			if (HttpURLConnection.HTTP_OK != connection.getResponseCode()) {
				log.info("WSDL地址不可用:{}", wsdlAddress);
			}
		} catch (IOException e) {
			/** 连接失败，直接抛出异常，继续访问下一台集群服务器，避免长时间连接占用资源 */
			log.error("WSDL地址调用前检测是否可用：经检测连接不可用", e);
			throw new ConnectException();
		}
		JaxWsDynamicClientFactory jaxWsDynamicClientFactory = JaxWsDynamicClientFactory.newInstance();
		Client client = jaxWsDynamicClientFactory.createClient(wsdlAddress);
		HTTPConduit conduit = (HTTPConduit) client.getConduit();
		/** 设置超时时间 */
		HTTPClientPolicy policy = new HTTPClientPolicy();
		policy.setConnectionTimeout(3000);
		policy.setReceiveTimeout(30000);
		conduit.setClient(policy);
		
		/** 处理由于webservice接口和实现不在同一个包下，namespace不一致，而找不到调用方法的问题 */
		Endpoint endpoint = client.getEndpoint();
		String nameSpace = endpoint.getService().getName().getNamespaceURI();
		QName opName = new QName(nameSpace, method);
		BindingInfo bindingInfo = endpoint.getEndpointInfo().getBinding();
		if (bindingInfo.getOperation(opName) == null) {
			for (BindingOperationInfo operationInfo : bindingInfo.getOperations()) {
				if (method.equals(operationInfo.getName().getLocalPart())) {
					opName = operationInfo.getName();
					break;
				}
			}
		}
		try {
			Object[] objects = client.invoke(opName, parameters);
			return objects[0].toString();
		} catch (Exception e) {
			log.error("webService动态调用接口异常，【url】:{},【method】:{},【parameters】:{}", wsdlAddress, method, parameters, e);
		} finally {
			/** 为了防止服务端改了方法名以后,客户端仍然获取的是老的方法名 */
			BusFactory.setDefaultBus(null);
			//client.close();
			client.destroy();
		}
		return null;
	}
	
	
	public static void main(String[] args) {
		String type = "XSCK";
		String fromSYS = "test";
		String fromSYSCode = "95271";
		String requestTime = "2019-3-22 19:56:24";
		String appKey = "test";
		String sign = "test";
		String requestContent = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" + "<root>\n" + "    " +
				                        "<SalesOrderReceivings>\n" + "        <SalesOrderReceiving>\n" + "            "
				                        + "<orderCode>201702131024024</orderCode>\n" + "            " + "<companyCode" + ">130</companyCode>\n" + "            <billTypeCode>OUT_SALES" + "</billTypeCode>\n" + "            <orderDate>2017-02-10 " + "10:13:31" + "</orderDate>\n" + "            <fromOrgName>-</fromOrgName>\n" + "   " + "   " + "      <fromContactName />\n" + "            <fromTelephone />\n" + "      " + "      <fromAddress />\n" + "            <toOrgName>小梅子</toOrgName>\n" + "    " + "        <toContactName>小梅子</toContactName>\n" + "            " + "<toTelephone>18691323068</toTelephone>\n" + "            <toAddress>陕西省 渭南市 " + "华阴市 051基地 卫生所</toAddress>\n" + "            <carrierCode>100001</carrierCode" + ">\n" + "            <finsuType>2064</finsuType>\n" + "            " + "<receiveAmount>0.00</receiveAmount>\n" + "            <declaredAmount>0" + ".00</declaredAmount>\n" + "            <premium>0.00</premium>\n" + "        " + "    <beReturn>N</beReturn>\n" + "            <orderTotalAmount>2057" + "</orderTotalAmount>\n" + "            <orderDiscount>-9.73</orderDiscount>\n" + "            <freight>10</freight>\n" + "            <shipWarehouseCode>BJDP001</shipWarehouseCode>\n" + "            <description />\n" + "            <invoiceType></invoiceType>\n" + "            <invoiceTitle>个人</invoiceTitle>\n" + "            <drawer>nubia</drawer>\n" + "            <postCode></postCode>\n" + "            <operTime>2017-02-13 10:13:31:823</operTime>\n" + "            <productDetail>\n" + "                <product>\n" + "                    <itemCode>127201903270003</itemCode>\n" + "                    <itemName>宅急送测试03</itemName>\n" + "                    <baseUnit>个</baseUnit>\n" + "                    <unitPrice>58</unitPrice>\n" + "                    <expectedQuantity>1</expectedQuantity>\n" + "                    <supplierCode />\n" + "                    <extendPropC1 />\n" + "                    <extendPropC2 />\n" + "                    <extendPropC3 />\n" + "                    <extendPropC4 />\n" + "                    <extendPropC5 />\n" + "                    <extendPropC6 />\n" + "                    <extendPropC7 />\n" + "                    <extendPropC8 />\n" + "                    <extendPropC9></extendPropC9>\n" + "                </product>\n" + "                <product>\n" + "                    <itemCode>127201903270004</itemCode>\n" + "                    <itemName>宅急送测试04</itemName>\n" + "                    <baseUnit>个</baseUnit>\n" + "                    <unitPrice>1999</unitPrice>\n" + "                    <expectedQuantity>1</expectedQuantity>\n" + "                    <supplierCode />\n" + "                    <extendPropC1 />\n" + "                    <extendPropC2 />\n" + "                    <extendPropC3 />\n" + "                    <extendPropC4 />\n" + "                    <extendPropC5 />\n" + "                    <extendPropC6 />\n" + "                    <extendPropC7 />\n" + "                    <extendPropC8 />\n" + "                    <extendPropC9></extendPropC9>\n" + "                </product>\n" + "            </productDetail>\n" + "        </SalesOrderReceiving>\n" + "    </SalesOrderReceivings>\n" + "</root>";
		
		try {
			String getRequestContentFromV2 = WebServiceClient.sendData("http://businesstest" + ".zjs.com" + ".cn:8001" +
					                                                           "/edi/gdck/services/webService?wsdl",
			                                                           "getRequestContentFromV2", type, fromSYS,
			                                                           fromSYSCode, requestTime, appKey, sign,
			                                                           requestContent);
			System.out.println("getRequestContentFromV2 = " + getRequestContentFromV2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

