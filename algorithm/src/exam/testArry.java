package exam;

public class testArry<T extends Number> {

    T x;


    public static void main(String[] args) {
        testArry<Long> arry=new testArry<>();
        arry.x=123l;
    }
}
