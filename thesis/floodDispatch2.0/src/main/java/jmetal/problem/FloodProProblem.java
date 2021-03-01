package jmetal.problem;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import entity.Result;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.omg.PortableInterceptor.INACTIVE;
import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import until.EasyExcelUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static until.Msjg.riverevolustion;

public class FloodProProblem extends AbstractDoubleProblem {

    private static final long serialVersionUID = 1L;

    //约束
    private HashMap<String, double[]> constraints;//目前考虑的：时段的水库约束
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
                double[][] ZVCurvedOfxld = EasyExcelUtil.readTable("1溪洛渡基本特性曲线", 0);
                PolynomialSplineFunction ZVCurvedOfXLD = splineInterpolator.interpolate(ZVCurvedOfxld[0], ZVCurvedOfxld[1]);
                PolynomialSplineFunction VZCurvedOfXLD = splineInterpolator.interpolate(ZVCurvedOfxld[1], ZVCurvedOfxld[0]);
                this.ZVCurvedOfStation.put("XLD", ZVCurvedOfXLD);
                this.VZCurvedOfStation.put("XLD", VZCurvedOfXLD);

                double[][] ZVCurvedOfxjb = EasyExcelUtil.readTable("1向家坝基本特性曲线", 0);
                PolynomialSplineFunction ZVCurvedOfXJB = splineInterpolator.interpolate(ZVCurvedOfxjb[0], ZVCurvedOfxjb[1]);
                PolynomialSplineFunction VZCurvedOfXJB = splineInterpolator.interpolate(ZVCurvedOfxjb[1], ZVCurvedOfxjb[0]);
                this.ZVCurvedOfStation.put("XJB", ZVCurvedOfXJB);
                this.ZVCurvedOfStation.put("XJB", VZCurvedOfXJB);

                double[][] ZOutCapacityOfxld = EasyExcelUtil.readTable("泄流能力曲线", 1);
                PolynomialSplineFunction ZOutCapacityOfXLD = splineInterpolator.interpolate(ZOutCapacityOfxld[0], ZOutCapacityOfxld[1]);
                this.ZOutQOfStation.put("XLD", ZOutCapacityOfXLD);

                double[][] ZOutCapacityOfxjb = EasyExcelUtil.readTable("泄流能力曲线", 2);
                PolynomialSplineFunction ZOutCapacityOfXJB = splineInterpolator.interpolate(ZOutCapacityOfxjb[0], ZOutCapacityOfxjb[1]);
                this.ZOutQOfStation.put("XJB", ZOutCapacityOfXJB);

            case 1:
                double[][] ZVCurvedOfsx = EasyExcelUtil.readTable("1三峡基本特性曲线", 0);
                PolynomialSplineFunction ZVCurvedOfSX = splineInterpolator.interpolate(ZVCurvedOfsx[0], ZVCurvedOfsx[1]);
                PolynomialSplineFunction VZCurvedOfSX = splineInterpolator.interpolate(ZVCurvedOfsx[1], ZVCurvedOfsx[0]);
                this.ZVCurvedOfStation.put("SX", ZVCurvedOfSX);
                this.VZCurvedOfStation.put("SX", VZCurvedOfSX);

                double[][] ZOutCapacityOfsx = EasyExcelUtil.readTable("泄流能力曲线", 0);
                PolynomialSplineFunction ZOutCapacityOfSX = splineInterpolator.interpolate(ZOutCapacityOfsx[0], ZOutCapacityOfsx[1]);
                this.ZOutQOfStation.put("SX", ZOutCapacityOfSX);

