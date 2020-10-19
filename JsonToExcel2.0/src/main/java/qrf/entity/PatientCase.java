package qrf.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 这个实体类是整个病例包含了所有信息
 * 其中检查列表是一个list，由每一个检查记录（CheckRecords）构成
 */
@Data
public class PatientCase {

    private String 就诊ID;

//    /**
//     * 检验列表
//     */
//    private List<String> checkList=new ArrayList<>();

    private List<MedicalOrder> 医嘱列表=new ArrayList<>();

    private Date 日期;

    private List<CheckRecords> 检查列表=new ArrayList<>();

    private String 住院流水号;

    private String 科室;

    private String 类型;

    private Date 报告时间;
}
