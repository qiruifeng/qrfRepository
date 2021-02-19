package jmetal.problem;

import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import until.EasyExcelUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FloodProProblem extends AbstractDoubleProblem {

    private static final long serialVersionUID = 1L;

    //约束
    private HashMap<String, Double[]> constraints;//目前考虑的：时段的水库约束
    //水库个数，单库或者3库或者5库
    private int stationNum;
    //对应控制站点的流量,K-站点名称，V-流量数组
    private HashMap<String, Double[]> inQFromStation;
    //水库的水位库容曲线,K-水库名称，V-水位库容曲线，0-水位，1-库容
    private HashMap<String, Double[][]> ZVCurvedOfStation;
    //水库下泄能力曲线，K-水库名称，V-下泄能力曲线，0-水位，1-下泄能力
    private HashMap<String, Double[][]> ZOutQOfStation;


    public static void main(String[] args) {
        FloodProProblem a = new FloodProProblem(122, 5);
    }

    /**
     * 构造器1：设置时段个数和水库个数
     * 决策变量的个数就是两者相乘
     *
     * @param xNum
     * @param stationNum
     */
    public FloodProProblem(int xNum, int stationNum) {

        this.stationNum = stationNum;

        initData();

        setNumberOfVariables(xNum * stationNum);
        List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables());
        List<Double> upperLimit = new ArrayList<>(getNumberOfVariables());

        //针对不同水库设置不同的水位限制，汛限水位和防洪高水位，三向溪白乌


        switch (stationNum) {
            case 5:
                for (int i = 0; i < xNum; i++) {
                    lowerLimit.add(145.0);
                    upperLimit.add(175.0);//三峡特征水位
                }
            case 3:
                for (int i = xNum; i < xNum * 2; i++) {
                    lowerLimit.add(370.0);
                    upperLimit.add(380.0);//向家坝特征水位
                }
                for (int i = xNum * 2; i < xNum * 3; i++) {
                    lowerLimit.add(560.0);
                    upperLimit.add(600.0);//溪洛渡特征水位
                }
            case 1:
                for (int i = xNum * 3; i < xNum * 4; i++) {
                    lowerLimit.add(785.0);
                    upperLimit.add(825.0);//向家坝特征水位
                }
                for (int i = xNum * 4; i < xNum * 5; i++) {
                    lowerLimit.add(952.0);
                    upperLimit.add(975.0);//溪洛渡特征水位
                }
                break;
            default:
                System.out.println("水库个数只能是1，3，5");

        }

        System.out.println("upperLimit:" + upperLimit);
        System.out.println("lowerLimit:" + lowerLimit);

        setUpperLimit(upperLimit);
        setLowerLimit(lowerLimit);
    }

    /**
     * 水库参数初始化
     */
    private void initData() {
        //整个模型的输入，单库就是三峡的入流，多库就是梯级开始的的入流和向三区间的输入

        switch (this.stationNum){
            case 5:
                Double[][] ZVCurvedOfWDD = EasyExcelUtil.readTable("1乌东德基本特性曲线",1);
                Double[][] ZVCurvedOfBHT = EasyExcelUtil.readTable("1白鹤滩基本特性曲线",1);
                this.ZVCurvedOfStation.put("ZVCurvedOfWDD",ZVCurvedOfWDD);
                this.ZVCurvedOfStation.put("ZVCurvedOfBHT",ZVCurvedOfBHT);

            case 3:
                Double[][] ZVCurvedOfXLD = EasyExcelUtil.readTable("1溪洛渡基本特性曲线",1);
                Double[][] ZVCurvedOfXJB = EasyExcelUtil.readTable("1向家坝基本特性曲线",1);
                this.ZVCurvedOfStation.put("ZVCurvedOfXLD",ZVCurvedOfXLD);
                this.ZVCurvedOfStation.put("ZVCurvedOfXJB",ZVCurvedOfXJB);

            case 1:
                Double[][] inQFromSX = EasyExcelUtil.readTable("三峡入库",0);
                Double[][] ZVCurvedOfSX = EasyExcelUtil.readTable("1三峡基本特性曲线",1);
                this.ZVCurvedOfStation.put("ZVCurvedOfSX",ZVCurvedOfSX);
            default:

        }
    }


    /**
     * 目标值计算
     * 目标1：三峡最高调洪水位最低
     * 目标2：每个库的最大下泄流量最小（目前只考虑向家坝和三峡吧）
     *
     * @param solution 种群
     */
    @Override
    public void evaluate(DoubleSolution solution) {

    }
}
