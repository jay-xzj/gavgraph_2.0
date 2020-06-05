package uk.ac.newcastle.redhat.gavgraph.common.pom.csv;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class WriteCsvJobExecutorV2 {

    private final CountDownLatch countDownLatch;
    private final List<WriteCsvTaskV2> writeCsvTasksV2;
    private final ExecutorService executor;

    public WriteCsvJobExecutorV2(CountDownLatch countDownLatch, List<WriteCsvTaskV2> writeCsvTasksV2, ExecutorService executor) {
        this.countDownLatch = countDownLatch;
        this.executor = executor;
        this.writeCsvTasksV2 = writeCsvTasksV2;
    }

    public void execute() {
        if (writeCsvTasksV2 == null|| writeCsvTasksV2.isEmpty()) {
            return;
        }
        try {
            for (int i = 0; i< writeCsvTasksV2.size(); i++) {
                executor.submit(new WriteCsvJobV2(countDownLatch, writeCsvTasksV2.get(i)));
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
