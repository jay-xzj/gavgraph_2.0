package uk.ac.newcastle.redhat.gavgraph.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import uk.ac.newcastle.redhat.gavgraph.domain.Parent;

import java.util.List;

public interface ParentRepository extends Neo4jRepository<Parent,Long> {


    List<Parent> findByGroupId(String groupId);

    List<Parent> findAllByGroupIdContains(String groupId);

    List<Parent> findByArtifactId(String artifactId);

    List<Parent> findAllByArtifactIdContains(String artifactId);
}
