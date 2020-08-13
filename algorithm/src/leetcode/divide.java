package leetcode;

public class divide {
    public static int divide(int dividend, int divisor){

        int ans=0;

        if (dividend==Integer.MIN_VALUE&&divisor==-1){
            return 2147483647;
        }

        if (dividend>0&&divisor>0){
            while (dividend>=0){
                dividend=dividend-divisor;
                ans++;
            }
            return ans-1;
        }else if (dividend<0&&divisor>0){
            while (dividend<=0){
                dividend=dividend+divisor;
                ans++;
            }
            return 0-ans+1;
        }else if (dividend>0&&divisor<0){
            while (dividend>=0){
                dividend=dividend+divisor;
                ans++;
            }
            return 0-ans+1;
        }else if (dividend<0&&divisor<0){
            while (dividend<=0){
                dividend=dividend-divisor;
                ans++;
            }
            return ans-1;
        }else {
            return  0;
        }
    }

    public static void main(String[] args) {
        System.out.println(divide(Integer.MIN_VALUE,-1));
    }
}
