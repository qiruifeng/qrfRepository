package qrf.entity;

import lombok.Data;

import java.util.Date;

/**
 * 记录检查详情信息
 */
@Data
public class CheckDetails {

    private String 审核医生姓名;

    private String 病人性别;

    private String 报告编号;

    private String 检查医生姓名;

    private String 诊断或提示;

    private String 检查科室名称;

    private Date 审核时间;

    private Date 检查时间;

    private String 病人姓名;

    private String 检查所见;

    private String 病人类型;

    private String 检查部位;

}
