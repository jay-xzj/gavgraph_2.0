package uk.ac.newcastle.redhat.gavgraph.common.pom.find;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class FindJob implements Callable<BlockingQueue<String>> {

    private final CountDownLatch countDownLatch;
    private final FindTask task;

    public FindJob(CountDownLatch countDownLatch, FindTask task) {
        this.countDownLatch = countDownLatch;
        this.task = task;
    }

    @Override
    public BlockingQueue<String> call() throws Exception {
        System.out.println("当前的线程是："+Thread.currentThread() +", 当前的task是："+ task.getId());
        //执行线程
        task.execute();
        //countDown自减
        countDownLatch.countDown();
        return task.getQueue();
    }



}
