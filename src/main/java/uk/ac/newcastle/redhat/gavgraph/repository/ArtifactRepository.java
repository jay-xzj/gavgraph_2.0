package uk.ac.newcastle.redhat.gavgraph.repository;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import uk.ac.newcastle.redhat.gavgraph.domain.Artifact;

import java.util.List;

public interface ArtifactRepository extends Neo4jRepository<Artifact, Long> {

    /**
     * Nested property from select from roles -> movie -> title,
     * where this here represents the start node in a
     * relationship and movie the end node.
     *
     * @param artifactId
     * @return
     */
    List<Artifact> findAllByArtifactIdAndGroupId(String artifactId);


    List<Artifact> findAllDependenciesByArtifactId(String artifactId);


    /*@Query("MATCH (a:Artifact) RETURN a ")
    List<Artifact> getAllByArtifactId(String artifactId);*/

    //@Query("MATCH (a:Artifact) WHERE a.groupId = $groupId RETURN a")
    List<Artifact> getAllByGroupId(String groupId);

    //@Query("MATCH (a:Artifact) WHERE a.artifactId = $artifactId RETURN a")
    List<Artifact> getAllByArtifactId(String artifactId);

    //@Query("MATCH (a:Artifact) WHERE a.artifactId CONTAINS $artifactId RETURN a")
    List<Artifact> getAllByArtifactIdContains(String artifactId);

    //@Query("MATCH (a:Artifact) WHERE a.groupId CONTAINS $groupId RETURN a")
    List<Artifact> getALlByGroupIdContains(String groupId);
}
