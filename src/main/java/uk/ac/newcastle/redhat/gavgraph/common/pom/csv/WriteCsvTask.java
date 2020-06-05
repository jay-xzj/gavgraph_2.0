package uk.ac.newcastle.redhat.gavgraph.common.pom.csv;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import uk.ac.newcastle.redhat.gavgraph.common.WriteCsv;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class WriteCsvTask {

    private int id;
    private final BlockingQueue<String> queue;
    private final List<File> files;
    private static final AtomicInteger count = new AtomicInteger(0);

    public WriteCsvTask(int id, List<File> files, BlockingQueue<String> queue) {
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
                    Model model = getPomModel(ff);
                    if (model != null) {
                        //去除packaging是bundle的,我可以认为bundle的是OSGI的内容，而不去管它么？如果可以，那应该要去除;还是只要jar war这种
                        String packaging = model.getPackaging();
                        String artifactId = model.getArtifactId();
                        String groupId = model.getGroupId();
                        String version = model.getVersion();
                        if (filterConditions(packaging, artifactId, groupId, version)) {
                            try {
                                //test
                                //String fileName = "C:\\Users\\jayxu\\Desktop\\gavgraph-master\\src\\main\\resources\\static\\xml\\test0.csv";
                                //prod more_poms
                                //String fileName = "D:\\dev\\neo4j_data\\neo4jDatabases\\database-7665b2bb-35ee-4797-8011-6068d7c8edd4\\installation-3.5.14\\import\\gav_data1.csv";
                                //prod poms
                                String fileName = "D:\\dev\\neo4j_data\\neo4jDatabases\\database-7665b2bb-35ee-4797-8011-6068d7c8edd4\\installation-3.5.14\\import\\gav_data2.csv";

                                /*If the value of artifact or version is taken from another tag, such as the properties in the profile,
                                or directly taken from the properties, then the value of artifact or version is directly determined from the folder path*/
                                File parent = ff.getParentFile();
                                Optional<File> opt = Optional.ofNullable(parent);
                                if(version.contains("$")&&opt.isPresent()){
                                    version = parent.getName();
                                }
                                if (artifactId.contains("$")&&opt.isPresent()){
                                    artifactId = parent.getParentFile().getName();
                                }
                                Map<String,Object> map = new HashMap<>();
                                map.put("groupId", groupId);
                                map.put("artifactId", artifactId);
                                map.put("version", "s_"+version);
                                map.put("combination",groupId+artifactId+version);
                                List<Map<String, Object>> datas = new ArrayList<>();
                                datas.add(map);
                                String[] displayColNames = {"combination","groupId","artifactId","version"};
                                String[] fieldNames = {"combination","groupId","artifactId","version"};
                                WriteCsv.writeCvs(fileName, datas, displayColNames, fieldNames, true, false);
                            } catch (Exception e){
                                e.printStackTrace();
                            }finally {
                                model = null;
                            }
                        }
                    }
                }
            }
        }
    }

    public static Model getPomModel(File pom) {
        Model model = null;
        try {
            MavenXpp3Reader pomReader = new MavenXpp3Reader();
            FileReader fileReader = new FileReader(pom);
            model = pomReader.read(fileReader);//XmlPullParserException: only whitespace content allowed before start tag and not \u9518 (position: START_DOCUMENT seen \u9518... @1:1)
            fileReader.close();
        } catch (XmlPullParserException | IOException xe) {
            System.out.println("报错XmlPullParserException的路径是："+pom.getAbsolutePath());
            xe.printStackTrace();
        }
        return model;
    }
    /*public static Model getPomModel(String fileName) {
        Model model = null;
        try {
            File pom = new File(fileName);
            MavenXpp3Reader pomReader = new MavenXpp3Reader();
            FileReader fileReader = new FileReader(pom);
            model = pomReader.read(fileReader);//XmlPullParserException: only whitespace content allowed before start tag and not \u9518 (position: START_DOCUMENT seen \u9518... @1:1)
            fileReader.close();
        } catch (XmlPullParserException | IOException xe) {
            System.out.println("报错XmlPullParserException的路径是："+fileName);
            xe.printStackTrace();
        }
        return model;
    }*/

    public static boolean filterConditions(String packaging, String artifactId, String groupId, String version) {
        return packaging.equalsIgnoreCase("jar") && StringUtils.isNotBlank(artifactId)
                && StringUtils.isNotBlank(groupId) && StringUtils.isNotBlank(version);
    }
}
