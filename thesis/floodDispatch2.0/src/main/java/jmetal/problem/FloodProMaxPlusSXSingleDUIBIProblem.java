package jmetal.problem;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;
import until.EasyExcelUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static until.CalculateUtil.getDoubleArrMax;
import static until.CalculateUtil.getPingFangHe;

public class FloodProMaxPlusSXSingleDUIBIProblem extends AbstractDoubleProblem {
    private static final long serialVersionUID = 1L;

    //库容约束，直接给一个溪向预留库容
    private double reserveStorage;//目前考虑的:梯级预留库容的约束，给一个总量；String值为XX(溪向)，WBXX(乌白溪向)
    //整个调度时段
    private int[] T;
    //库容约束的时段
    private int[] period;
    //水库个数，单库或者3库或者5库
    private int stationNum;
    //对应控制站点的流量,K-站点名称，V-流量数组
    private HashMap<String, double[]> inQFromStation = new HashMap<>();
    //水库的水位库容曲线,由水位得到库容；K-水库名称，V-水位库容曲线
    private HashMap<String, PolynomialSplineFunction> ZVCurvedOfStation = new HashMap<>();
    //水库的库容水位曲线,由库容得到水位；K-水库名称，V-库容水位曲线
    private HashMap<String, PolynomialSplineFunction> VZCurvedOfStation = new HashMap<>();
    //水库下泄能力曲线，K-水库名称，V-下泄能力曲线，0-水位，1-下泄能力
    private HashMap<String, PolynomialSplineFunction> ZOutQOfStation = new HashMap<>();
    //水库流量尾水位曲线
    private HashMap<String, PolynomialSplineFunction> QOutZOutOfStation = new HashMap<>();
    //尾水位的反插值，即根据尾水位插流量
    private HashMap<String, PolynomialSplineFunction> ZOutQOutOfStation = new HashMap<>();
    //电站预想出力曲线
    private HashMap<String, PolynomialSplineFunction> PowerOfStation = new HashMap<>();
    //每个水库的起调水位，按照乌白溪向三排列
    private double[] levelStart;


