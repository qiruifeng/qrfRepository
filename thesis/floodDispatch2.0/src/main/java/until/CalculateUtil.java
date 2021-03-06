package until;

import java.util.Arrays;

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
        return x[x.length-1];
    }


    public static void main(String[] args) {
        double[] a = {1.1, 1.2, 1.3};
        System.out.println(getDoubleArrMax(a));
    }
}
