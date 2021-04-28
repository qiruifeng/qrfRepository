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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static until.CalculateUtil.*;
import static until.Msjg.riverevolustion;

public class 溪向固定库容下_梯级发电量向家坝出库流量平方三峡坝前水位 extends AbstractDoubleProblem implements ConstrainedProblem<DoubleSolution> {
    private static final long serialVersionUID = 1L;

    //整个调度时段
    private int[] T;
    //
    private int sheetIndex;
    //梯级预留库容
    private double reserveStorage;
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


    public 溪向固定库容下_梯级发电量向家坝出库流量平方三峡坝前水位(int[] T, int stationNum, double[] levelStart, double reserveStorage, int sheetIndex) {
        this.stationNum = stationNum;
        this.levelStart = levelStart;
        this.T = T;
        this.reserveStorage = reserveStorage;
        this.sheetIndex = sheetIndex;
        int xNum = T[1] - T[0] + 1;

        initData();
        setNumberOfVariables(xNum * (stationNum - 1));
        setNumberOfObjectives(3);
        setNumberOfConstraints(xNum);
        List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables());
        List<Double> upperLimit = new ArrayList<>(getNumberOfVariables());
        //设置水位廊道
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
                break;
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


    @Override
    public void evaluate(DoubleSolution solution) {
        int xNum = getNumberOfVariables() / (this.stationNum - 1);

        double[] Zxld = new double[xNum];
        for (int i = 0; i < xNum; i++) {
            Zxld[i] = solution.getVariableValue(i);
        }
        double[] Zxjb = new double[xNum];
        for (int i = 0; i < xNum; i++) {
            Zxjb[i] = solution.getVariableValue(xNum + i);
        }

        //先算溪洛渡
        Result resultXLD = getProcess("XLD", levelStart[0], inQFromStation.get("XLD"), Zxld);
        double[] QoutXLD = resultXLD.getQout();
        double[] QpowerXLD = resultXLD.getQpower();
        double[] ZnewXLD = resultXLD.getZnew();
        double powerXLD = resultXLD.getPower();
        System.out.println("powerXLD:" + powerXLD / 100000000);
        Map<Integer, Double> changeDataXLD = resultXLD.getIntegerDoubleMap();
        //遍历这个map设置种群
        for (Map.Entry<Integer, Double> entry : changeDataXLD.entrySet()) {
            solution.setVariableValue(entry.getKey(), entry.getValue());
        }

        //再算向家坝,向家坝的入库就是溪洛渡的出库
        Result resultXJB = getProcess("XJB", levelStart[1], QoutXLD, Zxjb);
        double[] QoutXJB = resultXJB.getQout();
        double[] QpowerXJB = resultXJB.getQpower();
        double[] ZnewXJB = resultXJB.getZnew();
        double powerXJB = resultXJB.getPower();
        System.out.println("powerXJB:" + powerXJB / 100000000);
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

        //再用常规调度算三峡的出库过程和水位过程
        double[] Z_sx = new double[xNum + 1];
        double[] V_sx = new double[xNum + 1];
        double[] detaQ_SX = new double[xNum];
        double[] detaV_SX = new double[xNum];
        double[] QoutSX = new double[xNum];
        Z_sx[0] = 145.0;
        V_sx[0] = this.ZVCurvedOfStation.get("SX").value(145.0);
        for (int i = 0; i < xNum; i++) {
            QoutSX[i] = 三峡常规调度由时段初水位和入库得到出库(Z_sx[i], QinSX[i]);
            detaQ_SX[i] = QinSX[i] - QoutSX[i];
            detaV_SX[i] = detaQ_SX[i] * 3600 * 24 / Math.pow(10, 8);
            V_sx[i + 1] = detaV_SX[i] + V_sx[i];
            Z_sx[i + 1] = this.VZCurvedOfStation.get("SX").value(V_sx[i + 1]);

        }

        //根据两个新的水位过程算预留库容
        double[] V_left = new double[xNum];
        for (int i = 0; i < xNum; i++) {
            double V_left_xld = 0.0;
            double V_left_xjb = 0.0;
            V_left_xld = this.ZVCurvedOfStation.get("XLD").value(600.0) - this.ZVCurvedOfStation.get("XLD").value(ZnewXLD[i]);
            V_left_xjb = this.ZVCurvedOfStation.get("XJB").value(380) - this.ZVCurvedOfStation.get("XJB").value(ZnewXJB[i]);
            V_left[i] = V_left_xld + V_left_xjb;
        }

        //算梯级的水能利用率
        double n = 0.0;
        n = (getSum(QpowerXLD) + getSum(QpowerXJB)) / (getSum(QoutXLD) + getSum(QoutXJB));

        //向家坝出库流量平方和
        double pingfanghe = getPingFangHe(QoutXJB);
        //梯级发电量
        double power = (powerXLD + powerXJB);
        //三峡最高坝前水位
        double Z_sxmax = getDoubleArrMax(Z_sx);

        solution.setObjective(0, 1 / power);//发电量最大
        solution.setObjective(1, pingfanghe);//出库流量平方和最小
        solution.setObjective(2, Z_sxmax);//三峡最高水位最低

        solution.setAttribute("Qmax", getDoubleArrMax(QoutXJB));
        solution.setAttribute("Qmin", getDoubleArrMin(QoutXJB));
        solution.setAttribute("n", n);
        solution.setAttribute("V_left_min", getDoubleArrMin(V_left));
        solution.setAttribute("V_left_max", getDoubleArrMax(V_left));
        solution.setAttribute("power", power / 100000000);

    }

