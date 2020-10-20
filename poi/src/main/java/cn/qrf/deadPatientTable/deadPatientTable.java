package cn.qrf.deadPatientTable;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.lang.model.element.NestingKind;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;

import static cn.qrf.tools.tools.printArr;
import static cn.qrf.tools.tools.printMatrix;

/**
 * 给靖靖算死亡时间的
 */
public class deadPatientTable {

    public static void deadPatientTime(String path,String fileName) throws IOException {
        //获取工作簿
        XSSFWorkbook readBook = new XSSFWorkbook(path+fileName);
        //获取工作表
        XSSFSheet sheet = readBook.getSheetAt(0);


        /**
         *将表格内容填到一个二维数组里面
         */
        int lastRowNum = sheet.getLastRowNum();//最后一行
        int lastCol = sheet.getRow(0).getLastCellNum();
        String[][] strMatrix = new String[lastRowNum + 1][lastCol];//用一个二维数组把这个表格装下

        for (int i = 0; i <= lastRowNum; i++) {
            XSSFRow row = sheet.getRow(i);
            if (row != null) {
                short cellNum = row.getLastCellNum();
                for (int j = 0; j <= cellNum; j++) {
                    XSSFCell cell = row.getCell(j);

                    if (cell != null) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);//设置单元格类型为字符串型
                        //cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        String value = cell.getStringCellValue();
                        strMatrix[i][j] = value;
                        //System.out.println(value);
                    }

                }

            }
        }


        Date[][] dateMatrix = StringToDate(strMatrix);//这个二维数组里面是date型变量
        printMatrix(dateMatrix);

        //将时间差存到一个一维数组中
        String[] liveTime=new String[dateMatrix.length];
        for (int i=0;i<dateMatrix.length;i++){
            if (dateMatrix[i][0]!=null){
                liveTime[i]=betweenTowDate(dateMatrix[i][0],dateMatrix[i][1]);
            }
        }
        //printMatrix(strMatrix);
        printArr(liveTime);
        writeExcel(liveTime,path);


    }

    /**
     * 将结果写入excel
     * @param strArr
     * @param path
     * @throws IOException
     */
    public static void writeExcel(String[] strArr,String path) throws IOException {
        XSSFWorkbook writeBook = new XSSFWorkbook();
        XSSFSheet sheet = writeBook.createSheet("result");
        for (int i=0;i<strArr.length;i++){
            XSSFRow row=sheet.createRow(i);
            if (strArr[i]!=null){
                row.createCell(0).setCellValue(strArr[i]);
            }
        }

        //输出流
        FileOutputStream out=new FileOutputStream(path+"result.xlsx");
        writeBook.write(out);

        out.flush();
        out.close();
    }

    /**
     * 把两个时间差转化为天数
     * @param start
     * @param end
     * @return
     */
    public static String  betweenTowDate(Date start, Date end) {
        DecimalFormat df=new DecimalFormat("0.0");
        long l1 = end.getTime();
        long l2 = start.getTime();
        long between = l1 - l2;
        String  day=df.format((float)between / (24 * 60 * 60 * 1000));
        return day;
    }

    /**
     * 把整型二维数组转化成Date型二维数组
     *
     * @param strMatrix
     * @return
     */
    public static Date[][] StringToDate(String[][] strMatrix) {
        Date[][] dateMatrix = new Date[strMatrix.length][strMatrix[0].length];
        for (int i = 0; i < dateMatrix.length; i++) {

            for (int j = 0; j < dateMatrix[0].length; j++) {
                if (strMatrix[i][j] != null) {
                    String[] temp = strMatrix[i][j].split("\\.");
                    int[] ints = StringChangeToInt(temp);
                    dateMatrix[i][j] = new Date(2020, ints[0]-1, ints[1], ints[2], ints[3]);
                }

            }
        }
        return dateMatrix;
    }

    /**
     * 把一个字符串数组转化为整型数组
     *
     * @param str
     * @return
     */
    public static int[] StringChangeToInt(String[] str) {
        int[] res = new int[str.length];
        for (int i = 0; i < str.length; i++) {
            res[i] = Integer.parseInt(str[i]);
        }
        return res;
    }


    public static void main(String[] args) throws IOException {
        deadPatientTime("D:\\work\\新型肺炎资料\\死亡患者\\","test.xlsx");
//        Date date1 = new Date(2020, 1, 2, 1, 24);
//        Date date2 = new Date(2020, 1, 3, 18, 24);
//        System.out.println(betweenTowDate(date1, date2));
    }
}
