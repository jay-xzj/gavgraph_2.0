package uk.ac.newcastle.redhat.gavgraph.common.pom.csv;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class WriteCsvJobExecutor {

    private final CountDownLatch countDownLatch;
    private final List<WriteCsvTask> writeCsvTasks;
    private final ExecutorService executor;

    public WriteCsvJobExecutor(CountDownLatch countDownLatch, List<WriteCsvTask> writeCsvTasks, ExecutorService executor) {
        this.countDownLatch = countDownLatch;
        this.executor = executor;
        this.writeCsvTasks = writeCsvTasks;
    }

    public void execute() {
        if (writeCsvTasks == null|| writeCsvTasks.isEmpty()) {
            return;
        }
        try {
            for (int i = 0; i< writeCsvTasks.size(); i++) {
                executor.submit(new WriteCsvJob(countDownLatch, writeCsvTasks.get(i)));
            }
            //等待所有线程结束
            boolean await = countDownLatch.await(5, TimeUnit.SECONDS);
            //执行其他操作
            System.out.println("findString completed！");
            //关闭线程池
            executor.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
