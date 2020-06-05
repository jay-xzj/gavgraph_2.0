package uk.ac.newcastle.redhat.gavgraph.common.pom.find;

import java.util.List;
import java.util.concurrent.*;

public class FindJobExecutor {

    private CountDownLatch countDownLatch;
    private List<FindTask> findTasks;
    private static ExecutorService executor = null;

    public FindJobExecutor(List<FindTask> findTasks) {
        countDownLatch = new CountDownLatch(findTasks.size());
        executor = Executors.newFixedThreadPool(16);
        this.findTasks = findTasks;
    }

    public BlockingQueue<String> execute() {
        if (findTasks == null|| findTasks.isEmpty()) {
            return null;
        }
        BlockingQueue<String> strings = new LinkedBlockingQueue<>();
        try {
            for (int i = 0; i< findTasks.size(); i++) {
                Future<BlockingQueue<String>> submit = executor.submit(new FindJob(countDownLatch, findTasks.get(i)));
                strings.addAll(submit.get());
            }
            //等待所有线程结束
            countDownLatch.await();
            //执行其他操作
            System.out.println("findString completed！");
            //关闭线程池
            executor.shutdown();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return strings;
    }
}
