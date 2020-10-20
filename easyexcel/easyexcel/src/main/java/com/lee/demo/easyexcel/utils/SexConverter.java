package com.lee.demo.easyexcel.utils;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

/**
 * @ClassName: GenderConverter
 * @Author: Jimmy
 * @Date: 2020/1/14 10:59
 * @Description: 性别 自定义转换器
 */
public class SexConverter implements Converter<String> {

    public static final String MALE = "男";
    public static final String FEMALE = "女";

    @Override
    public Class supportJavaTypeKey() {
        return String.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    /**
     * Excel转Java，用于Excel导入操作
     * @param cellData
     * @param excelContentProperty
     * @param globalConfiguration
     * @return
     * @throws Exception
     */
    @Override
    public String convertToJavaData(CellData cellData, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        String stringValue = cellData.getStringValue();
        if (MALE.equals(stringValue)){
            return "0";
        }else {
            return "1";
        }
    }

    /**
     * Java转Excel，用于Excel导出操作
     * @param value
     * @param excelContentProperty
     * @param globalConfiguration
     * @return
     * @throws Exception
     */
    @Override
    public CellData convertToExcelData(String value, ExcelContentProperty excelContentProperty, GlobalConfiguration globalConfiguration) throws Exception {
        CellData cellData = new CellData(value);
        if (value.equals("0")){
            cellData.setStringValue(MALE);
        }else if (value.equals("1")){
            cellData.setStringValue(FEMALE);
        }
        return cellData;
    }
}