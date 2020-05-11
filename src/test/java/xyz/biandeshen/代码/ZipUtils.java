package xyz.biandeshen.代码;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author fjp
 * @Title: ZipUtils
 * @ProjectName icp
 * @Description: 压缩工具类
 * @date 2018/11/2110:14
 */
@Component
public class ZipUtils {
	private static final Logger logger = LoggerFactory.getLogger(ZipUtils.class);
	private static final String ZIP = ".zip";
	private static final int BUFFER_SIZE = 2 * 1024;
	
	/**
	 * 文件过滤 父子目录
	 * 过滤压缩文件本身，否则目标路径与源路径相同或为子目录时，
	 * 会造成递归压缩，压缩文件本身包含压缩文件
	 */
	private FileFilter fileFilter;
	
	/**
	 * 压缩成ZIP
	 *
	 * @param srcFilePath
	 * 		压缩文件夹源路径
	 * @param destFilePath
	 * 		压缩文件输出路径
	 * @param zipFileName
	 * 		压缩文件名称
	 *
	 * @return 是否压缩
	 */
	public boolean compress(String srcFilePath, String destFilePath, String zipFileName, File[] prepareUploadFiles) throws IOException {
		long start = System.currentTimeMillis();
		File src = new File(srcFilePath);
		if (!src.exists()) {
			logger.warn("不存在此源路径: {} !", srcFilePath);
			return false;
		}
		//判断路径是否包含文件后缀为zip，如果是，则替换文件名为给定的文件
		File destFilePathFile = new File(destFilePath);
		if (!destFilePathFile.exists()) {
			logger.warn("不存在此源路径: {} !", destFilePath);
			return false;
		}
		
		File destFile;
		destFile = fileHandle(destFilePath, zipFileName);
		
		FileOutputStream fos;
		ZipOutputStream zos;
		fos = new FileOutputStream(destFile);
		zos = new ZipOutputStream(fos, StandardCharsets.UTF_8);
		
		//File sourceFile = new File(srcFilePath);
		
		//过滤压缩文件本身，否则目标路径与源路径相同或为子目录时，
		//会造成递归压缩，压缩文件本身包含压缩文件
		//fileFilter = new FileFilter() {
		//	@Override
		//	public boolean accept(File dir) {
		//		if (!Objects.equals(destFile.getPath(), dir.getPath()) && !dir.getName().endsWith(ZIP)) {
		//			return true;
		//		} else {
		//			return false;
		//		}
		//	}
		//};
		
		//执行文件压缩
		//compress(sourceFile, zos, sourceFile.getName(), keepDirStructure);
		//关闭压缩流，若不关闭，则解压时会产生不可预料的压缩文件末端
		compressFiles(zos, prepareUploadFiles);
		zos.close();
		long end = System.currentTimeMillis();
		logger.info("压缩" + destFile.getName() + "完成，耗时：" + (end - start) + " ms");
		
		return true;
	}
	
	
	/**
	 * 根据 源路径 目标路径 指定文件名 产生 目标File
	 * 对目标路径及文件名进行处理
	 *
	 * @param destFilePath
	 * 		目标路径
	 * @param zipFileName
	 * 		指定文件名
	 *
	 * @return 目标File
	 */
	private File fileHandle(String destFilePath, String zipFileName) {
		File destFile;
		if (destFilePath.endsWith(ZIP)) {
			int startIndex = destFilePath.lastIndexOf(File.separator);
			StringBuilder stringBuilder = new StringBuilder(destFilePath);
			stringBuilder.replace(startIndex, destFilePath.length(), File.separator + zipFileName);
			destFilePath = stringBuilder.toString();
			destFile = new File(destFilePath + ZIP);
		} else {
			if (destFilePath.endsWith(File.separator)) {
				destFile = new File(destFilePath.concat(zipFileName) + ZIP);
			} else if (!destFilePath.contains(".")) {
				destFile = new File(destFilePath.concat(File.separator + zipFileName) + ZIP);
			} else {
				String[] split = destFilePath.split(File.separator + ".");
				destFilePath = split[0];
				int startIndex = destFilePath.lastIndexOf(File.separator);
				StringBuilder stringBuilder;
				stringBuilder = new StringBuilder(destFilePath);
				if (zipFileName.endsWith(ZIP)) {
					stringBuilder.replace(startIndex, destFilePath.length(), File.separator + zipFileName);
					destFilePath = stringBuilder.toString();
					destFile = new File(destFilePath);
				} else {
					stringBuilder.replace(startIndex, destFilePath.length(), File.separator + zipFileName);
					stringBuilder.append(ZIP);
					destFilePath = stringBuilder.toString();
					destFile = new File(destFilePath);
				}
			}
		}
		//if (!destFile.exists()) {
		//	logger.warn("不存在此源路径: {} !", destFilePath);
		//}
		return destFile;
	}
	
	
	/**
	 * 递归压缩方法
	 *
	 * @param sourceFile
	 * 		源文件
	 * @param zos
	 * 		zip输出流
	 * @param name
	 * 		压缩后的名称
	 * @param keepDirStructure
	 * 		是否保留原来的目录结构,true:保留目录结构; 压缩文件中存在父目录
	 * 		false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败);无父目录
	 * @param prepareUploadFiles
	 * 		输入的文件列表
	 *
	 * @throws Exception
	 */
	//private void compressFiles(File sourceFile, ZipOutputStream zos, String name, boolean keepDirStructure, File[] prepareUploadFiles) throws Exception {
	//	for (File file : prepareUploadFiles) {
	//
	//	}
	//
	//
	//}
	
