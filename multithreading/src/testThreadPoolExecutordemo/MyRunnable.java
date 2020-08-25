package testThreadPoolExecutordemo;

import java.util.Date;

/**
 * 这是⼀个简单的Runnable类，需要⼤约5秒钟来执⾏其任务。
 * @author qiruifeng
 */
public class MyRunnable implements Runnable {


    private String command;

    //自定义带参数的构造器
    public MyRunnable(String s) {
        this.command = s;
    }


    private void processCommand() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " Start.Time = " + new Date());

        processCommand();

        System.out.println(Thread.currentThread().getName() + " End.Time = " + new Date());

    }

    @Override
    public String toString(){
        return this.command;
    }
}
