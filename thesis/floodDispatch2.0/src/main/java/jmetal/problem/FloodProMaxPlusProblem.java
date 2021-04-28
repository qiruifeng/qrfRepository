package jmetal.problem;

import entity.Result;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;
import until.EasyExcelUtil;

import java.util.*;

import static until.CalculateUtil.getDoubleArrMax;
import static until.CalculateUtil.getPingFangHe;
import static until.Msjg.riverevolustion;

/***
 * 把预留库容当约束
 */
public class FloodProMaxPlusProblem extends AbstractDoubleProblem implements ConstrainedProblem<DoubleSolution> {

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

    public OverallConstraintViolation<DoubleSolution> overallConstraintViolationDegree;
    public NumberOfViolatedConstraints<DoubleSolution> numberOfViolatedConstraints;


    /**
     * 构造器1：设置时段个数和水库个数
     * 决策变量的个数就是两者相乘
     *
     * @param xNum       时段个数
     * @param stationNum 水库个数
     * @param levelStart 每个水库的起调水位，按照乌白溪向三排列
     * @param period
     */
    public FloodProMaxPlusProblem(int xNum, int stationNum, double[] levelStart, int[] T, double reserveStorage, int[] period) {

        this.stationNum = stationNum;
        this.levelStart = levelStart;
        this.T = T;
        this.reserveStorage = reserveStorage;
        this.period = period;

        initData();
        setNumberOfVariables(xNum * stationNum);
        if (stationNum == 1) {
            setNumberOfObjectives(2);
            setNumberOfConstraints(0);
        } else {//目前就是溪向三
            setNumberOfObjectives(2);
            //设置约束。预留库容约束
            setNumberOfConstraints(period[1] - period[0]);
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
                    upperLimit.add(Math.min(380.0, 370.0 + (i - xNum + 1) * 4));//向家坝特征水位
                }
            case 1:
                for (int i = 0; i < xNum; i++) {
                    lowerLimit.add(145.0);
                    upperLimit.add(Math.min(175, 145.0 + (i + 1) * 5));//三峡特征水位
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


        if (this.stationNum == 3) {

            double[] Zxld = new double[xNum];
            for (int i = 0; i < xNum; i++) {
                Zxld[i] = solution.getVariableValue(i);
            }
            double[] Zxjb = new double[xNum];
            for (int i = 0; i < xNum; i++) {
                Zxjb[i] = solution.getVariableValue(xNum + i);
            }
            double[] Zsx = new double[xNum];
            for (int i = 0; i < xNum; i++) {
                Zsx[i] = solution.getVariableValue(xNum * 2 + i);
            }

            //先算溪洛渡
            Result resultXLD = getProcess("XLD", levelStart[0], inQFromStation.get("XLD"), Zxld);
            double[] QoutXLD = resultXLD.getQout();
            double[] ZnewXLD = resultXLD.getZnew();
            double powerXLD = resultXLD.getPower();
            Map<Integer, Double> changeDataXLD = resultXLD.getIntegerDoubleMap();
            //遍历这个map设置种群
            for (Map.Entry<Integer, Double> entry : changeDataXLD.entrySet()) {
                solution.setVariableValue(entry.getKey(), entry.getValue());
            }

            //再算向家坝,向家坝的入库就是溪洛渡的出库
            Result resultXJB = getProcess("XJB", levelStart[1], QoutXLD, Zxjb);
            double[] QoutXJB = resultXJB.getQout();
            double[] ZnewXJB = resultXJB.getZnew();
            double powerXJB = resultXJB.getPower();
            Map<Integer, Double> changeDataXJB = resultXJB.getIntegerDoubleMap();
            //遍历这个map设置种群
            for (Map.Entry<Integer, Double> entry : changeDataXJB.entrySet()) {
                solution.setVariableValue(entry.getKey() + xNum, entry.getValue());
            }

            //先用设计洪水把溪洛渡的入流用马斯京根演进到三峡，求出向三区间的入流
            double[] inQOfXLD = inQFromStation.get("XLD");
            double[] inQOfXJB = inQOfXLD;
            double[] inQOfSXFromMSJG = riverevolustion(46, 0.37, inQOfXJB, "日");
            double[] QUJIAN = new double[inQOfXLD.length];
            for (int i = 0; i < QUJIAN.length; i++) {
                QUJIAN[i] = inQFromStation.get("SX")[i] - inQOfSXFromMSJG[i];
            }

            //算到三峡时，入库就是向家坝出库演进到三峡的流量加上区间的流量
            double[] MSJG = riverevolustion(46, 0.37, QoutXJB, "日");
            double[] QinSX = new double[xNum];
            for (int i = 0; i < xNum; i++) {
                QinSX[i] = MSJG[i] + QUJIAN[i];
            }
            Result resultSX = getProcess("SX", levelStart[2], QinSX, Zsx);
            double[] QoutSX = resultSX.getQout();
            double[] ZnewSX = resultSX.getZnew();
            double powerSX = resultSX.getPower();
            Map<Integer, Double> changeDataSX = resultSX.getIntegerDoubleMap();
            //遍历这个map设置种群
            for (Map.Entry<Integer, Double> entry : changeDataSX.entrySet()) {
                solution.setVariableValue(entry.getKey() + xNum * 2, entry.getValue());
            }
            //目标1，向家坝出库流量平方和最小
            //目标2，三峡坝前最高水位最低
            //目标3，三峡出库流量平方和最小
//            double object1 = getPingFangHe(QoutXJB);//溪洛渡出库流量平方和最小
//            double object1 = getDoubleArrMax(ZnewXLD);//溪洛渡最高水位最低
//            double object2 = getDoubleArrMax(ZnewSX);//三峡最高水位最低
//            double object3 = getPingFangHe(QoutSX);//三峡出库流量平方和最小
//            solution.setObjective(0, object1);
//            solution.setObjective(1, object2);
//            solution.setObjective(2, object3);

            //三库连调
//            double target1 = powerXLD + powerXJB + powerSX;
//            double target2 = getPingFangHe(QoutSX);
//            double q = getDoubleArrMax(QoutSX);
//            solution.setObjective(0, target1);
//            solution.setObjective(1, target2);
//            solution.setAttribute("Qmax", q);

            //溪向联合调度
            double target1 = 1/(powerXLD + powerXJB);
            double target2 = getPingFangHe(QoutXJB);
            double q = getDoubleArrMax(QoutXJB);
            solution.setObjective(0, target1);
            solution.setObjective(1, q);
            solution.setAttribute("Qmax", q);
        }


    }

    @Override
    public void evaluateConstraints(DoubleSolution solution) {

        int xNum = solution.getNumberOfVariables() / this.stationNum;

        double[] Z = new double[solution.getNumberOfVariables()];
        double[] constraint = new double[getNumberOfConstraints()];
        for (int i = 0; i < solution.getNumberOfVariables(); i++) {
            Z[i] = solution.getVariableValue(i);
        }


        double Z_XLD = 0.0;//xld目前水位
        double V_XLD = 0.0;//xld目前库容
        double V_XLD_left = 0.0;//xld剩余库容
        double V_XLD_FHG = ZVCurvedOfStation.get("XLD").value(600.0);//xld防洪高对应库容

        double Z_XJB = 0.0;//XJB目前水位
        double V_XJB = 0.0;//XJB目前库容
        double V_XJB_left = 0.0;//XJB剩余库容
        double V_XJB_FHG = ZVCurvedOfStation.get("XJB").value(370.0);//XJB防洪高对应库容


        //预留防洪库容约束
        for (int i = 0; i < getNumberOfConstraints(); i++) {
            Z_XLD = Z[period[0] - T[0] + i];
            V_XLD = ZVCurvedOfStation.get("XLD").value(Z_XLD);
            V_XLD_left = V_XLD_FHG - V_XLD;

            Z_XJB = Z[period[0] - T[0] + i + xNum];
            V_XJB = ZVCurvedOfStation.get("XJB").value(Z_XJB);
            V_XJB_left = V_XJB_FHG - V_XJB;

            constraint[i] = reserveStorage - (V_XLD_left + V_XJB_left);

        }

        //处理超约束情况
        double overallConstraintViolation = 0.0;
        int violatedConstraints = 0;
        for (int i = 0; i < getNumberOfConstraints(); i++) {
            if (constraint[i] < 0.0) {
                overallConstraintViolation += constraint[i];
                violatedConstraints++;
            }
        }

        overallConstraintViolationDegree.setAttribute(solution, overallConstraintViolation);
        numberOfViolatedConstraints.setAttribute(solution, violatedConstraints);

    }

    /**
     *
     * @param stationName 电站名字
     * @param Z           水位过程
     * @return
     */
//    public double getStationPower(String stationName,double[] Z){
//
//    }


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
                double[][] inQFromSXOrigin = EasyExcelUtil.readTable("入库数据", 0, T[0], T[1]);
                double[] inQFromSX = inQFromSXOrigin[2];
                this.inQFromStation.put("SX", inQFromSX);
                break;
            }
            case 3: {
                double[][] inQFromXLDOrigin = EasyExcelUtil.readTable("入库数据", 0, T[0], T[1]);
                double[][] inQFromSXOrigin = EasyExcelUtil.readTable("入库数据", 0, T[0], T[1]);

                double[] inQFromXLD = inQFromXLDOrigin[3];
                double[] inQFromSX = inQFromSXOrigin[2];

                this.inQFromStation.put("XLD", inQFromXLD);
                this.inQFromStation.put("SX", inQFromSX);
                break;
            }

