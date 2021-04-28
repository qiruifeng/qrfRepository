package jmetal.problem;

import entity.Result;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;
import until.EasyExcelUtil;

import java.util.*;

import static until.CalculateUtil.*;
import static until.CalculateUtil.getDoubleArrMax;
import static until.Msjg.riverevolustion;

//public class FloodProMaxPlusSXSingleProblem extends AbstractDoubleProblem implements ConstrainedProblem<DoubleSolution> {
public class FloodProMaxPlusSXSingleProblem extends AbstractDoubleProblem {
    private static final long serialVersionUID = 1L;

    //库容约束，直接给一个溪向预留库容
    private double reserveStorage;//目前考虑的:梯级预留库容的约束，给一个总量；String值为XX(溪向)，WBXX(乌白溪向)
    //    //整个调度时段
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

    public OverallConstraintViolation<DoubleSolution> overallConstraintViolationDegree;
    public NumberOfViolatedConstraints<DoubleSolution> numberOfViolatedConstraints;


    /**
     * 构造器1：设置时段个数和水库个数
     * 决策变量的个数就是两者相乘
     *
     * @param xNum       时段个数
     * @param stationNum 水库个数
     * @param levelStart 每个水库的起调水位，按照乌白溪向三排列
     */
    public FloodProMaxPlusSXSingleProblem(int xNum, int stationNum, double[] levelStart, int[] T) {

        this.stationNum = stationNum;
        this.levelStart = levelStart;
        this.T = T;
        this.reserveStorage = reserveStorage;
        this.period = period;

        initData();
        setNumberOfVariables(xNum * stationNum);
        if (stationNum == 1) {
            setNumberOfObjectives(2);
            setNumberOfConstraints(xNum * stationNum);
        } else {//目前就是溪向三
            setNumberOfObjectives(2);
            //设置约束。预留库容约束
            setNumberOfConstraints(period[1] - period[0] + 1);
        }


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
//                    Math.min(600.0, 560.0 + (i - xNum * 2 + 1) * 5);
                    upperLimit.add(Math.min(600.0, 560.0 + (i - xNum * 2 + 1) * 5));//溪洛渡特征水位
                }
                for (int i = xNum; i < xNum * 2; i++) {
                    lowerLimit.add(370.0);
                    upperLimit.add(Math.min(380.0, 370.0 + (i - xNum * 2 + 1) * 4));//向家坝特征水位
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

        overallConstraintViolationDegree = new OverallConstraintViolation<DoubleSolution>();
        numberOfViolatedConstraints = new NumberOfViolatedConstraints<DoubleSolution>();
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
        int xNum = getNumberOfVariables() / this.stationNum;


        if (this.stationNum == 1) {//三峡单库的情况

            HashMap<Integer, Double> map = new HashMap<>();

            double[] Z = new double[xNum];//把水位当作决策变量
            for (int i = 0; i < xNum; i++) {
                Z[i] = solution.getVariableValue(i);
            }
            //记录一个超过约束的值
            double cons = 0;

            Result resultSX = getProcess("SX", levelStart[0], inQFromStation.get("SX"), Z);
            double[] Znew = resultSX.getZnew();
            double[] Qnew = resultSX.getQout();
            double Zmax = getDoubleArrMax(Znew);
            double Qmax = getDoubleArrMax(Qnew);
            double Qmin = getDoubleArrMin(Qnew);
            double pingfanghe = getPingFangHe(Qnew);
            Map<Integer, Double> map1 = resultSX.getIntegerDoubleMap();

            for (Map.Entry<Integer, Double> entry : map1.entrySet()) {
                solution.setVariableValue(entry.getKey(), entry.getValue());
            }
            solution.setObjective(0, Zmax);
            solution.setObjective(1, pingfanghe);

            solution.setAttribute("Qmax", Qmax);
            solution.setAttribute("Qmin", Qmin);
        }

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
                double[][] inQFromSXOrigin = EasyExcelUtil.readTable("入库数据", 7, T[0], T[1]);
                double[] inQFromSX = inQFromSXOrigin[3];
                this.inQFromStation.put("SX", inQFromSX);
                break;
            }

