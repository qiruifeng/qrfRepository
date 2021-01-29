package until;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import service.solution.Solution;
//import service.solution.SolutionSet;
//import service.variable.common.Variable;

import java.io.*;
import java.util.*;

public class FileOperatorUtil {
	/**
	 * 保存非劣解集进excel
	 *
	 * @param pop
	 * @param sheetName
	 *            sheet名称
	 * @param tableHead
	 *            表头名称
	 * @param tableHeadDimension
	 *            表头单位
	 * @return
	 */
//	@SuppressWarnings("deprecation")
//	public static boolean exportExcel(SolutionSet pop, String sheetName, ArrayList<String> tableHead,
//			ArrayList<String> tableHeadDimension, String excelPath) {
//
//		File file = new File(excelPath);
//		File parentFile = file.getParentFile();
//		if (!parentFile.exists()) {
//			parentFile.mkdirs();
//		}
//
//		try {
//			file.createNewFile();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//
//		boolean succ = true;
//		// 声明一个工作簿,默认保存为低版本的
//		HSSFWorkbook wb = new HSSFWorkbook();
//		// 声明一个sheet
//		HSSFSheet sheet = wb.createSheet(sheetName);
//		// 设置sheet名称长度
//		sheet.setDefaultColumnWidth((short) 15);
//		// 生成一个样式
//		HSSFCellStyle style = wb.createCellStyle();
//		// 设置居中
//		style.setAlignment(HorizontalAlignment.CENTER);
//		// 创建表头
//		HSSFRow row = sheet.createRow(0);// 表头名称
//		HSSFRow rowD = sheet.createRow(1);// 表头单位
//
//		// 为表头赋值
//		// HSSFCell cell=row.createCell((short)0);
//		for (int i = 0; i < tableHead.size(); i++) {
//			row.createCell(i).setCellValue(tableHead.get(i));
//			row.getCell(i).setCellStyle(style);
//			rowD.createCell(i).setCellValue(tableHeadDimension.get(i));
//			rowD.getCell(i).setCellStyle(style);
//
//		}
//
//		// 向表格里填充数据
//		for (int i = 0; i < pop.size(); i++) {
//			row = sheet.createRow(i + 2);
//
//			Solution chrom = pop.getSolution(i);
//			// 添加决策变量
//			Variable[] vars = chrom.getDecisionVariables().getVariables();
//
//			for (int j = 0; j < vars.length; j++) {
//				// 写值
//				row.createCell(j).setCellValue(vars[j].getValue());
//				// 改样式
//				row.getCell(j).setCellStyle(style);
//			}
//
//			int curIndex = vars.length;
//
//			// 添加函数值
//			double[] objVals = chrom.getObjVals();
//			for (int j = 0; j < objVals.length; j++) {
//				// 写值
//				row.createCell(curIndex + j).setCellValue(objVals[j]);
//				// 改样式
//				row.getCell(curIndex + j).setCellStyle(style);
//			}
//			curIndex = curIndex + objVals.length;
//
//			// 添加违反约束的值
//			double overCons = chrom.getOverallConsVal();
//			// 写值
//			row.createCell(curIndex).setCellValue(overCons);
//			// 改样式
//			row.getCell(curIndex).setCellStyle(style);
//			curIndex = curIndex + 1;
//
//			// 添加违反约束数量
//			double overConsNum = chrom.getViolatedConsNum();
//			// 写值
//			row.createCell(curIndex).setCellValue(overConsNum);
//			// 改样式
//			row.getCell(curIndex).setCellStyle(style);
//			curIndex = curIndex + 1;
//
//			// 添加等级
//			int rank = chrom.getRank();
//			// 写值
//			row.createCell(curIndex).setCellValue(rank);
//			// 改样式
//			row.getCell(curIndex).setCellStyle(style);
//			curIndex = curIndex + 1;
//
//			// 添加拥挤距离
//			double dis = chrom.getCrowdingDis();
//			// 写值
//			row.createCell(curIndex).setCellValue(dis);
//			// 改样式
//			row.getCell(curIndex).setCellStyle(style);
//
//		}
//
//		FileOutputStream out;
//		try {
//			out = new FileOutputStream(excelPath);
//			wb.write(out);
//			out.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			succ = false;
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			succ = false;
//			e.printStackTrace();
//		}
//
//		return succ;
//	}