            case 5:
//                Double[][] inQFromWDDOrigin = EasyExcelUtil.readTable("入库数据",0);
//                Double[][] inQFromQUJIAN5Origin = EasyExcelUtil.readTable("入库数据",0);
                break;
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

            //当前水位最大下泄流量,这里最大不能按照下泄能力设置
            double maxQ = ZOutQOfStation.get(stationName).value(Zup[i]);
            if (stationName.equals("SX")) {
                if (Zup[i] > 145 && Zup[i] < 171) {
                    maxQ = Math.min(55000, maxQ);
                }
                if (Zup[i] >= 171) {
                    maxQ = Math.min(76000, maxQ);
                }
            }
            if (stationName.equals("XLD") || stationName.equals("XJB")) {
                maxQ = Math.min(50000, maxQ);
            }


            //预想出力最小值对应的最小水头，在对应一个尾水位的最大值
            double ZweiMax = 0.0;
            switch (stationName) {
                case "SX":
                    ZweiMax = Math.min(Zup[i] - 61.0, 82.86);
                    break;
                case "XJB":
                    ZweiMax = Math.min(Zup[i] - 82.5, 295);
                    break;
                case "XLD":
                    ZweiMax = Math.min(Zup[i] - 154.6, 407.56);
                    break;
            }
//            System.out.println("i:" + i + " " + stationName + " ZweiMax" + ZweiMax + " Zup[i]" + Zup[i]);
            //根据这个ZweiMax反插值一个出流量的最大值
            double Q反插值 = this.ZOutQOutOfStation.get(stationName).value(ZweiMax);
            maxQ = Math.min(maxQ, Q反插值);

