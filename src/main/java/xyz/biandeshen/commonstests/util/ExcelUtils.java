package xyz.biandeshen.commonstests.util;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Map;
import java.util.function.Function;

/**
 * @author fjp
 * @Title: ExcelUtils
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/8/2215:08
 */
public class ExcelUtils implements Function<Row[], Workbook> {
	
	///**输出Excel*/
	//
	//public static void writeExcel(OutputStream os)
	//
	//{
	//
	//	try
	//
	//	{
	//
	//		/** 只能通过API提供的 工厂方法来创建Workbook，而不能使用WritableWorkbook的构造函数，因为类WritableWorkbook的构造函数为 protected类型：方法一：直接从目标文件中读取 WritableWorkbook wwb = Workbook.createWorkbook(new File(targetfile));方法 二：如下实例所示 将WritableWorkbook直接写入到输出流*/
	//
	//		WritableWorkbook wwb = Workbook.createWorkbook(os);
	//
	//		//创建Excel工作表 指定名称和位置
	//
	//		WritableSheet ws = wwb.createSheet("Test Sheet 1",0);
	//
	//		/**************往工作表中添加数据*****************/
	//
	//		//1.添加Label对象
	//
	//		Label label = new Label(0,0,"测试");
	//
	//		ws.addCell(label);
	//
	//		//添加带有字型Formatting对象
	//
	//		WritableFont wf = new WritableFont(WritableFont.TIMES,18,WritableFont.BOLD,true);
	//
	//		WritableCellFormat wcf = new WritableCellFormat(wf);
	//
	//		Label labelcf = new Label(1,0,"this is a label test",wcf);
	//
	//		ws.addCell(labelcf);
	//
	//		//添加带有字体颜色的Formatting对象
	//
	//		WritableFont wfc = new WritableFont(WritableFont.ARIAL,10,WritableFont.NO_BOLD,false,
	//
	//				UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.DARK_YELLOW);
	//
	//		WritableCellFormat wcfFC = new WritableCellFormat(wfc);
	//
	//		Label labelCF = new Label(1,0,"Ok",wcfFC);
	//
	//		ws.addCell(labelCF);
	//
	//
	//
	//		//2.添加Number对象
	//
	//		Number labelN = new Number(0,1,3.1415926);
	//
	//		ws.addCell(labelN);
	//
	//		//添加带有formatting的Number对象
	//
	//		NumberFormat nf = new NumberFormat("#.##");
	//
	//		WritableCellFormat wcfN = new WritableCellFormat(nf);
	//
	//		Number labelNF = new jxl.write.Number(1,1,3.1415926,wcfN);
	//
	//		ws.addCell(labelNF);
	//
	//
	//
	//		//3.添加Boolean对象
	//
	//		Boolean labelB = new jxl.write.Boolean(0,2,true);
	//
	//		ws.addCell(labelB);
	//
	//		Boolean labelB1 = new jxl.write.Boolean(1,2,false);
	//
	//		ws.addCell(labelB1);
	//
	//		//4.添加DateTime对象
	//
	//		jxl.write.DateTime labelDT = new jxl.write.DateTime(0,3,new java.util.Date());
	//
	//		ws.addCell(labelDT);
	//
	//
	//
	//		//5.添加带有formatting的DateFormat对象
	//
	//		DateFormat df = new DateFormat("dd MM yyyy hh:mm:ss");
	//
	//		WritableCellFormat wcfDF = new WritableCellFormat(df);
	//
	//		DateTime labelDTF = new DateTime(1,3,new java.util.Date(),wcfDF);
	//
	//		ws.addCell(labelDTF);
	//
	//		//6.添加图片对象,jxl只支持png格式图片
	//
	//		File image = new File("f:\\1.png");
	//
	//		WritableImage wimage = new WritableImage(0,4,6,17,image);
	//
	//		ws.addImage(wimage);
	//
	//		//7.写入工作表
	//
	//		wwb.write();
	//
	//		wwb.close();
	//
	//	}
	//
	//	catch(Exception e)
	//
	//	{
	//
	//		e.printStackTrace();
	//
	//	}
	//
	//}
	//
	///** 将file1拷贝后,进行修改并创建输出对象file2
	//
	// * 单元格原有的格式化修饰不能去掉，但仍可将新的单元格修饰加上去，
	//
	// * 以使单元格的内容以不同的形式表现
	//
	// */
	//
	//public static void modifyExcel(File file1,File file2)
	//
	//{
	//
	//	try
	//
	//	{
	//
	//		Workbook rwb = Workbook.getWorkbook(file1);
	//
	//		WritableWorkbook wwb = Workbook.createWorkbook(file2,rwb);//copy
	//
	//		WritableFont wfc = new WritableFont(WritableFont.ARIAL,10,WritableFont.NO_BOLD,false,
	//
	//				UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLUE);
	//
	//		WritableCellFormat wcfFC = new WritableCellFormat(wfc);
	//
	//		WritableSheet ws = wwb.getSheet(0);
	//
	//		WritableCell wc = ws.getWritableCell(0,0);
	//
	//		//判断单元格的类型,做出相应的转换
	//
	//		if(wc.getType() == CellType.LABEL)
	//
	//		{
	//
	//			Label labelCF =new Label(0,0,"人物（新）",wcfFC);
	//
	//			ws.addCell(labelCF);
	//
	//			//Label label = (Label)wc;
	//
	//			//label.setString("被修改");
	//
	//		}
	//
	//		wwb.write();
	//
	//		wwb.close();
	//
	//		rwb.close();
	//
	//	}
	//
	//	catch(Exception e)
	//
	//	{
	//
	//		e.printStackTrace();
	//
	//	}
	//
	//}
	
