package jmetal.problem;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import until.FileOperatorUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class floodProblem extends AbstractDoubleProblem {

    private static final long serialVersionUID = 1L;

    // 约束
    private LinkedHashMap<Integer, LinkedHashMap<Integer, Object>> constraints;
    // 入流
    private LinkedHashMap<Integer, LinkedHashMap<Integer, Object>> inQ;

    private HashMap<String, double[]> ZVdouble = new HashMap<>();
    private HashMap<String, double[]> ZALLQdouble = new HashMap<>();

    /**
     * 构造器1：设置决策变量个数和解决方案类型
     *
     * @param xNum 决策变量个数
     * @param solutionTypeStr 解决方案类型
     */
    public floodProblem(int xNum, String solutionTypeStr) {
        initData();

        setNumberOfVariables(xNum);
        setNumberOfObjectives(2);
        setNumberOfConstraints(0);

        List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables()) ;
        List<Double> upperLimit = new ArrayList<>(getNumberOfVariables()) ;

        for (int i = 0; i < getNumberOfVariables(); i++) {
            lowerLimit.add(145.0);
            upperLimit.add(175.0);
        }

        setUpperLimit(upperLimit);
        setLowerLimit(lowerLimit);

    }

    /**
     * 水库参数初始化
     */
    private void initData(){
        // 入库流量
        inQ = readBaseData("三峡入库.xls", 2, 124, 3, 4, 0);

        // 水位库容曲线
        // 水位库容
        LinkedHashMap<Integer, LinkedHashMap<Integer, Object>> ZV = readBaseData("三峡基本特性曲线.xls", 2, 552, 2, 4, 0);
        // 转换double[]类型，方便插值
        double[] z = new double[ZV.size()];
        double[] v = new double[ZV.size()];

        for (int i = 0; i < ZV.size(); i++) {
            z[i] = (double) ZV.get(i).get(0);
            v[i] = (double) ZV.get(i).get(1);
        }
        ZVdouble.put("Z", z);
        ZVdouble.put("V", v);

        // 读取枢纽泄流能力
        // 对应水位下水库泄流能力
        LinkedHashMap<Integer, LinkedHashMap<Integer, Object>> ZALLQ = readBaseData("三峡基本特性曲线.xls", 2, 12, 1, 3, 3);
        // 转换double[]类型，方便插值
        double[] zu = new double[ZALLQ.size()];
        double[] allq = new double[ZALLQ.size()];

        for (int i = 0; i < ZALLQ.size(); i++) {
            zu[i] = (double) ZALLQ.get(i).get(0);
            allq[i] = (double) ZALLQ.get(i).get(1);
        }
        ZALLQdouble.put("Z", zu);
        ZALLQdouble.put("ALLQ", allq);

        System.out.println("水库特征参数初始化完成");
    }

    /**
     * 读取水库参数表
     * @return
     */
    private LinkedHashMap<Integer, LinkedHashMap<Integer, Object>> readBaseData(
            String tableName, int rowStart, int rowEnd,
            int columnStart, int columnEnd, int sheetIndex) {

        String basePath = "data/baseData/";
        String path = basePath + tableName;

        int[] rowIndex = new int[rowEnd - rowStart];
        for (int i = rowStart; i < rowEnd; i++) {
            rowIndex[i - rowStart] = i;
        }
        int[] columnIndex = new int[columnEnd - columnStart];
        for (int i = columnStart; i < columnEnd; i++) {
            columnIndex[i - columnStart] = i;
        }

        return FileOperatorUtil.readTableForXLS(path, rowIndex, columnIndex, sheetIndex);
    }

    /**
     * 目标值计算
     * 目标1：最高水位最低
     * 目标2：最大下泄流量最小
     * @param solution 种群
     */
    public void evaluate(DoubleSolution solution) {

        int xNum = getNumberOfVariables();

        double[] Z = new double[xNum] ;
        for (int i = 0; i < xNum; i++) {
            Z[i] = solution.getVariableValue(i) ;
        }

        double cons = 0;
        int consNum = 0;

        // 时段时长,单位：小时
        final int AvT = 24;
        // 水库平均水位,单位m
        double[] Zup = new double[xNum];
        // 时刻库容(10^8m3)
        double[] V = new double[xNum+1];
        // 时段库容变化量(10^8m3)
        double[] detaV = new double[xNum];
        // 下泄流量(m3/s)
        double[] Qout = new double[xNum];
        // 时段流量变化量(m3/s),
        double[] detaQ = new double[xNum];

        SplineInterpolator splineInterp = new SplineInterpolator(); // 样条插值

        // 上游水位插库容
        PolynomialSplineFunction zvFunc = splineInterp.interpolate(ZVdouble.get("Z"), ZVdouble.get("V"));
        // 上游水位插最大下泄流量
        PolynomialSplineFunction zallqFunc = splineInterp.interpolate(ZALLQdouble.get("Z"), ZALLQdouble.get("ALLQ"));

        V[0] = zvFunc.value(145.0); // 起调水位
        double Zmax = 0.0; // 记录最高水位
        double Qmax = 0.0; // 记录最大下泄流量
        for (int i = 0; i < Z.length; i++) {
            // 计算时段平均水位
            if (i == 0) {
                Zup[i] = (145.0 + Z[i]) / 2;
            }else{
                Zup[i] = (Z[i] + Z[i - 1]) / 2;
            }
            // 根据时刻的水位插出时刻的库容
            V[i + 1] = zvFunc.value(Z[i]);
            // 计算时段库容变化量
            detaV[i] = V[i + 1] - V[i];
            // 计算时段流量变化量
            detaQ[i] = detaV[i] * Math.pow(10, 8) / AvT / 3600;

            // 时段下泄流量
            Qout[i] = (double) inQ.get(i).get(0) - detaQ[i];
            // 时段平均水位对应最大泄流能力
            double allQ = zallqFunc.value(Zup[i]);

            // 处理流量约束；最大最小下泄流量约束
            double controlQmax = 76000.0; // 当水位不超过171米时，下泄流量不超过55000
            if (Zup[i] <= 171.0) controlQmax = 55000; // 当水位超过171米后，下泄流量不超过76000
            double maxQ = Math.min(allQ, controlQmax); // 上限为泄流能力与要求的最大值
            double minQ = 20000; // 水位高于145，最小下泄20000
            if (Zup[i] <= 145.0) minQ = (double) inQ.get(i).get(0); // 水位等于145则按来水下泄

            if (Qout[i] > maxQ) {
                cons += (Qout[i] - maxQ) / maxQ;
                consNum = consNum + 1;
            } else if (Qout[i] < minQ) {
                cons += (minQ - Qout[i]) / (maxQ - minQ);
                consNum ++;
            }

            if (Z[i] > Zmax) Zmax = Z[i];
            if (Qout[i] > Qmax) Qmax = Qout[i];
        }

        // 目标
        solution.setObjective(0, Zmax);
        solution.setObjective(1, Qmax - cons);

        solution.setAttribute("overCons", (double)consNum);

    }
}
