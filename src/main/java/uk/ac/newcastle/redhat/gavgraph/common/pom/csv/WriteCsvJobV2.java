package uk.ac.newcastle.redhat.gavgraph.common.pom.csv;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class WriteCsvJobV2 implements Callable<Object> {

    private final CountDownLatch countDownLatch;
    private final WriteCsvTaskV2 task;

    public WriteCsvJobV2(CountDownLatch countDownLatch, WriteCsvTaskV2 task) {
        this.countDownLatch = countDownLatch;
        this.task = task;
    }

    @Override
    public BlockingQueue<String> call() throws Exception {
        //System.out.println("Thread.currentThread："+Thread.currentThread() +", current Task："+ task.getId());
        //thread execution
        task.execute();//5 XmlPullParserException: only whitespace content allowed before start tag and not \u9518 (position: START_DOCUMENT seen \u9518... @1:1)
        //countDown
        countDownLatch.countDown();
        return task.getQueue();
    }
}
