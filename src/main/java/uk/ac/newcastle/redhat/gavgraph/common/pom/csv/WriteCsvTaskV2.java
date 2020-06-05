package uk.ac.newcastle.redhat.gavgraph.common.pom.csv;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import uk.ac.newcastle.redhat.gavgraph.common.WriteCsv;
import uk.ac.newcastle.redhat.gavgraph.common.constant.Constant;
import uk.ac.newcastle.redhat.gavgraph.common.enums.ActivityEnum;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class WriteCsvTaskV2 {

    private int id;
    private final BlockingQueue<String> queue;
    private final List<File> files;
    private final String type;
    private static final AtomicInteger count = new AtomicInteger(0);

    public WriteCsvTaskV2(int id, List<File> files, BlockingQueue<String> queue,String type) {
        this.id = id;
        this.queue = queue;
        this.files = files;
        this.type = type;
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
                        String combination = groupId+artifactId+version;
                        if (filterConditions(packaging, artifactId, groupId, version)) {

                            try {
                                switch (type){
                                    case Constant.ARTIFACT_BIG:
                                        //the path of generated big pom csv file
                                        generateArtifactFile(version,artifactId,ff,groupId,ActivityEnum.ARTIFACT.getBigOut());
                                        break;
                                    case Constant.ARTIFACT_SMALL:
                                        generateArtifactFile(version,artifactId,ff,groupId,ActivityEnum.ARTIFACT.getSmallOut());
                                        break;
                                    case Constant.DEPEND_ON_BIG:
                                        generateDependOnFile(ff,model,combination,ActivityEnum.DEPEND_ON.getBigOut());
                                        break;
                                    case Constant.DEPEND_ON_SMALL:
                                        generateDependOnFile(ff,model,combination,ActivityEnum.DEPEND_ON.getSmallOut());
                                        break;
                                    default:
                                        break;
                                }
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

    private void generateDependOnFile(File ff,Model model,String acom, String fileName) throws Exception {
        //dependencies
        List<Dependency> dependencies = model.getDependencies();
        Properties properties = model.getProperties();
        Set<String> strings = new HashSet<>();

        Optional<List<Dependency>> dopt = Optional.ofNullable(dependencies);
        if(dopt.isPresent()&& dependencies.size()>0){
            List<Map<String, Object>> datas = new ArrayList<>();
            for (Dependency d:dependencies) {
                String groupId = d.getGroupId();
                String artifactId = d.getArtifactId();
                String version = d.getVersion();

                if (version != null) {
                    if (version.contains("$")) {//this means version should be found in properties
                        String key = version.replaceAll("\\$", "").replaceAll("\\{", "").replaceAll("\\}", "");
                        if (key.equalsIgnoreCase("project.version")) {
                            version = model.getVersion();
                        }else{
                            version = (String) properties.get(key);
                        }
                    }
                }else {
                    String absolutePath = ff.getAbsolutePath();
                    if (strings.add(absolutePath)) {
                        System.out.println(absolutePath);
                    }
                    /*DependencyManagement management = model.getDependencyManagement();
                    List<Dependency> dependencyList = management.getDependencies();*/
                }

                String dcom = groupId + artifactId + version;
                Map<String,Object> map = new HashMap<>();
                map.put("from",acom);
                map.put("to",dcom);
                map.put("groupId", groupId);
                map.put("artifactId", artifactId);
                map.put("version", "s_"+version);
                datas.add(map);
            }

            String[] displayColNames = {"from","to","groupId","artifactId","version"};
            String[] fieldNames = {"from","to","groupId","artifactId","version"};
            WriteCsv.writeCvs(fileName, datas, displayColNames, fieldNames, true, false);

        }
    }

    public void generateArtifactFile(String version,String artifactId,File ff,String groupId,String fileName) throws Exception {
        /*If the value of artifact or version is taken from another tag, such as the properties in the profile,
                                        or directly taken from the properties, then the value of artifact or version is directly determined from the folder path*/
        File parent = ff.getParentFile();
        Optional<File> opt = Optional.ofNullable(parent);
        if(version.contains("$")&&opt.isPresent()){
            version = parent.getName();
        }
        Optional<File> opt1 = Optional.ofNullable(Objects.requireNonNull(parent).getParentFile());
        if (artifactId.contains("$")&&opt1.isPresent()){
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
