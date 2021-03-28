package until;

import java.util.Random;

import static until.CalculateUtil.printArrays;


public class NewAlgorithm {
    /**
     * 生成原始序列
     *
     * @return
     */
    public static double[] generatorOriginalSequence() {
        double[] ans = new double[122];
        ans[0] = 145.0;
        ans[121] = 145.0;
        Random random = new Random();
        int 峰形 = random.nextInt(3);
//        System.out.println(峰形);
        //左单峰
        if (峰形 == 0) {
            int ran = random.nextInt(120);
            for (int i = 0; i < ran + 3; i++) {
                ans[i] = Math.min(175.0, Math.min(145.0 + 5 * i, 145 + (ran + 2 - i) * 5));
            }
            for (int i = ran + 3; i < 122; i++) {
                ans[i] = 145.0;
            }
        }
        //右单峰
        if (峰形 == 1) {
            int ran = random.nextInt(120);
            for (int i = 0; i < ran; i++) {
                ans[i] = 145;
            }
            for (int i = ran; i < 122; i++) {
                ans[i] = Math.min(175.0, Math.min(145 + (i - ran) * 5, 145 + (121 - i) * 5));
            }
        }
        //双峰
        if (峰形 == 2) {
            int ran1 = random.nextInt(118);
            int ran2 = random.nextInt(118);
            while (ran1 == ran2) {
                ran2 = random.nextInt(118);
            }
            int min = Math.min(ran1, ran2);
            int max = Math.max(ran1, ran2);


            for (int i = 0; i < min + 2; i++) {
                ans[i] = Math.min(175.0, Math.min(145 + i * 5, 145 + (min + 1 - i) * 5));
            }
            for (int i = min + 2; i < max + 2; i++) {
                ans[i] = 145.0;
            }
            for (int i = max + 2; i < 122; i++) {
                ans[i] = Math.min(175.0, Math.min(145 + (i - max - 2) * 5, 145 + (121 - i) * 5));
            }
        }

        return ans;
    }

    /**
     * 生成原始序列,不固定末水位
     *
     * @return
     */
    public static double[] generatorOriginalSequence2(int xNum, String stationName) {

        double levelMin = 0.0;
        double levelMax = 0.0;
        double detaH = 0.0;

        if (stationName.equals("SX")) {
            levelMin = 145;
            levelMax = 175;
            detaH = 5.0;
        }

        double[] ans = new double[xNum];
        Random random = new Random();
        int 峰形 = random.nextInt(2);//0,1随机生成
        if (峰形 == 0) {//类型1，回到初始水位
            int ran1 = random.nextInt(xNum);
            int ran2 = random.nextInt(xNum);
            while (ran1 == ran2) {
                ran2 = random.nextInt(xNum);
            }
            int ranMin = Math.min(ran1, ran2);
            int ranMax = Math.max(ran1, ran2);
            for (int i = 0; i < ranMin; i++) {
                ans[i] = levelMin;
            }
            for (int i = ranMin; i < ranMax; i++) {
                ans[i] = Math.min(levelMax, Math.min(145 + (i - ranMin) * detaH, 145 + (ranMax - i) * detaH));
            }
            for (int i = ranMax; i < xNum; i++) {
                ans[i] = levelMin;
            }

        }
        if (峰形 == 1) {//类型2，无法回到初始水位
            int ran = random.nextInt(xNum);
            for (int i = 0; i < ran; i++) {
                ans[i] = levelMin;
            }
            for (int i = ran; i < xNum; i++) {
                ans[i] = Math.min(levelMax, 145 + (i - ran) * detaH);
            }
        }
        return ans;
    }

