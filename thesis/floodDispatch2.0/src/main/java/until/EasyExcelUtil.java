package until;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.*;

public class EasyExcelUtil {


    public static void main(String[] args) {
        readTable("1三峡基本特性曲线",1);
        System.out.println("over");
    }

    //读取文件

    /**
     *
     * @param tableName
     * @param sheetIndex
     * @return
     */
    public static Double[][] readTable(String tableName, int sheetIndex){

        String bathPath = "data/baseData/";
        String endPath = ".xls";

        List<Map<Integer,String>> list=new LinkedList<>();
        EasyExcel.read(bathPath+tableName+endPath)
        .sheet(sheetIndex)
        .registerReadListener(new AnalysisEventListener<HashMap<Integer,String>>() {//这个里面指定什么样的泛型，就出来什么样的数据

            @Override
            public void invoke(HashMap<Integer,String> data, AnalysisContext context) {
                list.add(data);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                System.out.println("end");
            }
        }).doRead();

        Set<Integer> keySet=list.get(0).keySet();

        Double[][] res=new Double[keySet.size()][list.size()];
        for (int i = 0; i < keySet.size(); i++) {
            for (int j = 0; j < list.size(); j++) {
                res[i][j]=Double.valueOf(list.get(j).get(i));
            }
        }
        return res;
    }



}
