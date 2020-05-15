package uk.ac.newcastle.redhat.gavgraph.common;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.junit.Test;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.springframework.data.neo4j.repository.query.GraphRepositoryQuery;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Xpp3Demo {

    private Logger log = LogManager.getLogger(Xpp3Demo.class);

    @Test
    public void testXpp3Read() throws Exception {
        //File pom = new File("src\\main\\resources\\static\\xml\\pom.xml");
        File pom = new File("src\\main\\resources\\static\\xml\\hibernate-2.1.8-atlassian-34.pom");
        //File pom = new File("src\\main\\resources\\static\\xml\\antlr-2.7.7.redhat-7.pom");
        MavenXpp3Reader pomReader = new MavenXpp3Reader();
        Model model = pomReader.read(new FileReader(pom));

        log.info("Parent:{}",model.getParent());
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