    /**
     * 水位联动操作
     *
     * @param y2    使动水位原始值
     * @param y2new 使动水位结果值
     * @param y3    联动水位原始值
     * @return y3new 联动水位结果值
     */
    public static double NewPosition(double y2, double y2new, double y3, double detaZ, double lamda, double max, double min) {
        double y3new = 0.0;
        //up
        if (y2 < y2new) {
            double detax临界 = (detaZ - (y2 - y3)) / (1 - lamda);
            if (detax临界 >= (y2new - y2)) {
                y3new = y3 + lamda * (y2new - y2);
            } else {
                y3new = y3 + detax临界 * lamda + (y2new - y2 - detax临界);
            }

            if (y3new > max) y3new = max;
            if (y3new < 145.0) y3new = 145.0;

        } else {//down,往下的时候，detax临界是负值
            double detax临界 = ((y3 - y2) - detaZ) / (1 - lamda);
            if (Math.abs(detax临界) >= Math.abs(y2new - y2)) {
                y3new = y3 + lamda * (y2new - y2);
            } else {
                y3new = y3 + detax临界 * lamda + (y2new - y2 - detax临界);
            }
            if (y3new > max) y3new = max;
            if (y3new < min) y3new = min;
        }

        return y3new;
    }

    /**
     * 一次变异操作
     *
     * @param Z        输入序列
     * @param maxLevel 上限值
     * @param minlevel 下限值
     * @return 变异后的序列
     */
    public static double[] Mutations(double[] Z, double[] maxLevel, double[] minlevel) {

        int xNum = Z.length;
        double[] ans = new double[xNum];

        Random random = new Random();
        int T = random.nextInt(xNum);
        ans[T] = Math.random() * (maxLevel[T] - minlevel[T]) + minlevel[T];
        //往右联动
        for (int i = T; i < xNum - 1; i++) {
            ans[i + 1] = NewPosition(Z[i], ans[i], Z[i + 1], 5.0, 0.2, maxLevel[i+1], minlevel[i+1]);
        }
        //往左联动
        for (int i = T; i > 0; i--) {
            ans[i - 1] = NewPosition(Z[i], ans[i], Z[i - 1], 5.0, 0.2, maxLevel[i-1], minlevel[i-1]);
        }
        return ans;
    }

    /**
     * 生成初始个体
     *
     * @return 返回初始个体
     */
    public static double[] generatorOneSolution(double[] max, double[] min,int n) {

        double[] sequence = generatorOriginalSequence();
        for (int i = 0; i < n; i++) {
            sequence = Mutations(sequence, max, min);
        }
        double[] ans = sequence;
        return ans;
    }

    /**
     * 生成初始个体
     *
     * @return 返回初始个体
     */
    public static double[] generatorOneSolution2(int xNum,double[] max, double[] min,int n){
        double[] sequence = generatorOriginalSequence2(xNum,"SX");
        for (int i = 0; i < n; i++) {
            sequence = Mutations(sequence, max, min);
        }
        double[] ans = sequence;
        return ans;
    }

    public static void main(String[] args) {
//        double[] a = generatorOriginalSequence();
//        printArrays(a, 121);
//        double[] maxLevel = new double[122];
//        double[] minLevel = new double[122];
//        for (int i = 0; i < maxLevel.length; i++) {
////            minLevel[i] = 145;
//            maxLevel[i] = Math.min(175.0, Math.min(145 + i * 5, 145 + (122 - i - 1) * 5));
//        }

//        printArrays(maxLevel, 121);
//        double[] a = generatorOriginalSequence();
//        double[] b = Mutations(a, maxLevel, minLevel);
//        printArrays(maxLevel, 121);
//        double[] c = generatorOneSolution(maxLevel, minLevel);


        int xNum = 15;
        double[] maxLevel = new double[xNum];
        double[] minLevel = new double[xNum];
        for (int i = 0; i < xNum; i++) {
            minLevel[i] = 145;
            maxLevel[i] = Math.min(175.0, (145 + i * 5));
        }

//        double[] c = generatorOriginalSequence2(xNum, "SX");
//        double[] d = Mutations(c, maxLevel, minLevel);
        printArrays(maxLevel,14);
        printArrays(minLevel,14);

        double[] a = generatorOneSolution2(15,maxLevel,minLevel,10);
        printArrays(a, 14);
    }
}
