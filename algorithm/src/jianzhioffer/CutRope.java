package jianzhioffer;

/**
 * 给你一根长度为n的绳子，请把绳子剪成整数长的m段（m、n都是整数，n>1并且m>1，m<=n），
 * 每段绳子的长度记为k[1],...,k[m]。请问k[1]x...xk[m]可能的最大乘积是多少？
 * 例如，当绳子的长度是8时，我们把它剪成长度分别为2、3、3的三段，此时得到的最大乘积是18。
 */
public class CutRope {
    public static int cutRope(int target) {
        if (target < 3) {
            return 1;
        }
        if (target == 3) {
            return 2;
        }

        return f(target);
    }

    public static int f(int n) {
        int[] dp = new int[n];
        dp[0] = 1;
        dp[1] = 2;
        dp[2] = 3;

        for (int i = 3; i < n; i++) {
            for (int j = 2; j < i; j++) {
                dp[i] = Math.max(dp[i - 1], j * dp[i - j]);
            }
        }

        return dp[n - 1];
    }

    public static void main(String[] args) {
        System.out.println(cutRope(6));
    }


}
