package uk.ac.newcastle.redhat.gavgraph.common.pom.find;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class FindTask {
    private int id;
    private final BlockingQueue<String> queue;
    private final List<File> files;
    private static final AtomicInteger count = new AtomicInteger(0);

    public FindTask(int id, List<File> files, BlockingQueue<String> queue) {
        this.id = id;
        this.queue = queue;
        this.files = files;
    }

    public int getId() {
        return id;
    }

    public BlockingQueue<String> getQueue() {
        return queue;
    }

    public List<File> getFiles() {
        return files;
    }

    BlockingQueue<String> execute(){
        files.forEach(file -> recur(file));
        return getQueue();
    }

    private void recur(File file) {
        File[] files = file.listFiles();
        if (files != null && files.length != 0) {
            for (File ff : files) {
                if (ff.isDirectory()) {
                    recur(ff);
                } else if (ff.isFile() && ff.getName().endsWith(".pom")) {
                    if(count.incrementAndGet()%10000==0) {
                        System.err.println("当前线程：" + Thread.currentThread().getName() + "当前第：" + (count.incrementAndGet()) + " 条");
                    }
                    queue.add(ff.getAbsolutePath());
                    /*Model model = getPomModel(ff.getAbsolutePath());
                    if (model != null) {
                        //去除packaging是bundle的,我可以认为bundle的是OSGI的内容，而不去管它么？如果可以，那应该要去除;还是只要jar war这种
                        String packaging = model.getPackaging();
                        String artifactId = model.getArtifactId();
                        String groupId = model.getGroupId();
                        String version = model.getVersion();
                        if (filterConditions(packaging, artifactId, groupId, version)) {
                            try {
                                queue.add(ff.getAbsolutePath());
                            } finally {
                                model = null;
                            }
                        }
                    }*/
                }
            }
        }
    }

    public static Model getPomModel(String fileName) {
        Model model = null;
        try {
            File pom = new File(fileName);
            MavenXpp3Reader pomReader = new MavenXpp3Reader();
            FileReader fileReader = new FileReader(pom);
            model = pomReader.read(fileReader);
            fileReader.close();
        } catch (XmlPullParserException | IOException xe) {
            xe.printStackTrace();
        }
        return model;
    }

    public static boolean filterConditions(String packaging, String artifactId, String groupId, String version) {
        return packaging.equalsIgnoreCase("jar") && StringUtils.isNotBlank(artifactId)
                && StringUtils.isNotBlank(groupId) && StringUtils.isNotBlank(version);
    }
}
