package uk.ac.newcastle.redhat.gavgraph.domain.relationship;


import org.neo4j.ogm.annotation.*;
import uk.ac.newcastle.redhat.gavgraph.domain.Artifact;

@RelationshipEntity("HAS_DEPENDENCY")
public class HasDependency {

    @Id
    @GeneratedValue
    Long id;

    @StartNode
    Artifact startArtifact;
    @EndNode
    Artifact endArtifact;

    public Long getId() {
        return id;
    }

    public Artifact getStartArtifact(){
        return startArtifact;
    }
    public Artifact getEndArtifact(){
        return endArtifact;
    }
}
