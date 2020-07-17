package xyz.biandeshen.commonstests.anno;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * @FileName: BigDecimalValidator
 * @Author: fjp
 * @Date: 2020/6/10 9:57
 * @Description: 校验 bigdecimal 或 浮点数 格式的数据
 * History:
 * <author>          <time>          <version>
 * fjp           2020/6/10           0.0.1
 */
public class BigDecimalValidator implements ConstraintValidator<FloatingPointCheck, Object> {
	
	/**
	 * 默认不允许为空，需要校验
	 */
	private boolean required = true;
	
	/**
	 * 默认 值 包含0
	 */
	private boolean includeZero = true;
	
	/**
	 * 默认保留小数位数
	 */
	private int scale = 2;
	
	/**
	 * 默认最大值 空 不限制
	 */
	private String max;
	
	/**
	 * 默认最小值 空 不限制
	 */
	private String min;
	
	/**
	 * scale 位小数 Becimal 校验
	 */
	private boolean judgeScaleDecimal(Object obj) {
		// 是否允许为空 默认允许为空
		if (obj == null) {
			// 此时无需向下执行, object 无值
			return !required;
		}
		
		// 是否包含0
		if (!includeZero) {
			// 不包含 0
			String zeroRegex = "^([0]?([.]?[0]+[1,9]))$";
			Pattern pattern = Pattern.compile(zeroRegex);
			// 匹配成功 则代表值为 0
			if (pattern.matcher(obj.toString()).matches()) {
				return false;
			}
			// 未匹配到 则代表值不为 0	即不包含0 继续执行
		}
		
		// 此正则表达式表示指定小数位数的格式校验
		String regex = "^[+]?([0-9]+(.[0-9]{0," + scale + "})?)$";
		Pattern pattern = Pattern.compile(regex);
		// 若匹配成功，则代表参数格式正常  否则参数格式异常
		if (!pattern.matcher(obj.toString()).matches()) {
			return false;
		}
		
		// 取值范围的校验
		// 校验最大值
		if (!StringUtils.isEmpty(max)) {
			BigDecimal maxValue = BigDecimal.valueOf(Double.valueOf(max));
			if (obj instanceof Number) {
				// 当前值大于最大值
				if (BigDecimal.valueOf(Double.valueOf(obj.toString())).compareTo(maxValue) > 0) {
					return false;
				}
			}
			if (obj instanceof String) {
				// 当前值大于最大值
				if (BigDecimal.valueOf(Double.valueOf(obj.toString())).compareTo(maxValue) > 0) {
					return false;
				}
			}
		}
		// 校验最小值
		if (!StringUtils.isEmpty(min)) {
			BigDecimal minValue = BigDecimal.valueOf(Double.valueOf(min));
			if (obj instanceof Number) {
				// 当前值小于最小值
				if (BigDecimal.valueOf(Double.valueOf(obj.toString())).compareTo(minValue) < 0) {
					return false;
				}
			}
			if (obj instanceof String) {
				// 当前值小于最小值
				if (BigDecimal.valueOf(Double.valueOf(obj.toString())).compareTo(minValue) < 0) {
					return false;
				}
			}
			
		}
		return true;
	}
	
	@Override
	public void initialize(FloatingPointCheck floatingPointCheck) {
		required = floatingPointCheck.required();
		includeZero = floatingPointCheck.includeZero();
		scale = floatingPointCheck.scale();
		max = floatingPointCheck.max();
		min = floatingPointCheck.min();
		//	判断最大值是否小数，若为小数，计算小数位数>2时替换scale
		String[] splitPointMax = max.split(Pattern.quote("."));
		if (splitPointMax.length > 1) {
			String pointRight = splitPointMax[splitPointMax.length - 1];
			if (!pointRight.isEmpty()) {
				char[] pointRightChars = pointRight.toCharArray();
				if (pointRightChars.length > scale) {
					scale = pointRightChars.length;
				}
			}
		}
		//	判断最小值是否小数，若为小数，计算小数位数>2时替换scale
		String[] splitPointMin = min.split(Pattern.quote("."));
		if (splitPointMin.length > 1) {
			String pointRight = splitPointMin[splitPointMin.length - 1];
			if (!pointRight.isEmpty()) {
				char[] pointRightChars = pointRight.toCharArray();
				if (pointRightChars.length > scale) {
					scale = pointRightChars.length;
				}
			}
		}
	}
	
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		boolean judgeScaleDecimal;
		try {
			judgeScaleDecimal = judgeScaleDecimal(value);
			// 校验结果
			return judgeScaleDecimal;
		} catch (Exception e) {
			// 即校验过程中存在异常
			//throw new GlobalException(null, CommonResponseEntity.S0006.getMessage(), e);
			throw new ValidationException(e);
			//return false;
		}
	}
}