package xyz.biandeshen.代码;


import org.apache.axis.client.Call;
import org.apache.axis.encoding.XMLType;
import org.apache.axis.message.SOAPHeaderElement;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPException;
import javax.xml.ws.BindingProvider;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;

import org.apache.axis.client.Call;
import org.apache.axis.encoding.XMLType;
import org.apache.axis.message.SOAPHeaderElement;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.Service;
import javax.xml.soap.SOAPException;
import javax.xml.ws.BindingProvider;
import java.util.Map;

/**
 * @author fjp
 * @Title: CxfService
 * @ProjectName guangda
 * @Description: CxfAndAxisService, 用以调用webService服务
 * @date 2019/3/2117:27
 */
@Component
@SuppressWarnings("all")
public class CxfAndAxisService {
	public static final int CXF_CLIENT_CONNECT_TIMEOUT = 30 * 1000;
	public static final int CXF_CLIENT_RECEIVE_TIMEOUT = 30 * 1000;
	
	public <T> T getWebService(String paraName, T t, Integer... timeout) {
		
		/**
		 * 此处即为根据 key 读取配置文件中对应的 value
		 * example:
		 * application-dev:
		 * wsUrl=http://localhost/webservice?wsdl
		 * 则 paraName 为 wsUrl
		 */
		//ResourceBundle dBResources = ResourceBundle.getBundle("application-config");
		//String url = dBResources.getString(paraName);
		String url = paraName;
		if (timeout == null || timeout.length == 0) {
			return getWebServiceByUrl(url, t);
		} else if (timeout.length == 1) {
			return getWebServiceByUrl(url, t, timeout[0], timeout[0]);
		} else {
			return getWebServiceByUrl(url, t, timeout[0], timeout[1]);
		}
	}
	
	
	public <T> T getWebServiceByUrl(String url, T t) {
		return getWebServiceByUrl(url, t, CXF_CLIENT_CONNECT_TIMEOUT, CXF_CLIENT_RECEIVE_TIMEOUT);
	}
	
	/**
	 * 1.代理类工厂的方式,需要拿到对方的接口地址
	 *
	 * @param address
	 * 		接口地址
	 * @param t
	 * 		传入service对象
	 *
	 * @return T
	 * 返回一个代理接口实现的service对象,可直接调用
	 * <p>
	 * eg:
	 * // 数据准备
	 * String userId = "maple";
	 * // 调用代理接口的方法调用并返回结果
	 * String result = us.getUserName(userId);
	 * System.out.println("返回结果:" + result);
	 */
	private <T> T getWebServiceByUrl(String address, T t, Integer connetTimeout, Integer receiveTimeout) {
		T tproxy;
		// 代理工厂
		JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
		// 设置代理地址
		jaxWsProxyFactoryBean.setAddress(address);
		// 设置接口类型
		jaxWsProxyFactoryBean.setServiceClass(t.getClass());
		// 创建一个代理接口实现
		tproxy = (T) jaxWsProxyFactoryBean.create();
		
		// 设置接口 连接超时和请求超时
		// 通过代理对象获取本地客户端
		Client proxy = ClientProxy.getClient(tproxy);
		// 通过本地客户端设置 网络策略配置
		HTTPConduit conduit = (HTTPConduit) proxy.getConduit();
		// 用于配置客户端HTTP端口的属性
		HTTPClientPolicy policy = new HTTPClientPolicy();
		// 超时控制 单位 : 毫秒
		policy.setConnectionTimeout(connetTimeout);
		policy.setReceiveTimeout(receiveTimeout);
		conduit.setClient(policy);
		
		return tproxy;
	}
	
	
	/**
	 * 2：动态调用
	 * <p>
	 * 如果接口的注解上不加targetNamespace的话，动态调用的时候，会报如下的错误。
	 * no operation was found with the name (http:.......) {serviceName}
	 */
	public String getWebServiceResultByMethodAndArgs(String address, String methodName, Object... values) throws
	                                                                                                      Exception {
		// 创建动态客户端
		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		Client client = dcf.createClient(address);
		Object[] objects;
		// invoke("方法名",参数1,参数2,参数3....);
		objects = client.invoke(methodName, values);
		return objects[0].toString();
	}
	
	/**
	 * 3.调用axis生成的 todo 需要修改成对应webService客户端生成的stub类
	 *
	 * @param address
	 * 		地址
	 *
	 * @return Remote
	 * 返回一个代理接口实现的service对象,可直接调用
	 * <p>
	 * eg:
	 * // 数据准备
	 * String userId = "maple";
	 * // 调用代理接口的方法调用并返回结果
	 * String result = us.getUserName(userId);
	 * System.out.println("返回结果:" + result);
	 */
	//public WebServicePortType getServiceObjectWithAxis(String address) throws MalformedURLException, AxisFault {
	//	try {
	//		// 服务端的url，需要根据情况更改。
	//		String endpointURL = "http://61.163.78.6:8026/RelayService.asmx?WSDL";
	//		Service service = new RelayServiceLocator();
	//		Call call = (Call) service.createCall();
	//		call.setTargetEndpointAddress(new java.net.URL(endpointURL));
	//		call.setSOAPActionURI("http://tempuri.org/" + "GetOrderListByPage");
	//		call.setOperationName("GetOrderListByPage");// 设置操作的名称。
	//		// 由于需要认证，故需要设置调用的用户名和密码。
	//		SOAPHeaderElement soapHeaderElement = new SOAPHeaderElement("http://tempuri.org/", "MySoapHeader");
	//		soapHeaderElement.setNamespaceURI("http://tempuri.org/");
	//		try {
	//			soapHeaderElement.addChildElement("UserId").setValue("zjs10021");
	//			soapHeaderElement.addChildElement("UserPW").setValue("000000");
	//		} catch (SOAPException e) {
	//			e.printStackTrace();
	//		}
	//		call.addHeader(soapHeaderElement);
	//		call.setReturnType(XMLType.SOAP_STRING);// 返回的数据类型
	//		call.addParameter("PageIndex", XMLType.SOAP_INT, ParameterMode.IN);// 参数的类型
	//		int PageIndex = 1;
	//		String ret = (String) call.invoke(new Object[]{PageIndex});// 执行调用
	//		System.out.println(ret);
	//	} catch (Exception e) {
	//		e.printStackTrace();
	//	}
	//}
}