	/**
	 * 创建一个带有多个sheet的Excel工作簿
	 *
	 * @param sheetNames
	 * 		sheet 名称 列表
	 *
	 * @return Excel 工作簿 Workbook
	 */
	public static Workbook getNewWorkBookWithSheets(String... sheetNames) {
		Workbook wb = new XSSFWorkbook();
		for (int i = 0; i < sheetNames.length; i++) {
			wb.createSheet(sheetNames[i]);
			wb.setSheetOrder(sheetNames[i], i);
		}
		return wb;
	}
	
	/**
	 * 创建一个带有多个sheet的Excel工作簿
	 *
	 * @param sortReflectSheetNamesMap
	 * 		key 为 sheet 的 索引, value 为 sheet 的 名称
	 *
	 * @return Excel 工作簿 Workbook
	 */
	public static Workbook getNewWorkBookWithSheets(Map<Integer, String> sortReflectSheetNamesMap) {
		Workbook wb = new XSSFWorkbook();
		for (Integer integer : sortReflectSheetNamesMap.keySet()) {
			wb.createSheet(sortReflectSheetNamesMap.get(integer));
			wb.setSheetOrder(sortReflectSheetNamesMap.get(integer), integer);
		}
		return wb;
	}
	
	///**
	// * 创建一个带有多个sheet的Excel工作簿
	// *
	// * @param sortReflectSheetNamesMap
	// * 		key 为 sheet 的 索引, value 为 sheet 的 名称
	// *
	// * @return Excel 工作簿 Workbook
	// */
	//public static Workbook getNewWorkBookWithSheets(Workbook workbook) {
	//	Workbook wb = new XSSFWorkbook();
	//	for (Integer integer : sortReflectSheetNamesMap.keySet()) {
	//		wb.createSheet(sortReflectSheetNamesMap.get(integer));
	//		wb.setSheetOrder(sortReflectSheetNamesMap.get(integer), integer);
	//	}
	//	return wb;
	//}
	
	
	/**
	 * Applies this function to the given argument.
	 *
	 * @param rows
	 * 		the function argument
	 *
	 * @return the function result
	 */
	@Override
	public Workbook apply(Row[] rows) {
		return null;
	}
	
	/**
	 * Returns a composed function that first applies the {@code before}
	 * function to its input, and then applies this function to the result.
	 * If evaluation of either function throws an exception, it is relayed to
	 * the caller of the composed function.
	 *
	 * @param before
	 * 		the function to apply before this function is applied
	 *
	 * @return a composed function that first applies the {@code before}
	 * function and then applies this function
	 *
	 * @throws NullPointerException
	 * 		if before is null
	 * @see #andThen(Function)
	 */
	@Override
	public <V> Function<V, Workbook> compose(Function<? super V, ? extends Row[]> before) {
		return null;
	}
	
	/**
	 * Returns a composed function that first applies this function to
	 * its input, and then applies the {@code after} function to the result.
	 * If evaluation of either function throws an exception, it is relayed to
	 * the caller of the composed function.
	 *
	 * @param after
	 * 		the function to apply after this function is applied
	 *
	 * @return a composed function that first applies this function and then
	 * applies the {@code after} function
	 *
	 * @throws NullPointerException
	 * 		if after is null
	 * @see #compose(Function)
	 */
	@Override
	public <V> Function<Row[], V> andThen(Function<? super Workbook, ? extends V> after) {
		return null;
	}
}
