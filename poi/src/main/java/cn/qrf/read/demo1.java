package cn.qrf.read;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;

public class demo1 {
    public static void main(String[] args) throws IOException {
        //获取工作簿
        XSSFWorkbook readBook = new XSSFWorkbook("G:\\dadExcel\\201911profitCalculationTable.xlsx");
        //获取工作表
        XSSFSheet sheet = readBook.getSheetAt(0);
        //遍历单元格

        //增强for循环
//        for (Row row:sheet){//获取行
//            for (Cell cell:row){//获取单元格
//                cell.setCellType(Cell.CELL_TYPE_STRING);//设置单元格类型为字符型
//                String value=cell.getStringCellValue();
//                System.out.println(value);
//            }
//        }

        //普通for循环


        int lastRowNum = sheet.getLastRowNum();//最后一行
        int lastCol=sheet.getRow(4).getLastCellNum();
        String[][] strMatrix=new String[lastRowNum+1][lastCol+1];//用一个二维数组把这个表格装下

        for (int i = 0; i <= lastRowNum; i++) {
            XSSFRow row = sheet.getRow(i);
            if (row != null) {
                short cellNum = row.getLastCellNum();



                for (int j = 0; j <= cellNum; j++) {
                    XSSFCell cell = row.getCell(j);

                    if (cell != null) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);//设置单元格类型为字符串型
                        String value = cell.getStringCellValue();
                       strMatrix[i][j]=value;
                       // System.out.println(value);
                    }

                }

            }
        }
//        System.out.println("=================================");
//        System.out.println(strMatrix[4][3]);
//
//
//        //test
//        String test="你好吗我是漆瑞丰";
//        boolean isTrue=test.contains("你我");
//        System.out.println(isTrue);


        //创建写入的工作簿(空参构造器)
        XSSFWorkbook writeBook=new XSSFWorkbook();
        //创建工作表
        XSSFSheet sheet1 = writeBook.createSheet("处理后");
    }
}
