package uk.ac.newcastle.redhat.gavgraph.common.pom.read;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class ReadJob implements Callable<Object> {

    private final CountDownLatch countDownLatch;
    private final ReadTask task;

    public ReadJob(CountDownLatch countDownLatch, ReadTask task) {
        this.countDownLatch = countDownLatch;
        this.task = task;
    }

    @Override
    public Object call() throws Exception {
        System.out.println("当前的线程是："+Thread.currentThread() +", 当前的task是："+ task.getId());
        //执行线程
        task.execute();
        //countDown自减
        countDownLatch.countDown();
        return null;
    }




}
