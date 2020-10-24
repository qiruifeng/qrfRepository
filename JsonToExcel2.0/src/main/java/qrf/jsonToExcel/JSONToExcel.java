package qrf.jsonToExcel;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import qrf.entity.CheckDetails;
import qrf.entity.CheckRecords;
import qrf.entity.MedicalOrder;
import qrf.entity.PatientCase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import static qrf.util.util.getFiles;
import static qrf.util.util.getJSONString;

public class JSONToExcel {

    static String PATH = "F:\\test\\res\\";


    public static void main(String[] args) throws Exception {

        //1、获取所有json文件
        List<File> files = getFiles("F:\\temp2");

        //获取json数据
        File jsonFile = files.get(0);
        String jsonStr = getJSONString(jsonFile);
        JSONArray jsonArray = JSON.parseArray(jsonStr);


        //创建一个工作簿
        Workbook workbook = new XSSFWorkbook();


        if (jsonArray.size() == 1) {

            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            Long re= (Long) jsonObject.get("就诊ID");

            System.out.println("re: "+re);
            PatientCase patientCase = JSON.parseObject(jsonObject.toString(), PatientCase.class);//获取到病人信息之后，填写表格

            //创建基本信息工作表
            {
                Sheet sheet_baseIfo = workbook.createSheet("患者病历信息");
                //创建基本信息工作表表头
                Row tableHead_baseIfo = sheet_baseIfo.createRow(0);
                Cell cell1 = tableHead_baseIfo.createCell(0);
                cell1.setCellValue("就诊ID");
                sheet_baseIfo.setColumnWidth(0, 5500);
                Cell cell2 = tableHead_baseIfo.createCell(1);
                cell2.setCellValue("医嘱列表");
                sheet_baseIfo.setColumnWidth(1, 6000);
                Cell cell3 = tableHead_baseIfo.createCell(2);
                cell3.setCellValue("日期");
                sheet_baseIfo.setColumnWidth(2, 2500);
                Cell cell4 = tableHead_baseIfo.createCell(3);
                cell4.setCellValue("检查列表");
                sheet_baseIfo.setColumnWidth(3, 6000);
                Cell cell5 = tableHead_baseIfo.createCell(4);
                cell5.setCellValue("住院流水号");
                sheet_baseIfo.setColumnWidth(4, 2500);
                Cell cell6 = tableHead_baseIfo.createCell(5);
                cell6.setCellValue("科室");
                sheet_baseIfo.setColumnWidth(5, 3500);
                Cell cell7 = tableHead_baseIfo.createCell(6);
                cell7.setCellValue("类型");
                sheet_baseIfo.setColumnWidth(6, 2500);
                Cell cell8 = tableHead_baseIfo.createCell(7);
                cell8.setCellValue("报告时间");
                sheet_baseIfo.setColumnWidth(7, 3500);
                //填写基本信息表
                Row row1 = sheet_baseIfo.createRow(1);
                Cell row1Cell1 = row1.createCell(0);
                Cell row1Cell2 = row1.createCell(1);
                Cell row1Cell3 = row1.createCell(2);
                Cell row1Cell4 = row1.createCell(3);
                Cell row1Cell5 = row1.createCell(4);
                Cell row1Cell6 = row1.createCell(5);
                Cell row1Cell7 = row1.createCell(6);
                Cell row1Cell8 = row1.createCell(7);
                try {
                    row1Cell1.setCellValue(patientCase.get就诊ID());
                } catch (Exception e) {
                    System.out.println(e.toString());
                    System.out.println("就诊ID为空值");
                    row1Cell8.setCellValue("就诊ID为空值");
                }
                row1Cell2.setCellValue("医嘱列表见sheet-医嘱列表");
                try {
                    row1Cell3.setCellValue(patientCase.get日期());
                    System.out.println(patientCase.get日期());
                } catch (Exception e) {
                    System.out.println(e.toString());
                    System.out.println("日期为空值");
                    row1Cell8.setCellValue("日期为空值");
                }
                row1Cell4.setCellValue("检查列表见sheet-检查列表");
                try {
                    row1Cell5.setCellValue(patientCase.get住院流水号());
                } catch (Exception e) {
                    System.out.println(e.toString());
                    System.out.println("住院流水号为空值");
                    row1Cell8.setCellValue("住院流水号为空值");
                }
                try {
                    row1Cell6.setCellValue(patientCase.get科室());
                } catch (Exception e) {
                    System.out.println(e.toString());
                    System.out.println("科室为空值");
                    row1Cell8.setCellValue("科室为空值");
                }
                try {
                    row1Cell7.setCellValue(patientCase.get类型());
                } catch (Exception e) {
                    System.out.println(e.toString());
                    System.out.println("类型为空值");
                    row1Cell8.setCellValue("类型为空值");
                }
                try {
                    row1Cell8.setCellValue(patientCase.get报告时间());
                } catch (Exception e) {
                    System.out.println(e.toString());
                    System.out.println("报告时间为空值");
                    row1Cell8.setCellValue("报告时间为空值");
                }
            }

            //创建医嘱列表
            {
                Sheet sheet_doctorOrderList = workbook.createSheet("医嘱列表");
                Row tableHead_doctorOrderList_row = sheet_doctorOrderList.createRow(0);
                //创建表头
                Cell cell2_1 = tableHead_doctorOrderList_row.createCell(0);
                cell2_1.setCellValue("医嘱执行记录ID");

                Cell cell2_2 = tableHead_doctorOrderList_row.createCell(1);
                cell2_2.setCellValue("频次");
//            sheet_doctorOrderList.setColumnWidth(1,6000);
                Cell cell2_3 = tableHead_doctorOrderList_row.createCell(2);
                cell2_3.setCellValue("药品序号");
//            sheet_doctorOrderList.setColumnWidth(2,2500);
                Cell cell2_4 = tableHead_doctorOrderList_row.createCell(3);
                cell2_4.setCellValue("开单时间");
//            sheet_doctorOrderList.setColumnWidth(3,6000);
                Cell cell2_5 = tableHead_doctorOrderList_row.createCell(4);
                cell2_5.setCellValue("医嘱id");
//            sheet_doctorOrderList.setColumnWidth(4,2500);
                Cell cell2_6 = tableHead_doctorOrderList_row.createCell(5);
                cell2_6.setCellValue("医嘱名称");
//            sheet_doctorOrderList.setColumnWidth(5,3500);
                Cell cell2_7 = tableHead_doctorOrderList_row.createCell(6);
                cell2_7.setCellValue("组号");
//            sheet_doctorOrderList.setColumnWidth(6,2500);
                Cell cell2_8 = tableHead_doctorOrderList_row.createCell(7);
                cell2_8.setCellValue("住院流水号");
//            sheet_doctorOrderList.setColumnWidth(7,3500);
                Cell cell2_9 = tableHead_doctorOrderList_row.createCell(8);
                cell2_9.setCellValue("结束时间");
//            sheet_doctorOrderList.setColumnWidth(7,3500);

                //填写表格
                List<MedicalOrder> doctorOrderList = patientCase.get医嘱列表();
                for (int i = 0; i < doctorOrderList.size(); i++) {
                    MedicalOrder medicalOrderNow = doctorOrderList.get(i);
                    Row row_i = sheet_doctorOrderList.createRow(i + 1);
                    Cell row_i_cell1 = row_i.createCell(0);
                    Cell row_i_cell2 = row_i.createCell(1);
                    Cell row_i_cell3 = row_i.createCell(2);
                    Cell row_i_cell4 = row_i.createCell(3);
                    Cell row_i_cell5 = row_i.createCell(4);
                    Cell row_i_cell6 = row_i.createCell(5);
                    Cell row_i_cell7 = row_i.createCell(6);
                    Cell row_i_cell8 = row_i.createCell(7);
                    Cell row_i_cell9 = row_i.createCell(8);

                    try {
                        row_i_cell1.setCellValue(medicalOrderNow.get医嘱执行记录ID());
                    } catch (Exception e) {
                        System.out.println(e.toString());
                        System.out.println("医嘱执行记录ID为空值");
                        row_i_cell1.setCellValue("医嘱执行记录ID为空值");
                    }
                    try {
                        row_i_cell2.setCellValue(medicalOrderNow.get频次());
                    } catch (Exception e) {
                        System.out.println(e.toString());
                        System.out.println("频次为空值");
                        row_i_cell2.setCellValue("频次为空值");
                    }
                    try {
                        row_i_cell3.setCellValue(medicalOrderNow.get药品序号());
                    } catch (Exception e) {
                        System.out.println(e.toString());
                        System.out.println("药品序号为空值");
                        row_i_cell3.setCellValue("药品序号为空值");
                    }
                    try {
                        row_i_cell4.setCellValue(medicalOrderNow.get开单时间());
                    } catch (Exception e) {
                        System.out.println(e.toString());
                        System.out.println("开单时间为空值");
                        row_i_cell4.setCellValue("开单时间为空值");
                    }
                    try {
                        row_i_cell5.setCellValue(medicalOrderNow.get医嘱id());
                    } catch (Exception e) {
                        System.out.println(e.toString());
                        System.out.println("医嘱id为空值");
                        row_i_cell5.setCellValue("医嘱id为空值");
                    }
                    try {
                        row_i_cell6.setCellValue(medicalOrderNow.get医嘱名称());
                    } catch (Exception e) {
                        System.out.println(e.toString());
                        System.out.println("医嘱名称为空值");
                        row_i_cell6.setCellValue("医嘱名称为空值");
                    }
                    try {
                        row_i_cell7.setCellValue(medicalOrderNow.get组号());
                    } catch (Exception e) {
                        System.out.println(e.toString());
                        System.out.println("组号为空值");
                        row_i_cell7.setCellValue("组号为空值");
                    }
                    try {
                        row_i_cell8.setCellValue(medicalOrderNow.get住院流水号());
                    } catch (Exception e) {
                        System.out.println(e.toString());
                        System.out.println("住院流水号为空值");
                        row_i_cell8.setCellValue("住院流水号为空值");
                    }
                    try {
                        row_i_cell9.setCellValue(medicalOrderNow.get结束时间());
                    } catch (Exception e) {
                        System.out.println(e.toString());
                        System.out.println("结束时间为空值");
                        row_i_cell9.setCellValue("结束时间为空值");
                    }
                }
            }

            //创建检查列表
            {
                Sheet sheet_checkList = workbook.createSheet("检查列表");
                Row tableHead_checkList_row = sheet_checkList.createRow(0);
                //创建表头
                Cell cell3_1 = tableHead_checkList_row.createCell(0);
                cell3_1.setCellValue("检查详情");
//            sheet_doctorOrderList.setColumnWidth(0,6000);
                Cell cell3_2 = tableHead_checkList_row.createCell(1);
                cell3_2.setCellValue("检查ID");
//            sheet_doctorOrderList.setColumnWidth(1,6000);
                Cell cell3_3 = tableHead_checkList_row.createCell(2);
                cell3_3.setCellValue("报告时间");
//            sheet_doctorOrderList.setColumnWidth(2,2500);
                Cell cell3_4 = tableHead_checkList_row.createCell(3);
                cell3_4.setCellValue("患者姓名");
//            sheet_doctorOrderList.setColumnWidth(3,6000);
                Cell cell3_5 = tableHead_checkList_row.createCell(4);
                cell3_5.setCellValue("检查名称");
//            sheet_doctorOrderList.setColumnWidth(4,2500);
                Cell cell3_6 = tableHead_checkList_row.createCell(5);
                cell3_6.setCellValue("检查类型");
//            sheet_doctorOrderList.setColumnWidth(5,3500);

                Cell cell3_7 = tableHead_checkList_row.createCell(6);
                cell3_7.setCellValue("审核医生姓名");
//            sheet_doctorOrderList.setColumnWidth(0,6000);
                Cell cell3_8 = tableHead_checkList_row.createCell(7);
                cell3_8.setCellValue("病人性别");
//            sheet_doctorOrderList.setColumnWidth(1,6000);
                Cell cell3_9 = tableHead_checkList_row.createCell(8);
                cell3_9.setCellValue("报告编号");
//            sheet_doctorOrderList.setColumnWidth(2,2500);
                Cell cell3_10 = tableHead_checkList_row.createCell(9);
                cell3_10.setCellValue("检查医生姓名");
//            sheet_doctorOrderList.setColumnWidth(3,6000);
                Cell cell3_11 = tableHead_checkList_row.createCell(10);
                cell3_11.setCellValue("诊断或提示");
//            sheet_doctorOrderList.setColumnWidth(4,2500);
                Cell cell3_12 = tableHead_checkList_row.createCell(11);
                cell3_12.setCellValue("检查科室名称");
//            sheet_doctorOrderList.setColumnWidth(5,3500);
                Cell cell3_13 = tableHead_checkList_row.createCell(12);
                cell3_13.setCellValue("审核时间");
//            sheet_doctorOrderList.setColumnWidth(5,3500);
                Cell cell3_14 = tableHead_checkList_row.createCell(13);
                cell3_14.setCellValue("检查时间");
//            sheet_doctorOrderList.setColumnWidth(5,3500);
                Cell cell3_15 = tableHead_checkList_row.createCell(14);
                cell3_15.setCellValue("病人姓名");
//            sheet_doctorOrderList.setColumnWidth(5,3500);
                Cell cell3_16 = tableHead_checkList_row.createCell(15);
                cell3_16.setCellValue("检查所见");
//            sheet_doctorOrderList.setColumnWidth(5,3500);
                Cell cell3_17 = tableHead_checkList_row.createCell(16);
                cell3_17.setCellValue("病人类型");
//            sheet_doctorOrderList.setColumnWidth(5,3500);
                Cell cell3_18 = tableHead_checkList_row.createCell(17);
                cell3_18.setCellValue("检查部位");
//            sheet_doctorOrderList.setColumnWidth(5,3500);


                //填写表格
                List<CheckRecords> checkRecordsList = patientCase.get检查列表();
                for (int i = 0; i < checkRecordsList.size(); i++) {
                    CheckRecords checkRecordsNow = checkRecordsList.get(i);
                    Row row_i = sheet_checkList.createRow(i + 1);
                    Cell row_i_cell1 = row_i.createCell(0);
                    Cell row_i_cell2 = row_i.createCell(1);
                    Cell row_i_cell3 = row_i.createCell(2);
                    Cell row_i_cell4 = row_i.createCell(3);
                    Cell row_i_cell5 = row_i.createCell(4);
                    Cell row_i_cell6 = row_i.createCell(5);
                    Cell row_i_cell7 = row_i.createCell(6);
                    Cell row_i_cell8 = row_i.createCell(7);
                    Cell row_i_cell9 = row_i.createCell(8);
                    Cell row_i_cell10 = row_i.createCell(9);
                    Cell row_i_cell11= row_i.createCell(10);
                    Cell row_i_cell12= row_i.createCell(11);
                    Cell row_i_cell13= row_i.createCell(12);
                    Cell row_i_cell14= row_i.createCell(13);
                    Cell row_i_cell15= row_i.createCell(14);
                    Cell row_i_cell16= row_i.createCell(15);
                    Cell row_i_cell17= row_i.createCell(16);
                    Cell row_i_cell18= row_i.createCell(17);


                    row_i_cell1.setCellValue("见列G-H");
                    try {
                        row_i_cell2.setCellValue(checkRecordsNow.get检查ID());
                    } catch (Exception e) {
                        System.out.println(e.toString());
                        System.out.println("检查ID为空");
                        row_i_cell2.setCellValue("检查ID为空");
                    }
                    try {
                        row_i_cell3.setCellValue(checkRecordsNow.get报告时间());
                    } catch (Exception e) {
                        System.out.println(e.toString());
                        System.out.println("报告时间为空");
                        row_i_cell3.setCellValue("报告时间为空");
                    }
                    try {
                        row_i_cell4.setCellValue(checkRecordsNow.get患者姓名());
                    } catch (Exception e) {
                        System.out.println(e.toString());
                        System.out.println("患者姓名为空");
                        row_i_cell4.setCellValue("患者姓名为空");
                    }
                    try {
                        row_i_cell5.setCellValue(checkRecordsNow.get检查名称());
                    } catch (Exception e) {
                        System.out.println(e.toString());
                        System.out.println("检查名称为空");
                        row_i_cell5.setCellValue("检查名称为空");
                    }
                    try {
                        row_i_cell6.setCellValue(checkRecordsNow.get检查类型());
                    } catch (Exception e) {
                        System.out.println(e.toString());
                        System.out.println("检查类型为空");
                        row_i_cell6.setCellValue("检查类型为空");
                    }
                    try {
                        row_i_cell7.setCellValue(checkRecordsNow.get检查详情().get审核医生姓名());
                    } catch (Exception e) {
                        System.out.println(e.toString());
                        System.out.println("审核医生姓名为空");
                        row_i_cell7.setCellValue("审核医生姓名为空");
                    }
                    try {
                        row_i_cell8.setCellValue(checkRecordsNow.get检查详情().get病人性别());
                    } catch (Exception e) {
                        System.out.println(e.toString());
                        System.out.println("病人性别为空");
                        row_i_cell8.setCellValue("病人性别为空");
                    }
                    try {
                        row_i_cell9.setCellValue(checkRecordsNow.get检查详情().get报告编号());
                    } catch (Exception e) {
                        System.out.println(e.toString());
                        System.out.println("报告编号为空");
                        row_i_cell9.setCellValue("报告编号为空");
                    }
                    try {
                        row_i_cell10.setCellValue(checkRecordsNow.get检查详情().get检查医生姓名());
                    } catch (Exception e) {
                        System.out.println(e.toString());
                        System.out.println("检查医生姓名为空");
                        row_i_cell10.setCellValue("检查医生姓名为空");
                    }
                    try {
                        row_i_cell11.setCellValue(checkRecordsNow.get检查详情().get诊断或提示());
                    } catch (Exception e) {
                        System.out.println(e.toString());
                        System.out.println("诊断或提示为空");
                        row_i_cell11.setCellValue("诊断或提示为空");
                    }
                    try {
                        row_i_cell12.setCellValue(checkRecordsNow.get检查详情().get检查科室名称());
                    } catch (Exception e) {
                        System.out.println(e.toString());
                        System.out.println("检查科室名称为空");
                        row_i_cell12.setCellValue("检查科室名称为空");
                    }
                    try {
                        row_i_cell13.setCellValue(checkRecordsNow.get检查详情().get审核时间());
                    } catch (Exception e) {
                        System.out.println(e.toString());
                        System.out.println("审核时间为空");
                        row_i_cell13.setCellValue("审核时间为空");
                    }
                    try {
                        row_i_cell14.setCellValue(checkRecordsNow.get检查详情().get检查时间());
                    } catch (Exception e) {
                        System.out.println(e.toString());
                        System.out.println("检查时间为空");
                        row_i_cell14.setCellValue("检查时间为空");
                    }
                    try {
                        row_i_cell15.setCellValue(checkRecordsNow.get检查详情().get病人姓名());
                    } catch (Exception e) {
                        System.out.println(e.toString());
                        System.out.println("病人姓名为空");
                        row_i_cell15.setCellValue("病人姓名为空");
                    }
                    try {
                        row_i_cell16.setCellValue(checkRecordsNow.get检查详情().get检查所见());
                    } catch (Exception e) {
                        System.out.println(e.toString());
                        System.out.println("检查所见为空");
                        row_i_cell16.setCellValue("检查所见为空");
                    }
                    try {
                        row_i_cell17.setCellValue(checkRecordsNow.get检查详情().get病人类型());
                    } catch (Exception e) {
                        System.out.println(e.toString());
                        System.out.println("病人类型为空");
                        row_i_cell17.setCellValue("病人类型为空");
                    }
                    try {
                        row_i_cell18.setCellValue(checkRecordsNow.get检查详情().get检查部位());
                    } catch (Exception e) {
                        System.out.println(e.toString());
                        System.out.println("检查部位为空");
                        row_i_cell18.setCellValue("检查部位为空");
                    }
                }
            }


        } else {
            System.out.println("请查看相关文件" + jsonFile.toString());
        }


        //4、输出文件
        FileOutputStream fileOutputStream = new FileOutputStream(PATH + jsonFile.getName().substring(0, 7) + "test" + ".xlsx");
        workbook.write(fileOutputStream);
        fileOutputStream.close();
        System.out.println("输出完毕");
    }


}
