package entity;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Result {
    /**
     * 出流过程
     */
    private double[] Qout;

    /**
     * 新的水位过程
     */
    private double[] Znew;

    /**
     * 违反约束的水位的位置和调整后的值
     */
    private Map<Integer,Double> integerDoubleMap = new HashMap<>();

}
