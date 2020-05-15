package uk.ac.newcastle.redhat.gavgraph.domain.relationship;

import org.neo4j.ogm.annotation.*;
import uk.ac.newcastle.redhat.gavgraph.domain.Artifact;
import uk.ac.newcastle.redhat.gavgraph.domain.Parent;

@RelationshipEntity("HAS_PARENT")
public class HasParent {

    @Id
    @GeneratedValue
    Long id;

    @StartNode
    Artifact artifact;
    @EndNode
    Parent parent;

    public Long getId() {
        return id;
    }

    public Artifact getArtifact() {
        return artifact;
    }

    public Parent getParent() {
        return parent;
    }
}
