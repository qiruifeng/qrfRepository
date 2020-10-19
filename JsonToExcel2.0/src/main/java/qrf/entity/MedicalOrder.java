package qrf.entity;

import lombok.Data;

import java.util.Date;


/**
 * 医嘱列表内容
 * 		"医嘱执行记录ID": "1058386||616",
 * 		"频次": "st",
 * 		"药品序号": 0,Drug serial number
 * 		"开单时间": "2019-03-18 10:51:00",
 * 		"医嘱id": "220400011094701",Billing time
 * 		"单次剂量（单位）": " ",Single dose (unit)
 * 		"医嘱名称": "[H]今日出院",Name of doctor's order
 * 		"组号": "416653183",Group No
 * 		"住院流水号": "1879130",Hospital serial number
 * 		"结束时间": "2019-03-18 10:51:00"End Time
 */
@Data
public class MedicalOrder {

    private String 医嘱执行记录ID;

    private String 频次;

    private String 药品序号;

    private Date 开单时间;

    private String 医嘱id;

//    /**
//     * 单次剂量（单位）
//     */
//    private String 单次剂量(单位);

    private String 医嘱名称;

    private String 组号;

    private String 住院流水号;

    private Date 结束时间;
}
