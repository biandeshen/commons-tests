package xyz.biandeshen.commonstests.config;

/**
 * @FileName: ResponseEntity
 * @Author: admin
 * @Date: 2020/4/2 14:03
 * @Description: 响应结果实体
 * History:
 * <author>          <time>          <version>
 * admin           2020/4/2           版本号
 */
@SuppressWarnings("ALL")
public enum CommonResponseEntity {//
	//验证规则： 1位字母加4为数字
	//S:对接组错误标识开头
	//W:仓储错误标识开头
	//编码	说明
	//S0001	未查询到该用户
	//S0003	MD5 不正确
	//S0004	未知异常
	//W0001	MD5生产异常
	//W0002	数据验证错误
	//W0003	保存数据异常
	S0000("0000", "成功", null), S0001("S0001", "未查询到该用户", null), S0003("S0003", "MD5不正确", null), S0004("S0004", "未知异常",
	                                                                                                  null), //  S0005
	// 参数校验异常
	S0005("S0005", "参数校验异常", null), //S0006 内部系统异常 特指调用仓储接口异常，如500，404等
	S0006("S0006", "内部系统错误", null), //S0007 查询客户标识异常
	S0007("S0007", "客户信息接口异常", null),
	S0008("S0008", "序列化反序列化异常", null),
	W0001("W0001", "MD5生产异常", null), W0002("W0002", "数据验证错误", null), W0003("W0003",
	                                                                                                         "保存数据异常",
	                                                                                                         null),
	;
	
	//code	status	响应码":	String	10	是
	//message	describe	响应信息	String	200	是
	//errDetail	body	错误明细	对象		否
	
	//errOrderNo	errorCode	错误商品编号	String	10	否
	//errMsg	errorMsg	错误信息	String	200	否
	
	private String code;
	
	private String message;
	
	private Object obj;
	
	CommonResponseEntity(String code, String message, Object obj) {
		this.code = code;
		this.message = message;
		this.obj = obj;
	}
	
	/**
	 * 获取
	 *
	 * @return code
	 */
	public String getCode() {
		return this.code;
	}
	
	
	/**
	 * 获取
	 *
	 * @return message
	 */
	public String getMessage() {
		return this.message;
	}
	
	/**
	 * 获取
	 *
	 * @return obj
	 */
	public Object getObj() {
		return this.obj;
	}}