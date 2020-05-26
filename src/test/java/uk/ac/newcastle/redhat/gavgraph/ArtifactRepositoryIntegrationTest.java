package uk.ac.newcastle.redhat.gavgraph;

import org.apache.maven.model.*;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.newcastle.redhat.gavgraph.domain.Artifact;
import uk.ac.newcastle.redhat.gavgraph.repository.ArtifactRepository;
import uk.ac.newcastle.redhat.gavgraph.service.ArtifactService;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test demonstrating the use of ArtifactRepository
 *
 * @author jayxu
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ArtifactRepositoryIntegrationTest {

    @SpringBootApplication
    static class ExampleConfig {
    }

    @Resource
    ArtifactRepository artifactRepository;

    @Resource
    private ArtifactService artifactService;

    @Test
    public void testSaveAndLoadArtifact(){
        /*Artifact log4j2 = new Artifact(
                "org.springframework.boot",
                "spring-boot-starter-log4j2",
                "2.2.6.release");
        log4j2.dependOn("org.apache.logging.log4j",
                "log4j-core",
                "2.12.1");
        //artifactRepository.save(log4j2);
        artifactService.addArtifact(log4j2);
        List<Artifact> allByArtifactId = artifactRepository.getAllByArtifactId("spring-boot-starter-log4j2");
        allByArtifactId.forEach(System.out::println);
        assertThat(artifactRepository.findById(log4j2.getId()))
                .hasValueSatisfying(artifact -> {
                    assertThat(artifact.getGroupId()).isEqualTo(log4j2.getGroupId());
                    assertThat(artifact.getArtifactId()).isEqualTo(log4j2.getArtifactId());
                    assertThat(artifact.getVersion()).isEqualTo(log4j2.getVersion());
                    assertThat(artifact.getDependencies()).hasSize(1).first()
                            .satisfies(dependency->{
                                assertThat(dependency.getGroupId()).isEqualTo("org.apache.logging.log4j");
                                assertThat(dependency.getArtifactId()).isEqualTo("log4j-core");
                                assertThat(dependency.getVersion()).isEqualTo("2.12.1");
                            });
                });*/

    }

    @Test
    public void testSaveDataDirectlyToDB() throws IOException, XmlPullParserException {
        File pom = new File("src\\main\\resources\\static\\xml\\hibernate-2.1.8-atlassian-34.pom");
        MavenXpp3Reader pomReader = new MavenXpp3Reader();
        Model model = pomReader.read(new FileReader(pom));
        //System.out.println(model.toString());

        Artifact bean = new Artifact();

        String groupId = model.getGroupId();
        String artifactId = model.getArtifactId();
        String version = model.getVersion();

        //dependencies
        List<Dependency> dependencies = model.getDependencies();
        dependencies.forEach(dependency -> System.out.println("dependency_gav:::" + dependency.getGroupId() + ":"+dependency.getArtifactId() + ":" + dependency.getVersion()));

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

       /* bean.setArtifactId(artifactId);
        bean.setGroupId(groupId);
        bean.setVersion(version);*/

        /*bean.setDependencies(dependencies);
        bean.setDevelopers(developers);
        bean.setHasDependencies();*/



        artifactRepository.save(bean);




    }
}
