package until;

import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.uma.jmetal.solution.DoubleSolution;

import java.util.Arrays;
import java.util.List;

public class CalculateUtil {
    /**
     * 得到一个数组的平方和
     *
     * @param x
     * @return
     */
    public static double getPingFangHe(double[] x) {
        double sum = 0.0;
        for (int i = 0; i < x.length; i++) {
            sum = sum + x[i] * x[i];
        }
        return sum;
    }

    public static double getDoubleArrMin(double[] x) {
        Arrays.sort(x);
        return x[0];
    }

    public static double getDoubleArrMax(double[] x) {
        Arrays.sort(x);
        return x[x.length - 1];
    }

    /**
     * 打印一维数组
     *
     * @param x
     */
    public static void printArrays(double[] x, int index) {
        for (int i = 0; i < index; i++) {
            System.out.print(x[i] + " ");
        }
        System.out.println(x[index]);
    }

    /**
     * 打印二维数组
     *
     * @param x
     */
    public static void printMatrix(double[][] x) {
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x[0].length; j++) {
                System.out.print(x[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static double[][] getQs(List<DoubleSolution> population) {

        SplineInterpolator splineInterpolator = new SplineInterpolator();//样条差值

        //数据初始化
        double[][] inQFromSXOrigin = EasyExcelUtil.readTable("入库数据", 0);
        double[] inQ = inQFromSXOrigin[2];
        double[][] ZVCurvedOfsx = EasyExcelUtil.readTable("水位库容曲线", 2);
        PolynomialSplineFunction sxZVCurve = splineInterpolator.interpolate(ZVCurvedOfsx[0], ZVCurvedOfsx[1]);


        double[][] ans = new double[population.size()][population.get(0).getNumberOfVariables()];


        int populationNum = population.size();



        for (int i = 0; i < populationNum; i++) {
            int size = population.get(0).getNumberOfVariables();
            // 时段时长,单位：小时
            final int AvT = 24;
            // 水库平均水位,单位m
            double[] Zup = new double[size];
            // 时刻库容(10^8m3)
            double[] V = new double[size + 1];
            // 时段库容变化量(10^8m3)
            double[] detaV = new double[size];
            // 下泄流量(m3/s)
            double[] Qout = new double[size];
            // 时段流量变化量(m3/s),
            double[] detaQ = new double[size];
            double[] Z = new double[size];
            for (int j = 0; j < size; j++) {
                Z[j] = population.get(i).getVariableValue(j);
            }

            V[0] = sxZVCurve.value(145.0);
            for (int j = 0; j < Zup.length; j++) {

                if (i == 0) {
                    Zup[i] = (145.0 + Z[i]) / 2;
                } else {
                    Zup[i] = (Z[i] + Z[i - 1]) / 2;
                }
                //根据时刻的水位插出库容
                V[j + 1] = sxZVCurve.value(Z[j]);
                //计算时段库容变化量
                detaV[j] = V[j + 1] - V[j];
                // 计算时段流量变化量
                detaQ[j] = detaV[j] * Math.pow(10, 8) / AvT / 3600;

                //时段下泄流量
                Qout[j] = inQ[j] - detaQ[j];
            }
            ans[i] = Qout;
        }
        return ans;
    }


    public static void main(String[] args) {
        double[] a = {1.1, 1.2, 1.3};
        printArrays(a, 2);
//        System.out.println(getDoubleArrMax(a));
    }


}
