package xyz.biandeshen.图灵学院.第三期第一节.anno;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @FileName: CustomerConditon
 * @Author: admin
 * @Date: 2020/5/13 10:58
 * @Description: 测试注解 Condition 的使用
 * History:
 * <author>          <time>          <version>
 * admin           2020/5/13           版本号
 */
public class CustomerConditon implements Condition {
	
	@Override
	public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
		return conditionContext.getBeanFactory().containsBean("person2");
	}
}