            double minQ = 0.0;
            switch (stationName) {
                case "XLD":
                    minQ = 1200.0;
                    break;
                case "XJB":
                    minQ = 1200.0;
                    break;
                case "SX":
                    if (Z[i] <= 171) minQ = 45000.0;
                    if (Z[i] > 171) minQ = 70000.0;
                    break;
            }

            if (Qout[i] > maxQ) {
                Qout[i] = maxQ;
                detaQ[i] = Qin[i] - Qout[i];
                detaV[i] = detaQ[i] * 3600 * AvT / Math.pow(10, 8);
                V[i + 1] = detaV[i] + V[i];
                Z[i] = VZCurvedOfStation.get(stationName).value(V[i + 1]);
                map.put(i, Z[i]);
            }
            if (Qout[i] < minQ) {
                Qout[i] = minQ;
                detaQ[i] = Qin[i] - Qout[i];
                detaV[i] = detaQ[i] * 3600 * AvT / Math.pow(10, 8);
                V[i + 1] = detaV[i] + V[i];
                Z[i] = VZCurvedOfStation.get(stationName).value(V[i + 1]);
                map.put(i, Z[i]);
            }


            //在判断是否超过水位约束
            Znew[i] = Z[i];


            //计算发电量
            Zwei[i] = this.QOutZOutOfStation.get(stationName).value(Qout[i]);
            detaH[i] = Zup[i] - Zwei[i];
            /**测试***/
//            {
//                System.out.println("测试开始");
//                double detaHmin = 0.0;
//                double detaHmax = 0.0;
//
//                switch (stationName) {
//                    case "XLD":
//                        detaHmin = 154.0;
//                        detaHmax = 228;
//                        break;
//                    case "XJB":
//                        detaHmin = 83;
//                        detaHmax = 228;
//                        break;
//                    case "SX":
//                        detaHmin = 61;
//                        detaHmax = 113;
//                        break;
//                }
//
//                if (detaH[i] < detaHmin || detaH[i] > detaHmax) {
//                    if (i == 0) {
//                        System.out.println("时段初水位：" + levelStart + " 时段末水位：" + Znew[i] + " 入流：" + Qin[i] + " 出流：" + Qout[i]);
//                    } else {
//                        System.out.println("时段初水位：" + Znew[i - 1] + " 时段末水位：" + Znew[i] + " 入流：" + Qin[i] + " 出流：" + Qout[i]);
//                    }
//                }
//                System.out.println("测试结束");
//            }
            /**测试***/
            iPower[i] = 8.8 * Qout[i] * detaH[i];
            double powerMax = this.PowerOfStation.get(stationName).value(detaH[i]);
            if (iPower[i] > powerMax) {
                iPower[i] = powerMax;
            }
            power = power + iPower[i];

        }


        result.setQout(Qout);
        result.setZnew(Znew);
        result.setPower(power);
        result.setIntegerDoubleMap(map);

        return result;
    }
}