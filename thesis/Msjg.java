package com.hust.SimulationOperate.service.utils;

public class Msjg {
    public static double[] riverevolustion(int timeDelay2Downstream, double x, double[] up, String period) {

        double[] Q_up = time2hour(up, period);
        int Q_up_length = Q_up.length;// 时段数量

        double[] Q_down = new double[Q_up_length];

        if (x > 0) {// 如果马斯京根参数存在,则用马斯京根演进
            Q_down = msjg(Q_up, timeDelay2Downstream, x);
        } else if (timeDelay2Downstream != -1) {// 时延存在
            for (int i = 0; i < Q_up_length; i++) {
                if (i <= timeDelay2Downstream) {
                    Q_down[i] = Q_up[timeDelay2Downstream];
                } else {
                    Q_down[i] = Q_up[i - timeDelay2Downstream];
                }
            }
        } else {// 如果时延不存在
            for (int i = 0; i < Q_up_length; i++) {
                Q_down[i] = Q_up[i];
            }
        }
        return hour2time(Q_down, period);
    }

    public static double[] msjg(double[] Q_up, int k, double x) {

        int Q_up_length = Q_up.length;// 演进时间长度,单位小时
        double[][] Result = new double[Q_up_length][k + 1];// 行代表一个时段的演进河道对应各个断面的流量
        double[] Q_down = new double[Q_up_length];
        double[][] c = new double[3][k];// 第一行为c0,第二行c1，第三行c2
        // 将k和X转换成c0/c1/c2
        for (int i = 0; i < k; i++) {
            c[0][i] = (0.5 - 1 * x) / (0.5 + 1 - 1 * x);
            c[1][i] = (0.5 + 1 * x) / (0.5 + 1 - 1 * x);
            c[2][i] = (-0.5 + 1 - 1 * x) / (0.5 + 1 - 1 * x);
        }

        // 进行循环计算
        for (int i = 0; i < Q_up_length; i++) {
            for (int j = 0; j < k + 1; j++) {
                if (i == 0) {
                    Result[i][j] = Q_up[i];
                } else {
                    if (j == 0) {
                        Result[i][j] = Q_up[i];
                    } else {
                        Result[i][j] = c[0][j - 1] * Result[i][j - 1] + c[1][j - 1] * Result[i - 1][j - 1]
                                + c[2][j - 1] * Result[i - 1][j];
                    }
                }
            }
        }
        for (int i = 0; i < Q_up.length; i++) {
            Q_down[i] = Result[i][k];
        }
        return Q_down;

    }

    /*
     * 不同尺度数据转化为1小时
     * 
     */
    private static double[] time2hour(double[] array, String period) {
        double[] result = new double[1];
        int timelag = 0;
        switch (period) {
            case "日": {
                timelag = 24;
                break;
            }
            case "小时": {
                timelag = 4;
                break;
            }
            default: {
                break;
            }
        }
        result = new double[array.length * timelag];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < timelag; j++) {
                result[timelag * i + j] = array[i];
            }
        }
        return result;
    }

    /*
     * 1小时数据转化为不同尺度
     * 
     */
    private static double[] hour2time(double[] array, String period) {
        double[] result = new double[1];
        int timelag = 0;
        switch (period) {
            case "日": {
                timelag = 24;
                break;
            }
            case "小时": {
                timelag = 4;
                break;
            }
            default: {
                break;
            }
        }
        result = new double[array.length / timelag];
        for (int i = 0; i < array.length / timelag; i++) {
            double sum = 0;
            for (int j = 0; j < timelag; j++) {
                sum += array[timelag * i + j];

            }
            result[i] = sum / timelag;
        }
        return result;
    }
}
