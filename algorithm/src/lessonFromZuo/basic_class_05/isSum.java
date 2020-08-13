package lessonFromZuo.basic_class_05;


import static utils.tools.generateRandomArrays;

public class isSum {
    /**
     * 类似子序列的问题，选当前数不选前数
     *
     * @param arr 初始数组
     * @param i   当前来到了第几个数
     * @param sum 来到这个数之前产生的和
     * @param aim 目标数
     * @return
     */
    public static boolean isSun(int[] arr, int i, int sum, int aim) {
        if (i == arr.length) {//这里应该到到数组最后一个的下一位，才能把数组最后一个数包括进去
            return sum == aim;
        }
        return (isSun(arr, i + 1, sum, aim) || isSun(arr, i + 1, sum + arr[i], aim));
    }

    public static boolean isSum(int[] arr, int aim) {
        return isSun(arr, 0, 0, aim);
    }

    public static boolean isSumDp(int[] arr, int aim) {

        int max = 0;
        for (int i = 0; i < arr.length; i++) {
            max=max+arr[i];
        }

        if (aim>max){
            return false;
        }

        boolean[][] dp = new boolean[arr.length+1][max+1];

        dp[arr.length][aim]=true;//

        for (int i=max;i>=0&&i!=aim;i--){
            dp[arr.length][i]=false;
        }

        for (int i=arr.length-1;i>=0;i--){
            for (int j=aim;j>=0;j--){
                dp[i][j]=(dp[i+1][j]||dp[i+1][j+arr[i]]);
            }
        }

        return dp[0][0];
    }

    public static void main(String[] args) {
        int[] arry={1,3,4};
        int[] arr = generateRandomArrays(100);
        System.out.println(isSum(arr, 50));
        System.out.println(isSumDp(arr,50));
    }

}
