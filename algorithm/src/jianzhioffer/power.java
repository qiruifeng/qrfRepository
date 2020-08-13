package jianzhioffer;

public class power {
    public static double Power(double base, int exponent) {
        double res=1;

        if(base==0){
            res=0;
        }

        if(exponent==0){
            return 1;
        }else if(exponent>0){
            while(exponent>0){
                res=res*base;
                exponent--;
            }
            return res;
        }else{
            while(exponent<0){
                res=(double)res/base;
                exponent++;
            }
            return res;
        }
    }

    public static void main(String[] args) {
        System.out.println(Power(2,-3));
    }
}
