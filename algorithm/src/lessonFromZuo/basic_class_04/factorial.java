package lessonFromZuo.basic_class_04;

public class factorial {
    public static long factorial1(int a){
        long result=1;
        for (int i =1;i<=a;i++){
            result=result*i;
        }
        return result;
    }

    public static long factorial2(int n){
        if (n==1){
            return 1L;
        }
        return (long)n*factorial2(n-1);
    }

    public static void main(String[] args) {
        System.out.println(factorial1(3));
        System.out.println(factorial1(3));
    }

}
