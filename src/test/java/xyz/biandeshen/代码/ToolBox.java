package xyz.biandeshen.代码;

import java.text.NumberFormat;

/**
 * @author fjp
 * @Title: sdf
 * @ProjectName dj-fjp-benlaishenghuo
 * @Description: 单位转换, 保留小数
 * @date 2019/6/2411:16
 */
public class ToolBox {
	
	public static double putGToKG(int g) {
		return g / 1000d;
	}
	
	public static double putGToKG(double g) {
		return g / 1000d;
	}
	
	public static String putTwoDecimalPlaces(double d) {
		NumberFormat nf = NumberFormat.getNumberInstance();
		// 保留两位小数
		nf.setMaximumFractionDigits(2);
		//// 如果不需要四舍五入，可以使用RoundingMode.DOWN
		//nf.setRoundingMode(RoundingMode.UP);
		return nf.format(d);
	}
	
	
}
