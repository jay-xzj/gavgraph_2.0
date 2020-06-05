package uk.ac.newcastle.redhat.gavgraph.common.pom.read;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ReadTask {

    private int id;
    private final String path;

    public ReadTask(int id, String path) {
        this.id = id;
        this.path = path;
    }

    public int getId() {
        return id;
    }

    void execute(){
        //System.out.println("当前线程是："+Thread.currentThread().getName());
        Model pomModel = null;
        try {
            File pom = new File(path);
            MavenXpp3Reader pomReader = new MavenXpp3Reader();
            FileReader fileReader = new FileReader(pom);
            pomModel = pomReader.read(fileReader);
            fileReader.close();
            System.err.println("PATH === "+path+" GAV INFO === "+ pomModel.getArtifactId() +" : "+pomModel.getGroupId() +" : "+pomModel.getVersion());
        } catch (IOException | org.codehaus.plexus.util.xml.pull.XmlPullParserException xe) {

            System.out.println("当前pom的path："+path);
            xe.printStackTrace();
        } finally {
            pomModel = null;
        }
    }
}
