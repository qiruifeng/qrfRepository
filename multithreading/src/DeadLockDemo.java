public class DeadLockDemo {

    private static Object resource1=new Object();//资源 1
    private static Object resource2 = new Object();//资源 2

    public static void main(String[] args) {
        new Thread(()->{

        },"线程1").start();




        Thread 线程1=new Thread();
    }
}
