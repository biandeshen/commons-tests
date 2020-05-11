package xyz.biandeshen.代码;

/**
 * @author fjp
 * @Title: Constant
 * @ProjectName dj-fjp-benlaishenghuo
 * @Description: 常量值
 * @date 2018/10/1820:40
 */
@SuppressWarnings("all")
public class Constants {
	/**
	 * 宅急送站点类型
	 */
	public final static String SITE = "BOSCORP00000000A0001,BOSCORP00000000A0002,BOSCORP00000000A0003," + "BOSCORP00000000A0004,BOSCORP00000000A0006,BOSCORP00000000A0008,BOSCORP00000000A0009," + "BOSCORP00000000A0010,BOSCORP00000000A0011,BOSCORP00000000A0012,BOSCORP00000000A0013," + "BOSCORP00000000A0014,BOSCORP00000000A0015,BOSCORP00000000A0016,BOSCORP00000000A0017," + "BOSCORP00000000A0018,BOSCORP00000000A0019,BOSCORP00000000A0022,BOSCORP00000000A0023," + "BOSCORP00000000A0024,BOSCORP00000000A0025,BOSCORP00000000A0026,BOSCORP00000000A0027,BOSCORP00000000A0028";
	
	/**
	 * 宅急送中转站类型
	 */
	public final static String TRANSFER_STATION = "BOSCORP00000000A0005,BOSCORP00000000A0007,BOSCORP00000000A0020," + "BOSCORP00000000A0021";
	
	
	/**
	 * null Node
	 * nd = 0
	 */
	public final static String NODE_NULL = "0";
	
	/**
	 * 开单
	 */
	public final static String NODE_1 = "1";
	
	/**
	 * 入库
	 */
	public final static String NODE_2 = "2";
	
	/**
	 * 出库
	 */
	public final static String NODE_3 = "3";
	
	/**
	 * 派送
	 */
	public final static String NODE_4 = "4";
	
	/**
	 * 异常
	 */
	public final static String NODE_5 = "5";
	
	/**
	 * 签收
	 */
	public final static String NODE_6 = "6";
	
	
	//（以业务系统为准）
	/**
	 * 未签收 2231
	 */
	public final static String NON_SIGN = "2231";
	
	/**
	 * 正常签收 2232
	 */
	public final static String NORMAL_SIGN = "2232";
	
	/**
	 * 返货签收 2233
	 */
	public final static String RETURN_SIGN = "2233";
	
	/**
	 * 异常签收 2234
	 */
	public final static String EXCEPTION_SIGN = "2234";
	
	
	/**
	 * 2201:正常单
	 */
	public final static String NORMAL_ORDER = "2201";
	
	/**
	 * 2202:异字单
	 */
	public final static String DIFFERENT_ORDER = "2202";
	
	/**
	 * 2203:调字单
	 */
	public final static String ADJUST_ORDER = "2203";
	
	/**
	 * 2204:返货单
	 */
	public final static String RETURN_ORDER = "2204";
}
