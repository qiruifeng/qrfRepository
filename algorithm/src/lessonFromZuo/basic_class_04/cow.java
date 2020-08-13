package lessonFromZuo.basic_class_04;



public class cow {
    public static int cowNum(int n){

        if (n <= 0) {
            return -1;
        }else if (n==1||n==2||n==3){
            return n;
        }else {
            return cowNum(n-1)+cowNum(n-3);
        }
    }

    public static void main(String[] args) {
        System.out.println(cowNum(10));
    }
}