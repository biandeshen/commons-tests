package xyz.biandeshen.commonstests;


import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ZJSHttpUtils2 {
	public static String post(String url, Map<String, String> parameter) {
		// 用于接收返回参数
		String returnData = "";
		try {
			
			HttpPost post = new HttpPost(url);
			// 拿到参数map 放入,utf编码
			List<BasicNameValuePair> list = new ArrayList<>();
			int i = 0;
			for (String key : parameter.keySet()) {
				// 将map里的参数 放入BasicNameValuePair 类里拼接 & =
				list.add(new BasicNameValuePair(key, parameter.get(key)));
				System.out.println(list.get(i));
				i += 1;
			}
			
			UrlEncodedFormEntity uefe = new UrlEncodedFormEntity(list, "utf-8");
			post.setEntity(uefe);
			// 创建一个客户端
			CloseableHttpClient client = HttpClientBuilder.create().build();
			HttpResponse response = client.execute(post);
			// 根据返回的Http状态码，判断是否成功
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				//返回数据
				returnData = EntityUtils.toString(response.getEntity(), "utf-8");
			} else {
				throw new RuntimeException("接口连接失败");
			}
		} catch (Exception e) {
			throw new RuntimeException("接口连接失败");
		}
		return returnData;
	}
}