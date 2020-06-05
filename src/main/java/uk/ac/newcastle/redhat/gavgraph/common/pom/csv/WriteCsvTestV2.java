package uk.ac.newcastle.redhat.gavgraph.common.pom.csv;

import uk.ac.newcastle.redhat.gavgraph.common.constant.Constant;
import uk.ac.newcastle.redhat.gavgraph.common.enums.ActivityEnum;
import uk.ac.newcastle.redhat.gavgraph.common.pom.find.DiveDeeperUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class WriteCsvTestV2 {

    public static void main(String[] args) throws InterruptedException {
        //execution(ActivityEnum.ARTIFACT.getBigIn(), Constant.ARTIFACT_BIG);
        //execution(ActivityEnum.ARTIFACT.getSmallIn(), Constant.ARTIFACT_SMALL);
        execution(ActivityEnum.DEPEND_ON.getBigIn(), Constant.DEPEND_ON_BIG);
        //execution(ActivityEnum.DEPEND_ON.getSmallIn(), Constant.DEPEND_ON_SMALL);
    }

    public static void execution(String filePath,String type) throws InterruptedException {
        //20200603
        final ExecutorService executor = Executors.newFixedThreadPool(16);
        CountDownLatch latch = new CountDownLatch(16);
        List<File> list = DiveDeeperUtil.getDeeper3LevelFiles(filePath);
        //List<File> list = DiveDeeperUtil.getDeeper3LevelFiles(ActivityEnum.ARTIFACT.getBigIn());
        System.out.println("子文件夹个数：" + list.size());

        List<WriteCsvTaskV2> writeCsvTasks = new ArrayList<>();
        List<File> files = new ArrayList<>(100);
        for (int j = 1; j <= list.size(); j++) {
            File file = list.get(j - 1);
            files.add(file);
            if (j % 100 == 0) {
                List<File> ff = new ArrayList<>(files);
                WriteCsvTaskV2 writeCsvTask = new WriteCsvTaskV2((j / 100), ff, new LinkedBlockingQueue<>(),type);
                writeCsvTasks.add(writeCsvTask);
                files.clear();
            } else if (j == list.size()) {
                List<File> ff = new ArrayList<>(files);
                WriteCsvTaskV2 writeCsvTask = new WriteCsvTaskV2((j / 100 + 1), ff, new LinkedBlockingQueue<>(),type);
                writeCsvTasks.add(writeCsvTask);
                files.clear();
            }
        }

        if(writeCsvTasks.isEmpty()){
            throw new IllegalArgumentException("IllegalArgumentException");
        }

        System.out.println("================== BEFORE generating csv =================");
        WriteCsvJobExecutorV2 executors = new WriteCsvJobExecutorV2(latch,writeCsvTasks,executor);
        executors.execute();

        executor.awaitTermination(1, TimeUnit.HOURS);

        System.out.println("=================== AFTER csv generated ==================");
    }

}
