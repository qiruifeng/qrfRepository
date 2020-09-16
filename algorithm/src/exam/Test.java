package exam;

public class Test {


    public static void main(String[] args) {


        stackQueue test=new stackQueue();
        test.push(1);
        test.push(2);
        test.push(3);

        System.out.println(test.empty());
        System.out.println(test.size());
        System.out.println(test.pop());
        System.out.println(test.pop());
        System.out.println(test.pop());

    }
}