            default:
                System.out.println("水库个数只能是1，3，5");

        }
        System.out.println("输入初始化完毕");

    }


    //每个水库的演算过程，输入，水位过程即可；返回流量过程

    /**
     * @param stationName 水库名的首字母大写
     * @param levelStart  当前水库的起调水位
     * @param Qin         当前水库的入流
     * @param Z           当前水库的水位过程，决策变量
     * @return 返回的Result这个类包括出库流量过程，新的水位过程，违反约束的位置和调整后的值
     */

    // private Result getProcess(String stationName, double levelStart, double[] Qin, double[] Z, double[] ZPre, double reserveStorage) {
    private Result getProcess(String stationName, double levelStart, double[] Qin, double[] Z) {
        int xNum = getNumberOfVariables() / this.stationNum;

        Result result = new Result();

        double[] Znew = new double[xNum];
        Map<Integer, Double> map = new HashMap<>();

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
        //尾水位
        double[] Zwei = new double[xNum];
        //水头
        double[] detaH = new double[xNum];
        //时段发电量
        double[] iPower = new double[xNum];
        //发电量
        double power = 0.0;

//        //上游水位插库容
//        PolynomialSplineFunction zvCurve = ZVCurvedOfStation.get(stationName);
//        //库容插水位
//        PolynomialSplineFunction vzCurve = VZCurvedOfStation.get(stationName);
//        //上游水位插最大下泄流量
//        PolynomialSplineFunction zOutCurve = ZOutQOfStation.get(stationName);

        V[0] = ZVCurvedOfStation.get(stationName).value(levelStart);

        for (int i = 0; i < xNum; i++) {
            if (i == 0) {
                Zup[i] = (levelStart + Z[i]) / 2;
            } else {
                Zup[i] = (Z[i] + Z[i - 1]) / 2;
            }

            //根据时刻的水位插出库容
            V[i + 1] = ZVCurvedOfStation.get(stationName).value(Z[i]);
            //计算时段库容变化量
            detaV[i] = V[i + 1] - V[i];
            // 计算时段流量变化量
            detaQ[i] = detaV[i] * Math.pow(10, 8) / AvT / 3600;

            //时段下泄流量
            Qout[i] = Qin[i] - detaQ[i];

            //当前水位最大下泄流量
            double maxQ = ZOutQOfStation.get(stationName).value(Zup[i]);

            if (Z[i] > 145 && Z[i] < 171) maxQ = 55000;
            if (Z[i] >= 171) maxQ = 76000;


            //按照预想出力的
            double minQ = 0.0;
            switch (stationName) {
                case "XLD":
                    minQ = 1200.0;
                    break;
                case "XJB":
                    minQ = 1200.0;
                    break;
                case "SX":
                    minQ = 2000.0;
                    break;
            }

            if (Qout[i] > maxQ) {
                Qout[i] = maxQ;
                detaQ[i] = Qin[i] - Qout[i];
                detaV[i] = detaQ[i] * 3600 * AvT / Math.pow(10, 8);
                V[i + 1] = detaV[i] + V[i];
                if (V[i + 1] > 393.0) {
                    V[i + 1] = 393;
                }
                if (V[i + 1] < 171.5) {
                    V[i + 1] = 171.5;
                }
                Z[i] = VZCurvedOfStation.get(stationName).value(V[i + 1]);
                map.put(i, Z[i]);
            }
            if (Qout[i] < minQ) {
                Qout[i] = minQ;
                detaQ[i] = Qin[i] - Qout[i];
                detaV[i] = detaQ[i] * 3600 * AvT / Math.pow(10, 8);
                V[i + 1] = detaV[i] + V[i];
                if (V[i + 1] > 393.0) {
                    V[i + 1] = 393;
                }
                if (V[i + 1] < 171.5) {
                    V[i + 1] = 171.5;
                }
                Z[i] = VZCurvedOfStation.get(stationName).value(V[i + 1]);
                map.put(i, Z[i]);
            }

            if (i > 0 && Z[i] - Z[i - 1] > 5) {
                Z[i] = Z[i - 1] + 5;
                V[i + 1] = ZVCurvedOfStation.get(stationName).value(Z[i]);
                map.put(i, Z[i]);
            }
            if (i > 0 && Z[i] - Z[i - 1] < -5) {
                Z[i] = Z[i - 1] - 5;
                V[i + 1] = ZVCurvedOfStation.get(stationName).value(Z[i]);
                map.put(i, Z[i]);
            }

            detaV[i] = V[i + 1] - V[i];
            detaQ[i] = detaV[i] * Math.pow(10, 8) / AvT / 3600;
            Qout[i] = Qin[i] - detaQ[i];

            Znew[i] = Z[i];
        }


        result.setQout(Qout);
        result.setZnew(Znew);
        result.setPower(power);
        result.setIntegerDoubleMap(map);

        return result;
    }


}