    @Override
    public void evaluateConstraints(DoubleSolution solution) {
        int xNum = solution.getNumberOfVariables() / (this.stationNum - 1);

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

        for (int i = 0; i < getNumberOfConstraints(); i++) {
            Z_XLD = Z[i];
            V_XLD = ZVCurvedOfStation.get("XLD").value(Z_XLD);
            V_XLD_left = V_XLD_FHG - V_XLD;


            Z_XJB = Z[i + xNum];
            V_XJB = ZVCurvedOfStation.get("XJB").value(Z_XJB);
            V_XJB_left = V_XJB_FHG - V_XJB;

            constraint[i] = (V_XLD_left + V_XJB_left) - reserveStorage;
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
                double[][] inQFromXLDOrigin = EasyExcelUtil.readTable("入库数据", sheetIndex, T[0], T[1]);
                double[][] inQFromSXOrigin = EasyExcelUtil.readTable("入库数据", sheetIndex, T[0], T[1]);

                double[] inQFromXLD = inQFromXLDOrigin[2];
                double[] inQFromSX = inQFromSXOrigin[4];

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


    /**
     * 得到调度过程
     *
     * @param stationName
     * @param levelStart
     * @param Qin
     * @param Z
     * @return
     */
    private Result getProcess(String stationName, double levelStart, double[] Qin, double[] Z) {
        int xNum = getNumberOfVariables() / (this.stationNum - 1);

        Result result = new Result();

        double[] Znew = new double[xNum];
        Map<Integer, Double> map = new HashMap<>();

        // 时段时长,单位：小时
        final int AvT = 24;
        // 水库平均水位,单位m
        double[] Zup = new double[xNum];
        // 时刻库容(10^8m3)
        double[] V = new double[xNum + 1];
//        //剩余库容
//        double[] V_left = new double[xNum + 1];
        // 时段库容变化量(10^8m3)
        double[] detaV = new double[xNum];
        // 下泄流量(m3/s)
        double[] Qout = new double[xNum];
        //发电流量
        double[] Qpower = new double[xNum];
        // 时段流量变化量(m3/s),
        double[] detaQ = new double[xNum];
        //尾水位
        double[] Zwei = new double[xNum];
        //水头
        double[] detaH = new double[xNum];
        //时段出力
        double[] iN = new double[xNum];
        //总出力
        double N = 0.0;

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
                switch (stationName) {
                    case "XLD":
                        if (V[i + 1] > 115.738) {
                            V[i + 1] = 115.738;
                        }
                        if (V[i + 1] < 69.23) {
                            V[i + 1] = 69.23;
                        }
                        break;
                    case "XJB": {
                        if (V[i + 1] > 49.767) {
                            V[i + 1] = 49.767;
                        }
                        if (V[i + 1] < 40.736) {
                            V[i + 1] = 40.736;
                        }

                        break;
                    }
                }
                Z[i] = VZCurvedOfStation.get(stationName).value(V[i + 1]);
                map.put(i, Z[i]);

            }
            if (Qout[i] < minQ) {
                Qout[i] = minQ;
                detaQ[i] = Qin[i] - Qout[i];
                detaV[i] = detaQ[i] * 3600 * AvT / Math.pow(10, 8);
                V[i + 1] = detaV[i] + V[i];
                switch (stationName) {
                    case "XLD":
                        if (V[i + 1] > 115.738) {
                            V[i + 1] = 115.738;
                        }
                        if (V[i + 1] < 69.23) {
                            V[i + 1] = 69.23;
                        }
                        break;
                    case "XJB": {
                        if (V[i + 1] > 49.767) {
                            V[i + 1] = 49.767;
                        }
                        if (V[i + 1] < 40.736) {
                            V[i + 1] = 40.736;
                        }

                        break;
                    }
                }
                Z[i] = VZCurvedOfStation.get(stationName).value(V[i + 1]);
                map.put(i, Z[i]);
            }


            if (i > 0 && Z[i] - Z[i - 1] > 4) {
                Z[i] = Z[i - 1] + 4;
                V[i + 1] = ZVCurvedOfStation.get(stationName).value(Z[i]);
                map.put(i, Z[i]);
            }
            if (i > 0 && Z[i] - Z[i - 1] < -4) {
                Z[i] = Z[i - 1] - 4;
                V[i + 1] = ZVCurvedOfStation.get(stationName).value(Z[i]);
                map.put(i, Z[i]);
            }

            detaV[i] = V[i + 1] - V[i];
            detaQ[i] = detaV[i] * Math.pow(10, 8) / AvT / 3600;
            Qout[i] = Qin[i] - detaQ[i];


            Znew[i] = Z[i];


            //计算发电量
            Zwei[i] = this.QOutZOutOfStation.get(stationName).value(Qout[i]);
            detaH[i] = Zup[i] - Zwei[i];

            iN[i] = 8.8 * Qout[i] * detaH[i];
            double NMax = this.PowerOfStation.get(stationName).value(detaH[i]) * 10000;
            double iQpower = NMax / 8.8 / detaH[i];
            if (Qout[i] >= iQpower) {
                Qpower[i] = iQpower;
            } else {
                Qpower[i] = Qout[i];
            }
            if (iN[i] > NMax) {
                iN[i] = NMax;
            }
            N = N + iN[i];

        }

        double power = N * 24;


        result.setQout(Qout);
        result.setQpower(Qpower);
        result.setZnew(Znew);
        result.setPower(power);
        result.setIntegerDoubleMap(map);

        return result;
    }


    private double 三峡常规调度由时段初水位和入库得到出库(double Z_sx, double Qin) {
        double Qout = 0.0;
        if (Z_sx == 145.0) {
            if (Qin < 50000)
                Qout = Qin;
            else {
                Qout = 50000;
            }
        } else if (Z_sx < 158) {
            if (Qin < 50000)
                Qout = Math.min(Qin + (this.ZVCurvedOfStation.get("SX").value(Z_sx) - this.ZVCurvedOfStation.get("SX").value(145)) * 100000000 / (24 * 3600), 50000);
            else {
                Qout = 50000;
            }
        } else if (Z_sx < 171) {
            Qout = 55000;
        } else if (Z_sx < 175) {
            Qout = 76000;
        } else {
            Qout = Math.max(Qin, 76000);
        }

        return Qout;
    }

}
