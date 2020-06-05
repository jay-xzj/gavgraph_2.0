package uk.ac.newcastle.redhat.gavgraph.common.pom;


import uk.ac.newcastle.redhat.gavgraph.common.pom.find.FindJobExecutor;
import uk.ac.newcastle.redhat.gavgraph.common.pom.find.FindTask;
import uk.ac.newcastle.redhat.gavgraph.common.pom.read.ReadJobExecutor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Process {


    //private static final String rootPath = "/Users/xuzhijie/Desktop/dissertation_project/more_poms";
    //private static final String rootPath = "/Users/xuzhijie/Documents/small_poms";
    private static final String rootPath = "E:\\poms (2)";
    //private static final String rootPath = "E:\\more_poms";
    //private static final String rootPath = "E:\\small_poms";


    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();

        List<File> list = getDeeper3LevelFiles();
        System.out.println("子文件夹个数：" + list.size());//8900多个文件夹  125553个文件夹

        List<FindTask> findPathTasks = new ArrayList<>();
        List<File> files = new ArrayList<>(100);
        for (int j = 1; j <= list.size(); j++) {
            File file = list.get(j - 1);
            files.add(file);
            if (j % 100 == 0) {
                List<File> ff = new ArrayList<>(files);
                FindTask findTask = new FindTask((j / 100), ff, new LinkedBlockingQueue<>());
                findPathTasks.add(findTask);
                files.clear();
            } else if (j == list.size()) {
                List<File> ff = new ArrayList<>(files);
                FindTask findTask = new FindTask((j / 100 + 1), ff, new LinkedBlockingQueue<>());
                findPathTasks.add(findTask);
                files.clear();
            }
        }

        if(findPathTasks.isEmpty()){
            throw new IllegalStateException("参数异常");
        }

        final ExecutorService executor = Executors.newFixedThreadPool(16);
        CountDownLatch latch = new CountDownLatch(16);

        System.out.println("================== BEFORE 生成String Q =================");
        FindJobExecutor executors = new FindJobExecutor(findPathTasks);
        BlockingQueue<String> sQueue = executors.execute();
        System.out.println("================== AFTER 生成String Q =================");

        long end = System.currentTimeMillis();
        float  scds = (float) ((end - start)/1000);
        System.out.println("处理耗时："+scds+" 秒!");

        System.err.println("当前StringQueue里面共有："+sQueue.size()+" 条记录。");

        System.out.println("+++++++++  BEFORE 消耗 StringQueue  +++++++++");
        long start1 = System.currentTimeMillis();
        ReadJobExecutor readJobExecutor = new ReadJobExecutor(latch, sQueue, executor);
        readJobExecutor.execute();
        long end1 = System.currentTimeMillis();
        double  scds1 = (double) ((end1 - start1)/1000);
        System.out.println("处理耗时2："+scds1+" 秒!");
        System.out.println("+++++++++  AFTER 消耗 StringQueue +++++++++");

        System.err.println("当前StringQueue里面共有："+sQueue.size()+" 条记录。");
        executor.shutdown();
    }


    private static List<File> getDeeper3LevelFiles() {
        File file = new File(rootPath);
        File[] files = file.listFiles();
        List<File> l3SubFiles = new ArrayList<>();
        for (File fi : files) {
            if (fi.isDirectory()) {
                File[] fs = fi.listFiles();
                if (fs != null) {
                    for (File f : fs) {
                        if (f.isDirectory()) {
                            File[] fs1 = f.listFiles();
                            if (fs1 != null) {
                                for (File f1 : fs1) {
                                    if (f1.isDirectory()) {
                                        l3SubFiles.add(f1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return l3SubFiles;
    }
}