                break;
            default:
                System.out.println("水库个数只能是1，3，5");
        }
        System.out.println("水位库容曲线及泄流能力曲线初始化完毕");

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


    //每个水库的演算过程，输入，水位过程即可；返回流量过程
    private Result getQProcess(String stationName, double levelStart, double[] Qin, double[] Z) {
        int xNum = getNumberOfVariables() / this.stationNum;

        Result result = new Result();

        double[] Znew = new double[xNum];
        Map<Integer,Double> map = new HashMap<>();

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
        PolynomialSplineFunction zvCurve = ZVCurvedOfStation.get(stationName);
        //库容插水位
        PolynomialSplineFunction vzCurve = VZCurvedOfStation.get(stationName);
        //上游水位插最大下泄流量
        PolynomialSplineFunction zOutCurve = ZOutQOfStation.get(stationName);

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
            Qout[i] = inQFromStation.get(stationName)[i] - detaQ[i];

            //处理流量约束,最大值为最大下泄流量
            double maxQ = ZOutQOfStation.get(stationName).value(Zup[i]);
            double minQ = 0.0;
            if (Qout[i] > maxQ) {
                Qout[i] = maxQ;
                detaQ[i] = inQFromStation.get("XLD")[i] - detaQ[i];
                detaV[i] = detaQ[i] * 3600 * AvT / Math.pow(10, 8);
                V[i + 1] = detaV[i] + V[i];
                Z[i] = VZCurvedOfStation.get("XLD").value(V[i + 1]);
                map.put(i,Z[i]);
            }
            if (Qout[i] < minQ){
                Qout[i] = minQ;
                detaQ[i] = inQFromStation.get("XLD")[i] - detaQ[i];
                detaV[i] = detaQ[i] * 3600 * AvT / Math.pow(10, 8);
                V[i + 1] = detaV[i] + V[i];
                Z[i] = VZCurvedOfStation.get("XLD").value(V[i + 1]);
                map.put(i,Z[i]);
            }
            Znew[i] = Z[i];
        }

        result.setQout(Qout);
        result.setZnew(Z);
        result.setIntegerDoubleMap(map);

        return result;
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

            //记录超出约束的时段个数

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

            SplineInterpolator splineInterpolator = new SplineInterpolator();//样条差值

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

            //各个水库的水位，决策变量
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

            // 水库平均水位,单位m
            double[] ZupXLD = new double[xNum];
            double[] ZupXJB = new double[xNum];
            double[] ZupSX = new double[xNum];

            // 各个时刻库容(10^8m3)
            double[] Vxld = new double[xNum + 1];
            double[] Vxjb = new double[xNum + 1];//溪洛渡
            double[] Vsx = new double[xNum + 1];//溪洛渡


            // 时段库容变化量(10^8m3)
            double[] detaVXLD = new double[xNum];
            double[] detaVXJB = new double[xNum];
            double[] detaVSX = new double[xNum];


            // 下泄流量(m3/s)
            double[] QoutXLD = new double[xNum];
            double[] QoutXJB = new double[xNum];
            double[] QoutSX = new double[xNum];

            // 时段流量变化量(m3/s),
            double[] detaQXLD = new double[xNum];
            double[] detaQXJB = new double[xNum];
            double[] detaQSX = new double[xNum];


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
            Vxld[0] = ZVCurvedOfStation.get("XLD").value(levelStart[0]);
            double ZXLDmax = Double.MIN_VALUE;//记录最高水位
            double QXLDmax = Double.MIN_VALUE;//记录最大流量
            for (int i = 0; i < xNum; i++) {
                if (i == 0) {
                    ZupXLD[i] = (levelStart[0] + Zxld[i]) / 2;
                } else {
                    ZupXLD[i] = (Zxld[i] + Zxld[i - 1]) / 2;
                }

                //根据时刻的水位插出库容
                Vxld[i + 1] = ZVCurvedOfStation.get("XLD").value(Zxld[i]);
                //计算时段库容变化量
                detaVXLD[i] = Vxld[i + 1] - Vxld[i];
                // 计算时段流量变化量
                detaQXLD[i] = detaVXLD[i] * Math.pow(10, 8) / AvT / 3600;

                //时段下泄流量
                QoutXLD[i] = inQFromStation.get("XLD")[i] - detaQXLD[i];
                // 时段平均水位对应最大泄流能力
                double ZoutCapacityOfXLD = ZOutQOfStation.get("XLD").value(ZupXLD[i]);

                //处理流量约束，下泄量不能超过最大泄流约束,不能小于0；
                double maxQXLD = ZoutCapacityOfXLD;
                double minQXLD = 0;

                if (QoutXLD[i] > maxQXLD) {
                    QoutXLD[i] = maxQXLD;
                    detaQXLD[i] = inQFromStation.get("XLD")[i] - detaQXLD[i];
                    detaVXLD[i] = detaQXLD[i] * 3600 * AvT / Math.pow(10, 8);
                    Vxld[i + 1] = detaVXLD[i] + Vxld[i];
                    Zxld[i] = VZCurvedOfStation.get("XLD").value(Vxld[i + 1]);
                    solution.setVariableValue(i, Zxld[i]);
                }

                if (QoutXLD[i] < minQXLD) {
                    QoutXLD[i] = minQXLD;
                    detaQXLD[i] = inQFromStation.get("XLD")[i] - detaQXLD[i];
                    detaVXLD[i] = detaQXLD[i] * 3600 * AvT / Math.pow(10, 8);
                    Vxld[i + 1] = detaVXLD[i] + Vxld[i];
                    Zxld[i] = VZCurvedOfStation.get("XLD").value(Vxld[i + 1]);
                    solution.setVariableValue(i, Zxld[i]);
                }

                if (QoutXLD[i] > QXLDmax) {
                    QXLDmax = QoutXLD[i];
                }
                if (Zxld[i] > ZXLDmax) {
                    ZXLDmax = Zxld[i];
                }
            }
        }


    }
}
