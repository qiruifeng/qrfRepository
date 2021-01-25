package cn.qrf.spireXls;

import com.spire.xls.Chart;
import com.spire.xls.Workbook;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class demo {
    public static void main(String[] args) throws IOException {
        Workbook workbook = new Workbook();
        workbook.loadFromFile("D:\\test\\M05ZDJ.xlsx");
//

        Chart chart=workbook.getWorksheets().get(0).getCharts().get(0);
//        chart.setWidth(1500);
//        chart.setHeight(1000);



        //将Excel文档第一个工作表中的第一个图表保存为图片
        BufferedImage image= workbook.saveChartAsImage(workbook.getWorksheets().get(0),0);
        ImageIO.write(image,"png", new File("D:\\test\\ChartToImage2.png"));
        System.out.println("success");
    }
}
