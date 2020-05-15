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


    @Query("MATCH (a:Artifact) RETURN a ")
    List<Artifact> getAllByArtifactId(String artifactId);

}
