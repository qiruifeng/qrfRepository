package qrf.entity.info;

import lombok.Data;

@Data
public class hospitalBaseInfo {
    private String 入院途径;
    private String 入院日期;
    private String 入院科别;
    private String 入院病区;
    private String 转科科别;
    private String 出院日期;
    private String 出院科别;
    private String 出院病区;
    private String 实际住院天数;
}
