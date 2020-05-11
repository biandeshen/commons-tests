package xyz.biandeshen.代码;

import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


/**
 * @author yjc
 * @Title: ExcelUtil
 * @Description: Excel工具类
 * @date 2019/4/19 9:04
 */
public class ExcelUtil {
	
	
	//public static void main(String[] args) {
	//	List<String[]> strings = ExcelUtil.read("E:\\工作\\广州农商银行2019-4-19\\广州农商行信用卡快速发卡邮寄清单20181105.xls");
	//	for (String[] string : strings) {
	//		for (String s : string) {
	//			System.out.print(s + "\t");
	//		}
	//		System.out.println("\n");
	//	}
	//}
	
	/**
	 * 通过Excel文件路径,获取文件内容,返回List<String []>
	 *
	 * @param filename
	 *
	 * @return
	 */
	public static List<String[]> read(String filename) {
		List<String[]> liststrs = new LinkedList<>();
		// String file_dir = "D:\\guangfaorder\\广发银行_171211_金邦达to宅急送.xlsx";
		Workbook book;
		book = getExcelWorkbook(filename);
		if (Objects.nonNull(book)) {
			Sheet sheet;
			sheet = getSheetByNum(book, 0);
			if (Objects.nonNull(sheet)) {
				int lastRowNum = sheet.getLastRowNum();
				for (int i = 0; i <= lastRowNum; i++) {
					Row row;
					row = sheet.getRow(i);
					if (row != null) {
						int lastCellNum = row.getLastCellNum();
						if (lastCellNum < 0) {
							liststrs.add(new String[0]);
							continue;
						}
						Cell cell;
						String[] strs = new String[lastCellNum];
						for (int j = 0; j <= lastCellNum; j++) {
							cell = row.getCell(j);
							if (cell != null) {
								String cellValue;
								try {
									cellValue = cell.getStringCellValue();
								} catch (IllegalStateException e) {
									cellValue = cell.getNumericCellValue() + "";
								}
								strs[j] = cellValue;
							}
						}
						liststrs.add(strs);
					}
				}
			}
		}
		return liststrs;
	}
	
	
	public static Sheet getSheetByNum(Workbook book, int number) {
		Sheet sheet;
		try {
			sheet = book.getSheetAt(number);
		} catch (Exception e) {
			throw new RuntimeException("根据编号: " + number + ",获取Workbook: " + book + "失败,异常原因: " + e.getMessage());
		}
		return sheet;
	}
	
	public static Workbook getExcelWorkbook(String filePath) {
		Workbook book;
		File file;
		try {
			file = new File(filePath);
			if (!file.exists()) {
				throw new RuntimeException("文件不存在,文件路径为: " + filePath);
			} else {
				try (FileInputStream fis = new FileInputStream(file)) {
					book = WorkbookFactory.create(fis);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return book;
	}
	
}
