package xyz.biandeshen.commonstests;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

/**
 * @author fjp
 * @Title: TestDanHaoChiAPI2
 * @ProjectName DanHaoChi
 * @Description: 获取单号
 * @date 2019/1/2811:34
 */
public class TestDanHaoChiAPI2 {
	
	public static void main(String[] args) {
		String APP_ENCRYPT = "5f6befe7e27e4d73973c86ab347db8b3";
		String json = "{\"clientId\":\"DH100621\",\"key\":\"5f6befe7e27e4d73973c86ab347db8b3\",\"applynum\":\"10\"}";
		
		RestTemplate rest = new RestTemplate();
		ApplyRequire require = new ApplyRequire();
		require.setClientid("DH100621");
		require.setApplynum(10);
		require.setKey("5f6befe7e27e4d73973c86ab347db8b3");
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/json");
		HttpEntity<ApplyRequire> request = new HttpEntity<>(require, headers);
		
		//String url="http://cntm.zjs.com.cn/interface/iwc/ctdanhaochitest";
		String url="http://10.10.6.109:9021/wocode-api/fetchWocode";
		
		String result = rest.postForObject(url, request, String.class);
		System.out.println("加密前：" + json);
		System.out.println("加密密钥和解密密钥：" + APP_ENCRYPT + "\n");
		System.out.println(result);
	}
	
	
}

class ApplyRequire {
	
	//客户标识
	private String clientid;
	//系统标识
	private String key;
	//申请数量
	private Integer applynum;
	
	/**
	 * Gets the value of clientid.
	 *
	 * @return the value of clientid
	 */
	public String getClientid() {
		return clientid;
	}
	
	/**
	 * Sets the clientid.
	 *
	 * <p>You can use getClientid() to get the value of clientid</p>
	 *
	 * @param clientid
	 * 		clientid
	 */
	public void setClientid(String clientid) {
		this.clientid = clientid;
	}
	
	/**
	 * Gets the value of key.
	 *
	 * @return the value of key
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * Sets the key.
	 *
	 * <p>You can use getKey() to get the value of key</p>
	 *
	 * @param key
	 * 		key
	 */
	public void setKey(String key) {
		this.key = key;
	}
	
	/**
	 * Gets the value of applynum.
	 *
	 * @return the value of applynum
	 */
	public Integer getApplynum() {
		return applynum;
	}
	
	/**
	 * Sets the applynum.
	 *
	 * <p>You can use getApplynum() to get the value of applynum</p>
	 *
	 * @param applynum
	 * 		applynum
	 */
	public void setApplynum(Integer applynum) {
		this.applynum = applynum;
	}
}
