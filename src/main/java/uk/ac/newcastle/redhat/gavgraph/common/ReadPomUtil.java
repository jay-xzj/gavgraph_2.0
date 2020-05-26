package uk.ac.newcastle.redhat.gavgraph.common;

import org.apache.maven.model.*;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class ReadPomUtil {

    /**
     * model==antlr:antlr:jar:2.7.7.redhat-7
     * @param fileName
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    public String getCoordinate(String fileName) throws IOException, XmlPullParserException {
        File pom = new File(fileName);
        MavenXpp3Reader pomReader = new MavenXpp3Reader();
        Model model = pomReader.read(new FileReader(pom));
        //ifnotnull
        return model.getGroupId()+":"+model.getArtifactId()+":"+model.getVersion();
    }

    public static Map<String,String> getDependencyRelationships(String fileName) throws IOException, XmlPullParserException {
        File pom = new File(fileName);
        MavenXpp3Reader pomReader = new MavenXpp3Reader();
        Model model = pomReader.read(new FileReader(pom));
        String ms = model.toString();
        List<Dependency> dependencies = model.getDependencies();
        Map<String, String> map = new HashMap<>();
        //dependencies.forEach(dependency -> map.put(ms,));
        dependencies.forEach(System.out::println);
        List<Developer> developers = model.getDevelopers();
        developers.forEach(System.out::println);
        return null;
    }

    public static void main(String[] args) throws IOException, XmlPullParserException {
        //Map<String, String> map = getDependencyRelationships("src\\main\\resources\\static\\xml\\hibernate-2.1.8-atlassian-34.pom");
        MavenXpp3Reader pomReader = new MavenXpp3Reader();
        Model model = pomReader.read(new FileReader("src\\main\\resources\\static\\xml\\hibernate-2.1.8-atlassian-34.pom"));
        String ms = model.toString();

        //dependencies
        List<Dependency> dependencies = model.getDependencies();
        dependencies.forEach(dependency -> System.out.println("dependency_gav:::"+dependency.getGroupId()+":"+dependency.getArtifactId()+":"+dependency.getVersion()));

        //licenses
        List<License> licenses = model.getLicenses();
        licenses.forEach(license -> System.err.println("LICENSE==="+license.getName()+" : "+license.getUrl()));

        //developers
        List<Developer> developers = model.getDevelopers();
        System.out.println(developers);
        developers.forEach(developer -> System.out.println("developer==="+developer.getId()+developer.getEmail()+developer.getName()));

        //parent
        Parent parent = model.getParent();
        Optional<Parent> parentOpt = Optional.ofNullable(parent);//不确定parent是否为null
        String groupIdOrElse = parentOpt.map(Parent::getGroupId).orElse(null);
        String artifactIdOrElse = parentOpt.map(Parent::getArtifactId).orElse(null);
        String versionOrElse = parentOpt.map(Parent::getVersion).orElse(null);

        //System.out.println("parent___"+parentOpt.map(uk.ac.newcastle.redhat.gavgraph.domain.Parent::getGroupId)+":"+parent.getArtifactId()+":"+parent.getV ersion());

        //organization
        Optional<Organization> organizationOpt = Optional.ofNullable(model.getOrganization());
        String name = organizationOpt.map(Organization::getName).orElse(null);
        String url = organizationOpt.map(Organization::getUrl).orElse(null);
        System.out.println(name + " : " + url);
    }

}
