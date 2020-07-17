package xyz.biandeshen.commonstests.controller;

import lombok.Data;
import xyz.biandeshen.commonstests.anno.FloatingPointCheck;
import java.math.BigDecimal;

@Data
public class InsurancePolicy {
	private String itemName;
	@FloatingPointCheck(max = "150", min = "0")
	private Integer age;
	@FloatingPointCheck(max = "999999.9999", min = "0.001", required = false)
	private BigDecimal value;
	
	
	@Override
	public String toString() {
		final StringBuilder sb;
		sb = new StringBuilder();
		sb.append('{');
		sb.append("\"itemName\":\"").append(itemName).append('\"');
		sb.append(",\"age\":").append(age);
		sb.append(",\"value\":").append(value);
		sb.append('}');
		return sb.toString();
	}
}