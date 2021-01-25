package cn.qrf.read;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;

public class demo {
    public static void main(String[] args) throws IOException {
        XSSFWorkbook readBook = new XSSFWorkbook("D:\\study\\studyProjects\\monitor\\monitor-platform\\uploads\\zip\\LG03#（土压力计）.xlsx");
        System.out.println("success" );
    }
}
