package uk.ac.newcastle.redhat.gavgraph;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.newcastle.redhat.gavgraph.domain.Artifact;
import uk.ac.newcastle.redhat.gavgraph.repository.ArtifactRepository;
import uk.ac.newcastle.redhat.gavgraph.service.ArtifactService;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

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
        Artifact log4j2 = new Artifact(
                "org.springframework.boot",
                "spring-boot-starter-log4j2",
                "2.2.6.release");
        log4j2.dependOn("org.apache.logging.log4j",
                "log4j-core",
                "2.12.1");
        //artifactRepository.save(log4j2);
        artifactService.addArtifact(log4j2);
        List<Artifact> allByArtifactId = artifactRepository.getAllByArtifactId("spring-boot-starter-log4j2");
        allByArtifactId.forEach(System.err::println);
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
                });

    }

    @Test
    public void testSaveDataDirectlyToDB() throws IOException, XmlPullParserException {
        File pom = new File("src\\main\\resources\\static\\xml\\hibernate-2.1.8-atlassian-34.pom");
        MavenXpp3Reader pomReader = new MavenXpp3Reader();
        Model model = pomReader.read(new FileReader(pom));
        //System.out.println(model.toString());
        model.getGroupId();
        model.getArtifactId();
        model.getVersion();

        model.getParent();

        model.getLicenses();

        model.getOrganization();

        List<Dependency> dependencies = model.getDependencies();
        dependencies.forEach(dependency -> {

        });

        Boolean availability = true;



    }
}
