package xyz.biandeshen.图灵学院.第三期第一节.entity;

import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

/**
 * @FileName: CustomerFilterType
 * @Author: admin
 * @Date: 2020/5/13 10:19
 * @Description: FilterType.CUSTOM 的自定义类型的实现
 * History:
 * <author>          <time>          <version>
 * admin           2020/5/13           版本号
 */
public class CustomerFilterType implements TypeFilter {
	
	@Override
	public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
		AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
		ClassMetadata classMetadata = metadataReader.getClassMetadata();
		Resource resource = metadataReader.getResource();
		//System.out.println("resource.getURL().getProtocol() = " + resource.getURL().getProtocol());
		return classMetadata.getClassName().endsWith("Bean");
	}
}