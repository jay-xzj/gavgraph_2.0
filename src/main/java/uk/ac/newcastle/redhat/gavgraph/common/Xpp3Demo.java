package uk.ac.newcastle.redhat.gavgraph.common;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.junit.Test;
import uk.ac.newcastle.redhat.gavgraph.common.pom.find.DiveDeeperUtil;
import uk.ac.newcastle.redhat.gavgraph.common.pom.find.FindJobExecutor;
import uk.ac.newcastle.redhat.gavgraph.common.pom.find.FindTask;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class Xpp3Demo {

    //private Logger log = LogManager.getLogger(Xpp3Demo.class);

    private static final String rootPath = "E:\\small_poms";

    @Test
    public void testXpp3Read() throws Exception {
        //20200603
        final ExecutorService executor = Executors.newFixedThreadPool(16);
        CountDownLatch latch = new CountDownLatch(16);

        List<File> list = DiveDeeperUtil.getDeeper3LevelFiles(rootPath);
        System.out.println("子文件夹个数：" + list.size());

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

        System.out.println("================== BEFORE 生成String Q =================");
        FindJobExecutor executors = new FindJobExecutor(findPathTasks);
        BlockingQueue<String> sQueue = executors.execute();

        //File pom = new File("src\\main\\resources\\static\\xml\\pom.xml");
        File pom = new File("src\\main\\resources\\static\\xml\\hibernate-2.1.8-atlassian-34.pom");
        //File pom = new File("src\\main\\resources\\static\\xml\\antlr-2.7.7.redhat-7.pom");
        MavenXpp3Reader pomReader = new MavenXpp3Reader();
        Model model = pomReader.read(new FileReader(pom));

        //log.info("Parent:{}",model.getParent());
        System.out.println("parent=="+model.getParent());


        System.out.println(model.getDependencyManagement());
        List<Plugin> plugins = model.getBuild().getPlugins();
        //plugins.forEach(System.out::println);
        System.out.println(model.getBuild().getPlugins());

        System.out.println("model=="+model);
        System.out.println(model.getArtifactId());
        System.out.println(model.getGroupId());
        System.out.println(model.getVersion());
        String fileName = "C:\\Users\\jayxu\\Desktop\\gavgraph-master\\src\\main\\resources\\static\\xml\\test1.csv";
        List<Map<String, Object>> datas = new ArrayList<>();

        Map<String,Object> map = new HashMap<>();
        map.put("groupId", model.getGroupId());
        map.put("artifactId", model.getArtifactId());
        map.put("version", model.getVersion());
        datas.add(map);

        //get dependencies
        List<Dependency> dependencies = model.getDependencies();
        dependencies.forEach(dependency -> {
            Map<String, Object> dm = new HashMap<>();
            dm.put("groupId", dependency.getGroupId());
            dm.put("artifactId", dependency.getArtifactId());
            dm.put("version", "s"+dependency.getVersion());
            datas.add(dm);
        });
        String[] displayColNames = {"groupId","artifactId","version"};
        String[] fieldNames = {"groupId","artifactId","version"};
        WriteCsv.writeCvs(fileName,datas,displayColNames,fieldNames);


    }

}