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

    public static double getSum(double[] x) {
        double ans = 0.0;
        for (int i = 0; i < x.length; i++) {
            ans = ans + x[i];
        }
        return ans;
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

                if (j == 0) {
                    Zup[j] = (145.0 + Z[j]) / 2;
                } else {
                    Zup[j] = (Z[j] + Z[j - 1]) / 2;
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


    public static double[] 评估(double[][] origin, double[] weight) {
        double[] ans = new double[origin[0].length];


        double[] rg = new double[weight.length];
        double[] rb = new double[weight.length];
        for (int i = 0; i < origin.length; i++) {
            rg[i] = 1.0;
            rb[i] = 0.0;
        }

        for (int i = 0; i < ans.length; i++) {
            double 分子 = 0.0;
            double 分母 = 0.0;
            for (int j = 0; j < weight.length; j++) {
                分子 = 分子 + Math.pow(weight[j] * (rg[j] - origin[j][i]), 2);
                分母 = 分母 + Math.pow(weight[j] * (rb[j] - origin[j][i]), 2);
            }
            ans[i] = 1 / (1 + 分子 / 分母);
        }
        return ans;
    }

    public static void main(String[] args) {
//        double[][] origin = {
//                {1.00, 0.89, 0.77, 0.67, 0.58, 0.51, 0.44, 0.37, 0.30, 0.23, 0.18, 0.12, 0.07, 0.03, 0.00},
//                {1.00, 0.83, 0.67, 0.50, 0.50, 0.33, 0.33, 0.17, 0.17, 0.17, 0.00, 0.00, 0.00, 0.00, 0.00},
//                {0.00, 0.04, 0.11, 0.15, 0.22, 0.26, 0.33, 0.41, 0.48, 0.57, 0.63, 0.72, 0.80, 0.89, 1.00},
//                {0.00, 0.00, 0.09, 0.18, 0.49, 0.41, 0.40, 0.53, 0.57, 0.58, 0.77, 0.79, 0.92, 1.00, 1.00}
//        };

        double[][] origin = {
                {0, 0.23255814, 0.5, 0.73255814, 1},
                {0, 0.5, 0.5, 1, 1},
                {1, 0.833333333, 0.583333333, 0.25, 0},
                {0.451202474, 0.931927347, 1, 0.244959036, 0},

        };

        double[][] origin2 = {
                {0, 0.106109325, 0.22829582, 0.331189711, 0.424437299, 0.488745981, 0.562700965, 0.627009646, 0.70096463, 0.765273312, 0.823151125, 0.881028939, 0.932475884, 0.967845659, 1},
                {0, 0.166666667, 0.333333333, 0.5, 0.5, 0.666666667, 0.666666667, 0.833333333, 0.833333333, 0.833333333, 1, 1, 1, 1, 1},
                {1, 0.956521739, 0.891304348, 0.847826087, 0.782608696, 0.739130435, 0.673913043, 0.586956522, 0.52173913, 0.434782609, 0.369565217, 0.282608696, 0.195652174, 0.108695652, 0},
                {1, 0.996159501, 0.911025098, 0.823620788, 0.505406792, 0.588734154, 0.600533657, 0.469657192, 0.427196751, 0.416269958, 0.225093361, 0.210599777, 0.084349107, 0.001080499, 0}

        };


        double[] weight1 = {0.4, 0.2, 0.2, 0.2};
        double[] weight2 = {0.2, 0.2, 0.3, 0.3};
        double[] weight3 = {0.25, 0.25, 0.25, 0.25};



        double[] ans = 评估(origin2, weight3);


        printArrays(ans, ans.length - 1);

    }


}