    public FloodProMaxPlusSXSingleDUIBIProblem(int xNum, int stationNum, double[] levelStart) {

        this.stationNum = stationNum;
        this.levelStart = levelStart;
        initData();
        setNumberOfVariables(xNum * stationNum);
        if (stationNum == 1) {
            setNumberOfObjectives(2);
        } else {
            setNumberOfObjectives(3);
        }

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
                    upperLimit.add(Math.min(175, 145.0 + i * 5));//三峡特征水位
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

        SplineInterpolator splineInterpolator = new SplineInterpolator();//样条差值

        //水位库容曲线的初始化
        switch (this.stationNum) {
            case 5:
                double[][] ZVCurvedOfwdd = EasyExcelUtil.readTable("1乌东德基本特性曲线", 0);
                PolynomialSplineFunction ZVCurvedOfWDD = splineInterpolator.interpolate(ZVCurvedOfwdd[0], ZVCurvedOfwdd[1]);
                PolynomialSplineFunction VZCurvedOfWDD = splineInterpolator.interpolate(ZVCurvedOfwdd[1], ZVCurvedOfwdd[0]);
                this.ZVCurvedOfStation.put("WDD", ZVCurvedOfWDD);
                this.VZCurvedOfStation.put("WDD", VZCurvedOfWDD);

                double[][] ZVCurvedOfbht = EasyExcelUtil.readTable("1白鹤滩基本特性曲线", 0);
                PolynomialSplineFunction ZVCurvedOfBHT = splineInterpolator.interpolate(ZVCurvedOfbht[0], ZVCurvedOfbht[1]);
                PolynomialSplineFunction VZCurvedOfBHT = splineInterpolator.interpolate(ZVCurvedOfbht[1], ZVCurvedOfbht[0]);
                this.ZVCurvedOfStation.put("BHT", ZVCurvedOfBHT);
                this.ZVCurvedOfStation.put("BHT", VZCurvedOfBHT);

                double[][] ZOutCapacityOfwdd = EasyExcelUtil.readTable("泄流能力曲线", 4);
                PolynomialSplineFunction ZOutCapacityOfWDD = splineInterpolator.interpolate(ZOutCapacityOfwdd[0], ZOutCapacityOfwdd[1]);
                this.ZOutQOfStation.put("WDD", ZOutCapacityOfWDD);

                double[][] ZOutCapacityOfbht = EasyExcelUtil.readTable("泄流能力曲线", 3);
                PolynomialSplineFunction ZOutCapacityOfBHT = splineInterpolator.interpolate(ZOutCapacityOfbht[0], ZOutCapacityOfbht[1]);
                this.ZOutQOfStation.put("BHT", ZOutCapacityOfBHT);
            case 3:
                double[][] ZVCurvedOfxld = EasyExcelUtil.readTable("水位库容曲线", 0);
                PolynomialSplineFunction ZVCurvedOfXLD = splineInterpolator.interpolate(ZVCurvedOfxld[0], ZVCurvedOfxld[1]);
                PolynomialSplineFunction VZCurvedOfXLD = splineInterpolator.interpolate(ZVCurvedOfxld[1], ZVCurvedOfxld[0]);
                this.ZVCurvedOfStation.put("XLD", ZVCurvedOfXLD);
                this.VZCurvedOfStation.put("XLD", VZCurvedOfXLD);

                double[][] ZVCurvedOfxjb = EasyExcelUtil.readTable("水位库容曲线", 1);
                PolynomialSplineFunction ZVCurvedOfXJB = splineInterpolator.interpolate(ZVCurvedOfxjb[0], ZVCurvedOfxjb[1]);
                PolynomialSplineFunction VZCurvedOfXJB = splineInterpolator.interpolate(ZVCurvedOfxjb[1], ZVCurvedOfxjb[0]);
                this.ZVCurvedOfStation.put("XJB", ZVCurvedOfXJB);
                this.VZCurvedOfStation.put("XJB", VZCurvedOfXJB);

                double[][] ZOutCapacityOfxld = EasyExcelUtil.readTable("泄流能力曲线", 0);
                PolynomialSplineFunction ZOutCapacityOfXLD = splineInterpolator.interpolate(ZOutCapacityOfxld[0], ZOutCapacityOfxld[1]);
                this.ZOutQOfStation.put("XLD", ZOutCapacityOfXLD);

                double[][] ZOutCapacityOfxjb = EasyExcelUtil.readTable("泄流能力曲线", 1);
                PolynomialSplineFunction ZOutCapacityOfXJB = splineInterpolator.interpolate(ZOutCapacityOfxjb[0], ZOutCapacityOfxjb[1]);
                this.ZOutQOfStation.put("XJB", ZOutCapacityOfXJB);

                double[][] QOutZOutOfxld = EasyExcelUtil.readTable("尾水位曲线", 0);
                PolynomialSplineFunction QOutZOutOfXLD = splineInterpolator.interpolate(QOutZOutOfxld[0], QOutZOutOfxld[1]);
                PolynomialSplineFunction ZOutQOutOfXLD = splineInterpolator.interpolate(QOutZOutOfxld[1], QOutZOutOfxld[0]);
                this.QOutZOutOfStation.put("XLD", QOutZOutOfXLD);
                this.ZOutQOutOfStation.put("XLD", ZOutQOutOfXLD);

                double[][] QOutZOutOfxjb = EasyExcelUtil.readTable("尾水位曲线", 1);
                PolynomialSplineFunction QOutZOutOfXJB = splineInterpolator.interpolate(QOutZOutOfxjb[0], QOutZOutOfxjb[1]);
                PolynomialSplineFunction ZOutQOutOfXJB = splineInterpolator.interpolate(QOutZOutOfxjb[1], QOutZOutOfxjb[0]);
                this.QOutZOutOfStation.put("XJB", QOutZOutOfXJB);
                this.ZOutQOutOfStation.put("XJB", ZOutQOutOfXJB);


                double[][] PowerOfxld = EasyExcelUtil.readTable("预想出力曲线", 0);
                PolynomialSplineFunction PowerOfXLD = splineInterpolator.interpolate(PowerOfxld[0], PowerOfxld[1]);
                this.PowerOfStation.put("XLD", PowerOfXLD);

                double[][] PowerOfxjb = EasyExcelUtil.readTable("预想出力曲线", 1);
                PolynomialSplineFunction PowerOfXJB = splineInterpolator.interpolate(PowerOfxjb[0], PowerOfxjb[1]);
                this.PowerOfStation.put("XJB", PowerOfXJB);
            case 1:
                double[][] ZVCurvedOfsx = EasyExcelUtil.readTable("水位库容曲线", 2);
                PolynomialSplineFunction ZVCurvedOfSX = splineInterpolator.interpolate(ZVCurvedOfsx[0], ZVCurvedOfsx[1]);
                PolynomialSplineFunction VZCurvedOfSX = splineInterpolator.interpolate(ZVCurvedOfsx[1], ZVCurvedOfsx[0]);
                this.ZVCurvedOfStation.put("SX", ZVCurvedOfSX);
                this.VZCurvedOfStation.put("SX", VZCurvedOfSX);

                double[][] ZOutCapacityOfsx = EasyExcelUtil.readTable("泄流能力曲线", 2);
                PolynomialSplineFunction ZOutCapacityOfSX = splineInterpolator.interpolate(ZOutCapacityOfsx[0], ZOutCapacityOfsx[1]);
                this.ZOutQOfStation.put("SX", ZOutCapacityOfSX);

                double[][] QOutZOutOfsx = EasyExcelUtil.readTable("尾水位曲线", 2);
                PolynomialSplineFunction QOutZOutOfSX = splineInterpolator.interpolate(QOutZOutOfsx[0], QOutZOutOfsx[1]);
                this.QOutZOutOfStation.put("SX", QOutZOutOfSX);

                double[][] ZOutQOutOfsx = EasyExcelUtil.readTable("尾水位曲线", 3);
                PolynomialSplineFunction ZOutQOutOfSX = splineInterpolator.interpolate(ZOutQOutOfsx[1], ZOutQOutOfsx[0]);
                this.ZOutQOutOfStation.put("SX", ZOutQOutOfSX);

                double[][] PowerOfsx = EasyExcelUtil.readTable("预想出力曲线", 2);
                PolynomialSplineFunction PowerOfSX = splineInterpolator.interpolate(PowerOfsx[0], PowerOfsx[1]);
                this.PowerOfStation.put("SX", PowerOfSX);

                break;
            default:
                System.out.println("水库个数只能是1，3，5");
        }
        System.out.println("水位库容曲线及泄流能力曲线初始化完毕");

        //模型输入
        switch (this.stationNum) {
            case 1: {
                double[][] inQFromSXOrigin = EasyExcelUtil.readTable("0典型输入数据日尺度", 4);
                double[] inQFromSX = inQFromSXOrigin[3];
                this.inQFromStation.put("SX", inQFromSX);
                break;
            }
            default:
                System.out.println("水库个数只能是1，3，5");

        }
        System.out.println("输入初始化完毕");

    }

