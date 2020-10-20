package cn.qrf.deadPatientTable;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class testTime {

    public static void getTestTime(String path, String fileName) throws IOException {
        //获取工作簿
        XSSFWorkbook readBook = new XSSFWorkbook(path + fileName);
        //获取工作表
        XSSFSheet sheet = readBook.getSheetAt(1);


        int lastRowNum = sheet.getLastRowNum();
        System.out.println("start");
        for (int i = 2; i < lastRowNum; i++) {
            //获取单元格
            XSSFRow row = sheet.getRow(i);
            XSSFCell Cell1 = row.getCell(1);
            XSSFCell Cell2 = row.getCell(2);


//            System.out.println(Cell2);
//
//            if (Cell2!=null){
//                Date dateCell2=Cell2.getDateCellValue();
//                System.out.println(dateCell2);
//            }


            //将单元格转化为时间戳格式
            Date dateCell1 = Cell1.getDateCellValue();
            Date dateCell2 = Cell2.getDateCellValue();
            //判断时间间隔，写入表格
            if (dateCell1 != null && dateCell2 != null) {
                if (dateCell2.getTime() - dateCell1.getTime() > 2 * 24 * 60 * 60 * 1000) {
                    row.createCell(5).setCellValue("0");
                } else {
                    row.createCell(5).setCellValue("1");
                }
            }


        }

        //输出流
        FileOutputStream out=new FileOutputStream(path+fileName);
        readBook.write(out);

        out.flush();
        out.close();
        System.out.println("end");


//        //test
//        XSSFRow row1 = sheet.getRow(3);
//        XSSFCell Cell1 = row1.getCell(2);
//        Date dateCell1 = Cell1.getDateCellValue();
//
//        XSSFRow row2 = sheet.getRow(4);
//        XSSFCell Cell2 = row2.getCell(2);
//        Date dateCell2 = Cell2.getDateCellValue();
//
//        System.out.println(Cell1.getDateCellValue());
//        System.out.println(Cell2.getDateCellValue());
//        System.out.println(dateCell2.getTime() - dateCell1.getTime());

    }

    public static void main(String[] args) throws IOException {
        getTestTime("D:\\work\\新型肺炎资料\\", "test.xlsx");
    }

}
