package xyz.biandeshen.图灵学院.第三期第一节.entity;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @FileName: CustomerImportSelector
 * @Author: admin
 * @Date: 2020/5/13 14:30
 * @Description: 测试 ImportSelector 接口方式导入
 * History:
 * <author>          <time>          <version>
 * admin           2020/5/13           版本号
 */
public class CustomerImportSelector implements ImportSelector {
	
	@Override
	public String[] selectImports(AnnotationMetadata annotationMetadata) {
		return new String[]{"xyz.biandeshen.图灵学院.第三期第一节.BeanMain"};
	}
}