package testThreadPoolExecutordemo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorDemo {
    private static final int CORE_POOL_SIZE = 5;//核心线程数线程数定义了最小可以同时运行的线程数量。
    private static final int MAX_POOL_SIZE = 10;//当队列中存放的任务达到队列容量的时候，当前可以同时运行的线程数量变为最大线程数
    private static final int QUEUE_CAPACITY = 100;
    private static final Long KEEP_ALIVE_TIME = 1L;//当线程池中的线程数量大于corePoolSize的时候，如果这时没有新的任务提交，核心线程外的线程不会立即销毁，而是会等待，直到等待的时间超过了keepAliveTime才会被回收销毁

    public static void main(String[] args) {

        //使⽤阿⾥巴巴推荐的创建线程池的⽅式
        //通过ThreadPoolExecutor构造函数⾃定义参数创建
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(QUEUE_CAPACITY),
                new ThreadPoolExecutor.CallerRunsPolicy());


        for (int i = 0; i < 10; i++) {
            Runnable runnable = new MyRunnable("" + i);
            //执⾏Runnable
            executor.execute(runnable);//提交到线程池
        }

        //终⽌线程池
        executor.shutdown();

        while (!executor.isTerminated()) {}//如果没有终止成功，就能一直循环，直达成功终止线程池
        System.out.println("Finished all threads");

    }
}
