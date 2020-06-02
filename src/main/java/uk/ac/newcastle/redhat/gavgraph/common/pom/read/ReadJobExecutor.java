package uk.ac.newcastle.redhat.gavgraph.common.pom.read;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class ReadJobExecutor {

    private final CountDownLatch countDownLatch;
    private final BlockingQueue<String> pathQueue;
    private final ExecutorService executor;
    private static AtomicInteger incrementId = new AtomicInteger(0);

    public ReadJobExecutor(CountDownLatch countDownLatch, BlockingQueue<String> pathQueue, ExecutorService executor) {
        this.countDownLatch = countDownLatch;
        this.executor = executor;
        this.pathQueue = pathQueue;
    }

    public BlockingQueue<String> execute() {
        if (pathQueue == null|| pathQueue.isEmpty()) {
            return null;
        }
        BlockingQueue<String> strings = new LinkedBlockingQueue<>();
        try {
            while(pathQueue.size()>0){
                executor.submit(new ReadJob(countDownLatch, new ReadTask(incrementId.incrementAndGet(),pathQueue.take())));
            }
            //等待所有线程结束
            countDownLatch.await();
            //执行其他操作
            System.out.println("findString completed！");
            //关闭线程池
            //executor.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return strings;
    }
}