	/**
	 * 递归压缩方法
	 *
	 * @param zipOutputStream
	 * 		压缩文件流
	 * @param prepareUploadFiles
	 * 		准备压缩的文件列表
	 */
	public void compressFiles(ZipOutputStream zipOutputStream, File[] prepareUploadFiles) throws IOException {
		
		// 实例化 FileOutputStream 对象
		// 实例化 ZipOutputStream 对象
		// 创建 ZipEntry 对象
		ZipEntry zipEntry;
		// 遍历源文件数组
		if (prepareUploadFiles.length > 0) {
			for (File prepareUploadFile : prepareUploadFiles) {
				// 将源文件数组中的当前文件读入 FileInputStream 流中
				try (FileInputStream fileInputStream = new FileInputStream(prepareUploadFile)) {
					// 实例化 ZipEntry 对象，源文件数组中的当前文件
					zipEntry = new ZipEntry(prepareUploadFile.getName());
					zipOutputStream.putNextEntry(zipEntry);
					// 该变量记录每次真正读的字节个数
					int len;
					// 定义每次读取的字节数组
					byte[] buffer = new byte[1024];
					while ((len = fileInputStream.read(buffer)) > 0) {
						zipOutputStream.write(buffer, 0, len);
					}
				}
			}
		}
		zipOutputStream.closeEntry();
		
	}
	
	
	/**
	 * 递归压缩方法
	 *
	 * @param sourceFile
	 * 		源文件
	 * @param zos
	 * 		zip输出流
	 * @param name
	 * 		压缩后的名称
	 * @param keepDirStructure
	 * 		是否保留原来的目录结构,true:保留目录结构; 压缩文件中存在父目录
	 * 		false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败);无父目录
	 *
	 * @throws Exception
	 */
	private void compress(File sourceFile, ZipOutputStream zos, String name, boolean keepDirStructure) throws Exception {
		//文件
		if (sourceFile.isFile()) {
			// 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
			byte[] buf = new byte[BUFFER_SIZE];
			// copy文件到zip输出流中
			zos.putNextEntry(new ZipEntry(name));
			int len;
			try (FileInputStream in = new FileInputStream(sourceFile)) {
				while ((len = in.read(buf)) != -1) {
					zos.write(buf, 0, len);
				}
				// Complete the entry
				zos.closeEntry();
			}
			
		}
		//目录
		File[] listFiles = sourceFile.listFiles(fileFilter);
		
		//空目录
		if (listFiles == null || listFiles.length == 0) {
			// 需要保留原来的文件结构时,需要对空文件夹进行处理
			if (keepDirStructure) {
				// 空文件夹的处理
				zos.putNextEntry(new ZipEntry(name + "/"));
				// 没有文件，不需要文件的copy
				zos.closeEntry();
			}
		} else {
			//非空目录
			for (File file : listFiles) {
				// 判断是否需要保留原来的文件结构
				if (keepDirStructure) {
					// 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
					// 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
					compress(file, zos, name + File.separator + file.getName(), keepDirStructure);
				} else {
					compress(file, zos, file.getName(), keepDirStructure);
				}
			}
		}
	}
	
	//public static void main(String[] args) {
	//	//测试压缩方法1
	//	boolean bool = ZipUtils.compress("E:\\OneDrive\\", "E:\\OneDrive\\test", "压缩文件包1", false);
	//	System.out.println(bool);
	//}
}