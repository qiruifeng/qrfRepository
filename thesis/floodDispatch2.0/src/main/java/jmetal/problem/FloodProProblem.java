package jmetal.problem;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import until.EasyExcelUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static until.Msjg.riverevolustion;

public class FloodProProblem extends AbstractDoubleProblem {

    private static final long serialVersionUID = 1L;

    //约束
    private HashMap<String, double[]> constraints;//目前考虑的：时段的水库约束
    //水库个数，单库或者3库或者5库
    private int stationNum;
    //对应控制站点的流量,K-站点名称，V-流量数组
    private HashMap<String, double[]> inQFromStation = new HashMap<>();
    //水库的水位库容曲线,K-水库名称，V-水位库容曲线，0-水位，1-库容
    private HashMap<String, double[][]> ZVCurvedOfStation = new HashMap<>();
    //水库下泄能力曲线，K-水库名称，V-下泄能力曲线，0-水位，1-下泄能力
    private HashMap<String, double[][]> ZOutQOfStation = new HashMap<>();
    //每个水库的起调水位，按照乌白溪向三排列
    private double[] levelStart;


//    public static void main(String[] args) {
//        FloodProProblem a = new FloodProProblem(122, 5);
//    }

    /**
     * 构造器1：设置时段个数和水库个数
     * 决策变量的个数就是两者相乘
     *
     * @param xNum       时段个数
     * @param stationNum 水库个数
     * @param levelStart 每个水库的起调水位，按照乌白溪向三排列
     */
    public FloodProProblem(int xNum, int stationNum, double[] levelStart) {

        this.stationNum = stationNum;
        this.levelStart = levelStart;
        initData();

        setNumberOfVariables(xNum * stationNum);
        setNumberOfObjectives(2);
        setNumberOfConstraints(0);
        List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables());
        List<Double> upperLimit = new ArrayList<>(getNumberOfVariables());

        //针对不同水库设置不同的水位限制，汛限水位和防洪高水位，三向溪白乌


