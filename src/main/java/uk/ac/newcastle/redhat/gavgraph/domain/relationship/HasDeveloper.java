package uk.ac.newcastle.redhat.gavgraph.domain.relationship;

import org.neo4j.ogm.annotation.*;
import uk.ac.newcastle.redhat.gavgraph.domain.Artifact;
import uk.ac.newcastle.redhat.gavgraph.domain.Developer;
import uk.ac.newcastle.redhat.gavgraph.domain.License;

@RelationshipEntity("HAS_DEVELOPER")
public class HasDeveloper {

    @Id
    @GeneratedValue
    Long id;

    @StartNode
    Artifact artifact;
    @EndNode
    Developer developer;

    public Long getId() {
        return id;
    }

    public Artifact getArtifact() {
        return artifact;
    }

    public Developer getDeveloper() {
        return developer;
    }
}