	/**
	 * 导出计算过程到excel
	 * @param data
	 * @param sheetName
	 * @param tableHead
	 * @param tableHeadDimension
	 * @param excelPath
	 * @param start
	 * @return
	 */
	public static boolean exportProcessToExcel(List<double[]> data, String sheetName,String partName, ArrayList<String> tableHead,
			ArrayList<String> tableHeadDimension, String excelPath,int start,int end){
		File file = new File(excelPath);
		File parentFile = file.getParentFile();
		if(!file.exists()){
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}
			try {
				file.createNewFile();
				HSSFWorkbook wb = new HSSFWorkbook();
				FileOutputStream out = new FileOutputStream(excelPath);
				wb.write(out);
				out.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		InputStream ips;
		HSSFWorkbook wb=null;
		HSSFSheet sheet=null;
		try {
			ips = new FileInputStream(excelPath);
			wb= new HSSFWorkbook(ips);
			sheet= wb.getSheet(sheetName);
			if(sheet==null){
				sheet=wb.createSheet(sheetName);
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		// 声明一个工作簿,默认保存为低版本的
		//HSSFWorkbook wb = new HSSFWorkbook();
		// 声明一个sheet
		//HSSFSheet sheet = wb.createSheet(sheetName);
		// 设置sheet名称长度
		sheet.setDefaultColumnWidth((short) 15);
		// 生成一个样式
		HSSFCellStyle style = wb.createCellStyle();
		// 设置居中
		style.setAlignment(HorizontalAlignment.CENTER);

		HSSFCell partCell=sheet.createRow(start).createCell(0);
		partCell.setCellValue(partName);
		partCell.setCellStyle(style);

		// 创建表头
		HSSFRow row = sheet.createRow(start+1);// 表头名称
		HSSFRow rowD = sheet.createRow(start+2);// 表头单位

		// 为表头赋值
		for (int i = 0; i < tableHead.size(); i++) {
			row.createCell(i).setCellValue(tableHead.get(i));
			row.getCell(i).setCellStyle(style);
			rowD.createCell(i).setCellValue(tableHeadDimension.get(i));
			rowD.getCell(i).setCellStyle(style);

		}




		for(int j=0;j<data.size();j++){
			double[] da=data.get(j);
			for(int i=0;i<da.length;i++){
				if(sheet.getRow(start+3+i)==null){
					sheet.createRow(start+3+i);
				}

				HSSFCell cell=sheet.getRow(start+3+i).createCell(j);
				cell.setCellStyle(style);
				cell.setCellValue(da[i]);
			}

		}

		boolean succ;
		FileOutputStream out;
		try {
			out = new FileOutputStream(excelPath);
			wb.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			succ = false;
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			succ = false;
			e.printStackTrace();
		}

		return true;
	}
	/**
	 * 导出决策变量到excel
	 * @param data
	 * @param sheetName
	 * @param excelPath
	 * @return
	 */
	public static boolean exportVariableValueToExcel(double[][] data, String sheetName, String excelPath){
		File file = new File(excelPath);
		File parentFile = file.getParentFile();
		if(!file.exists()){
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}
			try {
				file.createNewFile();
				HSSFWorkbook wb = new HSSFWorkbook();
				FileOutputStream out = new FileOutputStream(excelPath);
				wb.write(out);
				out.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		InputStream ips;
		HSSFWorkbook wb=null;
		HSSFSheet sheet=null;
		try {
			ips = new FileInputStream(excelPath);
			wb= new HSSFWorkbook(ips);
			sheet= wb.getSheet(sheetName);
			if(sheet==null){
				sheet=wb.createSheet(sheetName);
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		// 声明一个工作簿,默认保存为低版本的
		//HSSFWorkbook wb = new HSSFWorkbook();
		// 声明一个sheet
		//HSSFSheet sheet = wb.createSheet(sheetName);
		// 设置sheet名称长度
		sheet.setDefaultColumnWidth((short) 15);
		// 生成一个样式
		HSSFCellStyle style = wb.createCellStyle();
		// 设置居中
		style.setAlignment(HorizontalAlignment.CENTER);

		int rowNum=data.length;
		int columnNum=data[0].length;
		for(int i=0;i<rowNum;i++){
			HSSFRow row = sheet.createRow(i);
			for(int j=0;j<columnNum;j++){
				HSSFCell cell=row.createCell(j);
				cell.setCellStyle(style);
				cell.setCellValue(data[i][j]);
			}
		}

		boolean succ;
		FileOutputStream out;
		try {
			out = new FileOutputStream(excelPath);
			wb.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			succ = false;
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			succ = false;
			e.printStackTrace();
		}

		return true;
	}



	/**
	 * 读取xls文件 通过对单元格遍历的形式来获取信息 精确判断单元格类型来取值
	 * @param path 文件地址
	 * @param rowIndex 行id，可以是离散的
	 * @param columnIndex 列id,可以是离散的
	 * @param sheetIndex 片id，从1开始计数
	 * @return
	 */
	public static LinkedHashMap<Integer, LinkedHashMap<Integer, Object>> readTableForXLS(String path, int[] rowIndex,
			int[] columnIndex,int sheetIndex) {
		LinkedHashMap<Integer, LinkedHashMap<Integer, Object>> result = new LinkedHashMap<>();
		try {
			InputStream ips = new FileInputStream(path);
			HSSFWorkbook wb = new HSSFWorkbook(ips);
			HSSFSheet sheet = wb.getSheetAt(sheetIndex);

			for (int i = 0; i < rowIndex.length; i++) {
				LinkedHashMap<Integer, Object> cellMap = new LinkedHashMap<>();
				for (int j = 0; j < columnIndex.length; j++) {
					HSSFCell cell = sheet.getRow(rowIndex[i] - 1).getCell(columnIndex[j] - 1);

					int ind = j;
					CellType type = cell.getCellTypeEnum();
					switch (type) {
						// 读取boolean类型
						case BOOLEAN:
							cellMap.put(ind, cell.getBooleanCellValue());
							break;
						case NUMERIC:
							if (HSSFDateUtil.isCellDateFormatted(cell)) {
								// 读取日期类型
								cellMap.put(ind, cell.getDateCellValue());
							} else {
								// 读取数字类型
								cellMap.put(ind, cell.getNumericCellValue());
							}
							break;
						// 读取公式
						case FORMULA:
							cellMap.put(ind, cell.getCellFormula());
							break;
						default:
							cellMap.put(ind, cell.getStringCellValue());
							break;
					}
				}
				result.put(i , cellMap);
			}
			ips.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 读取xls文件 通过对单元格遍历的形式来获取信息 精确判断单元格类型来取值
	 * @param path 文件地址
	 * @param rowIndex 行id,可以是离散的
	 * @param columnIndex 列id,可以是离散的
	 * @param sheetName 片名称
	 * @return
	 */
	public static LinkedHashMap<Integer, LinkedHashMap<Integer, Object>> readTableForXLS(String path, int[] rowIndex,
			int[] columnIndex,String sheetName) {
		LinkedHashMap<Integer, LinkedHashMap<Integer, Object>> result = new LinkedHashMap<>();
		try {
			InputStream ips = new FileInputStream(path);
			HSSFWorkbook wb = new HSSFWorkbook(ips);
			HSSFSheet sheet = wb.getSheet(sheetName);

			for (int i = 0; i < rowIndex.length; i++) {
				LinkedHashMap<Integer, Object> cellMap = new LinkedHashMap<>();
				for (int j = 0; j < columnIndex.length; j++) {
					HSSFCell cell = sheet.getRow(rowIndex[i] - 1).getCell(columnIndex[j] - 1);

					int ind = j;
					CellType type = cell.getCellTypeEnum();
					switch (type) {
						// 读取boolean类型
						case BOOLEAN:
							cellMap.put(ind, cell.getBooleanCellValue());
							break;
						case NUMERIC:
							if (HSSFDateUtil.isCellDateFormatted(cell)) {
								// 读取日期类型
								cellMap.put(ind, cell.getDateCellValue());
							} else {
								// 读取数字类型
								cellMap.put(ind, cell.getNumericCellValue());
							}
							break;
						// 读取公式
						case FORMULA:
							cellMap.put(ind, cell.getCellFormula());
							break;
						default:
							cellMap.put(ind, cell.getStringCellValue());
							break;
					}
				}
				result.put(i , cellMap);
			}
			ips.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 读取xlsx文件
	 * @param path
	 * @param rowIndex
	 * @param columnIndex
	 * @param sheetName
	 * @return
	 */
	public static LinkedHashMap<Integer, LinkedHashMap<Integer, Object>> readTableForXLSX(String path, int[] rowIndex,
			int[] columnIndex,String sheetName) {
		LinkedHashMap<Integer, LinkedHashMap<Integer, Object>> result = new LinkedHashMap<>();
		try {
			InputStream ips = new FileInputStream(path);
			XSSFWorkbook wb = new XSSFWorkbook(ips);
			XSSFSheet sheet = wb.getSheet(sheetName);

			for(int i=0;i<rowIndex.length;i++){
				LinkedHashMap<Integer, Object> cellMap = new LinkedHashMap<>();
				for(int j=0;j<columnIndex.length;j++){
					XSSFCell cell=sheet.getRow(rowIndex[i] - 1).getCell(columnIndex[j] - 1);

					int ind=j;
					CellType type = cell.getCellTypeEnum();
					switch (type) {
					// 读取boolean类型
						case BOOLEAN:
							cellMap.put(ind, cell.getBooleanCellValue());
							break;
						case NUMERIC:
							if (HSSFDateUtil.isCellDateFormatted(cell)) {
								// 读取日期类型
								cellMap.put(ind, cell.getDateCellValue());
							} else {
								// 读取数字类型
								cellMap.put(ind, cell.getNumericCellValue());
							}
							break;
						// 读取公式
						case FORMULA:
							cellMap.put(ind, cell.getCellFormula());
							break;
						default:
							cellMap.put(ind, cell.getStringCellValue());
							break;
					}
				}
				result.put(i, cellMap);
			}
			ips.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 读取xlsx文件
	 * @param path
	 * @param rowIndex
	 * @param columnIndex
	 * @param sheetIndex
	 * @return
	 */
	public static LinkedHashMap<Integer, LinkedHashMap<Integer, Object>> readTableForXLSX(String path, int[] rowIndex,
			int[] columnIndex,int sheetIndex) {
		LinkedHashMap<Integer, LinkedHashMap<Integer, Object>> result = new LinkedHashMap<>();
		try {
			InputStream ips = new FileInputStream(path);
			XSSFWorkbook wb = new XSSFWorkbook(ips);
			XSSFSheet sheet = wb.getSheetAt(sheetIndex);

			for(int i=0;i<rowIndex.length;i++){
				LinkedHashMap<Integer, Object> cellMap = new LinkedHashMap<>();
				for(int j=0;j<columnIndex.length;j++){
					XSSFCell cell=sheet.getRow(rowIndex[i] - 1).getCell(columnIndex[j] - 1);

					int ind=j;
					CellType type = cell.getCellTypeEnum();
					switch (type) {
						// 读取boolean类型
						case BOOLEAN:
							cellMap.put(ind, cell.getBooleanCellValue());
							break;
						case NUMERIC:
							if (HSSFDateUtil.isCellDateFormatted(cell)) {
								// 读取日期类型
								cellMap.put(ind, cell.getDateCellValue());
							} else {
								// 读取数字类型
								cellMap.put(ind, cell.getNumericCellValue());
							}
							break;
						// 读取公式
						case FORMULA:
							cellMap.put(ind, cell.getCellFormula());
							break;
						default:
							cellMap.put(ind, cell.getStringCellValue());
							break;
					}

				}
				result.put(i, cellMap);
			}
			ips.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 无差别读取excel文件
	 * @param path
	 * @param rowIndex
	 * @param columnIndex
	 * @param sheetIndex
	 * @return
	 */
	public static LinkedHashMap<Integer, LinkedHashMap<Integer, Object>> readExcel(String path, int[] rowIndex,
			int[] columnIndex,int sheetIndex){
		if(path.endsWith(".xls")){
			return FileOperatorUtil.readTableForXLS(path, rowIndex, columnIndex, sheetIndex);
		}else if(path.endsWith(".xlsx")){
			return FileOperatorUtil.readTableForXLSX(path, rowIndex, columnIndex, sheetIndex);
		}else{
			System.out.println("文件类型有误！");
		}
		return null;
	}
	/**
	 * 无差别读取excel文件
	 * @param path
	 * @param rowIndex
	 * @param columnIndex
	 * @param sheetName
	 * @return
	 */
	public static LinkedHashMap<Integer, LinkedHashMap<Integer, Object>> readExcel(String path, int[] rowIndex,
			int[] columnIndex,String sheetName){
		if(path.endsWith(".xls")){
			return FileOperatorUtil.readTableForXLS(path, rowIndex, columnIndex, sheetName);
		}else if(path.endsWith(".xlsx")){
			return FileOperatorUtil.readTableForXLSX(path, rowIndex, columnIndex, sheetName);
		}else{
			System.out.println("文件类型有误！");
		}
		return null;
	}


	/**
	 * 将用List保存的一行一行的数据写入文件中
	 * @param lines
	 * @param folderPath
	 * @param fileName
	 */
	public static void writeFileByList(List<String> lines,String folderPath,String fileName){

        Writer writer = null;

        //写入文件
        File html = new File(folderPath + fileName);
        try {
            writer = new OutputStreamWriter(new FileOutputStream(html), "UTF-8");
            for (String l : lines) {
                writer.write(l + "\n");
            }
        } catch (Exception e) {
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    //ignore
                }
            }
        }

	}

	/**
	 * 读取pf文件，获取真实前沿
	 * @param path
	 * @return
	 */
	public static Map<Integer,double[]> readFileForPf(String path){
		Map<Integer,double[]> result=new HashMap<>();
		File file=new File(path);
		if(file.exists()){
			try {
				FileInputStream fis=new FileInputStream(file);
				InputStreamReader isr=new InputStreamReader(fis, "UTF-8");
				BufferedReader br=new BufferedReader(isr);
				String line="";
				int count=0;
				while((line=br.readLine())!=null){
					String[] str=line.split(" ");
					int len=str.length;
					if(len==1){
						//真实前沿中有些是以“ ”分隔的，有些是“\t”分隔的
						str=line.split("\t");
						len=str.length;
					}
					double[] data=new double[len];
					for(int i=0;i<len;i++){
						data[i]=Double.valueOf(str[i]);
					}
					result.put(count, data);
					count++;
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			System.out.println("前沿文件不存在，无法读取！不再计算前沿指标！");
		}


		return result;
	}








}