        switch (stationNum) {
            case 5:
                for (int i = xNum * 4; i < xNum * 5; i++) {
                    lowerLimit.add(952.0);
                    upperLimit.add(975.0);//乌东德特征水位
                }
                for (int i = xNum * 3; i < xNum * 4; i++) {
                    lowerLimit.add(785.0);
                    upperLimit.add(825.0);//白鹤滩特征水位
                }
            case 3:
                for (int i = xNum * 2; i < xNum * 3; i++) {
                    lowerLimit.add(560.0);
                    upperLimit.add(600.0);//溪洛渡特征水位
                }
                for (int i = xNum; i < xNum * 2; i++) {
                    lowerLimit.add(370.0);
                    upperLimit.add(380.0);//向家坝特征水位
                }
            case 1:
                for (int i = 0; i < xNum; i++) {
                    lowerLimit.add(145.0);
                    upperLimit.add(175.0);//三峡特征水位
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

        //水位库容曲线的初始化
        switch (this.stationNum) {
            case 5:
                double[][] ZVCurvedOfWDD = EasyExcelUtil.readTable("1乌东德基本特性曲线", 0);
                double[][] ZVCurvedOfBHT = EasyExcelUtil.readTable("1白鹤滩基本特性曲线", 0);

                double[][] ZOutCapacityOfWDD = EasyExcelUtil.readTable("泄流能力曲线", 4);
                double[][] ZOutCapacityOfBHT = EasyExcelUtil.readTable("泄流能力曲线", 3);

                this.ZVCurvedOfStation.put("WDD", ZVCurvedOfWDD);
                this.ZVCurvedOfStation.put("BHT", ZVCurvedOfBHT);
                this.ZOutQOfStation.put("WDD", ZOutCapacityOfWDD);
                this.ZOutQOfStation.put("BHT", ZOutCapacityOfBHT);
            case 3:
                double[][] ZVCurvedOfXLD = EasyExcelUtil.readTable("1溪洛渡基本特性曲线", 0);
                double[][] ZVCurvedOfXJB = EasyExcelUtil.readTable("1向家坝基本特性曲线", 0);

                double[][] ZOutCapacityOfXLD = EasyExcelUtil.readTable("泄流能力曲线", 1);
                double[][] ZOutCapacityOfXJB = EasyExcelUtil.readTable("泄流能力曲线", 2);

                this.ZVCurvedOfStation.put("XLD", ZVCurvedOfXLD);
                this.ZVCurvedOfStation.put("XJB", ZVCurvedOfXJB);
                this.ZOutQOfStation.put("XLD", ZOutCapacityOfXLD);
                this.ZOutQOfStation.put("XJB", ZOutCapacityOfXJB);
            case 1:
                double[][] ZVCurvedOfSX = EasyExcelUtil.readTable("1三峡基本特性曲线", 0);
                double[][] ZOutCapacityOfSX = EasyExcelUtil.readTable("泄流能力曲线", 0);

                ZVCurvedOfStation.put("SX", ZVCurvedOfSX);
                ZOutQOfStation.put("SX", ZOutCapacityOfSX);
                break;
            default:
                System.out.println("水库个数只能是1，3，5");
        }
        System.out.println("水位库容曲线初始化完毕");

        //模型输入
        switch (this.stationNum) {
            case 1:
                double[][] inQFromSXOrigin = EasyExcelUtil.readTable("入库数据", 0);
                double[] inQFromSX = inQFromSXOrigin[2];
                this.inQFromStation.put("SX", inQFromSX);
                break;
            case 3:
                double[][] inQFromXLDOrigin = EasyExcelUtil.readTable("入库数据", 1);
                double[] inQFromXLD = inQFromXLDOrigin[2];
                double[][] inQFromQUJIAN3Origin = EasyExcelUtil.readTable("入库数据", 3);
                double[] inQFromQUJIAN3 = inQFromQUJIAN3Origin[0];
                this.inQFromStation.put("XLD", inQFromXLD);
                this.inQFromStation.put("QUJIAN3", inQFromQUJIAN3);
                break;
            case 5:
//                Double[][] inQFromWDDOrigin = EasyExcelUtil.readTable("入库数据",0);
//                Double[][] inQFromQUJIAN5Origin = EasyExcelUtil.readTable("入库数据",0);
                break;
            default:
                System.out.println("水库个数只能是1，3，5");

        }
        System.out.println("输入初始化完毕");

    }


    /**
     * 目标值计算
     * 目标1：三峡最高调洪水位最低
     * 目标2：每个库的最大下泄流量最小（目前只考虑三峡吧）
     * 考虑目标3：梯级发电量最小
     *
     * @param solution 种群
     */
    @Override
    public void evaluate(DoubleSolution solution) {
        int xNum = getNumberOfVariables();

        double[] Z = new double[xNum];//把水位当作决策变量
        for (int i = 0; i < xNum; i++) {
            Z[i] = solution.getVariableValue(i);
        }

        if (this.stationNum == 1) {//三峡单库的情况

            //记录超出约束的时段个数
            int consNum = 0;
            //记录一个超过约束的值
            double cons = 0;

            // 时段时长,单位：小时
            final int AvT = 24;
            // 水库平均水位,单位m
            double[] Zup = new double[xNum];
            // 时刻库容(10^8m3)
            double[] V = new double[xNum + 1];
            // 时段库容变化量(10^8m3)
            double[] detaV = new double[xNum];
            // 下泄流量(m3/s)
            double[] Qout = new double[xNum];
            // 时段流量变化量(m3/s),
            double[] detaQ = new double[xNum];

            SplineInterpolator splineInterpolator = new SplineInterpolator();//样条差值

            //上游水位插库容
            PolynomialSplineFunction zvCurve = splineInterpolator.interpolate(ZVCurvedOfStation.get("SX")[0], ZVCurvedOfStation.get("SX")[1]);
            //上游水位插最大下泄流量
            PolynomialSplineFunction zOutCurve = splineInterpolator.interpolate(ZOutQOfStation.get("SX")[0], ZOutQOfStation.get("SX")[1]);

            V[0] = zvCurve.value(this.levelStart[0]);//三峡的起调水位
            double Zmax = 0.0; // 记录最高水位
            double Qmax = 0.0; // 记录最大下泄流量

            for (int i = 0; i < Z.length; i++) {
                // 计算时段平均水位
                if (i == 0) {
                    Zup[i] = (this.levelStart[0] + Z[i]) / 2;
                } else {
                    Zup[i] = (Z[i] + Z[i - 1]) / 2;
                }
                // 根据时刻的水位插出时刻的库容
                V[i + 1] = zvCurve.value(Z[i]);
                // 计算时段库容变化量
                detaV[i] = V[i + 1] - V[i];
                // 计算时段流量变化量
                detaQ[i] = detaV[i] * Math.pow(10, 8) / AvT / 3600;

                //时段下泄流量
                Qout[i] = inQFromStation.get("SX")[i] - detaQ[i];
                // 时段平均水位对应最大泄流能力
                double ZoutCapacity = zOutCurve.value(Zup[i]);

                //处理流量约束
                double controlQmax = 76000.0;//三峡下泄量最大为76000.0
                if (Zup[i] <= 171) controlQmax = 55000;//当时段平均水位小于171时，
                double maxQ = Math.min(ZoutCapacity, controlQmax);//当水位大于145小于等于171时，按照控制和最大下泄能力下泄

                double minQ = 20000;//当水位高于145时，最小下泄流量为20000
                if (Zup[i] <= 145.0) minQ = inQFromStation.get("SX")[i];//水位小于等于145时，按照来水下泄

//                if (Qout[i] > maxQ || Qout[i] < minQ) consNum++;
                if (Qout[i] > maxQ) {
                    cons += (Qout[i] - maxQ) / maxQ;


                    consNum = consNum + 1;
                } else if (Qout[i] < minQ) {
                    cons += (minQ - Qout[i]) / (maxQ - minQ);
                    consNum++;
                }

                if (Z[i] > Zmax) Zmax = Z[i];
                if (Qout[i] > Qmax) Qmax = Qout[i];
            }
            // 目标
            solution.setObjective(0, Zmax);
            solution.setObjective(1, Qmax - cons);

            solution.setAttribute("overCons", (double) consNum);
        }

        if (this.stationNum == 3) {//溪洛渡向家坝三峡
            //记录超出约束的时段个数
            int consNum = 0;

            //记录一个超过约束的值
            double cons = 0;

            // 时段时长,单位：小时
            final int AvT = 24;

            // 水库平均水位,单位m
            double[] Zup = new double[xNum];

            // 时刻库容(10^8m3)
            double[] V = new double[xNum + 1];

            // 时段库容变化量(10^8m3)
            double[] detaV = new double[xNum];

            // 下泄流量(m3/s)
            double[] Qout = new double[xNum];

            // 时段流量变化量(m3/s),
            double[] detaQ = new double[xNum];

            SplineInterpolator splineInterpolator = new SplineInterpolator();//样条差值

            //先把溪洛渡的入流用马斯京根演进到三峡，求出向三区间的入流
            double[] inQOfXLD = inQFromStation.get("XLD");
            double[] inQOfXJB = inQOfXLD;
            double[] inQOfSXFromMSJG = riverevolustion(46, 0.37, inQOfXJB, "日");
            double[] QUJIAN = new double[inQOfXLD.length];
            for (int i = 0; i < QUJIAN.length; i++) {
                QUJIAN[i] = inQFromStation.get("SX")[i] - inQOfSXFromMSJG[i];
            }

            //当三库时，决策变量的0-121是溪洛渡的水位，122-243是向家坝水位，244-365为三峡水位

            //先算溪洛渡
            //溪洛渡水位序列
            double[] Vxld = new double[xNum / this.stationNum + 1];

            // 上游水位插库容
            PolynomialSplineFunction zvCurveOfXLD = splineInterpolator.interpolate(ZVCurvedOfStation.get("XLD")[0], ZVCurvedOfStation.get("XLD")[1]);
            // 上游水位插最大下泄流量
            PolynomialSplineFunction zOutCurveOfXLD = splineInterpolator.interpolate(ZOutQOfStation.get("XLD")[0], ZVCurvedOfStation.get("XLD")[1]);

            Vxld[0] = zvCurveOfXLD.value(levelStart[0]);
            double ZXLDmax = 0.0;//记录最高水位
            double QXLDMAX = 0.0;//记录最大流量
            for (int i = 0; i < 122; i++) {
                if (i == 0) {
                    Zup[i] = (levelStart[0] + Z[i]) / 2;
                } else {
                    Zup[i] = (Z[i] + Z[i - 1]) / 2;
                }

                //根据时刻的水位插出库容
                V[i + 1] = zvCurveOfXLD.value(Z[i]);
                //计算时段库容变化量
                detaV[i] = V[i + 1] - V[i];
                // 计算时段流量变化量
                detaQ[i] = detaV[i] * Math.pow(10, 8) / AvT / 3600;

                //时段下泄流量
                Qout[i] = inQFromStation.get("XLD")[i] - detaQ[i];
                // 时段平均水位对应最大泄流能力
                double ZoutCapacityOfXLD = zOutCurveOfXLD.value(Zup[i]);

                //处理流量约束，下泄量不能超过最大泄流约束
                double maxQXLD = ZoutCapacityOfXLD;
                double minQXLD = 0;

            }


        }


    }
}
