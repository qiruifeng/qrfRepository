package until;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {

    /**
     * 保存非劣解集进excel
     *
     * @param pop
     * @param sheetName          sheet名称
     * @param tableHead          表头名称
     * @param tableHeadDimension 表头单位
     * @return
     */
    @SuppressWarnings("deprecation")
    public static boolean exportExcel(List<DoubleSolution> pop, String sheetName, ArrayList<String> tableHead,
                                      ArrayList<String> tableHeadDimension, String excelPath) {

        File file = new File(excelPath);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        try {
            file.createNewFile();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        boolean succ = true;
        // 声明一个工作簿,默认保存为低版本的
        HSSFWorkbook wb = new HSSFWorkbook();
        // 声明一个sheet
        HSSFSheet sheet = wb.createSheet(sheetName);
        // 设置sheet名称长度
        sheet.setDefaultColumnWidth((short) 15);
        // 生成一个样式
        HSSFCellStyle style = wb.createCellStyle();
        // 设置居中
        style.setAlignment(HorizontalAlignment.CENTER);
        // 创建表头
        HSSFRow row = sheet.createRow(0);// 表头名称
        HSSFRow rowD = sheet.createRow(1);// 表头单位

        // 为表头赋值
        // HSSFCell cell=row.createCell((short)0);
        for (int i = 0; i < tableHead.size(); i++) {
            row.createCell(i).setCellValue(tableHead.get(i));
            row.getCell(i).setCellStyle(style);
            rowD.createCell(i).setCellValue(tableHeadDimension.get(i));
            rowD.getCell(i).setCellStyle(style);

        }

        // 向表格里填充数据
        for (int i = 0; i < pop.size(); i++) {
            row = sheet.createRow(i + 2);

            Solution chrom = pop.get(i);
            int curIndex = chrom.getNumberOfVariables();

            for (int j = 0; j < curIndex; j++) {
                // 写值
                row.createCell(j).setCellValue((Double) chrom.getVariableValue(j));
                // 改样式
                row.getCell(j).setCellStyle(style);
            }

            // 添加函数值
            for (int j = 0; j < chrom.getNumberOfObjectives(); j++) {
                // 写值
                row.createCell(curIndex + j).setCellValue(chrom.getObjective(j));
                // 改样式
                row.getCell(curIndex + j).setCellStyle(style);
            }
            curIndex = curIndex + chrom.getNumberOfObjectives();

            // 添加违反约束的值
//            double overCons = (double) chrom.getAttribute("overCons");
////            double overCons = (double) chrom.get;
//            // 写值
//            row.createCell(curIndex).setCellValue(overCons);
//            // 改样式
//            row.getCell(curIndex).setCellStyle(style);
//            curIndex = curIndex + 1;

            /*
            // 添加等级
            int rank = chrom.getRank();
            // 写值
            row.createCell(curIndex).setCellValue(rank);
            // 改样式
            row.getCell(curIndex).setCellStyle(style);
            curIndex = curIndex + 1;

            // 添加拥挤距离
            double dis = chrom.getCrowdingDis();
            // 写值
            row.createCell(curIndex).setCellValue(dis);
            // 改样式
            row.getCell(curIndex).setCellStyle(style);

             */

        }

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

        return succ;
    }


    public static boolean exportExcelXLSX(double[][] pop, String sheetName, ArrayList<String> tableHead,
                                          ArrayList<String> tableHeadDimension, String excelPath) {

        File file = new File(excelPath);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        try {
            file.createNewFile();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        boolean succ = true;
        // 声明一个工作簿,默认保存为低版本的
        XSSFWorkbook wb = new XSSFWorkbook();
        // 声明一个sheet
        XSSFSheet sheet = wb.createSheet(sheetName);
        // 设置sheet名称长度
        sheet.setDefaultColumnWidth((short) 15);
        // 生成一个样式
        XSSFCellStyle style = wb.createCellStyle();
        // 设置居中
        style.setAlignment(HorizontalAlignment.CENTER);
        // 创建表头
        XSSFRow row = sheet.createRow(0);// 表头名称
        XSSFRow rowD = sheet.createRow(1);// 表头单位

        // 为表头赋值
        // HSSFCell cell=row.createCell((short)0);
        for (int i = 0; i < tableHead.size(); i++) {
            row.createCell(i).setCellValue(tableHead.get(i));
            row.getCell(i).setCellStyle(style);
            rowD.createCell(i).setCellValue(tableHeadDimension.get(i));
            rowD.getCell(i).setCellStyle(style);

        }

        // 向表格里填充数据
        for (int i = 0; i < pop.length; i++) {
            row = sheet.createRow(i + 2);

//            Solution chrom = pop.get(i);
            double[] chrom = pop[i];
            int curIndex = chrom.length;

            for (int j = 0; j < curIndex; j++) {
                // 写值
                row.createCell(j).setCellValue((Double) chrom[j]);
                // 改样式
                row.getCell(j).setCellStyle(style);
            }

            // 添加函数值
//            for (int j = 0; j < chrom.length; j++) {
//                // 写值
//                row.createCell(curIndex + j).setCellValue(chrom.getObjective(j));
//                // 改样式
//                row.getCell(curIndex + j).setCellStyle(style);
//            }
//            curIndex = curIndex + chrom.getNumberOfObjectives();

            // 添加违反约束的值
//            double overCons = (double) chrom.getAttribute("overCons");
////            double overCons = (double) chrom.get;
//            // 写值
//            row.createCell(curIndex).setCellValue(overCons);
            // 改样式
//            row.getCell(curIndex).setCellStyle(style);
//            curIndex = curIndex + 1;

            /*
            // 添加等级
            int rank = chrom.getRank();
            // 写值
            row.createCell(curIndex).setCellValue(rank);
            // 改样式
            row.getCell(curIndex).setCellStyle(style);
            curIndex = curIndex + 1;

            // 添加拥挤距离
            double dis = chrom.getCrowdingDis();
            // 写值
            row.createCell(curIndex).setCellValue(dis);
            // 改样式
            row.getCell(curIndex).setCellStyle(style);

             */

        }

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

        return succ;
    }


    public static boolean exportExcelXLSX(List<DoubleSolution> pop, String sheetName, ArrayList<String> tableHead,
                                          ArrayList<String> tableHeadDimension, String excelPath) {

        File file = new File(excelPath);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        try {
            file.createNewFile();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        boolean succ = true;
        // 声明一个工作簿,默认保存为低版本的
        XSSFWorkbook wb = new XSSFWorkbook();
        // 声明一个sheet
        XSSFSheet sheet = wb.createSheet(sheetName);
        // 设置sheet名称长度
        sheet.setDefaultColumnWidth((short) 15);
        // 生成一个样式
        XSSFCellStyle style = wb.createCellStyle();
        // 设置居中
        style.setAlignment(HorizontalAlignment.CENTER);
        // 创建表头
        XSSFRow row = sheet.createRow(0);// 表头名称
        XSSFRow rowD = sheet.createRow(1);// 表头单位

        // 为表头赋值
        // HSSFCell cell=row.createCell((short)0);
        for (int i = 0; i < tableHead.size(); i++) {
            row.createCell(i).setCellValue(tableHead.get(i));
            row.getCell(i).setCellStyle(style);
            rowD.createCell(i).setCellValue(tableHeadDimension.get(i));
            rowD.getCell(i).setCellStyle(style);

        }

        // 向表格里填充数据
        for (int i = 0; i < pop.size(); i++) {
            row = sheet.createRow(i + 2);

            Solution chrom = pop.get(i);
            int curIndex = chrom.getNumberOfVariables();

            for (int j = 0; j < curIndex; j++) {
                // 写值
                row.createCell(j).setCellValue((Double) chrom.getVariableValue(j));
                // 改样式
                row.getCell(j).setCellStyle(style);
            }

            // 添加函数值
            for (int j = 0; j < chrom.getNumberOfObjectives(); j++) {
                // 写值
                row.createCell(curIndex + j).setCellValue(chrom.getObjective(j));
                // 改样式
                row.getCell(curIndex + j).setCellStyle(style);
            }
            curIndex = curIndex + chrom.getNumberOfObjectives();

            //添加违反约束的值
//            double overCons = (double) chrom.getAttribute("overCons");
////            double overCons = (double) chrom.get;
//            // 写值
//            row.createCell(curIndex).setCellValue(overCons);
//             //改样式
//            row.getCell(curIndex).setCellStyle(style);
//            curIndex = curIndex + 1;
            if (chrom.getAttribute("power") != null) {
                double power = (double) chrom.getAttribute("power");
                row.createCell(curIndex).setCellValue(power);
                row.getCell(curIndex).setCellStyle(style);
                curIndex = curIndex + 1;
            }
            if (chrom.getAttribute("Qmax") != null) {
                double Qmax = (double) chrom.getAttribute("Qmax");
                row.createCell(curIndex).setCellValue(Qmax);
                row.getCell(curIndex).setCellStyle(style);
                curIndex = curIndex + 1;
            }
            if (chrom.getAttribute("Qmin") != null) {
                double Qmin = (double) chrom.getAttribute("Qmin");
                row.createCell(curIndex).setCellValue(Qmin);
                row.getCell(curIndex).setCellStyle(style);
                curIndex = curIndex + 1;
            }
            if (chrom.getAttribute("n") != null){
                double n = (double) chrom.getAttribute("n");
                row.createCell(curIndex).setCellValue(n);
                row.getCell(curIndex).setCellStyle(style);
                curIndex = curIndex + 1;
            }
            if (chrom.getAttribute("V_left_min") != null){
                double v_left_min = (double) chrom.getAttribute("V_left_min");
                row.createCell(curIndex).setCellValue(v_left_min);
                row.getCell(curIndex).setCellStyle(style);
                curIndex = curIndex + 1;
            }
            if (chrom.getAttribute("V_left_max") != null){
                double v_left_max = (double) chrom.getAttribute("V_left_max");
                row.createCell(curIndex).setCellValue(v_left_max);
                row.getCell(curIndex).setCellStyle(style);
                curIndex = curIndex + 1;
            }
            if (chrom.getAttribute("PingFangHe") != null){
                double pingFangHe = (double) chrom.getAttribute("PingFangHe");
                row.createCell(curIndex).setCellValue(pingFangHe);
                row.getCell(curIndex).setCellStyle(style);
                curIndex = curIndex + 1;
            }




            /*
            // 添加等级
            int rank = chrom.getRank();
            // 写值
            row.createCell(curIndex).setCellValue(rank);
            // 改样式
            row.getCell(curIndex).setCellStyle(style);
            curIndex = curIndex + 1;

            // 添加拥挤距离
            double dis = chrom.getCrowdingDis();
            // 写值
            row.createCell(curIndex).setCellValue(dis);
            // 改样式
            row.getCell(curIndex).setCellStyle(style);

             */

        }

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

        return succ;
    }
}
