package qrf.entity;

import lombok.Data;

import java.util.Date;

/**
 * 检查列表
 * 每一项检查记录
 * 其中检查详情是一个对象(CheckDetails)，记录详情信息
 */
@Data
public class CheckRecords {

    private CheckDetails 检查详情;

    private String 检查ID;

    private Date 报告时间;

    private String 患者姓名;

    private String 检查名称;

    private String 检查类型;

}
