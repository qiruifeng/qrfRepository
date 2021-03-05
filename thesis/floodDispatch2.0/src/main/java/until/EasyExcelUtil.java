package until;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.*;

public class EasyExcelUtil {


    public static void main(String[] args) {
        System.out.println("start");
        double[][] test = readTable("入库数据",0,2,8);
        System.out.println("over");
    }

    //读取文件

    /**
     * @param tableName
     * @param sheetIndex
     * @return
     */
    public static double[][] readTable(String tableName, int sheetIndex) {

        String bathPath = "data/baseData/";
        String endPath = ".xls";

        List<Map<Integer, String>> list = new LinkedList<>();
        EasyExcel.read(bathPath + tableName + endPath)
                .sheet(sheetIndex)
                .registerReadListener(new AnalysisEventListener<HashMap<Integer, String>>() {//这个里面指定什么样的泛型，就出来什么样的数据

                    @Override
                    public void invoke(HashMap<Integer, String> data, AnalysisContext context) {
                        list.add(data);
                    }

                    @Override
                    public void doAfterAllAnalysed(AnalysisContext context) {

                    }
                }).doRead();

        Set<Integer> keySet = list.get(0).keySet();

        double[][] res = new double[keySet.size()][list.size()];
        for (int i = 0; i < keySet.size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                res[i][j] = Double.valueOf(list.get(j).get(i));
            }
        }
        return res;
    }

    public static double[][] readTable(String tableName, int sheetIndex, int rowStart, int rowEnd) {
        double[][] origin = readTable(tableName, sheetIndex);
        double[][] ans = new double[origin.length][rowEnd - rowStart + 1];

        for (int i = 0; i < ans.length; i++) {
            for (int j = rowStart - 2; j < rowEnd - 1; j++) {
                ans[i][j - (rowStart - 2)] = origin[i][j];
            }
        }
        return ans;
    }

}
