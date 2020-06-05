package uk.ac.newcastle.redhat.gavgraph.common.pom.csv;

import uk.ac.newcastle.redhat.gavgraph.common.pom.find.DiveDeeperUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class WriteCsvTest {

    //private static final String rootPath = "C:\\Users\\jayxu\\Desktop\\com";
    //private static final String rootPath = "E:\\small_poms";
    //private static final String rootPath = "E:\\more_poms";
    private static final String rootPath = "E:\\poms (2)";
    //private static final String rootPath = "C:\\Users\\jayxu\\Desktop\\gavgraph-master\\src\\main\\resources\\static\\xml\\poms";

    public static void main(String[] args) throws InterruptedException {
        //20200603
        final ExecutorService executor = Executors.newFixedThreadPool(16);
        CountDownLatch latch = new CountDownLatch(16);

        List<File> list = DiveDeeperUtil.getDeeper3LevelFiles(rootPath);
        System.out.println("子文件夹个数：" + list.size());

        List<WriteCsvTask> writeCsvTasks = new ArrayList<>();
        List<File> files = new ArrayList<>(100);
        for (int j = 1; j <= list.size(); j++) {
            File file = list.get(j - 1);
            files.add(file);
            if (j % 100 == 0) {
                List<File> ff = new ArrayList<>(files);
                WriteCsvTask writeCsvTask = new WriteCsvTask((j / 100), ff, new LinkedBlockingQueue<>());
                writeCsvTasks.add(writeCsvTask);
                files.clear();
            } else if (j == list.size()) {
                List<File> ff = new ArrayList<>(files);
                WriteCsvTask writeCsvTask = new WriteCsvTask((j / 100 + 1), ff, new LinkedBlockingQueue<>());
                writeCsvTasks.add(writeCsvTask);
                files.clear();
            }
        }

        if(writeCsvTasks.isEmpty()){
            throw new IllegalArgumentException("IllegalArgumentException");
        }

        System.out.println("================== BEFORE generating csv =================");
        WriteCsvJobExecutor executors = new WriteCsvJobExecutor(latch,writeCsvTasks,executor);
        executors.execute();

        executor.awaitTermination(1, TimeUnit.HOURS);

        System.out.println("=================== AFTER csv generated ==================");
    }

}