    @Override
    public void evaluate(DoubleSolution solution) {
        int xNum = getNumberOfVariables() / this.stationNum;

        if (this.stationNum == 1) {//三峡单库的情况


            double[] Z = new double[xNum];//把水位当作决策变量
            for (int i = 0; i < xNum; i++) {
                Z[i] = solution.getVariableValue(i);
            }
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

            //上游水位插库容
            PolynomialSplineFunction zvCurve = ZVCurvedOfStation.get("SX");
            //上游水位插最大下泄流量
            PolynomialSplineFunction zOutCurve = ZOutQOfStation.get("SX");

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

                double minQ = 6000;//当水位高于145时，最小下泄流量为20000
//                if (i>26&&i<45)minQ=30000;

                if (Zup[i] <= 145.0) minQ = Math.min(inQFromStation.get("SX")[i], minQ);//水位小于等于145时，按照来水下泄

//                if (Qout[i] > maxQ || Qout[i] < minQ) consNum++;
                if (Qout[i] > maxQ) {
                    Qout[i] = maxQ;
                    detaQ[i] = inQFromStation.get("SX")[i] - Qout[i];
                    detaV[i] = detaQ[i] * 3600 * AvT / Math.pow(10, 8);
                    V[i + 1] = detaV[i] + V[i];
                    Z[i] = VZCurvedOfStation.get("SX").value(V[i + 1]);
//                    cons += (Qout[i] - maxQ) / (maxQ - minQ) * (175 - Z[i]) * (175 - Z[i]);
//                    consNum = consNum++;
//                    map.put(i, Z[i]);
                    solution.setVariableValue(i,Z[i]);
                }
//                if (Qout[i] > 0.0 && Qout[i] < minQ) {
                if (Qout[i] < minQ) {
                    Qout[i] = minQ;
                    detaQ[i] = inQFromStation.get("SX")[i] - Qout[i];
                    detaV[i] = detaQ[i] * 3600 * AvT / Math.pow(10, 8);
                    V[i + 1] = detaV[i] + V[i];
                    Z[i] = VZCurvedOfStation.get("SX").value(V[i + 1]);
//                    cons += (minQ - Qout[i]) / (maxQ - minQ);
//                    consNum++;
                    solution.setVariableValue(i,Z[i]);
                }


//                if (Qout[i] < 0) {
//                    cons += Math.abs(Qout[i]) * Math.abs(Qout[i]) * 10000;
//                    consNum++;
//                }


                if (Z[i] > Zmax) Zmax = Z[i];
                if (Qout[i] > Qmax) Qmax = Qout[i];
            }
            // 目标

            double a = getDoubleArrMax(Qout);
            solution.setObjective(0, Zmax);
            solution.setObjective(1, a);

            solution.setAttribute("Qmax", (double) a);
        }
    }
}
